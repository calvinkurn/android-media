package com.tokopedia.product.detail.common.view

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.product.detail.common.R

/**
 * CoachMark Hierarchy
 * PDP can only shows 1 coach mark in 1 page
 */
class ProductDetailCoachMarkHelper(context: Context) {

    private var coachMarkView: CoachMark2? = null
    private var coachMarkEverShowing = false
    private var lastShown = ""
    private val sharedPref: SharedPreferences? by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private val editor = sharedPref?.edit()

    fun showCoachMarkAr(targetView: View?) {
        val shouldShowCoachMark =
            getCoachMarkState(PRODUCT_DETAIL_AR_PAGE_COACHMARK) == false && !coachMarkEverShowing

        if (targetView != null && shouldShowCoachMark) {
            initCoachMarkView(targetView.context)

            val coachMarkList = arrayListOf<CoachMark2Item>()
            coachMarkList.add(
                CoachMark2Item(
                    targetView,
                    targetView.context.getString(R.string.pdp_ar_coachmark_title),
                    targetView.context.getString(R.string.pdp_ar_coachmark_desc),
                    CoachMark2.POSITION_TOP
                )
            )

            coachMarkView?.showCoachMark(coachMarkList, null, 0)
            coachMarkEverShowing = true
            lastShown = PRODUCT_DETAIL_AR_PAGE_COACHMARK
            setCoachMarkState(PRODUCT_DETAIL_AR_PAGE_COACHMARK, true)
        }
    }

    fun showCoachMarkOneLiners(targetView: View?) {
        val shouldShowCoachMark =
            getCoachMarkState(COACH_MARK_IMS_TAG) == false && !coachMarkEverShowing


        if (targetView != null && shouldShowCoachMark) {
            initCoachMarkView(targetView.context)

            val coachMarkList = arrayListOf<CoachMark2Item>()

            coachMarkList.add(
                CoachMark2Item(
                    anchorView = targetView,
                    title = targetView.context.getString(R.string.pdp_oneliners_ims100_coachmark_title),
                    description = targetView.context.getString(R.string.pdp_oneliners_ims100_coachmark_description),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )

            coachMarkView?.showCoachMark(coachMarkList, null, 0)
            coachMarkEverShowing = true
            lastShown = COACH_MARK_IMS_TAG
            setCoachMarkState(COACH_MARK_IMS_TAG, true)
        }
    }

    fun hideCoachMark(key: String) {
        if (coachMarkView != null && coachMarkView?.isShowing == true && lastShown == key) {
            coachMarkView?.hideCoachMark()
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
        const val PREF_NAME = "pdp_coachmark"
        const val PRODUCT_DETAIL_AR_PAGE_COACHMARK = "coach_mark_pdp_ar_page"
        const val COACH_MARK_IMS_TAG = "pdp_coachmark_ims"
    }
}