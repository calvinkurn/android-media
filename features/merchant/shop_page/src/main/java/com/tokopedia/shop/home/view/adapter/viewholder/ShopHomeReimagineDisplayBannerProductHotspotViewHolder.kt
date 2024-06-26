package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousellayoutmanager.CarouselHorizontalFlingSwipeEffect
import com.tokopedia.carousellayoutmanager.CarouselLayoutManager
import com.tokopedia.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.tokopedia.carousellayoutmanager.CenterScrollListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.customview.bannerhotspot.ImageHotspotView
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ShopHomeDisplayBannerProductHotspotViewHolderLayoutBinding
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.view.adapter.ShopWidgetProductHotspotAdapter
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopHomeReimagineDisplayBannerProductHotspotViewHolder(
    itemView: View,
    private val listener: Listener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener
) : AbstractViewHolder<ShopWidgetDisplayBannerProductHotspotUiModel>(itemView),
    ImageHotspotView.Listener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_home_display_banner_product_hotspot_view_holder_layout
        private const val DEFAULT_RATIO = "1:1"
        private const val RV_HORIZONTAL_PADDING = 16
        private const val INT_TWO = 2
        private const val ONE_ITEM_PADDING_MULTIPLIER = 2
        private const val TWO_ITEM_PADDING_MULTIPLIER = 3
        private const val MORE_THAN_TWO_ITEM_PADDING_MULTIPLIER = 4
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
    private var currentSelectedItemPositionWhenUserTouchItem = 0
    private var carouselLayoutManager: CarouselLayoutManager? = null
    private val itemSelectListener: CarouselLayoutManager.OnCenterItemSelectionListener =
        CarouselLayoutManager.OnCenterItemSelectionListener { adapterPosition ->
            bannerIndicator?.setCurrentIndicator(
                adapterPosition
            )
        }
    private val itemTouchListener: RecyclerView.OnItemTouchListener =
        object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentSelectedItemPositionWhenUserTouchItem = carouselLayoutManager?.centerItemPosition.orZero()
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

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
            unifyprinciplesR.color.Unify_NN950_96
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
        val isShowIntroAnimation = uiModel.isShowIntroAnimation
        if (uiModel.isShowIntroAnimation) {
            uiModel.isShowIntroAnimation = false
        }
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
                            productPrice = productHotspot.displayedPrice
                        )
                    }
                ),
                listenerBubbleView = this,
                ratio = ratio,
                isShowIntroAnimation = isShowIntroAnimation
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
        carouselLayoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, isCircularRvLayout(uiModel), false)
        carouselLayoutManager?.setPostLayoutListener(CarouselZoomPostLayoutListener())
        carouselLayoutManager?.maxVisibleItems = Int.ONE
        carouselLayoutManager?.removeOnItemSelectionListener(itemSelectListener)
        carouselLayoutManager?.addOnItemSelectionListener(itemSelectListener)
        recyclerViewProductHotspot?.apply {
            isNestedScrollingEnabled = false
            (this@apply.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = ratio
            this.layoutManager = carouselLayoutManager
            this.setHasFixedSize(true)
            this.addOnScrollListener(CenterScrollListener())
            this.adapter = adapterShopWidgetProductHotspot
            this.setRecycledViewPool(recyclerviewPoolListener.parentPool)
            this.removeOnItemTouchListener(itemTouchListener)
            this.addOnItemTouchListener(itemTouchListener)
            carouselLayoutManager?.let {
                setupFlingListener(this, it)
            }
            configRvHeight(uiModel)
        }
    }

    /**
     * Need to calculate rv height manually before render to prevent jumpy scroll by finding rv item
     * height using it's width and ratio
     * rvHeight = (rvItem width without horizontal padding) * ratioDenominator / ratioNumerator
     * multiplier differences:
     * - ONE_ITEM_PADDING_MULTIPLIER: used for only rv with 1 item since we're adding horizontal padding to rv item
     * - TWO_ITEM_PADDING_MULTIPLIER: used for only rv with 2 items since we're adding half of horizontal padding to rv item
     *   and another horizontal padding to rv
     * - MORE_THAN_TWO_ITEM_PADDING_MULTIPLIER: used for only rv with more that 2 items since we're adding horizontal padding to both rv item and rv
     */
    private fun configRvHeight(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel) {
        recyclerViewProductHotspot?.apply {
            val numerator = getIndexRatio(uiModel, Int.ZERO)
            val denominator = getIndexRatio(uiModel, Int.ONE)
            val rvItemWidth = when (uiModel.data.size.orZero()) {
                Int.ONE -> {
                    getScreenWidth() - (RV_HORIZONTAL_PADDING.toPx() * ONE_ITEM_PADDING_MULTIPLIER)
                }

                INT_TWO -> {
                    getScreenWidth() - (RV_HORIZONTAL_PADDING.toPx() * TWO_ITEM_PADDING_MULTIPLIER)
                }

                else -> {
                    getScreenWidth() - (RV_HORIZONTAL_PADDING.toPx() * MORE_THAN_TWO_ITEM_PADDING_MULTIPLIER)
                }
            }
            layoutParams?.height = rvItemWidth * denominator / numerator
        }
    }

    private fun getIndexRatio(data: ShopWidgetDisplayBannerProductHotspotUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index)?.toIntOrNull() ?: Int.ONE
    }

    private fun setupFlingListener(
        recyclerView: RecyclerView,
        carouselLayoutManager: CarouselLayoutManager
    ) {
        recyclerView.onFlingListener = null
        recyclerView.onFlingListener = CarouselHorizontalFlingSwipeEffect(
            recyclerView,
            carouselLayoutManager,
            uiModel?.data?.size.orZero()
        ) { currentSelectedItemPositionWhenUserTouchItem }
    }

    private fun isCircularRvLayout(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel): Boolean {
        return uiModel.data.size > 2
    }

    override fun onBubbleViewClicked(
        hotspotData: ImageHotspotData.HotspotData,
        view: View,
        index: Int
    ) {
        // value should always be 0 because this one is for when we only have 1 image banner
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
