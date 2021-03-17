package com.tokopedia.product.addedit.common.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/02/21
 */

class AddEditSellerReviewHelper @Inject constructor(
        @ApplicationContext context: Context,
        private val userSession: UserSessionInterface
) {

    companion object {
        private const val PREFERENCE_NAME = "CACHE_SELLER_IN_APP_REVIEW"
        private const val KEY_HAS_ADDED_PRODUCT = "KEY_SIR_HAS_ADDED_PRODUCT"
    }

    private val editor: SharedPreferences.Editor

    init {
        val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun saveAddProductFrag() {
        editor.putBoolean(KEY_HAS_ADDED_PRODUCT+userSession.userId, true)
        editor.apply()
    }
}