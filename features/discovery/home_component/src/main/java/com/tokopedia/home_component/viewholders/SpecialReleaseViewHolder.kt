package com.tokopedia.home_component.viewholders

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseBinding
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.SpecialReleaseComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel.Companion.CAROUEL_ITEM_SPECIAL_RELEASE_TIMER_BIND
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.SpecialReleaseItemViewHolder
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.calculator.calculateHeight
import com.tokopedia.home_component.util.*
import com.tokopedia.home_component.viewholders.adapter.SpecialReleaseAdapter
import com.tokopedia.home_component.visitable.SpecialReleaseDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SpecialReleaseViewHolder(
        itemView: View,
        val homeComponentListener: HomeComponentListener?,
        val specialReleaseComponentListener: SpecialReleaseComponentListener?
) : AbstractViewHolder<SpecialReleaseDataModel>(itemView), CommonProductCardCarouselListener,
    CoroutineScope {
    private var binding: HomeComponentSpecialReleaseBinding? by viewBinding()
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }
    private var adapter: SpecialReleaseAdapter? = null
    private var isCacheData = false
    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main
    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_component_special_release
    }

    override fun bind(element: SpecialReleaseDataModel) {
        isCacheData = element.isCache
        mappingView(channel = element.channelModel)
        setHeaderComponent(element = element)
        setChannelDivider(element = element)
        setBackground(element = element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                specialReleaseComponentListener?.onSpecialReleaseChannelImpressed(element.channelModel, adapterPosition)
            }
        }
    }

    override fun bind(element: SpecialReleaseDataModel, payloads: MutableList<Any>) {
        if (payloads.size > 0) {
            val payload = payloads[0]
            if (payload is Bundle) {
                val bundle = payload as Bundle
                if (bundle.getBoolean(SpecialReleaseDataModel.SPECIAL_RELEASE_TIMER_BIND, false)) {
                    adjustGridTimer()
                    return
                }
            }
        }
        bind(element)
    }

    private fun setBackground(element: SpecialReleaseDataModel) {
        val bannerItem = element.channelModel.channelBanner
        if (bannerItem.gradientColor.isEmpty() || getGradientBackgroundViewAllWhite(
                bannerItem.gradientColor,
                itemView.context
            )
        ) {
            binding?.background?.gone()
        } else {
            binding?.background?.visible()
            binding?.background?.setGradientBackground(bannerItem.gradientColor)
        }
    }

    private fun adjustGridTimer() {
        for (i in 0..(binding?.homeComponentSpecialReleaseRv?.childCount?:0)) {
            val childView = binding?.homeComponentSpecialReleaseRv?.getChildAt(i)
            childView?.let {
                val holder = binding?.homeComponentSpecialReleaseRv?.getChildViewHolder(childView)
                holder?.let {
                    if (it is SpecialReleaseItemViewHolder) {
                        adapter?.notifyItemChanged(it.adapterPosition, Bundle().apply {
                            putBoolean(CAROUEL_ITEM_SPECIAL_RELEASE_TIMER_BIND, true)
                        })
                    }
                }
            }
        }
    }

    private fun setChannelDivider(element: SpecialReleaseDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun mappingView(channel: ChannelModel) {
        valuateRecyclerViewDecoration()
        val carouselDataModelList: MutableList<CarouselSpecialReleaseDataModel> = channel.channelGrids.map {
            CarouselSpecialReleaseDataModel(it, adapterPosition, this, channel)
        }.toMutableList()

//        launch {
//            try {
//                binding?.homeComponentSpecialReleaseRv?.
//                setHeightBasedOnProductCardMaxHeight(carouselDataModelList)
//            }
//            catch (throwable: Throwable) {
//                throwable.printStackTrace()
//            }
//        }

        val visitableList: MutableList<Visitable<*>> = carouselDataModelList.map { it }.toMutableList()
        if(channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty()) {
            if(channel.channelViewAllCard.id != CarouselViewAllCardViewHolder.DEFAULT_VIEW_ALL_ID && channel.channelViewAllCard.contentType.isNotBlank() && channel.channelViewAllCard.contentType != CarouselViewAllCardViewHolder.CONTENT_DEFAULT) {
                visitableList.add(
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
                visitableList.add(CarouselSeeMorePdpDataModel(channel.channelHeader.applink, channel.channelHeader.backImage, this))
            }
        }
        mappingItem(channel, visitableList)
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
        cardModelList: List<CarouselSpecialReleaseDataModel>) {
        val cardHeight = cardModelList.calculateHeight(context, Dispatchers.Default)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = cardHeight
        this.layoutParams = carouselLayoutParams
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        startSnapHelper.attachToRecyclerView(binding?.homeComponentSpecialReleaseRv)
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel)
        adapter = SpecialReleaseAdapter(visitables, typeFactoryImpl)
        binding?.homeComponentSpecialReleaseRv?.adapter = adapter
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.homeComponentSpecialReleaseRv?.itemDecorationCount == 0) binding?.homeComponentSpecialReleaseRv?.addItemDecoration(SimpleHorizontalLinearLayoutDecoration())
        binding?.homeComponentSpecialReleaseRv?.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        /**
         * Attach startSnapHelper to recyclerView
         */
        startSnapHelper.attachToRecyclerView(binding?.homeComponentSpecialReleaseRv)
    }

    private fun setHeaderComponent(element: SpecialReleaseDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                specialReleaseComponentListener?.onSpecialReleaseItemSeeAllClicked(element.channelModel, link)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        specialReleaseComponentListener?.onSpecialReleaseItemImpressed(
            grid = channelGrid,
            channelModel = channel,
            position = position
        )
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        specialReleaseComponentListener?.onSpecialReleaseItemClicked(
            grid = channelGrid,
            channelModel = channel,
            position = position,
            applink = applink
        )
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        specialReleaseComponentListener?.onSpecialReleaseItemSeeAllClicked(
            channelModel = channel,
            applink = applink
        )
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {}
}