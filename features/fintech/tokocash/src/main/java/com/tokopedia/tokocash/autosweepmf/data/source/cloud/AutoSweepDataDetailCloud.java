package com.tokopedia.tokocash.autosweepmf.data.source.cloud;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepDetailMapperEntity;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.data.source.AutoSweepDetailDataStore;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.api.AutoSweepApi;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class AutoSweepDataDetailCloud implements AutoSweepDetailDataStore {
    private AutoSweepApi mApi;
    private AutoSweepDetailMapperEntity mMapper;
    private Context mContext;

    @Inject
    public AutoSweepDataDetailCloud(AutoSweepApi api, AutoSweepDetailMapperEntity mapper, Context context) {
        this.mApi = api;
        this.mMapper = mapper;
        this.mContext = context;
    }

    @Override
    public Observable<AutoSweepDetailDomain> autoSweepDetail() {
        return mApi.getAutoSweepDetail(GraphqlHelper.loadRawString(mContext.getResources(), R.raw.gql_autosweepmf_detail)).map(new Func1<Response<GraphqlResponse<ResponseAutoSweepDetail>>, AutoSweepDetailDomain>() {
            @Override
            public AutoSweepDetailDomain call(Response<GraphqlResponse<ResponseAutoSweepDetail>> response) {
                if (response.isSuccessful()) {
                    return mMapper.transform(response.body().getData().getMfAutoSweepDetail());
                } else {
                    throw new RuntimeException(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                    );
                }
            }
        });
    }
}
