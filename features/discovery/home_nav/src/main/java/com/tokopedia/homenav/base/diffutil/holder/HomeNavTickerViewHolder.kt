package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.databinding.HolderHomeNavTickerBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class HomeNavTickerViewHolder(itemView: View,
                              private val listener: HomeNavListener
): AbstractViewHolder<HomeNavTickerDataModel>(itemView) {
    private var binding: HolderHomeNavTickerBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_ticker
    }

    override fun bind(element: HomeNavTickerDataModel) {
        val context = itemView.context
        binding?.navTicker?.tickerTitle = element.title
        binding?.navTicker?.setHtmlDescription(element.description)
        binding?.navTicker?.tickerType = element.tickerType

        binding?.navTicker?.setDescriptionClickEvent(object: TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(context, element.applink)
                TrackingProfileSection.onClickOpenShopSection(listener.getUserId())
            }

            override fun onDismiss() {

            }

        })
    }
}