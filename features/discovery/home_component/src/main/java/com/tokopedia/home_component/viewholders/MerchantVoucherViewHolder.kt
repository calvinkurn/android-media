package com.tokopedia.home_component.viewholders

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.home_component.viewholders.adapter.MerchantVoucherAdapter
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MerchantVoucherViewHolder(
    itemView: View,
    private val merchantVoucherComponentListener: MerchantVoucherComponentListener
) : AbstractViewHolder<MerchantVoucherDataModel>(itemView), CommonProductCardCarouselListener,
    CoroutineScope {
    private var binding: GlobalDcMerchantVoucherBinding? by viewBinding()
    private var adapter: MerchantVoucherAdapter? = null
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }

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
        binding?.rvBanner?.setHasFixedSize(true)
        valuateRecyclerViewDecoration()
        mappingItem(channel, visitables)
    }

    private fun mappingVisitablesFromChannel(channel: ChannelModel): MutableList<Visitable<*>> {
        val visitables: MutableList<Visitable<*>> = mutableListOf()
        val channelMerchantVoucherData = convertDataToMerchantVoucherData(channel)
        setRecyclerViewAndCardHeight()
        visitables.addAll(channelMerchantVoucherData)
        if(channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty()) {
            if(channel.channelViewAllCard.id != CarouselViewAllCardViewHolder.DEFAULT_VIEW_ALL_ID && channel.channelViewAllCard.contentType.isNotBlank() && channel.channelViewAllCard.contentType != CarouselViewAllCardViewHolder.CONTENT_DEFAULT) {
//                visitables.add(
//                    CarouselViewAllCardDataModel(
//                        channel.channelHeader.applink,
//                        channel.channelViewAllCard,
//                        this,
//                        channel.channelBanner.imageUrl,
//                        channel.channelBanner.gradientColor,
//                        channel.layout
//                    )
//                )
            }
        }
        return visitables
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        startSnapHelper.attachToRecyclerView(binding?.rvBanner)
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel)
        adapter = MerchantVoucherAdapter(visitables, typeFactoryImpl)
        binding?.rvBanner?.adapter = adapter
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
                    shopAppLink = element.shop.shopAppLink,
                    productAppLink = element.applink,
                    merchantVoucherComponentListener = merchantVoucherComponentListener,
                    shopId = element.shop.id,
                    bannerId = channel.channelBanner.id,
                    positionWidget = positionWidget,
                    headerName = channel.channelHeader.name,
                    userId = merchantVoucherComponentListener.getUserId(),
                    couponCode = "",
                    couponType = element.name,
                    creativeName = "",
                    productId = element.id,
                    productName = "",
                    productVariant = "",
                    productPrice = element.price,
                    productBrand = "",
                    buType = channel.trackingAttributionModel.galaxyAttribution,
                    topAds = element.getTopadsString(),
                    carousel = "",
                    recommendationType = element.recommendationType,
                    recomPageName = "",
                    catNameLevel1 = "",
                    catNameLevel2 = "",
                    catNameLevel3 = ""
                )
            )
        }
        return list
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.rvBanner?.itemDecorationCount == 0) binding?.rvBanner?.addItemDecoration(
            MerchantVoucherDecoration()
        )
        binding?.rvBanner?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        startSnapHelper.attachToRecyclerView(binding?.rvBanner)
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
                    element.channelModel.channelHeader.name,
                    link,
                    merchantVoucherComponentListener.getUserId()
                )
            }
            override fun onChannelExpired(channelModel: ChannelModel) {}
        })
    }

    private fun setRecyclerViewAndCardHeight() {
        launch {
            try {
                binding?.rvBanner?.setHeightBasedMerchantVoucherCardHeight()
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun RecyclerView.setHeightBasedMerchantVoucherCardHeight() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = this.resources.getDimensionPixelOffset(com.tokopedia.home_component.R.dimen.home_merchant_voucher_height)
        this.layoutParams = carouselLayoutParams
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        merchantVoucherComponentListener.onViewAllClicked(
            channel.channelHeader.name,
            channel.channelHeader.applink,
            merchantVoucherComponentListener.getUserId()
        )
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {}

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main
}