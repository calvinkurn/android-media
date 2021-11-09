package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTickerBinding
import com.tokopedia.digital.home.model.RechargeTickerHomepageModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter

/**
 * @author by firman on 09/03/21.
 */

class RechargeHomepageTickerViewHolder(itemView: View, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeTickerHomepageModel>(itemView) {

    override fun bind(element: RechargeTickerHomepageModel) {
        val bind = ViewRechargeHomeTickerBinding.bind(itemView)
        val section = element.rechargeTickers
        with(bind) {
            if (section.isNotEmpty()) {
                tickerRechargeHomePageShimmering.hide()
                tickerRechargeHomePage.apply {
                    show()
                    val tickersData = RechargeHomepageSectionMapper.
                    mapRechargeTickertoTickerData(element.rechargeTickers)

                    val tickerAdapter = TickerPagerAdapter(context, tickersData)

                    addPagerView(tickerAdapter,tickersData)
                    tickerAdapter.setDescriptionClickEvent(object: TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    RouteManager.route(context, linkUrl.toString())
                                }

                                override fun onDismiss() {
                                }
                            })
                }
            } else {
                tickerRechargeHomePage.hide()
                tickerRechargeHomePageShimmering.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_ticker
    }
}