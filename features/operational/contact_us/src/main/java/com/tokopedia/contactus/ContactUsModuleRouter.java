package com.tokopedia.contactus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import okhttp3.Interceptor;

/**
 * @author by alvinatin on 26/04/18.
 */

public interface ContactUsModuleRouter {

    Intent getChatBotIntent(Context context, String messageId);

    Interceptor getChuckerInterceptor();

    Intent getHomeIntent(Context context);

    Intent getHelpUsIntent(Context context);

    void actionNavigateByApplinksUrl(Activity activity, String s, Bundle bundle);

    Intent getTopProfileIntent(Context context, String loginID);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    String ACTION_CLOSE_ACTIVITY = "action_close_activity";
}
