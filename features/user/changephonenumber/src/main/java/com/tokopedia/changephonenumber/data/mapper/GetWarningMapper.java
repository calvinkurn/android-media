package com.tokopedia.changephonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.changephonenumber.data.model.GetWarningData;
import com.tokopedia.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningMapper implements Func1<Response<TokopediaWsV4Response>, WarningViewModel> {
    @Inject
    public GetWarningMapper() {
    }

    @Override
    public WarningViewModel call(Response<TokopediaWsV4Response> tkpdResponseResponse) {
        WarningViewModel model = new WarningViewModel();
        if (tkpdResponseResponse.isSuccessful()) {
            if (!tkpdResponseResponse.body().isError() &&
                    (tkpdResponseResponse.body().getErrorMessageJoined().isEmpty() ||
                            tkpdResponseResponse.body().getErrorMessages() == null)
                    ) {
                GetWarningData data = tkpdResponseResponse.body().convertDataObj(
                        GetWarningData.class);
                model.setAction(data.getAction());
                model.setTokocash(data.getTokocash());
                model.setTokopediaBalance(data.getSaldo());
                model.setTokocashNumber(data.getTokocashNumber());
                model.setTokopediaBalanceNumber(data.getSaldoNumber());
                model.setWarningList(data.getWarning());
                model.setHasBankAccount(data.getHasBankAccount());
                model.setOvoEligible(data.isOvoEligible());
                if (!TextUtils.isEmpty(data.getRedirectUrl())) {
                    model.setUrlOvo(data.getRedirectUrl());
                }
            } else {
                if (tkpdResponseResponse.body().getErrorMessages() != null &&
                        !tkpdResponseResponse.body().getErrorMessages().isEmpty()) {
                    Observable.error(new MessageErrorException(tkpdResponseResponse.body().getErrorMessageJoined()));
                } else {
                    throw new RuntimeException("");
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(tkpdResponseResponse.code()));
        }

        return model;
    }
}