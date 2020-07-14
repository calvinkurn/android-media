package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import kotlinx.android.synthetic.main.uoh_list_item.view.*

/**
 * Created by fwidjaja on 02/07/20.
 */

class UohListItemAdapter : RecyclerView.Adapter<UohListItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var uohItemList = mutableListOf<UohListOrder.Data.UohOrders.Order>()
    private var isLoading = false
    private var actionListener: ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.uoh_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return uohItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isLoading) {
            holder.itemView.cl_data_product.gone()
            holder.itemView.cl_loader.visible()

            holder.itemView.loader_ic_uoh_vertical.type = LoaderUnify.TYPE_CIRCLE
            holder.itemView.loader_tv_uoh_categories.type = LoaderUnify.TYPE_RECT
            holder.itemView.loader_label_status_and_three_dots.type = LoaderUnify.TYPE_RECT
            holder.itemView.loader_iv_uoh_product.type = LoaderUnify.TYPE_RECT
            holder.itemView.loader_product_name.type = LoaderUnify.TYPE_RECT
            holder.itemView.loader_product_desc.type = LoaderUnify.TYPE_RECT
            holder.itemView.loader_label_total_harga.type = LoaderUnify.TYPE_RECT
            holder.itemView.loader_value_total_harga.type = LoaderUnify.TYPE_RECT
        } else {
            if (uohItemList.isNotEmpty()) {
                holder.itemView.cl_loader.gone()
                holder.itemView.cl_data_product.visible()
                holder.itemView.ic_uoh_vertical?.loadImage(uohItemList[position].metadata.verticalLogo)
                holder.itemView.tv_uoh_categories?.text = uohItemList[position].metadata.verticalLabel
                holder.itemView.tv_uoh_date?.text = uohItemList[position].metadata.paymentDateStr
                holder.itemView.label_uoh_order?.text = uohItemList[position].metadata.status.label
                holder.itemView.label_uoh_order?.setLabelType(uohItemList[position].metadata.status.bgColor)
                holder.itemView.label_uoh_order?.fontColorByPass = uohItemList[position].metadata.status.textColor

                if (uohItemList[position].metadata.tickers.isNotEmpty()) {
                    holder.itemView.ticker_info_inside_card?.visible()
                    if (uohItemList[position].metadata.tickers.size > 1) {
                        val listTickerData = arrayListOf<TickerData>()
                        uohItemList[position].metadata.tickers.forEach {
                            var desc = it.text
                            if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                                desc += " ${holder.itemView.context.getString(R.string.ticker_info_selengkapnya)
                                        .replace(UohConsts.TICKER_URL, it.action.appUrl)
                                        .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                            }
                            listTickerData.add(TickerData(it.title, desc, UohUtils.getTickerType(it.text), true))
                        }
                        holder.itemView.context?.let {
                            val adapter = TickerPagerAdapter(it, listTickerData)
                            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                                    // TODO : cek lagi url applink nya, make sure lagi nanti
                                    RouteManager.route(it, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                                }
                            })
                            holder.itemView.ticker_info_inside_card?.setDescriptionClickEvent(object: TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                                override fun onDismiss() {
                                }

                            })
                            holder.itemView.ticker_info_inside_card?.addPagerView(adapter, listTickerData)
                        }
                    } else {
                        uohItemList[position].metadata.tickers.first().let {
                            holder.itemView.ticker_info_inside_card?.tickerTitle = it.title
                            var desc = it.text
                            if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                                desc += " ${holder.itemView.context.getString(R.string.ticker_info_selengkapnya)
                                        .replace(UohConsts.TICKER_URL, it.action.appUrl)
                                        .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                            }
                            holder.itemView.ticker_info_inside_card?.setHtmlDescription(desc)
                            holder.itemView.ticker_info_inside_card?.tickerType = UohUtils.getTickerType(it.type)
                            holder.itemView.ticker_info_inside_card?.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    RouteManager.route(holder.itemView.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                                }

                                override fun onDismiss() {
                                }

                            })
                        }
                    }
                } else {
                    holder.itemView.ticker_info_inside_card?.gone()
                }

                if (uohItemList[position].metadata.products.isNotEmpty()) {
                    holder.itemView.tv_uoh_product_name?.text = uohItemList[position].metadata.products.first().title
                    holder.itemView.tv_uoh_product_desc?.text = uohItemList[position].metadata.products.first().inline1.label
                    if (uohItemList[position].metadata.products.first().imageURL.isNotEmpty()) {
                        holder.itemView.iv_uoh_product?.loadImage(uohItemList[position].metadata.products.first().imageURL)
                    } else {
                        holder.itemView.cv_uoh_product?.gone()
                    }
                }

                if (uohItemList[position].metadata.dotMenus.isNotEmpty()) {
                    holder.itemView.iv_kebab_menu?.setOnClickListener {
                        actionListener?.onKebabMenuClicked(uohItemList[position].metadata.dotMenus)
                    }
                }

                if (uohItemList[position].metadata.otherInfo.label.isNotEmpty()) {
                    holder.itemView.label_other_info?.visible()
                    holder.itemView.label_other_info?.text = uohItemList[position].metadata.otherInfo.label
                } else {
                    holder.itemView.label_other_info?.gone()
                }

                holder.itemView.tv_uoh_total_belanja?.text = uohItemList[position].metadata.totalPrice.label
                holder.itemView.tv_uoh_total_belanja_value?.text = uohItemList[position].metadata.totalPrice.value
                if (uohItemList[position].metadata.buttons.isNotEmpty()) {
                    holder.itemView.uoh_btn_action?.text = uohItemList[position].metadata.buttons[0].label
                    holder.itemView.uoh_btn_action?.buttonType = UohUtils.getButtonType(uohItemList[position].metadata.buttons[0].variantColor)
                    holder.itemView.uoh_btn_action?.buttonVariant = UohUtils.getButtonVariant(uohItemList[position].metadata.buttons[0].type)
                } else {
                    holder.itemView.uoh_btn_action?.gone()
                }

                holder.itemView.cl_data_product?.setOnClickListener {
                    actionListener?.onListItemClicked(uohItemList[position].verticalCategory, uohItemList[position].verticalID, uohItemList[position].metadata.upstream)
                }
            }
        }
    }

    fun showLoader() {
        isLoading = true
        notifyDataSetChanged()
    }

    fun addList(list: List<UohListOrder.Data.UohOrders.Order>) {
        isLoading = false
        uohItemList.clear()
        uohItemList.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list: List<UohListOrder.Data.UohOrders.Order>) {
        isLoading = false
        uohItemList.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }

    interface ActionListener {
        fun onKebabMenuClicked(listDotMenu: List<UohListOrder.Data.UohOrders.Order.Metadata.DotMenu>)
        fun onListItemClicked(verticalCategory: String, verticalId: String, upstream: String)
    }
}