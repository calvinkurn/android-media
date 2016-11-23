package com.tokopedia.core.discovery.view;

import com.tokopedia.core.discovery.adapter.browseparent.BrowseCatalogAdapter;
import com.tokopedia.core.discovery.model.BrowseCatalogModel;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public interface CatalogView extends BaseView {
    void setupRecyclerView();
    void initAdapter();
    void notifyChangeData(List<BrowseCatalogAdapter.CatalogModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    boolean isLoading();
    int getStartIndexForQuery(String TAG);
    int getPage(String TAG);
    int getDataSize();
    void onCallNetwork();
    BrowseCatalogModel getDataModel();
    void setDynamicFilterAtrribute(DynamicFilterModel.Data filterAtrribute, int activeTab);
}
