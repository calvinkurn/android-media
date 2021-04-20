package com.tokopedia.notifications.inApp.viewEngine;

/**
 * @author lalit.singh
 */
public interface CmInAppConstant {

    String TYPE_FULL_SCREEN = "full_screen";
    String TYPE_FULL_SCREEN_IMAGE_ONLY = "full_screen_img";
    String TYPE_INTERSTITIAL = "interstitial";
    String TYPE_GRATIF = "gratification";
    String TYPE_INTERSTITIAL_IMAGE_ONLY = "interstitial_img";
    String TYPE_BORDER_BOTTOM = "borderBottom";
    String TYPE_BORDER_TOP = "borderTop";
    String TYPE_SILENT = "silent";

    String ORIENTATION_VERTICAL = "vertical";
    String ORIENTATION_HORIZONTAL = "horizontal";
    String TYPE_ALERT = "alert";
    String TYPE_DROP = "drop";

    String BUTTON_MAIN_TYPE = "main";
    String BUTTON_TRANSACTION_TYPE = "transaction";
    String BUTTON_ALTERNATE_TYPE = "alternate";

    String BUTTON_FILLED_VARIANT = "filled";
    String BUTTON_GHOST_VARIANT = "ghost";
    String BUTTON_TEXT_ONLY_VARIANT = "text_only";

    String BUTTON_LARGE_SIZE = "large";
    String BUTTON_MEDIUM_SIZE = "medium";
    String BUTTON_SMALL_SIZE = "small";
    String BUTTON_MICRO_SIZE = "micro";

    interface ScreenListConstants {
        String SPLASH = "com.tokopedia.tkpd.ConsumerSplashScreen";
        String DEEPLINK_ACTIVITY = "com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity";
        String DEEPLINK_HANDLER_ACTIVITY = "com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity";
    }

}
