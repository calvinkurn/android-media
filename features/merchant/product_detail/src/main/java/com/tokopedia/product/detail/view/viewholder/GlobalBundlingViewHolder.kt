package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.GlobalBundlingDataModel
import com.tokopedia.product.detail.databinding.ItemGlobalBundlingBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product_bundle.common.data.constant.BundlingPageSource
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.productbundlewidget.model.GetBundleParamBuilder
import com.tokopedia.productbundlewidget.model.WidgetType

class GlobalBundlingViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) :
    ProductDetailPageViewHolder<GlobalBundlingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_global_bundling

        private const val BUNDLE_TYPE_SINGLE = "SINGLE"
        private const val BUNDLE_TYPE_MULTIPLE = "MULTIPLE"
    }

    val binding = ItemGlobalBundlingBinding.bind(view)

    override fun bind(element: GlobalBundlingDataModel) {
        val data = element.data

        val widgetType = if (data.widgetType == -1) {
            GlobalBundlingDataModel.DEFAULT_WIDGET_TYPE
        } else data.widgetType

        val param = GetBundleParamBuilder()
            .setProductId(data.productId)
//            .setWarehouseId(data.whId)
//            .setWidgetType(widgetType) - belum bisa terima literal int
            .setWidgetType(WidgetType.TYPE_2)
            .setPageSource(BundlingPageSource.PRODUCT_DETAIL_PAGE)
            .build()

        binding.pdpBundlingWidget.run {
            setTitleText(data.title)
            getBundleData(param)
        }

        val listener = object : ProductBundleWidgetListener {
            override fun impressionMultipleBundle(
                selectedMultipleBundle: BundleDetailUiModel,
                bundlePosition: Int
            ) {
                itemView.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressionProductBundling(
                        selectedMultipleBundle.bundleId,
                        BUNDLE_TYPE_MULTIPLE,
                        getComponentTrackData(element)
                    )
                }
                println("vindo - impression multiple bundle - ${selectedMultipleBundle.bundleId}")
            }

            override fun impressionMultipleBundleProduct(
                selectedProduct: BundleProductUiModel,
                selectedBundle: BundleDetailUiModel
            ) {
                println("vindo - impression multiple bundle product ")
                // TODO vindo - we do not use this
            }

            override fun impressionSingleBundle(
                selectedBundle: BundleDetailUiModel,
                selectedProduct: BundleProductUiModel,
                bundleName: String
            ) {

                listener.onImpressionProductBundling(
                    selectedBundle.bundleId,
                    BUNDLE_TYPE_SINGLE,
                    getComponentTrackData(element)
                )
                println("vindo - impression single bundle")
            }

            override fun onMultipleBundleActionButtonClicked(
                selectedBundle: BundleDetailUiModel,
                productDetails: List<BundleProductUiModel>
            ) {
                println("vindo - multiple bundle action button clicked")
                listener.onClickCheckBundling(
                    selectedBundle.bundleId,
                    BUNDLE_TYPE_MULTIPLE,
                    getComponentTrackData(element)
                )
            }

            override fun onSingleBundleActionButtonClicked(
                selectedBundle: BundleDetailUiModel,
                bundleProducts: BundleProductUiModel
            ) {
                println("vindo - single bundle action button clicked")
                listener.onClickCheckBundling(
                    selectedBundle.bundleId,
                    BUNDLE_TYPE_SINGLE,
                    getComponentTrackData(element)
                )
            }

            override fun onSingleBundleChipsSelected(
                selectedProduct: BundleProductUiModel,
                selectedBundle: BundleDetailUiModel,
                bundleName: String
            ) {
                println("vindo - single bundle chips selected")
            }

            override fun onBundleProductClicked(
                bundle: BundleUiModel,
                selectedMultipleBundle: BundleDetailUiModel,
                selectedProduct: BundleProductUiModel
            ) {
                println("vindo - bundle product clicked")
            }
        }
        binding.pdpBundlingWidget.setListener(listener)
    }
}
