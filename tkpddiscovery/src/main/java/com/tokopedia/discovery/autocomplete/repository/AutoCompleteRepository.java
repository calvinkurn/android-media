package com.tokopedia.discovery.autocomplete.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

public interface AutoCompleteRepository {
    Observable<List<SearchData>> getSearchData(TKPDMapParam<String, Object> parameters);

    Observable<Response<Void>> deleteRecentSearch(TKPDMapParam<String, Object> parameters);
}
