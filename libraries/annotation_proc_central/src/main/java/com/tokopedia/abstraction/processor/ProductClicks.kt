package com.tokopedia.abstraction.processor


import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.checkers.ProductListClickChecker
import com.tokopedia.checkers.ProductListImpressionProductChecker
import com.tokopedia.firebase.analytic.rules.ProductListClicksRules
import com.tokopedia.util.GTMErrorHandlerImpl
import com.tokopedia.util.logger.GTMLoggerImpl

/**
 * Product List Click
 */
@Deprecated("use in beta package", replaceWith = ReplaceWith("use in beta package", imports = ["com.tokopedia.abstraction.processor.beta.ProductListClick"]))
@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@AnalyticEvent(true, Event.SELECT_CONTENT, ProductListClicksRules::class)
data class ProductListClick(
        @DefaultValueString("Search Results")
        val item_list: String,
        @CustomChecker(ProductListClickChecker::class, Level.ERROR, functionName = ["isOnlyOneProduct"])
        val items: ArrayList<ProductListClickProduct>,
        @DefaultValueString("")
        val eventCategory: String?,
        @CustomChecker(ProductListClickChecker::class, Level.ERROR, functionName = ["notContainWords"])
        @DefaultValueString("")
        val eventAction: String?,
        @CustomChecker(ProductListClickChecker::class, Level.ERROR, functionName = ["onlySelectContent"])
        @DefaultValueString("")
        val event: String?,
        @DefaultValueString("")
        val currentSite: String?,
        @DefaultValueString("")
        val businessUnit: String?,
        @DefaultValueString("")
        val screenName: String?
)


private const val KEY_DIMENSION_40 = "dimension40"

@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@BundleThis(false, true)
data class ProductListClickProduct(
        @Key(Param.ITEM_ID)
        val id: String,
        @Key(Param.ITEM_NAME)
        val name: String,
        @Key(Param.ITEM_CATEGORY)
        val category: String,
        @Key(Param.ITEM_VARIANT)
        val variant: String,
        @DefaultValueString("none")
        @Key(Param.ITEM_BRAND)
        val brand: String?,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.ERROR, functionName = ["isPriceNotZero", "isPriceNotZero", "isPriceNotZero", "isPriceNotZero"])
        @Key(Param.PRICE)
        val price: Double,
        @DefaultValueString("IDR")
        @Key(Param.CURRENCY)
        val currency: String?,
        @Key(KEY_DIMENSION_40)
        val keyDimension40: String,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.ERROR, functionName = ["isIndexNotZero"])
        @DefaultValueLong(0)
        @Key(Param.INDEX)
        val index: Long,
        @CustomChecker(ProductListImpressionProductChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: Map<String, String>
)
