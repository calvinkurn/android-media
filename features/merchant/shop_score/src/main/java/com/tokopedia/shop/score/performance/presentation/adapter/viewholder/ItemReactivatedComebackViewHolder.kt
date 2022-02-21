package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.databinding.ItemReactivatedSellerComebackBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemReactivatedComebackUiModel

class ItemReactivatedComebackViewHolder(
    view: View,
    private val shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<ItemReactivatedComebackUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_reactivated_seller_comeback
    }

    private val binding = ItemReactivatedSellerComebackBinding.bind(itemView)

    override fun bind(element: ItemReactivatedComebackUiModel) {
        with(binding) {
            containerReactivatedSeller.loadImage(ShopScoreConstant.BG_GREEN_TIMER)
            imgReactivatedSellerComeback.loadImage(
                ShopScoreConstant.IMG_REACTIVATED_SELLER_COMEBACK
            )
        }
        setBtnPerformanceClickListener()
        setIconVideoClickListener()
    }

    private fun setBtnPerformanceClickListener() {
        with(binding) {
            btnShopPerformanceLearn.setOnClickListener {
                shopPerformanceListener.onBtnLearnNowReactivatedClicked(
                    ShopScoreConstant.SHOP_INFO_URL
                )
            }
        }
    }

    private fun setIconVideoClickListener() {
        with(binding) {
            tvWatchVideoReactivated.setOnClickListener {
                shopPerformanceListener.onWatchVideoReactivatedClicked(ShopScoreConstant.VIDEO_YOUTUBE_ID)
            }

            icVideoPerformanceLearnReactivated.setOnClickListener {
                shopPerformanceListener.onWatchVideoReactivatedClicked(ShopScoreConstant.VIDEO_YOUTUBE_ID)
            }
        }
    }
}