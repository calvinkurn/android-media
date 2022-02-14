package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScoreConstant.BG_GREEN_TIMER
import com.tokopedia.shop.score.common.ShopScoreConstant.BG_ORANGE_TIMER
import com.tokopedia.shop.score.databinding.ItemTimerNewSellerBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemTimerNewSellerListener
import com.tokopedia.shop.score.performance.presentation.model.ItemTimerNewSellerUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemTimerNewSellerViewHolder(
    view: View,
    private val itemTimerNewSellerListener: ItemTimerNewSellerListener
) : AbstractViewHolder<ItemTimerNewSellerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_timer_new_seller
    }

    private val binding: ItemTimerNewSellerBinding? by viewBinding()

    override fun bind(element: ItemTimerNewSellerUiModel?) {
        binding?.run {
            containerTimerNewSeller.loadImage(
                if (element?.isTenureDate == true) BG_ORANGE_TIMER else BG_GREEN_TIMER
            )
            timerNewSeller.targetDate = element?.effectiveDate

            tvShopPerformanceNewSeller.text = if (element?.shopScore.isLessThanZero()) {
                getString(
                    R.string.title_shop_performance_become_existing_seller,
                    element?.effectiveDateText.orEmpty()
                )
            } else {
                if (element?.isTenureDate == true) {
                    getString(
                        R.string.desc_shop_performance_timer_after_first_monday_tenure,
                        element.effectiveDateText
                    )
                } else {
                    getString(
                        R.string.desc_shop_performance_timer_after_first_monday,
                        element?.effectiveDateText.orEmpty()
                    )
                }
            }
        }

        setIconVideoClickListener()
        setBtnPerformanceClickListener(element)
    }

    private fun setBtnPerformanceClickListener(element: ItemTimerNewSellerUiModel?) {
        binding?.run {
            btnShopPerformanceLearn.let { btn ->
                itemTimerNewSellerListener.onImpressBtnLearnPerformance()
                btn.setOnClickListener {
                    if (element?.shopScore.isLessThanZero()) {
                        itemTimerNewSellerListener.onBtnLearnNowToFaqClicked()
                    } else {
                        itemTimerNewSellerListener.onBtnLearnNowToSellerEduClicked(
                            ShopScoreConstant.SHOP_INFO_URL
                        )
                    }
                }
            }
        }
    }

    private fun setIconVideoClickListener() {
        binding?.run {
            tvWatchVideo.setOnClickListener {
                itemTimerNewSellerListener.onWatchVideoClicked(ShopScoreConstant.VIDEO_YOUTUBE_ID)
            }

            icVideoShopPerformanceLearn.setOnClickListener {
                itemTimerNewSellerListener.onWatchVideoClicked(ShopScoreConstant.VIDEO_YOUTUBE_ID)
            }

            if (tvWatchVideo.isVisible || icVideoShopPerformanceLearn.isVisible) {
                itemTimerNewSellerListener.onImpressWatchVideo()
            }
        }
    }
}