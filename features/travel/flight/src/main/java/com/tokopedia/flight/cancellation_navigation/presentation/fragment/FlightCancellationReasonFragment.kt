package com.tokopedia.flight.cancellation_navigation.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.activity.FlightCancellationReasonActivity
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapter
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.bottomsheet.FlightCancellationViewImageDialogFragment
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationReasonViewModel
import com.tokopedia.flight.cancellation_navigation.presentation.bottomsheet.FlightCancellationChooseReasonBottomSheet
import com.tokopedia.flight.cancellation_navigation.presentation.bottomsheet.FlightCancellationChooseReasonBottomSheet.Companion.TAG_CANCELLATION_CHOOSE_REASON
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationReviewFragment.Companion.EXTRA_CANCEL_WRAPPER
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationReviewFragment.Companion.EXTRA_INVOICE_ID
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
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            cancellationReasonViewModel = viewModelProvider.get(FlightCancellationReasonViewModel::class.java)

            arguments?.let {
                cancellationReasonViewModel.cancellationWrapperModel = it.getParcelable(FlightCancellationReasonActivity.EXTRA_CANCELLATION_MODEL)
                        ?: FlightCancellationWrapperModel()
            }
            if (cancellationReasonViewModel.getAttachments().isEmpty()) {
                cancellationReasonViewModel.buildAttachmentList()
                cancellationReasonViewModel.buildViewAttachmentList(DEFAULT_DOC_TYPE_ID)
            } else {
                cancellationReasonViewModel.buildViewAttachmentList(DEFAULT_DOC_TYPE_ID)
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
            if (it.first && it.second) {
                cancellationReasonViewModel.trackOnNext()
                cancellationReasonViewModel.disableNextButtonNotifyState()
                navigateToReviewPage()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
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
            val bottomsheet = FlightCancellationChooseReasonBottomSheet.getInstance(cancellationReasonViewModel.selectedReason)
            bottomsheet.listener = object : FlightCancellationChooseReasonBottomSheet.FlightChooseReasonListener {
                override fun onReasonChoosed(selectedReason: FlightCancellationPassengerEntity.Reason) {
                    cancellationReasonViewModel.selectedReason = selectedReason
                    renderSelectedReason()
                    setupNextButton()
                }
            }
            bottomsheet.setShowListener { bottomsheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
            bottomsheet.show(childFragmentManager, TAG_CANCELLATION_CHOOSE_REASON)
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
        dialogFragment.show(childFragmentManager, TAG_DIALOG_FRAGMENT)
    }

    private fun navigateToReviewPage() {
        view?.let {
            it.findNavController().navigate(R.id.action_flightCancellationReason_to_cancellationReviewFragment,
                    Bundle().apply {
                        putString(EXTRA_INVOICE_ID, cancellationReasonViewModel.cancellationWrapperModel.invoiceId)
                        putParcelable(EXTRA_CANCEL_WRAPPER, cancellationReasonViewModel.cancellationWrapperModel)
                    })
        }
    }

    companion object {
        const val EXTRA_CANCELLATION_MODEL = "EXTRA_CANCELLATION_VIEW_MODEL"

        private const val TAG_DIALOG_FRAGMENT = "TAG_DIALOG_FRAGMENT"

        private const val REQUEST_CODE_IMAGE = 1001

        private const val DEFAULT_DOC_TYPE_ID = 0

        fun newInstance(cancellationWrapperModel: FlightCancellationWrapperModel): FlightCancellationReasonFragment =
                FlightCancellationReasonFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_CANCELLATION_MODEL, cancellationWrapperModel)
                    }
                }
    }
}