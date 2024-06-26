package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.DynamicHeaderCustomView
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.DynamicHeaderCustomView.HeaderCustomViewListener
import com.tokopedia.shop_widget.common.util.ColorUtil.getBackGroundColor
import com.tokopedia.shop.databinding.ItemThematicWidgetBinding
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.adapter.ShopHomeThematicWidgetAdapter
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.adapter.ShopHomeThematicWidgetDiffUtil
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.typefactory.ShopHomeThematicWidgetTypeFactoryImpl
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSeeAllUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSpaceUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ThematicWidgetUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

// need to surpress this one, since there are no pii related data defined on this class
@SuppressLint("PII Data Exposure")
class ThematicWidgetViewHolder(
    itemView: View,
    private val listener: ThematicWidgetListener,
    private val isOverrideTheme: Boolean
) : AbstractViewHolder<ThematicWidgetUiModel>(itemView), CoroutineScope, HeaderCustomViewListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_thematic_widget

        private const val IMG_PARALLAX_ALPHA_VALUE = 1f
        private const val IMG_PARALLAX_TRANSLATE_X_VALUE = 0f
        private const val IMG_PARALLAX_TRANSLATE_X_MULTIPLIER = 0.2f
        private const val IMG_PARALLAX_ALPHA_MULTIPLIER = 0.80f
        private const val RV_DEFAULT_MARGIN_TOP = 16f
        private const val RV_DEFAULT_MARGIN_BOTTOM = 12f
        private const val CONTENT_CONTAINER_DEFAULT_MARGIN_BOTTOM = 8f
        private const val CONTENT_CONTAINER_FESTIVITY_MARGIN_BOTTOM = 10f
        private const val BIG_CAMPAIGN_THEMATIC = "big_campaign_thematic"
        private val SHOP_RE_IMAGINE_MARGIN = 16f.dpToPx()
    }

    private var binding: ItemThematicWidgetBinding? by viewBinding()

    private val masterJob = SupervisorJob()

    private var contentContainer: ConstraintLayout? = null
    private var ivParallaxImage: AppCompatImageView? = null
    private var rvProduct: RecyclerView? = null
    private var viewParallaxBackground: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var dynamicHeaderCustomView: DynamicHeaderCustomView? = null
    private var containerMixLeft: View? = null
    private var uiModel: ThematicWidgetUiModel? = null
    private var isFirstAttached: Boolean = true
    private var trackerProductsModel = mutableListOf<ShopHomeProductUiModel>()

    private var adapter: ShopHomeThematicWidgetAdapter? = null

    init {
        binding?.let {
            contentContainer = it.contentContainer
            rvProduct = it.rvProduct
            dynamicHeaderCustomView = it.dynamicHeaderCustomView
            ivParallaxImage = it.parallaxImage
            viewParallaxBackground = it.parallaxBackground
            containerMixLeft = it.containerMixleft
        }
        rvProduct?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                saveInstanceStateToLayoutManager(recyclerView)
                calculateParallaxImage(dx)
            }
        })
    }

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: ThematicWidgetUiModel) {
        uiModel = element
        dynamicHeaderCustomView?.setModel(
            model = element,
            header = element.header,
            listener = this
        )
        dynamicHeaderCustomView?.setShopPageCta(element.header)
        setupRecyclerView(element)
        setupImage(
            imageBanner = element.imageBanner
        )
        resetShopReimaginedContainerMargin()
        configColorTheme(element)
        checkTotalProduct(element)
        setShopReimaginedContainerMargin(element.name)
    }

    private fun resetShopReimaginedContainerMargin() {
        containerMixLeft?.let {
            it.background = null
            (it.layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart = 0
            (it.layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd = 0
        }
    }

    private fun configColorTheme(uiModel: ThematicWidgetUiModel) {
        if (uiModel.isFestivity) {
            configFestivity(uiModel)
        } else {
            if (isOverrideTheme) {
                configReimagined(uiModel)
            } else {
                configDefaultColor(uiModel)
            }
        }
    }

    private fun configReimagined(uiModel: ThematicWidgetUiModel) {
        dynamicHeaderCustomView?.configReimaginedColor(uiModel.header.colorSchema)
        setupBackgroundColor(
            startBackGroundColor = uiModel.firstBackgroundColor,
            endBackGroundColor = uiModel.secondBackgroundColor
        )
    }

    private fun configDefaultColor(uiModel: ThematicWidgetUiModel) {
        dynamicHeaderCustomView?.configDefaultColor()
        configMarginNonFestivity()
        setupBackgroundColor(
            startBackGroundColor = uiModel.firstBackgroundColor,
            endBackGroundColor = uiModel.secondBackgroundColor
        )
    }

    private fun configFestivity(uiModel: ThematicWidgetUiModel) {
        dynamicHeaderCustomView?.configShopPageFestivityColor()
        when (uiModel.name) {
            BIG_CAMPAIGN_THEMATIC -> {
                configMarginFestivity()
                viewParallaxBackground?.background = null
            }
            else -> {
                configMarginNonFestivity()
                setupBackgroundColor(
                    startBackGroundColor = uiModel.firstBackgroundColor,
                    endBackGroundColor = uiModel.secondBackgroundColor
                )
            }
        }
    }

    private fun setShopReimaginedContainerMargin(widgetName: String) {
        if(uiModel?.isFestivity == false || widgetName != BIG_CAMPAIGN_THEMATIC) {
            containerMixLeft?.let {
                it.clipToOutline = true
                it.background = MethodChecker.getDrawable(
                    itemView.context,
                    R.drawable.bg_shop_reimagined_rounded
                )
                (it.layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart =
                    SHOP_RE_IMAGINE_MARGIN.toInt()
                (it.layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd =
                    SHOP_RE_IMAGINE_MARGIN.toInt()
            }
        }
    }

    private fun configMarginFestivity() {
        val rvLayoutParams = rvProduct?.layoutParams as? ViewGroup.MarginLayoutParams
        rvLayoutParams?.setMargins(
            rvLayoutParams.leftMargin,
            Int.ZERO,
            rvLayoutParams.rightMargin,
            Int.ZERO
        )
        rvProduct?.layoutParams = rvLayoutParams
        val contentContainerLayoutParams = contentContainer?.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        contentContainerLayoutParams?.setMargins(
            contentContainerLayoutParams.leftMargin,
            contentContainerLayoutParams.topMargin,
            contentContainerLayoutParams.rightMargin,
            CONTENT_CONTAINER_FESTIVITY_MARGIN_BOTTOM.dpToPx().toInt()
        )
        contentContainer?.layoutParams = contentContainerLayoutParams
    }

    private fun configMarginNonFestivity() {
        val rvLayoutParams = rvProduct?.layoutParams as? ViewGroup.MarginLayoutParams
        rvLayoutParams?.setMargins(
            rvLayoutParams.leftMargin,
            RV_DEFAULT_MARGIN_TOP.dpToPx().toInt(),
            rvLayoutParams.rightMargin,
            RV_DEFAULT_MARGIN_BOTTOM.dpToPx().toInt()
        )
        rvProduct?.layoutParams = rvLayoutParams
        val contentContainerLayoutParams = contentContainer?.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        contentContainerLayoutParams?.setMargins(
            contentContainerLayoutParams.leftMargin,
            contentContainerLayoutParams.topMargin,
            contentContainerLayoutParams.rightMargin,
            CONTENT_CONTAINER_DEFAULT_MARGIN_BOTTOM.dpToPx().toInt()
        )
        contentContainer?.layoutParams = contentContainerLayoutParams
    }

    override fun onSeeAllClick(appLink: String) {
        listener.onSeeAllThematicWidgetClickListener(appLink, uiModel?.campaignId.orEmpty(), uiModel?.name.orEmpty())
    }

    override fun onTimerFinish() {
        listener.onThematicWidgetTimerFinishListener(uiModel)
    }

    private fun setupRecyclerView(element: ThematicWidgetUiModel) {
        adapter = ShopHomeThematicWidgetAdapter(
            baseListAdapterTypeFactory = ShopHomeThematicWidgetTypeFactoryImpl(
                productCardGridListener = productCardGridListenerImpl(),
                productCardListListener = productCardListListenerImpl(),
                productCardSeeAllListener = productCardSeeAllListenerImpl(),
                totalProductSize = uiModel?.productList?.size.orZero(),
                isOverrideWidgetTheme = isOverrideTheme,
                thematicWidgetUiModel = element,
                isFestivity = element.isFestivity
            ),
            differ = ShopHomeThematicWidgetDiffUtil()
        )
        restoreInstanceStateToLayoutManager()
        setHeightRecyclerView()
        rvProduct?.isNestedScrollingEnabled = false
        rvProduct?.adapter = adapter
    }

    private fun checkTotalProduct(element: ThematicWidgetUiModel) {
        if (isProductSizeOne(element.productList)) {
            configWidgetForOnlyOneProduct(element)
        } else {
            configWidgetDefault(element)
        }
    }

    private fun configWidgetDefault(element: ThematicWidgetUiModel) {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        rvProduct?.layoutManager = layoutManager
        ivParallaxImage?.show()
        submitList(element)
    }

    private fun configWidgetForOnlyOneProduct(element: ThematicWidgetUiModel) {
        layoutManager = GridLayoutManager(itemView.context, Int.ONE)
        rvProduct?.layoutManager = layoutManager
        ivParallaxImage?.hide()
        submitListForOneProduct(element)
    }

    private fun submitListForOneProduct(element: ThematicWidgetUiModel) {
        val products = element.productList
        val newList = mutableListOf<Visitable<*>>()
        newList.addAll(products)
        adapter?.submitList(newList)
        trackForTheFirstTimeViewHolderAttached(element)
    }

    private fun isProductSizeOne(
        productList: List<ShopHomeProductUiModel>
    ): Boolean {
        return productList.size == Int.ONE
    }

    private fun submitList(element: ThematicWidgetUiModel) {
        val products = element.productList
        val newList = mutableListOf<Visitable<*>>()
        newList.add(ProductCardSpaceUiModel())
        newList.addAll(products)
        if (element.header.ctaLink.isNotBlank()) {
            newList.add(ProductCardSeeAllUiModel(element.header.ctaLink))
        }
        adapter?.submitList(newList)
        trackForTheFirstTimeViewHolderAttached(element)
    }

    private fun trackForTheFirstTimeViewHolderAttached(element: ThematicWidgetUiModel) {
        if (isFirstAttached) {
            listener.onThematicWidgetImpressListener(element, bindingAdapterPosition)
            isFirstAttached = false
        }
    }

    private fun calculateParallaxImage(dx: Int) {
        launch {
            layoutManager?.apply {
                if (findFirstVisibleItemPosition() == Int.ZERO && dx != Int.ZERO) {
                    findViewByPosition(findFirstVisibleItemPosition())?.apply {
                        val distanceFromLeft = left
                        val translateX = distanceFromLeft * IMG_PARALLAX_TRANSLATE_X_MULTIPLIER
                        if (translateX <= Int.ZERO) {
                            ivParallaxImage?.translationX = translateX
                            if (distanceFromLeft <= Int.ZERO) {
                                val itemSize = width.toFloat()
                                val alpha = (abs(distanceFromLeft).toFloat() / itemSize * IMG_PARALLAX_ALPHA_MULTIPLIER)
                                ivParallaxImage?.alpha = Int.ONE - alpha
                            }
                        } else {
                            ivParallaxImage?.translationX = IMG_PARALLAX_TRANSLATE_X_VALUE
                            ivParallaxImage?.alpha = IMG_PARALLAX_ALPHA_VALUE
                        }
                    }
                }
            }
        }
    }

    private fun saveInstanceStateToLayoutManager(recyclerView: RecyclerView) {
        launch {
            uiModel?.rvState = recyclerView.layoutManager?.onSaveInstanceState()
        }
    }

    private fun restoreInstanceStateToLayoutManager() {
        launch {
            val rvState = uiModel?.rvState
            if (null != rvState) {
                rvProduct?.layoutManager?.onRestoreInstanceState(rvState)
            }
        }
    }

    private fun setHeightRecyclerView() {
        val carouselLayoutParams = rvProduct?.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        rvProduct?.layoutParams = carouselLayoutParams
    }

    private fun setupImage(imageBanner: String) {
        ivParallaxImage?.show()
        ivParallaxImage?.layout(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
        ivParallaxImage?.translationX = IMG_PARALLAX_TRANSLATE_X_VALUE
        ivParallaxImage?.alpha = IMG_PARALLAX_ALPHA_VALUE
        ivParallaxImage?.loadImage(imageBanner)
    }

    private fun setupBackgroundColor(startBackGroundColor: String?, endBackGroundColor: String?) {
        val colors = intArrayOf(
            getBackGroundColor(itemView.context, startBackGroundColor, R.color.dms_clr_thematic_widget_bg_default_first_color),
            getBackGroundColor(itemView.context, endBackGroundColor, R.color.dms_clr_thematic_widget_bg_default_second_color)
        )
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
        viewParallaxBackground?.background = gradientDrawable
    }

    private fun productCardGridListenerImpl(): ProductCardGridViewHolder.ProductCardListener = object : ProductCardGridViewHolder.ProductCardListener {
        override fun onProductCardClickListener(product: ShopHomeProductUiModel) {
            listener.onProductCardThematicWidgetClickListener(
                product = product,
                thematicWidgetUiModel = uiModel,
                position = bindingAdapterPosition
            )
        }

        override fun onProductCardImpressListener(product: ShopHomeProductUiModel) {
            trackerProductsModel.add(product)
            listener.onProductCardThematicWidgetImpressListener(trackerProductsModel, bindingAdapterPosition, uiModel)
        }

        override fun getPatternColorType() = listener.getPatternColorType()
        override fun getBackgroundColor() = listener.getBackgroundColor()
    }

    private fun productCardListListenerImpl(): ProductCardListViewHolder.ProductCardListener = object : ProductCardListViewHolder.ProductCardListener {
        override fun onProductCardClickListener(product: ShopHomeProductUiModel) {
            listener.onProductCardThematicWidgetClickListener(
                product = product,
                thematicWidgetUiModel = uiModel,
                position = bindingAdapterPosition
            )
        }

        override fun onProductCardImpressListener(product: ShopHomeProductUiModel) {
            trackerProductsModel.add(product)
            listener.onProductCardThematicWidgetImpressListener(trackerProductsModel, bindingAdapterPosition, uiModel)
        }

        override fun getPatternColorType() = listener.getPatternColorType()
        override fun getBackgroundColor() = listener.getBackgroundColor()
    }

    private fun productCardSeeAllListenerImpl(): ProductCardSeeAllViewHolder.ProductCardSeeAllListener = object : ProductCardSeeAllViewHolder.ProductCardSeeAllListener {
        override fun onProductCardSeeAllClickListener(appLink: String) {
            listener.onProductCardSeeAllThematicWidgetClickListener(
                appLink = appLink,
                campaignId = uiModel?.campaignId.orEmpty(),
                campaignName = uiModel?.name.orEmpty()
            )
        }
    }

    interface ThematicWidgetListener {
        fun onThematicWidgetImpressListener(model: ThematicWidgetUiModel, position: Int)
        fun onProductCardThematicWidgetImpressListener(products: List<ShopHomeProductUiModel>, position: Int, thematicWidgetUiModel: ThematicWidgetUiModel?)
        fun onProductCardThematicWidgetClickListener(product: ShopHomeProductUiModel, thematicWidgetUiModel: ThematicWidgetUiModel?, position: Int)
        fun onProductCardSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String)
        fun onSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String)
        fun onThematicWidgetTimerFinishListener(model: ThematicWidgetUiModel?)
        fun getPatternColorType(): String
        fun getBackgroundColor(): String
    }
}
