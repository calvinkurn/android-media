package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.utils.SpannableUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmProTargetBinding
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

/**
 * Created by @ilhamsuaib on 21/04/22.
 */

class PMProTargetView : ConstraintLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: ViewPmProTargetBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewPmProTargetBinding.inflate(inflater, this, true)
    }

    fun showInfo(
        completedOrder: Long,
        netIncome: Long,
    ) {
        binding.run {
            val eligibleColor = SpannableUtil.getColorHexString(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            val notEligibleColor = SpannableUtil.getColorHexString(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN500
            )

            val eligibleIconRes = R.drawable.ic_pm_checked
            val neutralIconRes = R.drawable.ic_pm_not_checked

            val isEligibleOrder = completedOrder >= Constant.PM_PRO_MIN_ORDER
            val completedOrderColor = if (isEligibleOrder) {
                eligibleColor
            } else {
                notEligibleColor
            }
            lblPmTargetCompletedOrder.text = root.context.getString(
                R.string.pm_completed_order, completedOrderColor, completedOrder.toString()
            ).parseAsHtml()
            icPmTargetCompletedOrder.setImageResource(
                if (isEligibleOrder) {
                    eligibleIconRes
                } else {
                    neutralIconRes
                }
            )

            val isEligibleIncome = netIncome >= Constant.PM_PRO_MIN_INCOME
            val netIncomeColor = if (isEligibleIncome) {
                eligibleColor
            } else {
                notEligibleColor
            }
            lblPmTargetNetIncome.text = root.context.getString(
                R.string.pm_net_income,
                netIncomeColor,
                CurrencyFormatHelper.convertToRupiah(netIncome.toString())
            ).parseAsHtml()
            icPmTargetNetIncome.setImageResource(
                if (isEligibleIncome) {
                    eligibleIconRes
                } else {
                    neutralIconRes
                }
            )
            tvPmTargetAchievement.setOnClickListener {
                val isVisible = groupPmTargetAchievement.isVisible
                setupExpandableView(isVisible)
            }
            icPmTargetChevron.setOnClickListener {
                val isVisible = groupPmTargetAchievement.isVisible
                setupExpandableView(isVisible)
            }
        }
    }

    private fun setupExpandableView(isExpanded: Boolean) {
        binding.run {
            groupPmTargetAchievement.isVisible = !isExpanded
            if (isExpanded) {
                icPmTargetChevron.setImage(IconUnify.CHEVRON_DOWN)
            } else {
                icPmTargetChevron.setImage(IconUnify.CHEVRON_UP)
            }
        }
    }

    /**
     * show the shop level info only for PM PRO Expert & Ultimate
     * which is, the shop level should be 3 or 4
     * */
}
