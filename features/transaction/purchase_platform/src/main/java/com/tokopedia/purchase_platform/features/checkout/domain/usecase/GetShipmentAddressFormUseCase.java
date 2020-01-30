package com.tokopedia.purchase_platform.features.checkout.domain.usecase;

import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.features.checkout.data.repository.ICheckoutRepository;
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class GetShipmentAddressFormUseCase extends UseCase<CartShipmentAddressFormData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS";
    public static final String PARAM_SKIP_ONBOARDING_UPDATE_STATE = "so";

    private final ICheckoutRepository checkoutRepository;
    private final IShipmentMapper shipmentMapper;

    @Inject
    public GetShipmentAddressFormUseCase(ICheckoutRepository checkoutRepository, IShipmentMapper shipmentMapper) {
        this.checkoutRepository = checkoutRepository;
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<CartShipmentAddressFormData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS);
        return checkoutRepository.getShipmentAddressForm(param)
                .map(shipmentMapper::convertToShipmentAddressFormData);
    }
}
