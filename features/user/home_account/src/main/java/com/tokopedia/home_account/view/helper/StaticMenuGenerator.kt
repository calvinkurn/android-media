package com.tokopedia.home_account.view.helper

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.user.DeeplinkMapperUser
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.data.pref.AccountPreference
import com.tokopedia.home_account.view.adapter.viewholder.CommonViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 04/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@ActivityScope
class StaticMenuGenerator @Inject constructor(val context: Context, val abTestPlatform: AbTestPlatform) {

    fun generateUserSettingMenu(): SettingDataView {
        return SettingDataView(
            context.getString(R.string.menu_account_section_title_account_setting),
            mutableListOf(
                CommonDataView(applink = ApplinkConstInternalLogistic.MANAGE_ADDRESS_FROM_ACCOUNT, title = context.getString(R.string.menu_account_title_address_list), body = context.getString(R.string.menu_account_desc_address), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.SHOP, id = AccountConstants.SettingCode.SETTING_ACCOUNT_ADDRESS_ID),
                CommonDataView(applink = ApplinkConstInternalGlobal.SETTING_BANK, title = context.getString(R.string.menu_account_title_bank), body = context.getString(R.string.menu_account_desc_bank), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.FINANCE, id = AccountConstants.SettingCode.SETTING_BANK_ACCOUNT_ID),
                CommonDataView(applink = ApplinkConstInternalUserPlatform.PAYMENT_SETTING, title = context.getString(R.string.menu_account_title_instant_payment), body = context.getString(R.string.menu_account_desc_instant_payment), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.CARD, id = AccountConstants.SettingCode.SETTING_INSTANT_PAYMENT),
                CommonDataView(applink = ApplinkConstInternalUserPlatform.ACCOUNT_SETTING, title = context.getString(R.string.menu_account_title_security), body = context.getString(R.string.menu_account_desc_security), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.LOCK, id = AccountConstants.SettingCode.SETTING_SECURITY),
                CommonDataView(applink = ApplinkConst.SETTING_NOTIFICATION, title = context.getString(R.string.menu_account_title_notification), body = context.getString(R.string.menu_account_desc_notification), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.BELL_RING, id = AccountConstants.SettingCode.SETTING_NOTIFICATION),
                CommonDataView(
                    applink = if (!isUsingPrivacyCenter()) {
                        ApplinkConstInternalUserPlatform.PRIVACY_ACCOUNT
                    } else {
                        ApplinkConstInternalUserPlatform.PRIVACY_CENTER
                    },
                    title = context.getString(R.string.menu_account_title_privacy_account),
                    body = context.getString(R.string.menu_account_desc_privacy_account),
                    type = CommonViewHolder.TYPE_DEFAULT,
                    icon = IconUnify.GLOBE,
                    id = AccountConstants.SettingCode.SETTING_PRIVACY_ACCOUNT
                ),
                CommonDataView(applink = ApplinkConstInternalUserPlatform.EXPLICIT_PROFILE, title = context.getString(R.string.menu_account_title_explicit_profile), body = context.getString(R.string.menu_account_desc_explicit_profile), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.SHOPPING_BAG, id = AccountConstants.SettingCode.SETTING_EXPLICIT_PROFILE)
            ),
            isExpanded = true
        )
    }

    fun generateApplicationSettingMenu(
        accountPref: AccountPreference,
        permissionChecker: PermissionChecker,
        showDarkModeToggle: Boolean,
        showScreenRecorder: Boolean
    ): SettingDataView {
        val listSetting = mutableListOf(
            CommonDataView(
                id = AccountConstants.SettingCode.SETTING_SAFE_SEARCH_ID,
                title = context.getString(R.string.menu_account_title_safe_mode),
                body = context.getString(R.string.menu_account_desc_safe_mode),
                type = CommonViewHolder.TYPE_SWITCH,
                icon = IconUnify.PROTECTION,
                isChecked = accountPref.isItemSelected(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, false)
            ),
            CommonDataView(
                id = AccountConstants.SettingCode.SETTING_PLAY_WIDGET_AUTOPLAY,
                title = context.getString(R.string.menu_account_title_play_widget_autoplay),
                body = context.getString(R.string.menu_account_desc_play_widget_autoplay),
                type = CommonViewHolder.TYPE_SWITCH,
                icon = IconUnify.VIDEO,
                isChecked = accountPref.isItemSelected(AccountConstants.KEY.KEY_PREF_PLAY_WIDGET_AUTOPLAY, true)
            )
        )

        if (!isUsingPrivacyCenter()) {
            listSetting.addAll(
                index = 0,
                elements = listOf(
                    CommonDataView(
                        id = AccountConstants.SettingCode.SETTING_SHAKE_ID,
                        title = context.getString(R.string.menu_account_title_shake),
                        body = context.getString(R.string.menu_account_desc_shake),
                        type = CommonViewHolder.TYPE_SWITCH,
                        icon = IconUnify.SHAKE,
                        isChecked = accountPref.isItemSelected(
                            key = AccountConstants.KEY.KEY_PREF_SHAKE,
                            defaultValue = true
                        )
                    ),
                    CommonDataView(
                        id = AccountConstants.SettingCode.SETTING_GEOLOCATION_ID,
                        title = context.getString(R.string.menu_account_title_geolocation),
                        body = context.getString(R.string.menu_account_desc_geolocation),
                        type = CommonViewHolder.TYPE_SWITCH,
                        icon = IconUnify.LOCATION,
                        isChecked = permissionChecker.hasLocationPermission()
                    )
                )
            )
        }

        if (showDarkModeToggle) {
            listSetting.add(
                CommonDataView(
                    id = AccountConstants.SettingCode.SETTING_DARK_MODE,
                    title = context.getString(R.string.menu_account_title_dark_mode),
                    body = context.getString(R.string.menu_account_desc_dark_mode),
                    type = CommonViewHolder.TYPE_SWITCH,
                    icon = IconUnify.MODE_SCREEN,
                    isChecked = accountPref.isItemSelected(TkpdCache.Key.KEY_DARK_MODE, false),
                    labelText = getLabelText(R.string.new_home_account_label_beta)
                )
            )
        }

        listSetting.addAll(
            mutableListOf(
                CommonDataView(id = AccountConstants.SettingCode.SETTING_QUALITY_SETTING, title = context.getString(R.string.menu_account_title_quality_setting), body = context.getString(R.string.menu_account_desc_quality_setting), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.IMAGE),
                CommonDataView(id = AccountConstants.SettingCode.SETTING_APP_ADVANCED_CLEAR_CACHE, title = context.getString(R.string.menu_account_title_clear_cache), body = context.getString(R.string.menu_account_desc_clear_cache), type = CommonViewHolder.TYPE_DEFAULT, icon = IconUnify.BROOM)
            )
        )
        if (showScreenRecorder) {
            listSetting.add(
                CommonDataView(
                    id = AccountConstants.SettingCode.SETTING_APP_ADVANCED_SCREEN_RECORD,
                    title = context.getString(R.string.menu_account_title_screen_recorder),
                    body = context.getString(R.string.menu_account_desc_screen_recorder),
                    type = CommonViewHolder.TYPE_DEFAULT,
                    icon = IconUnify.CAMERA
                )
            )
        }
        return SettingDataView(context.getString(R.string.menu_account_section_title_app_setting), listSetting, showArrowDown = true)
    }

    fun generateAboutTokopediaSettingMenu(): SettingDataView {
        val listSettingMenu = mutableListOf(
            CommonDataView(title = context.getString(R.string.menu_account_title_about_us), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.TOPED, id = AccountConstants.SettingCode.SETTING_ABOUT_US),
            CommonDataView(title = context.getString(R.string.menu_account_title_terms), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.CLIPBOARD, id = AccountConstants.SettingCode.SETTING_TNC_ID),
            CommonDataView(title = context.getString(R.string.menu_account_title_intellectual_property), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.IP, id = AccountConstants.SettingCode.SETTING_IP),
            CommonDataView(title = context.getString(R.string.menu_account_title_review), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.RATING, id = AccountConstants.SettingCode.SETTING_APP_REVIEW_ID)
        )

        if (!isUsingPrivacyCenter()) {
            listSettingMenu.add(
                index = 2,
                element = CommonDataView(
                    title = context.getString(R.string.menu_account_title_privacy_policy),
                    type = CommonViewHolder.TYPE_WITHOUT_BODY,
                    icon = IconUnify.POLICY_PRIVACY,
                    id = AccountConstants.SettingCode.SETTING_PRIVACY_ID
                )
            )
        }

        return SettingDataView(
            title = context.getString(R.string.menu_account_section_title_about_us),
            items = listSettingMenu,
            showArrowDown = true
        )
    }

    fun generateDeveloperOptionsSettingMenu(): SettingDataView {
        return SettingDataView(
            context.getString(R.string.menu_account_section_title_developer),
            mutableListOf(
                CommonDataView(title = context.getString(R.string.menu_account_title_dev_options), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.SETTING, id = AccountConstants.SettingCode.SETTING_DEV_OPTIONS),
                CommonDataView(title = context.getString(R.string.menu_account_title_feedback_form), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.SETTING, id = AccountConstants.SettingCode.SETTING_FEEDBACK_FORM)
            ),
            showArrowDown = true
        )
    }

    private fun getLabelText(id: Int): String {
        return try {
            context.getString(id)
        } catch (ignored: Throwable) {
            ""
        }
    }

    private fun isUsingPrivacyCenter(): Boolean {
        return abTestPlatform
            .getString(DeeplinkMapperUser.ROLLENCE_PRIVACY_CENTER)
            .isNotEmpty()
    }
}
