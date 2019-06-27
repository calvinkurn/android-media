package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class GetShipmentAddressFormUseCase extends UseCase<CartShipmentAddressFormData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS";
    public static final String PARAM_SKIP_ONBOARDING_UPDATE_STATE = "so";

    private final ICartRepository cartRepository;
    private final IShipmentMapper shipmentMapper;

    @Inject
    public GetShipmentAddressFormUseCase(ICartRepository cartRepository, IShipmentMapper shipmentMapper) {
        this.cartRepository = cartRepository;
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<CartShipmentAddressFormData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS);
        return cartRepository.getShipmentAddressForm(param)
                .map(new Func1<ShipmentAddressFormDataResponse, CartShipmentAddressFormData>() {
                    @Override
                    public CartShipmentAddressFormData call(ShipmentAddressFormDataResponse shipmentAddressFormDataResponse) {
                        return shipmentMapper.convertToShipmentAddressFormData(shipmentAddressFormDataResponse);
                    }
                });
    }
}
