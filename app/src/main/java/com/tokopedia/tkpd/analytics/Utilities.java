package com.tokopedia.tkpd.analytics;

import android.content.Context;
import android.util.Log;

/**
 * Created by Nanda J.A on 6/11/2015.
 */
public class Utilities {

    private static final String TAG = "Utilities";

    private Context context;
    private static boolean debugState = true;

    public Utilities(Context context) {
        this.context = context;
    }

    /**
     * enable/disable debugging (default : enable/true)
     * @param state
     */
    public void enableDebugging(boolean state) {
        debugState = state;
    }

    /**
     * show debugging
     * @param type
     */
    public void debug(int type) {
        if(debugState) {
            switch(type) {
                case Type.GTM :
                    TrackingUtils.enableDebugging(debugState);
                    break;

                case Type.LOCALYTICS :
                    TrackingUtils.enableDebugging(debugState);
                    break;
            }
        }
    }

    /**
     * Get screen name
     * @param context
     * @param screenName
     * @return
     */
    public String getScreenName(Context context, String screenName) {
        if(screenName == null || screenName.equals("")) {
            return context.getClass().getName();
        }

        return screenName;
    }

    public String getObjString(Object object) {
        try {
            return ((String) object);
        } catch(ClassCastException | NullPointerException e) {
            Log.e(TAG, "Error : "+ e.getMessage());
        }

        return "Object casting error";
    }

    public int getObjInteger(Object object) {
        try {
            return ((int) object);
        } catch(ClassCastException | NullPointerException e) {
            Log.e(TAG, "Error : "+ e.getMessage());
        }

        return 0;
    }

}
