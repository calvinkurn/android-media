package com.tokopedia.home.account.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.home.account.constant.SettingType.FLAG_ADMIN_MESSAGE;
import static com.tokopedia.home.account.constant.SettingType.FLAG_MESSAGE;
import static com.tokopedia.home.account.constant.SettingType.FLAG_NEWSLETTER;
import static com.tokopedia.home.account.constant.SettingType.FLAG_REVIEW;
import static com.tokopedia.home.account.constant.SettingType.FLAG_TALK;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FLAG_NEWSLETTER, FLAG_MESSAGE, FLAG_TALK, FLAG_ADMIN_MESSAGE, FLAG_REVIEW})
public @interface SettingType {
    String FLAG_NEWSLETTER = "flag_newsletter";
    String FLAG_MESSAGE = "flag_message";
    String FLAG_TALK = "flag_talk_product";
    String FLAG_ADMIN_MESSAGE = "flag_admin_message";
    String FLAG_REVIEW = "flag_review";
}
