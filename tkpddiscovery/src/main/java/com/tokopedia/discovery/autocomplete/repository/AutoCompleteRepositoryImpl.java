package com.tokopedia.discovery.autocomplete.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

public class AutoCompleteRepositoryImpl implements AutoCompleteRepository {

    private final AutoCompleteDataSource autoCompleteDataSource;

    public AutoCompleteRepositoryImpl(AutoCompleteDataSource autoCompleteDataSource) {
        this.autoCompleteDataSource = autoCompleteDataSource;
    }

    @Override
    public Observable<List<SearchData>> getSearchData(TKPDMapParam<String, Object> parameters) {
        return autoCompleteDataSource.getUniverseAutoComplete(parameters);
    }

    @Override
    public Observable<Response<Void>> deleteRecentSearch(TKPDMapParam<String, Object> parameters) {
        return autoCompleteDataSource.deleteRecentSearch(parameters);
    }
}
