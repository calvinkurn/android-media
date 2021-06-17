package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PMProStatusInfoUiModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.bottom_sheet_pm_pro_status_info.view.*

/**
 * Created By @ilhamsuaib on 17/06/21
 */

class PMProStatusInfoBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "PMProStatusInfoBottomSheet"
        private const val KEY_STATUS_INFO = "key_status_info"

        fun createInstance(model: PMProStatusInfoUiModel): PMProStatusInfoBottomSheet {
            return PMProStatusInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_STATUS_INFO, model)
                }
            }
        }
    }

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_pro_status_info

    override fun setupView() = childView?.run {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showStatusInfo()
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun showStatusInfo() = childView?.run {
        val statusInfo: PMProStatusInfoUiModel? = arguments?.getParcelable(KEY_STATUS_INFO)
        statusInfo?.let {
            tvPmActiveInfo.text = context.getString(R.string.pm_status_info_description, it.pmActiveShopScoreThreshold.toString()).parseAsHtml()
            tvPmProNextUpdateInfo.text = context.getString(R.string.pm_status_info_next_update, it.autoExtendDateFmt).parseAsHtml()
            tvPmProActiveInfo.text = context.getString(
                    R.string.pm_pro_status_info_description,
                    it.pmProActiveShopScoreThreshold.toString(),
                    it.itemSoldThreshold.toString(),
                    getNetIncomeValueFmt(it.netItemValueThreshold)
            ).parseAsHtml()
        }
    }

    private fun getNetIncomeValueFmt(netItemValueThreshold: Long): String {
        return CurrencyFormatHelper.convertToRupiah(netItemValueThreshold.toString())
    }
}