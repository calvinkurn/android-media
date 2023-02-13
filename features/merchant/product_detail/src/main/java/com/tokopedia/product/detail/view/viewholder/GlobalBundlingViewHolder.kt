package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.GlobalBundling
import com.tokopedia.product.detail.data.model.datamodel.GlobalBundlingDataModel
import com.tokopedia.product.detail.databinding.ItemGlobalBundlingBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product_bundle.common.data.constant.BundlingPageSource
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.productbundlewidget.model.GetBundleParamBuilder

class GlobalBundlingViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<GlobalBundlingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_global_bundling

        private const val BUNDLE_TYPE_SINGLE = "SINGLE"
        private const val BUNDLE_TYPE_MULTIPLE = "MULTIPLE"
    }

    val binding = ItemGlobalBundlingBinding.bind(view)

    override fun bind(element: GlobalBundlingDataModel) {
        if (!element.shouldRefresh) return

        setupBundleWidgetListener(element)
        initBundlingWidget(element.data)

        element.shouldRefresh = false
    }

    private fun initBundlingWidget(data: GlobalBundling) {

        val widgetType = if (data.widgetType == -1) {
            GlobalBundlingDataModel.DEFAULT_WIDGET_TYPE
        } else data.widgetType

        val param = GetBundleParamBuilder()
            .setProductId(data.productId)
            .setWarehouseId(data.whId)
            .setWidgetType(widgetType)
            .setPageSource(BundlingPageSource.PRODUCT_DETAIL_PAGE)
            .build()

        binding.pdpBundlingWidget.run {
            setTitleText(data.title)
            getBundleData(param)
        }
    }

    private fun setupBundleWidgetListener(element: GlobalBundlingDataModel) {
        val componentTrackDataModel = getComponentTrackData(element)

        val listener = object : ProductBundleWidgetListener {
            override fun impressionMultipleBundle(
                selectedMultipleBundle: BundleDetailUiModel,
                bundlePosition: Int
            ) {
                listener.onImpressionProductBundling(
                    selectedMultipleBundle.bundleId,
                    BUNDLE_TYPE_MULTIPLE,
                    componentTrackDataModel
                )
            }

            override fun impressionSingleBundle(
                selectedBundle: BundleDetailUiModel,
                selectedProduct: BundleProductUiModel,
                bundleName: String
            ) {
                listener.onImpressionProductBundling(
                    selectedBundle.bundleId,
                    BUNDLE_TYPE_SINGLE,
                    componentTrackDataModel
                )
            }

            override fun onMultipleBundleActionButtonClicked(
                selectedBundle: BundleDetailUiModel,
                productDetails: List<BundleProductUiModel>
            ) {
                listener.onClickActionButtonBundling(
                    selectedBundle.bundleId,
                    BUNDLE_TYPE_MULTIPLE,
                    componentTrackDataModel
                )
            }

            override fun onSingleBundleActionButtonClicked(
                selectedBundle: BundleDetailUiModel,
                bundleProducts: BundleProductUiModel
            ) {
                listener.onClickActionButtonBundling(
                    selectedBundle.bundleId,
                    BUNDLE_TYPE_SINGLE,
                    componentTrackDataModel
                )
            }

            override fun onBundleProductClicked(
                bundle: BundleUiModel,
                selectedMultipleBundle: BundleDetailUiModel,
                selectedProduct: BundleProductUiModel
            ) {
                listener.onClickProductInBundling(
                    bundle.selectedBundleId,
                    selectedProduct.productId,
                    componentTrackDataModel,
                    false
                )
            }
        }
        binding.pdpBundlingWidget.setListener(listener)
    }
}
