package com.tokopedia.product.addedit.productlimitation.domain.mapper

import android.content.Context
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductLimitationData
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel

class ProductLimitationMapper {

    fun createUpgradeToPmActionItem(context: Context) = ProductLimitationModel(
            IMAGE_URL_UPGRADE_TO_PM,
            context.getString(R.string.title_product_limitation_item_upgradepm),
            context.getString(R.string.label_product_limitation_item_upgradepm),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_UPGRADE_TO_PM
    )

    fun createUseVariantActionItem(context: Context) = ProductLimitationModel(
            IMAGE_URL_USE_VARIANT,
            context.getString(R.string.title_product_limitation_item_usevariant),
            context.getString(R.string.label_product_limitation_item_usevariant),
            ACTION_URL_USE_VARIANT
    )

    fun createDeleteProductActionItem(context: Context) = ProductLimitationModel(
            IMAGE_URL_DELETE_PRODUCTS,
            context.getString(R.string.title_product_limitation_item_delete),
            context.getString(R.string.label_product_limitation_item_delete),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_DELETE_PRODUCTS
    )

    fun createUsePromotionActionItem(context: Context) = ProductLimitationModel(
            IMAGE_URL_USE_PROMOTION,
            context.getString(R.string.title_product_limitation_item_usepromotion),
            context.getString(R.string.label_product_limitation_item_usepromotion),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_USE_PROMOTION
    )

    companion object {
        private const val UPGRADE_TO_PM = "UpToPM"
        private const val USE_VARIANT = "Variant"
        private const val DELETE_PRODUCTS = "Delete"
        private const val USE_PROMOTION = "Promotion"

        private const val IMAGE_URL_UPGRADE_TO_PM = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_pm.png"
        private const val IMAGE_URL_USE_VARIANT = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_variant.png"
        private const val IMAGE_URL_DELETE_PRODUCTS = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_deleting.png"
        private const val IMAGE_URL_USE_PROMOTION = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_ads.png"

        private const val ACTION_URL_UPGRADE_TO_PM = "https://seller.tokopedia.com/settings/power-merchant"
        private const val ACTION_URL_USE_VARIANT = "https://seller.tokopedia.com/edu/fitur-varian"
        private const val ACTION_URL_DELETE_PRODUCTS = "https://seller.tokopedia.com/manage-product"
        private const val ACTION_URL_USE_PROMOTION = "https://seller.tokopedia.com/promo"

        fun mapToActionItems(context: Context, productLimitationData: ProductLimitationData) =
                productLimitationData.eligible?.actionItems?.map {
                    when(it) {
                        UPGRADE_TO_PM -> {
                            ProductLimitationMapper().createUpgradeToPmActionItem(context)
                        }
                        USE_VARIANT -> {
                            ProductLimitationMapper().createUseVariantActionItem(context)
                        }
                        DELETE_PRODUCTS -> {
                            ProductLimitationMapper().createDeleteProductActionItem(context)
                        }
                        USE_PROMOTION -> {
                            ProductLimitationMapper().createUsePromotionActionItem(context)
                        }
                        else -> ProductLimitationModel()
                    }
                }.orEmpty()
    }
}