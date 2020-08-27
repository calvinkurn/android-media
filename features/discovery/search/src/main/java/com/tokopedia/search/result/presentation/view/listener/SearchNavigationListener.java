package com.tokopedia.search.result.presentation.view.listener;

public interface SearchNavigationListener {

    void setupSearchNavigation(ClickListener clickListener, boolean isSortEnabled);

    void refreshMenuItemGridIcon(int titleResId, int iconResId);

    void removeSearchPageLoading();

    interface ClickListener {
        void onChangeGridClick();
    }

    void configureTabLayout(boolean isVisible);
}
