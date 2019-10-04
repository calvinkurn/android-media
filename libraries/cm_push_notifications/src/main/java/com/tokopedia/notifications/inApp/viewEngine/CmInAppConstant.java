package com.tokopedia.notifications.inApp.viewEngine;

/**
 * @author lalit.singh
 */
public interface CmInAppConstant {


    String TYPE_FULL_SCREEN = "full_screen";
    String TYPE_FULL_SCREEN_IMAGE_ONLY = "full_screen_img";
    String TYPE_INTERSTITIAL = "interstitial";
    String TYPE_INTERSTITIAL_IMAGE_ONLY = "interstitial_img";
    String TYPE_BORDER_BOTTOM = "borderBottom";
    String TYPE_BORDER_TOP = "borderTop";

    String ORIENTATION_VERTICAL = "vertical";
    String ORIENTATION_HORIZONTAL = "horizontal";
    String TYPE_ALERT = "alert";


    interface ViewAnimation {
        String FADE_IN = "fdi";
        String FADE_OUT = "fdo";

        String FLIP_IN = "fli";
        String FLIP_OUT = "flo";

        String SLIDE_DOWN_IN = "sdi";
        String SLIDE_DOWN_OUT = "sdo";

        String SLIDE_LEFT_IN = "sli";
        String SLIDE_LEFT_OUT = "slo";

        String SLIDE_RIGHT_IN = "sri";
        String SLIDE_RIGHT_OUT = "sro";

        String SLIDE_UP_IN = "sui";
        String SLIDE_UP_OUT = "suo";
    }
}
