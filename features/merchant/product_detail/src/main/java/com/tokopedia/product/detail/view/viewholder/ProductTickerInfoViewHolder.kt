package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.toDateId
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import kotlinx.android.synthetic.main.item_ticker_info_view_holder.view.*

/**
 * Created by Yehezkiel on 08/06/20
 */
class ProductTickerInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductTickerInfoDataModel>(view) {

    var componentTrackDataModel: ComponentTrackDataModel? = null

    companion object {
        val LAYOUT = R.layout.item_ticker_info_view_holder
    }

    override fun bind(element: ProductTickerInfoDataModel) {
        if (element.generalTickerInfo == null) {
            hideComponent()
        } else {
            componentTrackDataModel = element.getComponentTrackData(adapterPosition)
            renderTicker(element)
        }
    }

    private fun renderTicker(element: ProductTickerInfoDataModel) {
        showComponent()
        if (element.statusInfo != null && element.statusInfo?.shopStatus != 1 && element.statusInfo?.isIdle == false) {
            setupShopInfoTicker(element.statusInfo, element.closedInfo)
        } else if (element.statusInfo != null && element.statusInfo?.isIdle == true) {
            setupShopInfoTicker(element.statusInfo, element.closedInfo)
        } else if (element.generalTickerInfo?.isNotEmpty() == true) {
            setupGeneralTicker(element.generalTickerInfo ?: listOf())
        } else {
            hideComponent()
        }
    }

    private fun setupShopInfoTicker(statusInfo: ShopInfo.StatusInfo?, closedInfo: ShopInfo.ClosedInfo?) = with(view) {
        shop_ticker_info.show()
        when (statusInfo?.shopStatus) {
            ProductShopStatusTypeDef.OPEN, ProductShopStatusTypeDef.INACTIVE -> {
                val statusMessage = getStringRes(R.string.ticker_desc_shop_idle)
                val statusTitle = getStringRes(R.string.ticker_title_shop_idle)
                renderShopTicker(statusTitle, statusMessage, null)
            }
            ProductShopStatusTypeDef.CLOSED -> {
                val openDate = closedInfo?.closeDetail?.openDateUnix.toDateId("EEEE, dd MMM yyyy")
                val statusMessage = view.context.getString(R.string.ticker_desc_shop_close, openDate)
                val statusTitle = getStringRes(R.string.ticker_title_shop_close)
                renderShopTicker(statusTitle, statusMessage, listener::onTickerShopClicked)
            }
            ProductShopStatusTypeDef.MODERATED_PERMANENTLY, ProductShopStatusTypeDef.MODERATED -> {
                val statusMessage = if (listener.isOwner()) getStringRes(R.string.ticker_desc_shop_moderated_seller) else getStringRes(R.string.ticker_desc_shop_moderated_buyer)
                val statusTitle = if (listener.isOwner()) getStringRes(R.string.ticker_title_shop_moderated_seller) else getStringRes(R.string.ticker_title_shop_moderated_buyer)
                renderShopTicker(statusTitle, statusMessage, listener::onTickerShopClicked)
            }
            ProductShopStatusTypeDef.INCUBATED -> {
                val processedMessage = getStringRes(R.string.ticker_desc_shop_incubated)
                val titleMessage = getStringRes(R.string.ticker_title_shop_incubated)
                renderShopTicker(titleMessage, processedMessage, null)
            }
            else -> {
                renderShopTicker(statusInfo?.statusTitle ?: "", statusInfo?.statusMessage
                        ?: "", null)
            }
        }
    }

    private fun renderShopTicker(statusTitle: String, statusMessage: String, tickerClicked: ((String, Int, ComponentTrackDataModel?) -> Unit?)?) = with(view) {
        shop_ticker_info.setHtmlDescription(statusMessage)
        shop_ticker_info.tickerTitle = statusTitle
        shop_ticker_info.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                tickerClicked?.invoke(shop_ticker_info.tickerTitle.toString(), shop_ticker_info.tickerType, componentTrackDataModel)
            }

            override fun onDismiss() {}
        })
    }

    private fun setupGeneralTicker(generalTickerData: List<StickyLoginTickerPojo.TickerDetail>) = with(view) {
        shop_ticker_info.hide()
        general_ticker_info.show()
        if (generalTickerData.isNotEmpty()) {
            val tickerData = generalTickerData.map { TickerData(description = it.message, type = Ticker.TYPE_ANNOUNCEMENT, title = null, isFromHtml = true) }
            val tickerViewPager = TickerPagerAdapter(view.context, tickerData)

            general_ticker_info.addPagerView(tickerViewPager, tickerData)
            tickerViewPager.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener.onTickerGeneralClicked(general_ticker_info.tickerTitle.toString(), general_ticker_info.tickerType, linkUrl.toString(), componentTrackDataModel)
                }

                override fun onDismiss() {}
            })
        }
    }

    private fun hideComponent() = with(view) {
        view_ticker_container.layoutParams.height = 0
    }

    private fun showComponent() = with(view) {
        view_ticker_container.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun getStringRes(@StringRes value: Int): String = with(view) {
        return context.getString(value)
    }
}