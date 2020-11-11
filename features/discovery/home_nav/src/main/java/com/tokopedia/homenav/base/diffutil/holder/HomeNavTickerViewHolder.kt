package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import kotlinx.android.synthetic.main.holder_home_nav_ticker.view.*

class HomeNavTickerViewHolder(itemView: View
): AbstractViewHolder<HomeNavTickerViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_ticker
    }

    override fun bind(element: HomeNavTickerViewModel) {
        val context = itemView.context
        itemView.nav_ticker?.tickerTitle = element.title
        itemView.nav_ticker?.setHtmlDescription(element.description)
        itemView.nav_ticker?.tickerType = element.tickerType
        if (element.applink.isNotEmpty()) {
            itemView.nav_ticker.setOnClickListener { RouteManager.route(
                    context, element.applink
            ) }
        }
    }
}