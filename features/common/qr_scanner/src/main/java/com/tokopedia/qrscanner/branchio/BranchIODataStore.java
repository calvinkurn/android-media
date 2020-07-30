package com.tokopedia.qrscanner.branchio;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.qrscanner.branchio.api.BranchIOAPI;
import com.tokopedia.qrscanner.branchio.api.BranchIOURL;

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
