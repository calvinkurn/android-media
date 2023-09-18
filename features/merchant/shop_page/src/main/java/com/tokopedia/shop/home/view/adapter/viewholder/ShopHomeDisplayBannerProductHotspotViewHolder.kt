package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousellayoutmanager.CarouselLayoutManager
import com.tokopedia.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.tokopedia.carousellayoutmanager.CenterScrollListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.customview.bannerhotspot.ImageHotspotView
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ShopHomeDisplayBannerProductHotspotViewHolderLayoutBinding
import com.tokopedia.shop.home.view.adapter.ShopWidgetProductHotspotAdapter
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding


class ShopHomeDisplayBannerProductHotspotViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ShopWidgetDisplayBannerProductHotspotUiModel>(itemView),
    ImageHotspotView.Listener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_home_display_banner_product_hotspot_view_holder_layout
        private const val MAX_VISIBLE_ITEM_CAROUSEL = 3
        private const val DEFAULT_RATIO = "1:1"
    }

    interface Listener {
        fun onClickProductBannerHotspot(
            uiModel: ShopWidgetDisplayBannerProductHotspotUiModel,
            bannerItemUiModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data,
            imageBannerPosition: Int,
            bubblePosition: Int
        )

        fun onImpressionBannerHotspotImage(
            uiModel: ShopWidgetDisplayBannerProductHotspotUiModel,
            bannerItemUiModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data,
            imageBannerPosition: Int
        )
    }

    private val viewBinding: ShopHomeDisplayBannerProductHotspotViewHolderLayoutBinding? by viewBinding()
    private val placeholderContainer: View? = viewBinding?.containerPlaceholder
    private val contentContainer: View? = viewBinding?.containerContent
    private val imageBannerHotspot: ImageHotspotView? = viewBinding?.imageBannerHotspot
    private val recyclerViewProductHotspot: RecyclerView? = viewBinding?.rvProductHotspot
    private val textTitle: Typography? = viewBinding?.textTitle
    private val bannerIndicator: PageControl? = viewBinding?.pageControl
    private var adapterShopWidgetProductHotspot: ShopWidgetProductHotspotAdapter? = null
    private var uiModel: ShopWidgetDisplayBannerProductHotspotUiModel? = null
    private val itemSelectListener: CarouselLayoutManager.OnCenterItemSelectionListener =
        CarouselLayoutManager.OnCenterItemSelectionListener { adapterPosition ->
            bannerIndicator?.setCurrentIndicator(
                adapterPosition
            )
        }

    override fun bind(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel) {
        this.uiModel = uiModel
        if (uiModel.isWidgetShowPlaceholder()) {
            showPlaceholderView()
        } else {
            showContentView()
            setHeaderSection()
            setImageBannerHotspotSection()
            setBannerIndicatorSection()
            configColorTheme()
        }
    }

    private fun configColorTheme() {
        if (uiModel?.header?.isOverrideTheme == true) {
            configReimaginedColor(uiModel?.header?.colorSchema ?: ShopPageColorSchema())
        } else {
            configDefaultColor()
        }
    }

    private fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        textTitle?.setTextColor(titleColor)
    }

    private fun configDefaultColor() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
        )
        textTitle?.setTextColor(titleColor)
    }

    private fun showContentView() {
        placeholderContainer?.hide()
        contentContainer?.show()
    }

    private fun showPlaceholderView() {
        placeholderContainer?.show()
        contentContainer?.hide()
    }

    private fun setBannerIndicatorSection() {
        bannerIndicator?.setIndicator(uiModel?.data?.size.orZero())
    }

    private fun setImageBannerHotspotSection() {
        uiModel?.let {
            val dataSize = it.data.size
            if (dataSize > Int.ONE) {
                setupViewForMoreThanOneImageBanner()
                setupAdapter(it)
                initRecyclerView(it)
            } else if (dataSize == Int.ONE) {
                setupViewForOnlyOneImageBanner()
                setupImageBannerHotspotData(it)
                addImpressionForOneBanner(it)
            }
        }
    }

    private fun addImpressionForOneBanner(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel) {
        uiModel.data.firstOrNull()?.let { bannerItemUiModel ->
            imageBannerHotspot?.addOnImpressionListener(bannerItemUiModel) {
                listener.onImpressionBannerHotspotImage(
                    uiModel,
                    bannerItemUiModel,
                    Int.ZERO
                )
            }
        }
    }

    private fun setHeaderSection() {
        val title = uiModel?.header?.title.orEmpty()
        textTitle?.shouldShowWithAction(title.isNotEmpty()) {
            textTitle.text = title
        }
    }

    private fun setupImageBannerHotspotData(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel) {
        val ratio = uiModel.header.ratio.takeIf { it.isNotEmpty() } ?: DEFAULT_RATIO
        (imageBannerHotspot?.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = ratio
        uiModel.data.firstOrNull()?.let {
            imageBannerHotspot?.setData(
                ImageHotspotData(
                    imageBannerUrl = it.imageUrl,
                    listHotspot = it.listProductHotspot.map { productHotspot ->
                        ImageHotspotData.HotspotData(
                            x = productHotspot.hotspotCoordinate.x,
                            y = productHotspot.hotspotCoordinate.y,
                            productImage = productHotspot.imageUrl,
                            productName = productHotspot.name,
                            productPrice = productHotspot.displayedPrice,
                        )
                    },
                ),
                listenerBubbleView = this,
                ratio = ratio
            )
        }
    }

    private fun setupViewForMoreThanOneImageBanner() {
        imageBannerHotspot?.hide()
        recyclerViewProductHotspot?.show()
        bannerIndicator?.show()
    }

    private fun setupViewForOnlyOneImageBanner() {
        imageBannerHotspot?.show()
        recyclerViewProductHotspot?.hide()
        bannerIndicator?.hide()
    }

    private fun setupAdapter(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel) {
        adapterShopWidgetProductHotspot = ShopWidgetProductHotspotAdapter(listener, uiModel)
        adapterShopWidgetProductHotspot?.submit(uiModel.data)
    }

    private fun initRecyclerView(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel) {
        val ratio = uiModel.header.ratio.takeIf { it.isNotEmpty() } ?: DEFAULT_RATIO
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        layoutManager.maxVisibleItems = MAX_VISIBLE_ITEM_CAROUSEL
        layoutManager.removeOnItemSelectionListener(itemSelectListener)
        layoutManager.addOnItemSelectionListener(itemSelectListener)
        recyclerViewProductHotspot?.apply {
            isNestedScrollingEnabled = false
            (this@apply.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = ratio
            this.layoutManager = layoutManager
            this.setHasFixedSize(true)
            this.addOnScrollListener(CenterScrollListener())
            this.adapter = adapterShopWidgetProductHotspot
        }
        updateRecyclerViewHeightBasedOnFirstChild()
    }

    private fun updateRecyclerViewHeightBasedOnFirstChild() {
        recyclerViewProductHotspot?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val firstChildHeight = recyclerViewProductHotspot.findViewHolderForAdapterPosition(
                    Int.ZERO
                )?.itemView?.height.orZero()
                val lp = recyclerViewProductHotspot.layoutParams as? ViewGroup.LayoutParams
                lp?.height = firstChildHeight
                recyclerViewProductHotspot.layoutParams = lp
                recyclerViewProductHotspot.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun onBubbleViewClicked(
        hotspotData: ImageHotspotData.HotspotData,
        view: View,
        index: Int
    ) {
        //value should always be 0 because this one is for when we only have 1 image banner
        uiModel?.let {
            it.data.firstOrNull()?.let { bannerItemUiModel ->
                listener.onClickProductBannerHotspot(
                    it,
                    bannerItemUiModel,
                    Int.ZERO,
                    index
                )
            }
        }
    }

}
