package com.tokopedia.content.common.producttag.util.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 19, 2022
 */
class ProductTagPreference @Inject constructor(
    @ApplicationContext context: Context,
    private val userSession: UserSessionInterface
){
    private val mSharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun isFirstGlobalTag(): Boolean {
        return mSharedPref.getBoolean(
            String.format(KEY_FIRST_GLOBAL_TAG, userSession.userId),
            true
        )
    }

    fun setNotFirstGlobalTag() {
        mSharedPref.edit()
            .putBoolean(
                String.format(KEY_FIRST_GLOBAL_TAG, userSession.userId),
                false
            ).apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "content_product_tag_preference"

        private const val KEY_FIRST_GLOBAL_TAG = "first_global_tag_%s"
    }
}