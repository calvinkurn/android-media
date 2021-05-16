package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.text.method.LinkMovementMethod
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import kotlinx.android.synthetic.main.bottom_sheet_pm_pro_deactivation.view.*

/**
 * Created By @ilhamsuaib on 12/05/21
 */

class PowerMerchantProDeactivationBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "PowerMerchantProDeactivationBottomSheet"
        fun createInstance(): PowerMerchantProDeactivationBottomSheet {
            return PowerMerchantProDeactivationBottomSheet()
        }
    }

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_pro_deactivation

    override fun setupView() = childView?.run {
        setupOptions()
        setupTnc()
    }

    private fun setupTnc() = childView?.run {
        val clickableText = "S&K"
        val tncDescription = PowerMerchantSpannableUtil.createSpannableString(
                text = getString(R.string.pm_options_tnc_text).parseAsHtml(),
                highlightText = clickableText,
                colorId = requireContext().getResColor(com.tokopedia.unifyprinciples.R.color.Green_G500),
                isBold = true
        ) {
            RouteManager.route(requireContext(), Constant.Url.POWER_MERCHANT_TERMS_AND_CONDITION)
        }

        tvPmOptionsTnC.movementMethod = LinkMovementMethod.getInstance()
        tvPmOptionsTnC.text = tncDescription
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupOptions() = childView?.run {
        val rmLabel = getString(R.string.pm_stop_pm)
        val rmName = getString(R.string.pm_regular_merchant)
        val rmDescription = getString(R.string.pm_option_become_regular_merchant_description)
        with(optionPmPowerMerchant) {
            show(rmLabel, rmName, rmDescription)
            setSelectedStatus(false)
            setOnClickListener {
                if (!isOptionSelected) {
                    val selectedStatus = !isOptionSelected
                    setSelectedStatus(selectedStatus)
                    childView?.optionPmRegularMerchant?.setSelectedStatus(!selectedStatus)
                    childView?.btnPmOptionsNext?.isEnabled = true
                }
            }
        }

        val pmLabel = getString(R.string.pm_downgrade_to_pm)
        val pmName = getString(R.string.pm_title_activity)
        val pmDescription = getString(R.string.pm_option_become_power_merchant_description)
        with(optionPmRegularMerchant) {
            show(pmLabel, pmName, pmDescription)
            setSelectedStatus(false)
            setOnClickListener {
                if (!isOptionSelected) {
                    val selectedStatus = !isOptionSelected
                    setSelectedStatus(selectedStatus)
                    childView?.optionPmPowerMerchant?.setSelectedStatus(!selectedStatus)
                    childView?.btnPmOptionsNext?.isEnabled = true
                }
            }
        }
    }
}