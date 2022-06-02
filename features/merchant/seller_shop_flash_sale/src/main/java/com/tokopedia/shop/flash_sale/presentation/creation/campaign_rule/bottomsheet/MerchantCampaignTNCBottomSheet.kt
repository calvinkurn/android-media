package com.tokopedia.shop.flash_sale.presentation.creation.campaign_rule.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flash_sale.common.extension.setNumberedText
import com.tokopedia.shop.flash_sale.domain.entity.MerchantCampaignTNC
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class MerchantCampaignTNCBottomSheet : BottomSheetUnify() {

    companion object {
        private const val KEY_SHOW_TICKER_AND_BUTTON = "KEY_SHOW_TICKER_AND_BUTTON"

        @JvmStatic
        fun createInstance(
            context: Context,
            showTickerAndButton: Boolean = true
        ): MerchantCampaignTNCBottomSheet =
            MerchantCampaignTNCBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_SHOW_TICKER_AND_BUTTON, showTickerAndButton)
                }
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_merchant_campaign_tnc,
                    null
                )
                setChild(view)
            }

        private const val TAG = "MerchantCampaignTNCBottomSheet"
    }

    private var tncData = MerchantCampaignTNC()
    private var showTickerAndButton: Boolean? = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    @SuppressLint("ResourcePackage")
    private fun setupView(view: View) {
        showCloseIcon = true
        setTitle(tncData.title)

        if (arguments != null) {
            showTickerAndButton = arguments?.getBoolean("KEY_SHOW_TICKER")
        }
        val tgTncContent = view.findViewById<Typography>(R.id.tg_tnc_content)
        val ticker = view.findViewById<Ticker>(R.id.ticker_tnc)
        val btnAgree = view.findViewById<UnifyButton>(R.id.btn_agree)

        tgTncContent.setNumberedText(
            tncData.messages,
            resources.getDimensionPixelSize(R.dimen.dp_8)
        )

        when (showTickerAndButton) {
            true -> {
                ticker.visible()
                btnAgree.visible()
            }
            else -> {
                ticker.gone()
                btnAgree.gone()
            }
        }
    }

    fun setupContent(data: MerchantCampaignTNC) {
        tncData = data
    }
}