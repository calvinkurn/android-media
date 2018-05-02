package com.tokopedia.checkout.view.view.shipmentform;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.checkout.data.entity.request.DropshipDataCheckoutRequest;
import com.tokopedia.checkout.data.entity.request.ProductDataCheckoutRequest;
import com.tokopedia.checkout.data.entity.request.ShippingInfoCheckoutRequest;
import com.tokopedia.checkout.data.entity.request.ShopProductCheckoutRequest;
import com.tokopedia.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transaction.common.constant.PickupPointParamConstant;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressShipmentPresenter implements IMultipleAddressShipmentPresenter {

    private final IMultipleAddressShipmentView view;
    private final CompositeSubscription compositeSubscription;
    private final GetShipmentAddressFormUseCase getShipmentAddressFormUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;

    @Inject
    public MultipleAddressShipmentPresenter(IMultipleAddressShipmentView view,
                                            CompositeSubscription compositeSubscription,
                                            GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                            CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase) {
        this.view = view;
        this.compositeSubscription = compositeSubscription;
        this.getShipmentAddressFormUseCase = getShipmentAddressFormUseCase;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
    }

    @Override
    public CheckoutRequest generateCheckoutRequest(List<MultipleAddressShipmentAdapterData> shipmentData,
                                                   MultipleAddressPriceSummaryData priceData,
                                                   String promoCode) {
        CheckoutRequest.Builder checkoutRequest = new CheckoutRequest.Builder();
        List<DataCheckoutRequest> dataCheckoutRequests = new ArrayList<>();
        for (int i = 0; i < shipmentData.size(); i++) {
            MultipleAddressShipmentAdapterData currentShipmentAdapterData = shipmentData.get(i);
            ShipmentDetailData currentShipmentDetailData = currentShipmentAdapterData
                    .getSelectedShipmentDetailData();

            DataCheckoutRequest.Builder checkoutData = new DataCheckoutRequest.Builder();

            List<ProductDataCheckoutRequest> productDataCheckoutRequests = new ArrayList<>();
            ProductDataCheckoutRequest.Builder productDataCheckoutRequest =
                    new ProductDataCheckoutRequest.Builder();
            productDataCheckoutRequests.add(productDataCheckoutRequest
                    .productId(Integer.parseInt(
                            currentShipmentAdapterData.getItemData().getProductId())
                    ).build());

            ShopProductCheckoutRequest.Builder shopCheckoutBuilder;
            shopCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                    .productData(productDataCheckoutRequests)
                    .shippingInfo(setShippingInfoRequest(currentShipmentDetailData));
            if (currentShipmentDetailData.getUseDropshipper()) {
                shopCheckoutBuilder
                        .dropshipData(setDropshipDataCheckoutRequest(currentShipmentDetailData));
            }

            shopCheckoutBuilder
                    .fcancelPartial(
                            switchValue(currentShipmentDetailData.getUsePartialOrder())
                    );
            shopCheckoutBuilder
                    .finsurance(switchValue(currentShipmentDetailData.getUseInsurance()));
            shopCheckoutBuilder
                    .isPreorder(switchValue(currentShipmentAdapterData.isProductIsPreorder()));
            shopCheckoutBuilder
                    .isDropship(switchValue(currentShipmentDetailData.getUseDropshipper()));
            shopCheckoutBuilder.shopId(currentShipmentAdapterData.getShopId());

            List<ShopProductCheckoutRequest> shopCheckoutRequests = new ArrayList<>();
            shopCheckoutRequests.add(shopCheckoutBuilder.build());

            checkoutData.addressId(Integer
                    .parseInt(shipmentData.get(i).getItemData().getAddressId()))
                    .shopProducts(shopCheckoutRequests).build();
            dataCheckoutRequests.add(checkoutData.build());
        }
        checkoutRequest.isDonation(0)
                .promoCode(promoCode)
                .data(dataCheckoutRequests);
        return checkoutRequest.build();
    }

    private DropshipDataCheckoutRequest setDropshipDataCheckoutRequest(ShipmentDetailData data) {
        return new DropshipDataCheckoutRequest.Builder()
                .name(data.getDropshipperName())
                .telpNo(data.getDropshipperPhone()).build();
    }

    private ShippingInfoCheckoutRequest setShippingInfoRequest(ShipmentDetailData data) {
        return new ShippingInfoCheckoutRequest.Builder()
                .shippingId(data.getSelectedCourier().getShipperId())
                .spId(data.getSelectedCourier().getShipperProductId())
                .build();
    }

    @Override
    public List<MultipleAddressShipmentAdapterData> initiateAdapterData(CartShipmentAddressFormData data) {
        List<MultipleAddressShipmentAdapterData> adapterDataList = new ArrayList<>();
        for (int addressIndex = 0; addressIndex < data.getGroupAddress().size(); addressIndex++) {
            GroupAddress currentAddress = data.getGroupAddress().get(addressIndex);
            List<GroupShop> groupShopList = data.getGroupAddress().get(addressIndex).getGroupShop();
            for (int shopIndex = 0; shopIndex < groupShopList.size(); shopIndex++) {
                GroupShop currentGroupShop = groupShopList.get(shopIndex);
                List<Product> productList = currentGroupShop.getProducts();

                boolean isErrorGroupShop = currentGroupShop.isError();
                String errorMessageGroupShop = currentGroupShop.getErrorMessage();
                boolean isWarningGroupShop = currentGroupShop.isWarning();
                String warningMessageGroupShop = currentGroupShop.getWarningMessage();

                for (int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    MultipleAddressShipmentAdapterData adapterData =
                            new MultipleAddressShipmentAdapterData();
                    Product currentProduct = productList.get(productIndex);

                    adapterData.setError(isErrorGroupShop);
                    adapterData.setErrorMessage(errorMessageGroupShop);
                    adapterData.setWarning(isWarningGroupShop);
                    adapterData.setWarningMessage(warningMessageGroupShop);

                    adapterData.setInvoicePosition(adapterDataList.size());
                    adapterData.setShopId(currentGroupShop.getShop().getShopId());
                    adapterData.setProductName(currentProduct.getProductName());
                    adapterData.setProductPriceNumber(currentProduct.getProductPrice());
                    adapterData.setProductPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            currentProduct.getProductPrice(), true));
                    adapterData.setProductImageUrl(currentProduct.getProductImageSrc200Square());
                    adapterData.setSenderName(currentGroupShop.getShop().getShopName());
                    MultipleAddressItemData addressItemData = new MultipleAddressItemData();
                    addressItemData.setCartPosition(productIndex);
                    addressItemData.setAddressPosition(0);
                    addressItemData.setProductId(String.valueOf(currentProduct.getProductId()));
                    addressItemData.setProductWeight(currentProduct.getProductWeightFmt());
                    addressItemData.setProductRawWeight(currentProduct.getProductWeight());
                    addressItemData.setProductNotes(currentProduct.getProductNotes());
                    addressItemData.setProductQty(
                            String.valueOf(currentProduct.getProductQuantity())
                    );
                    addressItemData.setAddressId(
                            String.valueOf(currentAddress.getUserAddress().getAddressId())
                    );
                    addressItemData.setAddressTitle(currentAddress.getUserAddress()
                            .getAddressName());
                    addressItemData.setAddressReceiverName(currentAddress.getUserAddress()
                            .getReceiverName());
                    addressItemData.setAddressStreet(currentAddress.getUserAddress().getAddress());
                    addressItemData.setAddressCityName(currentAddress.getUserAddress().getCityName());
                    addressItemData.setAddressProvinceName(
                            currentAddress.getUserAddress().getProvinceName()
                    );
                    addressItemData.setRecipientPhoneNumber(
                            currentAddress.getUserAddress().getPhone()
                    );
                    addressItemData.setAddressCountryName(currentAddress.getUserAddress()
                            .getCountry());
                    adapterData.setItemData(addressItemData);
                    adapterData.setShipmentCartData(new ShipmentRatesDataMapper()
                            .getShipmentCartData(data, currentAddress.getUserAddress(),
                                    currentGroupShop, adapterData));

                    adapterData.setProductIsFreeReturns(currentProduct.isProductIsFreeReturns());
                    adapterData.setProductIsPreorder(currentProduct.isProductIsPreorder());
                    adapterData.setProductFcancelPartial(currentProduct.isProductFcancelPartial());
                    adapterData.setProductFinsurance(currentProduct.isProductFinsurance());

                    adapterDataList.add(adapterData);
                }
            }
        }
        return adapterDataList;
    }

    @Override
    public CheckPromoCodeCartShipmentRequest generateCheckPromoRequest(
            List<MultipleAddressShipmentAdapterData> shipmentData, CartItemPromoHolderData appliedPromo
    ) {
        CheckPromoCodeCartShipmentRequest.Builder checkoutPromoRequest =
                new CheckPromoCodeCartShipmentRequest.Builder();

        List<CheckPromoCodeCartShipmentRequest.Data> orderDatas = new ArrayList<>();

        for (int i = 0; i < shipmentData.size(); i++) {
            CheckPromoCodeCartShipmentRequest.Data.Builder orderData =
                    new CheckPromoCodeCartShipmentRequest.Data.Builder();
            orderData.addressId(Integer.parseInt(shipmentData.get(i).getItemData().getAddressId()));

            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProduct =
                    new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder();

            List<CheckPromoCodeCartShipmentRequest.ProductData> productDatas = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ProductData.Builder productData =
                    new CheckPromoCodeCartShipmentRequest.ProductData.Builder();
            productData
                    .productId(Integer.parseInt(shipmentData.get(i).getItemData().getProductId()))
                    .productNotes(shipmentData.get(i).getItemData().getProductNotes())
                    .productQuantity(
                            Integer.parseInt(shipmentData.get(i).getItemData().getProductQty()
                            )
                    );
            productDatas.add(productData.build());

            CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder shipmentInfo =
                    new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder();
            shipmentInfo
                    .shippingId(shipmentData.get(i).getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperId())
                    .spId(shipmentData.get(i).getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperProductId());

            shopProduct.productData(productDatas)
                    .shopId(shipmentData.get(i).getShopId())
                    .fcancelPartial(switchValue(shipmentData.get(i).getSelectedShipmentDetailData().getUsePartialOrder()))
                    .finsurance(switchValue(shipmentData.get(i).getSelectedShipmentDetailData().getUseInsurance()))
                    .isPreorder(switchValue(shipmentData.get(i).isProductIsPreorder()))
                    .shippingInfo(shipmentInfo.build());

            if (shipmentData.get(i).getSelectedShipmentDetailData().getUseDropshipper()) {
                CheckPromoCodeCartShipmentRequest.DropshipData.Builder dropshipData =
                        new CheckPromoCodeCartShipmentRequest.DropshipData.Builder();

                dropshipData
                        .name(shipmentData.get(i).getSelectedShipmentDetailData()
                                .getDropshipperName())
                        .telpNo(shipmentData.get(i).getSelectedShipmentDetailData()
                                .getDropshipperPhone());
            }

            shopProducts.add(shopProduct.build());

            orderData.shopProducts(shopProducts);

            orderDatas.add(orderData.build());
        }

        checkoutPromoRequest.data(orderDatas);
        if (appliedPromo.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
            checkoutPromoRequest.promoCode(appliedPromo.getVoucherCode());
        } else if (appliedPromo.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
            checkoutPromoRequest.promoCode(appliedPromo.getCouponCode());
        }

        return checkoutPromoRequest.build();
    }

    @Override
    public CartItemPromoHolderData generateCartPromoHolderData(
            PromoCodeAppliedData appliedPromoData
    ) {
        return null;
    }

    @Override
    public Subscriber<CheckPromoCodeCartShipmentResult> checkPromoSubscription(
            final CartItemPromoHolderData cartItemPromoHolderData) {
        return new Subscriber<CheckPromoCodeCartShipmentResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.showPromoError(null);
            }

            @Override
            public void onNext(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
                if (!checkPromoCodeCartShipmentResult.isError()) {
                    view.showPromoMessage(checkPromoCodeCartShipmentResult, cartItemPromoHolderData);
                } else {
                    view.showPromoError(checkPromoCodeCartShipmentResult.getErrorMessage());
                }
            }
        };
    }

    @Override
    public void processCheckShipmentFormPrepareCheckout() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                view.getGeneratedAuthParamNetwork(null));
        compositeSubscription.add(
                getShipmentAddressFormUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(
                                new Subscriber<CartShipmentAddressFormData>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                                        boolean isEnableCheckout = true;
                                        for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                                            if (groupAddress.isError() || groupAddress.isWarning())
                                                isEnableCheckout = false;
                                            for (GroupShop groupShop : groupAddress.getGroupShop()) {
                                                if (groupShop.isError() || groupShop.isWarning())
                                                    isEnableCheckout = false;
                                            }
                                        }
                                        if (isEnableCheckout) {
                                            view.renderCheckShipmentPrepareCheckoutSuccess();
                                        } else {
                                            view.renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                                                    cartShipmentAddressFormData
                                            );
                                        }
                                    }
                                }
                        )
        );
    }

    private int switchValue(boolean isTrue) {
        if (isTrue) return 1;
        else return 0;
    }

    @Override
    public void processCheckPromoCodeFromSuggestedPromo(String promoCode) {
        view.showLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("promo_code", promoCode);
        param.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO,
                view.getGeneratedAuthParamNetwork(param));
        compositeSubscription.add(
                checkPromoCodeCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(
                                getSubscriberCheckPromoCodeFromSuggested()
                        )
        );

    }

    @NonNull
    private Subscriber<CheckPromoCodeCartListResult> getSubscriberCheckPromoCodeFromSuggested() {
        return new Subscriber<CheckPromoCodeCartListResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideLoading();
            }

            @Override
            public void onNext(CheckPromoCodeCartListResult checkPromoCodeCartListResult) {
                view.hideLoading();
                if (!checkPromoCodeCartListResult.isError()) {
                    view.renderCheckPromoCodeFromSuggestedPromoSuccess(checkPromoCodeCartListResult);
                } else {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(checkPromoCodeCartListResult.getErrorMessage());
                }
            }
        };
    }

    @Override
    public HashMap<String, String> generatePickupPointParams(MultipleAddressShipmentAdapterData addressAdapterData) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PickupPointParamConstant.PARAM_DISTRICT_ID,
                String.valueOf(addressAdapterData.getDestinationDistrictId()));
        params.put(PickupPointParamConstant.PARAM_PAGE, PickupPointParamConstant.DEFAULT_PAGE);
        params.put(PickupPointParamConstant.PARAM_TOKEN,
                addressAdapterData.getTokenPickup() != null ? addressAdapterData.getTokenPickup() : "");
        params.put(PickupPointParamConstant.PARAM_UT, addressAdapterData.getUnixTime());
        return params;
    }

}
