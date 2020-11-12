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
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailHeaderStatusView
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailJourneyView
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightOrderDetailViewModel
import com.tokopedia.flight.resend_email.presentation.bottomsheet.FlightOrderResendEmailBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_order_detail.*
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailFragment : BaseDaggerFragment(),
        FlightOrderDetailJourneyView.Listener,
        FlightOrderDetailHeaderStatusView.Listener {

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

    override fun onCopyInvoiceIdClicked(invoiceId: String) {
        copyToClipboard(CLIP_LABEL_INVOICE_ID, invoiceId)
    }

    override fun onDetailPaymentClicked() {
        openDetailPaymentBottomSheet()
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

        /* Render Order Status */
        flightOrderDetailHeaderStatus.listener = this
        flightOrderDetailHeaderStatus.setData(
                data.status,
                data.statusString,
                flightOrderDetailViewModel.invoiceId,
                data.createTime,
                data.payment.gatewayName,
                data.payment.totalAmountStr
        )
        flightOrderDetailHeaderStatus.buildView()

        /* Render Journey Ticket View */
        flightOrderDetailJourney.listener = this
        flightOrderDetailJourney.setData(
                data.hasETicket,
                data.journeys
        )
        flightOrderDetailJourney.buildView()
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