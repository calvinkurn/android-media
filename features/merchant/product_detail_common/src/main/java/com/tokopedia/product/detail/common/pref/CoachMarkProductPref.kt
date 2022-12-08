package com.tokopedia.product.detail.common.pref

import android.content.Context
import android.content.SharedPreferences

class CoachMarkProductPref(context: Context, pref_name: String) {

    private val EXTRA_IS_COACHMARK = "EXTRA_IS_COACHMARK"

    private val sharedPref: SharedPreferences? by lazy {
        context.getSharedPreferences(pref_name, Context.MODE_PRIVATE)
    }

    private val editor = sharedPref?.edit()

    fun setCoachMarkState(state: Boolean) {
        editor?.putBoolean(EXTRA_IS_COACHMARK, state)
        editor?.apply()
    }

    fun getCoachMarkState(): Boolean? {
        return sharedPref?.getBoolean(EXTRA_IS_COACHMARK, false)
    }

    companion object {
        const val PRODUCT_AR_PAGE_COACHMARK = "coach_mark_ar_page"
        const val PRODUCT_DETAIL_AR_PAGE_COACHMARK = "coach_mark_pdp_ar_page"

    }
}