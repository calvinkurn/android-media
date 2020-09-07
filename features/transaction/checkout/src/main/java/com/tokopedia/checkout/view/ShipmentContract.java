package com.tokopedia.checkout.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.purchase_platform.common.feature.checkout.request.CheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.cod.Data;
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartResponse;
import com.tokopedia.checkout.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData;
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult;
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.view.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata;
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel;

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

        void renderCheckoutPage(boolean isInitialRender, boolean isReloadAfterPriceChangeHigher, boolean isOneClickShipment);

        void renderInsuranceCartData(InsuranceCartResponse insuranceCartResponse);

        void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData cartShipmentAddressFormData);

        void renderDataChanged();

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

        void setCourierPromoApplied(int itemPosition);

        void showBottomSheetError(String htmlMessage);

        void navigateToCodConfirmationPage(Data data, CheckoutRequest checkoutRequest);

        void stopTrace();

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

        void sendAnalyticsOnClickTopDonation();

        void sendAnalyticsOnClickChangeAddress();

        void sendAnalyticsOnClickSubtotal();

        void sendAnalyticsOnClickCheckBoxDropShipperOption();

        void sendAnalyticsOnClickCheckBoxInsuranceOption();

        void sendAnalyticsScreenName(String screenName);

        void sendAnalyticsOnClickEditPinPointErrorValidation(String message);

        void sendAnalyticsCourierNotComplete();

        void sendAnalyticsPromoRedState();

        void sendAnalyticsDropshipperNotComplete();

        void sendAnalyticsOnClickChooseShipmentDurationOnShipmentRecomendation(String isBlackbox);

        void sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration();

        void sendAnalyticsOnClickChecklistShipmentRecommendationDuration(String duration);

        void sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(String courier);

        void sendAnalyticsOnClickChangeCourierShipmentRecommendation(ShipmentCartItemModel shipmentCartItemModel);

        void sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier();

        void sendAnalyticsOnClickChangeDurationShipmentRecommendation();

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

        boolean isIneligiblePromoDialogEnabled();

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
