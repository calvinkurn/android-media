package com.tokopedia.flight.cancellation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.activity.FlightCancellationChooseReasonActivity
import com.tokopedia.flight.cancellation.presentation.activity.FlightCancellationReasonActivity
import com.tokopedia.flight.cancellation.presentation.activity.FlightCancellationReviewActivity
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapter
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.bottomsheet.FlightCancellationViewImageDialogFragment
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationReasonViewModel
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationReasonFragment.Companion.EXTRA_CANCELLATION_MODEL
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_cancellation_refundable_step_two.*
import javax.inject.Inject

/**
 * @author by furqan on 17/07/2020
 */
class FlightCancellationReasonFragment : BaseDaggerFragment(),
        FlightCancellationAttachmentAdapterTypeFactory.AdapterInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cancellationReasonViewModel: FlightCancellationReasonViewModel

    private lateinit var adapter: FlightCancellationAttachmentAdapter

    override fun getScreenName(): String = FlightAnalytics.Screen.FLIGHT_CANCELLATION_STEP_TWO

    override fun initInjector() {
        getComponent(FlightCancellationComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationReasonViewModel = viewModelProvider.get(FlightCancellationReasonViewModel::class.java)

            arguments?.let {
                cancellationReasonViewModel.cancellationWrapperModel = it.getParcelable(FlightCancellationReasonActivity.EXTRA_CANCELLATION_MODEL)
                        ?: FlightCancellationWrapperModel()
            }
            if (cancellationReasonViewModel.getAttachments().isEmpty()) {
                cancellationReasonViewModel.buildAttachmentList()
                cancellationReasonViewModel.buildViewAttachmentList(0)
            } else {
                cancellationReasonViewModel.buildViewAttachmentList(0)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_cancellation_refundable_step_two, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationReasonViewModel.viewAttachmentModelList.observe(viewLifecycleOwner, Observer {
            if (it.size > 0) {
                renderAttachments(it)
            }
        })

        cancellationReasonViewModel.attachmentErrorStringRes.observe(viewLifecycleOwner, Observer {
            if (it != FlightCancellationReasonViewModel.DEFAULT_STRING_RES_ERROR) {
                showErrorSnackbar(it)
            }
        })

        cancellationReasonViewModel.attachmentErrorString.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    try {
                        hideProgressBar()
                        showErrorSnackbar(String.format(getString(it.data.first), it.data.second))
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }
                is Fail -> {
                    hideProgressBar()
                    showErrorSnackbar(ErrorHandler.getErrorMessage(requireContext(), it.throwable))
                }
            }
        })

        cancellationReasonViewModel.canNavigateToNextStep.observe(viewLifecycleOwner, Observer {
            hideProgressBar()
            if (it.first) {
                cancellationReasonViewModel.trackOnNext()
                navigateToReviewPage()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_CHOOSE_REASON -> {
                data?.let {
                    cancellationReasonViewModel.selectedReason = it.getParcelableExtra(FlightCancellationChooseReasonFragment.EXTRA_SELECTED_REASON)
                    renderSelectedReason()
                    setupNextButton()
                }
            }
            REQUEST_CODE_IMAGE -> {
                data?.let {
                    val imagePathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
                    if (imagePathList.isEmpty()) {
                        return
                    }

                    val imagePath = imagePathList[0]
                    if (imagePath.isNotEmpty()) {
                        cancellationReasonViewModel.onSuccessChangeAttachment(imagePath)
                    }
                    setupNextButton()
                }
            }
            REQUEST_CODE_REVIEW -> {
                if (resultCode == Activity.RESULT_OK) {
                    closeReasonPage()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    data?.extras?.let {
                        if (it.containsKey(FlightCancellationReviewFragment.EXTRA_CANCELLATION_ERROR) &&
                                it.getBoolean(FlightCancellationReviewFragment.EXTRA_CANCELLATION_ERROR)) {
                            requireActivity().setResult(Activity.RESULT_CANCELED, data)
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    override fun onUploadAttachmentButtonClicked(position: Int) {
        cancellationReasonViewModel.editedAttachmentPosition = position
        val imagePickerBuilder = ImagePickerBuilder.getOriginalImageBuilder(requireContext())
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(imagePickerBuilder)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    override fun viewImage(filePath: String) {
        showImageInFragment(filePath)
    }

    private fun buildView() {
        til_saved_passenger.textFieldInput.isClickable = true
        til_saved_passenger.textFieldInput.isFocusable = false
        til_saved_passenger.textFieldInput.isSingleLine = true
        til_saved_passenger.textFieldInput.setOnClickListener {
            startActivityForResult(FlightCancellationChooseReasonActivity.getCallingIntent(requireContext(), cancellationReasonViewModel.selectedReason),
                    REQUEST_CODE_CHOOSE_REASON)
            requireActivity().overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_slide_up_in, com.tokopedia.common.travel.R.anim.travel_anim_stay)
        }

        val adapterTypeFactory = FlightCancellationAttachmentAdapterTypeFactory(this, true)
        adapter = FlightCancellationAttachmentAdapter(adapterTypeFactory)
        rv_attachments.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_attachments.setHasFixedSize(true)
        rv_attachments.isNestedScrollingEnabled = false
        rv_attachments.adapter = adapter

        btn_next.setOnClickListener {
            showProgressBar()
            cancellationReasonViewModel.onNextButtonClicked()
        }

        buildAttachmentReasonView()
        hideProgressBar()
        setupNextButton()
    }

    private fun showProgressBar() {
        container.visibility = View.GONE
        btn_next.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        container.visibility = View.VISIBLE
        btn_next.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    private fun renderSelectedReason() {
        cancellationReasonViewModel.selectedReason?.let {
            til_saved_passenger.textFieldInput.setText(it.title)
            deleteAllAttachments()
            if (it.formattedRequiredDocs.size > 0) {
                cancellationReasonViewModel.buildViewAttachmentList(it.formattedRequiredDocs[0].id.toInt())
            } else {
                cancellationReasonViewModel.buildViewAttachmentList(0)
            }
            buildAttachmentReasonView()
        }
    }

    private fun deleteAllAttachments() {
        adapter.clearAllElements()
    }

    private fun buildAttachmentReasonView() {
        val selectedReason = cancellationReasonViewModel.selectedReason
        val attachments = cancellationReasonViewModel.getAttachments()
        val viewAttachments = cancellationReasonViewModel.viewAttachmentModelList.value
        if (selectedReason != null && selectedReason.formattedRequiredDocs.size > 0 &&
                attachments.isNotEmpty() && viewAttachments != null && viewAttachments.size > 0) {
            showAttachmentContainer()
        } else {
            hideAttachmentContainer()
        }
    }

    private fun showAttachmentContainer() {
        attachment_container.visibility = View.VISIBLE
    }

    private fun hideAttachmentContainer() {
        attachment_container.visibility = View.GONE
    }

    private fun renderAttachments(attachmentList: MutableList<FlightCancellationAttachmentModel>) {
        adapter.clearAllElements()
        adapter.addElement(attachmentList)
        adapter.notifyDataSetChanged()
    }

    private fun setupNextButton() {
        var shouldEnabledNextButton = true

        if (cancellationReasonViewModel.selectedReason == null) {
            shouldEnabledNextButton = false
        } else if (cancellationReasonViewModel.selectedReason!!.formattedRequiredDocs.size > 0) {
            cancellationReasonViewModel.viewAttachmentModelList.value?.let {
                for (attachment in it) {
                    if (attachment.filename.isEmpty() || attachment.filepath.isEmpty()) {
                        shouldEnabledNextButton = false
                        break
                    }
                }
            }
        }

        btn_next.isEnabled = shouldEnabledNextButton
    }

    private fun showErrorSnackbar(resId: Int) {
        Toaster.build(requireView(), getString(resId), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun showErrorSnackbar(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun showImageInFragment(filePath: String) {
        val dialogFragment = FlightCancellationViewImageDialogFragment.newInstance(filePath)
        dialogFragment.show(requireFragmentManager(), TAG_DIALOG_FRAGMENT)
    }

    private fun navigateToReviewPage() {
        startActivityForResult(FlightCancellationReviewActivity.getCallingIntent(
                requireContext(),
                cancellationReasonViewModel.cancellationWrapperModel.invoiceId,
                cancellationReasonViewModel.cancellationWrapperModel),
                REQUEST_CODE_REVIEW)
    }

    private fun closeReasonPage() {
        requireActivity().setResult(Activity.RESULT_OK)
        requireActivity().finish()
    }

    companion object {

        private const val TAG_DIALOG_FRAGMENT = "TAG_DIALOG_FRAGMENT"

        private const val REQUEST_CODE_IMAGE = 1001
        private const val REQUEST_CODE_CHOOSE_REASON = 1111
        private const val REQUEST_CODE_REVIEW = 2112

        fun newInstance(cancellationWrapperModel: FlightCancellationWrapperModel): FlightCancellationReasonFragment =
                FlightCancellationReasonFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_CANCELLATION_MODEL, cancellationWrapperModel)
                    }
                }
    }
}