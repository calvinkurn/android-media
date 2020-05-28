package com.tokopedia.purchase_platform.features.checkout.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.CheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.response.cod.Data;
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartResponse;
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData;
import com.tokopedia.purchase_platform.common.domain.model.PriceValidationData;
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.ticker_announcement.TickerAnnouncementHolderData;
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.DataChangeAddressRequest;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataConverter;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.purchase_platform.features.promo.data.request.PromoRequest;
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.PromoUiModel;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.SummariesItemUiModel;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel;

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

        void onCacheExpired(String message);

        void renderCheckoutPage(boolean isInitialRender, boolean isReloadAfterPriceChangeHigher, boolean isFromPdp);

        void renderInsuranceCartData(InsuranceCartResponse insuranceCartResponse);

        void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                CartShipmentAddressFormData cartShipmentAddressFormData, boolean needToRefreshItemList
        );

        void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData cartShipmentAddressFormData);

        void renderDataChanged();

        void renderErrorDataHasChangedAfterCheckout(List<ShipmentCartItemModel> shipmentCartItemModelList);

        void renderThanksTopPaySuccess(String message);

        void renderCheckoutCartSuccess(CheckoutData checkoutData);

        void renderCheckoutCartError(String message);

        void renderCheckoutCartErrorReporter(CheckoutData checkoutData);

        void renderCheckoutPriceUpdated(PriceValidationData priceValidationData);

        void renderSubmitHelpTicketSuccess(SubmitTicketResult submitTicketResult);

        void renderPromoCheckoutFromCourierSuccess(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel, int itemPosition, boolean noToast);

        void renderErrorCheckPromoShipmentData(String message);

        void renderEditAddressSuccess(String latitude, String longitude);

        void renderChangeAddressSuccess();

        void renderChangeAddressFailed();

        void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition, boolean isTradeInDropOff);

        void renderCourierStateFailed(int itemPosition, boolean isTradeInDropOff);

        void cancelAllCourierPromo();

        void updateCourierBottomssheetHasData(List<ShippingCourierUiModel> shippingCourierUiModels, int cartPosition,
                                              ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList);

        void updateCourierBottomsheetHasNoData(int cartPosition, ShipmentCartItemModel shipmentCartItemModel, List<ShopShipment> shopShipmentList);

        void navigateToSetPinpoint(String message, LocationPass locationPass);

        List<DataCheckoutRequest> generateNewCheckoutRequest(List<ShipmentCartItemModel> shipmentCartItemModelList, boolean isAnalyticsPurpose);

        Activity getActivityContext();

        boolean checkCourierPromoStillExist();

        void setCourierPromoApplied(int itemPosition);

        void showBottomSheetError(String htmlMessage);

        void navigateToCodConfirmationPage(Data data, CheckoutRequest checkoutRequest);

        void setPromoStackingData(CartShipmentAddressFormData cartShipmentAddressFormData);

        void showToastFailedTickerPromo(String text);

        void stopTrace();

        void onFailedClearPromoStack(boolean ignoreAPIResponse);

        void onSuccessClearPromoLogistic(int position, boolean isLastAppliedPromo);

        void resetCourier(int position);

        ValidateUsePromoRequest generateValidateUsePromoRequest();

        PromoRequest generateCouponListRecommendationRequest();

        void clearTotalBenefitPromoStacking();

        void triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(String transactionId);

        void removeIneligiblePromo(int checkoutType, ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList);

        boolean isInsuranceEnabled();

        void updateTickerAnnouncementMessage();

        void resetPromoBenefit();

        void setPromoBenefit(List<SummariesItemUiModel> summariesUiModels);

        boolean isTradeInByDropOff();

        void updateButtonPromoCheckout(PromoUiModel promoUiModel);

        void resetCourier(ShipmentCartItemModel shipmentCartItemModel);
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

        void sendAnalyticsOnClickChangeCourierShipmentRecommendation(ShipmentCartItemModel shipmentCartItemModel);

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

        void sendAnalyticsViewInformationAndWarningTickerInCheckout(String tickerId);

        void sendAnalyticsViewPromoAfterAdjustItem(String msg);
    }

    interface Presenter extends CustomerPresenter<View> {

        void processInitialLoadCheckoutPage(boolean isReloadData, boolean isOneClickShipment,
                                            boolean isTradeIn, boolean skipUpdateOnboardingState,
                                            boolean isReloadAfterPriceChangeHinger,
                                            String cornerId, String deviceId, String leasingId);

        void processReloadCheckoutPageFromMultipleAddress(RecipientAddressModel recipientAddressModel,
                                                          ArrayList<ShipmentCartItemModel> shipmentCartItemModels);

        void processReloadCheckoutPageBecauseOfError(boolean isOneClickShipment, boolean isTradeIn, String deviceId);

        void processCheckout(boolean hasInsurance,
                             boolean isOneClickShipment, boolean isTradeIn,
                             boolean isTradeInDropOff, String deviceId,
                             String cornerId, String leasingId);

        void checkPromoCheckoutFinalShipment(ValidateUsePromoRequest validateUsePromoRequest);

        void doValidateuseLogisticPromo(int cartPosition, String cartString, ValidateUsePromoRequest validateUsePromoRequest);

        void processCheckPromoCheckoutCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast);

        void processSaveShipmentState(ShipmentCartItemModel shipmentCartItemModel);

        void processSaveShipmentState();

        void processGetCourierRecommendation(int shipperId, int spId, int itemPosition,
                                             ShipmentDetailData shipmentDetailData,
                                             ShipmentCartItemModel shipmentCartItemModel,
                                             List<ShopShipment> shopShipmentList,
                                             boolean isInitialLoad, ArrayList<Product> products,
                                             String cartString, boolean isTradeInDropOff,
                                             RecipientAddressModel recipientAddressModel);

        RecipientAddressModel getRecipientAddressModel();

        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);

        List<ShipmentCartItemModel> getShipmentCartItemModelList();

        void setShipmentCartItemModelList(List<ShipmentCartItemModel> recipientCartItemList);

        void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList);

        void setDataChangeAddressRequestList(List<DataChangeAddressRequest> dataChangeAddressRequestList);

        ShipmentCostModel getShipmentCostModel();

        EgoldAttributeModel getEgoldAttributeModel();

        TickerAnnouncementHolderData getTickerAnnouncementHolderData();

        void setTickerAnnouncementHolderData(TickerAnnouncementHolderData tickerAnnouncementHolderData);

        void setShipmentCostModel(ShipmentCostModel shipmentCostModel);

        void setEgoldAttributeModel(EgoldAttributeModel egoldAttributeModel);

        void editAddressPinpoint(String latitude, String longitude, ShipmentCartItemModel shipmentCartItemModel, LocationPass locationPass);

        void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList, int checkoutType);

        void cancelAutoApplyPromoStackLogistic(int itemPosition, String promoCode);

        void cancelAutoApplyPromoStackAfterClash(ArrayList<String> promoCodesToBeCleared);

        void changeShippingAddress(RecipientAddressModel newRecipientAddressModel, boolean isOneClickShipment, boolean isTradeInDropOff, boolean isHandleFallback);

        void setShipmentDonationModel(ShipmentDonationModel shipmentDonationModel);

        ShipmentDonationModel getShipmentDonationModel();

        void setShipmentButtonPaymentModel(ShipmentButtonPaymentModel shipmentButtonPaymentModel);

        ShipmentButtonPaymentModel getShipmentButtonPaymentModel();

        void setShippingCourierViewModelsState(List<ShippingCourierUiModel> shippingCourierUiModelsState,
                                               int itemPosition);

        List<ShippingCourierUiModel> getShippingCourierViewModelsState(int itemPosition);

        void setCouponStateChanged(boolean appliedCoupon);

        boolean getCouponStateChanged();

        void setHasDeletePromoAfterChecKPromoCodeFinal(boolean state);

        boolean getHasDeletePromoAfterChecKPromoCodeFinal();

        CodModel getCodData();

        void proceedCodCheckout(boolean hasInsurance, boolean isOneClickShipment, boolean isTradeIn, String deviceId, String leasingId);

        CampaignTimerUi getCampaignTimer();

        Token getKeroToken();

        boolean isShowOnboarding();

        void triggerSendEnhancedEcommerceCheckoutAnalytics(List<DataCheckoutRequest> dataCheckoutRequests,
                                                           boolean hasInsurance,
                                                           String step, String eventAction,
                                                           String eventLabel, String leasingId);

        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(String cartString, String shippingDuration, String shippingPrice, String courierName);

        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(List<ShipmentCartItemModel> shipmentCartItemModels);

        boolean isIneligbilePromoDialogEnabled();

        void processSubmitHelpTicket(CheckoutData checkoutData);

        CheckoutRequest generateCheckoutRequest(List<DataCheckoutRequest> analyticsDataCheckoutRequests, boolean hasInsurance, int isDonation, String leasingId);

        void getInsuranceTechCartOnCheckout();

        ShipmentDataConverter getShipmentDataConverter();

        void releaseBooking();

        void setLastApplyData(LastApplyUiModel lastApplyData);

        LastApplyUiModel getLastApplyData();

        void setValidateUsePromoRevampUiModel(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel);

        ValidateUsePromoRevampUiModel getValidateUsePromoRevampUiModel();

        void setLatValidateUseRequest(ValidateUsePromoRequest latValidateUseRequest);

        ValidateUsePromoRequest getLastValidateUseRequest();
    }

}
