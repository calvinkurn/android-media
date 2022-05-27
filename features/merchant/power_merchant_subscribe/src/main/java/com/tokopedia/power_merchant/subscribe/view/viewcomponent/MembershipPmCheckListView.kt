package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmMembershipCheckListViewBinding
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDataUiModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

/**
 * Created by @ilhamsuaib on 24/05/22.
 */

class MembershipPmCheckListView : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: ViewPmMembershipCheckListViewBinding by lazy {
        ViewPmMembershipCheckListViewBinding.inflate(LayoutInflater.from(context), this, true)
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
            com.tokopedia.unifyprinciples.R.color.Unify_RN500
        )
    }

    fun show(data: MembershipDataUiModel) {
        showTitle(data)
        showShopScoreCheckList(data)
        showOrderCheckList(data)
        showNetIncomeCheckList(data)
    }

    private fun showTitle(data: MembershipDataUiModel) {
        binding.tvPmMembershipChecklistTitle.text = context.getString(
            R.string.pm_membership_current_grade_pm_title
        )
    }

    private fun showNetIncomeCheckList(data: MembershipDataUiModel) {
        val netIncomeStr = CurrencyFormatHelper.convertToRupiah(data.netIncome.toString())
        val netIncomeThresholdStr = CurrencyFormatHelper.convertToRupiah(
            data.netIncomeThreshold.toString()
        )
        val netIncomeFmt = if (data.isEligibleIncome()) {
            context.getString(R.string.pm_net_income, eligibleColor, netIncomeStr)
        } else {
            context.getString(R.string.pm_net_income, notEligibleColor, netIncomeStr)
        }
        val checkListIcon = if (data.isEligibleIncome()) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }
        binding.run {
            imgPmMembershipChecklist3.loadImage(checkListIcon)
            tvPmMembershipChecklistNetIncome.text = netIncomeFmt.parseAsHtml()
            tvPmMembershipChecklistNetIncomeDescription.text = context.getString(
                R.string.pm_niv_threshold_term, netIncomeThresholdStr
            )
        }
    }

    private fun showOrderCheckList(data: MembershipDataUiModel) {
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
        val checkListIcon = if (data.isEligibleOrder()) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }
        binding.run {
            imgPmMembershipChecklist2.loadImage(checkListIcon)
            tvPmMembershipChecklistOrder.text = orderFmt.parseAsHtml()
            tvPmMembershipChecklistOrderDescription.text = context.getString(
                R.string.pm_niv_threshold_term, data.orderThreshold.toString()
            )
        }
    }

    private fun showShopScoreCheckList(data: MembershipDataUiModel) {
        val shopScoreFmt = if (data.isEligibleShopScore()) {
            context.getString(
                R.string.pm_term_shop_score,
                eligibleColor,
                data.shopScore.toString()
            )
        } else {
            context.getString(
                R.string.pm_term_shop_score,
                notEligibleColor,
                data.shopScore.toString()
            )
        }
        val checkListIcon = if (data.isEligibleShopScore()) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }
        binding.run {
            imgPmMembershipChecklist1.loadImage(checkListIcon)
            tvPmMembershipChecklistShopScore.text = shopScoreFmt.parseAsHtml()
            tvPmMembershipChecklistShopScoreDescription.text = context.getString(
                R.string.pm_membership_shop_score_threshold,
                data.shopScoreThreshold
            )
        }
    }
}