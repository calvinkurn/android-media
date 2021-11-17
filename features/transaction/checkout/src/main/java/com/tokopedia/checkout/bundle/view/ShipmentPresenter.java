package com.tokopedia.checkout.bundle.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.bundle.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.checkout.bundle.data.api.CommonPurchaseApiUrl;
import com.tokopedia.checkout.bundle.data.model.request.changeaddress.DataChangeAddressRequest;
import com.tokopedia.checkout.bundle.data.model.request.checkout.CheckoutRequestMapper;
import com.tokopedia.checkout.bundle.data.model.request.checkout.cross_sell.CrossSellItemRequestModel;
import com.tokopedia.checkout.bundle.data.model.request.checkout.cross_sell.CrossSellRequest;
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.CheckoutRequest;
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.DataCheckoutRequest;
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.EgoldData;
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.ProductDataCheckoutRequest;
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.PromoRequest;
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.ShopProductCheckoutRequest;
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.TokopediaCornerData;
import com.tokopedia.checkout.bundle.data.model.request.common.RatesFeature;
import com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate.SaveShipmentStateRequest;
import com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate.ShipmentStateDropshipData;
import com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate.ShipmentStateProductData;
import com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate.ShipmentStateProductPreorder;
import com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate.ShipmentStateRequestData;
import com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate.ShipmentStateShippingInfoData;
import com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate.ShipmentStateShopProductData;
import com.tokopedia.checkout.bundle.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.checkout.bundle.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.bundle.domain.model.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.bundle.domain.model.changeaddress.SetShippingAddressData;
import com.tokopedia.checkout.bundle.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.bundle.domain.usecase.ChangeShippingAddressGqlUseCase;
import com.tokopedia.checkout.bundle.domain.usecase.CheckoutGqlUseCase;
import com.tokopedia.checkout.bundle.domain.usecase.GetShipmentAddressFormV3UseCase;
import com.tokopedia.checkout.bundle.domain.usecase.ReleaseBookingUseCase;
import com.tokopedia.checkout.bundle.domain.usecase.SaveShipmentStateGqlUseCase;
import com.tokopedia.checkout.bundle.utils.CheckoutFingerprintUtil;
import com.tokopedia.checkout.bundle.view.converter.RatesDataConverter;
import com.tokopedia.checkout.bundle.view.converter.ShipmentDataConverter;
import com.tokopedia.checkout.bundle.view.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.bundle.view.helper.ShipmentCartItemModelHelper;
import com.tokopedia.checkout.bundle.view.subscriber.ClearNotEligiblePromoSubscriber;
import com.tokopedia.checkout.bundle.view.subscriber.ClearShipmentCacheAutoApplyAfterClashSubscriber;
import com.tokopedia.checkout.bundle.view.subscriber.GetCourierRecommendationSubscriber;
import com.tokopedia.checkout.bundle.view.subscriber.ReleaseBookingStockSubscriber;
import com.tokopedia.checkout.bundle.view.subscriber.SaveShipmentStateSubscriber;
import com.tokopedia.checkout.bundle.view.uimodel.CrossSellModel;
import com.tokopedia.checkout.bundle.view.uimodel.EgoldAttributeModel;
import com.tokopedia.checkout.bundle.view.uimodel.EgoldTieringModel;
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentCostModel;
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentCrossSellModel;
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentDonationModel;
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentTickerErrorModel;
import com.tokopedia.fingerprint.util.FingerPrintUtil;
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.address.UserAddress;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticCommon.domain.param.EditAddressParam;
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter;
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData;
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
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData;
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException;
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest;
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult;
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ClashingInfoDetailUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoClashOptionUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoClashVoucherOrdersUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel;
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData;
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.tokopedia.checkout.bundle.data.model.request.checkout.CheckoutRequestKt.FEATURE_TYPE_REGULAR_PRODUCT;
import static com.tokopedia.checkout.bundle.data.model.request.checkout.CheckoutRequestKt.FEATURE_TYPE_TOKONOW_PRODUCT;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public class ShipmentPresenter extends BaseDaggerPresenter<ShipmentContract.View>
        implements ShipmentContract.Presenter {

    private static final long LAST_THREE_DIGIT_MODULUS = 1000;
    private final CheckoutGqlUseCase checkoutGqlUseCase;
    private final CompositeSubscription compositeSubscription;
    private final GetShipmentAddressFormV3UseCase getShipmentAddressFormV3UseCase;
    private final EditAddressUseCase editAddressUseCase;
    private final ChangeShippingAddressGqlUseCase changeShippingAddressGqlUseCase;
    private final SaveShipmentStateGqlUseCase saveShipmentStateGqlUseCase;
    private final GetRatesUseCase ratesUseCase;
    private final GetRatesApiUseCase ratesApiUseCase;
    private final ShippingCourierConverter shippingCourierConverter;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;
    private final SubmitHelpTicketUseCase submitHelpTicketUseCase;
    private final UserSessionInterface userSessionInterface;
    private final ShipmentDataConverter shipmentDataConverter;
    private final ReleaseBookingUseCase releaseBookingUseCase;
    private final ValidateUsePromoRevampUseCase validateUsePromoRevampUseCase;
    private final ExecutorSchedulers executorSchedulers;

    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private ShipmentTickerErrorModel shipmentTickerErrorModel = new ShipmentTickerErrorModel();
    private TickerAnnouncementHolderData tickerAnnouncementHolderData = new TickerAnnouncementHolderData();
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private EgoldAttributeModel egoldAttributeModel;
    private ShipmentDonationModel shipmentDonationModel;
    private ArrayList<ShipmentCrossSellModel> listShipmentCrossSellModel;
    private ShipmentButtonPaymentModel shipmentButtonPaymentModel;
    private CodModel codData;
    private CampaignTimerUi campaignTimer;
    private ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel;
    private ValidateUsePromoRequest lastValidateUsePromoRequest;
    private Gson gson;

    private List<DataCheckoutRequest> dataCheckoutRequestList;
    private CheckoutData checkoutData;
    private boolean couponStateChanged;
    private Map<Integer, List<ShippingCourierUiModel>> shippingCourierViewModelsState;
    private boolean isPurchaseProtectionPage = false;
    private boolean isShowOnboarding;
    private boolean isIneligiblePromoDialogEnabled;

    private ShipmentContract.AnalyticsActionListener analyticsActionListener;
    private CheckoutAnalyticsPurchaseProtection mTrackerPurchaseProtection;
    private CheckoutAnalyticsCourierSelection mTrackerShipment;
    private String statusOK = "OK";
    private RatesResponseStateConverter stateConverter;
    private LastApplyUiModel lastApplyData;

    @Inject
    public ShipmentPresenter(CompositeSubscription compositeSubscription,
                             CheckoutGqlUseCase checkoutGqlUseCase,
                             GetShipmentAddressFormV3UseCase getShipmentAddressFormV3UseCase,
                             EditAddressUseCase editAddressUseCase,
                             ChangeShippingAddressGqlUseCase changeShippingAddressGqlUseCase,
                             SaveShipmentStateGqlUseCase saveShipmentStateGqlUseCase,
                             GetRatesUseCase ratesUseCase,
                             GetRatesApiUseCase ratesApiUseCase,
                             ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                             SubmitHelpTicketUseCase submitHelpTicketUseCase,
                             RatesResponseStateConverter stateConverter,
                             ShippingCourierConverter shippingCourierConverter,
                             ShipmentContract.AnalyticsActionListener shipmentAnalyticsActionListener,
                             UserSessionInterface userSessionInterface,
                             CheckoutAnalyticsPurchaseProtection analyticsPurchaseProtection,
                             CheckoutAnalyticsCourierSelection checkoutAnalytics,
                             ShipmentDataConverter shipmentDataConverter,
                             ReleaseBookingUseCase releaseBookingUseCase,
                             ValidateUsePromoRevampUseCase validateUsePromoRevampUseCase,
                             Gson gson,
                             ExecutorSchedulers executorSchedulers) {
        this.compositeSubscription = compositeSubscription;
        this.checkoutGqlUseCase = checkoutGqlUseCase;
        this.getShipmentAddressFormV3UseCase = getShipmentAddressFormV3UseCase;
        this.editAddressUseCase = editAddressUseCase;
        this.changeShippingAddressGqlUseCase = changeShippingAddressGqlUseCase;
        this.saveShipmentStateGqlUseCase = saveShipmentStateGqlUseCase;
        this.ratesUseCase = ratesUseCase;
        this.ratesApiUseCase = ratesApiUseCase;
        this.clearCacheAutoApplyStackUseCase = clearCacheAutoApplyStackUseCase;
        this.stateConverter = stateConverter;
        this.shippingCourierConverter = shippingCourierConverter;
        this.analyticsActionListener = shipmentAnalyticsActionListener;
        this.submitHelpTicketUseCase = submitHelpTicketUseCase;
        this.mTrackerPurchaseProtection = analyticsPurchaseProtection;
        this.userSessionInterface = userSessionInterface;
        this.mTrackerShipment = checkoutAnalytics;
        this.shipmentDataConverter = shipmentDataConverter;
        this.releaseBookingUseCase = releaseBookingUseCase;
        this.validateUsePromoRevampUseCase = validateUsePromoRevampUseCase;
        this.gson = gson;
        this.executorSchedulers = executorSchedulers;
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
    public ShipmentTickerErrorModel getShipmentTickerErrorModel() {
        return shipmentTickerErrorModel;
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

    public List<DataCheckoutRequest> getDataCheckoutRequestList() {
        return dataCheckoutRequestList;
    }

    @Override
    public void setEgoldAttributeModel(EgoldAttributeModel egoldAttributeModel) {
        this.egoldAttributeModel = egoldAttributeModel;
    }

    public void updateEgoldBuyValue() {

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
    public ArrayList<ShipmentCrossSellModel> getListShipmentCrossSellModel() {
        if (listShipmentCrossSellModel == null) {
            listShipmentCrossSellModel = new ArrayList<>();
        }
        return listShipmentCrossSellModel;
    }

    @Override
    public void setListShipmentCrossSellModel(ArrayList<ShipmentCrossSellModel> listShipmentCrossSellModel) {
        this.listShipmentCrossSellModel = listShipmentCrossSellModel;
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
                                                              Map<String, String> tradeInCustomDimension,
                                                              String step,
                                                              String eventCategory,
                                                              String eventAction,
                                                              String eventLabel,
                                                              String leasingId,
                                                              String pageSource) {
        CheckoutRequest checkoutRequest = generateCheckoutRequest(
                dataCheckoutRequests, shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0,
                listShipmentCrossSellModel, leasingId
        );
        Map<String, Object> eeDataLayer = generateCheckoutAnalyticsDataLayer(checkoutRequest, step, pageSource);
        if (eeDataLayer != null) {
            String transactionId = "";
            if (checkoutData != null) {
                transactionId = checkoutData.getTransactionId();
            }
            analyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
                    eeDataLayer, tradeInCustomDimension, transactionId, eventCategory, eventAction, eventLabel
            );
        }
    }

    @Override
    public List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(String cartString, String shippingDuration, String shippingPrice, String courierName) {
        List<DataCheckoutRequest> dataCheckoutRequests = dataCheckoutRequestList;
        if (dataCheckoutRequests == null) {
            dataCheckoutRequests = getView().generateNewCheckoutRequest(getShipmentCartItemModelList(), true);
        }

        for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequests) {
            if (dataCheckoutRequest.getShopProducts() != null) {
                boolean foundItem = false;
                for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.getShopProducts()) {
                    if (shopProductCheckoutRequest != null && shopProductCheckoutRequest.getCartString().equalsIgnoreCase(cartString) && shopProductCheckoutRequest.getProductData() != null) {
                        for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.getProductData()) {
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
                        if (dataCheckoutRequest.getShopProducts() != null) {
                            for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.getShopProducts()) {
                                if (shopProductCheckoutRequest != null && shopProductCheckoutRequest.getCartString().equalsIgnoreCase(shipmentCartItemModel.getCartString()) &&
                                        shopProductCheckoutRequest.getProductData() != null) {
                                    for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                                        for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.getProductData()) {
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
    public void processInitialLoadCheckoutPage(boolean isReloadData,
                                               boolean isOneClickShipment,
                                               boolean isTradeIn,
                                               boolean isSkipUpdateOnboardingState,
                                               boolean isReloadAfterPriceChangeHinger,
                                               @Nullable String cornerId,
                                               @Nullable String deviceId,
                                               @Nullable String leasingId) {
        if (getView().isBundleToggleChanged()) {
            getView().recreateActivity();
            return;
        }

        if (isReloadData) {
            getView().setHasRunningApiCall(true);
            getView().showLoading();
        } else {
            getView().showInitialLoading();
        }

        getShipmentAddressFormV3UseCase.setParams(
                isOneClickShipment, isTradeIn, isSkipUpdateOnboardingState, cornerId, deviceId, leasingId
        );
        getShipmentAddressFormV3UseCase.execute(
                cartShipmentAddressFormData -> {
                    if (getView() != null) {
                        if (isReloadData) {
                            getView().setHasRunningApiCall(false);
                            getView().resetPromoBenefit();
                            getView().clearTotalBenefitPromoStacking();
                            getView().hideLoading();
                        } else {
                            getView().hideInitialLoading();
                        }

                        validateShipmentAddressFormData(cartShipmentAddressFormData, isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment);
                        getView().stopTrace();
                    }
                    return Unit.INSTANCE;
                }, throwable -> {
                    Timber.d(throwable);
                    if (getView() != null) {
                        if (isReloadData) {
                            getView().setHasRunningApiCall(false);
                            getView().hideLoading();
                        } else {
                            getView().hideInitialLoading();
                        }
                        String errorMessage = throwable.getMessage();
                        if (!(throwable instanceof CartResponseErrorException) && !(throwable instanceof AkamaiErrorException)) {
                            errorMessage = ErrorHandler.getErrorMessage(getView().getActivityContext(), throwable);
                        }
                        getView().showToastError(errorMessage);
                        getView().stopTrace();
                        getView().logOnErrorLoadCheckoutPage(throwable);
                    }
                    return Unit.INSTANCE;
                }
        );
    }

    private void validateShipmentAddressFormData(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                 boolean isReloadData,
                                                 boolean isReloadAfterPriceChangeHigher,
                                                 boolean isOneClickShipment) {
        if (cartShipmentAddressFormData == null) {
            getView().onShipmentAddressFormEmpty();
        } else {
            if (cartShipmentAddressFormData.isError()) {
                if (cartShipmentAddressFormData.isOpenPrerequisiteSite()) {
                    getView().onCacheExpired(cartShipmentAddressFormData.getErrorMessage());
                } else {
                    getView().showToastError(cartShipmentAddressFormData.getErrorMessage());
                    getView().logOnErrorLoadCheckoutPage(new MessageErrorException(cartShipmentAddressFormData.getErrorMessage()));
                }
            } else {
                List<GroupAddress> groupAddressList = cartShipmentAddressFormData.getGroupAddress();
                if (groupAddressList.size() > 0) {
                    UserAddress userAddress = groupAddressList.get(0).getUserAddress();
                    validateRenderCheckoutPage(cartShipmentAddressFormData, userAddress, isReloadData, isReloadAfterPriceChangeHigher, isOneClickShipment);
                } else {
                    validateRenderCheckoutPage(cartShipmentAddressFormData, null, isReloadData, isReloadAfterPriceChangeHigher, isOneClickShipment);
                }
            }
        }
    }

    private void validateRenderCheckoutPage(CartShipmentAddressFormData cartShipmentAddressFormData,
                                            @Nullable UserAddress userAddress,
                                            boolean isReloadData,
                                            boolean isReloadAfterPriceChangeHigher,
                                            boolean isOneClickShipment) {
        if (cartShipmentAddressFormData.getErrorCode() == CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS) {
            getView().renderCheckoutPageNoAddress(cartShipmentAddressFormData);
        } else if (cartShipmentAddressFormData.getErrorCode() == CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST) {
            getView().renderCheckoutPageNoMatchedAddress(cartShipmentAddressFormData, userAddress != null ? userAddress.getState() : 0);
        } else if (cartShipmentAddressFormData.getErrorCode() == CartShipmentAddressFormData.NO_ERROR) {
            if (userAddress == null) {
                getView().onShipmentAddressFormEmpty();
            } else {
                getView().updateLocalCacheAddressData(userAddress);
                initializePresenterData(cartShipmentAddressFormData);
                getView().renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHigher, isOneClickShipment);
                if (cartShipmentAddressFormData.getPopUpMessage().length() > 0) {
                    getView().showToastNormal(cartShipmentAddressFormData.getPopUpMessage());
                }
            }

        }
    }

    public void initializePresenterData(CartShipmentAddressFormData cartShipmentAddressFormData) {
        setLatValidateUseRequest(null);
        setValidateUsePromoRevampUiModel(null);

        shipmentTickerErrorModel = new ShipmentTickerErrorModel(cartShipmentAddressFormData.getErrorTicker());

        if (cartShipmentAddressFormData.getTickerData() != null) {
            setTickerAnnouncementHolderData(
                    new TickerAnnouncementHolderData(cartShipmentAddressFormData.getTickerData().getId(),
                            cartShipmentAddressFormData.getTickerData().getMessage())
            );
            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerAnnouncementHolderData.getId());
        } else {
            setTickerAnnouncementHolderData(new TickerAnnouncementHolderData());
        }

        RecipientAddressModel newAddress = shipmentDataConverter
                .getRecipientAddressModel(cartShipmentAddressFormData);
        setRecipientAddressModel(newAddress);

        if (cartShipmentAddressFormData.getDonation() != null) {
            ShipmentDonationModel shipmentDonationModel = shipmentDataConverter.getShipmentDonationModel(cartShipmentAddressFormData);
            shipmentDonationModel.setEnabled(!shipmentTickerErrorModel.isError());
            setShipmentDonationModel(shipmentDonationModel);
        } else {
            setShipmentDonationModel(null);
        }

        if (!cartShipmentAddressFormData.getCrossSell().isEmpty()) {
            ArrayList<ShipmentCrossSellModel> listShipmentCrossSellModel = shipmentDataConverter.getListShipmentCrossSellModel(cartShipmentAddressFormData);
            setListShipmentCrossSellModel(listShipmentCrossSellModel);
        } else {
            setListShipmentCrossSellModel(null);
        }

        setLastApplyData(cartShipmentAddressFormData.getLastApplyData());

        setShipmentCartItemModelList(shipmentDataConverter.getShipmentItems(
                cartShipmentAddressFormData, newAddress != null && newAddress.getLocationDataModel() != null)
        );

        this.codData = cartShipmentAddressFormData.getCod();

        this.campaignTimer = cartShipmentAddressFormData.getCampaignTimerUi();

        List<String> ppImpressionData = cartShipmentAddressFormData.getGetAvailablePurchaseProtection();
        if (!ppImpressionData.isEmpty()) {
            isPurchaseProtectionPage = true;
            mTrackerPurchaseProtection.eventImpressionOfProduct(userSessionInterface.getUserId(), ppImpressionData);
        }

        EgoldAttributeModel egoldAttributes = cartShipmentAddressFormData.getEgoldAttributes();
        if (egoldAttributes != null) {
            egoldAttributes.setEnabled(!shipmentTickerErrorModel.isError());
        }
        setEgoldAttributeModel(egoldAttributes);

        isShowOnboarding = cartShipmentAddressFormData.isShowOnboarding();
        isIneligiblePromoDialogEnabled = cartShipmentAddressFormData.isIneligiblePromoDialogEnabled();
    }

    public void setPurchaseProtection(boolean isPurchaseProtectionPage) {
        this.isPurchaseProtectionPage = isPurchaseProtectionPage;
    }

    @Override
    public void processCheckout(boolean isOneClickShipment,
                                boolean isTradeIn,
                                boolean isTradeInDropOff,
                                String deviceId,
                                String cornerId,
                                String leasingId) {
        if (getView().isBundleToggleChanged()) {
            getView().recreateActivity();
            return;
        }

        removeErrorShopProduct();
        CheckoutRequest checkoutRequest = generateCheckoutRequest(null,
                shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0,
                listShipmentCrossSellModel, leasingId
        );

        if (checkoutRequest != null && checkoutRequest.getData() != null && checkoutRequest.getData().size() > 0) {
            // Get additional param for trade in analytics
            String deviceModel = "";
            long devicePrice = 0L;
            String diagnosticId = "";
            if (shipmentCartItemModelList != null && shipmentCartItemModelList.size() > 0) {
                List<CartItemModel> cartItemModels = shipmentCartItemModelList.get(0).getCartItemModels();
                if (cartItemModels != null && cartItemModels.size() > 0) {
                    CartItemModel cartItemModel = cartItemModels.get(0);
                    if (cartItemModel != null) {
                        deviceModel = cartItemModel.getDeviceModel();
                        devicePrice = cartItemModel.getOldDevicePrice();
                        diagnosticId = cartItemModel.getDiagnosticId();
                    }
                }
            }

            Map<String, Object> params = generateCheckoutParams(isOneClickShipment, isTradeIn, isTradeInDropOff, deviceId, checkoutRequest);
            RequestParams requestParams = RequestParams.create();
            requestParams.putAll(params);
            compositeSubscription.add(
                    checkoutGqlUseCase.createObservable(requestParams)
                            .subscribe(getSubscriberCheckoutCart(
                                    checkoutRequest, isOneClickShipment, isTradeIn, deviceId,
                                    cornerId, leasingId, deviceModel, devicePrice, diagnosticId)
                            )
            );
        } else {
            getView().hideLoading();
            getView().setHasRunningApiCall(false);
            getView().showToastError(getView().getActivityContext().getString(R.string.message_error_checkout_empty));
        }
    }

    @NotNull
    public Map<String, Object> generateCheckoutParams(boolean isOneClickShipment,
                                                      boolean isTradeIn,
                                                      boolean isTradeInDropOff,
                                                      String deviceId,
                                                      CheckoutRequest checkoutRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put(CheckoutGqlUseCase.PARAM_CARTS, CheckoutRequestMapper.INSTANCE.map(checkoutRequest));
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

        if (CheckoutFingerprintUtil.INSTANCE.getEnableFingerprintPayment(getView().getActivityContext())) {
            PublicKey publicKey = CheckoutFingerprintUtil.INSTANCE.getFingerprintPublicKey(getView().getActivityContext());
            if (publicKey != null) {
                params.put(CheckoutGqlUseCase.PARAM_FINGERPRINT_PUBLICKEY, FingerPrintUtil.INSTANCE.getPublicKey(publicKey));
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
        }
    }

    // This is for akamai error case
    private void clearAllPromo() {
        ValidateUsePromoRequest validateUsePromoRequest = lastValidateUsePromoRequest;
        if (validateUsePromoRequest == null) return;
        ArrayList<String> codes = new ArrayList<>();
        for (String code : validateUsePromoRequest.getCodes()) {
            if (code != null) {
                codes.add(code);
            }
        }
        validateUsePromoRequest.setCodes(new ArrayList<>());
        ArrayList<OrdersItem> cloneOrders = new ArrayList<>();
        for (OrdersItem order : validateUsePromoRequest.getOrders()) {
            if (order != null) {
                codes.addAll(order.getCodes());
                order.setCodes(new ArrayList<>());
                cloneOrders.add(order);
            }
        }
        validateUsePromoRequest.setOrders(cloneOrders);
        if (!codes.isEmpty()) {
            clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, codes);
            compositeSubscription.add(
                    clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe()
            );
        }
        ShipmentPresenter.this.lastValidateUsePromoRequest = validateUsePromoRequest;
        ShipmentPresenter.this.validateUsePromoRevampUiModel = null;
    }

    @Override
    public void checkPromoCheckoutFinalShipment(ValidateUsePromoRequest validateUsePromoRequest,
                                                int lastSelectedCourierOrderIndex,
                                                String cartString) {
        setCouponStateChanged(true);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);

        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(executorSchedulers.getIo())
                        .observeOn(executorSchedulers.getMain())
                        .subscribe(new Subscriber<ValidateUsePromoRevampUiModel>() {
                                       @Override
                                       public void onCompleted() {

                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           Timber.d(e);
                                           if (getView() != null) {
                                               if (e instanceof AkamaiErrorException) {
                                                   clearAllPromo();
                                                   getView().showToastError(e.getMessage());
                                                   getView().resetAllCourier();
                                                   getView().cancelAllCourierPromo();
                                                   getView().doResetButtonPromoCheckout();
                                               } else {
                                                   getView().renderErrorCheckPromoShipmentData(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                               }
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
                                                   getView().updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel(), false);
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

                                               reloadCourierForMvc(validateUsePromoRevampUiModel, lastSelectedCourierOrderIndex, cartString);
                                           }
                                       }
                                   }
                        )
        );

    }

    // Re-fetch rates to get promo mvc icon
    private void reloadCourierForMvc(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel, int lastSelectedCourierOrderIndex, String cartString) {
        List<PromoSpIdUiModel> promoSpids = validateUsePromoRevampUiModel.getPromoUiModel().getAdditionalInfoUiModel().getPromoSpIds();
        if (promoSpids.size() > 0 && lastSelectedCourierOrderIndex > -1) {
            for (PromoSpIdUiModel promoSpId : promoSpids) {
                if (promoSpId.getUniqueId().equalsIgnoreCase(cartString)) {
                    getView().prepareReloadRates(lastSelectedCourierOrderIndex, false);
                    break;
                }
            }
        }
    }

    private void updateTickerAnnouncementData(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
        if (!UtilsKt.isNullOrEmpty(validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getMessage())) {
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
                                                               boolean isTradeIn,
                                                               String deviceId,
                                                               String cornerId,
                                                               String leasingId,
                                                               String deviceModel,
                                                               long devicePrice,
                                                               String diagnosticId) {
        return new Subscriber<CheckoutData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                Timber.d(e);
                String errorMessage = e.getMessage();
                if (!(e instanceof CartResponseErrorException || e instanceof AkamaiErrorException)) {
                    errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(getView().getActivityContext(), e);
                }
                analyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(errorMessage);
                getView().setHasRunningApiCall(false);
                getView().showToastError(errorMessage);
                processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, cornerId, deviceId, leasingId);
                getView().logOnErrorCheckout(e, checkoutRequest.toString());
            }

            @Override
            public void onNext(CheckoutData checkoutData) {
                getView().setHasRunningApiCall(false);
                ShipmentPresenter.this.checkoutData = checkoutData;
                if (!checkoutData.isError()) {
                    getView().triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(checkoutData.getTransactionId(), deviceModel, devicePrice, diagnosticId);
                    if (isPurchaseProtectionPage) {
                        mTrackerPurchaseProtection.eventClickOnBuy(userSessionInterface.getUserId(), checkoutRequest.getProtectionAnalyticsData());
                    }
                    boolean isCrossSellChecked = false;
                    for(ShipmentCrossSellModel shipmentCrossSellModel : listShipmentCrossSellModel) {
                        if (shipmentCrossSellModel.isChecked()) isCrossSellChecked = true;
                    }
                    if (isCrossSellChecked) triggerCrossSellClickPilihPembayaran();
                    getView().renderCheckoutCartSuccess(checkoutData);
                } else if (checkoutData.getErrorReporter().getEligible()) {
                    getView().hideLoading();
                    getView().renderCheckoutCartErrorReporter(checkoutData);
                } else if (checkoutData.getPriceValidationData().isUpdated()) {
                    getView().hideLoading();
                    getView().renderCheckoutPriceUpdated(checkoutData.getPriceValidationData());
                } else {
                    analyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(checkoutData.getErrorMessage());
                    getView().hideLoading();
                    if (!checkoutData.getErrorMessage().isEmpty()) {
                        getView().renderCheckoutCartError(checkoutData.getErrorMessage());
                        getView().logOnErrorCheckout(new MessageErrorException(checkoutData.getErrorMessage()), checkoutRequest.toString());
                    } else {
                        String defaultErrorMessage = getView().getActivityContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown);
                        getView().renderCheckoutCartError(defaultErrorMessage);
                        getView().logOnErrorCheckout(new MessageErrorException(defaultErrorMessage), checkoutRequest.toString());
                    }
                    processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, cornerId, deviceId, leasingId);
                }
            }
        };
    }

    private void triggerCrossSellClickPilihPembayaran() {
        List<ShipmentCrossSellModel> shipmentCrossSellModelList = getListShipmentCrossSellModel();
        for (int i=0; i<shipmentCrossSellModelList.size(); i++) {
            CrossSellModel crossSellModel = shipmentCrossSellModelList.get(i).getCrossSellModel();
            String digitalCategoryName = crossSellModel.getOrderSummary().getTitle();
            String digitalProductId = crossSellModel.getId();
            String eventLabel = digitalCategoryName + " - " + digitalProductId;
            String digitalProductName = crossSellModel.getInfo().getTitle();

            List<Object> productList = new ArrayList<>();
            for (int j = 0; j < shipmentCartItemModelList.size(); j++) {
                for (CartItemModel cartItemModel : shipmentCartItemModelList.get(i).getCartItemModels()) {
                    AnalyticsProductCheckoutData dataAnalytics = cartItemModel.getAnalyticsProductCheckoutData();
                    productList.add(DataLayer.mapOf(
                            ConstantTransactionAnalytics.Key.BRAND, "",
                            ConstantTransactionAnalytics.Key.CATEGORY, dataAnalytics.getProductCategoryId(),
                            ConstantTransactionAnalytics.Key.ID, "",
                            ConstantTransactionAnalytics.Key.NAME, dataAnalytics.getProductName(),
                            ConstantTransactionAnalytics.Key.PRICE, dataAnalytics.getProductPrice(),
                            ConstantTransactionAnalytics.Key.QUANTITY, dataAnalytics.getProductQuantity(),
                            ConstantTransactionAnalytics.Key.SHOP_ID, dataAnalytics.getProductShopId(),
                            ConstantTransactionAnalytics.Key.SHOP_NAME, dataAnalytics.getProductShopName(),
                            ConstantTransactionAnalytics.Key.SHOP_TYPE, dataAnalytics.getProductShopType(),
                            ConstantTransactionAnalytics.Key.VARIANT, digitalProductName
                    ));
                }
            }

            /*for (DataCheckoutRequest dataCheckoutRequest : checkoutRequest.getData()) {
                if (dataCheckoutRequest != null) {
                    for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.getShopProducts()) {
                        if (shopProductCheckoutRequest != null) {
                            for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.getProductData()) {
                                long productCategoryId = 0L;
                                for (int j = 0; j < shipmentCartItemModelList.size(); j++) {
                                    for (CartItemModel cartItemModel : shipmentCartItemModelList.get(i).getCartItemModels()) {
                                        if (cartItemModel.getProductId() == productDataCheckoutRequest.getProductId()) {
                                            productCategoryId = cartItemModel.getProductCatId();
                                            break;
                                        }
                                    }
                                }

                                if (productDataCheckoutRequest != null) {
                                    productList.add(DataLayer.mapOf(
                                            ConstantTransactionAnalytics.Key.BRAND, "",
                                            ConstantTransactionAnalytics.Key.CATEGORY, productCategoryId,
                                            ConstantTransactionAnalytics.Key.ID, "",
                                            ConstantTransactionAnalytics.Key.NAME, productDataCheckoutRequest.getProductName(),
                                            ConstantTransactionAnalytics.Key.PRICE, productDataCheckoutRequest.getProductPrice(),
                                            ConstantTransactionAnalytics.Key.QUANTITY, productDataCheckoutRequest.getProductQuantity(),
                                            ConstantTransactionAnalytics.Key.SHOP_ID, productDataCheckoutRequest.getProductShopId(),
                                            ConstantTransactionAnalytics.Key.SHOP_NAME, productDataCheckoutRequest.getProductShopName(),
                                            ConstantTransactionAnalytics.Key.SHOP_TYPE, productDataCheckoutRequest.getProductShopType(),
                                            ConstantTransactionAnalytics.Key.VARIANT, digitalProductName
                                    ));
                                }
                            }
                        }
                    }
                }
            }*/
            analyticsActionListener.sendEnhancedEcommerceAnalyticsCrossSellClickPilihPembayaran(eventLabel, userSessionInterface.getUserId(), productList);
        }
    }

    public Map<String, Object> generateCheckoutAnalyticsDataLayer(CheckoutRequest checkoutRequest, String step, String pageSource) {
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
            for (DataCheckoutRequest dataCheckoutRequest : checkoutRequest.getData()) {
                if (dataCheckoutRequest != null) {
                    for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.getShopProducts()) {
                        if (shopProductCheckoutRequest != null) {
                            for (ProductDataCheckoutRequest productDataCheckoutRequest : shopProductCheckoutRequest.getProductData()) {
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
                                    enhancedECommerceProductCartMapData.setShopType(productDataCheckoutRequest.getProductShopType());
                                    enhancedECommerceProductCartMapData.setCategoryId(productDataCheckoutRequest.getProductCategoryId() != null ? productDataCheckoutRequest.getProductCategoryId() : "");
                                    enhancedECommerceProductCartMapData.setDimension38(productDataCheckoutRequest.getProductAttribution() != null ? productDataCheckoutRequest.getProductAttribution() : "");
                                    enhancedECommerceProductCartMapData.setDimension40(productDataCheckoutRequest.getProductListName() != null ? productDataCheckoutRequest.getProductListName() : "");
                                    enhancedECommerceProductCartMapData.setDimension45(String.valueOf(productDataCheckoutRequest.getCartId()));
                                    enhancedECommerceProductCartMapData.setDimension53(productDataCheckoutRequest.isDiscountedPrice());
                                    enhancedECommerceProductCartMapData.setDimension54(getFulfillmentStatus(shopProductCheckoutRequest.getShopId()));
                                    enhancedECommerceProductCartMapData.setDimension12(shopProductCheckoutRequest.getShippingInfo().getAnalyticsDataShippingCourierPrice() != null ? shopProductCheckoutRequest.getShippingInfo().getAnalyticsDataShippingCourierPrice() : "");
                                    enhancedECommerceProductCartMapData.setWarehouseId(productDataCheckoutRequest.getWarehouseId() != null ? productDataCheckoutRequest.getWarehouseId() : "");
                                    enhancedECommerceProductCartMapData.setProductWeight(productDataCheckoutRequest.getProductWeight() != null ? productDataCheckoutRequest.getProductWeight() : "");
                                    enhancedECommerceProductCartMapData.setPromoCode(productDataCheckoutRequest.getPromoCode() != null ? productDataCheckoutRequest.getPromoCode() : "");
                                    enhancedECommerceProductCartMapData.setPromoDetails(productDataCheckoutRequest.getPromoDetails() != null ? productDataCheckoutRequest.getPromoDetails() : "");
                                    enhancedECommerceProductCartMapData.setCartId(String.valueOf(productDataCheckoutRequest.getCartId()));
                                    enhancedECommerceProductCartMapData.setBuyerAddressId(productDataCheckoutRequest.getBuyerAddressId() != null ? productDataCheckoutRequest.getBuyerAddressId() : "");
                                    enhancedECommerceProductCartMapData.setShippingDuration(productDataCheckoutRequest.getShippingDuration() != null ? productDataCheckoutRequest.getShippingDuration() : "");
                                    enhancedECommerceProductCartMapData.setCourier(productDataCheckoutRequest.getCourier() != null ? productDataCheckoutRequest.getCourier() : "");
                                    enhancedECommerceProductCartMapData.setShippingPrice(productDataCheckoutRequest.getShippingPrice() != null ? productDataCheckoutRequest.getShippingPrice() : "");
                                    enhancedECommerceProductCartMapData.setCodFlag(productDataCheckoutRequest.getCodFlag() != null ? productDataCheckoutRequest.getCodFlag() : "");
                                    enhancedECommerceProductCartMapData.setTokopediaCornerFlag(productDataCheckoutRequest.getTokopediaCornerFlag() != null ? productDataCheckoutRequest.getTokopediaCornerFlag() : "");
                                    enhancedECommerceProductCartMapData.setIsFulfillment(productDataCheckoutRequest.isFulfillment() != null ? productDataCheckoutRequest.isFulfillment() : "");
                                    if (productDataCheckoutRequest.isFreeShippingExtra()) {
                                        enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA);
                                    } else if (productDataCheckoutRequest.isFreeShipping()) {
                                        enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR);
                                    } else {
                                        enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
                                    }
                                    enhancedECommerceProductCartMapData.setCampaignId(String.valueOf(productDataCheckoutRequest.getCampaignId()));
                                    enhancedECommerceProductCartMapData.setPageSource(pageSource);
                                    enhancedECommerceProductCartMapData.setDimension117(productDataCheckoutRequest.getBundleType());
                                    enhancedECommerceProductCartMapData.setDimension118(productDataCheckoutRequest.getBundleId());

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

    private boolean getFulfillmentStatus(long shopId) {
        for (ShipmentCartItemModel cartItemModel : shipmentCartItemModelList) {
            if (cartItemModel.getShopId() == shopId) {
                return cartItemModel.isFulfillment();
            }
        }
        return false;
    }

    @Override
    public void doValidateUseLogisticPromo(int cartPosition, String cartString, ValidateUsePromoRequest validateUsePromoRequest) {
        setCouponStateChanged(true);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);
        getView().setStateLoadingCourierStateAtIndex(cartPosition, true);

        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(executorSchedulers.getIo())
                        .observeOn(executorSchedulers.getMain())
                        .subscribe(new Subscriber<ValidateUsePromoRevampUiModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().setStateLoadingCourierStateAtIndex(cartPosition, false);
                                Timber.d(e);
                                if (getView() != null) {
                                    mTrackerShipment.eventClickLanjutkanTerapkanPromoError(e.getMessage());
                                    if (e instanceof AkamaiErrorException) {
                                        clearAllPromo();
                                        getView().showToastError(e.getMessage());
                                        getView().resetAllCourier();
                                        getView().cancelAllCourierPromo();
                                        getView().doResetButtonPromoCheckout();
                                    } else {
                                        getView().showToastError(e.getMessage());
                                        getView().resetCourier(cartPosition);
                                    }
                                }
                            }

                            @Override
                            public void onNext(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
                                getView().setStateLoadingCourierStateAtIndex(cartPosition, false);
                                ShipmentPresenter.this.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel;
                                if (getView() != null) {
                                    updateTickerAnnouncementData(validateUsePromoRevampUiModel);
                                    showErrorValidateUseIfAny(validateUsePromoRevampUiModel);
                                    validateBBO(validateUsePromoRevampUiModel);
                                    if (validateUsePromoRevampUiModel.getStatus().equalsIgnoreCase(statusOK)) {
                                        getView().updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel(), true);
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
        ValidateUsePromoRequest validateUsePromoRequest = getView().generateValidateUsePromoRequest();
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);

        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(executorSchedulers.getIo())
                        .observeOn(executorSchedulers.getMain())
                        .subscribe(new Subscriber<ValidateUsePromoRevampUiModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.d(e);
                                if (getView() != null) {
                                    if (e instanceof AkamaiErrorException) {
                                        clearAllPromo();
                                        getView().showToastError(e.getMessage());
                                        getView().resetAllCourier();
                                        getView().cancelAllCourierPromo();
                                        getView().doResetButtonPromoCheckout();
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
                                                   int isDonation,
                                                   ArrayList<ShipmentCrossSellModel> listShipmentCrossSellModel,
                                                   String leasingId) {
        if (analyticsDataCheckoutRequests == null && dataCheckoutRequestList == null) {
            getView().showToastError(getView().getActivityContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown_short));
            return null;
        }

        // Set promo merchant request data
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
                    true,
                    getRecipientAddressModel().getUserCornerId(),
                    Integer.parseInt(getRecipientAddressModel().getCornerId())
            );
        }

        EgoldData egoldData = new EgoldData();

        if (egoldAttributeModel != null && egoldAttributeModel.isEligible()) {
            egoldData.setEgold(egoldAttributeModel.isChecked());
            egoldData.setEgoldAmount(egoldAttributeModel.getBuyEgoldValue());
        }

        CrossSellRequest crossSellRequest = new CrossSellRequest();
        ArrayList<CrossSellItemRequestModel> listCrossSellItemRequest = new ArrayList<>();

        if (!listShipmentCrossSellModel.isEmpty()) {
            CrossSellItemRequestModel crossSellItemRequestModel = new CrossSellItemRequestModel();
            for (ShipmentCrossSellModel shipmentCrossSellModel : listShipmentCrossSellModel) {
                if (shipmentCrossSellModel.isChecked()) {
                    crossSellItemRequestModel.setId(Utils.toIntOrZero(shipmentCrossSellModel.getCrossSellModel().getId()));
                    crossSellItemRequestModel.setPrice((int) shipmentCrossSellModel.getCrossSellModel().getPrice());
                    crossSellItemRequestModel.setAdditionalVerticalId(Utils.toIntOrZero(shipmentCrossSellModel.getCrossSellModel().getAdditionalVerticalId()));
                    crossSellItemRequestModel.setTransactionType(shipmentCrossSellModel.getCrossSellModel().getTransactionType());
                    listCrossSellItemRequest.add(crossSellItemRequestModel);
                }
            }
            crossSellRequest.setListItem(listCrossSellItemRequest);
        }
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setDonation(isDonation);
        checkoutRequest.setCrossSell(crossSellRequest);
        checkoutRequest.setData(analyticsDataCheckoutRequests != null ? analyticsDataCheckoutRequests : dataCheckoutRequestList);
        checkoutRequest.setEgoldData(egoldData);

        setCheckoutFeatureTypeData(checkoutRequest);

        if (cornerData != null) {
            checkoutRequest.setCornerData(cornerData);
        }

        // Set promo global request data
        if (validateUsePromoRevampUiModel != null) {
            // Clear data first
            checkoutRequest.setPromos(null);
            checkoutRequest.setPromoCodes(null);

            // Then set the data promo global
            PromoUiModel promoUiModel = validateUsePromoRevampUiModel.getPromoUiModel();
            if (promoUiModel.getCodes().size() > 0 && !promoUiModel.getMessageUiModel().getState().equals("red")) {
                ArrayList<String> codes = new ArrayList<>(promoUiModel.getCodes());
                checkoutRequest.setPromoCodes(codes);

                List<PromoRequest> promoRequests = new ArrayList<>();
                for (String promoCode : promoUiModel.getCodes()) {
                    PromoRequest promoRequest = new PromoRequest();
                    promoRequest.setCode(promoCode);
                    promoRequest.setType(PromoRequest.TYPE_GLOBAL);

                    promoRequests.add(promoRequest);
                }
                checkoutRequest.setPromos(promoRequests);

            }

            checkoutRequest.setHasPromoStacking(true);
        }

        if (leasingId != null && !leasingId.isEmpty()) {
            checkoutRequest.setLeasingId(Integer.parseInt(leasingId));
        }

        return checkoutRequest;
    }

    private void setCheckoutFeatureTypeData(CheckoutRequest checkoutRequest) {
        boolean hasTokoNowProduct = false;
        List<DataCheckoutRequest> dataCheckoutRequests = checkoutRequest.getData();
        if (dataCheckoutRequests != null) {
            for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequests) {
                List<ShopProductCheckoutRequest> shopProductCheckoutRequests = dataCheckoutRequest.getShopProducts();
                if (!hasTokoNowProduct && shopProductCheckoutRequests != null) {
                    for (ShopProductCheckoutRequest shopProductCheckoutRequest : shopProductCheckoutRequests) {
                        if (shopProductCheckoutRequest.isTokoNow()) {
                            hasTokoNowProduct = true;
                            break;
                        }
                    }
                }
            }

            checkoutRequest.setFeatureType(hasTokoNowProduct ? FEATURE_TYPE_TOKONOW_PRODUCT : FEATURE_TYPE_REGULAR_PRODUCT);
        }
    }

    private void setCheckoutRequestPromoData(List<DataCheckoutRequest> dataCheckoutRequestList) {
        // Clear data first
        for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequestList) {
            if (dataCheckoutRequest.getShopProducts() != null && dataCheckoutRequest.getShopProducts().size() > 0) {
                for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.getShopProducts()) {
                    if (shopProductCheckoutRequest.getPromoCodes() != null) {
                        shopProductCheckoutRequest.getPromoCodes().clear();
                    }
                    if (shopProductCheckoutRequest.getPromos() != null) {
                        shopProductCheckoutRequest.getPromos().clear();
                    }
                }
            }
        }

        // Then set the data promo merchant & logistic
        for (DataCheckoutRequest dataCheckoutRequest : dataCheckoutRequestList) {
            if (dataCheckoutRequest.getShopProducts() != null && dataCheckoutRequest.getShopProducts().size() > 0) {
                for (ShopProductCheckoutRequest shopProductCheckoutRequest : dataCheckoutRequest.getShopProducts()) {
                    for (PromoCheckoutVoucherOrdersItemUiModel voucherOrder : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
                        if (shopProductCheckoutRequest.getCartString().equals(voucherOrder.getUniqueId())) {
                            if (shopProductCheckoutRequest.getPromoCodes() != null &&
                                    shopProductCheckoutRequest.getPromoCodes().size() > 0 &&
                                    !shopProductCheckoutRequest.getPromoCodes().contains(voucherOrder.getCode())) {
                                // This section logic's seems to be invalid, since promo will always be cleared on previous logic
                                shopProductCheckoutRequest.getPromoCodes().add(voucherOrder.getCode());
                            } else {
                                ArrayList<String> codes = new ArrayList<>();
                                codes.add(voucherOrder.getCode());
                                shopProductCheckoutRequest.setPromoCodes(codes);
                            }

                            if (voucherOrder.getCode().length() > 0 && voucherOrder.getType().length() > 0) {
                                if (shopProductCheckoutRequest.getPromos() != null && shopProductCheckoutRequest.getPromos().size() > 0 &&
                                        !hasInsertPromo(shopProductCheckoutRequest.getPromos(), voucherOrder.getCode())) {
                                    // This section logic's seems to be invalid, since promo will always be cleared on previous logic
                                    PromoRequest promoRequest = new PromoRequest();
                                    promoRequest.setCode(voucherOrder.getCode());
                                    promoRequest.setType(voucherOrder.getType());

                                    shopProductCheckoutRequest.getPromos().add(promoRequest);
                                } else {
                                    PromoRequest promoRequest = new PromoRequest();
                                    promoRequest.setCode(voucherOrder.getCode());
                                    promoRequest.setType(voucherOrder.getType());

                                    List<PromoRequest> promoRequests = new ArrayList<>();
                                    promoRequests.add(promoRequest);

                                    shopProductCheckoutRequest.setPromos(promoRequests);
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

        ShipmentStateRequestData shipmentStateRequestData = new ShipmentStateRequestData();
        shipmentStateRequestData.setAddressId(Integer.parseInt(recipientAddressModel.getId()));
        shipmentStateRequestData.setShopProductDataList(shipmentStateShopProductDataList);
        shipmentStateRequestDataList.add(shipmentStateRequestData);

        SaveShipmentStateRequest saveShipmentStateRequest = new SaveShipmentStateRequest();
        saveShipmentStateRequest.setRequestDataList(shipmentStateRequestDataList);

        return saveShipmentStateRequest;
    }

    private void setSaveShipmentStateData(ShipmentCartItemModel shipmentCartItemModel,
                                          List<ShipmentStateShopProductData> shipmentStateShopProductDataList) {
        if (shipmentCartItemModel == null) return;
        CourierItemData courierData = null;
        if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
            if (getView().isTradeInByDropOff()) {
                courierData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff();
            } else {
                courierData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
            }
        }

        if (courierData != null) {
            List<ShipmentStateProductData> shipmentStateProductDataList = new ArrayList<>();
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                ShipmentStateProductData shipmentStateProductData = new ShipmentStateProductData();
                shipmentStateProductData.setProductId(cartItemModel.getProductId());
                if (cartItemModel.isPreOrder()) {
                    ShipmentStateProductPreorder shipmentStateProductPreorder = new ShipmentStateProductPreorder();
                    shipmentStateProductPreorder.setDurationDay(cartItemModel.getPreOrderDurationDay());
                    shipmentStateProductData.setProductPreorder(shipmentStateProductPreorder);
                }
                shipmentStateProductDataList.add(shipmentStateProductData);
            }

            ShipmentStateDropshipData shipmentStateDropshipData = new ShipmentStateDropshipData();
            shipmentStateDropshipData.setName(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperName());
            shipmentStateDropshipData.setTelpNo(shipmentCartItemModel.getSelectedShipmentDetailData().getDropshipperPhone());

            RatesFeature ratesFeature = ShipmentDataRequestConverter.generateRatesFeature(courierData);

            ShipmentStateShippingInfoData shipmentStateShippingInfoData = new ShipmentStateShippingInfoData();
            shipmentStateShippingInfoData.setShippingId(courierData.getShipperId());
            shipmentStateShippingInfoData.setSpId(courierData.getShipperProductId());
            shipmentStateShippingInfoData.setRatesFeature(ratesFeature);

            ShipmentStateShopProductData shipmentStateShopProductData = new ShipmentStateShopProductData();
            shipmentStateShopProductData.setShopId(shipmentCartItemModel.getShopId());
            shipmentStateShopProductData.setFinsurance((shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance() != null &&
                    shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance()) ? 1 : 0);
            shipmentStateShopProductData.setDropship((shipmentCartItemModel.getSelectedShipmentDetailData().getUseDropshipper() != null &&
                    shipmentCartItemModel.getSelectedShipmentDetailData().getUseDropshipper()) ? 1 : 0);
            shipmentStateShopProductData.setOrderPriority((shipmentCartItemModel.getSelectedShipmentDetailData().isOrderPriority() != null &&
                    shipmentCartItemModel.getSelectedShipmentDetailData().isOrderPriority()) ? 1 : 0);
            shipmentStateShopProductData.setPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0);
            shipmentStateShopProductData.setWarehouseId(shipmentCartItemModel.getFulfillmentId());
            shipmentStateShopProductData.setDropshipData(shipmentStateDropshipData);
            shipmentStateShopProductData.setShippingInfoData(shipmentStateShippingInfoData);
            shipmentStateShopProductData.setProductDataList(shipmentStateProductDataList);
            shipmentStateShopProductDataList.add(shipmentStateShopProductData);
        }
    }

    @Override
    public void editAddressPinpoint(final String latitude, final String longitude,
                                    ShipmentCartItemModel shipmentCartItemModel,
                                    LocationPass locationPass) {
        if (getView() != null) {
            getView().showLoading();
            getView().setHasRunningApiCall(true);
            RequestParams requestParams = generateEditAddressRequestParams(shipmentCartItemModel, latitude, longitude);
            compositeSubscription.add(
                    editAddressUseCase.createObservable(requestParams)
                            .subscribeOn(executorSchedulers.getIo())
                            .observeOn(executorSchedulers.getMain())
                            .unsubscribeOn(executorSchedulers.getIo())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Timber.d(e);
                                    if (getView() != null) {
                                        getView().setHasRunningApiCall(false);
                                        getView().hideLoading();
                                        getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                    }
                                }

                                @Override
                                public void onNext(String stringResponse) {
                                    if (getView() != null) {
                                        getView().setHasRunningApiCall(false);
                                        getView().hideLoading();
                                        JsonObject response = null;
                                        String messageError = null;
                                        boolean statusSuccess;
                                        try {
                                            response = new JsonParser().parse(stringResponse).getAsJsonObject();
                                            int statusCode = response.getAsJsonObject().getAsJsonObject(EditAddressUseCase.RESPONSE_DATA)
                                                    .get(EditAddressUseCase.RESPONSE_IS_SUCCESS).getAsInt();
                                            statusSuccess = statusCode == 1;
                                            if (!statusSuccess) {
                                                messageError = response.getAsJsonArray("message_error").get(0).getAsString();
                                            }
                                        } catch (Exception e) {
                                            Timber.d(e);
                                            statusSuccess = false;
                                        }

                                        if (response != null && statusSuccess) {
                                            if (recipientAddressModel != null) {
                                                recipientAddressModel.setLatitude(latitude);
                                                recipientAddressModel.setLongitude(longitude);
                                            }
                                            getView().renderEditAddressSuccess(latitude, longitude);
                                        } else {
                                            if (UtilsKt.isNullOrEmpty(messageError)) {
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
    private RequestParams generateEditAddressRequestParams(ShipmentCartItemModel shipmentCartItemModel,
                                                           String addressLatitude, String addressLongitude) {
        Map<String, String> params = AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(),
                userSessionInterface.getDeviceId(),
                new TKPDMapParam<>()
        );

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

        if (recipientAddressModel != null) {
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

        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodeList);
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
                            if (!UtilsKt.isNullOrEmpty(responseData.getSuccessDataModel().getTickerMessage())) {
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
    public void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList) {
        setCouponStateChanged(true);
        ArrayList<String> notEligiblePromoCodes = new ArrayList<>();
        for (NotEligiblePromoHolderdata notEligiblePromoHolderdata : notEligiblePromoHolderdataArrayList) {
            notEligiblePromoCodes.add(notEligiblePromoHolderdata.getPromoCode());
        }

        if (notEligiblePromoCodes.size() > 0) {
            clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, notEligiblePromoCodes);
            compositeSubscription.add(
                    clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                            .subscribe(new ClearNotEligiblePromoSubscriber(getView(), this, notEligiblePromoHolderdataArrayList))
            );
        }
    }

    // Clear promo after clash (rare, almost zero probability)
    @Override
    public void cancelAutoApplyPromoStackAfterClash(ArrayList<String> promoCodesToBeCleared) {
        setCouponStateChanged(true);
        getView().showLoading();
        getView().setHasRunningApiCall(true);
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodesToBeCleared);
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe(
                        new ClearShipmentCacheAutoApplyAfterClashSubscriber(getView(), this)
                )
        );
    }

    @Override
    public void changeShippingAddress(RecipientAddressModel newRecipientAddressModel,
                                      ChosenAddressModel chosenAddressModel,
                                      boolean isOneClickShipment,
                                      boolean isTradeInDropOff,
                                      boolean isHandleFallback,
                                      boolean reloadCheckoutPage) {
        getView().showLoading();
        getView().setHasRunningApiCall(true);
        List<DataChangeAddressRequest> dataChangeAddressRequests = new ArrayList<>();
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                    DataChangeAddressRequest dataChangeAddressRequest = new DataChangeAddressRequest();
                    dataChangeAddressRequest.setQuantity(cartItemModel.getQuantity());
                    dataChangeAddressRequest.setProductId(cartItemModel.getProductId());
                    dataChangeAddressRequest.setNotes(cartItemModel.getNoteToSeller());
                    dataChangeAddressRequest.setCartIdStr(String.valueOf(cartItemModel.getCartId()));
                    if (newRecipientAddressModel != null) {
                        if (isTradeInDropOff) {
                            dataChangeAddressRequest.setAddressId(newRecipientAddressModel.getLocationDataModel().getAddrId());
                            dataChangeAddressRequest.setIndomaret(true);
                        } else {
                            dataChangeAddressRequest.setAddressId(newRecipientAddressModel.getId());
                            dataChangeAddressRequest.setIndomaret(false);
                        }
                    }
                    if (chosenAddressModel != null) {
                        dataChangeAddressRequest.setAddressId(String.valueOf(chosenAddressModel.getAddressId()));
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
                        .subscribeOn(executorSchedulers.getIo())
                        .observeOn(executorSchedulers.getMain())
                        .unsubscribeOn(executorSchedulers.getIo())
                        .subscribe(new Subscriber<SetShippingAddressData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (getView() != null) {
                                    getView().hideLoading();
                                    getView().setHasRunningApiCall(false);
                                    Timber.d(e);
                                    String errorMessage;
                                    if (e instanceof AkamaiErrorException) {
                                        errorMessage = e.getMessage();
                                    } else {
                                        errorMessage = ErrorHandler.getErrorMessage(getView().getActivityContext(), e);
                                    }
                                    getView().showToastError(errorMessage);
                                    if (isHandleFallback) {
                                        getView().renderChangeAddressFailed(reloadCheckoutPage);
                                    }
                                }
                            }

                            @Override
                            public void onNext(SetShippingAddressData setShippingAddressData) {
                                if (getView() != null) {
                                    getView().hideLoading();
                                    getView().setHasRunningApiCall(false);
                                    if (setShippingAddressData.isSuccess()) {
                                        getView().showToastNormal(getView().getActivityContext().getString(R.string.label_change_address_success));
                                        getView().renderChangeAddressSuccess(reloadCheckoutPage);
                                    } else {
                                        if (setShippingAddressData.getMessages().size() > 0) {
                                            StringBuilder stringBuilder = new StringBuilder();
                                            for (String errorMessage : setShippingAddressData.getMessages()) {
                                                stringBuilder.append(errorMessage).append(" ");
                                            }
                                            getView().showToastError(stringBuilder.toString());
                                            if (isHandleFallback) {
                                                getView().renderChangeAddressFailed(reloadCheckoutPage);
                                            }
                                        } else {
                                            getView().showToastError(getView().getActivityContext().getString(R.string.label_change_address_failed));
                                            if (isHandleFallback) {
                                                getView().renderChangeAddressFailed(reloadCheckoutPage);
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
                                                RecipientAddressModel recipientAddressModel,
                                                boolean isForceReload, boolean skipMvc) {
        ShippingParam shippingParam = getShippingParam(shipmentDetailData, products, cartString, isTradeInDropOff, recipientAddressModel);

        int counter = codData == null ? -1 : codData.getCounterCod();
        boolean cornerId = false;
        if (getRecipientAddressModel() != null) {
            cornerId = getRecipientAddressModel().isCornerAddress();
        }
        String pslCode = RatesDataConverter.getLogisticPromoCode(shipmentCartItemModel);
        boolean isLeasing = shipmentCartItemModel.isLeasingProduct();

        String mvc = generateRatesMvcParam(cartString);

        RatesParam.Builder ratesParamBuilder = new RatesParam.Builder(shopShipmentList, shippingParam)
                .isCorner(cornerId)
                .codHistory(counter)
                .isLeasing(isLeasing)
                .promoCode(pslCode)
                .mvc("");

        if (!skipMvc) {
            ratesParamBuilder.mvc(mvc);
        }

        RatesParam param = ratesParamBuilder.build();

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
                                shippingCourierConverter, shipmentCartItemModel,
                                isInitialLoad, isTradeInDropOff, isForceReload
                        ));
    }

    @Override
    public String generateRatesMvcParam(String cartString) {
        String mvc = "";

        List<MvcShippingBenefitUiModel> tmpMvcShippingBenefitUiModel = new ArrayList<>();
        List<PromoSpIdUiModel> promoSpIdUiModels = new ArrayList<>();
        if (validateUsePromoRevampUiModel != null) {
            promoSpIdUiModels = validateUsePromoRevampUiModel.getPromoUiModel().getAdditionalInfoUiModel().getPromoSpIds();
        } else if (lastApplyData != null) {
            promoSpIdUiModels = lastApplyData.getAdditionalInfo().getPromoSpIds();
        }

        if (promoSpIdUiModels.size() > 0) {
            for (PromoSpIdUiModel promoSpIdUiModel : promoSpIdUiModels) {
                if (cartString.equals(promoSpIdUiModel.getUniqueId())) {
                    tmpMvcShippingBenefitUiModel.addAll(promoSpIdUiModel.getMvcShippingBenefits());
                }
            }
        }

        if (tmpMvcShippingBenefitUiModel.size() > 0) {
            mvc = gson.toJson(tmpMvcShippingBenefitUiModel);
        }

        return mvc.replace("\n", "").replace(" ", "");
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
        shippingParam.setWeightActualInKilograms(shipmentDetailData.getShipmentCartData().getWeightActual() / 1000);
        shippingParam.setShopId(shipmentDetailData.getShopId());
        shippingParam.setShopTier(shipmentDetailData.getShipmentCartData().getShopTier());
        shippingParam.setToken(shipmentDetailData.getShipmentCartData().getToken());
        shippingParam.setUt(shipmentDetailData.getShipmentCartData().getUt());
        shippingParam.setInsurance(shipmentDetailData.getShipmentCartData().getInsurance());
        shippingParam.setProductInsurance(shipmentDetailData.getShipmentCartData().getProductInsurance());
        shippingParam.setOrderValue(shipmentDetailData.getShipmentCartData().getOrderValue());
        shippingParam.setCategoryIds(shipmentDetailData.getShipmentCartData().getCategoryIds());
        shippingParam.setBlackbox(shipmentDetailData.isBlackbox());
        shippingParam.setPreorder(shipmentDetailData.getPreorder());
        shippingParam.setAddressId(recipientAddressModel.getId());
        shippingParam.setTradein(shipmentDetailData.isTradein());
        shippingParam.setProducts(products);
        shippingParam.setUniqueId(cartString);
        shippingParam.setTradeInDropOff(isTradeInDropOff);
        shippingParam.setPreOrderDuration(shipmentDetailData.getShipmentCartData().getPreOrderDuration());
        shippingParam.setFulfillment(shipmentDetailData.getShipmentCartData().isFulfillment());
        shippingParam.setBoMetadata(shipmentDetailData.getShipmentCartData().getBoMetadata());

        if (isTradeInDropOff && recipientAddressModel.getLocationDataModel() != null) {
            shippingParam.setDestinationDistrictId(recipientAddressModel.getLocationDataModel().getDistrict());
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
    public List<ShippingCourierUiModel> getShippingCourierViewModelsState(int orderNumber) {
        if (shippingCourierViewModelsState != null) {
            return shippingCourierViewModelsState.get(orderNumber);
        }
        return null;
    }

    @Override
    public void setShippingCourierViewModelsState
            (List<ShippingCourierUiModel> shippingCourierUiModelsState,
             int orderNumber) {
        if (this.shippingCourierViewModelsState == null) {
            this.shippingCourierViewModelsState = new HashMap<>();
        }
        this.shippingCourierViewModelsState.put(orderNumber, shippingCourierUiModelsState);
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
        if (campaignTimer == null || !campaignTimer.getShowTimer()) return null;
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

    @NotNull
    @Override
    public ShipmentDataConverter getShipmentDataConverter() {
        return shipmentDataConverter;
    }

    @Override
    public void releaseBooking() {
        // As deals product is using OCS, the shipment should only contain 1 product
        long productId = ShipmentCartItemModelHelper.getFirstProductId(shipmentCartItemModelList);
        if (productId != 0) {
            compositeSubscription.add(releaseBookingUseCase
                    .execute(productId)
                    .subscribe(new ReleaseBookingStockSubscriber()));
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
    public void setValidateUsePromoRevampUiModel(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
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
        getView().setHasRunningApiCall(true);
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
                        getView().setHasRunningApiCall(false);
                        getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                    }

                    @Override
                    public void onNext(SubmitTicketResult submitTicketResult) {
                        getView().hideLoading();
                        getView().setHasRunningApiCall(false);
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

    @Override
    public void setCheckoutData(CheckoutData checkoutData) {
        this.checkoutData = checkoutData;
    }
}
