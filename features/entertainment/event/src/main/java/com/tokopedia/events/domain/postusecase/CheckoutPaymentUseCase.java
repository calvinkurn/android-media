package com.tokopedia.events.domain.postusecase;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.Cart;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

import static com.tokopedia.events.view.utils.Utils.Constants.CHECKOUTDATA;
/**
 * Created by pranaymohapatra on 15/12/17.
 */

public class CheckoutPaymentUseCase extends UseCase<CheckoutResponse> {

    private final EventRepository eventRepository;

    @Inject
    public CheckoutPaymentUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<CheckoutResponse> createObservable(RequestParams requestParams) {
        Cart verfiedCart = (Cart) requestParams.getObject(CHECKOUTDATA);
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(verfiedCart));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return eventRepository.checkoutCart(requestBody);
    }
}
