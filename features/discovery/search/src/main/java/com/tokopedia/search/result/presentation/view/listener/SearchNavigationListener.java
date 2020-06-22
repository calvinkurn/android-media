package com.tokopedia.search.result.presentation.view.listener;

public interface SearchNavigationListener {

    void setupSearchNavigation(ClickListener clickListener, boolean isSortEnabled);

    void showBottomNavigation();

    void hideBottomNavigation();

    void refreshMenuItemGridIcon(int titleResId, int iconResId);

    void removeSearchPageLoading();

    interface ClickListener {
        void onFilterClick();
        void onSortClick();
        void onChangeGridClick();
    }

    void animateTab(boolean isVisible);
}
