package com.tokopedia.core.discovery.view;

import android.os.Bundle;

import com.tokopedia.core.discovery.adapter.ProductAdapter;
import com.tokopedia.core.discovery.fragment.browseparent.ProductFragment;
import com.tokopedia.core.discovery.model.BrowseProductModel;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.ProductItem;

import java.util.List;

/**
 * Created by raditya.gumay on 18/03/2016.
 */
public interface FragmentBrowseProductView extends BaseView {
    /**
     * get data size based on Fragment TAG
     * {@link ProductFragment#TAG}
     * @param TAG
     * @return
     */
    int getDataSize(String TAG);
    void setupAdapter();
    void setupRecyclerView();
    void onCallProductServiceResult2(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    void onCallProductServiceLoadMore(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    boolean isLoading();
    int getStartIndexForQuery(String TAG);
    int getPage(String TAG);
    void savePaging(Bundle savedState);
    void restorePaging(Bundle savedState);

    void addTopAds(List<ProductItem> passProduct, int page, String tag);
    void addHotListHeader(ProductAdapter.HotListBannerModel hotListBannerModel);
    BrowseProductModel getDataModel();
    int FRAGMENT_ID = 812_192;
    int getTopAdsPaging();
}
