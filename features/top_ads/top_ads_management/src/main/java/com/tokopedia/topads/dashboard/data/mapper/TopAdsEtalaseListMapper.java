package com.tokopedia.topads.dashboard.data.mapper;

import com.tokopedia.topads.dashboard.data.model.data.DataEtalase;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;
import com.tokopedia.seller.common.data.response.DataResponse;

import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsEtalaseListMapper implements Func1< Response<DataResponse<DataEtalase>>, List<Etalase>> {
    @Override
    public List<Etalase> call(Response<DataResponse<DataEtalase>> dataResponse) {
        return mappingResponse(dataResponse);
    }

    private List<Etalase> mappingResponse(Response<DataResponse<DataEtalase>> response) {
        if (response.isSuccessful() ){
            List<Etalase> etalaseList = response.body().getData().getList();
            if (etalaseList != null
                    && etalaseList.size() > 0){
                return etalaseList;
            }
        }
        return null;
    }

}
