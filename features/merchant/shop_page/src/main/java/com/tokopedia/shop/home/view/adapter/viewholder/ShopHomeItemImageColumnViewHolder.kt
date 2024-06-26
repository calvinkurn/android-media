package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_IMAGE_MULTIPLE_COLUMN_TRACE
import com.tokopedia.shop.common.view.ShopCarouselBannerImageUnify
import com.tokopedia.shop.databinding.ItemShopHomePageImageColumnBinding
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeItemImageColumnViewHolder(
    itemView: View,
    val listener: ShopHomeDisplayWidgetListener,
    private val heightRatio: Float,
    private val imageColumnRoundedSize: Float
) : RecyclerView.ViewHolder(itemView) {

    var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    var parentPosition: Int = 0
    private val viewBinding: ItemShopHomePageImageColumnBinding? by viewBinding()
    private val ivMultipleColumn: ShopCarouselBannerImageUnify? = viewBinding?.ivMultipleColumn
    private var performanceMonitoring: PerformanceMonitoring? = null

    fun bind(data: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        performanceMonitoring = PerformanceMonitoring.start(SHOP_HOME_IMAGE_MULTIPLE_COLUMN_TRACE)
        // avoid crash in ShopCarouselBannerImageUnify when image url is returned as base64
        try {
            if (ivMultipleColumn?.context.isValidGlideContext()) {
                ivMultipleColumn?.heightRatio = heightRatio
                ivMultipleColumn?.cornerRadius = imageColumnRoundedSize.toInt()
                ivMultipleColumn?.setImageUrl(data.imageUrl, heightRatio = heightRatio)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ivMultipleColumn?.onUrlLoaded = {
            performanceMonitoring?.stopTrace() ?: Unit
        }
        ivMultipleColumn?.setOnClickListener {
            listener.onDisplayItemClicked(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
        ivMultipleColumn?.addOnImpressionListener(data) {
            listener.onDisplayItemImpression(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
    }
}
