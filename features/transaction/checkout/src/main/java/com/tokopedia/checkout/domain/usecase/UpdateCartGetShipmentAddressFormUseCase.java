package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateToSingleAddressShipmentData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class UpdateCartGetShipmentAddressFormUseCase extends UseCase<UpdateToSingleAddressShipmentData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final IShipmentMapper shipmentMapper;

    @Inject
    public UpdateCartGetShipmentAddressFormUseCase(ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<UpdateToSingleAddressShipmentData> createObservable(RequestParams requestParams) {
        final TKPDMapParam<String, String> paramUpdate = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        final TKPDMapParam<String, String> paramGetShipment = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS);
        return Observable.just(new UpdateToSingleAddressShipmentData())
                .flatMap(new Func1<UpdateToSingleAddressShipmentData, Observable<UpdateToSingleAddressShipmentData>>() {
                    @Override
                    public Observable<UpdateToSingleAddressShipmentData> call(final UpdateToSingleAddressShipmentData updateCartListData) {
                        return cartRepository.updateCartData(paramUpdate)
                                .map(new Func1<UpdateCartDataResponse, UpdateToSingleAddressShipmentData>() {
                                    @Override
                                    public UpdateToSingleAddressShipmentData call(UpdateCartDataResponse updateCartDataResponse) {
                                        UpdateCartData updateCartData =
                                                cartMapper.convertToUpdateCartData(updateCartDataResponse);
                                        updateCartListData.setUpdateCartData(updateCartData);
                                        if (!updateCartData.isSuccess()) {
                                            throw new ResponseCartApiErrorException(
                                                    TkpdBaseURL.Cart.PATH_UPDATE_CART,
                                                    0,
                                                    updateCartData.getMessage()

                                            );
                                        }
                                        return updateCartListData;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<UpdateToSingleAddressShipmentData, Observable<UpdateToSingleAddressShipmentData>>() {
                    @Override
                    public Observable<UpdateToSingleAddressShipmentData> call(final UpdateToSingleAddressShipmentData updateCartListData) {
                        return cartRepository.getShipmentAddressForm(paramGetShipment)
                                .map(new Func1<ShipmentAddressFormDataResponse, UpdateToSingleAddressShipmentData>() {
                                    @Override
                                    public UpdateToSingleAddressShipmentData call(ShipmentAddressFormDataResponse shipmentAddressFormDataResponse) {
                                        CartShipmentAddressFormData cartShipmentAddressFormData =
                                                shipmentMapper.convertToShipmentAddressFormData(
                                                        shipmentAddressFormDataResponse
                                                );
                                        updateCartListData.setShipmentAddressFormData(
                                                cartShipmentAddressFormData
                                        );
                                        if (cartShipmentAddressFormData.isError()) {
                                            throw new ResponseCartApiErrorException(
                                                    TkpdBaseURL.Cart.PATH_SHIPMENT_ADDRESS_FORM_DIRECT,
                                                    cartShipmentAddressFormData.getErrorCode(),
                                                    cartShipmentAddressFormData.getErrorMessage()
                                            );
                                        }
                                        return updateCartListData;
                                    }
                                });
                    }
                });
    }
}
