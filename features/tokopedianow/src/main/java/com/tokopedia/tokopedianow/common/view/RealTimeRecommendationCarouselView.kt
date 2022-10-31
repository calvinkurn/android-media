package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography

class RealTimeRecommendationCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), RecomCarouselWidgetBasicListener,
    RecommendationCarouselTokonowListener {

    private var itemView: View? = null
    private var imageProduct: ImageUnify? = null
    private var textTitle: Typography? = null
    private var textRefreshRecommendation: Typography? = null
    private var imageClose: ImageUnify? = null
    private var recommendationCarousel: RecommendationCarouselWidgetView? = null
    private var progressBar: LoaderUnify? = null
    private var container: ConstraintLayout? = null

    var listener: RealTimeRecommendationListener? = null

    init {
        itemView = LayoutInflater.from(context)
            .inflate(R.layout.layout_tokopedianow_real_time_recommendation, this)

        imageProduct = itemView?.findViewById(R.id.image_product)
        textTitle = itemView?.findViewById(R.id.text_title)
        textRefreshRecommendation = itemView?.findViewById(R.id.text_refresh_recommendation)
        imageClose = itemView?.findViewById(R.id.image_close)
        recommendationCarousel = itemView?.findViewById(R.id.recommendation_carousel)
        progressBar = itemView?.findViewById(R.id.progress_bar)
        container = itemView?.findViewById(R.id.container)
    }

    fun bind(data: HomeRealTimeRecomUiModel) {
        when(data.widgetState) {
            RealTimeRecomWidgetState.LOADING -> renderLoadingState()
            RealTimeRecomWidgetState.READY -> renderRealTimeRecom(data)
            RealTimeRecomWidgetState.REFRESH -> renderRefreshRecommendation(data)
            else -> hideContent()
        }
    }

    private fun renderRealTimeRecom(data: HomeRealTimeRecomUiModel) {
        val widget = data.widget

        if (widget?.recommendationItemList?.isNotEmpty() == true) {
            renderTitle(data)
            renderProductImage(data)
            renderRecommendationCarousel(data)
            renderCloseBtn(data)
            hideRefreshRecommendation()
            hideProgressBar()
            setBackgroundColor()
        } else {
            hideContent()
        }
    }

    private fun renderTitle(data: HomeRealTimeRecomUiModel) {
        textTitle?.text = itemView?.context?.getString(
            R.string.tokopedianow_real_time_recom_title, data.category
        )
        textTitle?.show()
    }

    private fun renderProductImage(data: HomeRealTimeRecomUiModel) {
        imageProduct?.loadImage(data.productImageUrl)
        imageProduct?.show()
    }

    private fun renderRefreshRecommendation(data: HomeRealTimeRecomUiModel) {
        textRefreshRecommendation?.text = MethodChecker.fromHtml(
            context.getString(R.string.tokopedianow_refresh_recommendation)
        )
        textRefreshRecommendation?.setOnClickListener {
            onClickRefresh(data)
        }
        textRefreshRecommendation?.show()
    }

    private fun hideRefreshRecommendation() {
        textRefreshRecommendation?.hide()
    }

    private fun onClickRefresh(data: HomeRealTimeRecomUiModel) {
        listener?.refreshRealTimeRecommendation(data)
        textRefreshRecommendation?.hide()
    }

    private fun renderRecommendationCarousel(data: HomeRealTimeRecomUiModel) {
        data.widget?.let { widget ->
            recommendationCarousel?.bind(
                carouselData = RecommendationCarouselData(
                    recommendationData = widget,
                    state = data.carouselState,
                ),
                basicListener = this,
                tokonowListener = this
            )
            recommendationCarousel?.show()
        }
    }

    private fun renderCloseBtn(data: HomeRealTimeRecomUiModel) {
        imageClose?.setOnClickListener {
            listener?.removeRealTimeRecommendation(data)
            hideContent()
        }
        imageClose?.show()
    }

    private fun renderLoadingState() {
        hideContent()
        resetBackgroundColor()
        showProgressBar()
    }

    private fun setBackgroundColor() {
        container?.setBackgroundColor(
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN50))
    }

    private fun resetBackgroundColor() {
        container?.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun hideContent() {
        imageProduct?.hide()
        textTitle?.hide()
        textRefreshRecommendation?.hide()
        imageClose?.hide()
        recommendationCarousel?.hide()
        progressBar?.hide()
    }

    private fun showProgressBar() {
        progressBar?.show()
    }

    private fun hideProgressBar() {
        progressBar?.hide()
    }

    override fun onRecomBannerImpressed(
        data: RecommendationCarouselData,
        adapterPosition: Int
    ) {
    }

    override fun onRecomBannerClicked(
        data: RecommendationCarouselData,
        applink: String,
        adapterPosition: Int
    ) {
    }

    override fun onChannelWidgetEmpty() {
    }

    override fun onChannelExpired(
        data: RecommendationCarouselData,
        channelPosition: Int
    ) {
    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) { /* nothing to do */
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
    }

    override fun onShowError(pageName: String, e: Throwable) {
    }

    override fun onRecomProductCardImpressed(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        listener?.onRecomProductCardImpressed(
            recomItem,
            data.recommendationData.title,
            data.recommendationData.pageName
        )
    }

    override fun onSeeAllBannerClicked(
        data: RecommendationCarouselData,
        applink: String
    ) {
    }

    override fun onRecomProductCardClicked(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        applink: String,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        listener?.onRecomProductCardClicked(
            recomItem,
            data.recommendationData.title,
            recomItem.position.toString(),
            applink
        )
    }

    override fun onRecomProductCardAddToCartNonVariant(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int,
        quantity: Int
    ) {
        listener?.onProductRecomNonVariantClick(
            recomItem,
            quantity,
            data.recommendationData.title,
            recomItem.position.toString()
        )
    }

    override fun onRecomProductCardAddVariantClick(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int
    ) {
        listener?.onRecomProductCardAddVariantClick(data, recomItem, adapterPosition)
    }
}

