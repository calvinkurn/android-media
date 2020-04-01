package com.tokopedia.abstraction.common.utils.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class CommonUtils {

    public static <T> boolean checkStringNotNull(T reference) {
        return checkNotNull(reference) && (!reference.equals("")) && (!reference.equals("0"));
    }

    public static <T> boolean checkNotNull(T reference) {
        return reference != null;
    }

    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

}
