package com.tokopedia.product.detail.common.view

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.product.detail.common.R

class ProductDetailCoachMarkHelper(context: Context) {

    private var coachMarkView: CoachMark2? = null

    private val sharedPref: SharedPreferences? by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private val editor = sharedPref?.edit()

    fun showCoachMarkHampers(targetView: View?) {
        val shouldShowCoachMark = getCoachMarkState(PDP_HAMPERS_COACHMARK_EXTRA) == false

        if (targetView != null && shouldShowCoachMark) {
            initCoachMarkView(targetView.context)

            val coachMarkList = arrayListOf<CoachMark2Item>()

            coachMarkList.add(CoachMark2Item(targetView,
                    targetView.context.getString(R.string.pdp_hampers_coachmark_title),
                    targetView.context.getString(R.string.pdp_hampers_coachmark_desc),
                    CoachMark2.POSITION_TOP))

            coachMarkView?.showCoachMark(coachMarkList, null, 0)

            setCoachMarkState(PDP_HAMPERS_COACHMARK_EXTRA, true)
        }
    }

    private fun setCoachMarkState(key: String, state: Boolean) {
        editor?.putBoolean(key, state)
        editor?.apply()
    }

    private fun getCoachMarkState(key: String): Boolean? {
        return sharedPref?.getBoolean(key, false)
    }

    private fun initCoachMarkView(context: Context) {
        if (coachMarkView == null) {
            coachMarkView = CoachMark2(context)
        }
    }

    companion object {
        private const val PREF_NAME = "pdp_coachmark"
        private const val PDP_HAMPERS_COACHMARK_EXTRA = "pdp_hampers_coachmark"
    }
}