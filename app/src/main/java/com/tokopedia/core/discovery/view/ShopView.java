package com.tokopedia.core.discovery.view;

import com.tokopedia.core.discovery.adapter.browseparent.BrowseShopAdapter;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public interface ShopView extends BaseView {
    void setupRecyclerView();
    void initAdapter();
    void onCallProductServiceLoadMore(List<BrowseShopAdapter.ShopModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    boolean isLoading();
    int getStartIndexForQuery(String TAG);
    int getPage(String TAG);
    int getDataSize();
    void setLoading(boolean isLoading);
    void setDynamicFilterAtrribute(DynamicFilterModel.Data filterAtrribute, int activeTab);
}
