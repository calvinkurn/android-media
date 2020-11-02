package com.tokopedia.digital.home.presentation.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_product_card_custom_banner_item.view.*

class RechargeCustomBannerProductCardAdapter(val items: List<RechargeHomepageSections.Item>,
                                             val listener: RechargeHomepageItemListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (position > 0) (viewHolder as ViewHolder).bind(items[position - 1])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EMPTY_LAYOUT) {
            val view = LayoutInflater.from(parent.context).inflate(EMPTY_LAYOUT, parent, false)
            EmptyViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(parent.context).inflate(PRODUCT_CARD_LAYOUT, parent, false)
            ViewHolder(view, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) EMPTY_LAYOUT else PRODUCT_CARD_LAYOUT
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    class EmptyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class ViewHolder(itemView: View, val listener: RechargeHomepageItemListener)
        : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeHomepageSections.Item) {
            with(itemView) {
                tv_recharge_product_title.text = element.title
                tv_recharge_product_content.text = MethodChecker.fromHtml(element.subtitle)

                tv_recharge_product_ori_price.text = MethodChecker.fromHtml(element.label1)
                tv_recharge_product_ori_price.paintFlags = tv_recharge_product_discount_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                tv_recharge_product_discount_price.text = MethodChecker.fromHtml(element.label2)

                iv_recharge_product_image.loadImage(element.mediaUrl)

                if (element.label3.isNotEmpty()) {
                    tv_recharge_product_tag.text = element.label3
                    tv_recharge_product_tag.show()
                } else {
                    tv_recharge_product_tag.hide()
                }

                setOnClickListener {
                    listener.onRechargeSectionItemClicked(element)
                    RouteManager.route(context, element.applink)
                }
            }
        }
    }

    companion object {
        val EMPTY_LAYOUT = R.layout.home_banner_item_empty_carousel
        val PRODUCT_CARD_LAYOUT = R.layout.view_recharge_home_product_card_custom_banner_item
    }
}
