package com.tokopedia.tkpd.discovery.view;

import com.tokopedia.tkpd.discovery.adapter.browseparent.BrowseCatalogAdapter;
import com.tokopedia.tkpd.discovery.model.BrowseCatalogModel;
import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.util.PagingHandler;

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
}
