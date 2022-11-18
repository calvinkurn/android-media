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
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductRecommendationViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class TokoNowProductRecommendationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): BaseCustomView(context, attrs) {

    @Inject
    lateinit var userSession: UserSession

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
                is Fail -> hideWidget()
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

    private fun hideWidget() {
        listener?.hideProductRecommendationWidget()
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
     * The listener will be used when data are set outside of this widget
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
     * The listener will be used for fetching gql inside this widget
     */
    fun setListener(
        productRecommendationListener: TokoNowProductRecommendationListener? = null
    ) {
        if (listener == null) {
            listener = productRecommendationListener

            viewModel = listener?.getProductRecommendationViewModel()

            val callback = TokoNowProductRecommendationCallback(
                viewModel = viewModel,
                listener = listener,
                userSession = userSession
            )
            setListener(
                productCardCarouselListener = callback,
                headerCarouselListener = callback
            )

            viewModel?.run {
                observeRecommendationWidget()
                observeProductModelsUpdate()

                requestParam?.let { requestParam ->
                    getRecommendationCarousel(requestParam)
                }
            }
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
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun productCardImpressed(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun productCardQuantityChanged(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun seeMoreClicked(
            seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
        )
        fun seeAllClicked(
            appLink: String
        )
    }
}
