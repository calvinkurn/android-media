package com.tokopedia.homenav.mainnav.domain

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.homenav.mainnav.domain.model.MainNavProfileCache
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel

object MainNavSharedPref {
    private const val PREF_KEY_MAIN_NAV_SHARED_PREF = "main_nav_shared_preference"
    private const val PREF_KEY_MAIN_NAV_PROFILE_NAME = "main_nav_profile_name_shared_preference"
    private const val PREF_KEY_MAIN_NAV_PROFILE_PIC = "main_nav_profile_pic_shared_preference"
    private const val PREF_KEY_MAIN_NAV_PROFILE_MEMBER_STATUS = "main_nav_profile_member_status_shared_preference"
    private const val PREF_KEY_MAIN_NAV_ONCE_DOT_BUY_AGAIN = "main_nav_buy_again_once_dot"

    private fun setProfileNameCache(context: Context, profileName: String) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_MAIN_NAV_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(PREF_KEY_MAIN_NAV_PROFILE_NAME, profileName).apply()
    }

    private fun setProfilePicCache(context: Context, picCacheUrl: String) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_MAIN_NAV_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(PREF_KEY_MAIN_NAV_PROFILE_PIC, picCacheUrl).apply()
    }

    private fun setProfileMemberCache(context: Context, memberCacheUrl: String) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_MAIN_NAV_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(PREF_KEY_MAIN_NAV_PROFILE_MEMBER_STATUS, memberCacheUrl).apply()
    }

    fun setTemporaryDotInBuyAgain(context: Context, isShown: Boolean) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_MAIN_NAV_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(PREF_KEY_MAIN_NAV_ONCE_DOT_BUY_AGAIN, isShown).apply()
    }

    fun setProfileCacheFromAccountModel(context: Context, accountHeaderDataModel: AccountHeaderDataModel) {
        setProfileNameCache(context, accountHeaderDataModel.profileDataModel.userName)
        setProfilePicCache(context, accountHeaderDataModel.profileDataModel.userImage)
        setProfileMemberCache(context, accountHeaderDataModel.profileMembershipDataModel.badge)
    }

    fun getProfileCacheData(context: Context): MainNavProfileCache? {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_MAIN_NAV_SHARED_PREF, Context.MODE_PRIVATE)
        val profileName = sharedPrefs.getString(PREF_KEY_MAIN_NAV_PROFILE_NAME, "")
        val profilePic = sharedPrefs.getString(PREF_KEY_MAIN_NAV_PROFILE_PIC, "")
        val profileMember = sharedPrefs.getString(PREF_KEY_MAIN_NAV_PROFILE_MEMBER_STATUS, "")

        if (profileName.isNullOrEmpty() && profilePic.isNullOrEmpty() && profileMember.isNullOrEmpty()) {
            return null
        }
        return MainNavProfileCache(
            profileName = profileName,
            profilePicUrl = profilePic,
            memberStatusIconUrl = profileMember
        )
    }
}
