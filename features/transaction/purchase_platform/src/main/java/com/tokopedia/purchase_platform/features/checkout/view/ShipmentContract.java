package com.tokopedia.purchase_platform.features.checkout.view;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.CheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.response.cod.Data;
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartResponse;
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData;
import com.tokopedia.purchase_platform.common.domain.model.PriceValidationData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.DataChangeAddressRequest;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataConverter;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.EgoldAttributeModel;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.NotEligiblePromoHolderdata;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.ShipmentButtonPaymentModel;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.ShipmentDonationModel;

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

        void renderCheckPromoStackCodeFromCourierSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, int itemPosition, boolean noToast);

        void renderCheckPromoStackLogisticSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, String promoCode);

        void renderErrorCheckPromoCodeFromSuggestedPromo(String message);

        void renderErrorCheckPromoShipmentData(String message);

        void renderCheckPromoStackingShipmentDataSuccess(ResponseGetPromoStackUiModel responseGetPromoStackUiModel);

        void renderEditAddressSuccess(String latitude, String longitude);

        void renderChangeAddressSuccess();

        void renderChangeAddressFailed();

        void renderCancelAutoApplyCouponSuccess(String variant);

        void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition, boolean isTradeInDropOff);

        void renderCourierStateFailed(int itemPosition, boolean isTradeInDropOff);

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

        void onSuccessClearPromoStack(int shopIndex, String voucherType);

        void onFailedClearPromoStack(boolean ignoreAPIResponse);

        void onSuccessClearPromoLogistic();

        void resetCourier(int position);

        Promo generateCheckPromoFirstStepParam();

        void onClashCheckPromo(ClashingInfoDetailUiModel clashingInfoDetailUiModel, String type);

        void onSuccessCheckPromoFirstStepAfterClash(ResponseGetPromoStackUiModel responseGetPromoStackUiModel, String promoCode);

        void onSuccessCheckPromoFirstStep(ResponseGetPromoStackUiModel promoData);

        void onSuccessClearPromoStackAfterClash();

        void clearTotalBenefitPromoStacking();

        void triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(String transactionId);

        void removeIneligiblePromo(int checkoutType, ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList);

        boolean isInsuranceEnabled();

        void updateTickerAnnouncementMessage();

        void resetPromoBenefit();

        void setPromoBenefit(List<SummariesUiModel> summariesUiModels);

        boolean isTradeInByDropOff();
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
    }

    interface Presenter extends CustomerPresenter<View> {

        void processInitialLoadCheckoutPage(boolean isReloadData, boolean isOneClickShipment,
                                            boolean isTradeIn, boolean skipUpdateOnboardingState,
                                            boolean isReloadAfterPriceChangeHinger,
                                            String cornerId, String deviceId, String leasingId);

        void processReloadCheckoutPageFromMultipleAddress(PromoStackingData promoStackingData,
                                                          CartPromoSuggestionHolderData cartPromoSuggestionHolderData,
                                                          RecipientAddressModel recipientAddressModel,
                                                          ArrayList<ShipmentCartItemModel> shipmentCartItemModels,
                                                          ShipmentCostModel shipmentCostModel,
                                                          ShipmentDonationModel shipmentDonationModel);

        void processReloadCheckoutPageBecauseOfError(boolean isOneClickShipment, boolean isTradeIn, String deviceId);

        void processCheckout(CheckPromoParam checkPromoParam, boolean hasInsurance,
                             boolean isOneClickShipment, boolean isTradeIn,
                             boolean isTradeInDropOff, String deviceId, String lesingId);

        void checkPromoFinalStackShipment(Promo promo);
//
//         void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isOneClickShipment);
//
//         void processCheckPromoCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast, boolean isOneClickShipment);

        void processCheckPromoStackingLogisticPromo(int cartPosition, String cartString, String code);

        void processCheckPromoStackingCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast);

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

        CartPromoSuggestionHolderData getCartPromoSuggestionHolderData();

        void setCartPromoSuggestionHolderData(CartPromoSuggestionHolderData cartPromoSuggestionHolderData);

        void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList);

        void setDataChangeAddressRequestList(List<DataChangeAddressRequest> dataChangeAddressRequestList);

        ShipmentCostModel getShipmentCostModel();

        EgoldAttributeModel getEgoldAttributeModel();

        TickerAnnouncementHolderData getTickerAnnouncementHolderData();

        void setTickerAnnouncementHolderData(TickerAnnouncementHolderData tickerAnnouncementHolderData);

        void setShipmentCostModel(ShipmentCostModel shipmentCostModel);

        void setEgoldAttributeModel(EgoldAttributeModel egoldAttributeModel);

        void editAddressPinpoint(String latitude, String longitude, ShipmentCartItemModel shipmentCartItemModel, LocationPass locationPass);

        void cancelAutoApplyPromoStack(int shopIndex, ArrayList<String> promoCodeList, boolean ignoreAPIResponse, String voucherType);

        void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList, int checkoutType);

        void cancelAutoApplyPromoStackLogistic(String promoCode);

        void cancelAutoApplyPromoStackAfterClash(ArrayList<String> oldPromoList, ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                                 boolean isFromMultipleAddress, boolean isOneClickShipment, boolean isTradeIn,
                                                 @Nullable String cornerId, String deviceId, String type);

        void applyPromoStackAfterClash(ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                       boolean isFromMultipleAddress, boolean isOneClickShipment,
                                       boolean isTradeIn, String cornerId, String deviceId, String type);

        void changeShippingAddress(RecipientAddressModel newRecipientAddressModel, boolean isOneClickShipment, boolean isTradeInDropOff, boolean isHandleFallback);

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

        void proceedCodCheckout(CheckPromoParam checkPromoParam, boolean hasInsurance, boolean isOneClickShipment, boolean isTradeIn, String deviceId, String leasingId);

        Token getKeroToken();

        boolean isShowOnboarding();

        void triggerSendEnhancedEcommerceCheckoutAnalytics(List<DataCheckoutRequest> dataCheckoutRequests,
                                                           boolean hasInsurance,
                                                           String step, String eventAction,
                                                           String eventLabel, String leasingId);

        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(String cartString, String shippingDuration, String shippingPrice, String courierName);

        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(PromoStackingData promoStackingData, List<ShipmentCartItemModel> shipmentCartItemModels);

        boolean isIneligbilePromoDialogEnabled();

        void processSubmitHelpTicket(CheckoutData checkoutData);

        CheckoutRequest generateCheckoutRequest(List<DataCheckoutRequest> analyticsDataCheckoutRequests, boolean hasInsurance, CheckPromoParam checkPromoParam, int isDonation, String leasingId);

        void getInsuranceTechCartOnCheckout();
    }

}
