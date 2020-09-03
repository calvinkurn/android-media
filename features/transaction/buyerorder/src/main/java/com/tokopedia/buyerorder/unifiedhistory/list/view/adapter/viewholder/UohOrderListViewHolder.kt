package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import kotlinx.android.synthetic.main.uoh_list_item.view.*

/**
 * Created by fwidjaja on 25/07/20.
 */
class UohOrderListViewHolder(itemView: View, private val actionListener: UohItemAdapter.ActionListener?) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    override fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohListOrder.Data.UohOrders.Order) {
            itemView.cl_data_product.visible()
            itemView.ic_uoh_vertical?.loadImage(item.dataObject.metadata.verticalLogo)
            itemView.tv_uoh_categories?.text = item.dataObject.metadata.verticalLabel
            itemView.tv_uoh_date?.text = item.dataObject.metadata.paymentDateStr
            itemView.label_uoh_order?.text = item.dataObject.metadata.status.label
            if (item.dataObject.metadata.status.bgColor.isNotEmpty()) {
                itemView.label_uoh_order?.setLabelType(item.dataObject.metadata.status.bgColor)
            }
            itemView.label_uoh_order?.fontColorByPass = item.dataObject.metadata.status.textColor

            if (item.dataObject.metadata.tickers.isNotEmpty()) {
                itemView.ticker_info_inside_card?.visible()
                if (item.dataObject.metadata.tickers.size > 1) {
                    val listTickerData = arrayListOf<TickerData>()
                    item.dataObject.metadata.tickers.forEach {
                        var desc = it.text
                        if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                            desc += " ${itemView.context.getString(R.string.buyer_ticker_info_selengkapnya)
                                    .replace(UohConsts.TICKER_URL, it.action.appUrl)
                                    .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                        }
                        listTickerData.add(TickerData(it.title, desc, UohUtils.getTickerType(it.text), true))
                    }
                    itemView.context?.let {
                        val adapter = TickerPagerAdapter(it, listTickerData)
                        adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                                // TODO : cek lagi url applink nya, make sure lagi nanti
                                RouteManager.route(it, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            }
                        })
                        itemView.ticker_info_inside_card?.setDescriptionClickEvent(object: TickerCallback {
                            override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                            override fun onDismiss() {
                            }

                        })
                        itemView.ticker_info_inside_card?.addPagerView(adapter, listTickerData)
                    }
                } else {
                    item.dataObject.metadata.tickers.first().let {
                        itemView.ticker_info_inside_card?.tickerTitle = it.title
                        var desc = it.text
                        if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                            desc += " ${itemView.context.getString(R.string.buyer_ticker_info_selengkapnya)
                                    .replace(UohConsts.TICKER_URL, it.action.appUrl)
                                    .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                        }
                        itemView.ticker_info_inside_card?.setHtmlDescription(desc)
                        itemView.ticker_info_inside_card?.tickerType = UohUtils.getTickerType(it.type)
                        itemView.ticker_info_inside_card?.setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                RouteManager.route(itemView.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            }

                            override fun onDismiss() {
                            }

                        })
                    }
                }
            } else {
                itemView.ticker_info_inside_card?.gone()
            }

            if (item.dataObject.metadata.products.isNotEmpty()) {
                itemView.tv_uoh_product_name?.text = item.dataObject.metadata.products.first().title
                itemView.tv_uoh_product_desc?.text = item.dataObject.metadata.products.first().inline1.label
                if (item.dataObject.metadata.products.first().imageURL.isNotEmpty()) {
                    itemView.iv_uoh_product?.loadImage(item.dataObject.metadata.products.first().imageURL)
                } else {
                    itemView.cv_uoh_product?.gone()
                }
            }

            if (item.dataObject.metadata.dotMenus.isNotEmpty()) {
                itemView.iv_kebab_menu?.setOnClickListener {
                    actionListener?.onKebabMenuClicked(item.dataObject.metadata.dotMenus)
                }
            }

            if (item.dataObject.metadata.otherInfo.label.isNotEmpty()) {
                itemView.label_other_info?.visible()
                itemView.label_other_info?.text = item.dataObject.metadata.otherInfo.label
            } else {
                itemView.label_other_info?.gone()
            }

            itemView.tv_uoh_total_belanja?.text = item.dataObject.metadata.totalPrice.label
            itemView.tv_uoh_total_belanja_value?.text = item.dataObject.metadata.totalPrice.value
            if (item.dataObject.metadata.buttons.isNotEmpty()) {
                itemView.uoh_btn_action?.visible()
                itemView.uoh_btn_action?.text = item.dataObject.metadata.buttons[0].label
                itemView.uoh_btn_action?.buttonType = UohUtils.getButtonType(item.dataObject.metadata.buttons[0].variantColor)
                itemView.uoh_btn_action?.buttonVariant = UohUtils.getButtonVariant(item.dataObject.metadata.buttons[0].type)
            } else {
                itemView.uoh_btn_action?.gone()
            }

            itemView.cl_data_product?.setOnClickListener {
                actionListener?.onListItemClicked(item.dataObject.metadata.detailURL)
            }

            itemView.uoh_btn_action?.setOnClickListener {
                if (item.dataObject.metadata.buttons.isNotEmpty()) {
                    actionListener?.onActionButtonClicked(
                            item.dataObject.metadata.buttons.first(),
                            position,
                            item.dataObject.orderUUID,
                            item.dataObject.verticalID,
                            item.dataObject.metadata.listProducts)
                }
            }
        }
    }
}