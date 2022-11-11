package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcMerchantVoucherBinding
import com.tokopedia.home_component.decoration.MerchantVoucherDecoration
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.home_component.viewholders.adapter.MerchantVoucherAdapter
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.utils.view.binding.viewBinding

class MerchantVoucherViewHolder(
    itemView: View,
    private val merchantVoucherComponentListener: MerchantVoucherComponentListener,
    private val cardInteraction: Boolean = false,
) : AbstractViewHolder<MerchantVoucherDataModel>(itemView), CommonProductCardCarouselListener {
    private var binding: GlobalDcMerchantVoucherBinding? by viewBinding()
    private var adapter: MerchantVoucherAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_merchant_voucher
    }

    override fun bind(element: MerchantVoucherDataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        mappingView(element.channelModel)
    }

    private fun mappingView(channel: ChannelModel) {
        val visitables: MutableList<Visitable<*>> = mappingVisitablesFromChannel(channel)
        binding?.homeComponentMvcRv?.setHasFixedSize(true)
        valuateRecyclerViewDecoration()
        mappingItem(channel, visitables)
    }

    private fun mappingVisitablesFromChannel(channel: ChannelModel): MutableList<Visitable<*>> {
        val visitables: MutableList<Visitable<*>> = mutableListOf()
        val channelMerchantVoucherData = convertDataToMerchantVoucherData(channel)
        visitables.addAll(channelMerchantVoucherData)
        if(channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty()) {
            if (channel.channelViewAllCard.id != CarouselViewAllCardViewHolder.DEFAULT_VIEW_ALL_ID && channel.channelViewAllCard.contentType.isNotBlank() && channel.channelViewAllCard.contentType != CarouselViewAllCardViewHolder.CONTENT_DEFAULT) {
                visitables.add(
                    CarouselViewAllCardDataModel(
                        channel.channelHeader.applink,
                        channel.channelViewAllCard,
                        this,
                        channel.channelBanner.imageUrl,
                        channel.channelBanner.gradientColor,
                        channel.layout
                    )
                )
            }
            else {
                visitables.add(
                    CarouselSeeMorePdpDataModel(
                        channel.channelHeader.applink,
                        channel.channelHeader.backImage,
                        this
                    )
                )
            }
        }
        return visitables
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel, cardInteraction)
        adapter = MerchantVoucherAdapter(visitables, typeFactoryImpl)
        binding?.homeComponentMvcRv?.adapter = adapter
        binding?.homeComponentMvcRv?.scrollToPosition(0)
    }

    private fun convertDataToMerchantVoucherData(channel: ChannelModel): List<CarouselMerchantVoucherDataModel> {
        val list :MutableList<CarouselMerchantVoucherDataModel> = mutableListOf()
        val positionWidget = "$adapterPosition"
        for (element in channel.channelGrids) {
            list.add(
                CarouselMerchantVoucherDataModel(
                    shopName = element.shop.shopName,
                    benefit = element.name,
                    benefitPrice = element.benefit.value,
                    totalOtherCoupon = element.label,
                    iconBadge = if (element.badges.isNotEmpty()) element.badges[0].imageUrl else "",
                    imageProduct = element.imageUrl,
                    shopAppLink = element.shop.shopApplink,
                    productAppLink = element.applink,
                    merchantVoucherComponentListener = merchantVoucherComponentListener,
                    shopId = element.shop.id,
                    bannerId = channel.channelBanner.id,
                    positionWidget = positionWidget,
                    headerName = channel.channelHeader.name,
                    userId = merchantVoucherComponentListener.getUserId(),
                    couponType = element.name,
                    productId = element.id,
                    productPrice = element.price,
                    buType = channel.trackingAttributionModel.galaxyAttribution,
                    topAds = element.getTopadsString(),
                    recommendationType = element.recommendationType,
                    campaignCode = element.campaignCode.ifEmpty { channel.trackingAttributionModel.campaignCode },
                    channelId = channel.id,
                    attribution = element.attribution,
                    brandId = channel.trackingAttributionModel.brandId
                )
            )
        }
        return list
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.homeComponentMvcRv?.itemDecorationCount == 0) binding?.homeComponentMvcRv?.addItemDecoration(
            MerchantVoucherDecoration()
        )
        binding?.homeComponentMvcRv?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun setChannelDivider(element: MerchantVoucherDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setHeaderComponent(element: MerchantVoucherDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                merchantVoucherComponentListener.onViewAllClicked(
                    element.channelModel.id,
                    element.channelModel.channelHeader.name,
                    link,
                    merchantVoucherComponentListener.getUserId(),
                    element.channelModel.trackingAttributionModel.campaignCode
                )
            }
            override fun onChannelExpired(channelModel: ChannelModel) {}
        })
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {}

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {}

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        merchantVoucherComponentListener.onViewAllCardClicked(
            channel.id,
            channel.channelHeader.name,
            channel.channelHeader.applink,
            merchantVoucherComponentListener.getUserId(),
            channel.trackingAttributionModel.campaignCode
        )
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {}
}
