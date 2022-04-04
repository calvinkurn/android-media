package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcCueCategoryBinding
import com.tokopedia.home_component.decoration.CueWidgetCategoryItemDecoration
import com.tokopedia.home_component.decoration.MerchantVoucherDecoration
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.viewholders.adapter.CueWidgetCategoryAdapter
import com.tokopedia.home_component.viewholders.adapter.MerchantVoucherAdapter
import com.tokopedia.home_component.visitable.CueCategoryDataModel
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CueWidgetCategoryViewHolder (
    itemView: View
) : AbstractViewHolder<CueCategoryDataModel>(itemView) {
    private var binding: GlobalDcCueCategoryBinding? by viewBinding()
    private var adapter: CueWidgetCategoryAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_cue_category
        private const val TOTAL_SPAN_RECYCLER = 2
    }

    override fun bind(element: CueCategoryDataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        mappingView(element.channelModel)
    }

    private fun mappingView(channel: ChannelModel) {
        val visitables: MutableList<Visitable<*>> = mappingVisitablesFromChannel(channel)
        binding?.homeComponentCueCategoryRv?.setHasFixedSize(true)
        valuateRecyclerViewDecoration()
        mappingItem(channel, visitables)
    }

    private fun mappingVisitablesFromChannel(channel: ChannelModel): MutableList<Visitable<*>> {
        val visitables: MutableList<Visitable<*>> = mutableListOf()
        val channelMerchantVoucherData = convertDataToMerchantVoucherData(channel)

        return visitables
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel)
        adapter = CueWidgetCategoryAdapter(channel)
        binding?.run {
            homeComponentCueCategoryRv.adapter = adapter
//            homeComponentCueCategoryRv.layoutManager = GridLayoutManager(
//                itemView.context,
//                TOTAL_SPAN_RECYCLER,
//                GridLayoutManager.HORIZONTAL,
//                false
//            )
            val layoutManager = GridLayoutManager(itemView.context, 2)
            homeComponentCueCategoryRv.layoutManager = layoutManager

            if (homeComponentCueCategoryRv.itemDecorationCount == 0) {
                homeComponentCueCategoryRv.addItemDecoration(CueWidgetCategoryItemDecoration(8f.toDpInt()))
            }
        }
//        binding?.homeComponentMvcRv?.scrollToPosition(0)
    }

    private fun convertDataToMerchantVoucherData(channel: ChannelModel): List<CarouselMerchantVoucherDataModel> {
        val list :MutableList<CarouselMerchantVoucherDataModel> = mutableListOf()
        val positionWidget = "$adapterPosition"
        for (element in channel.channelGrids) {
//            list.add(
//                CarouselMerchantVoucherDataModel(
//                    shopName = element.shop.shopName,
//                    benefit = element.name,
//                    benefitPrice = element.benefit.value,
//                    totalOtherCoupon = element.label,
//                    iconBadge = if (element.badges.isNotEmpty()) element.badges[0].imageUrl else "",
//                    imageProduct = element.imageUrl,
//                    shopAppLink = element.shop.shopApplink,
//                    productAppLink = element.applink,
//                    merchantVoucherComponentListener = merchantVoucherComponentListener,
//                    shopId = element.shop.id,
//                    bannerId = channel.channelBanner.id,
//                    positionWidget = positionWidget,
//                    headerName = channel.channelHeader.name,
//                    userId = merchantVoucherComponentListener.getUserId(),
//                    couponType = element.name,
//                    productId = element.id,
//                    productPrice = element.price,
//                    buType = channel.trackingAttributionModel.galaxyAttribution,
//                    topAds = element.getTopadsString(),
//                    recommendationType = element.recommendationType,
//                )
//            )
        }
        return list
    }

    private fun valuateRecyclerViewDecoration() {

    }

    private fun setChannelDivider(element: CueCategoryDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setHeaderComponent(element: CueCategoryDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {

            }
            override fun onChannelExpired(channelModel: ChannelModel) {}
        })
    }
}