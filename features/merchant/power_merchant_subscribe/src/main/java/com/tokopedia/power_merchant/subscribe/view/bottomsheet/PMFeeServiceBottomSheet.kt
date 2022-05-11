package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant.Url.PM_FEE_SERVICE
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetPmFeeServiceBinding
import com.tokopedia.webview.BaseSessionWebViewFragment

class PMFeeServiceBottomSheet :
    BaseBottomSheet<BottomSheetPmFeeServiceBinding>() {

    companion object {
        private const val TAG = "PMFeeServiceBottomSheet"
        fun createInstance(): PMFeeServiceBottomSheet {
            return PMFeeServiceBottomSheet()
        }
    }

    private val webViewPage: BaseSessionWebViewFragment by lazy {
        BaseSessionWebViewFragment.newInstance(PM_FEE_SERVICE)
    }

    override fun bind(view: View) = BottomSheetPmFeeServiceBinding.bind(view)

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_fee_service

    override fun setupView() = binding?.run {
        val title =
            context?.getString(com.tokopedia.power_merchant.subscribe.R.string.pm_fee_service_category)
                ?: String.EMPTY
        setTitle(title)
        setupWebView()
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupWebView(){
        childFragmentManager.beginTransaction()
            .replace(R.id.frameFeeServiceFragment, webViewPage)
            .commit()
    }
}