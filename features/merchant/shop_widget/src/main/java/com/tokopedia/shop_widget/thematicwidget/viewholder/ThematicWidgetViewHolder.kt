package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.thematicwidget.adapter.ProductCardAdapter
import com.tokopedia.shop_widget.thematicwidget.adapter.ProductCardDiffer
import com.tokopedia.shop_widget.common.customview.DynamicHeaderCustomView
import com.tokopedia.shop_widget.common.customview.DynamicHeaderCustomView.HeaderCustomViewListener
import com.tokopedia.shop_widget.thematicwidget.typefactory.ProductCardTypeFactoryImpl
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSpaceUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.common.util.ColorUtil.getBackGroundColor
import com.tokopedia.shop_widget.databinding.ItemThematicWidgetBinding
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

class ThematicWidgetViewHolder (
    itemView: View,
    private val listener: ThematicWidgetListener
) : AbstractViewHolder<ThematicWidgetUiModel>(itemView), CoroutineScope, HeaderCustomViewListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_thematic_widget

        private const val IMG_PARALLAX_ALPHA_VALUE = 1f
        private const val IMG_PARALLAX_TRANSLATE_X_VALUE = 0f
        private const val IMG_PARALLAX_TRANSLATE_X_MULTIPLIER = 0.2f
        private const val IMG_PARALLAX_ALPHA_MULTIPLIER = 0.80f
        private const val RV_DEFAULT_MARGIN_TOP = 12f
        private const val RV_DEFAULT_MARGIN_BOTTOM = 12f
        private const val BIG_CAMPAIGN_THEMATIC = "big_campaign_thematic"
    }

    private var binding: ItemThematicWidgetBinding? by viewBinding()

    private val masterJob = SupervisorJob()

    private var ivParallaxImage: AppCompatImageView? = null
    private var rvProduct: RecyclerView? = null
    private var viewParallaxBackground: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var dynamicHeaderCustomView: DynamicHeaderCustomView? = null
    private var uiModel: ThematicWidgetUiModel? = null
    private var isFirstAttached: Boolean = true
    private var trackerProductsModel = mutableListOf<ProductCardUiModel>()

    private val adapter by lazy {
        ProductCardAdapter(
            baseListAdapterTypeFactory = ProductCardTypeFactoryImpl(
                productCardListener = productCardListenerImpl(),
                productCardSeeAllListener = productCardSeeAllListenerImpl()
            ),
            differ = ProductCardDiffer()
        )
    }

    init {
        binding?.let {
            rvProduct = it.rvProduct
            dynamicHeaderCustomView = it.dynamicHeaderCustomView
            ivParallaxImage = it.parallaxImage
            viewParallaxBackground = it.parallaxBackground
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
            model = element.header,
            listener = this
        )
        setupRecyclerView(
            element = element
        )
        setupImage(
            imageBanner = element.imageBanner
        )
        checkFestivity(element)
    }

    private fun checkFestivity(uiModel: ThematicWidgetUiModel) {
        if (uiModel.isFestivity) {
            configFestivity(uiModel)
        } else {
            configNonFestivity(uiModel)
        }
    }

    private fun configNonFestivity(uiModel: ThematicWidgetUiModel) {
        dynamicHeaderCustomView?.configNonFestivity()
        configMarginNonFestivity()
        setupBackgroundColor(
            startBackGroundColor = uiModel.firstBackgroundColor,
            endBackGroundColor = uiModel.secondBackgroundColor
        )
    }

    private fun configFestivity(uiModel: ThematicWidgetUiModel) {
        dynamicHeaderCustomView?.configFestivity()
        when(uiModel.name){
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

    private fun configMarginFestivity(){
        val rvLayoutParams = rvProduct?.layoutParams as? ConstraintLayout.LayoutParams
        rvLayoutParams?.setMargins(
            rvLayoutParams.leftMargin,
            Int.ZERO,
            rvLayoutParams.rightMargin,
            Int.ZERO
        )
        rvProduct?.layoutParams = rvLayoutParams
    }

    private fun configMarginNonFestivity(){
        val rvLayoutParams = rvProduct?.layoutParams as? ConstraintLayout.LayoutParams
        rvLayoutParams?.setMargins(
            rvLayoutParams.leftMargin,
            RV_DEFAULT_MARGIN_TOP.dpToPx().toInt(),
            rvLayoutParams.rightMargin,
            RV_DEFAULT_MARGIN_BOTTOM.dpToPx().toInt()
        )
        rvProduct?.layoutParams = rvLayoutParams
    }

    override fun onSeeAllClick(appLink: String) {
        listener.onSeeAllThematicWidgetClickListener(appLink, uiModel?.campaignId.orEmpty(), uiModel?.name.orEmpty())
    }

    override fun onTimerFinish() {
        listener.onThematicWidgetTimerFinishListener(uiModel)
    }

    private fun setupRecyclerView(element: ThematicWidgetUiModel) {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        rvProduct?.layoutManager = layoutManager
        restoreInstanceStateToLayoutManager()
        setHeightRecyclerView()
        rvProduct?.adapter = adapter
        submitList(element)
    }

    private fun submitList(element: ThematicWidgetUiModel) {
        val products = element.productList
        val newList = mutableListOf<Visitable<*>>()
        newList.add(ProductCardSpaceUiModel())
        newList.addAll(products)
        if (element.header.ctaTextLink.isNotBlank()) {
            newList.add(ProductCardSeeAllUiModel(element.header.ctaTextLink))
        }
        adapter.submitList(newList)
        trackForTheFirstTimeViewHolderAttached(element)
    }

    private fun trackForTheFirstTimeViewHolderAttached(element: ThematicWidgetUiModel) {
        if (isFirstAttached) {
            listener.onThematicWidgetImpressListener(element, adapterPosition)
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
            uiModel?.productList?.firstOrNull()?.let {
                it.rvState = recyclerView.layoutManager?.onSaveInstanceState()
            }
        }
    }

    private fun restoreInstanceStateToLayoutManager() {
        launch {
            val rvState =  uiModel?.productList?.firstOrNull()?.rvState
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
            getBackGroundColor(itemView.context, startBackGroundColor, com.tokopedia.unifyprinciples.R.color.Unify_N75),
            getBackGroundColor(itemView.context, endBackGroundColor, com.tokopedia.unifyprinciples.R.color.Unify_N75)
        )
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
        viewParallaxBackground?.background = gradientDrawable
    }

    private fun productCardListenerImpl(): ProductCardViewHolder.ProductCardListener = object : ProductCardViewHolder.ProductCardListener {
        override fun onProductCardClickListener(product: ProductCardUiModel) {
            listener.onProductCardThematicWidgetClickListener(
                product = product,
                campaignId = uiModel?.campaignId.orEmpty(),
                campaignName = uiModel?.name.orEmpty(),
                position = adapterPosition
            )
        }

        override fun onProductCardImpressListener(product: ProductCardUiModel) {
            trackerProductsModel.add(product)
            listener.onProductCardThematicWidgetImpressListener(trackerProductsModel, adapterPosition, uiModel?.campaignId.orEmpty(), uiModel?.name.orEmpty())
        }
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
        fun onProductCardThematicWidgetImpressListener(products: List<ProductCardUiModel>, position: Int, campaignId: String, campaignName: String, campaignTitle: String = "")
        fun onProductCardThematicWidgetClickListener(product: ProductCardUiModel, campaignId: String, campaignName: String, position: Int, campaignTitle: String = "")
        fun onProductCardSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String)
        fun onSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String)
        fun onThematicWidgetTimerFinishListener(model: ThematicWidgetUiModel?)
    }
}
