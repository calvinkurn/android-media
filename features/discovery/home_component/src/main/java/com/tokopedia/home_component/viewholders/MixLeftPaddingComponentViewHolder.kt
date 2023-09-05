package com.tokopedia.home_component.viewholders

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcMixLeftPaddingBinding
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.listener.BannerMixLeftPaddingListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselBannerCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.viewholders.adapter.MixTopComponentAdapter
import com.tokopedia.home_component.visitable.MixLeftPaddingDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by dhaba
 */
class MixLeftPaddingComponentViewHolder(
    itemView: View,
    val mixLeftComponentListener: MixLeftComponentListener?,
    val homeComponentListener: HomeComponentListener?,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<MixLeftPaddingDataModel>(itemView),
    CoroutineScope,
    CommonProductCardCarouselListener,
    BannerMixLeftPaddingListener {

    private var adapter: MixTopComponentAdapter? = null
    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main
    private var layoutManager: LinearLayoutManager? = null
    private var isCacheData = false
    private var binding: GlobalDcMixLeftPaddingBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_mix_left_padding
        private const val FPM_MIX_LEFT = "home_mix_left_padding"
    }

    override fun bind(element: MixLeftPaddingDataModel) {
        isCacheData = element.isCache
        setupList(element.channelModel)
        setSnapEffect()
        setHeaderComponent(element)
        setChannelDivider(element)

        itemView.addOnImpressionListener(element.channelModel) {
            if (!isCacheData) {
                mixLeftComponentListener?.onMixLeftImpressed(
                    element.channelModel,
                    element.channelModel.verticalPosition
                )
            }
        }
    }

    override fun bind(element: MixLeftPaddingDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onProductCardImpressed(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        if (!isCacheData) {
            mixLeftComponentListener?.onProductCardImpressed(
                channelModel,
                channelGrid,
                channelModel.verticalPosition,
                position
            )
        }
    }

    override fun onProductCardClicked(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        mixLeftComponentListener?.onProductCardClicked(
            channelModel,
            channelGrid,
            channelModel.verticalPosition,
            position,
            applink
        )
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        mixLeftComponentListener?.onSeeMoreCardClicked(channel, applink)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        mixLeftComponentListener?.onEmptyCardClicked(channel, applink, parentPos)
    }

    private fun setChannelDivider(element: MixLeftPaddingDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setupList(channel: ChannelModel) {
        binding?.rvProductMixLeftPadding?.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvProductMixLeftPadding?.layoutManager = layoutManager
        if (binding?.rvProductMixLeftPadding?.itemDecorationCount == 0) {
            binding?.rvProductMixLeftPadding?.addItemDecoration(
                SimpleHorizontalLinearLayoutDecoration()
            )
        }
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel, cardInteraction, this)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(
            CarouselBannerCardDataModel(
                channel,
                this
            )
        )
        val productDataList = convertDataToProductData(channel)
        listData.addAll(productDataList)

        launch {
            try {
                binding?.rvProductMixLeftPadding?.setHeightBasedOnProductCardMaxHeight(
                    productDataList.map { it.productModel }
                )
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }

        if (channel.channelGrids.isNotEmpty() && channel.channelHeader.applink.isNotEmpty()) {
            if (channel.channelViewAllCard.id != CarouselViewAllCardViewHolder.DEFAULT_VIEW_ALL_ID && channel.channelViewAllCard.contentType.isNotBlank() && channel.channelViewAllCard.contentType != CarouselViewAllCardViewHolder.CONTENT_DEFAULT) {
                listData.add(
                    CarouselViewAllCardDataModel(
                        channel.channelHeader.applink,
                        channel.channelViewAllCard,
                        this,
                        "",
                        arrayListOf(),
                        ""
                    )
                )
            } else {
                listData.add(
                    CarouselSeeMorePdpDataModel(
                        channel.channelHeader.applink,
                        channel.channelHeader.backImage,
                        this
                    )
                )
            }
        }
        adapter = MixTopComponentAdapter(listData, typeFactoryImpl)
        binding?.rvProductMixLeftPadding?.adapter = adapter
    }

    private fun setSnapEffect() {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(binding?.rvProductMixLeftPadding)
    }

    private fun convertDataToProductData(channel: ChannelModel): List<CarouselProductCardDataModel> {
        val list: MutableList<CarouselProductCardDataModel> = mutableListOf()
        for (element in channel.channelGrids) {
            list.add(
                CarouselProductCardDataModel(
                    ChannelModelMapper.mapToProductCardModel(element, cardInteraction),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = element,
                    applink = element.applink,
                    componentName = FPM_MIX_LEFT
                )
            )
        }
        return list
    }

    private fun RecyclerView.resetLayout() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
        productCardModelList: List<ProductCardModel>
    ) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        val productCardWidth =
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.product_card_flashsale_width)
        return productCardModelList.getMaxHeightForGridView(
            itemView.context,
            Dispatchers.Default,
            productCardWidth
        )
    }

    private fun setHeaderComponent(element: MixLeftPaddingDataModel) {
        binding?.homeComponentHeaderView?.setChannel(
            element.channelModel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    mixLeftComponentListener?.onSeeAllBannerClicked(
                        element.channelModel,
                        element.channelModel.channelHeader.applink
                    )
                }

                override fun onChannelExpired(channelModel: ChannelModel) {
                    homeComponentListener?.onChannelExpired(
                        channelModel,
                        element.channelModel.verticalPosition,
                        element
                    )
                }
            }
        )
    }

    override fun onBannerClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        mixLeftComponentListener?.onEmptyCardClicked(channel, applink, parentPos)
    }

    override fun onBannerImpressed(channelModel: ChannelModel) {
        mixLeftComponentListener?.onImageBannerImpressed(
            channelModel,
            channelModel.verticalPosition
        )
    }
}
