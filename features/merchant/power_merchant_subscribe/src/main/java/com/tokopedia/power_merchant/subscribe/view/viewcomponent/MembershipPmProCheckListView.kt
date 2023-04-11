package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmProMembershipCheckListViewBinding
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDataUiModel
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

    fun show(data: MembershipDataUiModel) {
        showTitle(data)
        showOrderCheckList(data)
        showNetIncomeCheckList(data)
    }

    private fun showTitle(data: MembershipDataUiModel) {
        val title = if (data.gradeBenefit.isTabActive) {
            context.getString(
                R.string.pm_membership_current_grade_title,
                data.gradeBenefit.tabLabel
            )
        } else {
            context.getString(
                R.string.pm_membership_next_grade_title,
                data.gradeBenefit.getShopLevel(),
                data.gradeBenefit.tabLabel
            )
        }

        binding.tvPmMembershipChecklistTitle.text = title
    }

    private fun showNetIncomeCheckList(data: MembershipDataUiModel) {
        val netIncomeStr = CurrencyFormatHelper.convertToRupiah(data.getNetIncomeValue().toString())
        val netIncomeFmt = if (data.isEligibleIncome()) {
            context.getString(R.string.pm_net_income, eligibleColor, netIncomeStr)
        } else {
            context.getString(R.string.pm_net_income, notEligibleColor, netIncomeStr)
        }
        binding.run {
            tvPmMembershipChecklistNetIncome.text = netIncomeFmt.parseAsHtml()
            tvPmMembershipChecklistNetIncomeDescription.text = context.getString(
                R.string.pm_niv_threshold_term_membership, data.netIncomeThresholdFmt
            )
        }
    }

    private fun showOrderCheckList(data: MembershipDataUiModel) {
        val totalOrderStr = data.getTotalOrderValue().toString()
        val orderFmt = if (data.isEligibleOrder()) {
            context.getString(
                R.string.pm_pro_number_of_order,
                eligibleColor,
                totalOrderStr
            )
        } else {
            context.getString(
                R.string.pm_pro_number_of_order,
                notEligibleColor,
                totalOrderStr
            )
        }
        binding.run {
            tvPmMembershipChecklistOrder.text = orderFmt.parseAsHtml()
            tvPmMembershipChecklistOrderDescription.text = context.getString(
                R.string.pm_niv_threshold_term_membership, data.orderThreshold.toString()
            )
        }
    }
}