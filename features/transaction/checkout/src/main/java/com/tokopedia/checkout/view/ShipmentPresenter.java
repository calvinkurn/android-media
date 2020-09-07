package com.tokopedia.checkout.view;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.checkout.data.api.CommonPurchaseApiUrl;
import com.tokopedia.checkout.data.model.request.DataChangeAddressRequest;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.SaveShipmentStateRequest;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateDropshipData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductPreorder;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShippingInfoData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShopProductData;
import com.tokopedia.checkout.data.model.response.ReleaseBookingResponse;
import com.tokopedia.checkout.data.model.response.cod.CodResponse;
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData;
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase;
import com.tokopedia.checkout.domain.usecase.CodCheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormGqlUseCase;
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase;
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase;
import com.tokopedia.checkout.utils.FingerprintUtil;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.checkout.view.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.helper.ShipmentCartItemModelHelper;
import com.tokopedia.checkout.view.subscriber.ClearNotEligiblePromoSubscriber;
import com.tokopedia.checkout.view.subscriber.ClearShipmentCacheAutoApplyAfterClashSubscriber;
import com.tokopedia.checkout.view.subscriber.GetCourierRecommendationSubscriber;
import com.tokopedia.checkout.view.subscriber.GetShipmentAddressFormSubscriber;
import com.tokopedia.checkout.view.subscriber.SaveShipmentStateSubscriber;
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel;
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.RatesParam;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingParam;
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase;
import com.tokopedia.logisticdata.data.analytics.CodAnalytics;
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData;
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException;
import com.tokopedia.purchase_platform.common.feature.checkout.request.CheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.CheckoutRequestGqlDataMapper;
import com.tokopedia.purchase_platform.common.feature.checkout.request.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.EgoldData;
import com.tokopedia.purchase_platform.common.feature.checkout.request.ProductDataCheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.PromoRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.RatesFeature;
import com.tokopedia.purchase_platform.common.feature.checkout.request.ShopProductCheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.checkout.request.TokopediaCornerData;
import com.tokopedia.purchase_platform.common.feature.cod.Data;
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.param.EditAddressParam;
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.usecase.EditAddressUseCase;
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest;
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult;
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase;
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartGqlResponse;
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ClashingInfoDetailUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoClashOptionUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoClashVoucherOrdersUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel;
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public class ShipmentPresenter extends BaseDaggerPresenter<ShipmentContract.View>
        implements ShipmentContract.Presenter {

    private static final long LAST_THREE_DIGIT_MODULUS = 1000;
    private final CheckoutGqlUseCase checkoutGqlUseCase;
    private final CompositeSubscription compositeSubscription;
    private final GetShipmentAddressFormGqlUseCase getShipmentAddressFormGqlUseCase;
    private final EditAddressUseCase editAddressUseCase;
    private final ChangeShippingAddressGqlUseCase changeShippingAddressGqlUseCase;
    private final SaveShipmentStateGqlUseCase saveShipmentStateGqlUseCase;
    private final GetRatesUseCase ratesUseCase;
    private final GetRatesApiUseCase ratesApiUseCase;
    private final ShippingCourierConverter shippingCourierConverter;
    private final CodCheckoutUseCase codCheckoutUseCase;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;
    private final SubmitHelpTicketUseCase submitHelpTicketUseCase;
    private final UserSessionInterface userSessionInterface;
    private final GetInsuranceCartUseCase getInsuranceCartUseCase;
    private final ShipmentDataConverter shipmentDataConverter;
    private final ReleaseBookingUseCase releaseBookingUseCase;
    private final ValidateUsePromoRevampUseCase validateUsePromoRevampUseCase;

    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private TickerAnnouncementHolderData tickerAnnouncementHolderData;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private EgoldAttributeModel egoldAttributeModel;
    private ShipmentDonationModel shipmentDonationModel;
    private ShipmentButtonPaymentModel shipmentButtonPaymentModel;
    private CodModel codData;
    private CampaignTimerUi campaignTimer;
    private Token token;
    private ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel;
    private ValidateUsePromoRequest lastValidateUsePromoRequest;

    private List<DataCheckoutRequest> dataCheckoutRequestList;
    private CheckoutData checkoutData;
    private boolean partialCheckout;
    private boolean couponStateChanged;
    private Map<Integer, List<ShippingCourierUiModel>> shippingCourierViewModelsState;
    private boolean isPurchaseProtectionPage = false;
    private boolean isShowOnboarding;
    private boolean isIneligiblePromoDialogEnabled;

    private ShipmentContract.AnalyticsActionListener analyticsActionListener;
    private CheckoutAnalyticsPurchaseProtection mTrackerPurchaseProtection;
    private CheckoutAnalyticsCourierSelection mTrackerShipment;
    private CodAnalytics mTrackerCod;
    private String PARAM_GLOBAL = "global";
    private String PARAM_MERCHANT = "merchant";
    private String PARAM_LOGISTIC = "logistic";
    private String statusOK = "OK";
    private RatesResponseStateConverter stateConverter;
    private LastApplyUiModel lastApplyData;

    @Inject
    public ShipmentPresenter(CompositeSubscription compositeSubscription,
                             CheckoutGqlUseCase checkoutGqlUseCase,
                             GetShipmentAddressFormGqlUseCase getShipmentAddressFormGqlUseCase,
                             EditAddressUseCase editAddressUseCase,
                             ChangeShippingAddressGqlUseCase changeShippingAddressGqlUseCase,
                             SaveShipmentStateGqlUseCase saveShipmentStateGqlUseCase,
                             GetRatesUseCase ratesUseCase,
                             GetRatesApiUseCase ratesApiUseCase,
                             CodCheckoutUseCase codCheckoutUseCase,
                             ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                             SubmitHelpTicketUseCase submitHelpTicketUseCase,
                             RatesResponseStateConverter stateConverter,
                             ShippingCourierConverter shippingCourierConverter,
                             ShipmentContract.AnalyticsActionListener shipmentAnalyticsActionListener,
                             UserSessionInterface userSessionInterface,
                             CheckoutAnalyticsPurchaseProtection analyticsPurchaseProtection,
                             CodAnalytics codAnalytics,
                             CheckoutAnalyticsCourierSelection checkoutAnalytics,
                             GetInsuranceCartUseCase getInsuranceCartUseCase,
                             ShipmentDataConverter shipmentDataConverter,
                             ReleaseBookingUseCase releaseBookingUseCase,
                             ValidateUsePromoRevampUseCase validateUsePromoRevampUseCase) {
        this.compositeSubscription = compositeSubscription;
        this.checkoutGqlUseCase = checkoutGqlUseCase;
        this.getShipmentAddressFormGqlUseCase = getShipmentAddressFormGqlUseCase;
        this.editAddressUseCase = editAddressUseCase;
        this.changeShippingAddressGqlUseCase = changeShippingAddressGqlUseCase;
        this.saveShipmentStateGqlUseCase = saveShipmentStateGqlUseCase;
        this.ratesUseCase = ratesUseCase;
        this.ratesApiUseCase = ratesApiUseCase;
        this.clearCacheAutoApplyStackUseCase = clearCacheAutoApplyStackUseCase;
        this.stateConverter = stateConverter;
        this.shippingCourierConverter = shippingCourierConverter;
        this.analyticsActionListener = shipmentAnalyticsActionListener;
        this.codCheckoutUseCase = codCheckoutUseCase;
        this.submitHelpTicketUseCase = submitHelpTicketUseCase;
        this.mTrackerPurchaseProtection = analyticsPurchaseProtection;
        this.userSessionInterface = userSessionInterface;
        this.mTrackerCod = codAnalytics;
        this.mTrackerShipment = checkoutAnalytics;
        this.getInsuranceCartUseCase = getInsuranceCartUseCase;
        this.shipmentDataConverter = shipmentDataConverter;
        this.releaseBookingUseCase = releaseBookingUseCase;
        this.validateUsePromoRevampUseCase = validateUsePromoRevampUseCase;
    }

    @Override
    public void attachView(ShipmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        if (ratesUseCase != null) {
            ratesUseCase.unsubscribe();
        }
        if (ratesApiUseCase != null) {
            ratesApiUseCase.unsubscribe();
        }
        if (codCheckoutUseCase != null) {
            codCheckoutUseCase.unsubscribe();
        }
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
    public void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList) {
        this.dataCheckoutRequestList = dataCheckoutRequestList;
    }

    @Override
    public ShipmentCostModel getShipmentCostModel() {
        if (shipmentCostModel == null) {
            shipmentCostModel = new ShipmentCostModel();
        }
        return shipmentCostModel;
    }

    @Override
    public TickerAnnouncementHolderData getTickerAnnouncementHolderData() {
        return tickerAnnouncementHolderData;
    }

    @Override
    public void setTickerAnnouncementHolderData(TickerAnnouncementHolderData tickerAnnouncementHolderData) {
        this.tickerAnnouncementHolderData = tickerAnnouncementHolderData;
    }

    @Override
    public EgoldAttributeModel getEgoldAttributeModel() {
        return egoldAttributeModel;
    }

    @Override
    public void setShipmentCostModel(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;

        if (getEgoldAttributeModel() != null && getEgoldAttributeModel().isEligible()) {
            updateEgoldBuyValue();
        }
    }

    @Override
    public void setEgoldAttributeModel(EgoldAttributeModel egoldAttributeModel) {
        this.egoldAttributeModel = egoldAttributeModel;
    }

    private void updateEgoldBuyValue() {

        long totalPrice = (long) shipmentCostModel.getTotalPrice();

        int valueTOCheck = 0;
        int buyEgoldValue = 0;

        if (egoldAttributeModel.isTiering()) {
            Collections.sort(egoldAttributeModel.getEgoldTieringModelArrayList(), (o1, o2) -> (int) (o1.getMinTotalAmount() - o2.getMinTotalAmount()));
            EgoldTieringModel egoldTieringModel = new EgoldTieringModel();
            for (EgoldTieringModel data : egoldAttributeModel.getEgoldTieringModelArrayList()) {
                if (totalPrice >= data.getMinTotalAmount()) {
                    valueTOCheck = (int) (totalPrice % data.getBasisAmount());
                    egoldTieringModel = data;
                }
            }
            buyEgoldValue = calculateBuyEgoldValue(valueTOCheck, (int) egoldTieringModel.getMinAmount(), (int) egoldTieringModel.getMaxAmount(), egoldTieringModel.getBasisAmount());
        } else {
            valueTOCheck = (int) (totalPrice % LAST_THREE_DIGIT_MODULUS);
            buyEgoldValue = calculateBuyEgoldValue(valueTOCheck, egoldAttributeModel.getMinEgoldRange(), egoldAttributeModel.getMaxEgoldRange(), LAST_THREE_DIGIT_MODULUS);

        }
        egoldAttributeModel.setBuyEgoldValue(buyEgoldValue);
        getView().renderDataChanged();
    }

    private int calculateBuyEgoldValue(int valueTOCheck, int minRange, int maxRange, long basisAmount) {

        if (basisAmount == 0) {
            return 0;
        }

        int buyEgoldValue = 0;
        for (int i = minRange; i <= maxRange; i++) {
            if ((valueTOCheck + i) % basisAmount == 0) {
                buyEgoldValue = i;
                break;
            }
        }
        return buyEgoldValue;
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
    public void setShipmentButtonPaymentModel(ShipmentButtonPaymentModel shipmentButtonPaymentModel) {
        this.shipmentButtonPaymentModel = shipmentButtonPaymentModel;
    }

    @Override
    public ShipmentButtonPaymentModel getShipmentButtonPaymentModel() {
        if (shipmentButtonPaymentModel == null) {
            shipmentButtonPaymentModel = new ShipmentButtonPaymentModel();
        }
        return shipmentButtonPaymentModel;
    }

    @Override
    public boolean isShowOnboarding() {
        return isShowOnboarding;
    }

    @Override
    public void triggerSendEnhancedEcommerceCheckoutAnalytics(List<DataCheckoutRequest> dataCheckoutRequests,
                                                              boolean hasInsurance,
                                                              String step,
                                                              String eventAction,
                                                              String eventLabel,
                                                              String leasingId) {
        CheckoutRequest checkoutRequest = generateCheckoutRequest(dataCheckoutRequests, hasInsurance,
                shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0, leasingId
        );
        Map<String, Object> eeDataLayer = generateCheckoutAnalyticsDataLayer(checkoutRequest, step);
        if (eeDataLayer != null) {
            String transactionId = "";
            if (checkoutData != null) {
                transactionId = checkoutData.getTransactionId();
            }
            analyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(eeDataLayer, transactionId, eventAction, eventLabel);
        }
    }

    @Override
    public List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(String cartString, String shippingDuration, String shippingPrice, String courierName) {
        List<DataCheckoutRequest> dataCheckoutRequests = dataCheckoutRequestList;
        if (dataCheckoutRequests == null) {
            dataCheckoutRequests = getView().generateNewCheckoutRequest(getShipmentCartItemModelList(), true);
        }

        for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequests) {
            if (dataCheckoutRequest.shopProducts != null) {
                boolean foundItem = false;
                for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.shopProducts) {
                    if (shopProductCheckoutRequest != null && shopProductCheckoutRequest.cartString.equalsIgnoreCase(cartString) && shopProductCheckoutRequest.productData != null) {
                        for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.productData) {
                            productDataCheckoutRequest.setShippingDuration(shippingDuration);
                            productDataCheckoutRequest.setShippingPrice(shippingPrice);
                            productDataCheckoutRequest.setCourier(courierName);
                        }
                        foundItem = true;
                        break;
                    }
                }
                if (foundItem) {
                    break;
                }
            }
        }

        return dataCheckoutRequests;
    }

    @Override
    public List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(List<ShipmentCartItemModel> shipmentCartItemModels) {
        List<DataCheckoutRequest> dataCheckoutRequests = dataCheckoutRequestList;
        if (dataCheckoutRequests == null) {
            dataCheckoutRequests = getView().generateNewCheckoutRequest(getShipmentCartItemModelList(), true);
        }

        if (dataCheckoutRequests != null) {
            StringBuilder promoCodes = new StringBuilder();
            StringBuilder promoDetails = new StringBuilder();

            if (validateUsePromoRevampUiModel != null) {
                if (validateUsePromoRevampUiModel.getPromoUiModel().getCodes().size() > 0) {
                    promoCodes.append(validateUsePromoRevampUiModel.getPromoUiModel().getCodes().get(0));
                    promoDetails.append(validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getState());
                }
            }

            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                if (shipmentCartItemModel != null) {
                    for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequests) {
                        if (dataCheckoutRequest.shopProducts != null) {
                            for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.shopProducts) {
                                if (shopProductCheckoutRequest != null && shopProductCheckoutRequest.cartString.equalsIgnoreCase(shipmentCartItemModel.getCartString()) &&
                                        shopProductCheckoutRequest.productData != null) {
                                    for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                                        for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.productData) {
                                            if (productDataCheckoutRequest.getProductId() == cartItemModel.getProductId()) {
                                                productDataCheckoutRequest.setPromoCode(cartItemModel.getAnalyticsProductCheckoutData().getPromoCode());
                                                productDataCheckoutRequest.setPromoDetails(cartItemModel.getAnalyticsProductCheckoutData().getPromoDetails());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return dataCheckoutRequests;
    }

    @Override
    public boolean isIneligiblePromoDialogEnabled() {
        return isIneligiblePromoDialogEnabled;
    }


    @Override
    public void getInsuranceTechCartOnCheckout() {
        getInsuranceCartUseCase.execute(getSubscriberInsuranceCart());
    }

    private Subscriber<GraphqlResponse> getSubscriberInsuranceCart() {
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().renderInsuranceCartData(null);
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {

                if (getView() == null) return;

                InsuranceCartGqlResponse insuranceCartGqlResponse = null;
                if (graphqlResponse != null &&
                        graphqlResponse.getData(InsuranceCartGqlResponse.class) != null) {
                    insuranceCartGqlResponse =
                            graphqlResponse.getData(InsuranceCartGqlResponse.class);
                    getView().renderInsuranceCartData(insuranceCartGqlResponse.getData());
                } else {
                    getView().renderInsuranceCartData(null);
                }

            }
        };
    }

    @Override
    public void processInitialLoadCheckoutPage(boolean isReloadData,
                                               boolean isOneClickShipment,
                                               boolean isTradeIn,
                                               boolean isSkipUpdateOnboardingState,
                                               boolean isReloadAfterPriceChangeHinger,
                                               @Nullable String cornerId,
                                               @Nullable String deviceId,
                                               @Nullable String leasingId) {
        if (isReloadData) {
            getView().showLoading();
        } else {
            getView().showInitialLoading();
        }

        Map<String, Object> params = generateShipmentAddressFormParams(
                isOneClickShipment, isTradeIn, isSkipUpdateOnboardingState, cornerId, deviceId, leasingId
        );

        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(params);
        compositeSubscription.add(
                getShipmentAddressFormGqlUseCase.createObservable(requestParams)
                        .subscribe(new GetShipmentAddressFormSubscriber(this, getView(),
                                isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment))
        );
    }

    @NotNull
    private Map<String, Object> generateShipmentAddressFormParams(boolean isOneClickShipment,
                                                                  boolean isTradeIn,
                                                                  boolean isSkipUpdateOnboardingState,
                                                                  @Nullable String cornerId,
                                                                  @Nullable String deviceId,
                                                                  @Nullable String leasingId) {
        Map<String, Object> params = new HashMap<>();
        params.put(GetShipmentAddressFormGqlUseCase.PARAM_KEY_LANG, "id");
        params.put(GetShipmentAddressFormGqlUseCase.PARAM_KEY_IS_ONE_CLICK_SHIPMENT, isOneClickShipment);
        params.put(GetShipmentAddressFormGqlUseCase.PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE, isSkipUpdateOnboardingState ? 1 : 0);
        if (cornerId != null) {
            try {
                int tmpCornerId = Integer.parseInt(cornerId);
                params.put(GetShipmentAddressFormGqlUseCase.PARAM_KEY_CORNER_ID, tmpCornerId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (leasingId != null && !leasingId.isEmpty()) {
            try {
                int tmpLeasingId = Integer.parseInt(leasingId);
                params.put(GetShipmentAddressFormGqlUseCase.PARAM_KEY_VEHICLE_LEASING_ID, tmpLeasingId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (isTradeIn) {
            params.put(GetShipmentAddressFormGqlUseCase.PARAM_KEY_IS_TRADEIN, true);
            params.put(GetShipmentAddressFormGqlUseCase.PARAM_KEY_DEVICE_ID, deviceId != null ? deviceId : "");
        }
        return params;
    }

    public void initializePresenterData(CartShipmentAddressFormData cartShipmentAddressFormData) {
        setLatValidateUseRequest(null);
        setValidateUsePromoRevampUiModel(null);

        if (getView().isInsuranceEnabled()) {
            getInsuranceTechCartOnCheckout();
        }

        if (cartShipmentAddressFormData.getTickerData() != null) {
            setTickerAnnouncementHolderData(
                    new TickerAnnouncementHolderData(String.valueOf(cartShipmentAddressFormData.getTickerData().getId()),
                            cartShipmentAddressFormData.getTickerData().getMessage())
            );
            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerAnnouncementHolderData.getId());
        } else {
            setTickerAnnouncementHolderData(null);
        }

        RecipientAddressModel newAddress = shipmentDataConverter
                .getRecipientAddressModel(cartShipmentAddressFormData);
        setRecipientAddressModel(newAddress);

        if (cartShipmentAddressFormData.getDonation() != null) {
            setShipmentDonationModel(shipmentDataConverter.getShipmentDonationModel(cartShipmentAddressFormData));
        } else {
            setShipmentDonationModel(null);
        }

        if (cartShipmentAddressFormData.getLastApplyData() != null) {
            setLastApplyData(cartShipmentAddressFormData.getLastApplyData());
        } else {
            setLastApplyData(null);
        }

        setShipmentCartItemModelList(shipmentDataConverter.getShipmentItems(
                cartShipmentAddressFormData, newAddress != null && newAddress.getLocationDataModel() != null)
        );

        this.codData = cartShipmentAddressFormData.getCod();

        this.campaignTimer = cartShipmentAddressFormData.getCampaignTimerUi();

        if (cartShipmentAddressFormData.isAvailablePurchaseProtection()) {
            isPurchaseProtectionPage = true;
            mTrackerPurchaseProtection.eventImpressionOfProduct();
        }

        setEgoldAttributeModel(cartShipmentAddressFormData.getEgoldAttributes());

        token = new Token();
        token.setUt(cartShipmentAddressFormData.getKeroUnixTime());
        token.setDistrictRecommendation(cartShipmentAddressFormData.getKeroDiscomToken());

        isShowOnboarding = cartShipmentAddressFormData.isShowOnboarding();
        isIneligiblePromoDialogEnabled = cartShipmentAddressFormData.isIneligiblePromoDialogEnabled();
    }

    private Map<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return originParams == null
                ? AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(), userSessionInterface.getDeviceId(), new TKPDMapParam<>())
                : AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(), userSessionInterface.getDeviceId(), originParams);
    }

    @Override
    public void processCheckout(boolean hasInsurance,
                                boolean isOneClickShipment,
                                boolean isTradeIn,
                                boolean isTradeInDropOff,
                                String deviceId,
                                String cornerId,
                                String leasingId) {
        getView().showLoading();
        removeErrorShopProduct();
        CheckoutRequest checkoutRequest = generateCheckoutRequest(null, hasInsurance,
                shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0, leasingId
        );

        if (checkoutRequest != null && checkoutRequest.data != null && checkoutRequest.data.size() > 0) {
            Map<String, Object> params = generateCheckoutParams(isOneClickShipment, isTradeIn, isTradeInDropOff, deviceId, checkoutRequest);
            RequestParams requestParams = RequestParams.create();
            requestParams.putAll(params);
            compositeSubscription.add(
                    checkoutGqlUseCase.createObservable(requestParams)
                            .subscribe(getSubscriberCheckoutCart(checkoutRequest, isOneClickShipment, isTradeIn, deviceId, cornerId, leasingId))
            );
        } else {
            getView().hideLoading();
            getView().showToastError(getView().getActivityContext().getString(R.string.message_error_checkout_empty));
        }
    }

    @NotNull
    private Map<String, Object> generateCheckoutParams(boolean isOneClickShipment,
                                                       boolean isTradeIn,
                                                       boolean isTradeInDropOff,
                                                       String deviceId,
                                                       CheckoutRequest checkoutRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put(CheckoutGqlUseCase.PARAM_CARTS, CheckoutRequestGqlDataMapper.INSTANCE.map(checkoutRequest));
        params.put(CheckoutGqlUseCase.PARAM_IS_ONE_CLICK_SHIPMENT, String.valueOf(isOneClickShipment));
        if (isTradeIn) {
            params.put(CheckoutGqlUseCase.PARAM_IS_TRADE_IN, true);
            params.put(CheckoutGqlUseCase.PARAM_IS_TRADE_IN_DROP_OFF, isTradeInDropOff);
            params.put(CheckoutGqlUseCase.PARAM_DEV_ID, deviceId);
        }
        params.put(CheckoutGqlUseCase.PARAM_OPTIONAL, 0);
        params.put(CheckoutGqlUseCase.PARAM_IS_THANKYOU_NATIVE, true);
        params.put(CheckoutGqlUseCase.PARAM_IS_THANKYOU_NATIVE_NEW, true);
        params.put(CheckoutGqlUseCase.PARAM_IS_EXPRESS, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && FingerprintUtil.getEnableFingerprintPayment(getView().getActivityContext())) {
            PublicKey publicKey = FingerPrintDialog.generatePublicKey(getView().getActivityContext());
            if (publicKey != null) {
                params.put(CheckoutGqlUseCase.PARAM_FINGERPRINT_PUBLICKEY, FingerPrintDialog.getPublicKey(publicKey));
                params.put(CheckoutGqlUseCase.PARAM_FINGERPRINT_SUPPORT, String.valueOf(true));
            } else {
                params.put(CheckoutGqlUseCase.PARAM_FINGERPRINT_SUPPORT, String.valueOf(false));
            }
        } else {
            params.put(CheckoutGqlUseCase.PARAM_FINGERPRINT_SUPPORT, String.valueOf(false));
        }
        return params;
    }

    private void removeErrorShopProduct() {
        List<ShipmentCartItemModel> newShipmentCartItemModelList = new ArrayList<>();
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            List<CartItemModel> cartItemModels = new ArrayList<>(shipmentCartItemModel.getCartItemModels());
            newShipmentCartItemModelList.add(ShipmentCartItemModel.clone(shipmentCartItemModel, cartItemModels));
        }

        boolean cartListHasError = false;
        ArrayList<ShipmentCartItemModel> indexShopErrorList = new ArrayList<>();
        Map<ShipmentCartItemModel, List<CartItemModel>> indexShopItemErrorMap = new HashMap<>();
        for (int i = 0; i < newShipmentCartItemModelList.size(); i++) {
            for (int j = 0; j < newShipmentCartItemModelList.get(i).getCartItemModels().size(); j++) {
                if (newShipmentCartItemModelList.get(i).getCartItemModels().get(j).isError()) {
                    newShipmentCartItemModelList.get(i).setError(true);
                }
            }
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
                    if (oldShipmentCartItemModel.equals(newShipmentCartItemModel)) {
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

            dataCheckoutRequestList = getView().generateNewCheckoutRequest(newShipmentCartItemModelList, false);
            partialCheckout = true;
        }
    }

    @Override
    public void checkPromoCheckoutFinalShipment(ValidateUsePromoRequest validateUsePromoRequest) {
        setCouponStateChanged(true);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);

        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ValidateUsePromoRevampUiModel>() {
                                       @Override
                                       public void onCompleted() {

                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           Timber.d(e);
                                           if (getView() != null) {
                                               getView().renderErrorCheckPromoShipmentData(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                           }
                                       }

                                       @Override
                                       public void onNext(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
                                           if (getView() != null) {
                                               ShipmentPresenter.this.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel;
                                               setCouponStateChanged(true);
                                               showErrorValidateUseIfAny(validateUsePromoRevampUiModel);
                                               validateBBO(validateUsePromoRevampUiModel);
                                               updateTickerAnnouncementData(validateUsePromoRevampUiModel);
                                               if (validateUsePromoRevampUiModel.getStatus().equalsIgnoreCase("ERROR")) {
                                                   String message = "";
                                                   if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                                                       message = validateUsePromoRevampUiModel.getMessage().get(0);
                                                   }
                                                   getView().renderErrorCheckPromoShipmentData(message);
                                                   getView().resetPromoBenefit();
                                                   getView().cancelAllCourierPromo();
                                               } else {
                                                   getView().updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel());
                                                   if (validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getState().equals("red")) {
                                                       analyticsActionListener.sendAnalyticsViewPromoAfterAdjustItem(validateUsePromoRevampUiModel.getPromoUiModel().getMessageUiModel().getText());
                                                   } else {
                                                       for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
                                                           if (voucherOrdersItemUiModel.getMessageUiModel().getState().equals("red")) {
                                                               analyticsActionListener.sendAnalyticsViewPromoAfterAdjustItem(voucherOrdersItemUiModel.getMessageUiModel().getText());
                                                               break;
                                                           }
                                                       }
                                                   }
                                               }

                                               ClashingInfoDetailUiModel clashingInfoDetailUiModel = validateUsePromoRevampUiModel.getPromoUiModel().getClashingInfoDetailUiModel();
                                               if (clashingInfoDetailUiModel.getClashMessage().length() > 0 ||
                                                       clashingInfoDetailUiModel.getClashReason().length() > 0 ||
                                                       clashingInfoDetailUiModel.getOptions().size() > 0) {

                                                   ArrayList<String> clashPromoCodes = new ArrayList<>();
                                                   for (PromoClashOptionUiModel promoClashOptionUiModel : clashingInfoDetailUiModel.getOptions()) {
                                                       if (promoClashOptionUiModel != null && promoClashOptionUiModel.getVoucherOrders() != null) {
                                                           for (PromoClashVoucherOrdersUiModel promoClashVoucherOrdersUiModel : promoClashOptionUiModel.getVoucherOrders()) {
                                                               clashPromoCodes.add(promoClashVoucherOrdersUiModel.getCode());
                                                           }
                                                       }
                                                   }

                                                   cancelAutoApplyPromoStackAfterClash(clashPromoCodes);
                                               }
                                           }
                                       }
                                   }
                        )
        );

    }

    private void updateTickerAnnouncementData(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
        if (!TextUtils.isEmpty(validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getMessage())) {
            if (tickerAnnouncementHolderData == null) {
                setTickerAnnouncementHolderData(
                        new TickerAnnouncementHolderData(
                                String.valueOf(validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getStatusCode()),
                                validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getMessage())
                );
            } else {
                tickerAnnouncementHolderData.setId(String.valueOf(validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getStatusCode()));
                tickerAnnouncementHolderData.setMessage(validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getMessage());
            }
            getView().updateTickerAnnouncementMessage();
            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerAnnouncementHolderData.getId());
        }
    }

    @NonNull
    private Subscriber<CheckoutData> getSubscriberCheckoutCart(CheckoutRequest checkoutRequest,
                                                               boolean isOneClickShipment,
                                                               boolean isTradeIn, String deviceId,
                                                               String cornerId, String leasingId) {
        return new Subscriber<CheckoutData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e);
                String errorMessage = e.getMessage();
                if (!(e instanceof CartResponseErrorException || e instanceof AkamaiErrorException)) {
                    errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(getView().getActivityContext(), e);
                }
                analyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(errorMessage);
                getView().showToastError(errorMessage);
                processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, cornerId, deviceId, leasingId);
            }

            @Override
            public void onNext(CheckoutData checkoutData) {
                ShipmentPresenter.this.checkoutData = checkoutData;
                getView().hideLoading();
                if (!checkoutData.isError()) {
                    getView().triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(checkoutData.getTransactionId());
                    if (isPurchaseProtectionPage) {
                        mTrackerPurchaseProtection.eventClickOnBuy(
                                checkoutRequest.isHavingPurchaseProtectionEnabled() ?
                                        ConstantTransactionAnalytics.EventLabel.SUCCESS_TICKED_PPP :
                                        ConstantTransactionAnalytics.EventLabel.SUCCESS_UNTICKED_PPP);
                    }
                    getView().renderCheckoutCartSuccess(checkoutData);
                } else if (checkoutData.getErrorReporter() != null && checkoutData.getErrorReporter().getEligible()) {
                    getView().renderCheckoutCartErrorReporter(checkoutData);
                } else if (checkoutData.getPriceValidationData() != null && checkoutData.getPriceValidationData().isUpdated() &&
                        checkoutData.getPriceValidationData().getMessage() != null) {
                    getView().renderCheckoutPriceUpdated(checkoutData.getPriceValidationData());
                } else {
                    analyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(checkoutData.getErrorMessage());
                    if (checkoutData.getErrorMessage() != null && !checkoutData.getErrorMessage().isEmpty()) {
                        getView().renderCheckoutCartError(checkoutData.getErrorMessage());
                    } else {
                        getView().renderCheckoutCartError(getView().getActivityContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown));
                    }
                    processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, cornerId, deviceId, leasingId);
                }
            }
        };
    }

    private Map<String, Object> generateCheckoutAnalyticsDataLayer(CheckoutRequest checkoutRequest, String step) {
        if (checkoutRequest != null) {
            Map<String, Object> checkoutMapData = new HashMap<>();
            EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
            enhancedECommerceActionField.setStep(step);
            String option = "";
            if (step.equalsIgnoreCase(EnhancedECommerceActionField.STEP_2)) {
                option = EnhancedECommerceActionField.STEP_2_OPTION_CHECKOUT_PAGE_LOADED;
            } else if (step.equalsIgnoreCase(EnhancedECommerceActionField.STEP_3)) {
                option = EnhancedECommerceActionField.STEP_3_OPTION_DATA_VALIDATION;
            } else if (step.equalsIgnoreCase(EnhancedECommerceActionField.STEP_4)) {
                option = EnhancedECommerceActionField.STEP_4_OPTION_CLICK_PAYMENT_OPTION_BUTTON;
            }
            enhancedECommerceActionField.setOption(option);

            EnhancedECommerceCheckout enhancedECommerceCheckout = new EnhancedECommerceCheckout();
            for (DataCheckoutRequest dataCheckoutRequest : checkoutRequest.data) {
                if (dataCheckoutRequest != null) {
                    for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.shopProducts) {
                        if (shopProductCheckoutRequest != null) {
                            for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.productData) {
                                if (productDataCheckoutRequest != null) {
                                    EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                                            new EnhancedECommerceProductCartMapData();
                                    enhancedECommerceProductCartMapData.setProductName(productDataCheckoutRequest.getProductName() != null ? productDataCheckoutRequest.getProductName() : "");
                                    enhancedECommerceProductCartMapData.setProductID(String.valueOf(productDataCheckoutRequest.getProductId()));
                                    enhancedECommerceProductCartMapData.setPrice(productDataCheckoutRequest.getProductPrice() != null ? productDataCheckoutRequest.getProductPrice() : "");
                                    enhancedECommerceProductCartMapData.setBrand("");
                                    enhancedECommerceProductCartMapData.setCategory(productDataCheckoutRequest.getProductCategory() != null ? productDataCheckoutRequest.getProductCategory() : "");
                                    enhancedECommerceProductCartMapData.setVariant("");
                                    enhancedECommerceProductCartMapData.setQty(productDataCheckoutRequest.getProductQuantity());
                                    enhancedECommerceProductCartMapData.setShopId(productDataCheckoutRequest.getProductShopId() != null ? productDataCheckoutRequest.getProductShopId() : "");
                                    enhancedECommerceProductCartMapData.setShopName(productDataCheckoutRequest.getProductShopName() != null ? productDataCheckoutRequest.getProductShopName() : "");
                                    enhancedECommerceProductCartMapData.setShopType(productDataCheckoutRequest.getProductShopType() != null ? productDataCheckoutRequest.getProductShopType() : "");
                                    enhancedECommerceProductCartMapData.setCategoryId(productDataCheckoutRequest.getProductCategoryId() != null ? productDataCheckoutRequest.getProductCategoryId() : "");
                                    enhancedECommerceProductCartMapData.setDimension38(productDataCheckoutRequest.getProductAttribution() != null ? productDataCheckoutRequest.getProductAttribution() : "");
                                    enhancedECommerceProductCartMapData.setDimension40(productDataCheckoutRequest.getProductListName() != null ? productDataCheckoutRequest.getProductListName() : "");
                                    enhancedECommerceProductCartMapData.setDimension45(String.valueOf(productDataCheckoutRequest.getCartId()));
                                    enhancedECommerceProductCartMapData.setDimension53(productDataCheckoutRequest.isDiscountedPrice());
                                    enhancedECommerceProductCartMapData.setDimension54(getFulfillmentStatus(shopProductCheckoutRequest.getShopId()));
                                    enhancedECommerceProductCartMapData.setDimension12(shopProductCheckoutRequest.shippingInfo.analyticsDataShippingCourierPrice != null ? shopProductCheckoutRequest.shippingInfo.analyticsDataShippingCourierPrice : "");
                                    enhancedECommerceProductCartMapData.setWarehouseId(productDataCheckoutRequest.getWarehouseId() != null ? productDataCheckoutRequest.getWarehouseId() : "");
                                    enhancedECommerceProductCartMapData.setProductWeight(productDataCheckoutRequest.getProductWeight() != null ? productDataCheckoutRequest.getProductWeight() : "");
                                    enhancedECommerceProductCartMapData.setPromoCode(productDataCheckoutRequest.getPromoCode() != null ? productDataCheckoutRequest.getPromoCode() : "");
                                    enhancedECommerceProductCartMapData.setPromoDetails(productDataCheckoutRequest.getPromoDetails() != null ? productDataCheckoutRequest.getPromoDetails() : "");
                                    enhancedECommerceProductCartMapData.setCartId(String.valueOf(productDataCheckoutRequest.getCartId()));
                                    enhancedECommerceProductCartMapData.setBuyerAddressId(productDataCheckoutRequest.getBuyerAddressId() != null ? productDataCheckoutRequest.getBuyerAddressId() : "");
                                    enhancedECommerceProductCartMapData.setCourier(productDataCheckoutRequest.getCourier() != null ? productDataCheckoutRequest.getCourier() : "");
                                    enhancedECommerceProductCartMapData.setShippingPrice(productDataCheckoutRequest.getShippingPrice() != null ? productDataCheckoutRequest.getShippingPrice() : "");
                                    enhancedECommerceProductCartMapData.setCodFlag(productDataCheckoutRequest.getCodFlag() != null ? productDataCheckoutRequest.getCodFlag() : "");
                                    enhancedECommerceProductCartMapData.setTokopediaCornerFlag(productDataCheckoutRequest.getTokopediaCornerFlag() != null ? productDataCheckoutRequest.getTokopediaCornerFlag() : "");
                                    enhancedECommerceProductCartMapData.setIsFulfillment(productDataCheckoutRequest.getIsFulfillment() != null ? productDataCheckoutRequest.getIsFulfillment() : "");
                                    enhancedECommerceProductCartMapData.setDimension83(productDataCheckoutRequest.isFreeShipping() ?
                                            EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR : EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);

                                    enhancedECommerceCheckout.addProduct(enhancedECommerceProductCartMapData.getProduct());
                                }
                            }
                        }
                    }
                }
            }
            enhancedECommerceCheckout.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
            enhancedECommerceCheckout.setActionField(enhancedECommerceActionField.getActionFieldMap());

            checkoutMapData.put(EnhancedECommerceCheckout.KEY_CHECKOUT, enhancedECommerceCheckout.getCheckoutMap());

            return checkoutMapData;
        }
        return null;
    }

    private boolean getFulfillmentStatus(int shopId) {
        for (ShipmentCartItemModel cartItemModel : shipmentCartItemModelList) {
            if (cartItemModel.getShopId() == shopId) {
                return cartItemModel.isFulfillment();
            }
        }
        return false;
    }

    @Override
    public void doValidateuseLogisticPromo(int cartPosition, String cartString, ValidateUsePromoRequest validateUsePromoRequest) {
        setCouponStateChanged(true);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);

        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ValidateUsePromoRevampUiModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.d(e);
                                if (getView() != null) {
                                    mTrackerShipment.eventClickLanjutkanTerapkanPromoError(e.getMessage());
                                    getView().showToastError(e.getMessage());
                                    getView().resetCourier(cartPosition);
                                }
                            }

                            @Override
                            public void onNext(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
                                ShipmentPresenter.this.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel;
                                if (getView() != null) {
                                    updateTickerAnnouncementData(validateUsePromoRevampUiModel);
                                    showErrorValidateUseIfAny(validateUsePromoRevampUiModel);
                                    validateBBO(validateUsePromoRevampUiModel);
                                    if (validateUsePromoRevampUiModel.getStatus().equalsIgnoreCase(statusOK)) {
                                        getView().updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel());
                                    } else {
                                        if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                                            String errMessage = validateUsePromoRevampUiModel.getMessage().get(0);
                                            mTrackerShipment.eventClickLanjutkanTerapkanPromoError(errMessage);
                                            PromoRevampAnalytics.INSTANCE.eventCheckoutViewPromoMessage(errMessage);
                                            getView().showToastError(errMessage);
                                            getView().resetCourier(cartPosition);
                                        }
                                    }
                                }
                            }
                        }));
    }

    private int getBBOCount(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
        int bboCount = 0;
        for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
            if (voucherOrdersItemUiModel.getType().equalsIgnoreCase("logistic")) {
                bboCount++;
            }
        }

        return bboCount;
    }

    private void showErrorValidateUseIfAny(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
        int bboCount = getBBOCount(validateUsePromoRevampUiModel);
        if (bboCount == 1) {
            for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
                if (voucherOrdersItemUiModel.getType().equalsIgnoreCase("logistic") && voucherOrdersItemUiModel.getMessageUiModel().getState().equalsIgnoreCase("red")) {
                    getView().showToastError(voucherOrdersItemUiModel.getMessageUiModel().getText());
                    return;
                }
            }
        }

        String messageInfo = validateUsePromoRevampUiModel.getPromoUiModel().getAdditionalInfoUiModel().getErrorDetailUiModel().getMessage();
        if (messageInfo.length() > 0) {
            getView().showToastError(messageInfo);
        }
    }

    private void validateBBO(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
        for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
            if (voucherOrdersItemUiModel.getType().equalsIgnoreCase("logistic") && voucherOrdersItemUiModel.getMessageUiModel().getState().equalsIgnoreCase("red")) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                    if (shipmentCartItemModel.getCartString().equals(voucherOrdersItemUiModel.getUniqueId())) {
                        if (getView() != null) {
                            getView().resetCourier(shipmentCartItemModel);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void processCheckPromoCheckoutCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast) {
        setCouponStateChanged(true);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, getView().generateValidateUsePromoRequest());

        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ValidateUsePromoRevampUiModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.d(e);
                                if (getView() != null) {
                                    if (e instanceof CheckPromoCodeException) {
                                        getView().showToastError(e.getMessage());
                                    } else {
                                        getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                    }
                                }
                            }

                            @Override
                            public void onNext(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
                                ShipmentPresenter.this.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel;
                                setCouponStateChanged(true);
                                if (getView() != null) {
                                    updateTickerAnnouncementData(validateUsePromoRevampUiModel);
                                    showErrorValidateUseIfAny(validateUsePromoRevampUiModel);
                                    validateBBO(validateUsePromoRevampUiModel);
                                    if (validateUsePromoRevampUiModel.getStatus().equalsIgnoreCase(statusOK)) {
                                        getView().renderPromoCheckoutFromCourierSuccess(validateUsePromoRevampUiModel, itemPosition, noToast);
                                    } else {
                                        if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                                            String errMessage = validateUsePromoRevampUiModel.getMessage().get(0);
                                            getView().renderErrorCheckPromoShipmentData(errMessage);
                                        }
                                    }
                                }
                            }
                        }));

    }

    @Override
    public CheckoutRequest generateCheckoutRequest(List<DataCheckoutRequest> analyticsDataCheckoutRequests,
                                                   boolean hasInsurance,
                                                   int isDonation,
                                                   String leasingId) {
        if (analyticsDataCheckoutRequests == null && dataCheckoutRequestList == null) {
            getView().showToastError(getView().getActivityContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown_short));
            return null;
        }

        if (validateUsePromoRevampUiModel != null) {
            if (dataCheckoutRequestList != null) {
                setCheckoutRequestPromoData(dataCheckoutRequestList);
            }

            if (analyticsDataCheckoutRequests != null) {
                setCheckoutRequestPromoData(analyticsDataCheckoutRequests);
            }
        }

        TokopediaCornerData cornerData = null;
        if (getRecipientAddressModel() != null && getRecipientAddressModel().isCornerAddress()) {
            cornerData = new TokopediaCornerData(
                    getRecipientAddressModel().getUserCornerId(),
                    Integer.parseInt(getRecipientAddressModel().getCornerId())
            );
        }

        EgoldData egoldData = new EgoldData();

        if (egoldAttributeModel != null && egoldAttributeModel.isEligible()) {
            egoldData.setEgold(egoldAttributeModel.isChecked());
            egoldData.setEgoldAmount(egoldAttributeModel.getBuyEgoldValue());
        }

        CheckoutRequest.Builder builder = new CheckoutRequest.Builder()
                .isDonation(isDonation)
                .hasInsurance(hasInsurance)
                .data(analyticsDataCheckoutRequests != null ? analyticsDataCheckoutRequests : dataCheckoutRequestList)
                .egoldData(egoldData);

        if (cornerData != null) {
            builder.cornerData(cornerData);
        }

        if (validateUsePromoRevampUiModel != null) {
            // Clear data first
            builder.promos(null);
            builder.promoCodes(null);

            // Then set the data promo global
            PromoUiModel promoUiModel = validateUsePromoRevampUiModel.getPromoUiModel();
            if (promoUiModel.getCodes().size() > 0 && !promoUiModel.getMessageUiModel().getState().equals("red")) {
                ArrayList<String> codes = new ArrayList<>(promoUiModel.getCodes());
                builder.promoCodes(codes);

                List<PromoRequest> promoRequests = new ArrayList<>();
                for (String promoCode : promoUiModel.getCodes()) {
                    PromoRequest promoRequest = new PromoRequest();
                    promoRequest.setCode(promoCode);
                    promoRequest.setType(PromoRequest.TYPE_GLOBAL);

                    promoRequests.add(promoRequest);
                }
                builder.promos(promoRequests);

            }

            builder.hasPromoStacking(true);
        }

        if (leasingId != null && !leasingId.isEmpty()) {
            builder.setLeasingId(Integer.parseInt(leasingId));
        }

        return builder.build();
    }

    private void setCheckoutRequestPromoData(List<DataCheckoutRequest> dataCheckoutRequestList) {
        // Clear data first
        for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequestList) {
            if (dataCheckoutRequest.shopProducts != null && dataCheckoutRequest.shopProducts.size() > 0) {
                for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.shopProducts) {
                    if (shopProductCheckoutRequest.promoCodes != null) {
                        shopProductCheckoutRequest.promoCodes.clear();
                    }
                    if (shopProductCheckoutRequest.promos != null) {
                        shopProductCheckoutRequest.promos.clear();
                    }
                }
            }
        }

        // Then set the data promo merchant & logistic
        for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequestList) {
            if (dataCheckoutRequest.shopProducts != null && dataCheckoutRequest.shopProducts.size() > 0) {
                for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.shopProducts) {
                    for (PromoCheckoutVoucherOrdersItemUiModel voucherOrder : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
                        if (shopProductCheckoutRequest.cartString.equals(voucherOrder.getUniqueId())) {
                            if (shopProductCheckoutRequest.promoCodes != null &&
                                    shopProductCheckoutRequest.promoCodes.size() > 0 &&
                                    !shopProductCheckoutRequest.promoCodes.contains(voucherOrder.getCode())) {
                                shopProductCheckoutRequest.promoCodes.add(voucherOrder.getCode());
                            } else {
                                ArrayList<String> codes = new ArrayList<>();
                                codes.add(voucherOrder.getCode());
                                shopProductCheckoutRequest.promoCodes = codes;
                            }

                            if (voucherOrder.getCode().length() > 0 && voucherOrder.getType().length() > 0) {
                                if (shopProductCheckoutRequest.promos != null && shopProductCheckoutRequest.promos.size() > 0 &&
                                        !hasInsertPromo(shopProductCheckoutRequest.promos, voucherOrder.getCode())) {
                                    PromoRequest promoRequest = new PromoRequest();
                                    promoRequest.setCode(voucherOrder.getCode());
                                    promoRequest.setType(voucherOrder.getType());

                                    shopProductCheckoutRequest.promos.add(promoRequest);
                                } else {
                                    PromoRequest promoRequest = new PromoRequest();
                                    promoRequest.setCode(voucherOrder.getCode());
                                    promoRequest.setType(voucherOrder.getType());

                                    List<PromoRequest> promoRequests = new ArrayList<>();
                                    promoRequests.add(promoRequest);

                                    shopProductCheckoutRequest.promos = promoRequests;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean hasInsertPromo(List<PromoRequest> promoRequests, String promoCode) {
        for (PromoRequest promoRequest : promoRequests) {
            if (promoRequest.getCode().equals(promoCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void processSaveShipmentState(ShipmentCartItemModel shipmentCartItemModel) {
        List<ShipmentCartItemModel> shipmentCartItemModels = new ArrayList<>();
        shipmentCartItemModels.add(shipmentCartItemModel);

        Map<String, Object> param = new HashMap<>();
        List<ShipmentStateRequestData> saveShipmentDataArray = getShipmentItemSaveStateData(shipmentCartItemModels);
        List<ShipmentStateRequestData> tmpSaveShipmentDataArray = new ArrayList<>();
        for (ShipmentStateRequestData requestData : saveShipmentDataArray) {
            if (requestData.getShopProductDataList() != null && !requestData.getShopProductDataList().isEmpty()) {
                tmpSaveShipmentDataArray.add(requestData);
            }
        }

        if (tmpSaveShipmentDataArray.isEmpty()) return;

        param.put(SaveShipmentStateGqlUseCase.PARAM_CARTS, tmpSaveShipmentDataArray);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT, param);

        compositeSubscription.add(saveShipmentStateGqlUseCase.createObservable(requestParams)
                .subscribe(new SaveShipmentStateSubscriber()));
    }

    @Override
    public void processSaveShipmentState() {
        Map<String, Object> param = new HashMap<>();
        List<ShipmentStateRequestData> saveShipmentDataArray = getShipmentItemSaveStateData(shipmentCartItemModelList);
        param.put(SaveShipmentStateGqlUseCase.PARAM_CARTS, saveShipmentDataArray);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT, param);

        getView().showLoading();
        compositeSubscription.add(saveShipmentStateGqlUseCase.createObservable(requestParams)
                .subscribe(new SaveShipmentStateSubscriber()));
    }

    private List<ShipmentStateRequestData> getShipmentItemSaveStateData(List<ShipmentCartItemModel> shipmentCartItemModels) {
        SaveShipmentStateRequest saveShipmentStateRequest = generateSaveShipmentStateRequestSingleAddress(shipmentCartItemModels);
        return saveShipmentStateRequest.getRequestDataList();
    }

    private SaveShipmentStateRequest generateSaveShipmentStateRequestSingleAddress(List<ShipmentCartItemModel> shipmentCartItemModels) {
        List<ShipmentStateShopProductData> shipmentStateShopProductDataList = new ArrayList<>();

        List<ShipmentStateRequestData> shipmentStateRequestDataList = new ArrayList<>();
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
            setSaveShipmentStateData(shipmentCartItemModel, shipmentStateShopProductDataList);
        }

        ShipmentStateRequestData shipmentStateRequestData = new ShipmentStateRequestData.Builder()
                .addressId(Integer.parseInt(recipientAddressModel.getId()))
                .shopProductDataList(shipmentStateShopProductDataList)
                .build();
        shipmentStateRequestDataList.add(shipmentStateRequestData);

        return new SaveShipmentStateRequest.Builder()
                .requestDataList(shipmentStateRequestDataList)
                .build();
    }

    private void setSaveShipmentStateData(ShipmentCartItemModel shipmentCartItemModel,
                                          List<ShipmentStateShopProductData> shipmentStateShopProductDataList) {
        if (shipmentCartItemModel == null) return;
        CourierItemData courierData = null;
        if (getView().isTradeInByDropOff()) {
            courierData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff();
        } else {
            courierData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
        }

        if (courierData != null) {
            List<ShipmentStateProductData> shipmentStateProductDataList = new ArrayList<>();
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                ShipmentStateProductData.Builder builder = new ShipmentStateProductData.Builder()
                        .productId(cartItemModel.getProductId());
                if (cartItemModel.isPreOrder()) {
                    ShipmentStateProductPreorder.Builder shipmentStateProductPreorder =
                            new ShipmentStateProductPreorder.Builder()
                                    .durationDay(cartItemModel.getPreOrderDurationDay());
                    builder.productPreorder(shipmentStateProductPreorder.build());
                }
                shipmentStateProductDataList.add(builder.build());
            }

            ShipmentStateDropshipData dropshipDataBuilder = new ShipmentStateDropshipData.Builder()
                    .name(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperName())
                    .telpNo(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperPhone())
                    .build();

            RatesFeature ratesFeature = ShipmentDataRequestConverter.generateRatesFeature(courierData);

            ShipmentStateShippingInfoData shippingInfoDataBuilder = new ShipmentStateShippingInfoData.Builder()
                    .shippingId(courierData.getShipperId())
                    .spId(courierData.getShipperProductId())
                    .ratesFeature(ratesFeature)
                    .build();

            ShipmentStateShopProductData.Builder builder = new ShipmentStateShopProductData.Builder()
                    .shopId(shipmentCartItemModel.getShopId())
                    .finsurance((shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance() != null &&
                            shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance()) ? 1 : 0)
                    .isDropship((shipmentCartItemModel.getSelectedShipmentDetailData().getUseDropshipper() != null &&
                            shipmentCartItemModel.getSelectedShipmentDetailData().getUseDropshipper()) ? 1 : 0)
                    .isOrderPriority((shipmentCartItemModel.getSelectedShipmentDetailData().isOrderPriority() != null &&
                            shipmentCartItemModel.getSelectedShipmentDetailData().isOrderPriority()) ? 1 : 0)
                    .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
                    .warehouseId(shipmentCartItemModel.getFulfillmentId())
                    .dropshipData(dropshipDataBuilder)
                    .shippingInfoData(shippingInfoDataBuilder)
                    .productDataList(shipmentStateProductDataList);
            shipmentStateShopProductDataList.add(builder.build());
        }
    }

    @Override
    public void editAddressPinpoint(final String latitude, final String longitude,
                                    ShipmentCartItemModel shipmentCartItemModel,
                                    LocationPass locationPass) {
        if (getView() != null) {
            getView().showLoading();
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
                                    Timber.d(e);
                                    if (getView() != null) {
                                        getView().hideLoading();
                                        getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                    }
                                }

                                @Override
                                public void onNext(String stringResponse) {
                                    if (getView() != null) {
                                        getView().hideLoading();
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
                                            Timber.d(e);
                                            statusSuccess = false;
                                        }

                                        if (response != null && statusSuccess) {
                                            if (recipientAddressModel != null) {
                                                recipientAddressModel.setLatitude(latitude);
                                                recipientAddressModel.setLongitude(longitude);
                                            } else {
                                                shipmentCartItemModel.getRecipientAddressModel().setLatitude(latitude);
                                                shipmentCartItemModel.getRecipientAddressModel().setLongitude(longitude);
                                            }
                                            getView().renderEditAddressSuccess(latitude, longitude);
                                        } else {
                                            if (TextUtils.isEmpty(messageError)) {
                                                messageError = getView().getActivityContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown);
                                            }
                                            getView().navigateToSetPinpoint(messageError, locationPass);
                                        }
                                    }
                                }
                            })
            );
        }
    }

    @NonNull
    private RequestParams generateEditAddressRequestParams(ShipmentCartItemModel
                                                                   shipmentCartItemModel,
                                                           String addressLatitude, String addressLongitude) {
        Map<String, String> params = getGeneratedAuthParamNetwork(null);

        String addressId = "";
        String addressName = "";
        String addressStreet = "";
        String postalCode = "";
        String districtId = "";
        String cityId = "";
        String provinceId = "";
        String latitude = "";
        String longitude = "";
        String receiverName = "";
        String receiverPhone = "";

        if (recipientAddressModel == null && shipmentCartItemModel != null && shipmentCartItemModel.getRecipientAddressModel() != null) {
            addressId = shipmentCartItemModel.getRecipientAddressModel().getId();
            addressName = shipmentCartItemModel.getRecipientAddressModel().getAddressName();
            addressStreet = shipmentCartItemModel.getRecipientAddressModel().getStreet();
            postalCode = shipmentCartItemModel.getRecipientAddressModel().getPostalCode();
            districtId = shipmentCartItemModel.getRecipientAddressModel().getDestinationDistrictId();
            cityId = shipmentCartItemModel.getRecipientAddressModel().getCityId();
            provinceId = shipmentCartItemModel.getRecipientAddressModel().getProvinceId();
            receiverName = shipmentCartItemModel.getRecipientAddressModel().getRecipientName();
            receiverPhone = shipmentCartItemModel.getRecipientAddressModel().getRecipientPhoneNumber();
        } else if (recipientAddressModel != null) {
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

        params.put(EditAddressParam.ADDRESS_ID, addressId);
        params.put(EditAddressParam.ADDRESS_NAME, addressName);
        params.put(EditAddressParam.ADDRESS_STREET, addressStreet);
        params.put(EditAddressParam.POSTAL_CODE, postalCode);
        params.put(EditAddressParam.DISTRICT_ID, districtId);
        params.put(EditAddressParam.CITY_ID, cityId);
        params.put(EditAddressParam.PROVINCE_ID, provinceId);
        params.put(EditAddressParam.LATITUDE, latitude);
        params.put(EditAddressParam.LONGITUDE, longitude);
        params.put(EditAddressParam.RECEIVER_NAME, receiverName);
        params.put(EditAddressParam.RECEIVER_PHONE, receiverPhone);

        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(params);

        return requestParams;
    }

    // Clear promo BBO after choose other / non BBO courier
    @Override
    public void cancelAutoApplyPromoStackLogistic(int itemPosition, String promoCode) {
        setCouponStateChanged(true);
        ArrayList<String> promoCodeList = new ArrayList<>();
        promoCodeList.add(promoCode);

        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), promoCodeList);
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe(new Subscriber<ClearPromoUiModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // Do nothing
                    }

                    @Override
                    public void onNext(ClearPromoUiModel responseData) {
                        if (getView() != null) {
                            if (!TextUtils.isEmpty(responseData.getSuccessDataModel().getTickerMessage())) {
                                tickerAnnouncementHolderData.setMessage(responseData.getSuccessDataModel().getTickerMessage());
                                getView().updateTickerAnnouncementMessage();
                            }
                            boolean isLastAppliedPromo = isLastAppliedPromo(promoCode);
                            if (isLastAppliedPromo) {
                                validateUsePromoRevampUiModel = null;
                            }
                            getView().onSuccessClearPromoLogistic(itemPosition, isLastAppliedPromo);
                        }
                    }
                })
        );
    }

    // Clear promo red state before checkout
    @Override
    public void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList,
                                       int checkoutType) {
        setCouponStateChanged(true);
        ArrayList<String> notEligiblePromoCodes = new ArrayList<>();
        for (NotEligiblePromoHolderdata notEligiblePromoHolderdata : notEligiblePromoHolderdataArrayList) {
            notEligiblePromoCodes.add(notEligiblePromoHolderdata.getPromoCode());
        }

        if (notEligiblePromoCodes.size() > 0) {
            getView().showLoading();
            clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), notEligiblePromoCodes);
            compositeSubscription.add(
                    clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                            .subscribe(new ClearNotEligiblePromoSubscriber(getView(), this, checkoutType, notEligiblePromoHolderdataArrayList))
            );
        }
    }

    // Clear promo after clash (rare, almost zero probability)
    @Override
    public void cancelAutoApplyPromoStackAfterClash(ArrayList<String> promoCodesToBeCleared) {
        setCouponStateChanged(true);
        getView().showLoading();
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), promoCodesToBeCleared);
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe(
                        new ClearShipmentCacheAutoApplyAfterClashSubscriber(getView(), this)
                )
        );
    }

    @Override
    public void changeShippingAddress(RecipientAddressModel newRecipientAddressModel,
                                      boolean isOneClickShipment,
                                      boolean isTradeInDropOff,
                                      boolean isHandleFallback) {
        getView().showLoading();
        List<DataChangeAddressRequest> dataChangeAddressRequests = new ArrayList<>();
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                    DataChangeAddressRequest dataChangeAddressRequest = new DataChangeAddressRequest();
                    dataChangeAddressRequest.setQuantity(cartItemModel.getQuantity());
                    dataChangeAddressRequest.setProductId(cartItemModel.getProductId());
                    dataChangeAddressRequest.setNotes(cartItemModel.getNoteToSeller());
                    dataChangeAddressRequest.setCartIdStr(String.valueOf(cartItemModel.getCartId()));
                    if (isTradeInDropOff) {
                        dataChangeAddressRequest.setAddressId(newRecipientAddressModel != null ?
                                newRecipientAddressModel.getLocationDataModel().getAddrId() : 0
                        );
                    } else {
                        dataChangeAddressRequest.setAddressId(newRecipientAddressModel != null ?
                                Integer.parseInt(newRecipientAddressModel.getId()) :
                                Integer.parseInt(shipmentCartItemModel.getRecipientAddressModel().getId())
                        );
                    }
                    dataChangeAddressRequests.add(dataChangeAddressRequest);
                }
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put(ChangeShippingAddressGqlUseCase.PARAM_CARTS, dataChangeAddressRequests);
        params.put(ChangeShippingAddressGqlUseCase.PARAM_ONE_CLICK_SHIPMENT, isOneClickShipment);

        RequestParams requestParam = RequestParams.create();
        requestParam.putObject(ChangeShippingAddressGqlUseCase.CHANGE_SHIPPING_ADDRESS_PARAMS, params);

        compositeSubscription.add(
                changeShippingAddressGqlUseCase.createObservable(requestParam)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<SetShippingAddressData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (getView() != null) {
                                    getView().hideLoading();
                                    Timber.d(e);
                                    getView().showToastError(
                                            ErrorHandler.getErrorMessage(getView().getActivityContext(), e)
                                    );
                                    if (isHandleFallback) {
                                        getView().renderChangeAddressFailed();
                                    }
                                }
                            }

                            @Override
                            public void onNext(SetShippingAddressData setShippingAddressData) {
                                if (getView() != null) {
                                    getView().hideLoading();
                                    if (setShippingAddressData.isSuccess()) {
                                        getView().showToastNormal(getView().getActivityContext().getString(R.string.label_change_address_success));
                                        getView().renderChangeAddressSuccess();
                                    } else {
                                        if (setShippingAddressData.getMessages() != null &&
                                                setShippingAddressData.getMessages().size() > 0) {
                                            StringBuilder stringBuilder = new StringBuilder();
                                            for (String errorMessage : setShippingAddressData.getMessages()) {
                                                stringBuilder.append(errorMessage).append(" ");
                                            }
                                            getView().showToastError(stringBuilder.toString());
                                            if (isHandleFallback) {
                                                getView().renderChangeAddressFailed();
                                            }
                                        } else {
                                            getView().showToastError(getView().getActivityContext().getString(R.string.label_change_address_failed));
                                            if (isHandleFallback) {
                                                getView().renderChangeAddressFailed();
                                            }
                                        }
                                    }
                                }
                            }
                        })
        );
    }

    @Override
    public void processGetCourierRecommendation(int shipperId, int spId, int itemPosition,
                                                ShipmentDetailData shipmentDetailData,
                                                ShipmentCartItemModel shipmentCartItemModel,
                                                List<ShopShipment> shopShipmentList,
                                                boolean isInitialLoad, ArrayList<Product> products,
                                                String cartString, boolean isTradeInDropOff,
                                                RecipientAddressModel recipientAddressModel) {
        ShippingParam shippingParam = getShippingParam(shipmentDetailData, products, cartString, isTradeInDropOff, recipientAddressModel);

        int counter = codData == null ? -1 : codData.getCounterCod();
        boolean cornerId = false;
        if (getRecipientAddressModel() != null) {
            cornerId = getRecipientAddressModel().isCornerAddress();
        }
        String pslCode = RatesDataConverter.getLogisticPromoCode(shipmentCartItemModel);
        boolean isLeasing = shipmentCartItemModel.getIsLeasingProduct();

        RatesParam param = new RatesParam.Builder(shopShipmentList, shippingParam)
                .isCorner(cornerId)
                .codHistory(counter)
                .isLeasing(isLeasing)
                .promoCode(pslCode)
                .build();

        Observable<ShippingRecommendationData> observable;
        if (isTradeInDropOff) {
            observable = ratesApiUseCase.execute(param);
        } else {
            observable = ratesUseCase.execute(param);
        }
        observable
                .map(shippingRecommendationData ->
                        stateConverter.fillState(shippingRecommendationData, shopShipmentList,
                                spId, 0))
                .subscribe(
                        new GetCourierRecommendationSubscriber(
                                getView(), this, shipperId, spId, itemPosition,
                                shippingCourierConverter, shipmentCartItemModel, shopShipmentList,
                                isInitialLoad, isTradeInDropOff
                        ));
    }

    @NonNull
    private ShippingParam getShippingParam(ShipmentDetailData shipmentDetailData,
                                           List<Product> products,
                                           String cartString,
                                           boolean isTradeInDropOff,
                                           RecipientAddressModel recipientAddressModel) {
        ShippingParam shippingParam = new ShippingParam();
        shippingParam.setOriginDistrictId(shipmentDetailData.getShipmentCartData().getOriginDistrictId());
        shippingParam.setOriginPostalCode(shipmentDetailData.getShipmentCartData().getOriginPostalCode());
        shippingParam.setOriginLatitude(shipmentDetailData.getShipmentCartData().getOriginLatitude());
        shippingParam.setOriginLongitude(shipmentDetailData.getShipmentCartData().getOriginLongitude());
        shippingParam.setWeightInKilograms(shipmentDetailData.getShipmentCartData().getWeight() / 1000);
        shippingParam.setShopId(shipmentDetailData.getShopId());
        shippingParam.setToken(shipmentDetailData.getShipmentCartData().getToken());
        shippingParam.setUt(shipmentDetailData.getShipmentCartData().getUt());
        shippingParam.setInsurance(shipmentDetailData.getShipmentCartData().getInsurance());
        shippingParam.setProductInsurance(shipmentDetailData.getShipmentCartData().getProductInsurance());
        shippingParam.setOrderValue(shipmentDetailData.getShipmentCartData().getOrderValue());
        shippingParam.setCategoryIds(shipmentDetailData.getShipmentCartData().getCategoryIds());
        shippingParam.setIsBlackbox(shipmentDetailData.getIsBlackbox());
        shippingParam.setIsPreorder(shipmentDetailData.getPreorder());
        shippingParam.setAddressId(shipmentDetailData.getAddressId());
        shippingParam.setTradein(shipmentDetailData.isTradein());
        shippingParam.setProducts(products);
        shippingParam.setUniqueId(cartString);
        shippingParam.setTradeInDropOff(isTradeInDropOff);

        if (isTradeInDropOff && recipientAddressModel.getLocationDataModel() != null) {
            shippingParam.setDestinationDistrictId(String.valueOf(recipientAddressModel.getLocationDataModel().getDistrict()));
            shippingParam.setDestinationPostalCode(recipientAddressModel.getLocationDataModel().getPostalCode());
            shippingParam.setDestinationLatitude(recipientAddressModel.getLocationDataModel().getLatitude());
            shippingParam.setDestinationLongitude(recipientAddressModel.getLocationDataModel().getLongitude());
        } else {
            shippingParam.setDestinationDistrictId(shipmentDetailData.getShipmentCartData().getDestinationDistrictId());
            shippingParam.setDestinationPostalCode(shipmentDetailData.getShipmentCartData().getDestinationPostalCode());
            shippingParam.setDestinationLatitude(shipmentDetailData.getShipmentCartData().getDestinationLatitude());
            shippingParam.setDestinationLongitude(shipmentDetailData.getShipmentCartData().getDestinationLongitude());
        }
        return shippingParam;
    }

    @Override
    public List<ShippingCourierUiModel> getShippingCourierViewModelsState(int itemPosition) {
        if (shippingCourierViewModelsState != null) {
            return shippingCourierViewModelsState.get(itemPosition);
        }
        return null;
    }

    @Override
    public void setShippingCourierViewModelsState
            (List<ShippingCourierUiModel> shippingCourierUiModelsState,
             int itemPosition) {
        if (this.shippingCourierViewModelsState == null) {
            this.shippingCourierViewModelsState = new HashMap<>();
        }
        this.shippingCourierViewModelsState.put(itemPosition, shippingCourierUiModelsState);
    }

    @Override
    public void setCouponStateChanged(boolean couponStateChanged) {
        this.couponStateChanged = couponStateChanged;
    }

    @Override
    public boolean getCouponStateChanged() {
        return couponStateChanged;
    }

    @Override
    public CodModel getCodData() {
        return this.codData;
    }

    @Override
    public CampaignTimerUi getCampaignTimer() {
        if (campaignTimer == null) return null;
        else {
            // Set necessary analytics attributes to be passed so the gtm will just trigger
            // the method without collecting the data again (quite expensive)
            campaignTimer.setGtmProductId(
                    ShipmentCartItemModelHelper.getFirstProductId(shipmentCartItemModelList)
            );
            campaignTimer.setGtmUserId(userSessionInterface.getUserId());
            return campaignTimer;
        }
    }

    @Override
    public void proceedCodCheckout(boolean hasInsurance,
                                   boolean isOneClickShipment, boolean isTradeIn,
                                   String deviceId, String leasingId) {
        CheckoutRequest checkoutRequest = generateCheckoutRequest(null, hasInsurance,
                shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0, leasingId
        );
        getView().showLoading();
        codCheckoutUseCase.clearRequest();
        codCheckoutUseCase.addRequest(codCheckoutUseCase.getRequest(checkoutRequest, isOneClickShipment));
        codCheckoutUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e);
                mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIncomplete();
                processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, null, deviceId, leasingId);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                getView().hideLoading();
                CodResponse response = graphqlResponse.getData(CodResponse.class);
                if (getView() == null || !response.getValidateCheckoutCod().getHeader().getErrorCode().equals("200")) {
                    mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIncomplete();
                    processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, null, deviceId, leasingId);
                    getView().showToastError("");
                } else if (response.getValidateCheckoutCod().getData() != null &&
                        response.getValidateCheckoutCod().getData().getData() != null) {
                    Data data = response.getValidateCheckoutCod().getData().getData();
                    if (TextUtils.isEmpty(data.getErrorMessage())
                            && data.getPriceSummary() != null && data.getPriceSummary().size() > 0) {
                        // validation succeeded, go to cod confirmation page
                        mTrackerCod.eventClickBayarDiTempatShipmentSuccessEligible();
                        getView().navigateToCodConfirmationPage(data, checkoutRequest);
                    } else {
                        // show bottomsheet error indicating cod ineligibility
                        mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIneligible();
                        getView().showBottomSheetError(data.getErrorMessage());
                    }
                }
            }
        });
    }

    @Override
    public Token getKeroToken() {
        return token;
    }

    @NotNull
    @Override
    public ShipmentDataConverter getShipmentDataConverter() {
        return shipmentDataConverter;
    }

    @Override
    public void releaseBooking() {
        // As deals product is using OCS, the shipment should only contain 1 product
        int productId = ShipmentCartItemModelHelper.getFirstProductId(shipmentCartItemModelList);
        if (productId != 0) {
            compositeSubscription.add(releaseBookingUseCase
                    .execute(productId)
                    .subscribe(new Subscriber<ReleaseBookingResponse>() {
                        @Override
                        public void onCompleted() {
                            // no op
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d(e);
                        }

                        @Override
                        public void onNext(ReleaseBookingResponse releaseBookingResponse) {
                            Timber.d("Release Booking Success %s", releaseBookingResponse.getData());
                        }
                    }));
        }

    }

    @Override
    public void setLastApplyData(LastApplyUiModel lastApplyData) {
        this.lastApplyData = lastApplyData;
    }

    @Override
    public LastApplyUiModel getLastApplyData() {
        return lastApplyData;
    }

    @Override
    public void setValidateUsePromoRevampUiModel(ValidateUsePromoRevampUiModel
                                                         validateUsePromoRevampUiModel) {
        this.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel;
    }

    @Override
    public ValidateUsePromoRevampUiModel getValidateUsePromoRevampUiModel() {
        return validateUsePromoRevampUiModel;
    }

    @Override
    public void setLatValidateUseRequest(ValidateUsePromoRequest latValidateUseRequest) {
        this.lastValidateUsePromoRequest = latValidateUseRequest;
    }

    @Override
    public ValidateUsePromoRequest getLastValidateUseRequest() {
        return lastValidateUsePromoRequest;
    }

    @Override
    public void processSubmitHelpTicket(CheckoutData checkoutData) {
        getView().showLoading();
        RequestParams requestParams = RequestParams.create();
        SubmitHelpTicketRequest submitHelpTicketRequest = new SubmitHelpTicketRequest();
        submitHelpTicketRequest.setApiJsonResponse(checkoutData.getJsonResponse());
        submitHelpTicketRequest.setErrorMessage(checkoutData.getErrorReporter().getTexts().getSubmitDescription());
        submitHelpTicketRequest.setHeaderMessage(checkoutData.getErrorMessage());
        submitHelpTicketRequest.setPage(SubmitHelpTicketUseCase.PAGE_CHECKOUT);
        submitHelpTicketRequest.setRequestUrl(CommonPurchaseApiUrl.PATH_CHECKOUT);
        requestParams.putObject(SubmitHelpTicketUseCase.PARAM, submitHelpTicketRequest);
        compositeSubscription.add(
                submitHelpTicketUseCase.createObservable(requestParams).subscribe(new Subscriber<SubmitTicketResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d(e);
                        getView().hideLoading();
                        getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                    }

                    @Override
                    public void onNext(SubmitTicketResult submitTicketResult) {
                        getView().hideLoading();
                        if (submitTicketResult.getStatus()) {
                            getView().renderSubmitHelpTicketSuccess(submitTicketResult);
                        } else {
                            getView().showToastError(submitTicketResult.getMessage());
                        }
                    }
                }));
    }

    private boolean isLastAppliedPromo(String promoCode) {
        if (validateUsePromoRevampUiModel != null) {
            List<PromoCheckoutVoucherOrdersItemUiModel> voucherOrders = validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels();
            if (voucherOrders.size() > 0) {
                for (PromoCheckoutVoucherOrdersItemUiModel voucherOrder : voucherOrders) {
                    if (!voucherOrder.getCode().equals(promoCode))
                        return false;
                }
            }

            List<String> codes = validateUsePromoRevampUiModel.getPromoUiModel().getCodes();
            if (!codes.isEmpty()) {
                for (String code : codes) {
                    if (!code.equals(promoCode)) return false;
                }
            }
        }

        return true;
    }

}
