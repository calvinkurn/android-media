package com.tokopedia.creation.common.presentation.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 28, 2023
 */
class ContentCreationEntryPointSharedPref @Inject constructor(
    @ApplicationContext context: Context,
    private val userSession: UserSessionInterface
) {

    private val mSharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun hasShownViewContentInfoInMa(): Boolean {
        return mSharedPref.getBoolean(
            String.format(VIEW_CONTENT_INFO_IN_MA, userSession.userId),
            false
        )
    }

    fun setShownViewContentInfoInMa() {
        mSharedPref.edit()
            .putBoolean(
                String.format(VIEW_CONTENT_INFO_IN_MA, userSession.userId),
                true
            ).apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "content_creation_entry_point_shared_pref"

        private const val VIEW_CONTENT_INFO_IN_MA = "view_content_info_in_ma_%s"
    }
}
