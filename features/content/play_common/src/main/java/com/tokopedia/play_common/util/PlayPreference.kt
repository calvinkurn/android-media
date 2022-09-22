package com.tokopedia.play_common.util

import android.content.Context
import android.util.Log
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 17/01/20
 */
class PlayPreference @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val PLAY_PREFERENCE = "play_preference"

        private const val FORMAT_ONE_TAP_ONBOARDING = "one_tap_onboarding_%s"
        private const val FORMAT_SWIPE_ONBOARDING = "swipe_onboarding_%s"
        private const val SWIPE_ONBOARDING = "new_swipe_onboarding_%s"
    }

    private val sharedPref = context.getSharedPreferences(PLAY_PREFERENCE, Context.MODE_PRIVATE)

    fun setOnboardingShown(tag: String) {
        setSwipeOnboardingShown(tag)
    }

    fun isOnboardingShown(tag: String): Boolean {
        return isSwipeOnboardingShown(tag)
    }

    /**
     * Valid from Initial Play -> Swipe Room
     */
    private fun setOneTapOnboardingShown(tag: String) {
        sharedPref.edit().putBoolean(String.format(FORMAT_ONE_TAP_ONBOARDING, tag), true).apply()
    }

    /**
     * Valid from Initial Play -> Swipe Room
     */
    private fun isOneTapOnboardingShown(tag: String): Boolean {
        return sharedPref.getBoolean(String.format(FORMAT_ONE_TAP_ONBOARDING, tag), false)
    }

    /**
     * Valid since Swipe Room
     */
    private fun setSwipeOnboardingShown(tag: String) {
        sharedPref.edit().putBoolean(String.format(FORMAT_SWIPE_ONBOARDING, tag), true).apply()
    }

    /**
     * Valid since Swipe Room
     */
    private fun isSwipeOnboardingShown(tag: String): Boolean {
        return sharedPref.getBoolean(String.format(FORMAT_SWIPE_ONBOARDING, tag), false)
    }

    /**
     * check last visit
     */

    private fun countDays(time: Long) : Long  {
        val diff = System.currentTimeMillis() - time
        return diff
    }

    private val diffDay: Long get() {
        val lastVisit = sharedPref.getLong(SWIPE_ONBOARDING, 0L)
        val diff = System.currentTimeMillis() - lastVisit
        Log.d("sukses diff", diff.toString())
        return diff
    }

    private val dayPlusOne: Long get(){ //by lazy
        return DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.DAY_OF_MONTH, 1).time
    }

    fun setCoachMark() { // first channel event
        if (isCoachMark()) { // move to val get ambil sendiri
            Log.d("sukses", "in")
            sharedPref.edit().putLong(SWIPE_ONBOARDING, System.currentTimeMillis()).apply()


            //coba delay get urrent time millis
        }
        Log.d("sukses", "out")
    }

    //day should be this time + 1

    fun isCoachMark(): Boolean {
        return diffDay >= dayPlusOne
    }

    //AB TEST if variant x
}
