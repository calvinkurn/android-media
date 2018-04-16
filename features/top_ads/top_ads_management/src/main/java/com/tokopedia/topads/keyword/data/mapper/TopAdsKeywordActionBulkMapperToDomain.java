package com.tokopedia.topads.keyword.data.mapper;

import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public class TopAdsKeywordActionBulkMapperToDomain implements Func1<PageDataResponse<DataBulkKeyword>, Boolean> {
    @Inject
    public TopAdsKeywordActionBulkMapperToDomain() {
    }

    @Override
    public Boolean call(PageDataResponse<DataBulkKeyword> dataBulkKeywordPageDataResponse) {
        if(dataBulkKeywordPageDataResponse != null && dataBulkKeywordPageDataResponse.getData()!= null){
            return true;
        }else{
            return false;
        }
    }
}
