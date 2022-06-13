package com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemHeaderParameterDetailBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.adapter.tablet.DetailPerformanceAdapterTabletTypeFactory
import com.tokopedia.shop.score.performance.presentation.adapter.tablet.DetailPerformanceTabletAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemHeaderShopPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.BaseParameterDetail
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemHeaderParameterDetailUiModel

class ItemHeaderParameterPerformanceViewHolder(
    view: View,
    private val shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<ItemHeaderParameterDetailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_parameter_detail
    }

    private val binding = ItemHeaderParameterDetailBinding.bind(itemView)

    private val detailPerformanceAdapterTypeFactory by lazy {
        DetailPerformanceAdapterTabletTypeFactory(
            shopPerformanceListener
        )
    }

    private var detailPerformanceTabletAdapter: DetailPerformanceTabletAdapter? = null

    override fun bind(element: ItemHeaderParameterDetailUiModel) {
        setHeaderWidgetData(element.headerShopPerformanceUiModel)
        setDetailPerformanceAdapter(element.detailParameterList)
    }

    private fun setHeaderWidgetData(data: HeaderShopPerformanceUiModel) {
        setBackgroundRadiusHeader()
        binding.headerPerformanceWidget.setData(data, shopPerformanceListener)
    }

    private fun setBackgroundRadiusHeader() {
        binding.run {
            val containerHeaderPerformance =
                headerPerformanceWidget.binding?.containerHeaderShopPerformance
            containerHeaderPerformance?.let {
                it.shapeAppearanceModel =
                    containerHeaderPerformance.shapeAppearanceModel
                        .toBuilder()
                        .setBottomLeftCorner(
                            CornerFamily.ROUNDED,
                            ItemHeaderShopPerformanceViewHolder.ROUNDED_RADIUS
                        )
                        .setTopLeftCorner(
                            CornerFamily.ROUNDED,
                            ItemHeaderShopPerformanceViewHolder.ROUNDED_RADIUS
                        )
                        .build()
            }
        }
    }

    private fun setDetailPerformanceAdapter(parameterDetailList: List<BaseParameterDetail>) {
        detailPerformanceTabletAdapter =
            DetailPerformanceTabletAdapter(detailPerformanceAdapterTypeFactory)
        binding.rvParameterDetailTablet.run {
            layoutManager = LinearLayoutManager(context)
            adapter = detailPerformanceTabletAdapter
        }
        detailPerformanceTabletAdapter?.setDetailPerformanceData(parameterDetailList)
    }
}