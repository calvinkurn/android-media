package com.tokopedia.discovery.autocomplete.repository;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.search.domain.interactor.SearchMapper;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.List;

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

    public Observable<List<SearchData>> getUniverseAutoComplete(TKPDMapParam<String, Object> param) {
        return browseApi.getUniverseAutoCompleteV5(param)
                .doOnNext(new Action1<Response<String>>() {
                    @Override
                    public void call(Response<String> response) {
                        int tenMinute = 600000;
                        new GlobalCacheManager()
                                .setKey(TkpdCache.Key.UNIVERSEARCH)
                                .setCacheDuration(tenMinute)
                                .setValue(response.body())
                                .store();
                    }
                })
                .map(autoCompleteMapper);
    }

    public Observable<Response<Void>> deleteRecentSearch(TKPDMapParam<String, Object> parameters) {
        return browseApi.deleteRecentSearch(parameters);
    }
}
