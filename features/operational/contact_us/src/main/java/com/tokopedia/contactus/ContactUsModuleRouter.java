package com.tokopedia.contactus;

import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

/**
 * @author by alvinatin on 26/04/18.
 */

public interface ContactUsModuleRouter {
    Intent getChatBotIntent(Context context, String messageId);
    public Interceptor getChuckInterceptor();
}
