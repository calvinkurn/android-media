package com.tokopedia.flight.orderdetail.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailJourneyStatusView
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.mapper.OrderDetailStatusMapper
import com.tokopedia.flight.orderdetail.presentation.utils.OrderDetailUtils
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightOrderDetailViewModel
import com.tokopedia.flight.resend_email.presentation.bottomsheet.FlightOrderResendEmailBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_order_detail.*
import kotlinx.android.synthetic.main.include_flight_order_detail_header.*
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailFragment : BaseDaggerFragment(), FlightOrderDetailJourneyStatusView.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightOrderDetailViewModel: FlightOrderDetailViewModel

    private var isCancellation: Boolean = false
    private var isRequestCancel: Boolean = false

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancellation = arguments?.getBoolean(EXTRA_IS_CANCELLATION) ?: false
        isRequestCancel = arguments?.getBoolean(EXTRA_REQUEST_CANCEL) ?: false

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        flightOrderDetailViewModel = viewModelProvider.get(FlightOrderDetailViewModel::class.java)
        flightOrderDetailViewModel.invoiceId = arguments?.getString(EXTRA_INVOICE_ID) ?: ""
        flightOrderDetailViewModel.fetchOrderDetailData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_order_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()

        flightOrderDetailViewModel.orderDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderView(it.data)
                }
                is Fail -> {

                }
            }
        })

        flightOrderDetailViewModel.eticketData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {

                }
                is Fail -> {

                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_SEND_E_TICKET -> {
                if (resultCode == Activity.RESULT_OK)
                    showSnackbarSuccess(getString(com.tokopedia.flight.orderlist.R.string.resend_eticket_success))
            }
        }
    }

    override fun onPnrCopyClicked(pnr: String, isReturn: Boolean) {
        copyToClipboard(if (isReturn) CLIP_LABEL_RETURN_BOOKING_CODE else CLIP_LABEL_DEPARTURE_BOOKING_CODE, pnr)
    }

    override fun onSendETicketClicked() {
        val bottomSheet = FlightOrderResendEmailBottomSheet.getInstance(
                flightOrderDetailViewModel.getUserEmail(),
                flightOrderDetailViewModel.invoiceId
        )
        bottomSheet.setTargetFragment(this, REQUEST_CODE_SEND_E_TICKET)
        bottomSheet.setShowListener { bottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        bottomSheet.show(requireFragmentManager(), FlightOrderResendEmailBottomSheet.TAG)
    }

    override fun onViewETicketClicked() {
        flightOrderDetailViewModel.fetchETicketData()
    }

    private fun showLoading() {
        containerContentOrderDetail.visibility = View.GONE
        containerLoaderOrderDetail.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        containerContentOrderDetail.visibility = View.VISIBLE
        containerLoaderOrderDetail.visibility = View.GONE
    }

    private fun renderView(data: OrderDetailDataModel) {
        hideLoading()
        renderOrderStatus(data.status, data.statusString)
        renderInvoiceId(flightOrderDetailViewModel.invoiceId)
        renderTransactionDate(data.createTime)
        renderPaymentView(data.payment.gatewayName, data.payment.totalAmountStr)

        flightOrderDetailJourneyStatus.listener = this
        flightOrderDetailJourneyStatus.setData(data.hasETicket, data.journeys)
        flightOrderDetailJourneyStatus.buildView()
    }

    private fun renderOrderStatus(statusInt: Int, statusString: String) {
        context?.let {
            when (OrderDetailStatusMapper.getStatusOrder(statusInt)) {
                OrderDetailStatusMapper.SUCCESS -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_G200)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                }
                OrderDetailStatusMapper.IN_PROGRESS, OrderDetailStatusMapper.WAITING_FOR_PAYMENT -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_Y200)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Y500))
                }
                OrderDetailStatusMapper.FAILED, OrderDetailStatusMapper.REFUNDED -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_R100)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_R400))
                }
            }
            tgFlightOrderStatus.text = statusString
        }
    }

    private fun renderInvoiceId(invoiceId: String) {
        tgFlightOrderInvoice.text = invoiceId
        ivFlightOrderInvoiceCopy.setOnClickListener {
            copyToClipboard(CLIP_LABEL_INVOICE_ID, invoiceId)
        }
    }

    private fun renderTransactionDate(transactionDate: String) {
        tgFlightOrderCreateTime.text = TravelDateUtil.formatDate(
                TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TravelDateUtil.DEFAULT_VIEW_TIME_FORMAT,
                transactionDate)
    }

    private fun renderPaymentView(paymentMethod: String, totalPayment: String) {
        tgFlightOrderPaymentMethod.text = paymentMethod
        tgFlightOrderTotalPayment.text = totalPayment

        tgFlightOrderLabelDetailPayment.setOnClickListener {
            openDetailPaymentBottomSheet()
        }
        ivFlightOrderLabelDetailPayment.setOnClickListener {
            openDetailPaymentBottomSheet()
        }
    }

    private fun copyToClipboard(label: String, textToCopy: String) {
        context?.let {
            val clipboardManager: ClipboardManager = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(label, textToCopy)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    private fun openDetailPaymentBottomSheet() {

    }

    private fun showSnackbarSuccess(message: String) {
        Toaster.make(requireView(), message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
    }

    companion object {
        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        private const val EXTRA_IS_CANCELLATION = "EXTRA_IS_CANCELLATION"
        private const val EXTRA_REQUEST_CANCEL = "EXTRA_REQUEST_CANCEL"

        private const val CLIP_LABEL_INVOICE_ID = "Flight Invoice Id"
        private const val CLIP_LABEL_DEPARTURE_BOOKING_CODE = "Flight Departure Booking Code"
        private const val CLIP_LABEL_RETURN_BOOKING_CODE = "Flight Return Booking Code"

        private const val REQUEST_CODE_SEND_E_TICKET = 1111

        fun createInstance(invoiceId: String,
                           isCancellation: Boolean,
                           isRequestCancellation: Boolean): FlightOrderDetailFragment =
                FlightOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                        putBoolean(EXTRA_IS_CANCELLATION, isCancellation)
                        putBoolean(EXTRA_REQUEST_CANCEL, isRequestCancellation)
                    }
                }
    }
}