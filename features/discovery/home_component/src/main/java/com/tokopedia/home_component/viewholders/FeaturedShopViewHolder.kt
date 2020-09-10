package com.tokopedia.home_component.viewholders

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselFeaturedShopCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.adapter.FeaturedShopAdapter
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.home_component_lego_banner.view.home_component_header_view
import kotlinx.android.synthetic.main.home_featured_shop.view.*

/**
 * Created by Lukas on 07/09/20.
 */
class FeaturedShopViewHolder(
        itemView: View,
        private val listener: FeaturedShopListener,
        private val homeComponentListener: HomeComponentListener?,
        private val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null
) : AbstractViewHolder<FeaturedShopDataModel>(itemView), CommonProductCardCarouselListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_featured_shop
    }

    private lateinit var adapter: FeaturedShopAdapter

    override fun bind(element: FeaturedShopDataModel) {
        setHeaderComponent(element)
        initView(element)
        setHeaderComponent(element)
    }

    override fun bind(element: FeaturedShopDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initView(element: FeaturedShopDataModel) {
        initRecycleView()
        initItems(element)
    }

    private fun initRecycleView() {
        parentRecyclerViewPool?.let { itemView.dc_banner_rv?.setRecycledViewPool(parentRecyclerViewPool) }
    }

    private fun initItems(element: FeaturedShopDataModel) {
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(element.channelModel)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(CarouselEmptyCardDataModel(element.channelModel, adapterPosition, this))
        val productDataList = convertDataToProductData(element.channelModel)
        listData.addAll(productDataList)

        if(element.channelModel.channelGrids.size > 1 && element.channelModel.channelHeader.applink.isNotEmpty())
            listData.add(CarouselSeeMorePdpDataModel(element.channelModel.channelHeader.applink, element.channelModel.channelHeader.backImage, this))
        adapter = FeaturedShopAdapter(listData, typeFactoryImpl)
        itemView.dc_banner_rv?.adapter = adapter
    }

    private fun setHeaderComponent(element: FeaturedShopDataModel) {
        val ctaData = element.channelModel.channelBanner.cta
        itemView.featured_shop_background.setGradientBackground(element.channelModel.channelBanner.gradientColor)
        itemView.banner_title?.text = element.channelModel.channelBanner.title
        itemView.banner_description?.text = element.channelModel.channelBanner.description
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
        listener.onSeeAllBannerClicked(channel, adapterPosition)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        /* do nothing */
    }
}