package com.tokopedia.purchase_platform.features.checkout.domain.usecase;

import com.tokopedia.purchase_platform.features.checkout.data.repository.ICheckoutRepository;
import com.tokopedia.purchase_platform.features.checkout.domain.model.saveshipmentstate.SaveShipmentStateData;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.SaveShipmentStateResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class SaveShipmentStateUseCase extends UseCase<SaveShipmentStateData> {

    public static final String PARAM_CART_DATA_OBJECT = "PARAM_CART_DATA_OBJECT";
    public static final String PARAM_CARTS = "carts";

    private final ICheckoutRepository checkoutRepository;

    @Inject
    public SaveShipmentStateUseCase(ICheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<SaveShipmentStateData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_CART_DATA_OBJECT);

        return checkoutRepository.saveShipmentState(param)
                .map(new Func1<SaveShipmentStateResponse, SaveShipmentStateData>() {
                    @Override
                    public SaveShipmentStateData call(SaveShipmentStateResponse saveShipmentStateResponse) {
                        SaveShipmentStateData saveShipmentStateData = new SaveShipmentStateData();
                        saveShipmentStateData.setSuccess(saveShipmentStateResponse.getSuccess() == 1);
                        saveShipmentStateData.setError(saveShipmentStateResponse.getError());
                        saveShipmentStateData.setMessage(saveShipmentStateResponse.getMessage());

                        return saveShipmentStateData;
                    }
                });
    }

}
