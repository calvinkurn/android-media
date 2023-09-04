package com.tokopedia.digital.home.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardsItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.media.loader.loadImage

class RechargeItemProductCardsAdapter(
    val items: List<RechargeHomepageSections.Item>,
    val onItemBindListener: RechargeHomepageItemListener,
    private val isProductCard1x1: Boolean = false
) :
    RecyclerView.Adapter<RechargeItemProductCardsAdapter.RechargeItemProductCardViewHolder>() {

    override fun onBindViewHolder(viewHolder: RechargeItemProductCardViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener, isProductCard1x1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeItemProductCardViewHolder {
        val view = ViewRechargeHomeProductCardsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RechargeItemProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RechargeItemProductCardViewHolder(val binding: ViewRechargeHomeProductCardsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            element: RechargeHomepageSections.Item,
            onItemBindListener: RechargeHomepageItemListener,
            isProductCard1x1: Boolean
        ) {
            with(binding) {
                if (isProductCard1x1) {
                    (ivRechargeHomeProductCardsItem.layoutParams as ConstraintLayout.LayoutParams)
                        .dimensionRatio = SQUARE_DIMEN_RATIO
                    ivRechargeHomeProductCardsItem.scaleType = ImageView.ScaleType.FIT_CENTER
                }
                ivRechargeHomeProductCardsItem.loadImage(element.mediaUrl)
                tvRechargeHomeProductCardsItemCategory.text = element.title
                tvRechargeHomeProductCardsItemTitle.text = element.subtitle
                tvRechargeHomeProductCardsItemPriceInfo.text = Html.fromHtml(element.label1)
                tvRechargeHomeProductCardsItemPrice.text = element.label2
                root.setOnClickListener {
                    onItemBindListener.onRechargeSectionItemClicked(element)
                }
            }
        }

        companion object {
            const val SQUARE_DIMEN_RATIO = "1:1"
        }
    }
}
