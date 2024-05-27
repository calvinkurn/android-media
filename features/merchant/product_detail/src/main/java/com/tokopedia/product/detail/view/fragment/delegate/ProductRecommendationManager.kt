package com.tokopedia.product.detail.view.fragment.delegate

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.showToasterError
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.util.PdpUiUpdater
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.viewmodel.product_detail.ProductDetailViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ProductRecommendationEvent
import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ViewState
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.ProductRecommUiState
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.asLifecycleOwner
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.product.detail.common.R as productdetailcommonR

class PartialRecommendationManager(
    val mediator: PdpComponentCallbackMediator
) : PartialRecommendationManagerListener {

    private val lifeCycleManager = object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            observeRecommendationProduct()
        }
    }

    private val context: Context?
        get() = mediator.rootView.context
    private val viewLifecycleOwner: LifecycleOwner
        get() = mediator.rootView.viewLifecycleOwner
    private val viewModel: ProductDetailViewModel
        get() = mediator.pdpViewModel
    private val pdpUiUpdater: PdpUiUpdater?
        get() = mediator.uiUpdater
    private val remoteConfig: RemoteConfig
        get() = mediator.pdpRemoteConfig

    override fun init() {
        context?.asLifecycleOwner()?.let {
            configureLifecycle(it)
        }
    }

    override fun loadRecommendation(
        pageName: String,
        queryParam: String,
        thematicId: String,
        isViewToView: Boolean
    ) {
        val p1 = viewModel.getProductInfoP1 ?: ProductInfoP1()
        val miniCart = if (isViewToView) null else viewModel.p2Data.value?.miniCart
        viewModel.onRecommendationEvent(
            ProductRecommendationEvent.LoadRecommendation(
                pageName = pageName,
                productId = p1.basic.productID,
                isTokoNow = p1.basic.isTokoNow,
                miniCart = miniCart,
                queryParam = queryParam,
                thematicId = thematicId
            )
        )
    }

    private fun observeRecommendationProduct() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.productListData.collect {
                collectRecommendation(it)
            }
        }

        /**
         * This is retained only for fallback and risk mitigation
         * will be remove soon
         */
        viewLifecycleOwner.observe(viewModel.loadTopAdsProduct) { data ->
            observeOldRecommendation(data)
        }

        viewLifecycleOwner.observe(viewModel.statusFilterTopAdsProduct) {
            if (it is Fail) {
                context?.let { ctx ->
                    mediator.rootView.view.showToasterError(
                        ctx.getString(R.string.recom_filter_chip_click_error_network).orEmpty(),
                        ctaText = ctx.getString(productdetailcommonR.string.pdp_common_oke)
                    )
                }
            }
        }

        viewLifecycleOwner.observe(viewModel.filterTopAdsProduct) { data ->
            pdpUiUpdater?.updateFilterRecommendationData(data)
            mediator.updateUi()
        }
    }

    private fun collectRecommendation(recomList: MutableList<ProductRecommUiState>) {
        recomList.forEach {
            val result = it.data
            when (result) {
                is ViewState.RenderSuccess -> {
                    if (result.data.recommendationItemList.isNotEmpty()) {
                        renderSuccessRecom(result.data)
                    } else {
                        // recomUiPageName used because there is possibilites gql recom return empty pagename
                        pdpUiUpdater?.removeComponent(result.data.recomUiPageName)
                    }
                }

                is ViewState.RenderFailure -> {
                    renderFailureRecom(result.throwable)
                }

                else -> {
                }
            }
        }
        mediator.updateUi()
    }

    /**
     * This is retained only for fallback and risk mitigation
     * will be remove soon
     */
    private fun observeOldRecommendation(result: Result<RecommendationWidget>) {
        result.doSuccessOrFail({
            if (it.data.recommendationItemList.isNotEmpty()) {
                renderSuccessRecom(it.data)
            } else {
                // recomUiPageName used because there is possibilites gql recom return empty pagename
                pdpUiUpdater?.removeComponent(it.data.recomUiPageName)
            }
            mediator.updateUi()
        }, {
            renderFailureRecom(it)
            mediator.updateUi()
        })
    }

    private fun renderFailureRecom(e: Throwable) {
        pdpUiUpdater?.removeComponent(e.message ?: "")
        mediator.logException(e)
    }

    private fun renderSuccessRecom(result: RecommendationWidget) {
        val enableComparisonWidget = remoteConfig.getBoolean(
            RemoteConfigKey.RECOMMENDATION_ENABLE_COMPARISON_WIDGET,
            true
        )
        if (enableComparisonWidget) {
            when (result.layoutType) {
                RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET -> {
                    pdpUiUpdater?.updateComparisonBpcDataModel(
                        viewModel.getProductInfoP1,
                        result,
                    )
                }

                RecommendationTypeConst.TYPE_COMPARISON_WIDGET -> {
                    pdpUiUpdater?.updateComparisonDataModel(
                        viewModel.getProductInfoP1,
                        result
                    )
                }

                RecommendationTypeConst.TYPE_VIEW_TO_VIEW -> {
                    renderViewToView(result)
                }

                else -> {
                    pdpUiUpdater?.updateRecommendationData(result)
                }
            }
        } else {
            pdpUiUpdater?.updateRecommendationData(result)
        }
    }

    private fun renderViewToView(result: RecommendationWidget) {
        if (result.recommendationItemList.size > 1) {
            pdpUiUpdater?.updateViewToViewData(
                result.copy(
                    recommendationItemList = result.recommendationItemList
                )
            )
        } else {
            pdpUiUpdater?.removeComponent(result.recomUiPageName)
        }
    }

    private fun configureLifecycle(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Fragment) {
            lifecycleOwner.viewLifecycleOwnerLiveData.observe(lifecycleOwner) {
                it.lifecycle.addObserver(lifeCycleManager)
            }
        } else {
            lifecycleOwner.lifecycle.addObserver(lifeCycleManager)
        }
    }
}

interface PartialRecommendationManagerListener {
    fun init()
    fun loadRecommendation(
        pageName: String,
        queryParam: String,
        thematicId: String,
        isViewToView: Boolean = false
    )
}
