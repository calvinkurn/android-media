package com.tokopedia.checkout.view;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public interface ShipmentOldContract {

//    interface View extends CustomerView {
//        void showInitialLoading();
//
//        void hideInitialLoading();
//
//        void showLoading();
//
//        void hideLoading();
//
//        void showToastNormal(String message);
//
//        void showToastError(String message);
//
//        void showToastErrorAkamai(String message);
//
//        void renderErrorPage(String message);
//
//        void onCacheExpired(String message);
//
//        void onShipmentAddressFormEmpty();
//
//        void renderCheckoutPage(boolean isInitialRender, boolean isReloadAfterPriceChangeHigher, boolean isOneClickShipment);
//
//        void renderCheckoutPageNoAddress(CartShipmentAddressFormData cartShipmentAddressFormData, boolean isEligibleForRevampAna);
//
//        void renderCheckoutPageNoMatchedAddress(CartShipmentAddressFormData cartShipmentAddressFormData, int addressState);
//
//        void renderDataChanged();
//
//        void renderCheckoutCartSuccess(CheckoutData checkoutData);
//
//        void renderCheckoutCartError(String message);
//
//        void renderCheckoutPriceUpdated(PriceValidationData priceValidationData);
//
//        void renderPrompt(Prompt prompt);
//
//        void renderPromoCheckoutFromCourierSuccess(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel, int itemPosition, boolean noToast);
//
//        void renderErrorCheckPromoShipmentData(String message);
//
//        void renderEditAddressSuccess(String latitude, String longitude);
//
//        void renderChangeAddressSuccess(boolean refreshCheckoutPage);
//
//        void renderChangeAddressFailed(boolean refreshCheckoutPageIfSuccess);
//
//        void renderCourierStateSuccess(CourierItemData courierItemData, int itemPosition, boolean isTradeInDropOff, boolean isForceReloadRates);
//
//        void renderCourierStateFailed(int itemPosition, boolean isTradeInDropOff, boolean isBoAutoApplyFlow);
//
//        void cancelAllCourierPromo();
//
//        void updateCourierBottomssheetHasData(List<ShippingCourierUiModel> shippingCourierUiModels, int cartPosition,
//                                              ShipmentCartItemModel shipmentCartItemModel, PreOrderModel preOrderModel);
//
//        void updateCourierBottomsheetHasNoData(int cartPosition, ShipmentCartItemModel shipmentCartItemModel);
//
//        void navigateToSetPinpoint(String message, LocationPass locationPass);
//
//        List<DataCheckoutRequest> generateNewCheckoutRequest(List<ShipmentCartItemModel> shipmentCartItemModelList, boolean isAnalyticsPurpose);
//
//        Activity getActivityContext();
//
//        void setCourierPromoApplied(int itemPosition);
//
//        void stopTrace();
//
//        void stopEmbraceTrace();
//
//        void onSuccessClearPromoLogistic(int position, boolean isLastAppliedPromo);
//
//        void resetCourier(int position);
//
//        ValidateUsePromoRequest generateValidateUsePromoRequest();
//
//        PromoRequest generateCouponListRecommendationRequest();
//
//        void clearTotalBenefitPromoStacking();
//
//        void triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(String transactionId,
//                                                                              String deviceModel,
//                                                                              long devicePrice,
//                                                                              String diagnosticId);
//
//        void removeIneligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataList);
//
//        void updateTickerAnnouncementMessage();
//
//        void resetPromoBenefit();
//
//        void setPromoBenefit(List<SummariesItemUiModel> summariesUiModels);
//
//        boolean isTradeInByDropOff();
//
//        void updateButtonPromoCheckout(PromoUiModel promoUiModel, boolean isNeedToHitValidateFinal);
//
//        void doResetButtonPromoCheckout();
//
//        void resetCourier(ShipmentCartItemModel shipmentCartItemModel);
//
//        void setHasRunningApiCall(boolean hasRunningApiCall);
//
//        void prepareReloadRates(int lastSelectedCourierOrder, boolean skipMvc);
//
//        void updateLocalCacheAddressData(UserAddress userAddress);
//
//        void resetAllCourier();
//
//        void setStateLoadingCourierStateAtIndex(int index, boolean isLoading);
//
//        void logOnErrorLoadCheckoutPage(Throwable throwable);
//
//        void logOnErrorLoadCourier(Throwable throwable, int itemPosition, String boPromoCode);
//
//        void logOnErrorApplyBo(Throwable throwable, int itemPosition, String boPromoCode);
//
//        void logOnErrorApplyBo(Throwable throwable, ShipmentCartItemModel shipmentCartItemModel, String boPromoCode);
//
//        void logOnErrorCheckout(Throwable throwable, String request);
//
//        void showPopUp(PopUpData popUpData);
//
//        void updateAddOnsData(AddOnsDataModel addOnsDataModel, int identifier, String cartString);
//
//        void doCheckout();
//
//        void updateAddOnsDynamicDataPassing(AddOnsDataModel addOnsDataModel, AddOnResult addOnResult, int identifier, String cartString, Long cartId);
//
//        void onNeedUpdateViewItem(int position);
//
//        void renderUnapplyBoIncompleteShipment(List<String> unappliedBoPromoUniqueIds);
//
//        int getShipmentCartItemModelAdapterPositionByUniqueId(String uniqueId);
//
//        @Nullable
//        ShipmentCartItemModel getShipmentCartItemModel(int position);
//
//        ShipmentDetailData getShipmentDetailData(ShipmentCartItemModel shipmentCartItemModel,
//                                                 RecipientAddressModel recipientAddressModel);
//
//        void showPrescriptionReminderDialog(UploadPrescriptionUiModel uploadPrescriptionUiModel);
//
//        void updateUploadPrescription(UploadPrescriptionUiModel uploadPrescriptionUiModel);
//
//        void showCoachMarkEpharmacy(UploadPrescriptionUiModel epharmacyGroupIds);
//
//        void setShipmentNewUpsellLoading(boolean isLoading);
//    }
//
//    interface AnalyticsActionListener {
//        void sendAnalyticsChoosePaymentMethodFailed(String errorMessage);
//
//        void sendEnhancedEcommerceAnalyticsCheckout(Map<String, Object> stringObjectMap,
//                                                    Map<String, String> tradeInCustomDimension,
//                                                    String transactionId,
//                                                    String userId,
//                                                    boolean promoFlag,
//                                                    String eventCategory,
//                                                    String eventAction,
//                                                    String eventLabel);
//
//        void sendEnhancedEcommerceAnalyticsCrossSellClickPilihPembayaran(String eventLabel,
//                                                                         String userId,
//                                                                         List<Object> listProducts);
//
//        void sendAnalyticsOnClickChooseOtherAddressShipment();
//
//        void sendAnalyticsOnClickTopDonation();
//
//        void sendAnalyticsOnClickChangeAddress();
//
//        void sendAnalyticsOnClickSubtotal();
//
//        void sendAnalyticsOnClickCheckBoxDropShipperOption();
//
//        void sendAnalyticsOnClickCheckBoxInsuranceOption();
//
//        void sendAnalyticsScreenName(String screenName);
//
//        void sendAnalyticsOnClickEditPinPointErrorValidation(String message);
//
//        void sendAnalyticsCourierNotComplete();
//
//        void sendAnalyticsPromoRedState();
//
//        void sendAnalyticsDropshipperNotComplete();
//
//        void sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration();
//
//        void sendAnalyticsOnClickChecklistShipmentRecommendationDuration(String duration);
//
//        void sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(String courier);
//
//        void sendAnalyticsOnClickChangeCourierShipmentRecommendation(ShipmentCartItemModel shipmentCartItemModel);
//
//        void sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier();
//
//        void sendAnalyticsOnClickChangeDurationShipmentRecommendation();
//
//        void sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(int shippingProductId);
//
//        void sendAnalyticsOnDisplayDurationThatContainPromo(boolean isCourierPromo, String duration);
//
//        void sendAnalyticsOnDisplayLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId);
//
//        void sendAnalyticsOnClickDurationThatContainPromo(boolean isCourierPromo, String duration, boolean isCod, String shippingPriceMin, String shippingPriceHigh);
//
//        void sendAnalyticsOnClickLogisticThatContainPromo(boolean isCourierPromo, int shippingProductId, boolean isCod);
//
//        void sendAnalyticsViewInformationAndWarningTickerInCheckout(String tickerId);
//
//        void sendAnalyticsViewPromoAfterAdjustItem(String msg);
//    }
//
//    interface Presenter extends CustomerPresenter<View> {
//
//        PublishSubject<Boolean> getLogisticDonePublisher();
//
//        void setScheduleDeliveryMapData(String cartString, ShipmentScheduleDeliveryMapData shipmentScheduleDeliveryMapData);
//
//        void processInitialLoadCheckoutPage(boolean isReloadData, boolean isOneClickShipment,
//                                            boolean isTradeIn, boolean skipUpdateOnboardingState,
//                                            boolean isReloadAfterPriceChangeHinger,
//                                            String cornerId, String deviceId, String leasingId, boolean isPlusSelected);
//
//        void processCheckout(boolean isOneClickShipment, boolean isTradeIn,
//                             boolean isTradeInDropOff, String deviceId,
//                             String cornerId, String leasingId, boolean isPlusSelected);
//
//        void checkPromoCheckoutFinalShipment(ValidateUsePromoRequest validateUsePromoRequest, int lastSelectedCourierOrderIndex, String cartString);
//
//        void doValidateUseLogisticPromo(int cartPosition, String cartString, ValidateUsePromoRequest validateUsePromoRequest, String promoCode, boolean showLoading);
//
//        void processCheckPromoCheckoutCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast);
//
//        void processSaveShipmentState(ShipmentCartItemModel shipmentCartItemModel);
//
//        void processSaveShipmentState();
//
//        void processGetCourierRecommendation(int shipperId, int spId, int itemPosition,
//                                             ShipmentDetailData shipmentDetailData,
//                                             ShipmentCartItemModel shipmentCartItemModel,
//                                             List<ShopShipment> shopShipmentList,
//                                             boolean isInitialLoad, ArrayList<Product> products,
//                                             String cartString, boolean isTradeInDropOff,
//                                             RecipientAddressModel recipientAddressModel,
//                                             boolean isForceReload, boolean skipMvc);
//
//        RecipientAddressModel getRecipientAddressModel();
//
//        void setRecipientAddressModel(RecipientAddressModel recipientAddressModel);
//
//        List<ShipmentCartItemModel> getShipmentCartItemModelList();
//
//        void setShipmentCartItemModelList(List<ShipmentCartItemModel> recipientCartItemList);
//
//        void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList);
//
//        ShipmentCostModel getShipmentCostModel();
//
//        EgoldAttributeModel getEgoldAttributeModel();
//
//        ShipmentTickerErrorModel getShipmentTickerErrorModel();
//
//        TickerAnnouncementHolderData getTickerAnnouncementHolderData();
//
//        void setTickerAnnouncementHolderData(TickerAnnouncementHolderData tickerAnnouncementHolderData);
//
//        void setShipmentCostModel(ShipmentCostModel shipmentCostModel);
//
//        void setEgoldAttributeModel(EgoldAttributeModel egoldAttributeModel);
//
//        void editAddressPinpoint(String latitude, String longitude, ShipmentCartItemModel shipmentCartItemModel, LocationPass locationPass);
//
//        void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList);
//
//        void cancelAutoApplyPromoStackLogistic(int itemPosition, String promoCode, ShipmentCartItemModel shipmentCartItemModel);
//
//        void cancelAutoApplyPromoStackAfterClash(ClashingInfoDetailUiModel clashingInfoDetailUiModel);
//
//        void changeShippingAddress(RecipientAddressModel newRecipientAddressModel,
//                                   ChosenAddressModel chosenAddressModel,
//                                   boolean isOneClickShipment,
//                                   boolean isTradeInDropOff,
//                                   boolean isHandleFallback,
//                                   boolean reloadCheckoutPage);
//
//        void setShipmentDonationModel(ShipmentDonationModel shipmentDonationModel);
//
//        ShipmentDonationModel getShipmentDonationModel();
//
//        void setListShipmentCrossSellModel(ArrayList<ShipmentCrossSellModel> listShipmentCrossSellModel);
//
//        ArrayList<ShipmentCrossSellModel> getListShipmentCrossSellModel();
//
//        void setShipmentButtonPaymentModel(ShipmentButtonPaymentModel shipmentButtonPaymentModel);
//
//        ShipmentButtonPaymentModel getShipmentButtonPaymentModel();
//
//        void setShippingCourierViewModelsState(List<ShippingCourierUiModel> shippingCourierUiModelsState,
//                                               int orderNumber);
//
//        List<ShippingCourierUiModel> getShippingCourierViewModelsState(int orderNumber);
//
//        void setCouponStateChanged(boolean appliedCoupon);
//
//        boolean getCouponStateChanged();
//
//        CodModel getCodData();
//
//        CampaignTimerUi getCampaignTimer();
//
//        boolean isShowOnboarding();
//
//        void triggerSendEnhancedEcommerceCheckoutAnalytics(List<DataCheckoutRequest> dataCheckoutRequests,
//                                                           Map<String, String> tradeInCustomDimension,
//                                                           String step,
//                                                           String eventCategory,
//                                                           String eventAction,
//                                                           String eventLabel,
//                                                           String leasingId,
//                                                           String pageSource);
//
//        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(String cartString, String shippingDuration, String shippingPrice, String courierName);
//
//        List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(List<ShipmentCartItemModel> shipmentCartItemModels);
//
//        boolean isIneligiblePromoDialogEnabled();
//
//        CheckoutRequest generateCheckoutRequest(List<DataCheckoutRequest> analyticsDataCheckoutRequests, int isDonation, ArrayList<ShipmentCrossSellModel> crossSellModelArrayList, String leasingId);
//
//        void releaseBooking();
//
//        void fetchEpharmacyData();
//
//        void setPrescriptionIds(ArrayList<String> prescriptionIds);
//
//        void setMiniConsultationResult(ArrayList<EPharmacyMiniConsultationResult> results);
//
//        void setLastApplyData(LastApplyUiModel lastApplyData);
//
//        LastApplyUiModel getLastApplyData();
//
//        void setValidateUsePromoRevampUiModel(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel);
//
//        ValidateUsePromoRevampUiModel getValidateUsePromoRevampUiModel();
//
//        void setLatValidateUseRequest(ValidateUsePromoRequest latValidateUseRequest);
//
//        ValidateUsePromoRequest getLastValidateUseRequest();
//
//        void setUploadPrescriptionData(UploadPrescriptionUiModel uploadPrescriptionUiModel);
//
//        UploadPrescriptionUiModel getUploadPrescriptionUiModel();
//
//        String generateRatesMvcParam(String cartString);
//
//        String getCartDataForRates();
//
//        void setCheckoutData(CheckoutData checkoutData);
//
//        void updateAddOnProductLevelDataBottomSheet(SaveAddOnStateResult saveAddOnStateResult);
//
//        void updateAddOnOrderLevelDataBottomSheet(SaveAddOnStateResult saveAddOnStateResult);
//
//        ShipmentUpsellModel getShipmentUpsellModel();
//
//        ShipmentNewUpsellModel getShipmentNewUpsellModel();
//
//        Pair<ArrayList<String>, ArrayList<String>> validateBoPromo(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel);
//
//        void clearOrderPromoCodeFromLastValidateUseRequest(String uniqueId, String promoCode);
//
//        void validateClearAllBoPromo();
//
//        void doUnapplyBo(String uniqueId, String promoCode);
//
//        List<Product> getProductForRatesRequest(ShipmentCartItemModel shipmentCartItemModel);
//
//        void processBoPromoCourierRecommendation(int itemPosition, PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel, ShipmentCartItemModel shipmentCartItemModel);
//
//        void doApplyBo(PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel);
//
//        void hitClearAllBo();
//
//        void cancelUpsell(boolean isReloadData, boolean isOneClickShipment,
//                          boolean isTradeIn, boolean skipUpdateOnboardingState,
//                          boolean isReloadAfterPriceChangeHinger,
//                          String cornerId, String deviceId, String leasingId, boolean isPlusSelected);
//
//        void clearAllBoOnTemporaryUpsell();
//
//        boolean validatePrescriptionOnBackPressed();
//
//        void setDynamicDataParam(DynamicDataPassingParamRequest dynamicDataPassingParam);
//
//        DynamicDataPassingParamRequest getDynamicDataParam();
//
//        void validateDynamicData();
//
//        boolean isUsingDynamicDataPassing();
//
//        void updateDynamicData(DynamicDataPassingParamRequest dynamicDataPassingParamRequest, boolean isFireAndForget);
//void getDynamicPaymentFee(PaymentFeeCheckoutRequest paymentFeeCheckoutRequest);

//    ShipmentPlatformFeeData getShipmentPlatformFeeData();
//    }

}