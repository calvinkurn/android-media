package com.tokopedia.product.addedit.productlimitation.domain.mapper

import android.content.Context
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.ACTION_URL_DELETE_PRODUCTS
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.ACTION_URL_UPGRADE_TO_PM
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.ACTION_URL_USE_PROMOTION
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.ACTION_URL_USE_PROMOTION_WEB
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.ACTION_URL_USE_VARIANT
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.IMAGE_URL_DELETE_PRODUCTS
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.IMAGE_URL_UPGRADE_TO_PM
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.IMAGE_URL_UPGRADE_TO_PM_PRO
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.IMAGE_URL_USE_PROMOTION
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants.Companion.IMAGE_URL_USE_VARIANT
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductLimitationData
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationActionItemModel
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel

class ProductLimitationMapper {

    fun createUpgradeToPmProActionItem(context: Context, itemTitle: String) = ProductLimitationActionItemModel(
            IMAGE_URL_UPGRADE_TO_PM_PRO,
            context.getString(R.string.title_product_limitation_item_upgradepmpro),
            context.getString(R.string.label_product_limitation_item_upgradepmpro),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_UPGRADE_TO_PM,
            itemTitle
    )

    fun createUpgradeToPmActionItem(context: Context, itemTitle: String) = ProductLimitationActionItemModel(
            IMAGE_URL_UPGRADE_TO_PM,
            context.getString(R.string.title_product_limitation_item_upgradepm),
            context.getString(R.string.label_product_limitation_item_upgradepm),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_UPGRADE_TO_PM,
            itemTitle
    )

    fun createUseVariantActionItem(context: Context, itemTitle: String) = ProductLimitationActionItemModel(
            IMAGE_URL_USE_VARIANT,
            context.getString(R.string.title_product_limitation_item_usevariant),
            context.getString(R.string.label_product_limitation_item_usevariant),
            context.getString(R.string.action_product_limitation_item),
            ACTION_URL_USE_VARIANT,
            itemTitle
    )

    fun createDeleteProductActionItem(context: Context, itemTitle: String) = ProductLimitationActionItemModel(
            IMAGE_URL_DELETE_PRODUCTS,
            context.getString(R.string.title_product_limitation_item_delete),
            context.getString(R.string.label_product_limitation_item_delete),
            context.getString(R.string.action_product_limitation_item_goto_productlist),
            ACTION_URL_DELETE_PRODUCTS,
            itemTitle
    )

    fun createUsePromotionActionItem(context: Context, itemTitle: String) = ProductLimitationActionItemModel(
            IMAGE_URL_USE_PROMOTION,
            context.getString(R.string.title_product_limitation_item_usepromotion),
            context.getString(R.string.label_product_limitation_item_usepromotion),
            context.getString(R.string.action_product_limitation_item),
            if(GlobalConfig.isSellerApp()) ACTION_URL_USE_PROMOTION else ACTION_URL_USE_PROMOTION_WEB,
            itemTitle
    )

    fun mapToActionItems(context: Context, productLimitationData: ProductLimitationData) =
            productLimitationData.eligible?.actionItems?.map {
                when(it) {
                    UPGRADE_TO_PM -> {
                        createUpgradeToPmActionItem(context, it)
                    }
                    UPGRADE_TO_PM_PRO -> {
                        createUpgradeToPmProActionItem(context, it)
                    }
                    USE_VARIANT -> {
                        createUseVariantActionItem(context, it)
                    }
                    DELETE_PRODUCTS -> {
                        createDeleteProductActionItem(context, it)
                    }
                    USE_PROMOTION -> {
                        createUsePromotionActionItem(context, it)
                    }
                    else -> ProductLimitationActionItemModel()
                }
            }.orEmpty()

    companion object {
        const val UPGRADE_TO_PM = "UpToPM"
        const val UPGRADE_TO_PM_PRO = "UpToPMPro"
        const val USE_VARIANT = "Variant"
        const val DELETE_PRODUCTS = "Delete"
        const val USE_PROMOTION = "Promotion"

        fun mapToProductLimitationModel(context: Context, productLimitationData: ProductLimitationData) =
                ProductLimitationModel(
                        productLimitationData.eligible?.value.orFalse(),
                        productLimitationData.eligible?.isUnlimited.orFalse(),
                        productLimitationData.eligible?.limit.orZero(),
                        ProductLimitationMapper().mapToActionItems(context, productLimitationData))
    }
}