package com.tokopedia.cmhomewidget.presentation.adapter.viewholder


import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetPaymentCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetPaymentData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetPaymentCardListener
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*


class CMHomeWidgetPaymentCardViewHolder(
    private val binding: LayoutCmHomeWidgetPaymentCardBinding,
    private val listener: CMHomeWidgetPaymentCardListener
) : AbstractViewHolder<CMHomeWidgetPaymentData>(binding.root) {

    override fun bind(paymentData: CMHomeWidgetPaymentData) {
        setBankLogo(paymentData.imgUrl)
        setBankGatewayName(paymentData.gatewayName)
        setAccountNo(paymentData.accountNumber)
        setTotalPayment(paymentData.totalPayment)
        setActionButtonText(paymentData.actionButton?.get(FIRST_INDEX)?.actionButtonText)
        setOnClickListeners(paymentData)
        if (paymentData.isWidgetClosePress) {
            binding.timer.onPause
        }
        paymentData.expiredTime?.let {
            val time = it.toLong()
            setTimer(time)
        }
        binding.root.post {
            listener.setPaymentCardHeight(binding.root.measuredHeight)
        }
        if (paymentData.isSingleDataItem()) {
            setDividerMargin()
        }
    }

    private fun setBankLogo(paymentImageUrl: String?) {
        paymentImageUrl?.let {
            binding.ivCmHomeWidgetPayment.setImageUrl(it)
        }
    }

    private fun setBankGatewayName(gatewayName: String?) {
        binding.tvCmHomeWidgetPaymentGatewayName.text = gatewayName
    }

    private fun setAccountNo(accountNo: String?){
        binding.tvCmHomeWidgetPaymentAccountNo.text = accountNo
    }

    private fun setTotalPayment(amount: String?) {
        binding.tvCmHomeWidgetPaymentAmt.text = amount
    }

    private fun setActionButtonText(text: String?) {
        binding.btnCmHomeWidgetProduct.text = text
    }

    private fun setTimer(time: Long) {
        binding.run {
            val epoch = System.currentTimeMillis() / 1000
            timer.isShowClockIcon = true
            timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
            timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
            timer.targetDate = Calendar.getInstance().apply {
                val timeDiff = time - epoch
                add(Calendar.SECOND, (timeDiff % 60).toInt())
                add(Calendar.MINUTE, ((timeDiff / 60) % 60).toInt())
                add(Calendar.HOUR, (timeDiff / (60 * 60)).toInt())
            }
            timer.onFinish = {
                listener.timerUpWidgetClose()
            }
        }
    }

    private fun setDividerMargin() {
        val param = binding.vCmHomeWidgetDivider.layoutParams as ViewGroup.MarginLayoutParams
        param.marginStart = DIVIDER_MARGIN_START
        binding.vCmHomeWidgetDivider.layoutParams = param
    }

    private fun setOnClickListeners(dataItem: CMHomeWidgetPaymentData) {
        binding.btnCmHomeWidgetProduct.setOnClickListener {
            listener.onPaymentBtnClick(dataItem)
        }
        binding.root.setOnClickListener {
            listener.onPaymentCardClick(dataItem)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_payment_card
        const val RATIO_WIDTH = 0.689
        const val SINGLE_ITEM_RATIO_WIDTH = 0.943
        const val FIRST_INDEX = 0
        const val DIVIDER_MARGIN_START = 180
    }

}