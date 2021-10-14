package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
import com.tokopedia.product.detail.data.model.ui.MultiBundle
import com.tokopedia.product.detail.data.model.ui.SingleBundle
import com.tokopedia.product.detail.data.model.ui.ViewBundle
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductBundlingViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductBundlingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_bundling

        private const val BUNDLE_TYPE_SINGLE = "SINGLE"
        private const val BUNDLE_TYPE_MULTIPLE = "MULTIPLE"
        private const val BUNDLE_ITEM_MINIMUM_COUNT_SINGLE = 1
        private const val BUNDLE_ITEM_MINIMUM_COUNT_MULTIPLE = 2
    }

    private val viewBundleDelegate = lazy { ViewBundle(view) }
    private val viewBundle: ViewBundle by viewBundleDelegate

    private val multiBundleDelegate = lazy { MultiBundle(view) }
    private val multiBundle: MultiBundle by multiBundleDelegate

    private val singleBundleDelegate = lazy { SingleBundle(view) }
    private val singleBundle: SingleBundle by singleBundleDelegate

    override fun bind(element: ProductBundlingDataModel) {

        val bundle = element.bundleInfo ?: return hideComponent()

        val bundleItems = bundle.bundleItems
        val bundleType = bundle.type
        if (!checkBundleItems(bundleType, bundleItems)) return hideComponent()

        val componentTrackDataModel = getComponentTrackData(element)

        val bundleId = bundle.bundleId
        viewBundle.process(bundle) {
            listener.onClickCheckBundling(bundleId, bundleType, componentTrackDataModel)
        }

        when (bundleType) {
            BUNDLE_TYPE_SINGLE -> showSingleBundle(bundleItems.first(), bundle.name)
            BUNDLE_TYPE_MULTIPLE -> showMultiBundle(bundle, bundleId, componentTrackDataModel)
        }

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressionProductBundling(bundleId, bundleType, componentTrackDataModel)
        }
    }

    private fun showSingleBundle(item: BundleInfo.BundleItem, bundleName: String) {
        if (multiBundleDelegate.isInitialized()) multiBundle.hide()
        singleBundle.process(item, bundleName)
    }

    private fun showMultiBundle(
        bundle: BundleInfo,
        bundleId: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (singleBundleDelegate.isInitialized()) singleBundle.hide()
        multiBundle.process(bundle) { productId ->
            listener.onClickProductInBundling(bundleId, productId, componentTrackDataModel)
        }
    }

    private fun checkBundleItems(
        bundleType: String,
        bundleItems: List<BundleInfo.BundleItem>
    ): Boolean {

        val minimumItemCount = when (bundleType) {
            BUNDLE_TYPE_SINGLE -> BUNDLE_ITEM_MINIMUM_COUNT_SINGLE
            BUNDLE_TYPE_MULTIPLE -> BUNDLE_ITEM_MINIMUM_COUNT_MULTIPLE
            else -> return false
        }

        return bundleItems.size >= minimumItemCount
    }

    private fun hideComponent() {
        if (viewBundleDelegate.isInitialized()) {
            viewBundle.hide()
        }
    }

    private fun getComponentTrackData(element: ProductBundlingDataModel) = ComponentTrackDataModel(
        componentType = element.type(),
        componentName = element.name(),
        adapterPosition = adapterPosition + 1
    )

}