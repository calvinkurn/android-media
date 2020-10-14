package com.tokopedia.product.util.processor


import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.checkers.ProductListImpressionProductChecker
import com.tokopedia.util.GTMErrorHandlerImpl
import com.tokopedia.util.logger.GTMLoggerImpl

private const val KEY_DIMENSION_40 = "dimension40"


object ProductTrackingConstant {

    object Category {
        const val PDP = "product detail page"
    }

    object Tracking {

        const val ID = "id"
        const val NAME = "name"
        const val DEFAULT_VALUE = "none / other"
        const val KEY_PRODUCT_ID = "productId"
        const val KEY_LAYOUT = "layout"
        const val KEY_COMPONENT = "component"

        const val KEY_DIMENSION_81 = "dimension81"
        const val KEY_DIMENSION_83 = "dimension83"
        const val KEY_DIMENSION_54 = "dimension54"
        const val KEY_DIMENSION_55 = "dimension55"
        const val KEY_DIMENSION_38 = "dimension38"
        const val KEY_DIMENSION_98 = "dimension98"
    }


    object Label {
        const val PDP = "pdp"
    }

    object MerchantVoucher {
        const val ACTION = "promo banner"
    }

    object Message {
        const val LABEL = "Message Shop"
    }
}

@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@BundleThis(false, true)
data class Product(
        @Key(Param.ITEM_NAME)
        val name: String,
        @Key(Param.ITEM_ID)
        val id: String,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.ERROR, functionName = ["isPriceNotZero"])
        @Key(Param.PRICE)
        val price: Double,
        @DefaultValueString("none")
        @Key(Param.ITEM_BRAND)
        val brand: String?,
        @Key(Param.ITEM_VARIANT)
        val variant: String,
        @Key(Param.ITEM_CATEGORY)
        val category: String,
        @DefaultValueString("IDR")
        @Key(Param.CURRENCY)
        val currency: String?,
        @DefaultValueString(ProductTrackingConstant.Tracking.DEFAULT_VALUE)
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_38)
        val dimension38: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_55)
        val dimension55: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_54)
        val dimension54: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_83)
        val dimension83: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_81)
        val dimension81: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_98)
        val dimension98: String,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.ERROR, functionName = ["isIndexNotZero"])
        @DefaultValueLong(1)
        @Key(Param.INDEX)
        val index: Long
)