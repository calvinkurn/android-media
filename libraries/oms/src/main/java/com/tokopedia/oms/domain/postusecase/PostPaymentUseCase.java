package com.tokopedia.oms.domain.postusecase;

import com.google.gson.JsonObject;
import com.tokopedia.oms.data.OmsRepositoryData;
import com.tokopedia.oms.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.oms.domain.OmsRepository;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;


public class PostPaymentUseCase extends UseCase<CheckoutResponse> {

    private final OmsRepositoryData omsRepository;

    @Inject
    public PostPaymentUseCase(OmsRepositoryData omsRepository) {
        super();
        this.omsRepository = omsRepository;
    }

    @Override
    public Observable<CheckoutResponse> createObservable(RequestParams requestParams) {

        JsonObject requestBody = (JsonObject) requestParams.getObject(Utils.Constants.CHECKOUTDATA);

//        Cart verfiedCart = (Cart) requestParams.getObject("verfiedcart");
//        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(verfiedCart));
//        JsonObject requestBody = jsonElement.getAsJsonObject();
        return omsRepository.checkoutCart(requestBody);
    }
}
