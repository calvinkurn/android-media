package com.tokopedia.checkout.view

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.LayoutBottomsheetTradeInInfoBinding
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify

const val TRADE_IN_NORMAL_IMAGE_URL = TokopediaImageUrl.TRADE_IN_NORMAL_IMAGE_URL
const val TRADE_IN_DROP_OFF_IMAGE_URL = TokopediaImageUrl.TRADE_IN_DROP_OFF_IMAGE_URL

fun showTradeInInfoBottomsheet(fragmentManager: FragmentManager, context: Context) {
    BottomSheetUnify().apply {
        val binding = LayoutBottomsheetTradeInInfoBinding.inflate(LayoutInflater.from(context), null, false)
        setupContent(binding, context)

        showKnob = true
        showCloseIcon = false
        showHeader = false
        clearContentPadding = true

        setChild(binding.root)
        show(fragmentManager, "Trade In Info")
    }
}

private fun setupContent(binding: LayoutBottomsheetTradeInInfoBinding, context: Context) {
    val tabTradeInInfo = binding.tabTradeInInfo
    if (tabTradeInInfo.getUnifyTabLayout().tabCount == 0) {
        tabTradeInInfo.addNewTab(context.getString(R.string.title_trade_in_default_address))
        tabTradeInInfo.addNewTab(context.getString(R.string.title_trade_in_drop_off_address))
    }

    binding.textTradeInInfo.text = MethodChecker.fromHtml(binding.root.resources.getString(R.string.checkout_trade_in_default_address_info_description))
    val imgTradeInInfo = binding.imgTradeInInfo
    imgTradeInInfo.loadImage(TRADE_IN_NORMAL_IMAGE_URL)
    tabTradeInInfo.tabLayout.getTabAt(0)?.select()

    tabTradeInInfo.getUnifyTabLayout().addOnTabSelectedListener(object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            when (tab.position) {
                0 -> {
                    imgTradeInInfo.loadImage(TRADE_IN_NORMAL_IMAGE_URL)
                    binding.textTradeInInfo.text =
                        MethodChecker.fromHtml(binding.root.resources.getString(R.string.checkout_trade_in_default_address_info_description))
                }
                1 -> {
                    imgTradeInInfo.loadImage(TRADE_IN_DROP_OFF_IMAGE_URL)
                    binding.textTradeInInfo.text =
                        MethodChecker.fromHtml(binding.root.resources.getString(R.string.checkout_trade_in_drop_off_address_info_description))
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    })
}
