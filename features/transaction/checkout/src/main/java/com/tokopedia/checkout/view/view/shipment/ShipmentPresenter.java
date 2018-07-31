package com.tokopedia.checkout.view.view.shipment;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShipProd;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.toppay.ThanksTopPayData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCheckoutButtonModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceActionField;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCheckout;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceProductCartMapData;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ProductDataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ShopProductCheckoutRequest;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public class ShipmentPresenter extends BaseDaggerPresenter<ShipmentContract.View>
        implements ShipmentContract.Presenter {

    private final CheckoutUseCase checkoutUseCase;
    private final CompositeSubscription compositeSubscription;
    private final GetThanksToppayUseCase getThanksToppayUseCase;
    private final CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase;
    private final GetShipmentAddressFormUseCase getShipmentAddressFormUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private final EditAddressUseCase editAddressUseCase;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;
    private final ChangeShippingAddressUseCase changeShippingAddressUseCase;

    private CartItemPromoHolderData cartItemPromoHolderData;
    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private RecipientAddressModel recipientAddressModel;
    private PromoCodeAppliedData promoCodeAppliedData;
    private CartPromoSuggestion cartPromoSuggestion;
    private ShipmentCostModel shipmentCostModel;
    private ShipmentCheckoutButtonModel shipmentCheckoutButtonModel;
    private ShipmentDonationModel shipmentDonationModel;

    private List<DataCheckoutRequest> dataCheckoutRequestList;
    private List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestDataList;
    private List<DataChangeAddressRequest> changeAddressRequestList;
    private CheckoutData checkoutData;
    private boolean partialCheckout;

    @Inject
    public ShipmentPresenter(CompositeSubscription compositeSubscription,
                             CheckoutUseCase checkoutUseCase,
                             GetThanksToppayUseCase getThanksToppayUseCase,
                             CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                             GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                             CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                             EditAddressUseCase editAddressUseCase,
                             CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                             ChangeShippingAddressUseCase changeShippingAddressUseCase) {
        this.compositeSubscription = compositeSubscription;
        this.checkoutUseCase = checkoutUseCase;
        this.getThanksToppayUseCase = getThanksToppayUseCase;
        this.checkPromoCodeCartShipmentUseCase = checkPromoCodeCartShipmentUseCase;
        this.getShipmentAddressFormUseCase = getShipmentAddressFormUseCase;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
        this.editAddressUseCase = editAddressUseCase;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
        this.changeShippingAddressUseCase = changeShippingAddressUseCase;
    }

    @Override
    public void attachView(ShipmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
    }

    @Override
    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
    }

    @Override
    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
    }

    @Override
    public List<ShipmentCartItemModel> getShipmentCartItemModelList() {
        return shipmentCartItemModelList;
    }

    @Override
    public void setShipmentCartItemModelList(List<ShipmentCartItemModel> recipientCartItemList) {
        this.shipmentCartItemModelList = recipientCartItemList;
    }

    @Override
    public PromoCodeAppliedData getPromoCodeAppliedData() {
        return promoCodeAppliedData;
    }

    @Override
    public void setPromoCodeAppliedData(PromoCodeAppliedData promoCodeAppliedData) {
        this.promoCodeAppliedData = promoCodeAppliedData;
    }

    @Override
    public CartPromoSuggestion getCartPromoSuggestion() {
        return cartPromoSuggestion;
    }

    @Override
    public void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        this.cartPromoSuggestion = cartPromoSuggestion;
    }

    @Override
    public CheckoutData getCheckoutData() {
        return checkoutData;
    }

    @Override
    public void setCheckoutData(CheckoutData checkoutData) {
        this.checkoutData = checkoutData;
    }

    @Override
    public void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList) {
        this.dataCheckoutRequestList = dataCheckoutRequestList;
    }

    @Override
    public void setPromoCodeCartShipmentRequestData(
            List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestData
    ) {
        this.promoCodeCartShipmentRequestDataList = promoCodeCartShipmentRequestData;
    }

    @Override
    public void setDataChangeAddressRequestList(List<DataChangeAddressRequest> dataChangeAddressRequestList) {
        this.changeAddressRequestList = dataChangeAddressRequestList;
    }

    @Override
    public ShipmentCostModel getShipmentCostModel() {
        return shipmentCostModel;
    }

    @Override
    public void setShipmentCostModel(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;
    }

    @Override
    public ShipmentCheckoutButtonModel getShipmentCheckoutButtonModel() {
        return shipmentCheckoutButtonModel;
    }

    @Override
    public void setShipmentCheckoutButtonModel(ShipmentCheckoutButtonModel shipmentCheckoutButtonModel) {
        this.shipmentCheckoutButtonModel = shipmentCheckoutButtonModel;
    }

    @Override
    public ShipmentDonationModel getShipmentDonationModel() {
        return shipmentDonationModel;
    }

    @Override
    public void setShipmentDonationModel(ShipmentDonationModel shipmentDonationModel) {
        this.shipmentDonationModel = shipmentDonationModel;
    }

    @Override
    public CartItemPromoHolderData getCartItemPromoHolderData() {
        return cartItemPromoHolderData;
    }

    @Override
    public void setCartItemPromoHolderData(CartItemPromoHolderData cartItemPromoHolderData) {
        this.cartItemPromoHolderData = cartItemPromoHolderData;
    }

    @Override
    public void processInitialLoadCheckoutPage(boolean isFromMultipleAddress) {
        if (isFromMultipleAddress) {
            getView().showLoading();
        } else {
            getView().showInitialLoading();
        }
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                getGeneratedAuthParamNetwork(paramGetShipmentForm));

        compositeSubscription.add(
                getShipmentAddressFormUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CartShipmentAddressFormData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                if (isFromMultipleAddress) {
                                    getView().hideLoading();
                                } else {
                                    getView().hideInitialLoading();
                                }
                                getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                            }

                            @Override
                            public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                                if (isFromMultipleAddress) {
                                    getView().hideLoading();
                                } else {
                                    getView().hideInitialLoading();
                                }

                                if (cartShipmentAddressFormData.isError()) {
                                    getView().showToastError(cartShipmentAddressFormData.getErrorMessage());
                                } else {
                                    if (cartShipmentAddressFormData.getGroupAddress() == null || cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
                                        getView().renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
                                    } else {
                                        initializePresenterData(cartShipmentAddressFormData);
                                        getView().renderCheckoutPage(!isFromMultipleAddress);
                                    }
                                }
                            }
                        })
        );
    }

    private void initializePresenterData(CartShipmentAddressFormData cartShipmentAddressFormData) {
        if (!cartShipmentAddressFormData.isMultiple()) {
            setRecipientAddressModel(getView().getShipmentDataConverter()
                    .getRecipientAddressModel(cartShipmentAddressFormData));
        } else {
            setRecipientAddressModel(null);
        }

        if (cartShipmentAddressFormData.getDonation() != null) {
            setShipmentDonationModel(getView().getShipmentDataConverter().getShipmentDonationModel(cartShipmentAddressFormData));
        } else {
            setShipmentDonationModel(null);
        }

        setShipmentCartItemModelList(getView()
                .getShipmentDataConverter().getShipmentItems(cartShipmentAddressFormData));
    }

    @Override
    public void processReloadCheckoutPageFromMultipleAddress(CartItemPromoHolderData oldCartItemPromoHolderData,
                                                             CartPromoSuggestion oldCartPromoSuggestion,
                                                             RecipientAddressModel oldRecipientAddressModel,
                                                             ArrayList<ShipmentCartItemModel> oldShipmentCartItemModels,
                                                             ShipmentCostModel oldShipmentCostModel,
                                                             ShipmentDonationModel oldShipmentDonationModel,
                                                             ShipmentCheckoutButtonModel oldShipmentCheckoutButtonModel) {
        getView().showLoading();
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                getGeneratedAuthParamNetwork(paramGetShipmentForm));

        compositeSubscription.add(
                getShipmentAddressFormUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CartShipmentAddressFormData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().hideLoading();
                                getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                            }

                            @Override
                            public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                                getView().hideLoading();

                                if (cartShipmentAddressFormData.isError()) {
                                    getView().showToastError(cartShipmentAddressFormData.getErrorMessage());
                                } else {
                                    if (cartShipmentAddressFormData.getGroupAddress() == null || cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
                                        getView().renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
                                    } else {
                                        RecipientAddressModel newRecipientAddressModel =
                                                getView().getShipmentDataConverter().getRecipientAddressModel(cartShipmentAddressFormData);
                                        List<ShipmentCartItemModel> shipmentCartItemModelList =
                                                getView().getShipmentDataConverter().getShipmentItems(cartShipmentAddressFormData);
                                        if (checkAddressHasChanged(oldRecipientAddressModel, newRecipientAddressModel) ||
                                                checkShipmentItemHasChanged(oldShipmentCartItemModels, shipmentCartItemModelList)) {
                                            initializePresenterData(cartShipmentAddressFormData);
                                            getView().renderDataChangedFromMultipleAddress(cartShipmentAddressFormData);
                                        }
                                    }
                                }
                            }
                        })
        );
    }

    private boolean checkAddressHasChanged(RecipientAddressModel oldModel, RecipientAddressModel newModel) {
        return !oldModel.equals(newModel);
    }

    private boolean checkShipmentItemHasChanged(List<ShipmentCartItemModel> oldShipmentCartItemModelList,
                                                List<ShipmentCartItemModel> newShipmentCartItemModelList) {
        if (oldShipmentCartItemModelList.size() != newShipmentCartItemModelList.size()) {
            return true;
        } else {
            List<ShipmentCartItemModel> equalShipmentCartItemModelList = new ArrayList<>();
            for (ShipmentCartItemModel oldShipmentCartItemModel : oldShipmentCartItemModelList) {
                for (ShipmentCartItemModel newShipmentCartItemModel : newShipmentCartItemModelList) {
                    if (isSameCartObject(oldShipmentCartItemModel, newShipmentCartItemModel) &&
                            oldShipmentCartItemModel.equals(newShipmentCartItemModel) &&
                            oldShipmentCartItemModel.getCartItemModels().size() == newShipmentCartItemModel.getCartItemModels().size() &&
                            oldShipmentCartItemModel.getShopShipmentList().size() == newShipmentCartItemModel.getShopShipmentList().size()) {
                        for (ShopShipment oldShopShipment : oldShipmentCartItemModel.getShopShipmentList()) {
                            for (ShopShipment newShopShipment : newShipmentCartItemModel.getShopShipmentList()) {
                                if (oldShopShipment.getShipId() == newShopShipment.getShipId() &&
                                        oldShopShipment.getShipProds().size() == newShopShipment.getShipProds().size() &&
                                        !equalShipmentCartItemModelList.contains(oldShipmentCartItemModel)) {
                                    equalShipmentCartItemModelList.add(oldShipmentCartItemModel);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (equalShipmentCartItemModelList.size() != newShipmentCartItemModelList.size()) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void processReloadCheckoutPageBecauseOfError() {
        getView().showLoading();
        com.tokopedia.abstraction.common.utils.TKPDMapParam<String, String> paramGetShipmentForm = new com.tokopedia.abstraction.common.utils.TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                getGeneratedAuthParamNetwork(paramGetShipmentForm));

        compositeSubscription.add(
                getShipmentAddressFormUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<CartShipmentAddressFormData>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                        getView().hideLoading();
                                    }

                                    @Override
                                    public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                                        if (cartShipmentAddressFormData.getGroupAddress() == null ||
                                                cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
                                            getView().renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
                                        } else {
                                            prepareDataAfterReloadCheckoutPage(cartShipmentAddressFormData);
                                        }
                                    }
                                }
                        )
        );
    }

    private void prepareDataAfterReloadCheckoutPage(CartShipmentAddressFormData cartShipmentAddressFormData) {
        List<ShipmentCartItemModel> newShipmentCartItemModelList = getView().getShipmentDataConverter().getShipmentItems(
                cartShipmentAddressFormData
        );
        List<ShipmentCartItemModel> oldShipmentCartItemModelList = getShipmentCartItemModelList();
        for (ShipmentCartItemModel oldShipmentCartItemModel : oldShipmentCartItemModelList) {
            for (ShipmentCartItemModel newShipmentCartItemModel : newShipmentCartItemModelList) {
                if (isSameCartObject(oldShipmentCartItemModel, newShipmentCartItemModel)) {
                    oldShipmentCartItemModel.setError(newShipmentCartItemModel.isError());
                    oldShipmentCartItemModel.setAllItemError(newShipmentCartItemModel.isAllItemError());
                    oldShipmentCartItemModel.setErrorMessage(newShipmentCartItemModel.getErrorMessage());
                    for (CartItemModel newCartItemModel : newShipmentCartItemModel.getCartItemModels()) {
                        for (CartItemModel oldCartItemModel : oldShipmentCartItemModel.getCartItemModels()) {
                            if (newCartItemModel.getProductId() == oldCartItemModel.getProductId()) {
                                oldCartItemModel.setError(newCartItemModel.isError());
                                oldCartItemModel.setErrorMessage(newCartItemModel.getErrorMessage());
                                if (oldShipmentCartItemModel.isAllItemError()) {
                                    oldCartItemModel.setError(oldShipmentCartItemModel.isError());
                                }
                            }
                        }
                    }
                    boolean breakFromNewShipmentCartItemModelLoop = false;
                    for (ShopShipment shopShipment : newShipmentCartItemModel.getShipmentCartData().getShopShipments()) {
                        if (oldShipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                                oldShipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId() == shopShipment.getShipId()) {
                            boolean breakFromShopShipmentLoop = false;
                            for (ShipProd shipProd : shopShipment.getShipProds()) {
                                if (oldShipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId() == shipProd.getShipProdId()) {
                                    newShipmentCartItemModel.setSelectedShipmentDetailData(oldShipmentCartItemModel.getSelectedShipmentDetailData());
                                    newShipmentCartItemModel.setStateDropshipperHasError(oldShipmentCartItemModel.isStateDropshipperHasError());
                                    newShipmentCartItemModel.setStateDropshipperDetailExpanded(oldShipmentCartItemModel.isStateDropshipperDetailExpanded());
                                    breakFromShopShipmentLoop = true;
                                    break;
                                }
                            }
                            if (breakFromShopShipmentLoop) {
                                breakFromNewShipmentCartItemModelLoop = true;
                                break;
                            }
                        }
                    }
                    if (breakFromNewShipmentCartItemModelLoop) {
                        break;
                    }
                }
            }
        }

        for (ShipmentCartItemModel oldShipmentCartItemModel : oldShipmentCartItemModelList) {
            for (ShipmentCartItemModel newShipmentCartItemModel : newShipmentCartItemModelList) {
                if (isSameCartObject(oldShipmentCartItemModel, newShipmentCartItemModel) && newShipmentCartItemModel.getSelectedShipmentDetailData() == null) {
                    oldShipmentCartItemModel.setSelectedShipmentDetailData(null);
                    oldShipmentCartItemModel.setShipmentCartData(newShipmentCartItemModel.getShipmentCartData());
                }
            }
        }

        getView().hideLoading();
        getView().renderErrorDataHasChangedAfterCheckout(oldShipmentCartItemModelList);
    }

    @Override
    public void processCheckShipmentPrepareCheckout() {
        boolean isNeedToRemoveErrorProduct = isNeedToremoveErrorShopProduct();
        if (partialCheckout || isNeedToRemoveErrorProduct) {
            processCheckout();
        } else {
            getView().showLoading();
            com.tokopedia.abstraction.common.utils.TKPDMapParam<String, String> paramGetShipmentForm = new com.tokopedia.abstraction.common.utils.TKPDMapParam<>();
            paramGetShipmentForm.put("lang", "id");

            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                    getGeneratedAuthParamNetwork(paramGetShipmentForm));

            compositeSubscription.add(
                    getShipmentAddressFormUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(
                                    new Subscriber<CartShipmentAddressFormData>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                            getView().hideLoading();
                                            getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                        }

                                        @Override
                                        public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                                            if (cartShipmentAddressFormData.getGroupAddress() == null ||
                                                    cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
                                                getView().renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
                                            } else {
                                                boolean isEnableCheckout = true;
                                                for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                                                    if (groupAddress.isError() || groupAddress.isWarning()) {
                                                        isEnableCheckout = false;
                                                        break;
                                                    }
                                                    for (GroupShop groupShop : groupAddress.getGroupShop()) {
                                                        if (groupShop.isError() || groupShop.isWarning()) {
                                                            isEnableCheckout = false;
                                                            break;
                                                        }
                                                        for (Product product : groupShop.getProducts()) {
                                                            if (product.isError() || !TextUtils.isEmpty(product.getErrorMessage())) {
                                                                isEnableCheckout = false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }

                                                if (isEnableCheckout) {
                                                    getView().renderCheckShipmentPrepareCheckoutSuccess();
                                                } else {
                                                    prepareDataAfterProcessShipmentPrepareCheckout(cartShipmentAddressFormData, isNeedToRemoveErrorProduct);
                                                }
                                            }
                                        }
                                    }
                            )
            );
        }
    }

    private void prepareDataAfterProcessShipmentPrepareCheckout(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                                boolean isNeedToRemoveErrorProduct) {
        List<ShipmentCartItemModel> newShipmentCartItemModelList = getView().getShipmentDataConverter().getShipmentItems(
                cartShipmentAddressFormData
        );
        List<ShipmentCartItemModel> oldShipmentCartItemModelList = getShipmentCartItemModelList();
        try {
            for (int i = 0; i < newShipmentCartItemModelList.size(); i++) {
                if (newShipmentCartItemModelList.get(i).isError()) {
                    oldShipmentCartItemModelList.get(i).setError(true);
                    oldShipmentCartItemModelList.get(i).setErrorMessage(newShipmentCartItemModelList.get(i).getErrorMessage());
                }
                for (int j = 0; j < newShipmentCartItemModelList.get(i).getCartItemModels().size(); j++) {
                    if (newShipmentCartItemModelList.get(i).isAllItemError()) {
                        oldShipmentCartItemModelList.get(i).getCartItemModels().get(j).setError(true);
                    } else {
                        if (newShipmentCartItemModelList.get(i).getCartItemModels().get(j).isError()) {
                            oldShipmentCartItemModelList.get(i).getCartItemModels().get(j).setError(true);
                            oldShipmentCartItemModelList.get(i).getCartItemModels().get(j).setErrorMessage(
                                    newShipmentCartItemModelList.get(i).getCartItemModels().get(j).getErrorMessage());
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        getView().hideLoading();
        getView().renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                cartShipmentAddressFormData, !isNeedToRemoveErrorProduct
        );
    }

    private TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return originParams == null
                ?
                AuthUtil.generateParamsNetwork(
                        getView().getActivityContext(), SessionHandler.getLoginID(getView().getActivityContext()),
                        GCMHandler.getRegistrationId(getView().getActivityContext())
                )
                :
                AuthUtil.generateParamsNetwork(
                        getView().getActivityContext(), originParams,
                        SessionHandler.getLoginID(getView().getActivityContext()),
                        GCMHandler.getRegistrationId(getView().getActivityContext())
                );
    }

    @Override
    public void processCheckout() {

        CheckoutRequest checkoutRequest = generateCheckoutRequest(
                promoCodeAppliedData != null && promoCodeAppliedData.getPromoCode() != null ?
                        promoCodeAppliedData.getPromoCode() : "",
                shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0
        );


        if (checkoutRequest != null) {
            getView().showLoading();
            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(CheckoutUseCase.PARAM_CARTS, checkoutRequest);
            compositeSubscription.add(
                    checkoutUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(getSubscriberCheckoutCart(checkoutRequest))
            );
        } else {
            getView().showToastError(getView().getActivityContext().getString(R.string.default_request_error_unknown));
        }
    }

    private boolean isNeedToremoveErrorShopProduct() {
        List<ShipmentCartItemModel> newShipmentCartItemModelList = new ArrayList<>();
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            List<CartItemModel> cartItemModels = new ArrayList<>(shipmentCartItemModel.getCartItemModels());
            newShipmentCartItemModelList.add(ShipmentCartItemModel.clone(shipmentCartItemModel, cartItemModels));
        }

        boolean cartListHasError = false;
        ArrayList<ShipmentCartItemModel> indexShopErrorList = new ArrayList<>();
        Map<ShipmentCartItemModel, List<CartItemModel>> indexShopItemErrorMap = new HashMap<>();
        for (int i = 0; i < newShipmentCartItemModelList.size(); i++) {
            if (newShipmentCartItemModelList.get(i).isAllItemError()) {
                cartListHasError = true;
                indexShopErrorList.add(newShipmentCartItemModelList.get(i));
            }
            if (newShipmentCartItemModelList.get(i).isError()) {
                List<CartItemModel> deletedCartItemModels = new ArrayList<>();
                for (int j = 0; j < newShipmentCartItemModelList.get(i).getCartItemModels().size(); j++) {
                    if (newShipmentCartItemModelList.get(i).getCartItemModels().get(j).isError()) {
                        cartListHasError = true;
                        deletedCartItemModels.add(newShipmentCartItemModelList.get(i).getCartItemModels().get(j));
                    }
                }
                indexShopItemErrorMap.put(newShipmentCartItemModelList.get(i), deletedCartItemModels);
                if (deletedCartItemModels.size() == newShipmentCartItemModelList.get(i).getCartItemModels().size()) {
                    indexShopErrorList.add(newShipmentCartItemModelList.get(i));
                }
            }
        }

        if (cartListHasError) {
            for (ShipmentCartItemModel oldShipmentCartItemModel : shipmentCartItemModelList) {
                for (ShipmentCartItemModel newShipmentCartItemModel : newShipmentCartItemModelList) {
                    if (isSameCartObject(oldShipmentCartItemModel, newShipmentCartItemModel)) {
                        newShipmentCartItemModel.setSelectedShipmentDetailData(oldShipmentCartItemModel.getSelectedShipmentDetailData());
                    }
                }
            }

            for (Map.Entry<ShipmentCartItemModel, List<CartItemModel>> entry : indexShopItemErrorMap.entrySet()) {
                ShipmentCartItemModel key = entry.getKey();
                List<CartItemModel> value = entry.getValue();
                for (CartItemModel cartItemModel : value) {
                    int index = newShipmentCartItemModelList.indexOf(key);
                    newShipmentCartItemModelList.get(index).getCartItemModels().remove(cartItemModel);
                }
            }

            for (ShipmentCartItemModel indexShopError : indexShopErrorList) {
                newShipmentCartItemModelList.remove(indexShopError);
            }

            dataCheckoutRequestList = getView().generateNewCheckoutRequest(newShipmentCartItemModelList);
            partialCheckout = true;
            return true;
        }
        return false;
    }

    private boolean isSameCartObject(ShipmentCartItemModel oldShipmentCartItemModel,
                                     ShipmentCartItemModel newShipmentCartItemModel) {

        if (oldShipmentCartItemModel.getRecipientAddressModel() != null && newShipmentCartItemModel.getRecipientAddressModel() != null) {
            if (!oldShipmentCartItemModel.getRecipientAddressModel().getId().equals(newShipmentCartItemModel.getRecipientAddressModel().getId()) ||
                    oldShipmentCartItemModel.getRecipientAddressModel().getLatitude() != newShipmentCartItemModel.getRecipientAddressModel().getLatitude() ||
                    oldShipmentCartItemModel.getRecipientAddressModel().getLongitude() != newShipmentCartItemModel.getRecipientAddressModel().getLongitude() ||
                    !oldShipmentCartItemModel.getRecipientAddressModel().getPostalCode().equalsIgnoreCase(newShipmentCartItemModel.getRecipientAddressModel().getPostalCode()) ||
                    !oldShipmentCartItemModel.getRecipientAddressModel().getDestinationDistrictId().equals(newShipmentCartItemModel.getRecipientAddressModel().getDestinationDistrictId())) {
                return false;
            }
        }
        return oldShipmentCartItemModel.getShopId() == newShipmentCartItemModel.getShopId() &&
                oldShipmentCartItemModel.getCartItemModels().size() == newShipmentCartItemModel.getCartItemModels().size() &&
                oldShipmentCartItemModel.getCartItemModels().get(0).getProductId() == newShipmentCartItemModel.getCartItemModels().get(0).getProductId();
    }

    @Override
    public void processVerifyPayment(String transactionId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(GetThanksToppayUseCase.PARAM_TRANSACTION_ID, transactionId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(param);
        compositeSubscription.add(
                getThanksToppayUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(getSubscriberThanksTopPay())
        );
    }

    @Override
    public void checkPromoShipment() {
        CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest =
                new CheckPromoCodeCartShipmentRequest.Builder()
                        .promoCode(promoCodeAppliedData.getPromoCode())
                        .data(promoCodeCartShipmentRequestDataList)
                        .build();

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(CheckPromoCodeCartShipmentUseCase.PARAM_CARTS,
                new Gson().toJson(checkPromoCodeCartShipmentRequest));
        param.put(CheckPromoCodeCartShipmentUseCase.PARAM_PROMO_LANG,
                CheckPromoCodeCartShipmentUseCase.PARAM_VALUE_LANG_ID);
        param.put(CheckPromoCodeCartShipmentUseCase.PARAM_PROMO_SUGGESTED,
                CheckPromoCodeCartShipmentUseCase.PARAM_VALUE_NOT_SUGGESTED);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartShipmentUseCase.PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO,
                getGeneratedAuthParamNetwork(param));

        compositeSubscription.add(
                checkPromoCodeCartShipmentUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<PromoCodeCartShipmentData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().renderErrorCheckPromoShipmentData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                            }

                            @Override
                            public void onNext(PromoCodeCartShipmentData promoCodeCartShipmentData) {
                                if (!promoCodeCartShipmentData.isError()) {
                                    getView().renderCheckPromoShipmentDataSuccess(promoCodeCartShipmentData);
                                } else {
                                    getView().renderErrorCheckPromoShipmentData(
                                            promoCodeCartShipmentData.getErrorMessage()
                                    );
                                }
                            }
                        })
        );
    }

    @NonNull
    private Subscriber<ThanksTopPayData> getSubscriberThanksTopPay() {
        return new Subscriber<ThanksTopPayData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideLoading();
                getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
            }

            @Override
            public void onNext(ThanksTopPayData thanksTopPayData) {
                getView().hideLoading();
                getView().renderThanksTopPaySuccess(getView().getActivityContext().getString(R.string.message_payment_success));
            }
        };
    }

    @NonNull
    private Subscriber<CheckoutData> getSubscriberCheckoutCart(CheckoutRequest checkoutRequest) {
        return new Subscriber<CheckoutData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().sendAnalyticsChoosePaymentMethodFailed();
                processReloadCheckoutPageBecauseOfError();
            }

            @Override
            public void onNext(CheckoutData checkoutData) {
                getView().hideLoading();
                if (!checkoutData.isError()) {
                    getView().sendAnalyticsChoosePaymentMethodSuccess();
                    getView().sendAnalyticsCheckoutStep2(generateCheckoutAnalyticsStep2DataLayer(checkoutRequest));
                    getView().renderCheckoutCartSuccess(checkoutData);
                } else {
                    getView().sendAnalyticsChoosePaymentMethodFailed();
                    getView().renderCheckoutCartError(checkoutData.getErrorMessage());
                }
            }
        };
    }

    private Map<String, Object> generateCheckoutAnalyticsStep2DataLayer(CheckoutRequest checkoutRequest) {

        Map<String, Object> checkoutMapData = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setStep(EnhancedECommerceActionField.STEP_2);
        enhancedECommerceActionField.setOption(EnhancedECommerceActionField.OPTION_CLICK_PAYMENT_OPTION_BUTTON);

        EnhancedECommerceCheckout enhancedECommerceCheckout = new EnhancedECommerceCheckout();
        for (DataCheckoutRequest dataCheckoutRequest : checkoutRequest.data) {
            for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.shopProducts) {
                for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.productData) {
                    EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                            new EnhancedECommerceProductCartMapData();
                    enhancedECommerceProductCartMapData.setProductName(productDataCheckoutRequest.getProductName());
                    enhancedECommerceProductCartMapData.setProductID(String.valueOf(productDataCheckoutRequest.getProductId()));
                    enhancedECommerceProductCartMapData.setPrice(productDataCheckoutRequest.getProductPrice());
                    enhancedECommerceProductCartMapData.setBrand(productDataCheckoutRequest.getProductBrand());
                    enhancedECommerceProductCartMapData.setCategory(productDataCheckoutRequest.getProductCategory());
                    enhancedECommerceProductCartMapData.setVariant(productDataCheckoutRequest.getProductVariant());
                    enhancedECommerceProductCartMapData.setQty(productDataCheckoutRequest.getProductQty());
                    enhancedECommerceProductCartMapData.setShopId(String.valueOf(shopProductCheckoutRequest.getShopId()));
                    enhancedECommerceProductCartMapData.setShopName(shopProductCheckoutRequest.getShopName());
                    enhancedECommerceProductCartMapData.setShopType(shopProductCheckoutRequest.getShopType());
                    enhancedECommerceProductCartMapData.setCategoryId(productDataCheckoutRequest.getProductCategoryId());
                    enhancedECommerceProductCartMapData.setAttribution(productDataCheckoutRequest.getProductAttribution());
                    enhancedECommerceCheckout.addProduct(enhancedECommerceProductCartMapData.getProduct());
                }
            }
        }
        enhancedECommerceCheckout.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        enhancedECommerceCheckout.setActionField(enhancedECommerceActionField.getActionFieldMap());

        checkoutMapData.put(EnhancedECommerceCheckout.KEY_CHECKOUT, enhancedECommerceCheckout.getCheckoutMap());

        return checkoutMapData;
    }

    @Override
    public void processCheckPromoCodeFromSuggestedPromo(String promoCode) {
        getView().showLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("promo_code", promoCode);
        param.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO,
                getGeneratedAuthParamNetwork(param));
        compositeSubscription.add(
                checkPromoCodeCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<PromoCodeCartListData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().hideLoading();
                                getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                            }

                            @Override
                            public void onNext(PromoCodeCartListData promoCodeCartListData) {
                                getView().hideLoading();
                                if (!promoCodeCartListData.isError()) {
                                    getView().renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                                } else {
                                    getView().renderErrorCheckPromoCodeFromSuggestedPromo(promoCodeCartListData.getErrorMessage());
                                }
                            }
                        })
        );
    }

    private CheckoutRequest generateCheckoutRequest(String promoCode, int isDonation) {
        if (dataCheckoutRequestList == null) {
            getView().showToastError(getView().getActivityContext().getString(R.string.default_request_error_unknown_short));
            return null;
        }

        return new CheckoutRequest.Builder()
                .promoCode(promoCode)
                .isDonation(isDonation)
                .data(dataCheckoutRequestList)
                .build();
    }

    @Override
    public void editAddressPinpoint(final String latitude, final String longitude,
                                    ShipmentCartItemModel shipmentCartItemModel,
                                    LocationPass locationPass) {
        RequestParams requestParams = generateEditAddressRequestParams(shipmentCartItemModel, latitude, longitude);
        compositeSubscription.add(
                editAddressUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                            }

                            @Override
                            public void onNext(String stringResponse) {
                                JSONObject response = null;
                                String messageError = null;
                                boolean statusSuccess;
                                try {
                                    response = new JSONObject(stringResponse);
                                    int statusCode = response.getJSONObject(EditAddressUseCase.RESPONSE_DATA)
                                            .getInt(EditAddressUseCase.RESPONSE_IS_SUCCESS);
                                    statusSuccess = statusCode == 1;
                                    if (!statusSuccess) {
                                        messageError = response.getJSONArray("message_error").getString(0);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    statusSuccess = false;
                                }

                                if (response != null && statusSuccess) {
                                    if (recipientAddressModel != null) {
                                        recipientAddressModel.setLatitude(Double.parseDouble(latitude));
                                        recipientAddressModel.setLongitude(Double.parseDouble(longitude));
                                    } else {
                                        shipmentCartItemModel.getRecipientAddressModel().setLatitude(Double.parseDouble(latitude));
                                        shipmentCartItemModel.getRecipientAddressModel().setLongitude(Double.parseDouble(longitude));
                                    }
                                    getView().renderEditAddressSuccess(latitude, longitude);
                                } else {
                                    if (TextUtils.isEmpty(messageError)) {
                                        messageError = getView().getActivityContext().getString(R.string.default_request_error_unknown);
                                    }
                                    getView().navigateToSetPinpoint(messageError, locationPass);
                                }
                            }
                        })
        );

    }

    @NonNull
    private RequestParams generateEditAddressRequestParams(ShipmentCartItemModel shipmentCartItemModel,
                                                           String addressLatitude, String addressLongitude) {
        TKPDMapParam<String, String> params = getGeneratedAuthParamNetwork(null);

        String addressId = null;
        String addressName = null;
        String addressStreet = null;
        String postalCode = null;
        String districtId = null;
        String cityId = null;
        String provinceId = null;
        String latitude = null;
        String longitude = null;
        String receiverName = null;
        String receiverPhone = null;

        if (recipientAddressModel == null && shipmentCartItemModel != null) {
            addressId = shipmentCartItemModel.getRecipientAddressModel().getId();
            addressName = shipmentCartItemModel.getRecipientAddressModel().getAddressName();
            addressStreet = shipmentCartItemModel.getRecipientAddressModel().getStreet();
            postalCode = shipmentCartItemModel.getRecipientAddressModel().getPostalCode();
            districtId = shipmentCartItemModel.getRecipientAddressModel().getDestinationDistrictId();
            cityId = shipmentCartItemModel.getRecipientAddressModel().getCityId();
            provinceId = shipmentCartItemModel.getRecipientAddressModel().getProvinceId();
            receiverName = shipmentCartItemModel.getRecipientAddressModel().getRecipientName();
            receiverPhone = shipmentCartItemModel.getRecipientAddressModel().getRecipientPhoneNumber();
        } else {
            addressId = recipientAddressModel.getId();
            addressName = recipientAddressModel.getAddressName();
            addressStreet = recipientAddressModel.getStreet();
            postalCode = recipientAddressModel.getPostalCode();
            districtId = recipientAddressModel.getDestinationDistrictId();
            cityId = recipientAddressModel.getCityId();
            provinceId = recipientAddressModel.getProvinceId();
            receiverName = recipientAddressModel.getRecipientName();
            receiverPhone = recipientAddressModel.getRecipientPhoneNumber();
        }
        latitude = addressLatitude;
        longitude = addressLongitude;

        params.put(EditAddressUseCase.Params.ADDRESS_ID, addressId);
        params.put(EditAddressUseCase.Params.ADDRESS_NAME, addressName);
        params.put(EditAddressUseCase.Params.ADDRESS_STREET, addressStreet);
        params.put(EditAddressUseCase.Params.POSTAL_CODE, postalCode);
        params.put(EditAddressUseCase.Params.DISTRICT_ID, districtId);
        params.put(EditAddressUseCase.Params.CITY_ID, cityId);
        params.put(EditAddressUseCase.Params.PROVINCE_ID, provinceId);
        params.put(EditAddressUseCase.Params.LATITUDE, latitude);
        params.put(EditAddressUseCase.Params.LONGITUDE, longitude);
        params.put(EditAddressUseCase.Params.RECEIVER_NAME, receiverName);
        params.put(EditAddressUseCase.Params.RECEIVER_PHONE, receiverPhone);

        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(params);

        return requestParams;
    }

    @Override
    public void cancelAutoApplyCoupon() {
        compositeSubscription.add(
                cancelAutoApplyCouponUseCase.createObservable(RequestParams.create())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                            }

                            @Override
                            public void onNext(String stringResponse) {
                                boolean resultSuccess = false;
                                try {
                                    JSONObject jsonObject = new JSONObject(stringResponse);
                                    resultSuccess = jsonObject.getJSONObject(CancelAutoApplyCouponUseCase.RESPONSE_DATA)
                                            .getBoolean(CancelAutoApplyCouponUseCase.RESPONSE_SUCCESS);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (resultSuccess) {
                                    getView().renderCancelAutoApplyCouponSuccess();
                                } else {
                                    getView().showToastError(getView().getActivityContext().getString(R.string.default_request_error_unknown));
                                }

                            }
                        })
        );
    }

    @Override
    public void changeShippingAddress(final RecipientAddressModel recipientAddressModel) {
        getView().showLoading();
        String changeAddressRequestJsonString = new Gson().toJson(changeAddressRequestList);

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("carts", changeAddressRequestJsonString);
        RequestParams requestParam = RequestParams.create();

        TKPDMapParam<String, String> authParam = AuthUtil.generateParamsNetwork(
                getView().getActivityContext(), param,
                SessionHandler.getLoginID(getView().getActivityContext()),
                GCMHandler.getRegistrationId(getView().getActivityContext()));

        requestParam.putAllString(authParam);

        compositeSubscription.add(
                changeShippingAddressUseCase.createObservable(requestParam)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<SetShippingAddressData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideLoading();
                                e.printStackTrace();
                                getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                            }

                            @Override
                            public void onNext(SetShippingAddressData setShippingAddressData) {
                                getView().hideLoading();
                                if (setShippingAddressData.isSuccess()) {
                                    getView().showToastNormal(getView().getActivityContext().getString(R.string.label_change_address_success));
                                    getView().renderChangeAddressSuccess(recipientAddressModel);
                                } else {
                                    if (setShippingAddressData.getMessages() != null &&
                                            setShippingAddressData.getMessages().size() > 0) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        for (String errorMessage : setShippingAddressData.getMessages()) {
                                            stringBuilder.append(errorMessage).append(" ");
                                        }
                                        getView().showToastError(stringBuilder.toString());
                                    } else {
                                        getView().showToastError(getView().getActivityContext().getString(R.string.label_change_address_failed));
                                    }
                                }
                            }
                        })
        );
    }
}
