package com.tokopedia.buyerorder.detail.revamp.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.RedeemVoucherDealsLayoutBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Body
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible

/**
 * created by @bayazidnasir on 25/8/2022
 */

class RedeemVoucherView : LinearLayout {

    private var voucherCount: Int = 0
    private var position: Int = 0
    private var isOMP: Boolean = false
    private var isLastItem:Boolean = false
    private var retryCount = 0

    private lateinit var actionButton: ActionButton
    private lateinit var item: Items
    private lateinit var body: Body
    private lateinit var binding: RedeemVoucherDealsLayoutBinding

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
        position: Int,
        isOMP: Boolean,
        isLastItem: Boolean,
        actionButton: ActionButton,
        item: Items,
        body: Body,
    ) : super(context) {
        this.voucherCount = voucherCount
        this.position = position
        this.isOMP = isOMP
        this.isLastItem = isLastItem
        this.actionButton = actionButton
        this.item = item
        this.body = body
        initView()
    }

    private fun initView(){
        binding = RedeemVoucherDealsLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        if (actionButton.control.equals(KEY_REFRESH, true)) {
            renderRetryButton(actionButton)
        } else {
            renderRedeemButton(actionButton)
        }

        binding.redeemBtnDeals.setOnClickListener {
            if (actionButton.label.equals(KEY_RETRY, true)) {
                retryCount++
                binding.loadingViewRetry.visible()
                binding.redeemBtnDeals.gone()
            } else {
                binding.loadingViewRetry.gone()
                binding.redeemBtnDeals.visible()
            }

            TODO("set tap action")
        }

        binding.dividerVoucher.showWithCondition(!isLastItem)

    }

    private fun renderRedeemButton(actionButton: ActionButton){
        binding.loadingViewRetry.gone()
        binding.redeemBtnDeals.visible()
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
            if (voucherCount > 0) {
                binding.voucherCodeTitleDeals.text = String.format(
                    E_VOUCHER_FORMAT,
                    context.getString(R.string.event_ticket_voucher_number_multiple),
                    (voucherCount + 1)
                )
            } else {
                binding.voucherCodeTitleDeals.text = context.getString(R.string.event_ticket_voucher_number)
            }
        }

        val shape = binding.redeemBtnDeals.background as GradientDrawable
        if (HexValidator.validate(actionButton.actionColor.background)) {
            shape.setColor(Color.parseColor(actionButton.actionColor.background))
        } else {
            shape.setColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        }

        if (HexValidator.validate(actionButton.actionColor.border)) {
            shape.setStroke(STROKE_WIDTH, Color.parseColor(actionButton.actionColor.border))
        } else {
            shape.setStroke(ZERO_STROKE_WIDTH, MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        }

        if (HexValidator.validate(actionButton.actionColor.textColor)) {
            binding.voucherCodeTitleDeals.setTextColor(Color.parseColor(actionButton.actionColor.textColor))
        } else {
            binding.voucherCodeTitleDeals.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }

        shape.cornerRadius = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
    }

    private fun renderRetryButton(actionButton: ActionButton){

    }

    private companion object{
        const val KEY_REFRESH = "refresh"
        const val KEY_RETRY = "Cek Ulang"
        const val E_VOUCHER_FORMAT = "%s %s"
        const val STROKE_WIDTH = 1
        const val ZERO_STROKE_WIDTH = 0
    }
}