package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleTypes
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowBundleWidgetViewHolder
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class BundleWidgetCallback(
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics
): ProductBundleWidgetListener, TokoNowBundleWidgetViewHolder.TokoNowBundleWidgetListener {
    private var widgetId = ""

    override fun onBundleProductClicked(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        itemPosition: Int
    ) {
        if (bundle.bundleType == BundleTypes.MULTIPLE_BUNDLE) {
            analytics.trackClickMultipleBundleProduct(
                bundleId = selectedMultipleBundle.bundleId,
                bundleName = selectedMultipleBundle.bundleName,
                bundlePrice = selectedMultipleBundle.displayPrice,
                bundlePriceCut = selectedMultipleBundle.discountPercentage.toString(),
                position = itemPosition,
                productId = selectedProduct.productId
            )
        } else {
            analytics.trackClickSingleBundleProduct(
                bundleId = selectedMultipleBundle.bundleId,
                bundleName = selectedMultipleBundle.bundleName,
                bundlePrice = selectedMultipleBundle.displayPrice,
                bundlePriceCut = selectedMultipleBundle.discountPercentage.toString(),
                packageVariant = selectedMultipleBundle.minOrderWording,
                position = itemPosition,
                productId = selectedProduct.productId
            )
        }
    }

    override fun onMultipleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>,
        bundlePosition: Int
    ) {
        analytics.trackClickMultipleBundleButton(
            bundleId = selectedBundle.bundleId,
            bundleName = selectedBundle.bundleName,
            bundlePriceCut = selectedBundle.discountPercentage.toString(),
            position = bundlePosition
        )
    }

    override fun impressionMultipleBundle(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        analytics.trackImpressionMultipleBundle(
            bundleId = selectedMultipleBundle.bundleId,
            bundleName = selectedMultipleBundle.bundleName,
            bundlePriceCut = selectedMultipleBundle.discountPercentage.toString(),
            position = bundlePosition
        )
    }

    override fun onSingleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel,
        bundlePosition: Int
    ) {
        analytics.trackClickSingleBundleButton(
            bundleId = selectedBundle.bundleId,
            bundleName = selectedBundle.bundleName,
            bundlePriceCut = selectedBundle.discountPercentage.toString(),
            position = bundlePosition,
            productId = bundleProducts.productId
        )
    }

    override fun onSingleBundleChipsSelected(
        selectedProduct: BundleProductUiModel,
        selectedBundle: BundleDetailUiModel,
        bundleName: String
    ) {
        analytics.trackClickSingleBundleChip(
            bundleId = selectedBundle.bundleId,
            bundleName = selectedBundle.bundleName,
            bundlePriceCut = selectedBundle.discountPercentage.toString(),
            packageVariant = selectedBundle.minOrderWording,
            productId = selectedProduct.productId
        )
    }

    override fun impressionSingleBundle(
        selectedBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        analytics.trackImpressionSingleBundle(
            bundleId = selectedBundle.bundleId,
            bundleName = bundleName,
            bundlePriceCut = selectedBundle.discountPercentage.toString(),
            position = bundlePosition,
            productId = selectedProduct.productId
        )
    }

    override fun onBundleEmpty() {
        viewModel.removeWidget(widgetId)
    }

    override fun onError(it: Throwable) {
        viewModel.removeWidget(widgetId)
    }

    override fun setWidgetId(id: String) {
        widgetId = id
    }
}
