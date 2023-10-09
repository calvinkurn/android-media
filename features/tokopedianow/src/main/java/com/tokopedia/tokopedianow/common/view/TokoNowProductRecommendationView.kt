package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState.LOADED
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState.LOADING
import com.tokopedia.tokopedianow.common.listener.TokoNowProductRecommendationCallback
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView.ProductCardCompactCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.inflateView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductRecommendationViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.tokopedianow.R

class TokoNowProductRecommendationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): BaseCustomView(context, attrs) {

    private var binding: LayoutTokopedianowProductRecommendationViewBinding =
        LayoutTokopedianowProductRecommendationViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    private var viewModel: TokoNowProductRecommendationViewModel? = null
    private var listener: TokoNowProductRecommendationListener? = null
    private var requestParam: GetRecommendationRequestParam? = null
    private var header: TokoNowDynamicHeaderView? = null
    private var mTickerPageSource: String = String.EMPTY

    private fun TokoNowProductRecommendationViewModel.observeRecommendationWidget() {
        productRecommendation.observe(context as AppCompatActivity) {
            when(it) {
                is Success -> setItems(it.data)
                is Fail -> hideWidget()
            }
        }
    }

    private fun TokoNowProductRecommendationViewModel.observeProductModelsUpdate() {
        productModelsUpdate.observe(context as AppCompatActivity) {
            binding.productCardCarousel.updateItems(it)
        }
    }

    private fun TokoNowProductRecommendationViewModel.observeLoadingState() {
        loadingState.observe(context as AppCompatActivity) { isShown ->
            binding.productCardShimmering.root.showWithCondition(isShown)
            binding.productCardCarousel.showWithCondition(!isShown)
            header?.showWithCondition(!isShown)
        }
    }

    private fun setItems(
        productRecommendation: TokoNowProductRecommendationViewUiModel
    ) {
        binding.apply {
            root.show()
            productCardCarousel.bindItems(
                items = productRecommendation.productModels,
                seeMoreModel = productRecommendation.seeMoreModel
            )
            header?.setModel(
                model = productRecommendation.headerModel
            )
        }
    }

    private fun hideWidget() {
        binding.root.hide()
        listener?.hideProductRecommendationWidget()
    }

    /**
     * setting the data from outside of this widget
     */
    fun bind(
        items: List<Visitable<*>> = listOf(),
        seeMoreModel: ProductCardCompactCarouselSeeMoreUiModel? = null,
        header: TokoNowDynamicHeaderUiModel? = null,
        state: TokoNowProductRecommendationState = LOADED
    ) {
        binding.productCardShimmering.root.showWithCondition(state == LOADING)
        binding.productCardCarousel.showIfWithBlock(state == LOADED) {
            bindItems(
                items = items,
                seeMoreModel = seeMoreModel
            )
        }
        bindHeader(header, state)
    }

    /**
     * The listener will be used when data are set outside of this widget
     */
    fun setListener(
        productCardCarouselListener: ProductCardCompactCarouselListener? = null,
        headerCarouselListener: TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener? = null
    ) {
        binding.productCardCarousel.setListener(
            productCardCarouselListener = productCardCarouselListener,
        )
        header?.setListener(
            headerListener =  headerCarouselListener
        )
    }

    /**
     * setting the data via fetching gql
     */
    fun setRequestParam(
        getRecommendationRequestParam: GetRecommendationRequestParam? = null,
        tickerPageSource: String
    ) {
        requestParam = getRecommendationRequestParam
        mTickerPageSource = tickerPageSource
    }

    /**
     * The listener will be used for fetching gql inside this widget
     */
    fun setListener(
        productRecommendationListener: TokoNowProductRecommendationListener? = null
    ) {
        if (listener == null) {
            listener = productRecommendationListener

            viewModel = listener?.getProductRecommendationViewModel()

            viewModel?.run {
                val callback = TokoNowProductRecommendationCallback(
                    viewModel = this,
                    listener = listener
                )
                setListener(
                    productCardCarouselListener = callback,
                    headerCarouselListener = callback
                )

                observeRecommendationWidget()
                observeProductModelsUpdate()
                observeLoadingState()

                requestParam?.let { requestParam ->
                    setRecommendationPageName(requestParam.pageName)
                    getFirstRecommendationCarousel(requestParam, mTickerPageSource)
                }
            }
        }
    }

    fun scrollToPosition(
        position: Int
    ) {
        binding.productCardCarousel.scrollToPosition(position)
    }

    fun setRecycledViewPool(
        recycledViewPool: RecycledViewPool?
    ) {
        binding.productCardCarousel.setRecycledViewPool(recycledViewPool)
    }

    private fun bindHeader(
        headerUiModel: TokoNowDynamicHeaderUiModel?,
        state: TokoNowProductRecommendationState
    ) {
        if(headerUiModel != null && state == LOADED) {
            inflateHeader()
            header?.setModel(headerUiModel)
        } else {
            header?.gone()
        }
    }

    private fun inflateHeader() {
        if(header == null) {
            val view = binding.headerViewStub
                .inflateView(R.layout.layout_tokopedianow_dynamic_header_view)
            header = view.findViewById(R.id.header)
        }
    }

    interface TokoNowProductRecommendationListener {
        fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel?
        fun hideProductRecommendationWidget()
        fun openLoginPage()
        fun productCardAddVariantClicked(
            productId: String,
            shopId: String
        )
        fun productCardClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            isLogin: Boolean,
            userId: String
        )
        fun productCardImpressed(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            isLogin: Boolean,
            userId: String
        )
        fun seeMoreClicked(
            seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel
        )
        fun seeAllClicked(
            appLink: String
        )
        fun productCardAddToCartBlocked()
    }
}
