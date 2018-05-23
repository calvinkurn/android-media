package com.tokopedia.imageuploader.data.source;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.imageuploader.data.entity.GenerateHost;
import com.tokopedia.imageuploader.data.entity.GenerateHostModel;
import com.tokopedia.imageuploader.data.entity.GetData;
import com.tokopedia.imageuploader.data.source.api.GenerateHostApi;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 4/11/17.
 */

public class GenerateHostCloud {
    private final GenerateHostApi generateHostApi;
    private UserSession userSession;

    public GenerateHostCloud(GenerateHostApi generateHostApi, UserSession userSession) {
        this.generateHostApi = generateHostApi;
        this.userSession = userSession;
    }

    public Observable<GenerateHost> generateHost() {
        return generateHostApi.generateHost(AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), getParamsGenerateHost()))
                .map(new GetData<GenerateHostModel>())
                .map(new GetGenerateHostModel());
    }


    public TKPDMapParam<String, String> getParamsGenerateHost() {
        TKPDMapParam<String, String> paramsGenerateHost = new TKPDMapParam<>();
        paramsGenerateHost.put("new_add", "2");
        return paramsGenerateHost;
    }

    private class GetGenerateHostModel implements Func1<GenerateHostModel, GenerateHost> {

        @Override
        public GenerateHost call(GenerateHostModel generateHostModel) {
            return generateHostModel.getData().getGenerateHost();
        }
    }
}
