package com.tokopedia.flight.orderdetail.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.model.mapper.FlightOrderDetailStatusMapper
import com.tokopedia.flight.orderdetail.presentation.utils.OrderDetailUtils
import kotlinx.android.synthetic.main.view_flight_order_detail_status_header.view.*

/**
 * @author by furqan on 11/11/2020
 */
class FlightOrderDetailHeaderStatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    var listener: Listener? = null

    private var statusInt: Int = 0
    private var statusStr: String = ""
    private var invoiceId: String = ""
    private var transactionDate: String = ""
    private var paymentMethod: String = ""
    private var paymentAmount: String = ""

    init {
        View.inflate(context, R.layout.view_flight_order_detail_status_header, this)
    }

    fun setData(statusInt: Int,
                statusStr: String,
                invoiceId: String,
                transactionDate: String,
                paymentMethod: String,
                paymentAmount: String) {
        this.statusInt = statusInt
        this.statusStr = statusStr
        this.invoiceId = invoiceId
        this.transactionDate = transactionDate
        this.paymentMethod = paymentMethod
        this.paymentAmount = paymentAmount
    }

    fun buildView() {
        renderOrderStatus()
        renderInvoiceId()
        renderTransactionDate()
        renderPaymentView()
    }

    private fun renderOrderStatus() {
        context?.let {
            when (FlightOrderDetailStatusMapper.getStatusOrder(statusInt)) {
                FlightOrderDetailStatusMapper.SUCCESS -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_G200)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                }
                FlightOrderDetailStatusMapper.IN_PROGRESS, FlightOrderDetailStatusMapper.WAITING_FOR_PAYMENT -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_Y200)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Y500))
                }
                FlightOrderDetailStatusMapper.FAILED, FlightOrderDetailStatusMapper.REFUNDED -> {
                    OrderDetailUtils.changeShapeColor(it, tgFlightOrderStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_R100)
                    tgFlightOrderStatus.setTextColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_R400))
                }
            }
            tgFlightOrderStatus.text = statusStr
        }
    }

    private fun renderInvoiceId() {
        tgFlightOrderInvoice.text = invoiceId
        tgFlightOrderInvoice.setOnClickListener {
            listener?.onInvoiceIdClicked()
        }
        ivFlightOrderInvoiceCopy.setOnClickListener {
            listener?.onCopyInvoiceIdClicked(invoiceId)
        }
    }

    private fun renderTransactionDate() {
        tgFlightOrderCreateTime.text = TravelDateUtil.formatDate(
                TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TravelDateUtil.DEFAULT_VIEW_TIME_FORMAT,
                transactionDate)
    }

    private fun renderPaymentView() {
        tgFlightOrderPaymentMethod.text = paymentMethod
        tgFlightOrderTotalPayment.text = paymentAmount

        tgFlightOrderLabelDetailPayment.setOnClickListener {
            listener?.onDetailPaymentClicked()
        }
        ivFlightOrderLabelDetailPayment.setOnClickListener {
            listener?.onDetailPaymentClicked()
        }
    }

    interface Listener {
        fun onCopyInvoiceIdClicked(invoiceId: String)
        fun onDetailPaymentClicked()
        fun onInvoiceIdClicked()
    }

}