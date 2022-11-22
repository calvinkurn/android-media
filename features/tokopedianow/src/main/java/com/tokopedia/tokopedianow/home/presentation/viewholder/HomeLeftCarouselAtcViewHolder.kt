package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.graphics.Rect
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderCustomView
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcBinding
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselAtcProductCardAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselAtcProductCardTypeFactoryImpl
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeLeftCarouselAtcProductCardDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeLeftCarouselAtcCallback
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.math.abs

class HomeLeftCarouselAtcViewHolder (
    itemView: View,
    private val homeLeftCarouselAtcCallback: HomeLeftCarouselAtcCallback? = null,
    private val tokoNowView: TokoNowView? = null,
    private val rtrListener: RealTimeRecommendationListener? = null,
    private val rtrAnalytics: RealTimeRecommendationAnalytics? = null
) : AbstractViewHolder<HomeLeftCarouselAtcUiModel>(itemView), CoroutineScope,
    TokoNowDynamicHeaderCustomView.HeaderCustomViewListener {

    companion object {
        const val IMAGE_TRANSLATION_X = "image_translation_x"
        const val IMAGE_ALPHA = "image_alpha"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc
    }

    private var binding: ItemTokopedianowHomeLeftCarouselAtcBinding? by viewBinding()

    private val masterJob = SupervisorJob()

    private var ivParallaxImage: AppCompatImageView? = null
    private var rvProduct: RecyclerView? = null
    private var viewParallaxBackground: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var dynamicHeaderCustomView: TokoNowDynamicHeaderCustomView? = null
    private var uiModel: HomeLeftCarouselAtcUiModel? = null

    private val adapter by lazy {
        HomeLeftCarouselAtcProductCardAdapter(
            baseListAdapterTypeFactory = HomeLeftCarouselAtcProductCardTypeFactoryImpl(
                productCardListener = homeLeftCarouselAtcCallback,
                productCardSeeMoreListener = homeLeftCarouselAtcCallback,
                productCardSpaceListener = homeLeftCarouselAtcCallback
            ),
            differ = HomeLeftCarouselAtcProductCardDiffer()
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
        rvProduct?.adapter = adapter
        setHeightRecyclerView()
        setLayoutManager()
    }

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: HomeLeftCarouselAtcUiModel) {
        uiModel = element
        dynamicHeaderCustomView?.setModel(
            model = element.header,
            listener = this
        )
        setupRecyclerView(
            element = element
        )
        setupImage(
            element = element
        )
        setupBackgroundColor(
            backgroundColorArray = element.backgroundColorArray
        )
        setupRealTimeRecommendation(
            element = element
        )
        onLeftCarouselImpressed(
            element = element
        )
    }

    override fun bind(element: HomeLeftCarouselAtcUiModel?, payloads: MutableList<Any>) {
        if(payloads.firstOrNull() == true && element != null) {
            setupRecyclerView(element = element)
            setupRealTimeRecommendation(element = element)
        }
    }

    private fun setupRealTimeRecommendation(element: HomeLeftCarouselAtcUiModel) {
        binding?.realTimeRecommendationCarousel?.apply {
            listener = rtrListener
            analytics = rtrAnalytics
            bind(element.realTimeRecom)
        }
    }

    override fun onSeeAllClicked(appLink: String) {
        homeLeftCarouselAtcCallback?.onSeeMoreClicked(
            appLink = appLink,
            channelId = uiModel?.id.orEmpty(),
            headerName = uiModel?.header?.title.orEmpty()
        )
    }

    override fun onChannelExpired() {
        homeLeftCarouselAtcCallback?.onRemoveLeftCarouselAtc(uiModel?.id.orEmpty())
    }

    private fun onLeftCarouselImpressed(element: HomeLeftCarouselAtcUiModel) {
        if (!element.isInvoke) {
            homeLeftCarouselAtcCallback?.onLeftCarouselImpressed(
                channelId = element.id,
                headerName = element.header.title
            )
            element.invoke()
        }
    }

    private fun setupRecyclerView(element: HomeLeftCarouselAtcUiModel) {
        restoreInstanceStateToLayoutManager()
        submitList(element)
        setHeightProductCard(element)
    }

    private fun submitList(element: HomeLeftCarouselAtcUiModel) {
        adapter.submitList(element.productList)
    }

    private fun setHeightProductCard(element: HomeLeftCarouselAtcUiModel) {
        launch {
            try {
                val productCardModels = element.productList.filterIsInstance<HomeLeftCarouselAtcProductCardUiModel>()
                rvProduct?.setHeightBasedOnProductCardMaxHeight(productCardModelList = productCardModels.map { it.productCardModel })
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    @Suppress("MagicNumber")
    private fun calculateParallaxImage(dx: Int) {
        launch {
            layoutManager?.apply {
                if (findFirstVisibleItemPosition() == 0 && dx != 0) {
                    findViewByPosition(findFirstVisibleItemPosition())?.apply {
                        val distanceFromLeft = left
                        val translateX = distanceFromLeft * 0.2f
                        if (translateX <= 0) {
                            ivParallaxImage?.translationX = translateX
                            if (distanceFromLeft <= 0) {
                                val itemSize = width.toFloat()
                                val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                                ivParallaxImage?.alpha = 1 - alpha
                            }
                        } else {
                            ivParallaxImage?.translationX = 0f
                            ivParallaxImage?.alpha = 1f
                        }
                    }
                }
            }
        }
    }

    private fun saveInstanceStateToLayoutManager(recyclerView: RecyclerView) {
        launch {
            val scrollState = recyclerView.layoutManager?.onSaveInstanceState()
            tokoNowView?.saveScrollState(adapterPosition, scrollState)

            val mapParallaxState = mapOf(
                IMAGE_TRANSLATION_X to ivParallaxImage?.translationX.orZero(),
                IMAGE_ALPHA to (ivParallaxImage?.alpha ?: 1f)
            )
            tokoNowView?.saveParallaxState(mapParallaxState)
        }
    }

    private fun restoreInstanceStateToLayoutManager() {
        launch {
            val scrollState = tokoNowView?.getScrollState(adapterPosition)
            rvProduct?.layoutManager?.onRestoreInstanceState(scrollState)

            ivParallaxImage?.translationX = tokoNowView?.getParallaxState()?.get(IMAGE_TRANSLATION_X).orZero()
            ivParallaxImage?.alpha = tokoNowView?.getParallaxState()?.get(IMAGE_ALPHA) ?: 1f
        }
    }

    private fun setHeightRecyclerView() {
        val carouselLayoutParams = rvProduct?.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        rvProduct?.layoutParams = carouselLayoutParams
    }

    private fun setupImage(element: HomeLeftCarouselAtcUiModel) {
        ivParallaxImage?.show()
        ivParallaxImage?.layout(0,0,0,0)
        ivParallaxImage?.translationX = 0f
        ivParallaxImage?.alpha = 1f
        ivParallaxImage?.loadImage(element.imageBanner)
        ivParallaxImage?.setOnClickListener {
            homeLeftCarouselAtcCallback?.onLeftCarouselLeftImageClicked(
                appLink = element.imageBannerAppLink,
                channelId = element.id,
                headerName = element.header.title
            )
        }
    }

    private fun setupBackgroundColor(backgroundColorArray: ArrayList<String>) {
        viewParallaxBackground?.setGradientBackground(backgroundColorArray)
    }

    private fun setLayoutManager() {
        layoutManager = object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
            override fun requestChildRectangleOnScreen(
                parent: RecyclerView,
                child: View,
                rect: Rect,
                immediate: Boolean,
                focusedChildVisible: Boolean
            ): Boolean {
                return if ((child as? ViewGroup)?.focusedChild is CardView) {
                    false
                } else super.requestChildRectangleOnScreen(
                    parent,
                    child,
                    rect,
                    immediate,
                    focusedChildVisible
                )
            }
        }
        rvProduct?.layoutManager = layoutManager
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
        productCardModelList: List<ProductCardModel>) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        val productCardWidth = binding?.root?.context?.resources?.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.product_card_flashsale_width)
        return productCardModelList.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth.orZero())
    }

    interface HomeLeftCarouselAtcListener {
        fun onSeeMoreClicked(appLink: String, channelId: String, headerName: String)
        fun onLeftCarouselImpressed(channelId: String, headerName: String)
        fun onLeftCarouselLeftImageClicked(appLink: String, channelId: String, headerName: String)
        fun onRemoveLeftCarouselAtc(channelId: String)
    }
}
