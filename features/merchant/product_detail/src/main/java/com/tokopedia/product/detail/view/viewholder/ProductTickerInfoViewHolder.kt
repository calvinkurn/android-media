package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.ticker.GeneralTickerDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.databinding.ItemTickerInfoViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.toDateId
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter

/**
 * Created by Yehezkiel on 08/06/20
 */
class ProductTickerInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductTickerInfoDataModel>(view) {

    var componentTrackDataModel: ComponentTrackDataModel? = null

    companion object {
        val LAYOUT = R.layout.item_ticker_info_view_holder
    }

    private val binding = ItemTickerInfoViewHolderBinding.bind(view)

    override fun bind(element: ProductTickerInfoDataModel) {
        if (element.statusInfo == null || element.isUpcomingType) {
            hideComponent()
        } else {
            componentTrackDataModel = element.getComponentTrackData(adapterPosition)
            renderTicker(element)
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(element.getComponentTrackData(adapterPosition))
            }
        }
    }

    private fun renderTicker(element: ProductTickerInfoDataModel) {
        if (element.statusInfo != null && element.statusInfo?.shopStatus != 1 && element.statusInfo?.isIdle == false) {
            showComponent()
            setupShopInfoTicker(element.statusInfo, element.closedInfo, element.impressHolder)
        } else if (element.statusInfo != null && element.statusInfo?.isIdle == true) {
            showComponent()
            setupShopInfoTicker(element.statusInfo, element.closedInfo, element.impressHolder)
        } else if (element.isOos() && !element.isProductParent) {
            showComponent()
            renderOutOfStockTicker(getStringRes(R.string.ticker_out_of_stock_description), getStringRes(R.string.stock_habis), element.impressHolder)
        } else if (element.isProductInactive()) {
            showComponent()
            renderOutOfStockTicker(getStringRes(R.string.ticker_product_inactive_description), getStringRes(R.string.ticker_product_inactive_title), element.impressHolder)
        } else if (element.generalTickerInfoDataModel?.isNotEmpty() == true) {
            showComponent()
            setupGeneralTicker(element.generalTickerInfoDataModel ?: listOf())
        } else {
            hideComponent()
        }
    }

    private fun setupShopInfoTicker(statusInfo: ShopInfo.StatusInfo?, closedInfo: ShopInfo.ClosedInfo?, impressHolder: ImpressHolder) = with(binding) {
        shopTickerInfo.show()
        when (statusInfo?.shopStatus) {
            ShopStatusDef.OPEN, ShopStatusDef.NOT_ACTIVE -> {
                val statusMessage = getStringRes(R.string.ticker_desc_shop_idle)
                val statusTitle = getStringRes(R.string.ticker_title_shop_idle)
                renderShopTicker(statusTitle, statusMessage, null)
                addImpressionListener(statusMessage, statusTitle, impressHolder)
            }
            ShopStatusDef.CLOSED -> {
                renderShopClosed(closedInfo, impressHolder)
            }
            ShopStatusDef.MODERATED_PERMANENTLY, ShopStatusDef.MODERATED -> {
                val statusMessage = if (listener.isOwner()) getStringRes(R.string.ticker_desc_shop_moderated_seller) else getStringRes(R.string.ticker_desc_shop_moderated_buyer)
                val statusTitle = if (listener.isOwner()) getStringRes(R.string.ticker_title_shop_moderated_seller) else getStringRes(R.string.ticker_title_shop_moderated_buyer)
                addImpressionListener(statusMessage, statusTitle, impressHolder)
                renderShopTicker(statusTitle, statusMessage, listener::onTickerShopClicked)
            }
            ShopStatusDef.INCUBATED -> {
                val processedMessage = getStringRes(R.string.ticker_desc_shop_incubated)
                val titleMessage = getStringRes(R.string.ticker_title_shop_incubated)
                addImpressionListener(processedMessage, titleMessage, impressHolder)
                renderShopTicker(titleMessage, processedMessage, null)
            }
            else -> {
                renderShopTicker(statusInfo?.statusTitle ?: "", statusInfo?.statusMessage
                        ?: "", null)
            }
        }
    }

    private fun renderShopClosed(closedInfo: ShopInfo.ClosedInfo?, impressHolder: ImpressHolder) {
        val openDateUnix = closedInfo?.closeDetail?.openDateUnixUtc
        var statusTitle = ""
        var statusMessage = ""
        if (openDateUnix == null || openDateUnix == "0") {
            statusTitle = getStringRes(R.string.ticker_title_shop_holiday)
            statusMessage = getStringRes(R.string.ticker_desc_shop_holiday)
            renderShopTicker(statusTitle, statusMessage, null)
        } else {
            val openDate = closedInfo?.closeDetail?.openDateUnixUtc.toDateId("EEEE, dd MMM yyyy")
            statusMessage = view.context.getString(R.string.ticker_desc_shop_close, openDate)
            statusTitle = getStringRes(R.string.ticker_title_shop_close)
            renderShopTicker(statusTitle, statusMessage, listener::onTickerShopClicked)
        }

        addImpressionListener(statusMessage, statusTitle, impressHolder)
    }

    private fun renderOutOfStockTicker(tickerDescription: String, tickerTitle: String, impressHolder: ImpressHolder) {
        binding.shopTickerInfo.apply {
            setHtmlDescription(tickerDescription)
            this.tickerTitle = tickerTitle
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener.onTickerGoToRecomClicked(binding.shopTickerInfo.tickerTitle.toString(),
                        binding.shopTickerInfo.tickerType, componentTrackDataModel, tickerDescription)
                }

                override fun onDismiss() {}
            })
            addImpressionListener(tickerDescription, tickerTitle, impressHolder)
            show()
        }
    }

    private fun renderShopTicker(statusTitle: String, statusMessage: String, tickerClicked: ((String, Int, ComponentTrackDataModel?, String) -> Unit?)?) = with(binding) {
        shopTickerInfo.setHtmlDescription(statusMessage)
        shopTickerInfo.tickerTitle = statusTitle
        shopTickerInfo.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                tickerClicked?.invoke(shopTickerInfo.tickerTitle.toString(),
                    shopTickerInfo.tickerType, componentTrackDataModel, statusMessage)
            }

            override fun onDismiss() {}
        })
    }

    private fun addImpressionListener(tickerDescription: String, tickerTitle: String, impressHolder: ImpressHolder) {
        binding.shopTickerInfo.apply {
            addOnImpressionListener(impressHolder) {
                DynamicProductDetailTracking.Impression.eventTickerImpression(tickerType, tickerTitle, tickerDescription)
            }
        }
    }

    private fun setupGeneralTicker(generalTickerDatumDataModels: List<GeneralTickerDataModel.TickerDetailDataModel>) = with(binding) {
        shopTickerInfo.hide()
        generalTickerInfo.show()
        if (generalTickerDatumDataModels.isNotEmpty()) {
            val tickerData = generalTickerDatumDataModels.map { TickerData(description = it.message, type = Ticker.TYPE_ANNOUNCEMENT, title = null, isFromHtml = true) }
            val tickerViewPager = TickerPagerAdapter(view.context, tickerData)

            generalTickerInfo.addPagerView(tickerViewPager, tickerData)
            tickerViewPager.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener.onTickerGeneralClicked(generalTickerInfo.tickerTitle.toString(), generalTickerInfo.tickerType, linkUrl.toString(), componentTrackDataModel, linkUrl.toString())
                }

                override fun onDismiss() {}
            })
        }
    }

    private fun hideComponent() = with(binding) {
        viewTickerContainer.layoutParams.height = 0
    }

    private fun showComponent() = with(binding) {
        viewTickerContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun getStringRes(@StringRes value: Int): String = with(view) {
        return context.getString(value)
    }
}