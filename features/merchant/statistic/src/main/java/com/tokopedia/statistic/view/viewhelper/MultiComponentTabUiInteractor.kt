package com.tokopedia.statistic.view.viewhelper

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.statistic.R.string


class MultiComponentTabUiInteractor(context: Context) {

    companion object {
        private const val PREF_NAME = "statistic_coachmark_pref"
        private const val STC_COACHMARK_MULTI_COMPONENT = "coachmark_multi_component_tab"
        private const val STC_AUTO_SCROLL_MULTI_COMPONENT = "auto_scroll_multi_component_tab"
    }

    private val sharedPref: SharedPreferences? by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    private var coachMarkView: CoachMark2? = null

    private val editor = sharedPref?.edit()

    fun showCoachMarkMultiComponent(targetView: View?, userId: String) {
        val shouldShowCoachMark =
            getCoachMarkState(STC_COACHMARK_MULTI_COMPONENT + userId) == false

        if (targetView != null && shouldShowCoachMark) {
            initCoachMarkView(targetView.context)
            val coachMarkList = arrayListOf<CoachMark2Item>()
            coachMarkList.add(
                CoachMark2Item(
                    targetView,
                    targetView.context.getString(string.stc_multi_component_coachmark_title),
                    targetView.context.getString(string.stc_multi_component_coachmark_description),
                    CoachMark2.POSITION_BOTTOM
                )
            )

            coachMarkView?.showCoachMark(coachMarkList, null, 0)
            setCoachMarkMultiComponentShown(userId)
        }
    }

    fun hideCoachMark() {
        coachMarkView?.hideCoachMark()
    }

    fun alreadyAutoScroll(userId: String): Boolean {
        return sharedPref?.getBoolean(STC_AUTO_SCROLL_MULTI_COMPONENT + userId, false) ?: false
    }

    fun setAlreadyAutoScroll(userId: String) {
        editor?.putBoolean(STC_AUTO_SCROLL_MULTI_COMPONENT + userId, true)
        editor?.apply()
    }

    fun destroy() {
        coachMarkView = null
    }

    private fun setCoachMarkMultiComponentShown(userId: String) {
        setCoachMarkState(STC_COACHMARK_MULTI_COMPONENT + userId, true)
    }

    private fun initCoachMarkView(context: Context) {
        if (coachMarkView == null) {
            coachMarkView = CoachMark2(context)
        }
    }

    private fun setCoachMarkState(key: String, state: Boolean) {
        editor?.putBoolean(key, state)
        editor?.apply()
    }

    private fun getCoachMarkState(key: String): Boolean? {
        return sharedPref?.getBoolean(key, false)
    }
}