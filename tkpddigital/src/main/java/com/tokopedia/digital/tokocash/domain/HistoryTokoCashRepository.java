package com.tokopedia.digital.tokocash.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.tokocash.WalletService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.tokocash.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.digital.tokocash.entity.OAuthInfoEntity;
import com.tokopedia.digital.tokocash.entity.ResponseHelpHistoryEntity;
import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;
import com.tokopedia.digital.tokocash.entity.WithdrawSaldoEntity;
import com.tokopedia.digital.tokocash.model.ParamsActionHistory;
import com.tokopedia.digital.utils.DeviceUtil;

import org.json.JSONException;
import org.json.JSONObject;

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

    private final WalletService walletService;

    private Gson gson;

    public HistoryTokoCashRepository(WalletService walletService) {
        this.walletService = walletService;
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
        return walletService.getApi().getHistoryTokocash(mapHistoryData)
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
        JSONObject requestHelpHistory = new JSONObject();
        try {
            requestHelpHistory.put("subject", subject);
            requestHelpHistory.put("message", message);
            requestHelpHistory.put("category", category);
            requestHelpHistory.put("transaction_id", transactionId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return walletService.getApi().postHelpHistory(requestHelpHistory)
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
        JSONObject paramsActionHistoryEntity = new JSONObject();
        try {
            paramsActionHistoryEntity.put("refund_id", paramsActionHistory.getRefundId());
            paramsActionHistoryEntity.put("refund_type", paramsActionHistory.getRefundType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return walletService.getApi().withdrawSaldoFromTokocash(url, paramsActionHistoryEntity)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<WithdrawSaldoEntity>>() {
                    @Override
                    public Observable<WithdrawSaldoEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return Observable.just(tkpdDigitalResponseResponse.body().convertDataObj(WithdrawSaldoEntity.class));
                    }
                });
    }

    @Override
    public Observable<OAuthInfoEntity> getOAuthInfo() {
        return walletService.getApi().getOAuthInfoAccount()
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<OAuthInfoEntity>>() {
                    @Override
                    public Observable<OAuthInfoEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return Observable.just(tkpdDigitalResponseResponse.body().convertDataObj(OAuthInfoEntity.class));
                    }
                });
    }

    @Override
    public Observable<Boolean> unlinkAccountTokoCash(String refreshToken, String identifier, String identifierType) {
        TKPDMapParam tkpdMapParam = new TKPDMapParam();
        tkpdMapParam.put("revoke_token", refreshToken);
        tkpdMapParam.put("identifier", identifier);
        tkpdMapParam.put("identifier_type", identifierType);
        return walletService.getApi().revokeAccessAccountTokoCash(tkpdMapParam)
                .map(new Func1<Response<String>, Boolean>() {
                    @Override
                    public Boolean call(Response<String> stringResponse) {
                        return stringResponse.isSuccessful();
                    }
                });
    }
}
