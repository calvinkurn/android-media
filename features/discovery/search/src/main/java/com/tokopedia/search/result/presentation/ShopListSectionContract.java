package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;
import java.util.Map;

public interface ShopListSectionContract {

    interface View extends SearchSectionContract.View {
        String getUserId();

        void onSearchShopSuccessWithData(List<Visitable> shopViewItemList, boolean isHasNextPage);

        void onSearchShopSuccessEmptyResult();

        void onSearchShopFailed();
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {
        void loadData(Map<String, Object> searchParameter, int loadShopRow);

        void loadMoreData(Map<String, Object> searchParameter, int loadShopRow);
    }
}
