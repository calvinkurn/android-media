package com.tokopedia.flight.orderdetail.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailJourneyModel
import com.tokopedia.flight.orderdetail.presentation.model.mapper.OrderDetailStatusMapper
import com.tokopedia.flight.orderdetail.presentation.utils.OrderDetailUtils
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightOrderDetailViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_order_detail.*
import kotlinx.android.synthetic.main.include_flight_order_detail_header.*
import kotlinx.android.synthetic.main.include_flight_order_detail_journey.*
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailFragment : BaseDaggerFragment() {

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
        renderTicketView(data.journeys)
    }

    private fun renderOrderStatus(statusInt: Int, statusString: String) {
        context?.let {
            when (OrderDetailStatusMapper.getStatusOrder(statusInt)) {
                OrderDetailStatusMapper.SUCCESS -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, R.color.Unify_G200)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, R.color.Unify_G500))
                }
                OrderDetailStatusMapper.IN_PROGRESS, OrderDetailStatusMapper.WAITING_FOR_PAYMENT -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, R.color.Unify_Y200)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, R.color.Unify_Y500))
                }
                OrderDetailStatusMapper.FAILED, OrderDetailStatusMapper.REFUNDED -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, R.color.Unify_R100)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, R.color.Unify_R400))
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

    private fun renderTicketView(journeys: List<OrderDetailJourneyModel>) {
        if (journeys.size > 1) {
            renderReturnTicketView(journeys[1])
        } else {
            hideReturnTicketView()
        }
    }

    private fun renderReturnTicketView(returnJourney: OrderDetailJourneyModel) {
        titleFlightOrderReturnTicket.visibility = View.GONE
        containerFlightOrderReturnTicket.visibility = View.GONE

        val airlineLogo = flightOrderDetailViewModel.getAirlineLogo(returnJourney)
        if (airlineLogo != null) {
            ivFlightOrderReturnAirlineLogo.loadImage(airlineLogo)
        } else {
            ivFlightOrderReturnAirlineLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.flight_ic_multi_airlines))
        }

        tgFlightOrderReturnAirlineName.text = flightOrderDetailViewModel.getAirlineName(returnJourney)

        if (flightOrderDetailViewModel.getRefundableInfo(returnJourney)) {
            tgFlightOrderReturnTicketRefundableStatus.visibility = View.VISIBLE
        } else {
            tgFlightOrderReturnTicketRefundableStatus.visibility = View.GONE
        }

        tgFlightOrderReturnJourneyTrip.text = getString(R.string.flight_order_detail_trip_city,
                returnJourney.departureCityName, returnJourney.departureId,
                returnJourney.arrivalCityName, returnJourney.arrivalId)

        if (returnJourney.routes.isNotEmpty() && returnJourney.routes[0].departureTerminal.isNotEmpty()) {
            tgFlightOrderReturnAirport.text = getString(R.string.flight_order_detail_airport_with_terminal,
                    returnJourney.departureAirportName, returnJourney.routes[0].departureTerminal)
        } else {
            tgFlightOrderReturnAirport.text = returnJourney.departureAirportName
        }

        val departureDateAndTimePair = flightOrderDetailViewModel.getDepartureDateAndTime(returnJourney)
        if (returnJourney.totalTransit > 0) {
            tgFlightOrderReturnDetail.text = getString(R.string.flight_order_detail_airport_journey_with_transit,
                    departureDateAndTimePair.first, departureDateAndTimePair.second, returnJourney.totalTransit)
        } else {
            tgFlightOrderReturnDetail.text = getString(R.string.flight_order_detail_airport_journey_without_transit,
                    departureDateAndTimePair.first, departureDateAndTimePair.second)
        }
    }

    private fun hideReturnTicketView() {
        titleFlightOrderReturnTicket.visibility = View.GONE
        containerFlightOrderReturnTicket.visibility = View.GONE
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

    companion object {
        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        private const val EXTRA_IS_CANCELLATION = "EXTRA_IS_CANCELLATION"
        private const val EXTRA_REQUEST_CANCEL = "EXTRA_REQUEST_CANCEL"

        private const val CLIP_LABEL_INVOICE_ID = "Flight Invoice Id"

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