package com.tokopedia.oms.domain.postusecase;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.OmsRepositoryData;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.domain.OmsRepository;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import javax.inject.Inject;
import rx.Observable;

public class PostVerifyCartUseCase extends UseCase<VerifyCartResponse> {

    private final OmsRepositoryData omsRepository;

    @Inject
    public PostVerifyCartUseCase(OmsRepositoryData omsRepository) {
        this.omsRepository = omsRepository;
    }

    @Override
    public Observable<VerifyCartResponse> createObservable(RequestParams requestParams) {
        JsonObject requestBody = (JsonObject) requestParams.getObject(Utils.Constants.CHECKOUTDATA);
        return omsRepository.verifyCard(requestBody);
    }
}
