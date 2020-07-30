package com.tokopedia.checkers

import com.tokopedia.abstraction.processor.beta.ProductListImpressionProduct
import timber.log.Timber
import java.lang.Exception
import java.util.regex.Pattern

private val getKlikPattern = Pattern.compile("(?=[K|k]).?(?=[L|l]).?(?=[I|i]).?(?=[K|k]).?")
private val getClickPattern = Pattern.compile("(?=[C|c]).?(?=[L|l]).?(?=[I|i]).?(?=[C|c]).?(?=[K|k]).?")

object ProductListImpressionChecker {
    /**
     * @value shouldn't contain "click" or "klik" or empty string
     */
    fun isContainInvalidTerm(eventAction: String): Boolean {

        val trim_value = eventAction.trim()
        try {
            return !(trim_value.isEmpty() ||
                    getKlikPattern.matcher(trim_value).find() ||
                    getClickPattern.matcher(trim_value).find())
        }catch (e : Exception){
            Timber.w("P2#CHECKER_CLICK_CHECK#event Action ${eventAction} get exception ${e.toString()}")
            return true;
        }
    }

    fun isSizeBiggerThanOne(items: List<ProductListImpressionProduct>) : Boolean{
        try {
            return items.size >= 1
        }catch (e : Exception){
            Timber.w("P2#CHECKER_IS_BIGGER#get exception ${e.toString()}")
            return true;
        }

    }

    fun isContainValidTerm(event: String): Boolean {
        try {
            return event.contains("view_item_list") || event.contains("view_search_results")
        }catch (e :Exception){
            Timber.w("P2#CHECKER_EVENT#event ${event} get exception ${e.toString()}")
            return true;
        }
    }
}