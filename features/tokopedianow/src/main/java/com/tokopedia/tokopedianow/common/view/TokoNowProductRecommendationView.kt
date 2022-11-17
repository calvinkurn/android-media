package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.di.component.DaggerCommonComponent
import com.tokopedia.tokopedianow.common.listener.TokoNowProductRecommendationCallback
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductRecommendationViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success

class TokoNowProductRecommendationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): BaseCustomView(context, attrs) {

    private var binding: LayoutTokopedianowProductRecommendationViewBinding

    private var viewModel: TokoNowProductRecommendationViewModel? = null
    private var listener: TokoNowProductRecommendationListener? = null
    private var requestParam: GetRecommendationRequestParam? = null

    init {
        initInjector()
        binding = LayoutTokopedianowProductRecommendationViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private fun initInjector() {
        DaggerCommonComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun TokoNowProductRecommendationViewModel.observeRecommendationWidget() {
        productRecommendation.observe(context as AppCompatActivity) {
            when(it) {
                is Success -> setItems(it.data)
            }
        }
    }

    private fun TokoNowProductRecommendationViewModel.observeMiniCartAdd() {
        miniCartAdd.observe(context as AppCompatActivity) { result ->
            when(result) {
                is Success -> {
                    Toaster.build(binding.root, result.data.errorMessage.firstOrNull().orEmpty()).show()
                    listener?.addToCartProductRecommendation()
                }
            }
        }
    }

    private fun TokoNowProductRecommendationViewModel.observeMiniCartUpdate() {
        miniCartUpdate.observe(context as AppCompatActivity) { result ->
            when(result) {
                is Success -> {
                    listener?.updateFromCartProductRecommendation()
                }
            }
        }
    }

    private fun TokoNowProductRecommendationViewModel.observeMiniCartRemove() {
        miniCartRemove.observe(context as AppCompatActivity) { result ->
            when(result) {
                is Success -> {
                    Toaster.build(binding.root, result.data.second).show()
                    listener?.removeFromCartProductRecommendation()
                }
            }
        }
    }

    private fun TokoNowProductRecommendationViewModel.observeProductModelsUpdate() {
        productModelsUpdate.observe(context as AppCompatActivity) {
            binding.productCardCarousel.updateItems(it)
        }
    }

    private fun setItems(
        productRecommendation: TokoNowProductRecommendationViewUiModel
    ) {
        binding.apply {
            productCardCarousel.bindItems(
                items = productRecommendation.productModels,
                seeMoreModel = productRecommendation.seeMoreModel
            )
            header.setModel(
                model = productRecommendation.headerModel
            )
        }
    }

    /**
     * set data outside of this widget
     */
    fun setItems(
        items: List<Visitable<*>> = listOf(),
        seeMoreModel: TokoNowSeeMoreCardCarouselUiModel? = null
    ) {
        binding.productCardCarousel.bindItems(
            items = items,
            seeMoreModel = seeMoreModel
        )
    }

    /**
     * set data via fetching gql
     */
    fun setRequstParam(
        getRecommendationRequestParam: GetRecommendationRequestParam? = null
    ) {
        requestParam = getRecommendationRequestParam
    }

    fun setHeader(
        header: TokoNowDynamicHeaderUiModel? = null
    ) {
        binding.header.showIfWithBlock(header != null) {
            header?.apply {
                setModel(this)
            }
        }
    }

    /**
     * The listeners will be used when data are set outside of this widget
     */
    fun setListener(
        productCardCarouselListener: TokoNowProductCardCarouselListener? = null,
        headerCarouselListener: TokoNowDynamicHeaderListener? = null
    ) {
        binding.productCardCarousel.setListener(
            productCardCarouselListener = productCardCarouselListener,
        )
        binding.header.setListener(
            headerListener =  headerCarouselListener
        )
    }

    /**
     * The listeners will be used for fetching gql inside this widget
     */
    fun setListener(
        productRecommendationListener: TokoNowProductRecommendationListener? = null
    ) {
        if (listener == null) {
            listener = productRecommendationListener

            viewModel = listener?.getProductRecommendationViewModel()

            setListener(
                productCardCarouselListener = TokoNowProductRecommendationCallback(
                    viewModel = viewModel
                )
            )

            viewModel?.run {
                observeRecommendationWidget()
                observeMiniCartAdd()
                observeMiniCartUpdate()
                observeMiniCartRemove()
                observeProductModelsUpdate()

                requestParam?.let { requestParam ->
                    getRecommendationCarousel(requestParam)
                }
            }
        }
    }

    interface TokoNowProductRecommendationListener {
        fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel?
        fun addToCartProductRecommendation()
        fun removeFromCartProductRecommendation()
        fun updateFromCartProductRecommendation()
    }
}
