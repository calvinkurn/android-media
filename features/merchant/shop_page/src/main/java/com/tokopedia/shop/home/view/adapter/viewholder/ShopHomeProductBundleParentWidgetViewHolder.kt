package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.*
import com.tokopedia.productbundlewidget.presentation.ProductBundleWidgetView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.databinding.ItemShopHomeProductBundleParentWidgetBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.utils.view.binding.viewBinding

/**
 * author by Rafli Syam on 05/01/2022
 */
class ShopHomeProductBundleParentWidgetViewHolder(
    itemView: View,
    private val multipleProductBundleListener: MultipleProductBundleListener,
    private val singleProductBundleListener: SingleProductBundleListener
) : AbstractViewHolder<ShopHomeProductBundleListUiModel>(itemView), ProductBundleWidgetListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_bundle_parent_widget
        private const val BUNDLE_RV_MARGIN_TOP = 6f
    }

    private val viewBinding: ItemShopHomeProductBundleParentWidgetBinding? by viewBinding()
    private var productBundleWidgetView: ProductBundleWidgetView? = null

    private var shopId: String = String.EMPTY
    private var warehouseId: String = String.EMPTY

    private var bundleListUiModel: ShopHomeProductBundleListUiModel? = null
    private var bundleWidgetTitle: String = String.EMPTY
    private var bundleWidgetName: String = String.EMPTY
    private var bundleWidgetType: String = String.EMPTY

    init {
        viewBinding?.apply {
            productBundleWidgetView = productBundleWidget
        }
    }

    override fun bind(element: ShopHomeProductBundleListUiModel) {
        bundleListUiModel = element
        shopId = element.productBundleList.firstOrNull()?.shopId.orEmpty()
        warehouseId = element.productBundleList.firstOrNull()?.warehouseId.orEmpty()
        bundleWidgetTitle = bundleListUiModel?.header?.title.orEmpty()
        bundleWidgetName = bundleListUiModel?.name.orEmpty()
        bundleWidgetType = bundleListUiModel?.type.orEmpty()
        // retrieve bundle ids from ui model
        val bundleIds = bundleListUiModel?.productBundleList?.map {
            it.bundleGroupId
        } ?: listOf()
        // build param and render product bundle widget
        val param = GetBundleParamBuilder()
            .setBundleId(bundleIds)
            .setWidgetType(WidgetType.TYPE_1)
            .setPageSource(ShopPageConstant.SOURCE)
            .build()
        productBundleWidgetView?.setListener(this)
        productBundleWidgetView?.setTitleText(bundleWidgetTitle)
        productBundleWidgetView?.getBundleData(param)
        productBundleWidgetView?.setTitleAndWidgetMargin(BUNDLE_RV_MARGIN_TOP.dpToPx().toInt())
        checkFestivity(element)
    }

    private fun checkFestivity(element: ShopHomeProductBundleListUiModel) {
        if (element.isFestivity) {
            configFestivity()
        } else {
            configNonFestivity()
        }
    }

    private fun configFestivity() {
        val festivityTextColorRes = com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        productBundleWidgetView?.setTitleTextColor(festivityTextColorRes)
    }

    private fun configNonFestivity() {
        val defaultTitleColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN950
        productBundleWidgetView?.setTitleTextColor(defaultTitleColorRes)
    }

    override fun onBundleProductClicked(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        itemPosition: Int
    ) {
        val selectedShopHomeBundleUiModel = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(
            selectedMultipleBundle,
            bundleListUiModel
        )
        val selectedShopHomeProductUiModel = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        when (bundle.bundleType) {
            BundleTypes.MULTIPLE_BUNDLE -> {
                handleMultipleBundleClickEvent(
                    bundleUiModel = bundle,
                    selectedBundle = selectedMultipleBundle,
                    selectedProduct = selectedProduct,
                    selectedShopHomeBundleUiModel = selectedShopHomeBundleUiModel,
                    selectedShopHomeProductUiModel = selectedShopHomeProductUiModel
                )
            }
            BundleTypes.SINGLE_BUNDLE -> {
                handleSingleBundleClickEvent(
                    bundleUiModel = bundle,
                    selectedBundle = selectedMultipleBundle,
                    selectedProduct = selectedProduct,
                    selectedShopHomeBundleUiModel = selectedShopHomeBundleUiModel,
                    selectedShopHomeProductUiModel = selectedShopHomeProductUiModel
                )
            }
        }
    }

    override fun impressionMultipleBundle(
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        val selectedBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(selectedMultipleBundle, bundleListUiModel)
        multipleProductBundleListener.impressionProductBundleMultiple(
            shopId = shopId,
            warehouseId = warehouseId,
            selectedMultipleBundle = selectedBundle,
            bundleName = bundle.bundleName,
            bundleType = bundle.bundleType.toString(),
            bundlePosition = adapterPosition
        )
    }

    override fun impressionMultipleBundleProduct(
        bundle: BundleUiModel,
        selectedProduct: BundleProductUiModel,
        selectedBundle: BundleDetailUiModel
    ) {
        val selectedBundleProduct = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        val selectedMultipleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(
            selectedBundle,
            bundleListUiModel
        )
        multipleProductBundleListener.impressionProductItemBundleMultiple(
            selectedProduct = selectedBundleProduct,
            selectedMultipleBundle = selectedMultipleBundle,
            bundleName = bundle.bundleName,
            bundlePosition = adapterPosition,
            widgetTitle = bundleWidgetTitle,
            widgetName = bundleWidgetName,
            productItemPosition = selectedBundle.products.indexOf(selectedProduct)
        )
    }

    override fun onSingleBundleChipsSelected(
        selectedProduct: BundleProductUiModel,
        selectedBundle: BundleDetailUiModel,
        bundleName: String
    ) {
        val selectedProductVariant = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        val selectedSingleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(
            selectedBundle,
            bundleListUiModel
        )
        singleProductBundleListener.onTrackSingleVariantChange(
            selectedProduct = selectedProductVariant,
            selectedSingleBundle = selectedSingleBundle,
            bundleName = bundleName
        )
    }

    override fun impressionSingleBundle(
        selectedBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        bundleName: String,
        bundlePosition: Int
    ) {
        val selectedSingleBundle = ShopPageHomeMapper.mapToShopHomeProductBundleDetailUiModel(
            selectedBundle,
            bundleListUiModel
        )
        val selectedBundleProduct = ShopPageHomeMapper.mapToShopHomeBundleProductUiModel(selectedProduct)
        singleProductBundleListener.impressionProductBundleSingle(
            shopId = shopId,
            warehouseId = warehouseId,
            selectedSingleBundle = selectedSingleBundle,
            selectedProduct = selectedBundleProduct,
            bundleName = bundleName,
            bundlePosition = adapterPosition,
            widgetTitle = bundleWidgetTitle,
            widgetName = bundleWidgetName,
            bundleType = getBundleType(bundleListUiModel, selectedBundle.bundleId)
        )
    }

    private fun handleMultipleBundleClickEvent(
        bundleUiModel: BundleUiModel,
        selectedBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        selectedShopHomeBundleUiModel: ShopHomeProductBundleDetailUiModel,
        selectedShopHomeProductUiModel: ShopHomeBundleProductUiModel
    ) {
        multipleProductBundleListener.onMultipleBundleProductClicked(
            shopId = shopId,
            warehouseId = warehouseId,
            selectedProduct = selectedShopHomeProductUiModel,
            selectedMultipleBundle = selectedShopHomeBundleUiModel,
            bundleName = bundleUiModel.bundleName,
            bundleType = bundleUiModel.bundleType.toString(),
            bundlePosition = adapterPosition,
            widgetTitle = bundleWidgetTitle,
            widgetName = bundleWidgetName,
            productItemPosition = selectedBundle.products.indexOf(selectedProduct)
        )
    }

    private fun handleSingleBundleClickEvent(
        bundleUiModel: BundleUiModel,
        selectedBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        selectedShopHomeBundleUiModel: ShopHomeProductBundleDetailUiModel,
        selectedShopHomeProductUiModel: ShopHomeBundleProductUiModel
    ) {
        singleProductBundleListener.onSingleBundleProductClicked(
            shopId = shopId,
            warehouseId = warehouseId,
            selectedProduct = selectedShopHomeProductUiModel,
            selectedSingleBundle = selectedShopHomeBundleUiModel,
            bundleName = bundleUiModel.bundleName,
            bundlePosition = selectedBundle.products.indexOf(selectedProduct),
            widgetTitle = bundleWidgetTitle,
            widgetName = bundleWidgetName,
            productItemPosition = adapterPosition,
            bundleType = bundleUiModel.bundleType.toString()
        )
    }

    private fun getBundleType(
        bundleListUiModel: ShopHomeProductBundleListUiModel?,
        bundleId: String
    ): String {
        return bundleListUiModel?.productBundleList?.firstOrNull {
            it.bundleGroupId == bundleId
        }?.bundleType.orEmpty()
    }
}
