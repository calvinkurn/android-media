package com.tokopedia.home_account.account_settings.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({SettingType.FLAG_NEWSLETTER, SettingType.FLAG_MESSAGE, SettingType.FLAG_TALK, SettingType.FLAG_ADMIN_MESSAGE, SettingType.FLAG_REVIEW})
public @interface SettingType {
    String FLAG_NEWSLETTER = "flag_newsletter";
    String FLAG_MESSAGE = "flag_message";
    String FLAG_TALK = "flag_talk_product";
    String FLAG_ADMIN_MESSAGE = "flag_admin_message";
    String FLAG_REVIEW = "flag_review";
}
