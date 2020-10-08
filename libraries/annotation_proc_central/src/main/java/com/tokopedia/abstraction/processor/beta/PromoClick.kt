package com.tokopedia.abstraction.processor.beta


import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.checkers.ProductListImpressionProductChecker
import com.tokopedia.firebase.analytic.rules.AddToCartRules
import com.tokopedia.firebase.analytic.rules.PromoClickRules
import com.tokopedia.util.GTMErrorHandlerImpl
import com.tokopedia.util.logger.GTMLoggerImpl
import timber.log.Timber

/**
 * Product Detail
 */
@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@AnalyticEvent(true, Event.VIEW_ITEM, PromoClickRules::class)
data class PromoClick(
        @CustomChecker(PromoClickChecker::class, Level.IGNORE, functionName = ["isOnlyOneProduct"])
        val promotions: List<PromoClickProduct>,
        @CustomChecker(PromoClickChecker::class, Level.IGNORE, functionName = ["isEventValid"])
        @DefaultValueString("")
        val event: String?,
        @CustomChecker(PromoClickChecker::class, Level.IGNORE, functionName = ["isEventActionValid"])
        @DefaultValueString("")
        val eventAction: String?,
        val currentSite: String?,
        @DefaultValueString("")
        val eventCategory: String?,
        @DefaultValueString("")
        val businessUnit: String?,
        @DefaultValueString("")
        val screenName: String?,
        @CustomChecker(PromoClickChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: HashMap<String, String>

)


private const val KEY_DIMENSION_40 = "dimension40"


@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@BundleThis(false, true)
data class PromoClickProduct(
        @Key(Param.ITEM_ID)
        val id: String,
        @Key(Param.ITEM_NAME)
        val name: String,
        @Key(Param.CREATIVE_NAME)
        val creative_name: String,
        @Key(Param.CREATIVE_SLOT)
        val creative_slot: String,
        @CustomChecker(PromoClickChecker::class, Level.ERROR, functionName = ["checkMap"])
        @DefinedInCollections
        val stringCollection: HashMap<String, String>
)

object PromoClickChecker {
    /**
     * @value should exactly match "ecommerce_purchase"
     */
    fun isEventValid(eventAction: String): Boolean {
        try {
            return eventAction.equals(Event.ECOMMERCE_PURCHASE)
        } catch (e: Exception) {
            Timber.w("P2#CHECKER_CLICK_CHECK#event Action ${eventAction} get exception ${e.toString()}")
            return true;
        }
    }

    /**
     * @value should exactly match "ecommerce_purchase"
     */
    fun isNotZero(eventAction: Double): Boolean {
        try {
            return eventAction > 0
        } catch (e: Exception) {
            Timber.w("P2#CHECKER_CLICK_CHECK#event Action ${eventAction} get exception ${e.toString()}")
            return true;
        }
    }

    fun checkMap(map: Map<String, String>) = map.isNotEmpty()

    fun isOnlyOneProduct(products: List<PromoClickProduct>): Boolean {
        try {
            return products.size == 1
        } catch (e: Exception) {
            Timber.w("P2#CHECKER_CLICK_CHECK#event Action ${products} get exception ${e.toString()}")
            return true;
        }
    }

    fun isCheckoutStepValid(products: Long): Boolean {
        try {
            return products == 1L
        } catch (e: Exception) {
            Timber.w("P2#CHECKER_CLICK_CHECK#event Action ${products} get exception ${e.toString()}")
            return true;
        }
    }

    fun isEventActionValid(eventAction: String): Boolean {
        try {
            return !(eventAction.contains("impression")||eventAction.contains("view"))
        } catch (e: Exception) {
            Timber.w("P2#CHECKER_CLICK_CHECK#event Action ${eventAction} get exception ${e.toString()}")
            return true;
        }
    }


}
