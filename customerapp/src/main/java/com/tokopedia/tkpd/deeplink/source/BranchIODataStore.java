package com.tokopedia.tkpd.deeplink.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.deeplink.source.api.BranchIOAPI;
import com.tokopedia.tkpd.deeplink.source.api.BranchIOURL;
import com.tokopedia.tkpd.deeplink.source.entity.BranchIOAndroidDeepLink;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public class BranchIODataStore {

    BranchIOAPI mBranchAPI;

    public BranchIODataStore(BranchIOAPI mAPI) {
        this.mBranchAPI = mAPI;
    }

    public Observable<BranchIOAndroidDeepLink> getCampaign(HashMap<String, Object> param) {
        return this.mBranchAPI.getCampaign(BranchIOURL.BranchIOURL,param).map(new Func1<Response<DataResponse<BranchIOAndroidDeepLink>>, BranchIOAndroidDeepLink>() {
            @Override
            public BranchIOAndroidDeepLink call(Response<DataResponse<BranchIOAndroidDeepLink>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }
}
