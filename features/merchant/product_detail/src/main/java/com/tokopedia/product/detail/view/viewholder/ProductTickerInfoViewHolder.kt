package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.ticker.TickerDataResponse
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter

/**
 * Created by Yehezkiel on 08/06/20
 */
class ProductTickerInfoViewHolder(private val view: View,
                                  private val listener: DynamicProductDetailListener)
    : AbstractViewHolder<ProductTickerInfoDataModel>(view) {

    var componentTrackDataModel: ComponentTrackDataModel? = null
    private val yellowTicker: Ticker? = itemView.findViewById(R.id.shop_ticker_info)
    private val blueTicker: Ticker? = itemView.findViewById(R.id.general_ticker_info)
    private val tickerContainer: ConstraintLayout? = itemView.findViewById(R.id.view_ticker_container)

    companion object {
        val LAYOUT = R.layout.item_ticker_info_view_holder
    }

    override fun bind(element: ProductTickerInfoDataModel) {
        if (element.tickerDataResponse.isEmpty()) {
            hideComponent()
        } else {
            val firstData = element.tickerDataResponse.firstOrNull()
            addImpressionListener(firstData?.title ?: "", firstData?.message
                    ?: "", element.impressHolder, firstData?.color ?: "")

            showComponent()
            componentTrackDataModel = element.getComponentTrackData(adapterPosition)
            renderTicker(element)
        }
    }

    private fun renderTicker(element: ProductTickerInfoDataModel) {
        if (element.tickerDataResponse.firstOrNull()?.color == "warning") {
            renderYellowTicker(element.tickerDataResponse.firstOrNull())
        } else {
            renderBlueTicker(element.tickerDataResponse)
        }
    }

    private fun renderYellowTicker(tickerResponse: TickerDataResponse?) = with(itemView) {
        if (tickerResponse == null) {
            hideComponent()
            return@with
        }

        yellowTicker?.setHtmlDescription(generateHtml(tickerResponse.message, tickerResponse.link))

        yellowTicker?.tickerTitle = tickerResponse.title
        yellowTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listener.onTickerShopClicked(yellowTicker.tickerTitle.toString(),
                        yellowTicker.tickerType,
                        componentTrackDataModel,
                        tickerResponse.message,
                        tickerResponse.actionLink,
                        tickerResponse.action,
                        tickerResponse.actionBottomSheet)
            }

            override fun onDismiss() {}
        })
        blueTicker?.hide()
        yellowTicker?.show()
    }

    private fun renderBlueTicker(generalTickerData: List<TickerDataResponse>) {
        val tickerData = generalTickerData.map {
            val title = if (it.title != "") it.title else null
            TickerData(description = it.message,
                    type = Ticker.TYPE_ANNOUNCEMENT,
                    title = title, isFromHtml = true)
        }
        val tickerViewPager = TickerPagerAdapter(view.context, tickerData)

        blueTicker?.addPagerView(tickerViewPager, tickerData)
        tickerViewPager.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listener.onTickerShopClicked(blueTicker?.tickerTitle.toString(),
                        blueTicker?.tickerType ?: 0,
                        componentTrackDataModel,
                        generalTickerData.firstOrNull()?.message ?: "",
                        linkUrl.toString(),
                        "applink",
                        null)
            }

            override fun onDismiss() {}
        })
        yellowTicker?.hide()
        blueTicker?.show()
    }

    private fun addImpressionListener(tickerDescription: String,
                                      tickerTitle: String,
                                      impressHolder: ImpressHolder,
                                      tickerType: String) {
        val tickerTypeInt = if (tickerType == "warning") 0 else 1
        itemView.apply {
            addOnImpressionListener(impressHolder) {
                DynamicProductDetailTracking.Impression.eventTickerImpression(tickerTypeInt,
                        tickerTitle,
                        tickerDescription)
            }
        }
    }

    private fun hideComponent() {
        tickerContainer?.layoutParams?.height = 0
    }

    private fun showComponent() {
        tickerContainer?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun generateHtml(message: String, link: String): String = with(itemView) {
        return message.replace("{link}", context.getString(R.string.ticker_href_builder, link))
    }
}