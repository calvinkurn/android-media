package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.decoration.ProductCardCarouselDecoration
import com.tokopedia.tokopedianow.common.util.CustomProductCardCarouselLinearLayoutManager
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcBinding
import com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel.HomeLeftCarouselAtcProductCardAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel.HomeLeftCarouselAtcProductCardDiffer
import com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel.HomeLeftCarouselAtcProductCardTypeFactoryImpl
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeLeftCarouselAtcCallback
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeLeftCarouselAtcViewHolder(
    itemView: View,
    private val homeLeftCarouselAtcCallback: HomeLeftCarouselAtcCallback? = null,
    private val rtrListener: RealTimeRecommendationListener? = null,
    private val rtrAnalytics: RealTimeRecommendationAnalytics? = null
) : AbstractViewHolder<HomeLeftCarouselAtcUiModel>(itemView), CoroutineScope {

    companion object {
        private const val FIRST_VISIBLE_ITEM_POSITION = 0
        private const val NO_SCROLLED = 0
        private const val IMAGE_PARALLAX_ALPHA = 0.5f
        private const val EXPECTED_IMAGE_PARALLAX_RATIO = 0.2f
        private const val X_RANGE_COORDINATE = 0f

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc
    }

    private val binding: ItemTokopedianowHomeLeftCarouselAtcBinding? by viewBinding()

    private val adapter by lazy {
        HomeLeftCarouselAtcProductCardAdapter(
            typeFactory = HomeLeftCarouselAtcProductCardTypeFactoryImpl(
                productCardListener = homeLeftCarouselAtcCallback,
                productCardSeeMoreListener = homeLeftCarouselAtcCallback
            ),
            differ = HomeLeftCarouselAtcProductCardDiffer()
        )
    }

    private val layoutManager: LinearLayoutManager = CustomProductCardCarouselLinearLayoutManager(itemView.context)

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            binding?.calculateParallaxImage(dx)
        }
    }

    private val masterJob = SupervisorJob()

    init {
        binding?.apply {
            rvProduct.addOnScrollListener(scrollListener)
            rvProduct.itemAnimator = null
            rvProduct.addItemDecoration(ProductCardCarouselDecoration(rvProduct.context))
            rvProduct.layoutManager = layoutManager
            rvProduct.adapter = adapter
        }
    }

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: HomeLeftCarouselAtcUiModel) {
        binding?.apply {
            setupHeader(
                element = element
            )
            hitLeftCarouselImpressionTracker(
                element = element
            )
            setupRealTimeRecommendation(
                element = element
            )
            setupParallaxImage(
                element = element
            )
            submitList(
                element = element
            )
        }
    }

    override fun bind(element: HomeLeftCarouselAtcUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.apply {
                submitList(element = element)
                setupRealTimeRecommendation(element = element)
            }
        }
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.setupHeader(
        element: HomeLeftCarouselAtcUiModel
    ) {
        dynamicHeaderCustomView.setModel(
            model = element.header
        )
        dynamicHeaderCustomView.setListener(
            headerListener = getDynamicHeaderListener(
                element = element
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.setupParallaxImage(
        element: HomeLeftCarouselAtcUiModel
    ) {
        parallaxImageView.apply {
            com.bumptech.glide.Glide.with(itemView.context)
                .load(element.imageBanner)
                .fitCenter()
                .into(this)
        }

        parallaxBackground.setGradientBackground(
            colorArray = element.backgroundColorArray
        )

        var xCoordinateActionDown = 0f
        rvProduct.setOnTouchListener { _, motionEvent ->
            /**
             * check empty space of recyclerview
             */
            if (rvProduct.findChildViewUnder(motionEvent.x, motionEvent.y) != null) return@setOnTouchListener false

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    xCoordinateActionDown = motionEvent.x
                }
                MotionEvent.ACTION_UP -> {
                     /**
                     * xRange is used to decide there is any move or not, if not call the listener
                     */
                    val xRange = abs(xCoordinateActionDown - motionEvent.x)
                    if (xRange == X_RANGE_COORDINATE) {
                        homeLeftCarouselAtcCallback?.onLeftCarouselLeftImageClicked(
                            appLink = element.imageBannerAppLink,
                            channelId = element.id,
                            headerName = element.header.title
                        )
                    }
                }
            }
            false
        }
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.setupRealTimeRecommendation(
        element: HomeLeftCarouselAtcUiModel
    ) {
        realTimeRecommendationCarousel.apply {
            listener = rtrListener
            analytics = rtrAnalytics
            bind(element.realTimeRecom)
        }
    }

    private fun submitList(element: HomeLeftCarouselAtcUiModel) {
        adapter.submitList(ArrayList(element.productList))
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.hitLeftCarouselImpressionTracker(
        element: HomeLeftCarouselAtcUiModel
    ) {
        root.addOnImpressionListener(element) {
            homeLeftCarouselAtcCallback?.onLeftCarouselImpressed(
                channelId = element.id,
                headerName = element.header.title
            )
        }
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.calculateParallaxImage(dx: Int) {
        launch {
            if (layoutManager.findFirstVisibleItemPosition() == FIRST_VISIBLE_ITEM_POSITION && dx != NO_SCROLLED) {
                layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())?.apply {
                    val distanceLeftFirstItem = left
                    val translateX = distanceLeftFirstItem * EXPECTED_IMAGE_PARALLAX_RATIO
                    parallaxImageView.translationX = translateX - (rvProduct.paddingStart.toFloat() * EXPECTED_IMAGE_PARALLAX_RATIO)

                    val itemSize = width.toFloat()
                    val alpha = (abs(distanceLeftFirstItem).toFloat() / itemSize * IMAGE_PARALLAX_ALPHA)
                    parallaxImageView.alpha = alpha + (abs(rvProduct.paddingStart).toFloat() / itemSize * IMAGE_PARALLAX_ALPHA)
                }
            }
        }
    }

    private fun getDynamicHeaderListener(
        element: HomeLeftCarouselAtcUiModel
    ) =
        object : TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {
            override fun onSeeAllClicked(headerName: String, appLink: String) {
                homeLeftCarouselAtcCallback?.onSeeMoreClicked(
                    appLink = appLink,
                    channelId = element.id,
                    headerName = element.header.title
                )
            }
            override fun onChannelExpired() {
                homeLeftCarouselAtcCallback?.onRemoveLeftCarouselAtc(element.id)
            }
        }

    interface HomeLeftCarouselAtcListener {
        fun onSeeMoreClicked(
            appLink: String,
            channelId: String,
            headerName: String
        )
        fun onLeftCarouselImpressed(
            channelId: String,
            headerName: String
        )
        fun onLeftCarouselLeftImageClicked(
            appLink: String,
            channelId: String,
            headerName: String
        )
        fun onRemoveLeftCarouselAtc(
            channelId: String
        )
    }
}
