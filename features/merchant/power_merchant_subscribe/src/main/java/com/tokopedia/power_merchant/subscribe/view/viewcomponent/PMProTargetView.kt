package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
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
        shopLevel: Int
    ) {
        binding.run {
            val eligibleColor = PowerMerchantSpannableUtil.getColorHexString(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            val notEligibleColor = PowerMerchantSpannableUtil.getColorHexString(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN500
            )

            val completedOrderColor = if (completedOrder >= Constant.PM_PRO_MIN_ORDER) {
                eligibleColor
            } else {
                notEligibleColor
            }
            lblPmTargetCompletedOrder.text = root.context.getString(
                R.string.pm_completed_order, completedOrderColor, completedOrder.toString()
            ).parseAsHtml()

            val netIncomeColor = if (completedOrder >= Constant.PM_PRO_MIN_INCOME) {
                eligibleColor
            } else {
                notEligibleColor
            }
            lblPmTargetNetIncome.text = root.context.getString(
                R.string.pm_net_income,
                netIncomeColor,
                CurrencyFormatHelper.convertToRupiah(netIncome.toString())
            ).parseAsHtml()

            showShopLevel(shopLevel)

            tvPmTargetAchievement.setOnClickListener {
                showShopLevel(shopLevel)
                setupExpandableView(groupPmTargetAchievement.isVisible)
            }
            icPmTargetChevron.setOnClickListener {
                showShopLevel(shopLevel)
                setupExpandableView(groupPmTargetAchievement.isVisible)
            }
        }
    }

    private fun setupExpandableView(isExpanded: Boolean) {
        binding.run {
            groupPmTargetAchievement.isVisible = !isExpanded
        }
    }

    /**
     * show the shop level info only for PM PRO Expert & Ultimate
     * which is, the shop level should be 3 or 4
     * */
    private fun showShopLevel(shopLevel: Int) {
        binding.run {
            if (shopLevel <= PMConstant.ShopLevel.TWO) {
                dividerVerPmTarget2.gone()
                icPmTargetShopLevel.gone()
                lblPmTargetShopLevel.gone()
                lblPmTargetShopLevelDesc.gone()
            } else {
                dividerVerPmTarget2.visible()
                icPmTargetShopLevel.visible()
                lblPmTargetShopLevel.visible()
                lblPmTargetShopLevelDesc.visible()

                lblPmTargetShopLevel.text = root.context.getString(
                    R.string.pm_shop_level, shopLevel.toString()
                ).parseAsHtml()
            }
        }
    }
}