package com.tokopedia.navigation_common.listener;

/**
 * @author by milhamj on 09/08/18.
 */

public interface FragmentListener {
    void onScrollToTop();

    void onScrollToRecommendationForYou();
    boolean isLightThemeStatusBar();
    default boolean isForceDarkModeNavigationBar() {
        return false;
    }
}
