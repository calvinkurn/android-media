package com.tokopedia.topads.auto.view.fragment.budget

import android.content.Context

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.base.AutoAdsViewModel
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoy

/**
 * Author errysuprayogi on 09,May,2019
 */
class DailyBudgetViewModel(
        private val context: Context,
        private val repository: AutoTopAdsRepositoy
) : AutoAdsViewModel(repository){

    fun getPotentialImpression(minBid: Double, maxBid: Double, bid: Double): String {
        return String.format("%,.0f - %,.0f", calculateImpression(maxBid, bid),
                calculateImpression(minBid, bid))
    }

    private fun calculateImpression(bid: Double, `val`: Double): Double {
        return 100 / 2.5 * (`val` / bid)
    }

    fun checkBudget(number: Double, minDailyBudget: Double, maxDailyBudget: Double): String? {
        return if (number <= 0) {
            context.getString(R.string.error_empty_budget)
        } else if (number < minDailyBudget) {
            String.format(context.getString(R.string.error_minimum_budget), minDailyBudget)
        } else if (number > maxDailyBudget) {
            String.format(context.getString(R.string.error_maximum_budget), maxDailyBudget)
        } else if (number < maxDailyBudget && number > minDailyBudget && number % BUDGET_MULTIPLE_BY != 0.0) {
            context.getString(R.string.error_multiply_budget, BUDGET_MULTIPLE_BY.toString())
        } else {
            null
        }
    }

    companion object {
        val BUDGET_MULTIPLE_BY = 1000.0
    }
}
