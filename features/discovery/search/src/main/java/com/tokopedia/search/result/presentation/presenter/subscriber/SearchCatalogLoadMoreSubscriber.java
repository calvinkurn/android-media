package com.tokopedia.search.result.presentation.presenter.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class SearchCatalogLoadMoreSubscriber extends Subscriber<SearchCatalogModel> {

    private final CatalogListSectionContract.View view;

    public SearchCatalogLoadMoreSubscriber(CatalogListSectionContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof MessageErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof RuntimeException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof IOException) {
            view.renderRetryInit();
        } else {
            view.renderUnknown();
            e.printStackTrace();
        }
        view.hideRefreshLayout();
    }

    @Override
    public void onNext(SearchCatalogModel searchCatalogModel) {
        view.renderNextListView(mappingCatalogViewModel(searchCatalogModel));
        view.setHasNextPage(isHasNextPage(searchCatalogModel.paging.uriNext));
        if (!isHasNextPage(searchCatalogModel.paging.uriNext)) {
            view.unSetTopAdsEndlessListener();
        }
    }

    protected boolean isHasNextPage(String uriNext) {
        return uriNext != null && !uriNext.isEmpty();
    }

    protected List<Visitable> mappingCatalogViewModel(SearchCatalogModel domain) {
        List<Visitable> list = new ArrayList<>();
        for (SearchCatalogModel.Catalog item : domain.catalogList) {
            CatalogViewModel model = new CatalogViewModel();
            model.setID(item.catalogId);
            model.setName(item.catalogName);
            model.setDesc(item.catalogDescription);
            model.setImage(item.catalogImage);
            model.setImage300(item.catalogImage300);
            model.setPrice(item.catalogPrice);
            model.setProductCounter(item.catalogCountProduct);
            model.setURL(item.catalogUri);
            list.add(model);
        }
        return list;
    }
}
