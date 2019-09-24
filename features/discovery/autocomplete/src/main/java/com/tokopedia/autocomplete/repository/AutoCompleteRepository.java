package com.tokopedia.autocomplete.repository;

import com.tokopedia.autocomplete.domain.model.SearchData;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;

public interface AutoCompleteRepository {
    Observable<List<SearchData>> getSearchData(HashMap<String, Object> parameters);

    Observable<Response<Void>> deleteRecentSearch(HashMap<String, Object> parameters);
}
