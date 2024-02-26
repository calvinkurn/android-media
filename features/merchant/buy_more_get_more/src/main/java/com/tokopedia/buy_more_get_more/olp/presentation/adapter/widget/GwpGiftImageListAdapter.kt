package com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemGwpGiftImageListBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.binding.viewBinding

class GwpGiftImageListAdapter : RecyclerView.Adapter<GwpGiftImageListAdapter.GwpGiftImageListViewHolder>() {

    var giftImageList: List<OfferInfoForBuyerUiModel.Offering.Tier.Benefit.ProductBenefit> = listOf()
    private var onTierClick: () -> Unit = {}

    companion object {
        private const val MAX_DISPLAYED_IMAGE_COUNT = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GwpGiftImageListViewHolder {
        return GwpGiftImageListViewHolder(parent.inflateLayout(R.layout.item_gwp_gift_image_list))
    }

    override fun getItemCount(): Int {
        return if (giftImageList.size > MAX_DISPLAYED_IMAGE_COUNT) {
            MAX_DISPLAYED_IMAGE_COUNT
        } else {
            giftImageList.size
        }
    }

    override fun onBindViewHolder(holder: GwpGiftImageListViewHolder, position: Int) {
        holder.bindData(giftImageList[position], position)
    }

    inner class GwpGiftImageListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context: Context

        private val binding: ItemGwpGiftImageListBinding? by viewBinding()

        init {
            context = itemView.context
        }

        fun bindData(tierInfo: OfferInfoForBuyerUiModel.Offering.Tier.Benefit.ProductBenefit, position: Int) {
            binding?.run {
                imgGiftItem.setImageUrl(tierInfo.image)
                if (position == Int.ONE && giftImageList.size > MAX_DISPLAYED_IMAGE_COUNT) {
                    overlay.visible()
                    tpgRemainingGiftCount.apply {
                        visible()
                        text = "${(giftImageList.size) - MAX_DISPLAYED_IMAGE_COUNT}+"
                    }
                } else {
                    overlay.gone()
                    tpgRemainingGiftCount.gone()
                }
                root.setOnClickListener { onTierClick() }
            }
        }
    }

    fun setOnTierClick(onTierClick: () -> Unit) {
        this.onTierClick = onTierClick
    }
}
