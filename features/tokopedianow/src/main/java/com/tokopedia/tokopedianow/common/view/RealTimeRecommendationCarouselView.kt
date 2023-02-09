package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowRealTimeRecommendationBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState
import com.tokopedia.unifycomponents.BaseCustomView

class RealTimeRecommendationCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr),
    ProductCardCompactCarouselView.TokoNowProductCardCarouseBasicListener {

    companion object {
        private const val FIRST_ITEM_POSITION = 0
        private const val SCROLL_DELAY = 500L
    }

    var listener: RealTimeRecommendationListener? = null
    var analytics: RealTimeRecommendationAnalytics? = null

    private var itemView: LayoutTokopedianowRealTimeRecommendationBinding? = null
    private var rtrData: HomeRealTimeRecomUiModel? = null
    private var previousState: RealTimeRecomWidgetState? = null

    init {
        itemView = LayoutTokopedianowRealTimeRecommendationBinding
            .inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(data: HomeRealTimeRecomUiModel) {
        setData(data)
        addImpressionTracker(data)
        renderWidgetView(data)
    }

    private fun renderWidgetView(data: HomeRealTimeRecomUiModel) {
        when (data.widgetState) {
            RealTimeRecomWidgetState.LOADING -> renderLoadingState()
            RealTimeRecomWidgetState.READY -> renderRealTimeRecom(data)
            RealTimeRecomWidgetState.REFRESH -> {
                renderProductCarousel(data)
                renderRefreshRecommendation(data)
                trackRefreshImpression(data)
            }
            else -> hideContent()
        }
    }

    private fun scrollToFirstPosition() {
        if (previousState == RealTimeRecomWidgetState.REFRESH) {
            itemView?.productCarousel?.postDelayed({
                itemView?.productCarousel?.scrollToPosition(FIRST_ITEM_POSITION
            )}, SCROLL_DELAY)
        }
    }

    private fun renderRealTimeRecom(data: HomeRealTimeRecomUiModel) {
        val productList = data.productList

        if (productList.isNotEmpty()) {
            renderTitle(data)
            renderProductImage(data)
            renderProductCarousel(data)
            renderCloseBtn(data)
            hideRefreshRecommendation()
            hideProgressBar()
            setBackgroundColor()
            scrollToFirstPosition()
        } else {
            hideContent()
        }
    }

    private fun renderTitle(data: HomeRealTimeRecomUiModel) {
        itemView?.apply {
            textTitle.text = getTitle(data)
            textTitle.show()
        }
    }

    private fun getTitle(data: HomeRealTimeRecomUiModel?): String? {
        return context?.getString(R.string.tokopedianow_real_time_recom_title, data?.category)
    }

    private fun renderProductImage(data: HomeRealTimeRecomUiModel) {
        itemView?.apply {
            imageProduct.loadImage(data.productImageUrl)
            imageProduct.show()
        }
    }

    private fun renderRefreshRecommendation(data: HomeRealTimeRecomUiModel) {
        itemView?.apply {
            textRefreshRecommendation.text = MethodChecker.fromHtml(
                context.getString(R.string.tokopedianow_refresh_recommendation)
            )
            textRefreshRecommendation.setOnClickListener {
                onClickRefresh(data)
            }
            textRefreshRecommendation.show()
        }
    }

    private fun hideRefreshRecommendation() {
        itemView?.textRefreshRecommendation?.hide()
    }

    private fun onClickRefresh(data: HomeRealTimeRecomUiModel) {
        listener?.refreshRealTimeRecommendation(data)
        analytics?.trackClickRefresh(data.parentProductId)
        itemView?.textRefreshRecommendation?.hide()
    }

    private fun renderProductCarousel(data: HomeRealTimeRecomUiModel) {
        itemView?.productCarousel?.apply {
            setListener(this@RealTimeRecommendationCarouselView)
            bind(items = data.productList, state = data.carouselState)
            show()
        }
    }

    private fun renderCloseBtn(data: HomeRealTimeRecomUiModel) {
        itemView?.apply {
            imageClose.setOnClickListener {
                listener?.removeRealTimeRecommendation(data)
                analytics?.trackClickClose(data.parentProductId)
                hideContent()
            }
            imageClose.show()
        }
    }

    private fun renderLoadingState() {
        hideContent()
        resetBackgroundColor()
        showProgressBar()
    }

    private fun setBackgroundColor() {
        itemView?.apply {
            container.setBackgroundColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN50))
            container.show()
        }
    }

    private fun resetBackgroundColor() {
        itemView?.apply {
            container.setBackgroundColor(Color.TRANSPARENT)
            container.show()
        }
    }

    private fun hideContent() {
        itemView?.apply {
            imageProduct.hide()
            textTitle.hide()
            textRefreshRecommendation.hide()
            imageClose.hide()
            productCarousel.hide()
            progressBar.hide()
            container.hide()
        }
    }

    private fun showProgressBar() {
        itemView?.progressBar?.show()
    }

    private fun hideProgressBar() {
        itemView?.progressBar?.hide()
    }

    private fun addImpressionTracker(data: HomeRealTimeRecomUiModel) {
        addOnImpressionListener(data.impressHolder) {
            analytics?.trackWidgetImpression(data.parentProductId, data.warehouseId)
        }
    }

    private fun trackRefreshImpression(data: HomeRealTimeRecomUiModel) {
        analytics?.trackRefreshImpression(data.parentProductId)
    }

    private fun trackProductImpression(position: Int, product: ProductCardCompactCarouselItemUiModel) {
        val headerName = getTitle(rtrData).orEmpty()
        val productId = rtrData?.parentProductId.orEmpty()
        analytics?.trackProductImpression(headerName, productId, product, position)
    }

    private fun trackProductClick(position: Int, product: ProductCardCompactCarouselItemUiModel) {
        val headerName = getTitle(rtrData).orEmpty()
        val productId = rtrData?.parentProductId.orEmpty()
        analytics?.trackProductClick(headerName, productId, product, position)
    }


    private fun trackProductAddToCart(product: ProductCardCompactCarouselItemUiModel, quantity: Int) {
        val productId = rtrData?.parentProductId.orEmpty()
        analytics?.trackAddToCart(productId, product, quantity)
    }

    private fun setData(data: HomeRealTimeRecomUiModel) {
        previousState = rtrData?.widgetState
        rtrData = data
    }

    override fun onProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onRecomProductCardClicked(position, product)
        trackProductClick(position, product)
    }

    override fun onProductCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        trackProductImpression(position, product)
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        val channelId = rtrData?.channelId.orEmpty()
        listener?.onAddToCartProductNonVariant(
            channelId,
            product,
            quantity
        )
        trackProductAddToCart(product, quantity)
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onAddToCartProductVariantClick(position, product)
    }
}

