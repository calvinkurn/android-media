package com.tokopedia.search.result.presentation.presenter.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;
import com.tokopedia.search.result.presentation.model.CatalogHeaderViewModel;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class SearchCatalogSubscriber extends Subscriber<SearchCatalogModel> {

    protected CatalogListSectionContract.View view;
    private boolean dataIsNull = false;

    public SearchCatalogSubscriber(CatalogListSectionContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        view.setTopAdsEndlessListener();
        view.showRefreshLayout();
    }

    @Override
    public void onNext(SearchCatalogModel searchCatalogModel) {
        if(searchCatalogModel == null) {
            dataIsNull = true;
            view.renderRetryInit();
            return;
        }

        view.renderListView(mappingCatalogViewModel(searchCatalogModel));
        view.renderShareURL(searchCatalogModel.shareURL);
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

        if (domain.catalogList == null || domain.catalogList.isEmpty()) {
            return list;
        }

        list.add(new CatalogHeaderViewModel());
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

    @Override
    public void onError(Throwable e) {
        if(e == null) {
            view.renderUnknown();
        }
        else {
            e.printStackTrace();

            if (e instanceof MessageErrorException || e instanceof RuntimeException) {
                view.renderErrorView(e.getMessage());
            } else if (e instanceof IOException) {
                view.renderRetryInit();
            } else {
                view.renderUnknown();
            }
        }

        view.hideRefreshLayout();
    }

    @Override
    public void onCompleted() {
        if(!dataIsNull) view.getDynamicFilter();
        view.hideRefreshLayout();
    }
}
