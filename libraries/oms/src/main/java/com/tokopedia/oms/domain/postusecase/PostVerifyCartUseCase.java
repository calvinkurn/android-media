package com.tokopedia.oms.domain.postusecase;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.OmsRepositoryData;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyMyCartResponse;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class PostVerifyCartUseCase extends UseCase<VerifyMyCartResponse> {

    private final OmsRepositoryData omsRepository;

    @Inject
    public PostVerifyCartUseCase(OmsRepositoryData omsRepository) {
        this.omsRepository = omsRepository;
    }

    @Override
    public Observable<VerifyMyCartResponse> createObservable(RequestParams requestParams) {
        JsonObject requestBody = (JsonObject) requestParams.getObject(Utils.Constants.CHECKOUTDATA);
        boolean flag = requestParams.getBoolean("ispromocodecase", false);
        return omsRepository.verifyCard(requestBody, flag);
    }
}
