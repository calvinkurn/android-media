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
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.shop.databinding.ItemShopHomeProductBundleSingleBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleSingleViewHolder(
    itemView: View,
    private val singleProductBundleListener: SingleProductBundleListener,
    private val bundleListSize: Int,
    private val widgetLayout: ShopHomeWidgetLayout
) : RecyclerView.ViewHolder(itemView), ProductBundleWidgetListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_bundle_single
    }

    private var viewBinding: ItemShopHomeProductBundleSingleBinding? by viewBinding()
    private var productBundleWidgetView: ProductBundleWidgetView? = null
    private var parentSingleBundle: ShopHomeProductBundleItemUiModel = ShopHomeProductBundleItemUiModel()
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
        val selectedSingleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedMultipleBundle)
        val selectedBundleProduct = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        singleProductBundleListener.onSingleBundleProductClicked(
            shopId = parentSingleBundle.shopId,
            warehouseId = parentSingleBundle.warehouseId,
            selectedProduct = selectedBundleProduct,
            selectedSingleBundle = selectedSingleBundle,
            bundleName = parentSingleBundle.bundleName,
            bundlePosition = selectedMultipleBundle.products.indexOf(selectedProduct),
            widgetTitle = widgetTitle,
            widgetName = widgetName,
            productItemPosition = adapterPosition,
            bundleType = parentSingleBundle.bundleType
        )
    }

    override fun onSingleBundleChipsSelected(
        selectedProduct: BundleProductUiModel,
        selectedBundle: BundleDetailUiModel,
        bundleName: String
    ) {
        val selectedProductVariant = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        val selectedSingleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedBundle)
        singleProductBundleListener.onTrackSingleVariantChange(
            selectedProduct = selectedProductVariant,
            selectedSingleBundle = selectedSingleBundle,
            bundleName = bundleName
        )
    }

    override fun onSingleBundleActionButtonClicked(
        selectedBundle: BundleDetailUiModel,
        bundleProducts: BundleProductUiModel
    ) {
        val selectedSingleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedBundle)
        val selectedBundleProduct = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(bundleProducts)
        singleProductBundleListener.addSingleBundleToCart(
            shopId = parentSingleBundle.shopId,
            warehouseId = parentSingleBundle.warehouseId,
            selectedBundle = selectedSingleBundle,
            bundleListSize = bundleListSize,
            bundleProducts = selectedBundleProduct,
            bundleName = parentSingleBundle.bundleName,
            bundleType = parentSingleBundle.bundleType,
            bundlePosition = adapterPosition,
            widgetLayout = widgetLayout,
            bundleGroupId = selectedBundle.bundleId
        )
    }

    override fun impressionSingleBundle(
        selectedBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String
    ) {
        val selectedSingleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedBundle)
        val selectedBundleProduct = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        itemView.addOnImpressionListener(parentSingleBundle) {
            singleProductBundleListener.impressionProductBundleSingle(
                shopId = parentSingleBundle.shopId,
                warehouseId = parentSingleBundle.warehouseId,
                selectedSingleBundle = selectedSingleBundle,
                selectedProduct = selectedBundleProduct,
                bundleName = parentSingleBundle.bundleName,
                bundlePosition = adapterPosition,
                widgetTitle = widgetTitle,
                widgetName = widgetName,
                bundleType = parentSingleBundle.bundleType
            )
        }
    }

    fun bind(bundle: ShopHomeProductBundleItemUiModel, widgetTitle: String, widgetName: String) {
        this.widgetTitle = widgetTitle
        this.widgetName = widgetName
        parentSingleBundle = bundle
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
