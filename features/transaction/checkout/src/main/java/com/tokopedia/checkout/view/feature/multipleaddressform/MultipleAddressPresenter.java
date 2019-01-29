package com.tokopedia.checkout.view.feature.multipleaddressform;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartMultipleAddressListUseCase;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transactiondata.apiservice.CartHttpErrorException;
import com.tokopedia.transactiondata.apiservice.CartResponseDataNullException;
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressPresenter implements IMultipleAddressPresenter {

    private final ChangeShippingAddressUseCase changeShippingAddressUseCase;
    private final GetCartMultipleAddressListUseCase getCartMultipleAddressListUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;

    private CartListData cartListData;

    private IMultipleAddressView view;

    public MultipleAddressPresenter(IMultipleAddressView view,
                                    GetCartMultipleAddressListUseCase getCartMultipleAddressListUseCase,
                                    ChangeShippingAddressUseCase changeShippingAddressUseCase,
                                    CartApiRequestParamGenerator cartApiRequestParamGenerator) {
        this.changeShippingAddressUseCase = changeShippingAddressUseCase;
        this.getCartMultipleAddressListUseCase = getCartMultipleAddressListUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.view = view;
    }

    @Override
    public CartListData getCartListData() {
        return cartListData;
    }

    @Override
    public void setCartListData(CartListData cartListData) {
        this.cartListData = cartListData;
    }

    @Override
    public void processGetCartList(String cartIds) {
        view.showInitialLoading();

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList(cartIds))
        );

        getCartMultipleAddressListUseCase.execute(requestParams, new Subscriber<CartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideInitialLoading();
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    view.showError(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    view.showError(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof CartResponseErrorException) {
                    view.showError(e.getMessage());
                } else if (e instanceof CartResponseDataNullException) {
                    view.showError(e.getMessage());
                } else if (e instanceof CartHttpErrorException) {
                    view.showError(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.showError(e.getMessage());
                } else {
                    view.showError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartListData cartListData) {
                view.hideInitialLoading();
                if (cartListData != null && cartListData.getShopGroupDataList().size() > 0) {
                    MultipleAddressPresenter.this.cartListData = cartListData;
                    view.renderCartData(cartListData);
                } else {
                    view.navigateToCartList();
                }
            }
        });

    }

    @Override
    public void sendData(Context context, List<MultipleAddressAdapterData> dataList) {
        view.showLoading();
        JsonArray dataArray = new JsonArray();
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j < dataList.get(i).getItemListData().size(); j++) {
                DataChangeAddressRequest request = new DataChangeAddressRequest();
                MultipleAddressItemData itemData = dataList.get(i).getItemListData().get(j);
                request.setCartId(Integer.parseInt(itemData.getCartId()));
                request.setProductId(Integer.parseInt(itemData.getProductId()));
                request.setAddressId(Integer.parseInt(itemData.getRecipientAddressModel().getId()));
                request.setNotes(itemData.getProductNotes());
                request.setQuantity(Integer.parseInt(itemData.getProductQty()));
                JsonElement cartData = new JsonParser().parse(new Gson().toJson(request));
                dataArray.add(cartData);
            }
        }

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("carts", dataArray.toString());
        RequestParams requestParam = RequestParams.create();

        TKPDMapParam<String, String> authParam = AuthUtil.generateParamsNetwork(
                view.getActivityContext(), param,
                SessionHandler.getLoginID(view.getActivityContext()),
                GCMHandler.getRegistrationId(view.getActivityContext()));

        requestParam.putAllString(authParam);

        changeShippingAddressUseCase.execute(
                requestParam,
                addMultipleAddressSubscriber());
    }

    @Override
    public List<MultipleAddressAdapterData> initiateMultipleAddressAdapterData(
            CartListData cartListData, RecipientAddressModel recipientAddressModel) {

        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (ShopGroupData shopGroupData : cartListData.getShopGroupDataList()) {
            for (CartItemHolderData cartItemHolderData : shopGroupData.getCartItemDataList()) {
                cartItemDataList.add(cartItemHolderData.getCartItemData());
            }
        }
        List<MultipleAddressAdapterData> adapterModels = new ArrayList<>();
        for (int i = 0; i < cartItemDataList.size(); i++) {
            MultipleAddressAdapterData addressAdapterData = new MultipleAddressAdapterData();
            addressAdapterData.setItemListData(
                    generateinitialItemData(
                            i,
                            recipientAddressModel,
                            cartItemDataList.get(i).getOriginData(),
                            cartItemDataList.get(i).getUpdatedData(),
                            cartItemDataList.get(i).getErrorData())
            );
            addressAdapterData.setProductImageUrl(
                    cartItemDataList.get(i).getOriginData().getProductImage()
            );
            addressAdapterData.setProductName(
                    cartItemDataList.get(i).getOriginData().getProductName()
            );
            addressAdapterData.setProductPrice(
                    cartItemDataList.get(i).getOriginData().getPriceFormatted()
            );
            addressAdapterData.setPreOrder(cartItemDataList.get(i).getOriginData().isPreOrder());
            addressAdapterData.setPreOrderInfo(cartItemDataList.get(i).getOriginData().getPreOrderInfo());
            addressAdapterData.setFreeReturn(cartItemDataList.get(i).getOriginData().isFreeReturn());
            addressAdapterData.setCashBack(cartItemDataList.get(i).getOriginData().isCashBack());
            addressAdapterData.setCashBackInfo(cartItemDataList.get(i).getOriginData().getCashBackInfo());
            addressAdapterData.setSenderName(cartItemDataList.get(i).getOriginData().getShopName());
            addressAdapterData.setOfficialStore(cartItemDataList.get(i).getOriginData().isOfficialStore());
            addressAdapterData.setGoldMerchant(cartItemDataList.get(i).getOriginData().isGoldMerchant());
            adapterModels.add(addressAdapterData);
        }
        return adapterModels;
    }

    private List<MultipleAddressItemData> generateinitialItemData(
            int cartPosition,
            RecipientAddressModel shipmentRecipientModel,
            CartItemData.OriginData originData,
            CartItemData.UpdatedData updatedData,
            CartItemData.MessageErrorData messageErrorData) {

        List<MultipleAddressItemData> initialItemData = new ArrayList<>();
        MultipleAddressItemData addressData = new MultipleAddressItemData();
        addressData.setRecipientAddressModel(shipmentRecipientModel);
        addressData.setCartPosition(cartPosition);
        addressData.setAddressPosition(0);
        addressData.setCartId(String.valueOf(originData.getCartId()));
        addressData.setParentId(originData.getParentId());
        addressData.setProductId(originData.getProductId());
        addressData.setProductQty(String.valueOf(updatedData.getQuantity()));
        addressData.setProductWeightFmt(String.valueOf(originData.getWeightFormatted()));
        addressData.setProductNotes(updatedData.getRemark());
        addressData.setMaxQuantity(originData.getInvenageValue() != 0 ? originData.getInvenageValue() : updatedData.getMaxQuantity());
        addressData.setMinQuantity(originData.getMinimalQtyOrder());
        addressData.setErrorCheckoutPriceLimit(messageErrorData.getErrorCheckoutPriceLimit());
        addressData.setErrorFieldBetween(messageErrorData.getErrorFieldBetween());
        addressData.setErrorFieldMaxChar(messageErrorData.getErrorFieldMaxChar());
        addressData.setErrorProductAvailableStock(messageErrorData.getErrorProductAvailableStock());
        addressData.setErrorProductAvailableStockDetail(messageErrorData.getErrorProductAvailableStockDetail());
        addressData.setErrorProductMaxQuantity(messageErrorData.getErrorProductMaxQuantity());
        addressData.setErrorProductMinQuantity(messageErrorData.getErrorProductMinQuantity());
        addressData.setMaxRemark(updatedData.getMaxCharRemark());
        initialItemData.add(addressData);
        return initialItemData;
    }

    @Override
    public void onUnsubscribe() {
        changeShippingAddressUseCase.unsubscribe();
    }

    private Subscriber<SetShippingAddressData> addMultipleAddressSubscriber() {
        return new Subscriber<SetShippingAddressData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideLoading();
                if (e instanceof UnknownHostException) {
                    view.showError(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    view.showError(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof CartResponseErrorException) {
                    view.showError(e.getMessage());
                } else if (e instanceof CartResponseDataNullException) {
                    view.showError(e.getMessage());
                } else if (e instanceof CartHttpErrorException) {
                    view.showError(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.showError(e.getMessage());
                } else {
                    view.showError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(SetShippingAddressData setShippingAddressData) {
                view.hideLoading();
                if (setShippingAddressData.isSuccess()) {
                    view.successMakeShipmentData();
                } else {
                    if (setShippingAddressData.getMessages() != null && setShippingAddressData.getMessages().size() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String errorMessage : setShippingAddressData.getMessages()) {
                            stringBuilder.append(errorMessage).append(" ");
                        }
                        view.showError(stringBuilder.toString());
                    } else {
                        view.showError(null);
                    }
                }
            }
        };
    }

    private TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return originParams == null
                ?
                AuthUtil.generateParamsNetwork(
                        view.getActivityContext(), SessionHandler.getLoginID(view.getActivityContext()),
                        GCMHandler.getRegistrationId(view.getActivityContext())
                )
                :
                AuthUtil.generateParamsNetwork(
                        view.getActivityContext(), originParams,
                        SessionHandler.getLoginID(view.getActivityContext()),
                        GCMHandler.getRegistrationId(view.getActivityContext())
                );
    }

}
