package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselViewAllCardData
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentFlashSaleBinding
import com.tokopedia.home_component.decoration.FlashSaleCarouselDecoration
import com.tokopedia.home_component.listener.FlashSaleWidgetListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.getGradientBackgroundViewAllWhite
import com.tokopedia.home_component.util.getHexColorFromIdColor
import com.tokopedia.home_component.util.loadImageWithDefault
import com.tokopedia.home_component.util.setGradientBackgroundRounded
import com.tokopedia.home_component.visitable.FlashSaleDataModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.viewallcard.ViewAllCard

/**
 * Created by frenzel
 */
class FlashSaleViewHolder(
    itemView: View,
    val flashSaleWidgetListener: FlashSaleWidgetListener?,
    val homeComponentListener: HomeComponentListener?,
    val parentRecycledViewPool: RecyclerView.RecycledViewPool? = null
) : AbstractViewHolder<FlashSaleDataModel>(itemView) {
    private var binding: HomeComponentFlashSaleBinding? by viewBinding()
    private val context = itemView.context
    private val flashSaleCarouselDecoration = FlashSaleCarouselDecoration()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_flash_sale
        private const val CONTENT_TITLE_AS_INTEGER = "title-as-integer"
        private const val className = "com.tokopedia.home_component.viewholders.FlashSaleViewHolder"
    }

    override fun bind(element: FlashSaleDataModel) {
        setupCarousel(element, parentRecycledViewPool)
        setHeaderComponent(element)
        setChannelDivider(element)
        setBannerBackground(element)
    }

    override fun bind(element: FlashSaleDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: FlashSaleDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setupCarousel(element: FlashSaleDataModel, parentRecycledViewPool: RecyclerView.RecycledViewPool?) {
        val channelGrids = element.channelModel.channelGrids
        val productCardList = channelGrids.map { ChannelModelMapper.mapToProductCardModel(it, animateOnPress = CardUnify2.ANIMATE_NONE, isTopStockbar = true, cardType = CardUnify2.TYPE_CLEAR) }
        binding?.run {
            carouselFlashSale.bindCarouselProductCardViewGrid(
                productCardModelList = productCardList,
                recyclerViewPool = parentRecycledViewPool,
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(
                        productCardModel: ProductCardModel,
                        carouselProductCardPosition: Int
                    ) {
                        channelGrids.getOrNull(carouselProductCardPosition)?.let {
                            if (it.isTopads) {
                                TopAdsUrlHitter(className).hitImpressionUrl(
                                    context,
                                    it.impression,
                                    it.id,
                                    it.name,
                                    it.imageUrl
                                )
                            }
                            flashSaleWidgetListener?.onProductCardImpressed(element.channelModel, it)
                        }
                    }

                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return channelGrids.getOrNull(carouselProductCardPosition)
                    }
                },
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(
                        productCardModel: ProductCardModel,
                        carouselProductCardPosition: Int
                    ) {
                        channelGrids.getOrNull(carouselProductCardPosition)?.let {
                            if (it.isTopads) {
                                TopAdsUrlHitter(className).hitImpressionUrl(
                                    context,
                                    it.productClickUrl,
                                    it.id,
                                    it.name,
                                    it.imageUrl
                                )
                            }
                            flashSaleWidgetListener?.onProductCardClicked(element.channelModel, it, it.applink)
                        }
                    }
                },
                carouselViewAllCardData = CarouselViewAllCardData(
                    title = element.channelModel.channelViewAllCard.title,
                    description = element.channelModel.channelViewAllCard.description,
                    viewAllCardMode = ViewAllCard.MODE_INVERT,
                    titleIsInteger = element.channelModel.channelViewAllCard.contentType == CONTENT_TITLE_AS_INTEGER,
                    ctaText = itemView.context.getString(R.string.lihat_semua)
                ),
                carouselViewAllCardClickListener = object : CarouselProductCardListener.OnViewAllCardClickListener {
                    override fun onViewAllCardClick() {
                        flashSaleWidgetListener?.onViewAllCardClicked(element.channelModel, element.channelModel.channelHeader.applink)
                    }
                },
                customItemDecoration = flashSaleCarouselDecoration
            )
        }
    }

    private fun setHeaderComponent(element: FlashSaleDataModel) {
        binding?.homeComponentHeaderView?.setChannel(
            element.channelModel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    flashSaleWidgetListener?.onSeeAllClicked(
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

    private fun setBannerBackground(element: FlashSaleDataModel) {
        element.channelModel.channelBanner.let {
            if (it.gradientColor.isNotEmpty() && !getGradientBackgroundViewAllWhite(it.gradientColor, context)) {
                binding?.containerFlashSale?.setGradientBackgroundRounded(it.gradientColor)
            } else {
                val defaultGradient = arrayListOf(
                    getHexColorFromIdColor(context, R.color.dms_flash_sale_default_container_start),
                    getHexColorFromIdColor(context, R.color.dms_flash_sale_default_container_end)
                )
                binding?.containerFlashSale?.setGradientBackgroundRounded(defaultGradient)
            }
            if (it.imageUrl.isNotEmpty()) {
                binding?.supergraphicFlashSale?.loadImageWithDefault(element.channelModel.channelBanner.imageUrl, defaultImage = R.drawable.supergraphic_flash_sale_default)
            }
        }
    }
}
