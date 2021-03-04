package com.tokopedia.abstraction.processor.beta


import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.checkers.ProductListImpressionChecker
import com.tokopedia.checkers.ProductListImpressionProductChecker
import com.tokopedia.firebase.analytic.rules.ProductListImpressionsRules
import com.tokopedia.util.GTMErrorHandlerImpl
import com.tokopedia.util.logger.GTMLoggerImpl

/**
 * Product Detail
 */
@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@AnalyticEvent(true, Event.VIEW_ITEM_LIST, ProductListImpressionsRules::class)
data class ProductListImpression(
        val item_list: String,
        @CustomChecker(ProductListImpressionChecker::class, Level.IGNORE, functionName = ["isSizeBiggerThanOne"])
        val items: List<ProductListImpressionProduct>,
        @DefaultValueString("")
        val currentSite: String?,
        @CustomChecker(ProductListImpressionChecker::class, Level.IGNORE, functionName = ["isContainValidTerm"])
        @DefaultValueString("")
        val event: String?,
        @DefaultValueString("")
        val eventCategory: String?,
        @CustomChecker(ProductListImpressionChecker::class, Level.IGNORE, functionName = ["isContainInvalidTerm"])
        @DefaultValueString("")
        val eventAction: String?,
        @DefaultValueString("")
        val businessUnit: String?,
        @DefaultValueString("")
        val screenName: String?,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: Map<String, String>

)


private const val KEY_DIMENSION_40 = "dimension40"


@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@BundleThis(false, true)
data class ProductListImpressionProduct(
        @Key(Param.ITEM_ID)
        val id: String,
        @Key(Param.ITEM_NAME)
        val name: String,
        @DefaultValueString("none")
        @Key(Param.ITEM_BRAND)
        val brand: String?,
        @Key(Param.ITEM_CATEGORY)
        val category: String,
        @Key(Param.ITEM_VARIANT)
        val variant: String,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.IGNORE, functionName = ["isPriceNotZero"])
        @Key(Param.PRICE)
        val price: Double,
        @DefaultValueString("IDR")
        @Key(Param.CURRENCY)
        val currency: String?,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.IGNORE, functionName = ["isIndexNotZero"])
        @DefaultValueLong(1)
        @Key(Param.INDEX)
        val index: Long,
        @Key("list")
        val list: String,
        @DefaultValueString("none / other")
        @Key(KEY_DIMENSION_40)
        val dimension40: String?,
        @DefaultValueString("")
        @Key("dimension87")
        val dimension87: String?,
        @DefaultValueString("")
        @Key("dimension88")
        val dimension88: String?,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: Map<String, String>
)
