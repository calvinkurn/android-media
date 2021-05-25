package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScoreConstant.BG_GREEN_TIMER
import com.tokopedia.shop.score.common.ShopScoreConstant.BG_ORANGE_TIMER
import com.tokopedia.shop.score.performance.presentation.adapter.ItemTimerNewSellerListener
import com.tokopedia.shop.score.performance.presentation.model.ItemTimerNewSellerUiModel
import kotlinx.android.synthetic.main.timer_new_seller_before_transition.view.*


class ItemTimerNewSellerViewHolder(view: View,
                                   private val itemTimerNewSellerListener: ItemTimerNewSellerListener) : AbstractViewHolder<ItemTimerNewSellerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.timer_new_seller_before_transition
    }

    override fun bind(element: ItemTimerNewSellerUiModel?) {
        with(itemView) {
            containerTimerNewSeller?.loadImage(if (element?.isTenureDate == true) BG_ORANGE_TIMER else BG_GREEN_TIMER)
            timerNewSeller?.targetDate = element?.effectiveDate

            tv_shop_performance_new_seller?.text = getString(R.string.title_shop_performance_become_existing_seller,
                    element?.effectiveDateText.orEmpty())
        }

        setIconVideoClickListener()
        setBtnPerformanceClickListener(element)
    }

    private fun setBtnPerformanceClickListener(element: ItemTimerNewSellerUiModel?) {
        with(itemView) {
            btn_shop_performance_learn?.let { btn ->
                itemTimerNewSellerListener.onImpressBtnLearnPerformance()
                btn.setOnClickListener {
                    if (element?.shopAge.orZero() < ShopScoreConstant.SHOP_AGE_SIXTY) {
                        itemTimerNewSellerListener.onBtnShopPerformanceToFaqClicked()
                    } else {
                        itemTimerNewSellerListener.onBtnShopPerformanceToInterruptClicked(GMCommonUrl.SHOP_INTERRUPT_PAGE)
                    }
                }
            }
        }
    }

    private fun setIconVideoClickListener() {
        with(itemView) {
            tv_watch_video?.setOnClickListener {
                itemTimerNewSellerListener.onWatchVideoClicked(ShopScoreConstant.VIDEO_YOUTUBE_ID)
            }

            ic_video_shop_performance_learn?.setOnClickListener {
                itemTimerNewSellerListener.onWatchVideoClicked(ShopScoreConstant.VIDEO_YOUTUBE_ID)
            }

            if (tv_watch_video?.isVisible == true || ic_video_shop_performance_learn?.isVisible == true) {
                itemTimerNewSellerListener.onImpressWatchVideo()
            }
        }
    }
}