package com.tokopedia.abstraction.processor.beta


import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.defaultvalues.DefaultValueDouble
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.checkers.ProductListImpressionProductChecker
import com.tokopedia.firebase.analytic.rules.TransactionRules
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.util.GTMErrorHandlerImpl
import com.tokopedia.util.logger.GTMLoggerImpl
import timber.log.Timber

/**
 * Product Detail
 */
@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@AnalyticEvent(true, Event.ECOMMERCE_PURCHASE, TransactionRules::class)
data class Transaction(
        val items: List<TransactionProduct>,
        @DefaultValueString("")
        val eventAction: String?,
        @CustomChecker(TransactionChecker::class, Level.IGNORE, functionName = ["isEventValid"])
        @DefaultValueString("")
        val event: String?,
        @DefaultValueString("")
        val transaction_id: String?,
        @DefaultValueString("")
        val affiliation: String?,
        @CustomChecker(TransactionChecker::class, Level.IGNORE, functionName = ["isNotZero"])
        @DefaultValueDouble(1.0)
        val value: Double,
        @DefaultValueDouble(1.0)
        val tax: Double,
        @DefaultValueString("")
        val shipping: String?,
        @DefaultValueString("")
        val currency: String?,
        @DefaultValueString("")
        val coupon: String?,
        @DefaultValueString("")
        val paymentId: String?,
        @DefaultValueString("")
        val currentSite: String?,
        @DefaultValueString("")
        val eventCategory: String?,
        @DefaultValueString("")
        val businessUnit: String?,
        @DefaultValueString("")
        val screenName: String?,
        @CustomChecker(TransactionChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: HashMap<String, String>

)


private const val KEY_DIMENSION_40 = "dimension40"


@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@BundleThis(false, true)
data class TransactionProduct(
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
        val quantity: Long,
        @CustomChecker(TransactionChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: HashMap<String, String>
)

object TransactionChecker {
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


}
