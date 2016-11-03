package com.tokopedia.tkpd.discovery.view;

import android.os.Bundle;

import com.tokopedia.tkpd.discovery.adapter.ProductAdapter;
import com.tokopedia.tkpd.discovery.fragment.browseparent.ProductFragment;
import com.tokopedia.tkpd.discovery.model.BrowseProductModel;
import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.util.PagingHandler;
import com.tokopedia.tkpd.var.ProductItem;

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
