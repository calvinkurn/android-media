package com.tokopedia.buyerorder.detail.revamp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.RedeemVoucherDealsNewLayoutBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.revamp.util.VisitableMapper
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * created by @bayazidnasir on 25/8/2022
 */

class RedeemVoucherView : LinearLayout {

    private var voucherCount: Int = 0
    private var isOMP: Boolean = false
    private var retryCount = 0
    private var onTapActionDeals: ((TextView?, Items, Int) -> Unit)? = null
    private var onShowRetry: ((String) -> Unit)? = null

    private lateinit var actionButton: ActionButton
    private lateinit var item: Items
    private lateinit var binding: RedeemVoucherDealsNewLayoutBinding

    constructor(context: Context?) : super(context){
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initView()
    }

    constructor(
        context: Context?,
        voucherCount: Int,
        isOMP: Boolean,
        actionButton: ActionButton,
        item: Items,
        onTapActionDeals: ((TextView?, Items, Int) -> Unit)?,
        onShowRetry: ((String) -> Unit)?
    ) : super(context) {
        this.voucherCount = voucherCount
        this.isOMP = isOMP
        this.actionButton = actionButton
        this.item = item
        this.onTapActionDeals = onTapActionDeals
        this.onShowRetry = onShowRetry
        initView()
    }

    private fun initView(){
        binding = RedeemVoucherDealsNewLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        if (actionButton.control.equals(KEY_REFRESH, true)) {
            renderRetryButton(actionButton)
        } else {
            renderRedeemButton(actionButton)
        }

        binding.redeemBtnDeals.setOnClickListener {
            if (actionButton.label.equals(KEY_RETRY, true)) {
                retryCount++
                binding.redeemBtnDeals.isLoading = true
            } else {
                binding.redeemBtnDeals.isLoading = false
            }

            onTapActionDeals?.invoke(binding.redeemBtnDeals, item, retryCount)
        }

        binding.dividerVoucher.showWithCondition(item.actionButtons.size - Int.ONE != voucherCount)

    }

    private fun renderRedeemButton(actionButton: ActionButton){
        binding.redeemBtnDeals.isLoading = false
        binding.redeemBtnDeals.text = actionButton.label
        if (actionButton.header.isNotEmpty()){
            val header = actionButton.headerObject
            if (header.itemLabel.isNotEmpty() || header.item_label.isNotEmpty()) {
                if (isOMP) {
                    binding.voucherCodeTitleDeals.text = header.item_label
                } else {
                    binding.voucherCodeTitleDeals.text = header.itemLabel
                }
            }
        }  else {
            if (voucherCount > Int.ZERO) {
                binding.voucherCodeTitleDeals.text = String.format(
                    E_VOUCHER_FORMAT,
                    context.getString(R.string.event_ticket_voucher_number_multiple),
                    (voucherCount + Int.ONE)
                )
            } else {
                binding.voucherCodeTitleDeals.text = context.getString(R.string.event_ticket_voucher_number)
            }
        }
    }

    private fun renderRetryButton(actionButton: ActionButton){
        binding.voucherCodeTitleDeals.text = actionButton.label
        if (retryCount == Int.ZERO) {
            if (actionButton.header.isNotEmpty() && actionButton.headerObject.itemLabel.isNotEmpty()) {
                binding.voucherCodeTitleDeals.text = actionButton.headerObject.itemLabel
            }
        } else {
            if (actionButton.control.equals(KEY_REFRESH, true)) {
                with(binding){
                    voucherCodeTitleDeals.text = context.getString(R.string.tkpdtransaction_oms_retry_text)
                    redeemBtnDeals.isEnabled = false
                    redeemBtnDeals.postDelayed({
                        retryCount = Int.ZERO
                        redeemBtnDeals.isEnabled = true
                        renderRetryButton(actionButton)
                    }, DELAY_TIME)
                }

                if (item.category.equals(VisitableMapper.CATEGORY_DEALS, true)) {
                    onShowRetry?.invoke(context.getString(R.string.tkpdtransaction_oms_retry_failed_deals))
                } else {
                    onShowRetry?.invoke(context.getString(R.string.tkpdtransaction_oms_retry_failed_event))
                }
            } else {
                onShowRetry?.invoke(context.getString(R.string.tkpdtransaction_oms_retry_success))
            }
        }
    }

    private companion object{
        const val KEY_REFRESH = "refresh"
        const val KEY_RETRY = "Cek Ulang"
        const val E_VOUCHER_FORMAT = "%s %s"
        const val DELAY_TIME = 30000L
    }
}