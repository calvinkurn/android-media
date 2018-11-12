package com.tokopedia.discovery.autocomplete.repository;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.newdiscovery.network.BrowseApi;
import com.tokopedia.discovery.search.domain.interactor.SearchMapper;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchResponse;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public class AutoCompleteDataSource {

    private final BrowseApi browseApi;
    private final SearchMapper autoCompleteMapper;

    public AutoCompleteDataSource(BrowseApi browseApi,
                                  SearchMapper autoCompleteMapper) {
        this.browseApi = browseApi;
        this.autoCompleteMapper = autoCompleteMapper;
    }

    public Observable<List<SearchData>> getUniverseAutoComplete(HashMap<String, Object> param) {
        return browseApi.getUniverseAutoCompleteV8(param)
                .debounce(300, TimeUnit.MILLISECONDS)
                .doOnNext(new Action1<Response<SearchResponse>>() {
                    @Override
                    public void call(Response<SearchResponse> response) {
                        int tenMinute = 600000;
                        Gson gson = new Gson();
                        new GlobalCacheManager()
                                .setKey(TkpdCache.Key.UNIVERSEARCH)
                                .setCacheDuration(tenMinute)
                                .setValue(gson.toJson(response.body()))
                                .store();
                    }

                })
                .map(autoCompleteMapper);
    }

    public Observable<Response<Void>> deleteRecentSearch(HashMap<String, Object> parameters) {
        return browseApi.deleteRecentSearch(parameters);
    }
}
