package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.decoration.ProductCardCarouselDecoration
import com.tokopedia.tokopedianow.common.util.CustomProductCardCarouselLinearLayoutManager
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.common.view.TokoNowView
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
    private val tokoNowView: TokoNowView? = null
) : AbstractViewHolder<HomeLeftCarouselAtcUiModel>(itemView), CoroutineScope {

    companion object {
        private const val IMAGE_TRANSLATION_X = "image_translation_x"
        private const val IMAGE_ALPHA = "image_alpha"

        private const val FIRST_VISIBLE_ITEM_POSITION = 0
        private const val NO_SCROLLED = 0
        private const val IMAGE_PARALLAX_ALPHA = 0.5f
        private const val EXPECTED_IMAGE_PARALLAX_RATIO = 0.2f
        private const val DEFAULT_IMAGE_TRANSLATION_VALUE = 0f
        private const val DEFAULT_IMAGE_ALPHA_VALUE = 1f

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc
    }

    private var binding: ItemTokopedianowHomeLeftCarouselAtcBinding? by viewBinding()

    private val adapter by lazy {
        HomeLeftCarouselAtcProductCardAdapter(
            typeFactory = HomeLeftCarouselAtcProductCardTypeFactoryImpl(
                productCardListener = homeLeftCarouselAtcCallback,
                productCardSeeMoreListener = homeLeftCarouselAtcCallback
            ),
            differ = HomeLeftCarouselAtcProductCardDiffer()
        )
    }

    private var layoutManager: LinearLayoutManager = CustomProductCardCarouselLinearLayoutManager(itemView.context)

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            binding?.apply {
                calculateParallaxImage(dx)
                saveInstanceStateToLayoutManager(recyclerView)
            }
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
            setupParallaxImage(
                element = element
            )
            setupRecyclerView(
                element = element
            )
            hitLeftCarouselImpressionTracker(
                element = element
            )
        }
    }

    override fun bind(element: HomeLeftCarouselAtcUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.setupRecyclerView(element = element)
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

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.setupParallaxImage(
        element: HomeLeftCarouselAtcUiModel
    ) {
        parallaxImageView.apply {
            loadImage(element.imageBanner)
            setOnClickListener {
                homeLeftCarouselAtcCallback?.onLeftCarouselLeftImageClicked(
                    appLink = element.imageBannerAppLink,
                    channelId = element.id,
                    headerName = element.header.title
                )
            }
        }
        parallaxBackground.setGradientBackground(
            colorArray = element.backgroundColorArray
        )
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.setupRecyclerView(
        element: HomeLeftCarouselAtcUiModel
    ) {
        adapter.submitList(ArrayList(element.productList))
        restoreInstanceStateToLayoutManager()
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

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.saveInstanceStateToLayoutManager(
        recyclerView: RecyclerView
    ) {
        launch {
            val scrollState = recyclerView.layoutManager?.onSaveInstanceState()
            tokoNowView?.saveScrollState(
                adapterPosition = layoutPosition,
                scrollState = scrollState
            )

            val mapParallaxState = mapOf(
                IMAGE_TRANSLATION_X to parallaxImageView.translationX,
                IMAGE_ALPHA to parallaxImageView.alpha
            )
            tokoNowView?.saveParallaxState(
                adapterPosition = layoutPosition,
                mapParallaxState = mapParallaxState
            )
        }
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.restoreInstanceStateToLayoutManager() {
        launch {
            tokoNowView?.apply {
                val scrollState = getScrollState(
                    adapterPosition = layoutPosition
                )
                rvProduct.layoutManager?.onRestoreInstanceState(scrollState)
                parallaxImageView.translationX = tokoNowView.getParallaxState(layoutPosition)?.get(IMAGE_TRANSLATION_X) ?: DEFAULT_IMAGE_TRANSLATION_VALUE
                parallaxImageView.alpha = tokoNowView.getParallaxState(layoutPosition)?.get(IMAGE_ALPHA) ?: DEFAULT_IMAGE_ALPHA_VALUE
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
