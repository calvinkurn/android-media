package com.tokopedia.search.result.presentation.presenter.subscriber;

import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;

public class RefreshCatalogSubscriber extends SearchCatalogSubscriber {

    public RefreshCatalogSubscriber(CatalogListSectionContract.View view) {
        super(view);
    }

    @Override
    public void onNext(SearchCatalogModel domainModel) {
        view.successRefreshCatalog(mappingCatalogViewModel(domainModel));
        view.renderShareURL(domainModel.shareURL);
        view.setHasNextPage(isHasNextPage(domainModel.paging.uriNext));
        if (!isHasNextPage(domainModel.paging.uriNext)) {
            view.unSetTopAdsEndlessListener();
        }
    }
}
