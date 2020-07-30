package com.tokopedia.imagesearch.search.fragment.product.adapter.listener;

public interface ImageSearchNavigationListener {

    void setupSearchNavigation(ClickListener clickListener);

    interface ClickListener {
        void onFilterClick();
        void onSortClick();
    }
}
