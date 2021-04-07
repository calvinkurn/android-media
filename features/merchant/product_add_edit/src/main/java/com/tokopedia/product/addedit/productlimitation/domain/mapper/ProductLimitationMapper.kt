package com.tokopedia.product.addedit.productlimitation.domain.mapper

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductLimitationData
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationActionItemModel
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel

class ProductLimitationMapper {

    fun createUpgradeToPmActionItem(context: Context) = ProductLimitationActionItemModel(
            IMAGE_URL_UPGRADE_TO_PM,
            context.getString(R.string.title_product_limitation_item_upgradepm),
            context.getString(R.string.label_product_limitation_item_upgradepm),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_UPGRADE_TO_PM
    )

    fun createUseVariantActionItem(context: Context) = ProductLimitationActionItemModel(
            IMAGE_URL_USE_VARIANT,
            context.getString(R.string.title_product_limitation_item_usevariant),
            context.getString(R.string.label_product_limitation_item_usevariant),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_USE_VARIANT
    )

    fun createDeleteProductActionItem(context: Context) = ProductLimitationActionItemModel(
            IMAGE_URL_DELETE_PRODUCTS,
            context.getString(R.string.title_product_limitation_item_delete),
            context.getString(R.string.label_product_limitation_item_delete),
            context.getString(R.string.action_product_limitation_item_goto_productlist),
            ACTION_URL_DELETE_PRODUCTS
    )

    fun createUsePromotionActionItem(context: Context) = ProductLimitationActionItemModel(
            IMAGE_URL_USE_PROMOTION,
            context.getString(R.string.title_product_limitation_item_usepromotion),
            context.getString(R.string.label_product_limitation_item_usepromotion),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_USE_PROMOTION
    )

    fun mapToActionItems(context: Context, productLimitationData: ProductLimitationData) =
            productLimitationData.eligible?.actionItems?.map {
                when(it) {
                    UPGRADE_TO_PM -> {
                        createUpgradeToPmActionItem(context)
                    }
                    USE_VARIANT -> {
                        createUseVariantActionItem(context)
                    }
                    DELETE_PRODUCTS -> {
                        createDeleteProductActionItem(context)
                    }
                    USE_PROMOTION -> {
                        createUsePromotionActionItem(context)
                    }
                    else -> ProductLimitationActionItemModel()
                }
            }.orEmpty()

    companion object {
        private const val UPGRADE_TO_PM = "UpToPM"
        private const val USE_VARIANT = "Variant"
        private const val DELETE_PRODUCTS = "Delete"
        private const val USE_PROMOTION = "Promotion"

        private const val IMAGE_URL_UPGRADE_TO_PM = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_pm.png"
        private const val IMAGE_URL_USE_VARIANT = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_variant.png"
        private const val IMAGE_URL_DELETE_PRODUCTS = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_deleting.png"
        private const val IMAGE_URL_USE_PROMOTION = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_ads.png"

        private val ACTION_URL_UPGRADE_TO_PM = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        private const val ACTION_URL_USE_VARIANT = "https://seller.tokopedia.com/edu/fitur-varian"
        private val ACTION_URL_DELETE_PRODUCTS = ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
        private val ACTION_URL_USE_PROMOTION = ApplinkConstInternalSellerapp.CENTRALIZED_PROMO

        fun mapToProductLimitationModel(context: Context, productLimitationData: ProductLimitationData) =
                ProductLimitationModel(
                        productLimitationData.eligible?.value.orFalse(),
                        productLimitationData.eligible?.limit.orZero(),
                        ProductLimitationMapper().mapToActionItems(context, productLimitationData))
    }
}