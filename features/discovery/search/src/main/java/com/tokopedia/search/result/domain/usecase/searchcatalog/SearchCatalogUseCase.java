package com.tokopedia.search.result.domain.usecase.searchcatalog;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

final class SearchCatalogUseCase extends UseCase<SearchCatalogModel> {

    private Repository<SearchCatalogModel> searchCatalogModelRepository;

    SearchCatalogUseCase(Repository<SearchCatalogModel> searchCatalogModelRepository) {
        this.searchCatalogModelRepository = searchCatalogModelRepository;
    }

    @Override
    public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
        return searchCatalogModelRepository.query(requestParams.getParameters());
    }
}
