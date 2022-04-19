package com.tokopedia.abstraction.processor.beta


import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.checkers.ProductListImpressionProductChecker
import com.tokopedia.firebase.analytic.rules.CheckoutFirstRules
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.util.GTMErrorHandlerImpl
import com.tokopedia.util.logger.GTMLoggerImpl

/**
 * Product Detail
 */
@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@AnalyticEvent(true, Event.BEGIN_CHECKOUT, CheckoutFirstRules::class)
data class CheckoutFirst(
        @CustomChecker(CheckoutFirstChecker::class, Level.IGNORE, functionName = ["isOnlyOneProduct"])
        val items: List<CheckoutFirstProduct>,
        @CustomChecker(CheckoutFirstChecker::class, Level.IGNORE, functionName = ["isEventValid"])
        @DefaultValueString("")
        val event: String?,
        @DefaultValueString("")
        val eventAction: String?,

        @DefaultValueString("")
        val checkout_option: String?,
        @CustomChecker(CheckoutFirstChecker::class, Level.IGNORE, functionName = ["isCheckoutStepValid"])
        @DefaultValueLong(1L)
        val checkout_step: Long?,
        @DefaultValueString("")
        val currentSite: String?,
        @DefaultValueString("")
        val eventCategory: String?,
        @DefaultValueString("")
        val businessUnit: String?,
        @DefaultValueString("")
        val screenName: String?,
        @CustomChecker(CheckoutFirstChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: HashMap<String, String>

)


private const val KEY_DIMENSION_40 = "dimension40"


@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@BundleThis(false, true)
data class CheckoutFirstProduct(
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

        @DefaultValueLong(1)
        @Key(Param.QUANTITY)
        val index: Long,
        @CustomChecker(CheckoutFirstChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: HashMap<String, String>
)

object CheckoutFirstChecker {
    /**
     * @value should exactly match "ecommerce_purchase"
     */
    fun isEventValid(eventAction: String): Boolean {
        try {
            return eventAction.equals(Event.ECOMMERCE_PURCHASE)
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CHECKER_CLICK_CHECK", mapOf("type" to "event Action $eventAction get exception $e"))
            return true
        }
    }

    /**
     * @value should exactly match "ecommerce_purchase"
     */
    fun isNotZero(eventAction: Double): Boolean {
        try {
            return eventAction > 0
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CHECKER_CLICK_CHECK", mapOf("type" to "event Action $eventAction get exception $e"))
            return true
        }
    }

    fun checkMap(map: Map<String, String>) = map.isNotEmpty()

    fun isOnlyOneProduct(products: List<CheckoutFirstProduct>): Boolean {
        try {
            return products.size > 0
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CHECKER_CLICK_CHECK", mapOf("type" to "event Action $products get exception $e"))
            return true
        }
    }

    fun isCheckoutStepValid(products: Long): Boolean {
        try {
            return products == 1L
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CHECKER_CLICK_CHECK", mapOf("type" to "event Action $products get exception $e"))
            return true
        }
    }


}
