package com.tokopedia.tkpd.beranda.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.tkpd.beranda.data.mapper.TopPicksMapper;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;

import rx.Observable;


/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TopPicksDataSource {

    private final SearchApi searchApi;
    private final TopPicksMapper topPicksMapper;

    public TopPicksDataSource(SearchApi searchApi, TopPicksMapper topPicksMapper) {
        this.searchApi = searchApi;
        this.topPicksMapper = topPicksMapper;
    }

    public Observable<TopPicksResponseModel> getTopPicks(RequestParams requestParams){
        return searchApi.getTopPicks(requestParams.getParamsAllValueInString(),
                GlobalConfig.VERSION_NAME, "android").map(topPicksMapper);
    }
}
