package com.tokopedia.autocomplete.repository;

import com.tokopedia.autocomplete.domain.interactor.SearchMapper;
import com.tokopedia.autocomplete.domain.model.SearchData;
import com.tokopedia.autocomplete.network.AutocompleteCache;
import com.tokopedia.autocomplete.network.BrowseApi;
import com.tokopedia.cachemanager.CacheManager;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;

public class AutoCompleteDataSource {

    private final BrowseApi browseApi;
    private final SearchMapper autoCompleteMapper;
    private final CacheManager cacheManager;

    public AutoCompleteDataSource(BrowseApi browseApi,
                                  SearchMapper autoCompleteMapper,
                                  CacheManager cacheManager) {
        this.browseApi = browseApi;
        this.autoCompleteMapper = autoCompleteMapper;
        this.cacheManager = cacheManager;
    }

    public Observable<List<SearchData>> getUniverseAutoComplete(HashMap<String, Object> param) {
        return browseApi.getUniverseAutoComplete(param)
                .debounce(300, TimeUnit.MILLISECONDS)
                .doOnNext(response -> {
                    int tenMinute = 600000;
                    cacheManager.put(AutocompleteCache.Key.UNIVERSEARCH, response.body(), tenMinute);
                })
                .map(autoCompleteMapper);
    }

    public Observable<Response<Void>> deleteRecentSearch(HashMap<String, Object> parameters) {
        return browseApi.deleteRecentSearch(parameters);
    }
}
