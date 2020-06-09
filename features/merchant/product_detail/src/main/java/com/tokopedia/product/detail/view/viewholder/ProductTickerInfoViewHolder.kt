package com.tokopedia.product.detail.view.viewholder

import android.text.SpannableStringBuilder
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.unifycomponents.ticker.*
import kotlinx.android.synthetic.main.item_ticker_info_view_holder.view.*

/**
 * Created by Yehezkiel on 08/06/20
 */
class ProductTickerInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductTickerInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_ticker_info_view_holder
    }

    override fun bind(element: ProductTickerInfoDataModel) {
        with(view) {
            if (element.statusInfo != null && element.statusInfo?.shopStatus != 1) {
                setupShopInfoTicker(element.statusInfo, element.closedInfo)
            } else if (element.generalTickerInfo.isNotEmpty()) {
                setupGeneralTicker(element.generalTickerInfo)
            } else {
                shop_ticker_info.hide()
                general_ticker_info.hide()
            }

        }
    }

    private fun setupShopInfoTicker(statusInfo: ShopInfo.StatusInfo?, closedInfo: ShopInfo.ClosedInfo?) = with(view) {
        shop_ticker_info.show()
        when (statusInfo?.shopStatus) {
            ProductShopStatusTypeDef.CLOSED -> {
                val processedMessage = view.context.getString(R.string.ticker_desc_shop_close, closedInfo?.closeUntil)
                renderShopTicker(view.context.getString(R.string.ticker_title_shop_close), processedMessage)
            }
            ProductShopStatusTypeDef.MODERATED -> {
                val processedMessage = view.context.getString(R.string.ticker_desc_shop_moderated)
                renderShopTicker(context.getString(R.string.ticker_title_shop_moderated), processedMessage)
            }
            else -> {
                renderShopTicker(statusInfo?.statusTitle ?: "", statusInfo?.statusMessage ?: "")
            }

        }
    }

    private fun renderShopTicker(statusTitle: String, statusMessage: String) = with(view) {
        shop_ticker_info.setHtmlDescription(statusMessage)
        shop_ticker_info.tickerTitle = statusTitle
        shop_ticker_info.setDescriptionClickEvent(object : TickerCallback{
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listener.onTickerShopClicked()
            }

            override fun onDismiss() {
            }

        })
    }

    private fun setupGeneralTicker(generalTickerData: List<StickyLoginTickerPojo.TickerDetail>) = with(view) {
        shop_ticker_info.hide()
        general_ticker_info.show()
        if (generalTickerData.isNotEmpty()) {
            val tickerData = generalTickerData.map { TickerData(description = it.message, type = Ticker.TYPE_ANNOUNCEMENT, title = null, isFromHtml = true) }
            val tickerViewPager = TickerPagerAdapter(view.context, tickerData)

            general_ticker_info.addPagerView(tickerViewPager, tickerData)

            tickerViewPager.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    listener.onTickerGeneralClicked(linkUrl.toString())
                }
            })
        }
    }

}