package com.tokopedia.content.product.preview.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 01/02/24
 */
class ProductPreviewSharedPreference @Inject constructor(
    @ApplicationContext context: Context,
    private val userSession: UserSessionInterface
) {
    private val userId: String
        get() = userSession.userId.ifEmpty { "0" }

    private val sharedPref =
        context.getSharedPreferences(PRODUCT_PREVIEW_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setHasVisit() {
        sharedPref.edit().putBoolean(String.format(COACH_MARK_PREF, userId), true).apply()
    }
    fun hasVisited(): Boolean {
        return sharedPref.getBoolean(String.format(COACH_MARK_PREF, userId), false)
    }

    companion object {
        private const val PRODUCT_PREVIEW_PREFERENCE_NAME = "product_prev_pref"
        private const val COACH_MARK_PREF = "product_prev_coach_mark_%1s"
    }
}
