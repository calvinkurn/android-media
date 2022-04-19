package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemHeaderShopPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemHeaderShopPerformanceViewHolder(
    view: View,
    private val shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<HeaderShopPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_shop_performance
        const val ROUNDED_RADIUS = 16F
    }

    private val binding: ItemHeaderShopPerformanceBinding? by viewBinding()

    override fun bind(element: HeaderShopPerformanceUiModel?) {
        setBackgroundRadiusHeader()
        binding?.headerPerformanceWidget?.setData(element, shopPerformanceListener)
    }

    private fun setBackgroundRadiusHeader() {
        binding?.run {
            val containerHeaderPerformance =
                headerPerformanceWidget.binding?.containerHeaderShopPerformance
            containerHeaderPerformance?.let {
                it.shapeAppearanceModel =
                    containerHeaderPerformance.shapeAppearanceModel
                        .toBuilder()
                        .setTopRightCorner(
                            CornerFamily.ROUNDED,
                            ROUNDED_RADIUS
                        )
                        .setTopLeftCorner(
                            CornerFamily.ROUNDED,
                            ROUNDED_RADIUS
                        )
                        .build()
            }
        }
    }
}