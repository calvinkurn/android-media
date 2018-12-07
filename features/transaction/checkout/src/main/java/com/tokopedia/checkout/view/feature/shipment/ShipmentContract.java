package com.tokopedia.checkout.view.feature.shipment;

import android.app.Activity;
import android.view.View;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public interface ShipmentContract {

    interface View extends CustomerView {
        void showInitialLoading();

        void hideInitialLoading();

        void showLoading();

        void hideLoading();

        void showToastNormal(String message);

        void showToastError(String message);

        void renderErrorPage(String message);

        void renderCheckoutPage(boolean isInitialRender, boolean isFromPdp);

        void renderCheckShipmentPrepareCheckoutSuccess();

        void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                CartShipmentAddressFormData cartShipmentAddressFormData, boolean needToRefreshItemList
        );

        void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData cartShipmentAddressFormData);

        void renderDataChanged();

        void renderErrorDataHasChangedAfterCheckout(List<ShipmentCartItemModel> shipmentCartItemModelList);

        void renderThanksTopPaySuccess(String message);

        void renderCheckoutCartSuccess(CheckoutData checkoutData);

        void renderCheckoutCartError(String message);

        void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData);

        void renderCheckPromoCodeFromCourierSuccess(PromoCodeCartListData promoCodeCartListData, int itemPosition, boolean noToast);

        void renderErrorCheckPromoCodeFromSuggestedPromo(String message);

        void renderErrorCheckPromoShipmentData(String message);

        void renderCheckPromoShipmentDataSuccess(PromoCodeCartShipmentData checkPromoCodeCartShipmentResult);

        void renderEditAddressSuccess(String latitude, String longitude);

        void renderChangeAddressSuccess(RecipientAddressModel recipientAddressModel);

        void renderCancelAutoApplyCouponSuccess();

        void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition);

        void renderCourierStateFailed(int itemPosition);

        void cancelAllCourierPromo();

        void updateCourierBottomssheetHasData(List<ShippingCourierViewModel> shippingCourierViewModels, int cartPosition,
                                              ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList);

        void updateCourierBottomsheetHasNoData(int cartPosition, ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList);

        void navigateToSetPinpoint(String message, LocationPass locationPass);

        List<DataCheckoutRequest> generateNewCheckoutRequest(List<ShipmentCartItemModel> shipmentCartItemModelList);

        ShipmentDataConverter getShipmentDataConverter();

        Activity getActivityContext();

        boolean checkCourierPromoStillExist();

        void setCourierPromoApplied(int itemPosition);

        void proceedCod(android.view.View view);
    }

    interface AnalyticsActionListener {
        void sendAnalyticsChoosePaymentMethodSuccess();

        void sendAnalyticsChoosePaymentMethodFailed();

        @Deprecated
        void sendAnalyticsChoosePaymentMethodCourierNotComplete();

        void sendAnalyticsCheckoutStep2(Map<String, Object> stringObjectMap, String transactionId);

        void sendAnalyticsOnClickChooseOtherAddressShipment();

        void sendAnalyticsOnClickChooseToMultipleAddressShipment();

        void sendAnalyticsOnClickTopDonation();

        void sendAnalyticsOnClickChangeAddress();

        void sendAnalyticsOnClickChooseCourierSelection();

        void sendAnalyticsOnBottomShetCourierSelectionShow(List<ShopShipment> shopShipmentList);

        void sendAnalyticsOnImpressionCourierSelectionShow();

        void sendAnalyticsOnClickUsePromoCodeAndCoupon();

        void sendAnalyticsOnClickCancelUsePromoCodeAndCouponBanner();

        void sendAnalyticsOnClickShipmentCourierItem(String agent, String service);

        void sendAnalyticsOnClickSubtotal();

        void sendAnalyticsOnClickCheckBoxDropShipperOption();

        void sendAnalyticsOnClickCheckBoxInsuranceOption();

        void sendAnalyticsScreenName(String screenName);

        void sendAnalyticsOnClickEditPinPointErrorValidation(String message);

        void sendAnalyticsCourierNotComplete();

        void sendAnalyticsDropshipperNotComplete();

        void sendAnalyticsOnCourierChanged(String agent, String service);


        void sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation();

        void sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration();

        void sendAnalyticsOnClickChecklistShipmentRecommendationDuration(String duration);

        void sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(String courier);

        void sendAnalyticsOnClickChangeCourierShipmentRecommendation();

        void sendAnalyticsOnClickSelectedCourierShipmentRecommendation(String courierName);

        void sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier();

        void sendAnalyticsOnClickChangeDurationShipmentRecommendation();

        void sendAnalyticsOnClickButtonDoneShowCaseDurationShipmentRecommendation();

        void sendAnalyticsOnViewPromoAutoApply();

        void sendAnalyticsOnViewPromoManualApply(String type);

        void sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(int shippingProductId);

        void sendAnalyticsOnDisplayDurationThatContainPromo(boolean isCourierPromo, String duration);

        void sendAnalyticsOnDisplayLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId);

        void sendAnalyticsOnClickDurationThatContainPromo(boolean isCourierPromo, String duration);

        void sendAnalyticsOnClickLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId);
    }

    interface Presenter extends CustomerPresenter<View> {

        void processInitialLoadCheckoutPage(boolean isFromMultipleAddress, boolean isOneClickShipment);

        void processReloadCheckoutPageFromMultipleAddress(CartItemPromoHolderData cartItemPromoHolderData,
                                                          CartPromoSuggestion cartPromoSuggestion,
                                                          RecipientAddressModel recipientAddressModel,
                                                          ArrayList<ShipmentCartItemModel> shipmentCartItemModels,
                                                          ShipmentCostModel shipmentCostModel,
                                                          ShipmentDonationModel shipmentDonationModel,
                                                          boolean isOneClickShipment);

        void processReloadCheckoutPageBecauseOfError(boolean isOneClickShipment);

        void processCheckShipmentPrepareCheckout(boolean isOneClickShipment);

        void processCheckout(boolean isOneClickShipment);

        void processVerifyPayment(String transactionId);

        void checkPromoShipment(boolean isOneClickShipment);

        void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isOneClickShipment);

        void processCheckPromoCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast);

        void processSaveShipmentState(ShipmentCartItemModel shipmentCartItemModel);

        void processSaveShipmentState();

        void processGetRates(int shipperId, int spId, int itemPosition,
                             ShipmentDetailData shipmentDetailData, List<ShopShipment> shopShipmentList);

        void processGetCourierRecommendation(int shipperId, int spId, int itemPosition,
                                             ShipmentDetailData shipmentDetailData,
                                             ShipmentCartItemModel shipmentCartItemModel,
                                             List<ShopShipment> shopShipmentList,
                                             boolean isInitialLoad);

        RecipientAddressModel getRecipientAddressModel();

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        List<ShipmentCartItemModel> getShipmentCartItemModelList();

        void setShipmentCartItemModelList(List<ShipmentCartItemModel> recipientCartItemList);

        PromoCodeAppliedData getPromoCodeAppliedData();

        void setPromoCodeAppliedData(PromoCodeAppliedData promoCodeAppliedData);

        CartPromoSuggestion getCartPromoSuggestion();

        void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion);

        CheckoutData getCheckoutData();

        void setCheckoutData(CheckoutData checkoutData);

        void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList);

        void setPromoCodeCartShipmentRequestData(
                List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestData
        );

        void setDataChangeAddressRequestList(List<DataChangeAddressRequest> dataChangeAddressRequestList);

        ShipmentCostModel getShipmentCostModel();

        void setShipmentCostModel(ShipmentCostModel shipmentCostModel);

        void editAddressPinpoint(String latitude, String longitude, ShipmentCartItemModel shipmentCartItemModel, LocationPass locationPass);

        void cancelAutoApplyCoupon();

        void changeShippingAddress(RecipientAddressModel recipientAddressModel, boolean isOneClickShipment);

        void setShipmentDonationModel(ShipmentDonationModel shipmentDonationModel);

        ShipmentDonationModel getShipmentDonationModel();

        void setCartItemPromoHolderData(CartItemPromoHolderData cartItemPromoHolderData);

        CartItemPromoHolderData getCartItemPromoHolderData();

        void setShippingCourierViewModelsState(List<ShippingCourierViewModel> shippingCourierViewModelsState,
                                               int itemPosition);

        List<ShippingCourierViewModel> getShippingCourierViewModelsState(int itemPosition);

        void setCouponStateChanged(boolean appliedCoupon);

        boolean getCouponStateChanged();

        void setHasDeletePromoAfterChecKPromoCodeFinal(boolean state);

        boolean getHasDeletePromoAfterChecKPromoCodeFinal();
    }

}
