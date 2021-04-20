package com.tokopedia.checkout.view

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.layout_bottomsheet_trade_in_info.view.*

const val TRADE_IN_NORMAL_IMAGE_URL = "https://ecs7.tokopedia.net/timi/desc-timi.png"
const val TRADE_IN_DROP_OFF_IMAGE_URL = "https://ecs7.tokopedia.net/timi/Ditukar%20Di%20Indomaret@3x.png"

fun showTradeInInfoBottomsheet(fragmentManager: FragmentManager, context: Context) {

    BottomSheetUnify().apply {
        val view = View.inflate(context, R.layout.layout_bottomsheet_trade_in_info, null)
        setupContent(view, context)

        showKnob = true
        showCloseIcon = false
        showHeader = false
        clearContentPadding = true

        setChild(view)
        show(fragmentManager, "Trade In Info")
    }
}

private fun setupContent(view: View, context: Context) {
    if (view.tab_trade_in_info.getUnifyTabLayout().tabCount == 0) {
        view.tab_trade_in_info.addNewTab(context.getString(R.string.title_trade_in_default_address))
        view.tab_trade_in_info.addNewTab(context.getString(R.string.title_trade_in_drop_off_address))
    }

    view.text_trade_in_info.text = MethodChecker.fromHtml(view.resources.getString(R.string.checkout_trade_in_default_address_info_description))
    view.img_trade_in_info.loadImage(TRADE_IN_NORMAL_IMAGE_URL)
    view.tab_trade_in_info.tabLayout.getTabAt(0)?.select()

    view.tab_trade_in_info.getUnifyTabLayout().addOnTabSelectedListener(object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            when (tab.position) {
                0 -> {
                    view.img_trade_in_info.loadImage(TRADE_IN_NORMAL_IMAGE_URL)
                    view.text_trade_in_info.text = MethodChecker.fromHtml(view.resources.getString(R.string.checkout_trade_in_default_address_info_description))
                }
                1 -> {
                    view.img_trade_in_info.loadImage(TRADE_IN_DROP_OFF_IMAGE_URL)
                    view.text_trade_in_info.text = MethodChecker.fromHtml(view.resources.getString(R.string.checkout_trade_in_drop_off_address_info_description))
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    })
}
