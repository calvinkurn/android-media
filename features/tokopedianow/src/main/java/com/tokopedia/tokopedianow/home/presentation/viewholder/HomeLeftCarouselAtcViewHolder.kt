package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderCustomView
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcBinding
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselAtcProductCardAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselAtcProductCardTypeFactoryImpl
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeLeftCarouselAtcProductCardDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeLeftCarouselAtcCallback
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeLeftCarouselAtcViewHolder (
    itemView: View,
    private val homeLeftCarouselAtcCallback: HomeLeftCarouselAtcCallback? = null,
    private val tokoNowView: TokoNowView? = null
) : AbstractViewHolder<HomeLeftCarouselAtcUiModel>(itemView), CoroutineScope,
    TokoNowDynamicHeaderCustomView.HeaderCustomViewListener {

    companion object {
        private const val IMAGE_TRANSLATION_X = "image_translation_x"
        private const val IMAGE_ALPHA = "image_alpha"

        private const val FIRST_VISIBLE_ITEM_POSITION = 0
        private const val NO_SCROLLED_POSITION = 0
        private const val IMAGE_PARALLAX_TRANSLATION = 1
        private const val IMAGE_PARALLAX_ALPHA = 0.80f

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc
    }

    private var binding: ItemTokopedianowHomeLeftCarouselAtcBinding? by viewBinding()

    private val masterJob = SupervisorJob()

    private var rvProduct: RecyclerView? = null
    private var viewParallaxBackground: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var dynamicHeaderCustomView: TokoNowDynamicHeaderCustomView? = null
    private var uiModel: HomeLeftCarouselAtcUiModel? = null

    private val adapter by lazy {
        HomeLeftCarouselAtcProductCardAdapter(
            baseListAdapterTypeFactory = HomeLeftCarouselAtcProductCardTypeFactoryImpl(
                productCardListener = homeLeftCarouselAtcCallback,
                productCardSeeMoreListener = homeLeftCarouselAtcCallback
            ),
            differ = HomeLeftCarouselAtcProductCardDiffer()
        )
    }

    init {
        binding?.let {
            rvProduct = it.rvProduct
            dynamicHeaderCustomView = it.dynamicHeaderCustomView
            viewParallaxBackground = it.parallaxBackground
        }
        rvProduct?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding?.apply {
                    saveInstanceStateToLayoutManager(recyclerView)
                    calculateParallaxImage()
                }
            }
        })
    }

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: HomeLeftCarouselAtcUiModel) {
        uiModel = element
        binding?.apply {
            setupImage(
                element = element
            )
            setupRecyclerView(
                element = element
            )
        }
        dynamicHeaderCustomView?.setModel(
            model = element.header,
            listener = this
        )
        setupBackgroundColor(
            backgroundColorArray = element.backgroundColorArray
        )
        onLeftCarouselImpressed(
            element = element
        )
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

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.setupRecyclerView(element: HomeLeftCarouselAtcUiModel) {
        setLayoutManager()
        restoreInstanceStateToLayoutManager()
        rvProduct.adapter = adapter
        submitList(element)
    }

    private fun submitList(element: HomeLeftCarouselAtcUiModel) {
        adapter.submitList(element.productList)
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.calculateParallaxImage() {
        launch {
            layoutManager?.apply {
                if (findFirstVisibleItemPosition() == FIRST_VISIBLE_ITEM_POSITION) {
                    findViewByPosition(findFirstVisibleItemPosition())?.apply {
                        val distanceFromLeft = left
                        val translateX = distanceFromLeft * 0.067f
                        parallaxImageView.translationX = translateX
                        val itemSize = width.toFloat()
                        val alpha = (abs(left).toFloat() / itemSize * IMAGE_PARALLAX_ALPHA)
                        parallaxImageView.alpha = alpha
                    }
                } else  {
                    parallaxBackground.translationX = NO_SCROLLED_POSITION.toFloat()
                }
            }
        }
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.saveInstanceStateToLayoutManager(recyclerView: RecyclerView) {
        launch {
            val scrollState = recyclerView.layoutManager?.onSaveInstanceState()
            tokoNowView?.saveScrollState(layoutPosition, scrollState)

            val mapParallaxState = mapOf(
                IMAGE_TRANSLATION_X to parallaxImageView.translationX,
                IMAGE_ALPHA to parallaxImageView.alpha
            )
            tokoNowView?.saveParallaxState(mapParallaxState)
        }
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.restoreInstanceStateToLayoutManager() {
        launch {
            val scrollState = tokoNowView?.getScrollState(adapterPosition)
            rvProduct.layoutManager?.onRestoreInstanceState(scrollState)

            parallaxImageView.translationX = tokoNowView?.getParallaxState()?.get(IMAGE_TRANSLATION_X).orZero()
            parallaxImageView.alpha = tokoNowView?.getParallaxState()?.get(IMAGE_ALPHA) ?: 1f
        }
    }

    private fun ItemTokopedianowHomeLeftCarouselAtcBinding.setupImage(element: HomeLeftCarouselAtcUiModel) {
        parallaxImageView.apply {
            translationX = 0f
            loadImage(element.imageBanner)
            setOnClickListener {
                homeLeftCarouselAtcCallback?.onLeftCarouselLeftImageClicked(
                    appLink = element.imageBannerAppLink,
                    channelId = element.id,
                    headerName = element.header.title
                )
            }
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
