package com.tokopedia.digital.tokocash.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.tokocash.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.digital.tokocash.entity.ParamsActionHistoryEntity;
import com.tokopedia.digital.tokocash.entity.ResponseHelpHistoryEntity;
import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;
import com.tokopedia.digital.tokocash.entity.WithdrawSaldoEntity;
import com.tokopedia.digital.tokocash.model.ParamsActionHistory;
import com.tokopedia.digital.tokocash.network.apiservice.HistoryTokoCashService;
import com.tokopedia.digital.tokocash.network.request.RequestHelpHistory;
import com.tokopedia.digital.utils.DeviceUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public class HistoryTokoCashRepository implements IHistoryTokoCashRepository {

    private final HistoryTokoCashService historyTokoCashService;

    private Gson gson;

    public HistoryTokoCashRepository(HistoryTokoCashService historyTokoCashService) {
        this.historyTokoCashService = historyTokoCashService;
        gson = new Gson();
    }

    @Override
    public Observable<TokoCashHistoryEntity> getTokoCashHistoryData(String type, String startDate,
                                                                    String endDate, int page) {
        Map<String, String> mapHistoryData = new HashMap<>();
        mapHistoryData.put("type", type);
        mapHistoryData.put("start_date", startDate);
        mapHistoryData.put("end_date", endDate);
        mapHistoryData.put("page", String.valueOf(page));
        return historyTokoCashService.getApi().getHistoryTokocash(mapHistoryData)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<TokoCashHistoryEntity>>() {
                    @Override
                    public Observable<TokoCashHistoryEntity> call(Response<TkpdDigitalResponse> response) {
                        return Observable
                                .just(response.body().convertDataObj(TokoCashHistoryEntity.class));
                    }
                });
    }

    @Override
    public Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData() {
        String helpHistoryList = DeviceUtil.loadJSONFromAsset("help_history_tokocash.json");
        return Observable.just(Arrays.asList((HelpHistoryTokoCashEntity[]) gson.fromJson(helpHistoryList,
                HelpHistoryTokoCashEntity[].class)));
    }

    @Override
    public Observable<ResponseHelpHistoryEntity> submitHelpHistory(String subject, String message, String category, String transactionId) {
        RequestHelpHistory requestHelpHistory = new RequestHelpHistory();
        requestHelpHistory.setSubject(subject);
        requestHelpHistory.setMessage(message);
        requestHelpHistory.setCategory(category);
        requestHelpHistory.setTransaction_id(transactionId);
        return historyTokoCashService.getApi().postHelpHistory(requestHelpHistory)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<ResponseHelpHistoryEntity>>() {
                    @Override
                    public Observable<ResponseHelpHistoryEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return Observable
                                .just(tkpdDigitalResponseResponse.body().convertDataObj(ResponseHelpHistoryEntity.class));
                    }
                });
    }

    @Override
    public Observable<WithdrawSaldoEntity> moveToSaldo(String url, ParamsActionHistory paramsActionHistory) {
        ParamsActionHistoryEntity paramsActionHistoryEntity = new ParamsActionHistoryEntity();
        paramsActionHistoryEntity.setRefundId(paramsActionHistory.getRefundId());
        paramsActionHistoryEntity.setRefundType(paramsActionHistory.getRefundType());
        return historyTokoCashService.getApi().withdrawSaldoFromTokocash(url, paramsActionHistoryEntity)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<WithdrawSaldoEntity>>() {
                    @Override
                    public Observable<WithdrawSaldoEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return Observable.just(tkpdDigitalResponseResponse.body().convertDataObj(WithdrawSaldoEntity.class));
                    }
                });
    }
}
