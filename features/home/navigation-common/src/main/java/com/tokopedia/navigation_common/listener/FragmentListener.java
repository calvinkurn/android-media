package com.tokopedia.navigation_common.listener;

/**
 * @author by milhamj on 09/08/18.
 */

public interface FragmentListener {
    void onScrollToTop();
    boolean isLightThemeStatusBar();
    default boolean isForceDarkModeNavigationBar() {
        return false;
    }
}
