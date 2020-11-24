package com.tokopedia.home_account.view.helper

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 04/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class StaticMenuGenerator @Inject constructor(val context: Context) {

    fun generateUserSettingMenu(): SettingDataView {
        return SettingDataView(context?.getString(R.string.menu_account_section_title_account_setting), mutableListOf(
                CommonDataView(applink = ApplinkConstInternalLogistic.MANAGE_ADDRESS, title = context?.getString(R.string.menu_account_title_address_list), body = context?.getString(R.string.menu_account_desc_address), type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_shop, id = AccountConstants.SettingCode.SETTING_ACCOUNT_ADDRESS_ID),
                CommonDataView(applink = ApplinkConstInternalGlobal.SETTING_BANK, title = context?.getString(R.string.menu_account_title_bank), body = context?.getString(R.string.menu_account_desc_bank), type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_bank, id = AccountConstants.SettingCode.SETTING_BANK_ACCOUNT_ID),
                CommonDataView(applink = ApplinkConstInternalGlobal.PAYMENT_SETTING, title = context?.getString(R.string.menu_account_title_instant_payment), body = context?.getString(R.string.menu_account_desc_instant_payment), type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_card),
                CommonDataView(applink = ApplinkConstInternalMarketplace.PREFERENCE_LIST, title = context?.getString(R.string.menu_account_title_quick_buy), body = context?.getString(R.string.menu_account_desc_quick_buy), type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_quick_buy),
                CommonDataView(applink = ApplinkConstInternalGlobal.ACCOUNT_SETTING, title = context?.getString(R.string.menu_account_title_security), body = context?.getString(R.string.menu_account_desc_security), type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_lock, id = AccountConstants.SettingCode.SETTING_SECURITY),
                CommonDataView(applink = ApplinkConst.SETTING_NOTIFICATION, title = context?.getString(R.string.menu_account_title_notification), body = context?.getString(R.string.menu_account_desc_notification), type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_ring, id = AccountConstants.SettingCode.SETTING_NOTIFICATION)
        ), isExpanded = true)
    }

    fun generateApplicationSettingMenu(accountPref: AccountPreference, permissionChecker: PermissionChecker): SettingDataView {
        return SettingDataView(context?.getString(R.string.menu_account_section_title_app_setting), mutableListOf(
                CommonDataView(id = AccountConstants.SettingCode.SETTING_SHAKE_ID, title = context?.getString(R.string.menu_account_title_shake), body = context?.getString(R.string.menu_account_desc_shake),
                        type = CommonViewHolder.TYPE_SWITCH, icon = R.drawable.ic_account_shake,
                        isChecked = accountPref.isItemSelected(AccountConstants.KEY.KEY_PREF_SHAKE, true)),
                CommonDataView(id = AccountConstants.SettingCode.SETTING_GEOLOCATION_ID, title = context?.getString(R.string.menu_account_title_geolocation), body = context?.getString(R.string.menu_account_desc_geolocation),
                        type = CommonViewHolder.TYPE_SWITCH, icon = R.drawable.ic_account_location,
                        isChecked = permissionChecker.hasLocationPermission()),
                CommonDataView(id = AccountConstants.SettingCode.SETTING_SAFE_SEARCH_ID, title = context?.getString(R.string.menu_account_title_safe_mode), body = context?.getString(R.string.menu_account_desc_safe_mode), type = CommonViewHolder.TYPE_SWITCH, icon = R.drawable.ic_account_safe_mode,
                        isChecked = accountPref.isItemSelected(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, false)),
                CommonDataView(id = AccountConstants.SettingCode.SETTING_APP_ADVANCED_CLEAR_CACHE, title = context?.getString(R.string.menu_account_title_clear_cache), body = context?.getString(R.string.menu_account_desc_clear_cache), type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_cache)
        ), showArrowDown = true)
    }

    fun generateAboutTokopediaSettingMenu(): SettingDataView {
        return  SettingDataView(context?.getString(R.string.menu_account_section_title_about_us), mutableListOf(
                CommonDataView(title = context?.getString(R.string.menu_account_title_about_us), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_toped, id = AccountConstants.SettingCode.SETTING_ABOUT_US),
                CommonDataView(title = context?.getString(R.string.menu_account_title_terms), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_clipboard, id = AccountConstants.SettingCode.SETTING_TNC_ID),
                CommonDataView(title = context?.getString(R.string.menu_account_title_privacy_policy), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_privacy, id = AccountConstants.SettingCode.SETTING_PRIVACY_ID),
                CommonDataView(title = context?.getString(R.string.menu_account_title_review), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_rating, id = AccountConstants.SettingCode.SETTING_APP_REVIEW_ID))
                , showArrowDown = true)
    }

    fun generateDeveloperOptionsSettingMenu(): SettingDataView {
        return  SettingDataView(context.getString(R.string.menu_account_section_title_developer), mutableListOf(
                CommonDataView(title = context.getString(R.string.menu_account_title_dev_options), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_toped, id = AccountConstants.SettingCode.SETTING_DEV_OPTIONS),
                CommonDataView(title = context.getString(R.string.menu_account_title_feedback_form), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_toped, id = AccountConstants.SettingCode.SETTING_FEEDBACK_FORM),
                CommonDataView(title = context.getString(R.string.menu_account_old_account), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_toped, id = AccountConstants.SettingCode.SETTING_OLD_ACCOUNT))

        , showArrowDown = true)
    }
}
