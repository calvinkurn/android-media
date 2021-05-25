package com.tokopedia.flight.cancellation.presentation.fragment

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
import com.tokopedia.flight.orderlist.util.FlightErrorUtil
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_cancellation_review.*
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_cancellation_review, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_description_refund.text = descriptionText()
        tv_description_refund.movementMethod = LinkMovementMethod.getInstance()
        button_submit.setOnClickListener {
            navigateToTermsAndConditionsPage()
        }

        val adapterTypeFactory = FlightCancellationAttachmentAdapterTypeFactory(this, false)
        attachmentAdapter = FlightCancellationAttachmentAdapter(adapterTypeFactory)
        rv_attachments.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_attachments.setHasFixedSize(true)
        rv_attachments.isNestedScrollingEnabled = false
        rv_attachments.adapter = attachmentAdapter

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
        sv_review_container.visibility = View.GONE
        full_page_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        sv_review_container.visibility = View.VISIBLE
        full_page_loading.visibility = View.GONE
    }

    private fun renderView() {
        val cancellationModel = flightCancellationReviewViewModel.cancellationWrapperModel

        renderList(cancellationModel.cancellationList)

        if (cancellationModel.cancellationReasonAndAttachmentModel.reason.isNotEmpty()) {
            txt_cancellation_reason.text = cancellationModel.cancellationReasonAndAttachmentModel.reason
            container_additional_reason.visibility = View.VISIBLE
        } else {
            container_additional_reason.visibility = View.GONE
        }

        if (flightCancellationReviewViewModel.shouldShowAttachments()) {
            attachmentAdapter.clearAllElements()
            attachmentAdapter.addElement(cancellationModel.cancellationReasonAndAttachmentModel.attachmentList)
        } else {
            container_additional_documents.visibility = View.GONE
        }

        if (cancellationModel.cancellationReasonAndAttachmentModel.reason.isEmpty() ||
                cancellationModel.cancellationReasonAndAttachmentModel.attachmentList.size == 0) {
            container_additional_data.visibility = View.GONE
        }

        tv_total_refund.text = cancellationModel.cancellationReasonAndAttachmentModel.estimateFmt
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
        val color = requireContext().resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G600)
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
        container_estimate_refund.visibility = View.VISIBLE

        if (estimationNotes.isNotEmpty()) {
            tv_refund_star.visibility = View.VISIBLE
        } else {
            tv_refund_star.visibility = View.GONE
        }

        if (!::estimationNotesAdapter.isInitialized) {
            estimationNotesAdapter = FlightCancellationReviewEstimationNotesAdapter()
        }
        estimationNotesAdapter.setData(estimationNotes)

        rvEstimationNotes.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvEstimationNotes.setHasFixedSize(true)
        rvEstimationNotes.adapter = estimationNotesAdapter
        rvEstimationNotes.visibility = View.VISIBLE
    }

    private fun hideEstimateValue() {
        container_estimate_refund.visibility = View.GONE
        rvEstimationNotes.visibility = View.GONE
    }

    private fun showRefundDetail(message: String) {
        tv_refund_detail.text = message
        tv_refund_detail.visibility = View.VISIBLE
    }

    private fun hideRefundDetail() {
        tv_refund_detail.visibility = View.GONE
    }

    private fun showCancellationError(t: Throwable) {
        Toaster.build(requireView(),
                FlightErrorUtil.getErrorIdAndTitleFromFlightError(requireContext(), t).second,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR).show()
    }

    companion object {

        const val EXTRA_CANCELLATION_ERROR = "EXTRA_CANCELLATION_ERROR"

        private const val REQUEST_CANCELLATION_TNC = 1

        private const val ERROR_ID_NO_MORE_ADULT = 165

        private const val LEARN_TEXT = "Pelajari"

        fun createInstance(invoiceId: String,
                           cancellationWrapperModel: FlightCancellationWrapperModel)
                : FlightCancellationReviewFragment =
                FlightCancellationReviewFragment().also {
                    it.arguments = Bundle().apply {
                        putString(FlightCancellationReviewActivity.EXTRA_INVOICE_ID, invoiceId)
                        putParcelable(FlightCancellationReviewActivity.EXTRA_CANCEL_WRAPPER, cancellationWrapperModel)
                    }
                }
    }
}