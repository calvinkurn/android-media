package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomBottomSheetState
import com.tokopedia.product.detail.databinding.BsProductMediaRecomBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ProductMediaRecomBottomSheetManager(
    private val fragmentManager: FragmentManager,
    private val listener: DynamicProductDetailListener
) {

    companion object {
        const val TAG = "ProductMediaRecomBottomSheet"
    }

    private val bottomSheet by lazyThreadSafetyNone { BottomSheet() }

    fun updateState(state: ProductMediaRecomBottomSheetState) {
        when (state) {
            is ProductMediaRecomBottomSheetState.Dismissed -> {
                getProductMediaRecomBottomSheet()?.dismiss()
            }
            is ProductMediaRecomBottomSheetState.Loading -> {
                updateProductMediaRecomBottomSheet {
                    setTitle(state.title)
                    showLoading()
                }
            }
            is ProductMediaRecomBottomSheetState.ShowingData -> {
                updateProductMediaRecomBottomSheet {
                    setTitle(state.title)
                    showData(state.recomWidgetData)
                }
            }
            is ProductMediaRecomBottomSheetState.ShowingError -> {
                updateProductMediaRecomBottomSheet {
                    setTitle(state.title)
                    showError(state.error)
                }
            }
        }
    }

    private fun getProductMediaRecomBottomSheet(): BottomSheet? {
        return fragmentManager.findFragmentByTag(TAG) as? BottomSheet
    }

    private fun updateProductMediaRecomBottomSheet(action: BottomSheet.() -> Unit) {
        if (bottomSheet.isAdded) {
            action(bottomSheet)
            bottomSheet.setListener(listener)
        } else {
            bottomSheet.setShowListener {
                action(bottomSheet)
                bottomSheet.setListener(listener)
                bottomSheet.setShowListener { }
            }
            bottomSheet.show(fragmentManager, TAG)
        }
    }

    class BottomSheet :
        BottomSheetUnify(),
        RecomCarouselWidgetBasicListener,
        RecommendationCarouselTokonowListener {

        companion object {
            private const val TOPADS_CLASS_NAME = "com.tokopedia.product.detail.view.bottomsheet.ProductMediaRecomBottomSheetManager.BottomSheet"
        }

        private var binding by viewBinding(BsProductMediaRecomBinding::bind)

        init {
            clearContentPadding = true
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = BsProductMediaRecomBinding.inflate(
                inflater,
                container,
                false
            ).apply { binding = this }.root
            setChild(view)
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {}

        override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
            RouteManager.route(context, applink)
        }

        override fun onRecomChannelImpressed(data: RecommendationCarouselData) {}

        override fun onRecomProductCardImpressed(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
        ) {
            if (recomItem.isTopAds) onTopAdsProductCardImpressed(recomItem)
        }

        override fun onRecomProductCardClicked(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            applink: String,
            itemPosition: Int,
            adapterPosition: Int
        ) {
            if (recomItem.isTopAds) onTopAdsProductCardClicked(recomItem)
            RouteManager.route(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                recomItem.productId.toString()
            )
        }

        override fun onRecomBannerImpressed(
            data: RecommendationCarouselData,
            adapterPosition: Int
        ) {}

        override fun onRecomBannerClicked(
            data: RecommendationCarouselData,
            applink: String,
            adapterPosition: Int
        ) {
            RouteManager.route(context, applink)
        }

        override fun onChannelWidgetEmpty() {}

        override fun onWidgetFail(pageName: String, e: Throwable) {}

        override fun onShowError(pageName: String, e: Throwable) {}

        override fun onRecomProductCardAddToCartNonVariant(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            adapterPosition: Int,
            quantity: Int
        ) {}

        override fun onRecomProductCardAddVariantClick(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            adapterPosition: Int
        ) {}

        fun setListener(listener: DynamicProductDetailListener) {
            setOnDismissListener { listener.onProductMediaRecomBottomSheetDismissed() }
            binding?.globalErrorProductMedia?.setActionClickListener {
                listener.onShowProductMediaRecommendationClicked()
            }
        }

        fun showLoading() {
            setupRecom(RecommendationCarouselData(state = RecommendationCarouselData.STATE_LOADING))
            binding?.recomProductMedia?.show()
            binding?.globalErrorProductMedia?.gone()
        }

        fun showData(recomWidgetData: RecommendationWidget) {
            setupRecom(
                RecommendationCarouselData(
                    recommendationData = recomWidgetData,
                    state = RecommendationCarouselData.STATE_READY
                )
            )
            binding?.recomProductMedia?.show()
            binding?.globalErrorProductMedia?.gone()
        }

        fun showError(error: Throwable) {
            setupRecom(RecommendationCarouselData(state = RecommendationCarouselData.STATE_FAILED))
            setupErrorState(error)
            binding?.recomProductMedia?.gone()
            binding?.globalErrorProductMedia?.show()
        }

        private fun setupRecom(carouselData: RecommendationCarouselData) {
            binding?.recomProductMedia?.bind(
                carouselData = carouselData,
                basicListener = this,
                tokonowListener = this
            )
        }

        private fun setupErrorState(error: Throwable) {
            val errorMessage = ErrorHandler.getErrorMessage(context, error)
            binding?.globalErrorProductMedia?.setType(
                when (error) {
                    is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                        GlobalError.NO_CONNECTION
                    }
                    else -> {
                        GlobalError.SERVER_ERROR
                    }
                }
            )
            binding?.globalErrorProductMedia?.errorDescription?.text = errorMessage
        }

        private fun onTopAdsProductCardClicked(recomItem: RecommendationItem) {
            val productId = recomItem.productId.toString()
            val context = context ?: return
            TopAdsUrlHitter(context).hitClickUrl(
                TOPADS_CLASS_NAME,
                recomItem.clickUrl,
                productId,
                recomItem.name,
                recomItem.imageUrl
            )
        }

        private fun onTopAdsProductCardImpressed(recomItem: RecommendationItem) {
            val context = context ?: return
            TopAdsUrlHitter(context).hitImpressionUrl(
                TOPADS_CLASS_NAME,
                recomItem.trackerImageUrl,
                recomItem.productId.toString(),
                recomItem.name,
                recomItem.imageUrl
            )
        }
    }
}
