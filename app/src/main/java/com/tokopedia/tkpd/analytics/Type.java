package com.tokopedia.tkpd.analytics;

/**
 * Created by Nanda J.A on 6/9/2015.
 * Modified & Cleaned by Alvarisi
 */
public final class Type {

    /**
     * Google Analytics
     */
    public static final int GA = 0;

    /**
     * Google TAG Manager
     */
    public static final int GTM = 1;

    /**
     * Localytics
     */
    public static final int LOCALYTICS = 2;

    /**
     * AppsFlyer
     */
    public static final int APPSFLYER = 3;

    /**
     * Get analytics type name
     * @param type
     * @return
     */
    public static String getName(int type) {
        switch(type) {
            case Type.GA :
                return "Google Analytics";

            case Type.GTM :
                return "Google TAG Manager";

            case Type.LOCALYTICS :
                return "Localytics";
        }

        return "Type undefined";
    }

}
