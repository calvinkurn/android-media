package com.tokopedia.promousage.view.bottomsheet

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageTncBottomsheetBinding
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
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
        private const val BUNDLE_KEY_PROMO_CODES = "promo_codes"
        private const val BUNDLE_KEY_SOURCE = "source"
        private const val BUNDLE_KEY_USER_ID = "user_id"

        private const val PATH_PROMO_TNC = "promo-tnc"
        private const val QUERY_KEY_CODES = "codes"
        private const val QUERY_KEY_SOURCE = "source"
        private const val QUERY_KEY_ID = "id"
        private const val QUERY_KEY_THEME = "theme"
        private const val QUERY_VALUE_THEME_DARK = "dark"

        fun newInstance(
            promoCodes: List<String>,
            source: PromoPageEntryPoint,
            userId: String
        ): PromoUsageTncBottomSheet {
            return PromoUsageTncBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(BUNDLE_KEY_PROMO_CODES, ArrayList(promoCodes))
                    putParcelable(BUNDLE_KEY_SOURCE, source)
                    putString(BUNDLE_KEY_USER_ID, userId)
                }
            }
        }
    }

    private var binding by autoClearedNullable<PromoUsageTncBottomsheetBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PromoUsageTncBottomsheetBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        val promoCodes = arguments?.getStringArrayList(BUNDLE_KEY_PROMO_CODES) ?: emptyList()
        val source = arguments?.getParcelable(BUNDLE_KEY_SOURCE)
            ?: PromoPageEntryPoint.CART_PAGE
        val userId = arguments?.getString(BUNDLE_KEY_USER_ID) ?: ""

        if (promoCodes.isNotEmpty() && source.toSourceString().isNotBlank() && userId.isNotBlank()) {
            renderContent(promoCodes, source.toSourceString(), userId)
        } else {
            dismiss()
        }
    }

    private fun PromoPageEntryPoint.toSourceString(): String {
        return when (this) {
            PromoPageEntryPoint.CART_PAGE -> "cart"
            PromoPageEntryPoint.CHECKOUT_PAGE -> "checkout"
            PromoPageEntryPoint.OCC_PAGE -> "occ"
        }
    }

    private fun renderContent(
        promoCodes: List<String>,
        source: String,
        userId: String
    ) {
        if (promoCodes.isEmpty()) {
            dismiss()
        }
        setTitle(getString(R.string.promo_usage_tnc_title))

        val tncUrl = generateTncUrl(promoCodes, source, userId)
        val webViewFragment = BaseSessionWebViewFragment.newInstance(tncUrl, true, false, false)
        childFragmentManager.beginTransaction()
            .replace(R.id.container_promo_tnc, webViewFragment)
            .commit()
    }

    private fun generateTncUrl(
        promoCodes: List<String>,
        source: String,
        userId: String
    ): String {
        val builder = Uri.parse(TokopediaUrl.getInstance().WEB)
            .buildUpon()
            .path(PATH_PROMO_TNC)
            .appendQueryParameter(QUERY_KEY_CODES, promoCodes.joinToString(","))
            .appendQueryParameter(QUERY_KEY_SOURCE, source)
            .appendQueryParameter(QUERY_KEY_ID, userId)
        val isDarkMode = context?.isDarkMode() ?: false
        if (isDarkMode) {
            builder.appendQueryParameter(QUERY_KEY_THEME, QUERY_VALUE_THEME_DARK)
        }
        return builder.build().toString()
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }
}
