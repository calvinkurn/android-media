package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.checkout.domain.datamodel.ResetAndShipmentFormCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ResetCartData;
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
public class ResetCartGetShipmentFormUseCase extends UseCase<ResetAndShipmentFormCartData> {

    public static final String PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_FORM
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_FORM";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final IShipmentMapper shipmentMapper;

    @Inject
    public ResetCartGetShipmentFormUseCase(ICartRepository cartRepository, ICartMapper cartMapper,
                                           IShipmentMapper shipmentMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<ResetAndShipmentFormCartData> createObservable(RequestParams requestParams) {

        final TKPDMapParam<String, String> paramReset = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART);

        final TKPDMapParam<String, String> paramGetShipment = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_FORM);

        return Observable.just(new ResetAndShipmentFormCartData())
                .flatMap(new Func1<ResetAndShipmentFormCartData, Observable<ResetAndShipmentFormCartData>>() {
                    @Override
                    public Observable<ResetAndShipmentFormCartData> call(
                            final ResetAndShipmentFormCartData resetAndRefreshCartListData
                    ) {
                        return cartRepository.resetCart(paramReset)
                                .map(new Func1<ResetCartDataResponse, ResetAndShipmentFormCartData>() {
                                    @Override
                                    public ResetAndShipmentFormCartData call(ResetCartDataResponse resetCartDataResponse) {
                                        ResetCartData resetCartData = cartMapper.convertToResetCartData(resetCartDataResponse);
                                        resetAndRefreshCartListData.setResetCartData(resetCartData);
                                        if (!resetCartData.isSuccess()) {
                                            throw new ResponseCartApiErrorException(
                                                    TkpdBaseURL.Cart.PATH_RESET_CART,
                                                    0,
                                                    ""
                                            );
                                        }
                                        return resetAndRefreshCartListData;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<ResetAndShipmentFormCartData, Observable<ResetAndShipmentFormCartData>>() {
                    @Override
                    public Observable<ResetAndShipmentFormCartData> call(
                            final ResetAndShipmentFormCartData resetAndShipmentFormCartData
                    ) {
                        return cartRepository.getShipmentAddressForm(paramGetShipment)
                                .map(new Func1<ShipmentAddressFormDataResponse, ResetAndShipmentFormCartData>() {
                                    @Override
                                    public ResetAndShipmentFormCartData call(
                                            ShipmentAddressFormDataResponse shipmentAddressFormDataResponse
                                    ) {
                                        CartShipmentAddressFormData cartShipmentAddressFormData =
                                                shipmentMapper.convertToShipmentAddressFormData(
                                                        shipmentAddressFormDataResponse
                                                );
                                        resetAndShipmentFormCartData.setCartShipmentAddressFormData(
                                                cartShipmentAddressFormData
                                        );
                                        if (cartShipmentAddressFormData.isError()) {
                                            throw new ResponseCartApiErrorException(
                                                    TkpdBaseURL.Cart.PATH_SHIPMENT_ADDRESS_FORM_DIRECT,
                                                    cartShipmentAddressFormData.getErrorCode(),
                                                    cartShipmentAddressFormData.getErrorMessage()
                                            );
                                        }
                                        return resetAndShipmentFormCartData;
                                    }
                                });

                    }
                });
    }
}
