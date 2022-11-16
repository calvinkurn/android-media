package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.*
import com.tokopedia.productbundlewidget.presentation.ProductBundleWidgetView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.shop.databinding.ItemShopHomeProductBundleMultipleBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleMultipleViewHolder(
    itemView: View,
    private val multipleProductBundleListener: MultipleProductBundleListener,
    private val bundleListSize: Int,
    private val widgetLayout: ShopHomeWidgetLayout
): RecyclerView.ViewHolder(itemView), ProductBundleWidgetListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_bundle_multiple
    }

    private var viewBinding: ItemShopHomeProductBundleMultipleBinding? by viewBinding()
    private var productBundleWidgetView: ProductBundleWidgetView? = null
    private var parentMultipleBundle: ShopHomeProductBundleItemUiModel = ShopHomeProductBundleItemUiModel()
    private var widgetTitle: String = String.EMPTY
    private var widgetName: String = String.EMPTY

    init {
        viewBinding?.apply {
            productBundleWidgetView = productBundleWidget
        }
    }

    override fun onBundleProductClicked(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel
    ) {
        val selectedBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedMultipleBundle)
        val selectedBundleProduct = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        multipleProductBundleListener.onMultipleBundleProductClicked(
            shopId = parentMultipleBundle.shopId,
            warehouseId = parentMultipleBundle.warehouseId,
            selectedProduct = selectedBundleProduct,
            selectedMultipleBundle = selectedBundle,
            bundleName = parentMultipleBundle.bundleName,
            bundleType = parentMultipleBundle.bundleType,
            bundlePosition = adapterPosition,
            widgetTitle = widgetTitle,
            widgetName = widgetName,
            productItemPosition = selectedMultipleBundle.products.indexOf(selectedProduct)
        )
    }

    override fun onMultipleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        productDetails: List<BundleProductUiModel>
    ) {
        val selectedMultipleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedBundle)
        multipleProductBundleListener.addMultipleBundleToCart(
            shopId = parentMultipleBundle.shopId,
            warehouseId = parentMultipleBundle.warehouseId,
            selectedMultipleBundle = selectedMultipleBundle,
            bundleListSize = bundleListSize,
            productDetails = parentMultipleBundle.bundleProducts,
            bundleName = parentMultipleBundle.bundleName,
            bundleType = parentMultipleBundle.bundleType,
            bundlePosition = adapterPosition,
            widgetLayout = widgetLayout,
            bundleGroupId = parentMultipleBundle.bundleGroupId
        )
    }

    override fun impressionMultipleBundle(
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        val selectedBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedMultipleBundle)
        itemView.addOnImpressionListener(parentMultipleBundle) {
            multipleProductBundleListener.impressionProductBundleMultiple(
                shopId = parentMultipleBundle.shopId,
                warehouseId = parentMultipleBundle.warehouseId,
                selectedMultipleBundle = selectedBundle,
                bundleName = parentMultipleBundle.bundleName,
                bundleType = parentMultipleBundle.bundleType,
                bundlePosition = adapterPosition
            )
        }
    }

    override fun impressionMultipleBundleProduct(
        selectedProduct: BundleProductUiModel,
        selectedBundle: BundleDetailUiModel
    ) {
        val selectedBundleProduct = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        val selectedMultipleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedBundle)
        multipleProductBundleListener.impressionProductItemBundleMultiple(
            selectedProduct = selectedBundleProduct,
            selectedMultipleBundle = selectedMultipleBundle,
            bundleName = parentMultipleBundle.bundleName,
            bundlePosition = adapterPosition,
            widgetTitle = widgetTitle,
            widgetName = widgetName,
            productItemPosition = selectedBundle.products.indexOf(selectedProduct)
        )
    }

    fun bind(bundle: ShopHomeProductBundleItemUiModel, widgetTitle: String, widgetName: String) {
        this.widgetTitle = widgetTitle
        this.widgetName = widgetName
        parentMultipleBundle = bundle
        // build param and render product bundle widget
        val param = GetBundleParamBuilder()
            .setBundleId(listOf(bundle.bundleGroupId))
            .setWidgetType(WidgetType.TYPE_1)
            .setPageSource(ShopPageConstant.SOURCE)
            .build()
        productBundleWidgetView?.setListener(this)
        productBundleWidgetView?.getBundleData(param)
    }
}
