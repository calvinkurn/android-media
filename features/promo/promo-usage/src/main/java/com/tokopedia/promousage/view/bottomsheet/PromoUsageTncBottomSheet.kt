package com.tokopedia.promousage.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageTncBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.BaseSessionWebViewFragment

class PromoUsageTncBottomSheet : BottomSheetUnify() {

    init {
        clearContentPadding = true
        isDragable = false
        isHideable = true
        showCloseIcon = true
        showHeader = true
    }

    companion object {
        private const val TAG = "PromoUsageTncBottomSheet"
        private const val KEY_PROMO_CODES = "promo_codes"

        fun newInstance(promoCodes: List<String>): PromoUsageTncBottomSheet {
            return PromoUsageTncBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(KEY_PROMO_CODES, ArrayList(promoCodes))
                }
            }
        }
    }

    private var binding by autoClearedNullable<PromoUsageTncBottomsheetBinding>()
    private var promoCodes: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PromoUsageTncBottomsheetBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        promoCodes = arguments?.getStringArrayList(KEY_PROMO_CODES)
        promoCodes?.let {
            renderContent(it)
        } ?: dismiss()
    }

    private fun renderContent(promoCodes: List<String>) {
        if (promoCodes.isEmpty()) {
            dismiss()
        }
        setTitle(getString(R.string.promo_usage_tnc_title))

        val tncUrl = generateTncUrl(promoCodes)
        val webViewFragment = BaseSessionWebViewFragment.newInstance(tncUrl, true, false, false)
        childFragmentManager.beginTransaction()
            .replace(R.id.container_promo_tnc, webViewFragment)
            .commit()
    }

    private fun generateTncUrl(promoCodes: List<String>): String {
        // TODO: Generate new Promo T&C URL
        return "https://www.tokopedia.com"
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }
}
