package com.tokopedia.checkout.view.feature.shipment;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.NotEligiblePromoHolderdata;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CodModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.EgoldAttributeModel;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;
import com.tokopedia.transactiondata.entity.response.cod.Data;
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData;

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

        void renderCheckPromoStackCodeFromCourierSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, int itemPosition, boolean noToast);

        void renderCheckPromoStackLogisticSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, String promoCode);

        void renderErrorCheckPromoCodeFromSuggestedPromo(String message);

        void renderErrorCheckPromoShipmentData(String message);

        void renderCheckPromoStackingShipmentDataSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel);

        void renderEditAddressSuccess(String latitude, String longitude);

        void renderChangeAddressSuccess(RecipientAddressModel recipientAddressModel);

        void renderCancelAutoApplyCouponSuccess(String variant);

        void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition);

        void renderCourierStateFailed(int itemPosition);

        void cancelAllCourierPromo();

        void updateCourierBottomssheetHasData(List<ShippingCourierViewModel> shippingCourierViewModels, int cartPosition,
                                              ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList);

        void updateCourierBottomsheetHasNoData(int cartPosition, ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList);

        void navigateToSetPinpoint(String message, LocationPass locationPass);

        List<DataCheckoutRequest> generateNewCheckoutRequest(List<ShipmentCartItemModel> shipmentCartItemModelList, boolean isAnalyticsPurpose);

        ShipmentDataConverter getShipmentDataConverter();

        Activity getActivityContext();

        boolean checkCourierPromoStillExist();

        void setCourierPromoApplied(int itemPosition);

        void showBottomSheetError(String htmlMessage);

        void navigateToCodConfirmationPage(Data data, CheckoutRequest checkoutRequest);

        void setPromoStackingData(CartShipmentAddressFormData cartShipmentAddressFormData);

        void showToastFailedTickerPromo(String text);

        void stopTrace();

        void onSuccessClearPromoStack(int shopIndex);

        void onFailedClearPromoStack(boolean ignoreAPIResponse);

        void resetCourier(int position);

        Promo generateCheckPromoFirstStepParam();

        void onClashCheckPromo(ClashingInfoDetailUiModel clashingInfoDetailUiModel, String type);

        void onSuccessCheckPromoFirstStepAfterClash(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, String promoCode);

        void onSuccessCheckPromoFirstStep(ResponseGetPromoStackUiModel promoData);

        void onSuccessClearPromoStackAfterClash();

        void clearTotalBenefitPromoStacking();

        void triggerSendEnhancedEcommerceCheckoutAnalyticAfterPromoChange(String eventAction, String eventLabel);

        void triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess();

        void removeIneligiblePromo(int checkoutType, ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList);
    }

    interface AnalyticsActionListener {
        void sendAnalyticsChoosePaymentMethodFailed(String errorMessage);

        @Deprecated
        void sendAnalyticsChoosePaymentMethodCourierNotComplete();

        void sendEnhancedEcommerceAnalyticsCheckout(Map<String, Object> stringObjectMap,
                                                    String transactionId,
                                                    String eventAction,
                                                    String eventLabel);

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

        void sendAnalyticsPromoRedState();

        void sendAnalyticsDropshipperNotComplete();

        void sendAnalyticsOnCourierChanged(String agent, String service);

        void sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation(String isBlackbox);

        void sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration();

        void sendAnalyticsOnClickChecklistShipmentRecommendationDuration(String duration);

        void sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(String courier);

        void sendAnalyticsOnClickChangeCourierShipmentRecommendation();

        void sendAnalyticsOnClickSelectedCourierShipmentRecommendation(String courierName);

        void sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier();

        void sendAnalyticsOnClickChangeDurationShipmentRecommendation();

        void sendAnalyticsOnViewPromoAutoApply();

        void sendAnalyticsOnViewPromoManualApply(String type);

        void sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(int shippingProductId);

        void sendAnalyticsOnDisplayDurationThatContainPromo(boolean isCourierPromo, String duration);

        void sendAnalyticsOnDisplayLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId);

        void sendAnalyticsOnClickDurationThatContainPromo(boolean isCourierPromo, String duration, boolean isCod, String shippingPriceMin, String shippingPriceHigh);

        void sendAnalyticsOnClickLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId, boolean isCod);
    }

    interface Presenter extends CustomerPresenter<View> {

        void processInitialLoadCheckoutPage(boolean isReloadData, boolean isOneClickShipment,
                                            boolean isTradeIn, boolean skipUpdateOnboardingState,
                                            String cornerId, String deviceId);

        void processReloadCheckoutPageFromMultipleAddress(PromoStackingData promoStackingData,
                                                          CartPromoSuggestion cartPromoSuggestion,
                                                          RecipientAddressModel recipientAddressModel,
                                                          ArrayList<ShipmentCartItemModel> shipmentCartItemModels,
                                                          ShipmentCostModel shipmentCostModel,
                                                          ShipmentDonationModel shipmentDonationModel);

        void processReloadCheckoutPageBecauseOfError(boolean isOneClickShipment, boolean isTradeIn, String deviceId);

        void processCheckout(CheckPromoParam checkPromoParam, boolean isOneClickShipment, boolean isTradeIn, String deviceId);

        void processVerifyPayment(String transactionId);

        void checkPromoStackShipment(Promo promo);
//
//         void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isOneClickShipment);
//
//         void processCheckPromoCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast, boolean isOneClickShipment);

        void processCheckPromoStackingLogisticPromo(int cartPosition, String cartString, String code);

        void processCheckPromoStackingCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast);

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

        EgoldAttributeModel getEgoldAttributeModel();

        void setShipmentCostModel(ShipmentCostModel shipmentCostModel);

        void setEgoldAttributeModel(EgoldAttributeModel egoldAttributeModel);

        void editAddressPinpoint(String latitude, String longitude, ShipmentCartItemModel shipmentCartItemModel, LocationPass locationPass);

        void cancelAutoApplyCoupon(String variant);

        void cancelAutoApplyPromoStack(int shopIndex, ArrayList<String> promoCodeList, boolean ignoreAPIResponse);

        void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList, int checkoutType);

        void cancelAutoApplyPromoStackLogistic(String promoCode);

        void cancelAutoApplyPromoStackAfterClash(ArrayList<String> oldPromoList, ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                                 boolean isFromMultipleAddress, boolean isOneClickShipment, boolean isTradeIn,
                                                 @Nullable String cornerId, String deviceId, String type);

        void applyPromoStackAfterClash(ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                       boolean isFromMultipleAddress, boolean isOneClickShipment,
                                       boolean isTradeIn, String cornerId, String deviceId, String type);

        void changeShippingAddress(RecipientAddressModel recipientAddressModel, boolean isOneClickShipment);

        void setShipmentDonationModel(ShipmentDonationModel shipmentDonationModel);

        ShipmentDonationModel getShipmentDonationModel();

        void setShipmentButtonPaymentModel(ShipmentButtonPaymentModel shipmentButtonPaymentModel);

        ShipmentButtonPaymentModel getShipmentButtonPaymentModel();

        void setShippingCourierViewModelsState(List<ShippingCourierViewModel> shippingCourierViewModelsState,
                                               int itemPosition);

        List<ShippingCourierViewModel> getShippingCourierViewModelsState(int itemPosition);

        void setCouponStateChanged(boolean appliedCoupon);

        boolean getCouponStateChanged();

        void setHasDeletePromoAfterChecKPromoCodeFinal(boolean state);

        boolean getHasDeletePromoAfterChecKPromoCodeFinal();

        CodModel getCodData();

        void proceedCodCheckout(CheckPromoParam checkPromoParam, boolean isOneClickShipment, boolean isTradeIn, String deviceId);

        Token getKeroToken();

        boolean isShowOnboarding();

        void triggerSendEnhancedEcommerceCheckoutAnalytics(String step, String eventAction, String eventLabel);

        void triggerSendEnhancedEcommerceCheckoutAnalytics(List<DataCheckoutRequest> dataCheckoutRequests, String step, String eventAction, String eventLabel);

        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(String cartString, String shippingDuration, String shippingPrice, String courierName);

        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(PromoStackingData promoStackingData, List<ShipmentCartItemModel> shipmentCartItemModels);

        boolean isIneligbilePromoDialogEnabled();
    }

}
