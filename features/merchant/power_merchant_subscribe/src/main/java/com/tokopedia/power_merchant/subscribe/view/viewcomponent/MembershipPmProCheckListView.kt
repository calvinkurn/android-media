package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmProMembershipCheckListViewBinding
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

/**
 * Created by @ilhamsuaib on 24/05/22.
 */

class MembershipPmProCheckListView : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: ViewPmProMembershipCheckListViewBinding by lazy {
        ViewPmProMembershipCheckListViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private val eligibleColor by lazy {
        PowerMerchantSpannableUtil.getColorHexString(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
    }
    private val notEligibleColor by lazy {
        PowerMerchantSpannableUtil.getColorHexString(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        )
    }

    fun show(data: Data) {
        showOrderCheckList(data)
        showNetIncomeCheckList(data)
    }

    private fun showNetIncomeCheckList(data: Data) {
        val netIncomeStr = CurrencyFormatHelper.convertToRupiah(data.netIncome.toString())
        val netIncomeThresholdStr = CurrencyFormatHelper.convertToRupiah(
            data.netIncomeThreshold.toString()
        )
        val netIncomeFmt = if (data.isEligibleIncome()) {
            context.getString(R.string.pm_net_income, eligibleColor, netIncomeStr)
        } else {
            context.getString(R.string.pm_net_income, notEligibleColor, netIncomeStr)
        }
        binding.run {
            tvPmMembershipChecklistNetIncome.text = netIncomeFmt.parseAsHtml()
            tvPmMembershipChecklistNetIncomeDescription.text = context.getString(
                R.string.pm_niv_threshold_term, netIncomeThresholdStr
            )
        }
    }

    private fun showOrderCheckList(data: Data) {
        val orderFmt = if (data.isEligibleOrder()) {
            context.getString(
                R.string.pm_number_of_order,
                eligibleColor,
                data.totalOrder.toString()
            )
        } else {
            context.getString(
                R.string.pm_number_of_order,
                notEligibleColor,
                data.totalOrder.toString()
            )
        }
        binding.run {
            tvPmMembershipChecklistOrder.text = orderFmt.parseAsHtml()
            tvPmMembershipChecklistOrderDescription.text = context.getString(
                R.string.pm_niv_threshold_term, data.orderThreshold.toString()
            )
        }
    }

    data class Data(
        val orderThreshold: Long,
        val netIncomeThreshold: Long,
        val totalOrder: Long,
        val netIncome: Long
    ) {
        fun isEligibleOrder() = totalOrder >= orderThreshold

        fun isEligibleIncome() = netIncome >= netIncomeThreshold
    }
}