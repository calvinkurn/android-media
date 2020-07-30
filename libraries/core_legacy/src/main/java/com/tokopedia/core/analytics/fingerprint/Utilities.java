package com.tokopedia.core.analytics.fingerprint;

import android.util.Base64;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.UnsupportedEncodingException;

/**
 * Created by Herdi_WORK on 21.06.17.
 */

public class Utilities {

    public static String isNakama(UserSessionInterface userSession){
        if(GlobalConfig.DEBUG)
            return "True";
        if(userSession != null && CommonUtils.checkStringNotNull(userSession.getEmail()))
            return userSession.getEmail().contains("@tokopedia") ? "True" : "False";
        else
            return "False";
    }

    public static String toBase64(String text, int mode) throws UnsupportedEncodingException {
        byte[] data = text.getBytes("UTF-8");
        return Base64.encodeToString(data, mode);
    }
}