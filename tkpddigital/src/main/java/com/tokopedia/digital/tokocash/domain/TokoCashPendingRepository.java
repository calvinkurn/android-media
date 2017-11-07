package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.core.network.apiservices.tokocash.TokoCashCashBackService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.tokocash.entity.ResponseCashBack;
import com.tokopedia.digital.tokocash.mapper.ITokoCashMapper;
import com.tokopedia.digital.tokocash.model.CashBackData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 6/16/17. Tokopedia
 */

public class TokoCashPendingRepository implements ITokoCashPendingRepository {

    private final TokoCashCashBackService tokoCashCashBackService;
    private final ITokoCashMapper tokoCashMapper;

    public TokoCashPendingRepository(TokoCashCashBackService tokoCashCashBackService,
                                     ITokoCashMapper iTokoCashMapper) {
        this.tokoCashCashBackService = tokoCashCashBackService;
        this.tokoCashMapper = iTokoCashMapper;
    }

    @Override
    public Observable<CashBackData> getTokoCashPending() {
        TKPDMapParam<String, String> tokoCashPendingParams = new TKPDMapParam<>();
        tokoCashPendingParams.put("msisdn", SessionHandler.getPhoneNumber());
        return tokoCashCashBackService.getApi().getTokoCashPending(tokoCashPendingParams)
                .map(getFuncResponseToCashBackData());
    }

    private Func1<Response<TkpdDigitalResponse>, CashBackData>
    getFuncResponseToCashBackData() {
        return new Func1<Response<TkpdDigitalResponse>, CashBackData>() {
            @Override
            public CashBackData call(Response<TkpdDigitalResponse> tkpdDigitalResponse) {
                return tokoCashMapper.transformTokoCashCashbackData(tkpdDigitalResponse.body()
                        .convertDataObj(ResponseCashBack.class));
            }
        };
    }
}
