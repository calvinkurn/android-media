package com.tokopedia.flight.cancellation_navigation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationEstimateEntity
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.activity.FlightCancellationReviewActivity
import com.tokopedia.flight.cancellation.presentation.activity.FlightCancellationTermsAndConditionsActivity
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapter
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationReviewAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationReviewEstimationNotesAdapter
import com.tokopedia.flight.cancellation.presentation.bottomsheet.FlightCancellationRefundBottomSheet
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationReviewViewModel
import com.tokopedia.flight.common.util.FlightErrorUtil
import com.tokopedia.flight.databinding.FragmentFlightCancellationReviewBinding
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewFragment : BaseListFragment<FlightCancellationModel, FlightCancellationReviewAdapterTypeFactory>(),
        FlightCancellationAttachmentAdapterTypeFactory.AdapterInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightCancellationReviewViewModel: FlightCancellationReviewViewModel

    private lateinit var attachmentAdapter: FlightCancellationAttachmentAdapter
    private lateinit var estimationNotesAdapter: FlightCancellationReviewEstimationNotesAdapter

    private var binding by autoClearedNullable<FragmentFlightCancellationReviewBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightCancellationReviewViewModel = viewModelProvider.get(FlightCancellationReviewViewModel::class.java)

            arguments?.let {
                if (it.containsKey(FlightCancellationReviewActivity.EXTRA_INVOICE_ID))
                    flightCancellationReviewViewModel.invoiceId = it.getString(FlightCancellationReviewActivity.EXTRA_INVOICE_ID)
                            ?: ""
                if (it.containsKey(FlightCancellationReviewActivity.EXTRA_CANCEL_WRAPPER))
                    flightCancellationReviewViewModel.cancellationWrapperModel = it.getParcelable(FlightCancellationReviewActivity.EXTRA_CANCEL_WRAPPER)
                            ?: FlightCancellationWrapperModel()
            }

            flightCancellationReviewViewModel.onInit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFlightCancellationReviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvDescriptionRefund?.text = descriptionText()
        binding?.tvDescriptionRefund?.movementMethod = LinkMovementMethod.getInstance()
        binding?.buttonSubmit?.setOnClickListener {
            navigateToTermsAndConditionsPage()
        }

        val adapterTypeFactory = FlightCancellationAttachmentAdapterTypeFactory(this, false)
        attachmentAdapter = FlightCancellationAttachmentAdapter(adapterTypeFactory)
        binding?.rvAttachments?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding?.rvAttachments?.setHasFixedSize(true)
        binding?.rvAttachments?.isNestedScrollingEnabled = false
        binding?.rvAttachments?.adapter = attachmentAdapter

        showLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightCancellationReviewViewModel.estimateRefundFinish.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderView()
                    renderRefundableView(it.data)
                }
                is Fail -> {
                    val errorData = FlightErrorUtil.getErrorIdAndTitleFromFlightError(requireContext(), it.throwable)
                    showErrorFetchEstimateRefund(errorData.first, errorData.second)
                }
            }
        })

        flightCancellationReviewViewModel.requestCancel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    flightCancellationReviewViewModel.trackOnSubmit()
                    if (it.data) showSuccessDialog(R.string.flight_cancellation_review_dialog_non_refundable_success_description)
                }
                is Fail -> {
                    showCancellationError(it.throwable)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CANCELLATION_TNC -> {
                if (resultCode == Activity.RESULT_OK) flightCancellationReviewViewModel.requestCancellation()
            }
        }
    }

    override fun getAdapterTypeFactory(): FlightCancellationReviewAdapterTypeFactory =
            FlightCancellationReviewAdapterTypeFactory()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightCancellationComponent::class.java).inject(this)
    }

    override fun onItemClicked(t: FlightCancellationModel?) {}

    override fun loadData(page: Int) {}

    override fun onUploadAttachmentButtonClicked(position: Int) {}

    override fun viewImage(filePath: String) {}

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun showLoading() {
        binding?.svReviewContainer?.visibility = View.GONE
        binding?.fullPageLoading?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding?.svReviewContainer?.visibility = View.VISIBLE
        binding?.fullPageLoading?.visibility = View.GONE
    }

    private fun renderView() {
        val cancellationModel = flightCancellationReviewViewModel.cancellationWrapperModel

        adapter.clearAllElements()
        renderList(cancellationModel.cancellationList)

        if (cancellationModel.cancellationReasonAndAttachmentModel.reason.isNotEmpty()) {
            binding?.txtCancellationReason?.text = cancellationModel.cancellationReasonAndAttachmentModel.reason
            binding?.containerAdditionalReason?.visibility = View.VISIBLE
        } else {
            binding?.containerAdditionalReason?.visibility = View.GONE
        }

        if (flightCancellationReviewViewModel.shouldShowAttachments()) {
            attachmentAdapter.clearAllElements()
            attachmentAdapter.addElement(cancellationModel.cancellationReasonAndAttachmentModel.attachmentList)
        } else {
            binding?.containerAdditionalDocuments?.visibility = View.GONE
        }

        if (cancellationModel.cancellationReasonAndAttachmentModel.reason.isEmpty() ||
                cancellationModel.cancellationReasonAndAttachmentModel.attachmentList.size == 0) {
            binding?.containerAdditionalData?.visibility = View.GONE
        }

        binding?.tvTotalRefund?.text = cancellationModel.cancellationReasonAndAttachmentModel.estimateFmt
    }

    private fun renderRefundableView(data: FlightCancellationEstimateEntity) {
        if (flightCancellationReviewViewModel.isRefundable()) {
            if (flightCancellationReviewViewModel.cancellationWrapperModel.cancellationReasonAndAttachmentModel.showEstimateRefund) {
                showEstimateValue(data.estimationExistsPolicy)
                hideRefundDetail()
            } else {
                hideEstimateValue()
                showRefundDetail(data.estimationNotExistPolicy.joinToString(separator = "\n"))
            }
        } else {
            hideEstimateValue()
            showRefundDetail(data.nonRefundableText)
        }
    }

    private fun descriptionText(): SpannableString {
        val color = requireContext().resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN600)
        val startIndex = getString(R.string.flight_cancellation_refund_description).indexOf(LEARN_TEXT)
        val stopIndex = getString(R.string.flight_cancellation_refund_description).length
        val description = SpannableString(requireContext().getString(
                R.string.flight_cancellation_refund_description))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val bottomSheet = FlightCancellationRefundBottomSheet()
                bottomSheet.show(childFragmentManager, getString(R.string.flight_cancellation_refund_bottom_sheet_tag))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
        }
        description.setSpan(clickableSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return description
    }

    private fun navigateToTermsAndConditionsPage() {
        startActivityForResult(FlightCancellationTermsAndConditionsActivity.createIntent(requireContext()),
                REQUEST_CANCELLATION_TNC)
    }

    private fun closeCancellationReviewPage() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun showErrorFetchEstimateRefund(errorId: Int, message: String) {
        NetworkErrorHelper.showEmptyState(requireContext(),
                requireView(),
                message
        ) {
            if (errorId == ERROR_ID_NO_MORE_ADULT) {
                val intent = Intent().also {
                    it.putExtra(EXTRA_CANCELLATION_ERROR, true)
                }
                requireActivity().setResult(Activity.RESULT_CANCELED, intent)
                requireActivity().finish()
            } else {
                flightCancellationReviewViewModel.fetchRefundEstimation()
            }
        }
    }

    private fun showSuccessDialog(resId: Int) {
        val dialog = DialogUnify(requireContext(), DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.flight_cancellation_review_dialog_success_title))
        dialog.setDescription(Html.fromHtml(getString(resId)))
        dialog.setPrimaryCTAText("OK")
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            closeCancellationReviewPage()
        }
        dialog.show()
    }

    private fun showEstimateValue(estimationNotes: List<String>) {
        binding?.containerEstimateRefund?.visibility = View.VISIBLE

        if (estimationNotes.isNotEmpty()) {
            binding?.tvRefundStar?.visibility = View.VISIBLE
        } else {
            binding?.tvRefundStar?.visibility = View.GONE
        }

        if (!::estimationNotesAdapter.isInitialized) {
            estimationNotesAdapter = FlightCancellationReviewEstimationNotesAdapter()
        }
        estimationNotesAdapter.setData(estimationNotes)

        binding?.rvEstimationNotes?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding?.rvEstimationNotes?.setHasFixedSize(true)
        binding?.rvEstimationNotes?.adapter = estimationNotesAdapter
        binding?.rvEstimationNotes?.visibility = View.VISIBLE
    }

    private fun hideEstimateValue() {
        binding?.containerEstimateRefund?.visibility = View.GONE
        binding?.rvEstimationNotes?.visibility = View.GONE
    }

    private fun showRefundDetail(message: String) {
        binding?.tvRefundDetail?.text = message
        binding?.tvRefundDetail?.visibility = View.VISIBLE
    }

    private fun hideRefundDetail() {
        binding?.tvRefundDetail?.visibility = View.GONE
    }

    private fun showCancellationError(t: Throwable) {
        Toaster.build(requireView(),
                FlightErrorUtil.getErrorIdAndTitleFromFlightError(requireContext(), t).second,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR).show()
    }

    companion object {

        const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        const val EXTRA_CANCEL_WRAPPER = "EXTRA_CANCEL_WRAPPER"

        const val EXTRA_CANCELLATION_ERROR = "EXTRA_CANCELLATION_ERROR"

        private const val REQUEST_CANCELLATION_TNC = 1

        private const val ERROR_ID_NO_MORE_ADULT = 165

        private const val LEARN_TEXT = "Pelajari"
    }
}