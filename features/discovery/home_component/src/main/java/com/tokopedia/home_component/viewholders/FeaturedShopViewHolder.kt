package com.tokopedia.home_component.viewholders

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.CommonMarginStartDecoration
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselFeaturedShopCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.adapter.FeaturedShopAdapter
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.home_featured_shop.view.*

/**
 * Created by Lukas on 07/09/20.
 */
class FeaturedShopViewHolder(
        itemView: View,
        private val listener: FeaturedShopListener,
        private val homeComponentListener: HomeComponentListener?
) : AbstractViewHolder<FeaturedShopDataModel>(itemView), CommonProductCardCarouselListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_featured_shop
    }

    private lateinit var adapter: FeaturedShopAdapter

    override fun bind(element: FeaturedShopDataModel) {
        if(element.channelModel.channelGrids.size < 2){
            itemView.content_container?.hide()
        } else {
            itemView.content_container?.show()
            setHeaderComponent(element)
            setChannelDivider(element)
            initView(element)
        }
    }

    override fun bind(element: FeaturedShopDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initView(element: FeaturedShopDataModel) {
        initItems(element)
    }

    private fun setChannelDivider(element: FeaturedShopDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun initItems(element: FeaturedShopDataModel)
    {
        if (itemView.dc_banner_rv.itemDecorationCount == 0) {
            itemView.dc_banner_rv.addItemDecoration(
                    CommonMarginStartDecoration(
                            marginStart = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_14)
                    )
            )
        }
        val listData = mutableListOf<Visitable<*>>()
        val productDataList = convertDataToProductData(element.channelModel)
        listData.addAll(productDataList)
        if (element.channelModel.channelGrids.size > 1 && element.channelModel.channelHeader.applink.isNotEmpty())
            listData.add(CarouselSeeMorePdpDataModel(element.channelModel.channelHeader.applink, element.channelModel.channelHeader.backImage, this))

        if(!this::adapter.isInitialized) {
            val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(element.channelModel)
            adapter = FeaturedShopAdapter(listData, typeFactoryImpl)
            itemView.dc_banner_rv?.adapter = adapter
        } else {
            adapter.clearAllElements()
            adapter.setElement(listData)
        }
    }

    private fun setHeaderComponent(element: FeaturedShopDataModel) {
        var textColor = ContextCompat.getColor(itemView.context, R.color.Unify_N50)
        if(element.channelModel.channelBanner.textColor.isNotEmpty()){
            try {
                textColor = Color.parseColor(element.channelModel.channelBanner.textColor)
            } catch (e: IllegalArgumentException) { }
        }
        itemView.featured_shop_background.setGradientBackground(element.channelModel.channelBanner.gradientColor)
        itemView.featured_shop_background.setOnClickListener{
            listener.onFeaturedShopBannerBackgroundClicked(element.channelModel)
        }
        itemView.banner_title?.text = element.channelModel.channelBanner.title
        itemView.banner_description?.text = element.channelModel.channelBanner.description
        itemView.banner_title?.setTextColor(textColor)
        itemView.banner_description?.setTextColor(textColor)
        itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                listener.onSeeAllClicked(element.channelModel, adapterPosition)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }

    private fun convertDataToProductData(channel: ChannelModel): List<CarouselFeaturedShopCardDataModel> {
        val list :MutableList<CarouselFeaturedShopCardDataModel> = mutableListOf()
        for (element in channel.channelGrids) {
            list.add(CarouselFeaturedShopCardDataModel(
                    grid = element,
                    applink = channel.channelHeader.applink,
                    componentName = this::class.java.simpleName,
                    listener = this
            ))
        }
        return list
    }

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        listener.onFeaturedShopItemImpressed(channel, channelGrid, position, adapterPosition)
    }

    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) {
        listener.onFeaturedShopItemClicked(channel, channelGrid, position, adapterPosition)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        listener.onSeeAllBannerClicked(channel, applink, adapterPosition)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        /* do nothing */
    }
}