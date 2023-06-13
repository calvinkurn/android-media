package com.tokopedia.tradein.view.bottomsheet

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.tradein.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifyprinciples.Typography

const val TRADE_IN_NORMAL_IMAGE_URL = TokopediaImageUrl.TRADE_IN_NORMAL_IMAGE_URL
const val TRADE_IN_DROP_OFF_IMAGE_URL = TokopediaImageUrl.TRADE_IN_DROP_OFF_IMAGE_URL

fun showTradeInInfoBottomsheet(fragmentManager: FragmentManager, context: Context) {

    BottomSheetUnify().apply {
        val view = View.inflate(context, R.layout.trade_in_exchange_info_bs, null)
        setupContent(view, context)

        showKnob = true
        showCloseIcon = false
        showHeader = false
        clearContentPadding = true

        setChild(view)
        show(fragmentManager, "Trade In Info Exchange")
    }
}

private fun setupContent(view: View, context: Context) {
    val tabTradeInInfo = view.findViewById<TabsUnify>(R.id.tab_trade_in_info)
    if (tabTradeInInfo.getUnifyTabLayout().tabCount == 0) {
        tabTradeInInfo.addNewTab(context.getString(R.string.tradein_ditukar_di_alamatmu))
        tabTradeInInfo.addNewTab(context.getString(R.string.tradein_ditukar_di_indomaret))
    }

    val textTradeInInfo = view.findViewById<Typography>(R.id.text_trade_in_info)
    textTradeInInfo.text = MethodChecker.fromHtml(view.resources.getString(com.tokopedia.tradein.R.string.trade_in_default_address_info_description))
    val imgTradeInInfo = view.findViewById<ImageUnify>(R.id.img_trade_in_info)
    imgTradeInInfo.loadImage(TRADE_IN_NORMAL_IMAGE_URL)
    tabTradeInInfo.tabLayout.getTabAt(0)?.select()

    tabTradeInInfo.getUnifyTabLayout().addOnTabSelectedListener(object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            when (tab.position) {
                0 -> {
                    imgTradeInInfo.loadImage(TRADE_IN_NORMAL_IMAGE_URL)
                    textTradeInInfo.text = MethodChecker.fromHtml(view.resources.getString(com.tokopedia.tradein.R.string.trade_in_default_address_info_description))
                }
                1 -> {
                    imgTradeInInfo.loadImage(TRADE_IN_DROP_OFF_IMAGE_URL)
                    textTradeInInfo.text = MethodChecker.fromHtml(view.resources.getString(com.tokopedia.tradein.R.string.trade_in_drop_off_address_info_description))
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    })
}
