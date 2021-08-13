package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_IMAGE_SLIDER_SQUARE_TRACE
import com.tokopedia.shop.home.ShopCarouselBannerImageUnify
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class ShopHomeItemSliderSquareViewHolder(
        itemView: View,
        private val listener: ShopHomeDisplayWidgetListener,
        private val heightRatio: Float
): RecyclerView.ViewHolder(itemView) {

    companion object{
        const val ITEM_WIDTH_RATIO_DIVIDER = 1.8
    }

    var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    var parentPosition: Int = 0
    private val ivSliderSquare: ShopCarouselBannerImageUnify = itemView.findViewById(R.id.ivSliderSquare)
    private var performanceMonitoring: PerformanceMonitoring? = null

    fun bind(data: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        performanceMonitoring = PerformanceMonitoring.start(SHOP_HOME_IMAGE_SLIDER_SQUARE_TRACE)
        //avoid crash in ShopCarouselBannerImageUnify when image url is returned as base64
        try {
            if(ivSliderSquare.context.isValidGlideContext()) {
                ivSliderSquare.heightRatio = heightRatio
                ivSliderSquare.setImageUrl(data.imageUrl, heightRatio = heightRatio)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ivSliderSquare.onUrlLoaded = {
            performanceMonitoring?.stopTrace() ?: Unit
        }
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels;
        itemView.layoutParams.width = (deviceWidth / ITEM_WIDTH_RATIO_DIVIDER).toInt()
        ivSliderSquare.setOnClickListener {
            listener.onDisplayItemClicked(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
        ivSliderSquare.addOnImpressionListener(data) {
            listener.onDisplayItemImpression(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
    }
}