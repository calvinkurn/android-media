package com.tokopedia.product.manage.item.main.base.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.product.manage.item.common.util.ProductNetworkConstant;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.GenerateHost;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.GenerateHostModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 4/11/17.
 */

public class GenerateHostCloud {
    private final GenerateHostApi generateHostApi;
    private final Context context;

    @Inject
    public GenerateHostCloud(@ApplicationContext Context context, GenerateHostApi generateHostApi) {
        this.generateHostApi = generateHostApi;
        this.context = context;
    }

    public Observable<GenerateHost> generateHost() {
        return generateHostApi.generateHost(AuthUtil.generateParamsNetwork(context, getParamsGenerateHost()))
                .map(new Func1<Response<GenerateHostModel>, GenerateHostModel>() {
                    @Override
                    public GenerateHostModel call(Response<GenerateHostModel> generateHostModelResponse) {
                        return generateHostModelResponse.body();
                    }
                })
                .map(new GetGenerateHostModel());
    }


    public TKPDMapParam<String, String> getParamsGenerateHost() {
        TKPDMapParam<String, String> paramsGenerateHost = new TKPDMapParam<>();
        paramsGenerateHost.put(ProductNetworkConstant.SERVER_LANGUAGE, ProductNetworkConstant.GOLANG_VALUE);
        return paramsGenerateHost;
    }

    private class GetGenerateHostModel implements Func1<GenerateHostModel, GenerateHost> {

        @Override
        public GenerateHost call(GenerateHostModel generateHostModel) {
            return generateHostModel.getData().getGenerateHost();
        }
    }
}
