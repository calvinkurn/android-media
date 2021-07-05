package com.tokopedia.digital.home.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_recharge_home_product_cards_item.view.*


class RechargeItemProductCardsAdapter(val items: List<RechargeHomepageSections.Item>,
                                      val onItemBindListener: RechargeHomepageItemListener,
                                      private val isProductCard1x1: Boolean = false)
    : RecyclerView.Adapter<RechargeItemProductCardsAdapter.RechargeItemProductCardViewHolder>() {

    override fun onBindViewHolder(viewHolder: RechargeItemProductCardViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener, isProductCard1x1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeItemProductCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recharge_home_product_cards_item, parent, false)
        return RechargeItemProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RechargeItemProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: RechargeHomepageItemListener,
                 isProductCard1x1: Boolean) {
            with(itemView) {
                if (isProductCard1x1) {
                    (iv_recharge_home_product_cards_item.layoutParams as ConstraintLayout.LayoutParams)
                            .dimensionRatio = SQUARE_DIMEN_RATIO
                    iv_recharge_home_product_cards_item.scaleType = ImageView.ScaleType.FIT_CENTER
                }
                iv_recharge_home_product_cards_item.loadImage(element.mediaUrl)
                tv_recharge_home_product_cards_item_category.text = element.title
                tv_recharge_home_product_cards_item_title.text = element.subtitle
                tv_recharge_home_product_cards_item_price_info.text = Html.fromHtml(element.label1)
                tv_recharge_home_product_cards_item_price.text = element.label2
                setOnClickListener {
                    onItemBindListener.onRechargeSectionItemClicked(element)
                }
            }
        }

        companion object {
            const val SQUARE_DIMEN_RATIO = "1:1"
        }
    }
}
