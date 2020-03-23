package com.tokopedia.purchase_platform.features.checkout.view;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.Product;
import com.tokopedia.logisticcart.shipping.model.RatesParam;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingParam;
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase;
import com.tokopedia.logisticdata.data.analytics.CodAnalytics;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam;
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeFinalUseCase;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper;
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData;
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException;
import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseApiUrl;
import com.tokopedia.purchase_platform.common.data.model.param.EditAddressParam;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.CheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.EgoldData;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.ProductDataCheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.PromoRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.RatesFeature;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.ShopProductCheckoutRequest;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.TokopediaCornerData;
import com.tokopedia.purchase_platform.common.data.model.request.helpticket.SubmitHelpTicketRequest;
import com.tokopedia.purchase_platform.common.data.model.response.cod.Data;
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartGqlResponse;
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData;
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase;
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.ticker_announcement.TickerAnnouncementHolderData;
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult;
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase;
import com.tokopedia.purchase_platform.features.checkout.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.DataChangeAddressRequest;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate.SaveShipmentStateRequest;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate.ShipmentStateDropshipData;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate.ShipmentStateProductData;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate.ShipmentStateProductPreorder;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate.ShipmentStateShippingInfoData;
import com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate.ShipmentStateShopProductData;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.cod.CodResponse;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.CodCheckoutUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.GetShipmentAddressFormOneClickShipementUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.SaveShipmentStateUseCase;
import com.tokopedia.purchase_platform.features.checkout.view.converter.RatesDataConverter;
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataConverter;
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataRequestConverter;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.CheckShipmentPromoFirstStepAfterClashSubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.ClearNotEligiblePromoSubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.ClearShipmentCacheAutoApplyAfterClashSubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.ClearShipmentCacheAutoApplySubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.GetCourierRecommendationSubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.GetShipmentAddressFormReloadFromMultipleAddressSubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.GetShipmentAddressFormSubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.subscriber.SaveShipmentStateSubscriber;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.EgoldTieringModel;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest;
import com.tokopedia.purchase_platform.features.promo.domain.usecase.ValidateUsePromoRevampUseCase;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ClashingInfoDetailUiModel;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.PromoCheckoutVoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.PromoClashOptionUiModel;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.PromoClashVoucherOrdersUiModel;
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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
    private final CheckPromoStackingCodeFinalUseCase checkPromoStackingCodeFinalUseCase;
    private final CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase;
    private final CheckPromoStackingCodeMapper checkPromoStackingCodeMapper;
    private final CheckoutUseCase checkoutUseCase;
    private final CompositeSubscription compositeSubscription;
    private final GetShipmentAddressFormUseCase getShipmentAddressFormUseCase;
    private final GetShipmentAddressFormOneClickShipementUseCase getShipmentAddressFormOneClickShipementUseCase;
    private final EditAddressUseCase editAddressUseCase;
    private final ChangeShippingAddressUseCase changeShippingAddressUseCase;
    private final SaveShipmentStateUseCase saveShipmentStateUseCase;
    private final GetRatesUseCase ratesUseCase;
    private final GetRatesApiUseCase ratesApiUseCase;
    private final ShippingCourierConverter shippingCourierConverter;
    private final CodCheckoutUseCase codCheckoutUseCase;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;
    private final SubmitHelpTicketUseCase submitHelpTicketUseCase;
    private final UserSessionInterface userSessionInterface;
    private final GetInsuranceCartUseCase getInsuranceCartUseCase;
    private final ShipmentDataConverter shipmentDataConverter;
    private final ValidateUsePromoRevampUseCase validateUsePromoRevampUseCase;

    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private TickerAnnouncementHolderData tickerAnnouncementHolderData;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private EgoldAttributeModel egoldAttributeModel;
    private ShipmentDonationModel shipmentDonationModel;
    private ShipmentButtonPaymentModel shipmentButtonPaymentModel;
    private CodModel codData;
    private Token token;
    private ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel;

    private List<DataCheckoutRequest> dataCheckoutRequestList;
    private List<DataChangeAddressRequest> changeAddressRequestList;
    private CheckoutData checkoutData;
    private boolean partialCheckout;
    private boolean couponStateChanged;
    private boolean hasDeletePromoAfterChecKPromoCodeFinal;
    private Map<Integer, List<ShippingCourierUiModel>> shippingCourierViewModelsState;
    private boolean isPurchaseProtectionPage = false;
    private boolean isShowOnboarding;
    private boolean isIneligbilePromoDialogEnabled;

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
    public ShipmentPresenter(CheckPromoStackingCodeFinalUseCase checkPromoStackingCodeFinalUseCase,
                             CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase,
                             CheckPromoStackingCodeMapper checkPromoStackingCodeMapper,
                             CompositeSubscription compositeSubscription,
                             CheckoutUseCase checkoutUseCase,
                             GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                             GetShipmentAddressFormOneClickShipementUseCase getShipmentAddressFormOneClickShipementUseCase,
                             EditAddressUseCase editAddressUseCase,
                             ChangeShippingAddressUseCase changeShippingAddressUseCase,
                             SaveShipmentStateUseCase saveShipmentStateUseCase,
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
                             ValidateUsePromoRevampUseCase validateUsePromoRevampUseCase) {
        this.checkPromoStackingCodeFinalUseCase = checkPromoStackingCodeFinalUseCase;
        this.checkPromoStackingCodeUseCase = checkPromoStackingCodeUseCase;
        this.checkPromoStackingCodeMapper = checkPromoStackingCodeMapper;
        this.compositeSubscription = compositeSubscription;
        this.checkoutUseCase = checkoutUseCase;
        this.getShipmentAddressFormUseCase = getShipmentAddressFormUseCase;
        this.getShipmentAddressFormOneClickShipementUseCase = getShipmentAddressFormOneClickShipementUseCase;
        this.editAddressUseCase = editAddressUseCase;
        this.changeShippingAddressUseCase = changeShippingAddressUseCase;
        this.saveShipmentStateUseCase = saveShipmentStateUseCase;
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
        if (checkPromoStackingCodeFinalUseCase != null) {
            checkPromoStackingCodeFinalUseCase.unsubscribe();
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
    public void setDataChangeAddressRequestList(List<DataChangeAddressRequest> dataChangeAddressRequestList) {
        this.changeAddressRequestList = dataChangeAddressRequestList;
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
        CheckPromoParam checkPromoParam = new CheckPromoParam();
        checkPromoParam.setPromo(getView().generateCheckPromoFirstStepParam());
        CheckoutRequest checkoutRequest = generateCheckoutRequest(dataCheckoutRequests, hasInsurance, checkPromoParam,
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
    public List<DataCheckoutRequest> updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(PromoStackingData promoStackingGlobalData, List<ShipmentCartItemModel> shipmentCartItemModels) {
        List<DataCheckoutRequest> dataCheckoutRequests = dataCheckoutRequestList;
        if (dataCheckoutRequests == null) {
            dataCheckoutRequests = getView().generateNewCheckoutRequest(getShipmentCartItemModelList(), true);
        }

        if (dataCheckoutRequests != null) {
            StringBuilder promoCodes = new StringBuilder();
            StringBuilder promoDetails = new StringBuilder();

            if (promoStackingGlobalData != null && !TextUtils.isEmpty(promoStackingGlobalData.getPromoCode())) {
                promoCodes.append(promoStackingGlobalData.getPromoCode());
                promoDetails.append(TickerCheckoutUtilKt.revertMapToStatePromoStackingCheckout(promoStackingGlobalData.getState()));
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
    public boolean isIneligbilePromoDialogEnabled() {
        return isIneligbilePromoDialogEnabled;
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
    public void processInitialLoadCheckoutPage(boolean isReloadData, boolean isOneClickShipment,
                                               boolean isTradeIn, boolean isSkipUpdateOnboardingState,
                                               boolean isReloadAfterPriceChangeHinger,
                                               @Nullable String cornerId, String deviceId, String leasingId) {

        if (isReloadData) {
            getView().showLoading();
        } else {
            getView().showInitialLoading();
        }
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");
        if (cornerId != null) paramGetShipmentForm.put("corner_id", cornerId);
        if (leasingId != null && !leasingId.isEmpty())
            paramGetShipmentForm.put("vehicle_leasing_id", leasingId);

        RequestParams requestParams = RequestParams.create();
        Map<String, String> params = getGeneratedAuthParamNetwork(paramGetShipmentForm);
        params.put(GetShipmentAddressFormUseCase.PARAM_SKIP_ONBOARDING_UPDATE_STATE, String.valueOf(isSkipUpdateOnboardingState ? 1 : 0));

        if (isOneClickShipment) {
            if (isTradeIn) {
                params.put(GetShipmentAddressFormOneClickShipementUseCase.PARAM_IS_TRADEIN, String.valueOf(isTradeIn));
                params.put(GetShipmentAddressFormOneClickShipementUseCase.PARAM_DEVICE_ID, deviceId);
            }
            requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS, params);
            compositeSubscription.add(
                    getShipmentAddressFormOneClickShipementUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(new GetShipmentAddressFormSubscriber(this, getView(),
                                    isReloadData, isReloadAfterPriceChangeHinger, true))
            );
        } else {
            requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS, params);
            compositeSubscription.add(
                    getShipmentAddressFormUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(new GetShipmentAddressFormSubscriber(this, getView(),
                                    isReloadData, isReloadAfterPriceChangeHinger, false))
            );
        }
    }

    public void initializePresenterData(CartShipmentAddressFormData cartShipmentAddressFormData) {

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
        if (!cartShipmentAddressFormData.isMultiple()) {
            setRecipientAddressModel(newAddress);
        } else {
            setRecipientAddressModel(null);
        }

        if (cartShipmentAddressFormData.getDonation() != null) {
            setShipmentDonationModel(shipmentDataConverter.getShipmentDonationModel(cartShipmentAddressFormData));
        } else {
            setShipmentDonationModel(null);
        }

        // getView().setPromoStackingData(cartShipmentAddressFormData);
        //getView().setLastApplyData(cartShipmentAddressFormData.getLastApplyData());

        if (cartShipmentAddressFormData.getLastApplyData() != null) {
            setLastApplyData(cartShipmentAddressFormData.getLastApplyData());
        } else {
            setLastApplyData(null);
        }

        setShipmentCartItemModelList(shipmentDataConverter.getShipmentItems(
                cartShipmentAddressFormData, newAddress != null && newAddress.getLocationDataModel() != null)
        );

        this.codData = cartShipmentAddressFormData.getCod();
        if ((this.codData != null && this.codData.isCod()) || cartShipmentAddressFormData.isMultipleDisable()) {
            recipientAddressModel.setDisableMultipleAddress(true);
        }

        if (cartShipmentAddressFormData.isAvailablePurchaseProtection()) {
            isPurchaseProtectionPage = true;
            mTrackerPurchaseProtection.eventImpressionOfProduct();
        }

        setEgoldAttributeModel(cartShipmentAddressFormData.getEgoldAttributes());

        token = new Token();
        token.setUt(cartShipmentAddressFormData.getKeroUnixTime());
        token.setDistrictRecommendation(cartShipmentAddressFormData.getKeroDiscomToken());

        isShowOnboarding = cartShipmentAddressFormData.isShowOnboarding();
        isIneligbilePromoDialogEnabled = cartShipmentAddressFormData.isIneligbilePromoDialogEnabled();
    }

    @Override
    public void processReloadCheckoutPageFromMultipleAddress(RecipientAddressModel oldRecipientAddressModel,
                                                             ArrayList<ShipmentCartItemModel> oldShipmentCartItemModels) {
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
                        .subscribe(new GetShipmentAddressFormReloadFromMultipleAddressSubscriber(
                                this, getView(), oldRecipientAddressModel, oldShipmentCartItemModels)
                        )
        );
    }

    public boolean checkAddressHasChanged(RecipientAddressModel oldModel, RecipientAddressModel newModel) {
        if (oldModel.isCornerAddress()) return !oldModel.equalCorner(newModel);
        return !oldModel.equals(newModel);
    }

    public boolean checkShipmentItemHasChanged(List<ShipmentCartItemModel> oldShipmentCartItemModelList,
                                               List<ShipmentCartItemModel> newShipmentCartItemModelList) {
        List<ShipmentCartItemModel> finalShipmentCartItemModelList = new ArrayList<>(newShipmentCartItemModelList);
        if (oldShipmentCartItemModelList.size() != newShipmentCartItemModelList.size()) {
            return true;
        } else {
            List<ShipmentCartItemModel> equalShipmentCartItemModelList = new ArrayList<>();
            for (int i = 0; i < oldShipmentCartItemModelList.size(); i++) {
                ShipmentCartItemModel oldShipmentCartItemModel = oldShipmentCartItemModelList.get(i);
                boolean foundItem = false;
                for (ShipmentCartItemModel newShipmentCartItemModel : newShipmentCartItemModelList) {
                    if (oldShipmentCartItemModel.equals(newShipmentCartItemModel) &&
                            oldShipmentCartItemModel.getCartItemModels().size() == newShipmentCartItemModel.getCartItemModels().size() &&
                            oldShipmentCartItemModel.getShopShipmentList().size() == newShipmentCartItemModel.getShopShipmentList().size()) {
                        for (ShopShipment oldShopShipment : oldShipmentCartItemModel.getShopShipmentList()) {
                            for (ShopShipment newShopShipment : newShipmentCartItemModel.getShopShipmentList()) {
                                if (oldShopShipment.getShipId() == newShopShipment.getShipId() &&
                                        oldShopShipment.getShipProds().size() == newShopShipment.getShipProds().size() &&
                                        !equalShipmentCartItemModelList.contains(oldShipmentCartItemModel)) {
                                    equalShipmentCartItemModelList.add(oldShipmentCartItemModel);
                                    finalShipmentCartItemModelList.set(i, oldShipmentCartItemModel);
                                    foundItem = true;
                                    break;
                                }
                            }
                            if (foundItem) {
                                break;
                            }
                        }
                        if (foundItem) {
                            break;
                        }
                    }
                }
            }

            newShipmentCartItemModelList.clear();
            newShipmentCartItemModelList.addAll(finalShipmentCartItemModelList);

            return equalShipmentCartItemModelList.size() != newShipmentCartItemModelList.size();
        }
    }

    @Override
    public void processReloadCheckoutPageBecauseOfError(boolean isOneClickShipment, boolean isTradeIn, String deviceId) {
        getView().showLoading();
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        Map<String, String> params = getGeneratedAuthParamNetwork(paramGetShipmentForm);

        if (isOneClickShipment) {
            if (isTradeIn) {
                params.put(GetShipmentAddressFormOneClickShipementUseCase.PARAM_IS_TRADEIN, String.valueOf(isTradeIn));
                params.put(GetShipmentAddressFormOneClickShipementUseCase.PARAM_DEVICE_ID, deviceId);
            }
            requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS, params);
            compositeSubscription.add(
                    getShipmentAddressFormOneClickShipementUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(new GetShipmentAddressFormSubscriber(this, getView(),
                                    true, false, true))

            );
        } else {
            requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS, params);
            compositeSubscription.add(
                    getShipmentAddressFormUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(new GetShipmentAddressFormSubscriber(this, getView(),
                                    true, false, false))

            );
        }
    }

    private Map<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return originParams == null
                ? AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(), userSessionInterface.getDeviceId(), new TKPDMapParam<>())
                : AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(), userSessionInterface.getDeviceId(), originParams);
    }

    @Override
    public void processCheckout(CheckPromoParam checkPromoParam, boolean hasInsurance,
                                boolean isOneClickShipment, boolean isTradeIn, boolean isTradeInDropOff,
                                String deviceId, String cornerId, String leasingId) {
        removeErrorShopProduct();
        CheckoutRequest checkoutRequest = generateCheckoutRequest(null, hasInsurance, checkPromoParam,
                shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0, leasingId
        );

        if (checkoutRequest != null) {
            getView().showLoading();
            RequestParams requestParams = RequestParams.create();
            if (isTradeIn) {
                Map<String, String> params = new HashMap<>();
                params.put(CheckoutUseCase.PARAM_IS_TRADEIN, String.valueOf(true));
                params.put(CheckoutUseCase.PARAM_IS_TRADE_IN_DROP_OFF, String.valueOf(isTradeInDropOff));
                params.put(CheckoutUseCase.PARAM_DEVICE_ID, deviceId);
                requestParams.putObject(CheckoutUseCase.PARAM_TRADE_IN_DATA, params);
            }
            requestParams.putObject(CheckoutUseCase.PARAM_CARTS, checkoutRequest);
            requestParams.putBoolean(CheckoutUseCase.PARAM_ONE_CLICK_SHIPMENT, isOneClickShipment);
            compositeSubscription.add(
                    checkoutUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(getSubscriberCheckoutCart(checkoutRequest, isOneClickShipment, isTradeIn, deviceId, cornerId, leasingId))
            );
        } else {
            getView().showToastError(getView().getActivityContext().getString(R.string.default_request_error_unknown));
        }
    }

    private boolean removeErrorShopProduct() {
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
            return true;
        }
        return false;
    }

    @Override
    public void checkPromoFinalStackShipment(Promo promo) {
        TokopediaCornerData cornerData = null;
        RecipientAddressModel recipientAddressModel = getRecipientAddressModel();
        if (recipientAddressModel != null && recipientAddressModel.isCornerAddress()) {
            cornerData = new TokopediaCornerData(
                    recipientAddressModel.getUserCornerId(),
                    Integer.parseInt(recipientAddressModel.getCornerId())
            );
        }
        promo.setTokopediCornerData(cornerData);
        checkPromoStackingCodeFinalUseCase.setParams(promo);
        checkPromoStackingCodeFinalUseCase.execute(RequestParams.create(),
                new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (getView() != null) {
                            getView().renderErrorCheckPromoShipmentData(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                        }
                    }

                    @Override
                    public void onNext(GraphqlResponse graphqlResponse) {
                        setCouponStateChanged(true);
                        checkPromoStackingCodeMapper.setFinal(true);
                        ResponseGetPromoStackUiModel responseGetPromoStack = checkPromoStackingCodeMapper.map(graphqlResponse);
                        if (!TextUtils.isEmpty(responseGetPromoStack.getData().getTickerInfoUiModel().getMessage())) {
                            if (tickerAnnouncementHolderData == null) {
                                setTickerAnnouncementHolderData(
                                        new TickerAnnouncementHolderData(
                                                String.valueOf(responseGetPromoStack.getData().getTickerInfoUiModel().getStatusCode()),
                                                responseGetPromoStack.getData().getTickerInfoUiModel().getMessage())
                                );
                            }
                            getView().updateTickerAnnouncementMessage();
                            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerAnnouncementHolderData.getId());
                        }
                        if (responseGetPromoStack.getStatus().equalsIgnoreCase("ERROR")) {
                            String message = "";
                            if (responseGetPromoStack.getMessage().size() > 0) {
                                message = responseGetPromoStack.getMessage().get(0);
                            }
                            if (getView() != null) {
                                getView().renderErrorCheckPromoShipmentData(message);
                                getView().resetPromoBenefit();
                                getView().cancelAllCourierPromo();
                            }
                        } else {
                            getView().renderCheckPromoStackingShipmentDataSuccess(responseGetPromoStack);
                            getView().resetPromoBenefit();
                            getView().setPromoBenefit(responseGetPromoStack.getData().getBenefit().getSummaries());
                            if (responseGetPromoStack.getData().getMessage().getState().equals("red")) {
                                getView().showToastError(responseGetPromoStack.getData().getMessage().getText());
                            } else {
                                for (VoucherOrdersItemUiModel voucherOrdersItemUiModel : responseGetPromoStack.getData().getVoucherOrders()) {
                                    if (voucherOrdersItemUiModel.getMessage().getState().equals("red")) {
                                        getView().showToastError(voucherOrdersItemUiModel.getMessage().getText());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void checkPromoCheckoutFinalShipment(ValidateUsePromoRequest validateUsePromoRequest) {
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
                                           e.printStackTrace();
                                           if (getView() != null) {
                                               getView().renderErrorCheckPromoShipmentData(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                           }
                                       }

                                       @Override
                                       public void onNext(ValidateUsePromoRevampUiModel responseGetPromoStack) {
                                           setCouponStateChanged(true);
                                           if (!TextUtils.isEmpty(responseGetPromoStack.getPromoUiModel().getTickerInfoUiModel().getMessage())) {
                                               if (tickerAnnouncementHolderData == null) {
                                                   setTickerAnnouncementHolderData(
                                                           new TickerAnnouncementHolderData(
                                                                   String.valueOf(responseGetPromoStack.getPromoUiModel().getTickerInfoUiModel().getStatusCode()),
                                                                   responseGetPromoStack.getPromoUiModel().getTickerInfoUiModel().getMessage())
                                                   );
                                               }
                                               getView().updateTickerAnnouncementMessage();
                                               analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerAnnouncementHolderData.getId());
                                           }
                                           if (responseGetPromoStack.getStatus().equalsIgnoreCase("ERROR")) {
                                               String message = "";
                                               if (responseGetPromoStack.getMessage().size() > 0) {
                                                   message = responseGetPromoStack.getMessage().get(0);
                                               }
                                               if (getView() != null) {
                                                   getView().renderErrorCheckPromoShipmentData(message);
                                                   getView().resetPromoBenefit();
                                                   getView().cancelAllCourierPromo();
                                               }
                                           } else {
                                               // Todo: waiting backend to set promo benefit
//                                               getView().renderCheckPromoStackingShipmentDataSuccess(responseGetPromoStack);
//                                               getView().resetPromoBenefit();
//                                               getView().setPromoBenefit(responseGetPromoStack.getPromoUiModel().getData().getBenefit().getSummaries());
                                               if (responseGetPromoStack.getPromoUiModel().getMessageUiModel().getState().equals("red")) {
                                                   getView().showToastError(responseGetPromoStack.getPromoUiModel().getMessageUiModel().getText());
                                               } else {
                                                   for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : responseGetPromoStack.getPromoUiModel().getVoucherOrderUiModels()) {
                                                       if (voucherOrdersItemUiModel.getMessageUiModel().getState().equals("red")) {
                                                           getView().showToastError(voucherOrdersItemUiModel.getMessageUiModel().getText());
                                                           break;
                                                       }
                                                   }
                                               }
                                           }

                                           ClashingInfoDetailUiModel clashingInfoDetailUiModel = responseGetPromoStack.getPromoUiModel().getClashingInfoDetailUiModel();
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
                        )
        );

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
                e.printStackTrace();
                String errorMessage = e.getMessage();
                if (!(e instanceof CartResponseErrorException)) {
                    errorMessage = ErrorHandler.getErrorMessage(getView().getActivityContext(), e);
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
                        getView().renderCheckoutCartError(getView().getActivityContext().getString(R.string.default_request_error_unknown));
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
                                    enhancedECommerceProductCartMapData.setProductName(productDataCheckoutRequest.getProductName());
                                    enhancedECommerceProductCartMapData.setProductID(String.valueOf(productDataCheckoutRequest.getProductId()));
                                    enhancedECommerceProductCartMapData.setPrice(productDataCheckoutRequest.getProductPrice());
                                    enhancedECommerceProductCartMapData.setBrand("");
                                    enhancedECommerceProductCartMapData.setCategory(productDataCheckoutRequest.getProductCategory());
                                    enhancedECommerceProductCartMapData.setVariant("");
                                    enhancedECommerceProductCartMapData.setQty(productDataCheckoutRequest.getProductQuantity());
                                    enhancedECommerceProductCartMapData.setShopId(productDataCheckoutRequest.getProductShopId());
                                    enhancedECommerceProductCartMapData.setShopName(productDataCheckoutRequest.getProductShopName());
                                    enhancedECommerceProductCartMapData.setShopType(productDataCheckoutRequest.getProductShopType());
                                    enhancedECommerceProductCartMapData.setCategoryId(productDataCheckoutRequest.getProductCategoryId());
                                    enhancedECommerceProductCartMapData.setDimension38(productDataCheckoutRequest.getProductAttribution());
                                    enhancedECommerceProductCartMapData.setDimension40(productDataCheckoutRequest.getProductListName());
                                    enhancedECommerceProductCartMapData.setDimension45(String.valueOf(productDataCheckoutRequest.getCartId()));
                                    enhancedECommerceProductCartMapData.setDimension53(productDataCheckoutRequest.isDiscountedPrice());
                                    enhancedECommerceProductCartMapData.setDimension54(getFulfillmentStatus(shopProductCheckoutRequest.getShopId()));
                                    enhancedECommerceProductCartMapData.setDimension12(shopProductCheckoutRequest.shippingInfo.analyticsDataShippingCourierPrice);
                                    enhancedECommerceProductCartMapData.setWarehouseId(productDataCheckoutRequest.getWarehouseId());
                                    enhancedECommerceProductCartMapData.setProductWeight(productDataCheckoutRequest.getProductWeight());
                                    enhancedECommerceProductCartMapData.setPromoCode(productDataCheckoutRequest.getPromoCode());
                                    enhancedECommerceProductCartMapData.setPromoDetails(productDataCheckoutRequest.getPromoDetails());
                                    enhancedECommerceProductCartMapData.setCartId(String.valueOf(productDataCheckoutRequest.getCartId()));
                                    enhancedECommerceProductCartMapData.setBuyerAddressId(productDataCheckoutRequest.getBuyerAddressId());
                                    enhancedECommerceProductCartMapData.setCourier(productDataCheckoutRequest.getCourier());
                                    enhancedECommerceProductCartMapData.setShippingPrice(productDataCheckoutRequest.getShippingPrice());
                                    enhancedECommerceProductCartMapData.setCodFlag(productDataCheckoutRequest.getCodFlag());
                                    enhancedECommerceProductCartMapData.setTokopediaCornerFlag(productDataCheckoutRequest.getTokopediaCornerFlag());
                                    enhancedECommerceProductCartMapData.setIsFulfillment(productDataCheckoutRequest.getIsFulfillment());
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
                                e.printStackTrace();
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

    @Override
    public void processCheckPromoStackingLogisticPromo(int cartPosition, String cartString, String code) {
        Promo generatedPromo = null;
        if (getView() != null) {
            // Cleaning all codes except logistic promo code
            generatedPromo = getView().generateCheckPromoFirstStepParam();
            generatedPromo.setCodes(new ArrayList<>());
            if (generatedPromo.getOrders() != null) {
                for (Order order : generatedPromo.getOrders()) {
                    ArrayList<String> arr = new ArrayList<>();
                    if (order.getUniqueId() != null && order.getUniqueId().equals(cartString)) {
                        arr.add(code);
                    }
                    order.setCodes(arr);
                }
            }
            CurrentApplyCode currentApplyCode = new CurrentApplyCode();
            currentApplyCode.setCode(code);
            currentApplyCode.setType(PARAM_LOGISTIC);
            generatedPromo.setCurrentApplyCode(currentApplyCode);
            checkPromoStackingCodeUseCase.setParams(generatedPromo);
            compositeSubscription.add(
                    checkPromoStackingCodeUseCase.createObservable(RequestParams.create())
                            .subscribe(new Subscriber<ResponseGetPromoStackUiModel>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    if (getView() != null) {
                                        mTrackerShipment.eventClickLanjutkanTerapkanPromoError(e.getMessage());
                                        getView().showToastError(e.getMessage());
                                        getView().resetCourier(cartPosition);
                                    }
                                }

                                @Override
                                public void onNext(ResponseGetPromoStackUiModel responseGetPromoStack) {
                                    if (getView() != null) {
                                        checkPromoStackingCodeMapper.setFinal(false);
                                        String errMessage = "";

                                        if (responseGetPromoStack.getStatus().equalsIgnoreCase(statusOK)) {
                                            if (responseGetPromoStack.getData().getClashings().isClashedPromos()) {
                                                getView().onClashCheckPromo(responseGetPromoStack.getData().getClashings(), PARAM_LOGISTIC);
                                            } else {
                                                for (VoucherOrdersItemUiModel voucherOrdersItemUiModel : responseGetPromoStack.getData().getVoucherOrders()) {
                                                    if (voucherOrdersItemUiModel.getCode().equalsIgnoreCase(code)) {
                                                        // reset duration if getting red state
                                                        if (TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(voucherOrdersItemUiModel.getMessage().getState()) == TickerPromoStackingCheckoutView.State.FAILED) {
                                                            mTrackerShipment.eventClickLanjutkanTerapkanPromoError(voucherOrdersItemUiModel.getMessage().getText());
                                                            getView().showToastError(voucherOrdersItemUiModel.getMessage().getText());
                                                            getView().resetCourier(cartPosition);
                                                        } else {
                                                            mTrackerShipment.eventClickLanjutkanTerapkanPromoSuccess(code);
                                                            getView().renderCheckPromoStackLogisticSuccess(responseGetPromoStack, code);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            // reset duration when response error
                                            if (!responseGetPromoStack.getMessage().isEmpty()) {
                                                errMessage = responseGetPromoStack.getMessage().get(0);
                                            }
                                            mTrackerShipment.eventClickLanjutkanTerapkanPromoError(errMessage);
                                            getView().showToastError(errMessage);
                                            getView().resetCourier(cartPosition);
                                        }
                                    }
                                }
                            })
            );
        }
    }

    @Override
    public void processCheckPromoCheckoutCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast) {
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
                                e.printStackTrace();
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
                                setCouponStateChanged(true);
                                if (getView() != null) {

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
    public void processCheckPromoStackingCodeFromSelectedCourier(String promoCode, int itemPosition, boolean noToast) {
        Promo promo = getView().generateCheckPromoFirstStepParam();
        ArrayList<String> listCodes = new ArrayList<>();
        if (!TextUtils.isEmpty(promoCode)) {
            listCodes.add(promoCode);

            CurrentApplyCode currentApplyCode = new CurrentApplyCode();
            if (!promoCode.isEmpty()) {
                currentApplyCode.setCode(promoCode);
                currentApplyCode.setType(PARAM_GLOBAL);
            }
            promo.setCurrentApplyCode(currentApplyCode);
        }
        promo.setCodes(listCodes);
        checkPromoStackingCodeUseCase.setParams(promo);
        compositeSubscription.add(
                checkPromoStackingCodeUseCase.createObservable(RequestParams.create())
                        .subscribe(new Subscriber<ResponseGetPromoStackUiModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                if (getView() != null) {
                                    if (e instanceof CheckPromoCodeException) {
                                        getView().showToastError(e.getMessage());
                                    } else {
                                        getView().showToastError(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                                    }
                                }
                            }

                            @Override
                            public void onNext(ResponseGetPromoStackUiModel responseGetPromoStack) {
                                setCouponStateChanged(true);
                                checkPromoStackingCodeMapper.setFinal(false);
                                if (getView() != null) {
                                    if (responseGetPromoStack.getStatus().equalsIgnoreCase(statusOK)) {
                                        if (responseGetPromoStack.getData().getClashings().isClashedPromos()) {
                                            getView().onClashCheckPromo(responseGetPromoStack.getData().getClashings(), PARAM_GLOBAL);
                                        } else {
                                            if (responseGetPromoStack.getData().getCodes().get(0).equalsIgnoreCase(promoCode)) {
                                                if (TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(responseGetPromoStack.getData().getMessage().getState()) == TickerPromoStackingCheckoutView.State.FAILED) {
                                                    String message = responseGetPromoStack.getData().getMessage().getText();
                                                    getView().renderErrorCheckPromoShipmentData(message);
                                                } else {
                                                    getView().renderCheckPromoStackCodeFromCourierSuccess(responseGetPromoStack, itemPosition, noToast);
                                                }
                                            }
                                        }
                                    } else {
                                        if (!noToast) {
                                            getView().showToastError(responseGetPromoStack.getMessage().get(0));
                                        }
                                    }
                                }
                            }
                        })
        );
    }

    @Override
    public CheckoutRequest generateCheckoutRequest(List<DataCheckoutRequest> analyticsDataCheckoutRequests,
                                                   boolean hasInsurance,
                                                   CheckPromoParam checkPromoParam,
                                                   int isDonation,
                                                   String leasingId) {
        if (analyticsDataCheckoutRequests == null && dataCheckoutRequestList == null) {
            getView().showToastError(getView().getActivityContext().getString(R.string.default_request_error_unknown_short));
            return null;
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

        if (checkPromoParam != null && checkPromoParam.getPromo() != null) {
            if (checkPromoParam.getPromo().getCodes() != null && checkPromoParam.getPromo().getCodes().size() > 0) {
                builder.promoCodes(checkPromoParam.getPromo().getCodes());
                List<PromoRequest> promoRequests = new ArrayList<>();
                for (String promoCode : checkPromoParam.getPromo().getCodes()) {
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

    @Override
    public void processSaveShipmentState(ShipmentCartItemModel shipmentCartItemModel) {
        List<ShipmentCartItemModel> shipmentCartItemModels = new ArrayList<>();
        shipmentCartItemModels.add(shipmentCartItemModel);

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        JsonArray saveShipmentDataArray = getShipmentItemSaveStateData(shipmentCartItemModels);
        param.put(SaveShipmentStateUseCase.PARAM_CARTS, saveShipmentDataArray.toString());

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SaveShipmentStateUseCase.PARAM_CART_DATA_OBJECT, param);

        compositeSubscription.add(saveShipmentStateUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new SaveShipmentStateSubscriber(getView())));
    }

    @Override
    public void processSaveShipmentState() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        JsonArray saveShipmentDataArray = getShipmentItemSaveStateData(shipmentCartItemModelList);
        param.put(SaveShipmentStateUseCase.PARAM_CARTS, saveShipmentDataArray.toString());

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SaveShipmentStateUseCase.PARAM_CART_DATA_OBJECT, param);

        getView().showLoading();
        compositeSubscription.add(saveShipmentStateUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new SaveShipmentStateSubscriber(getView())));
    }

    private JsonArray getShipmentItemSaveStateData(List<ShipmentCartItemModel> shipmentCartItemModels) {
        SaveShipmentStateRequest saveShipmentStateRequest;
        if (recipientAddressModel != null) {
            saveShipmentStateRequest = generateSaveShipmentStateRequestSingleAddress(shipmentCartItemModels);
        } else {
            saveShipmentStateRequest = generateSaveShipmentStateRequestMultipleAddress(shipmentCartItemModels);
        }
        String saveShipmentDataString = new Gson().toJson(saveShipmentStateRequest.getRequestDataList());
        return new JsonParser().parse(saveShipmentDataString).getAsJsonArray();
    }

    private SaveShipmentStateRequest generateSaveShipmentStateRequestSingleAddress(
            List<ShipmentCartItemModel> shipmentCartItemModels) {

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

    private SaveShipmentStateRequest generateSaveShipmentStateRequestMultipleAddress(
            List<ShipmentCartItemModel> shipmentCartItemModels) {

        List<ShipmentStateRequestData> shipmentStateRequestDataList = new ArrayList<>();
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
            List<ShipmentStateShopProductData> shipmentStateShopProductDataList = new ArrayList<>();

            setSaveShipmentStateData(shipmentCartItemModel, shipmentStateShopProductDataList);

            ShipmentStateRequestData shipmentStateRequestData = new ShipmentStateRequestData.Builder()
                    .addressId(Integer.parseInt(shipmentCartItemModel.getRecipientAddressModel().getId()))
                    .shopProductDataList(shipmentStateShopProductDataList)
                    .build();
            shipmentStateRequestDataList.add(shipmentStateRequestData);
        }

        return new SaveShipmentStateRequest.Builder()
                .requestDataList(shipmentStateRequestDataList)
                .build();
    }

    private void setSaveShipmentStateData(ShipmentCartItemModel shipmentCartItemModel, List<ShipmentStateShopProductData> shipmentStateShopProductDataList) {
        // todo: refactor to converter class
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
                                    e.printStackTrace();
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
                                            e.printStackTrace();
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
                                                messageError = getView().getActivityContext().getString(R.string.default_request_error_unknown);
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

    @Override
    public void cancelAutoApplyPromoStack(int shopIndex, ArrayList<String> promoCodeList, boolean ignoreAPIResponse, String voucherType) {
        if (promoCodeList.size() > 0) {
            if (!ignoreAPIResponse) {
                getView().showLoading();
            }
            clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), promoCodeList);
            compositeSubscription.add(
                    clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                            .subscribe(new ClearShipmentCacheAutoApplySubscriber(getView(), this, voucherType, shopIndex, ignoreAPIResponse))
            );
        }
    }

    @Override
    public void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList, int checkoutType) {
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

    @Override
    public void cancelAutoApplyPromoStackLogistic(String promoCode) {
        ArrayList<String> promoCodeList = new ArrayList<>();
        promoCodeList.add(promoCode);

        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), promoCodeList);
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe(new Subscriber<ClearCacheAutoApplyStackResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // Do nothing
                    }

                    @Override
                    public void onNext(ClearCacheAutoApplyStackResponse responseData) {
                        if (getView() != null) {
                            if (!TextUtils.isEmpty(responseData.getSuccessData().getTickerMessage())) {
                                tickerAnnouncementHolderData.setMessage(responseData.getSuccessData().getTickerMessage());
                                getView().updateTickerAnnouncementMessage();
                            }
                            getView().onSuccessClearPromoLogistic();
                        }
                    }
                })
        );
    }

    @Override
    public void cancelAutoApplyPromoStackAfterClash(ArrayList<String> promoCodesToBeCleared) {
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
                    dataChangeAddressRequest.setCartId(cartItemModel.getCartId());
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

        String changeAddressRequestJsonString = new Gson().toJson(dataChangeAddressRequests);

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("carts", changeAddressRequestJsonString);
        RequestParams requestParam = RequestParams.create();

        Map<String, String> authParam = AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(), userSessionInterface.getDeviceId(), param);

        requestParam.putAllString(authParam);
        requestParam.putBoolean(ChangeShippingAddressUseCase.PARAM_ONE_CLICK_SHIPMENT,
                isOneClickShipment);

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
                                if (getView() != null) {
                                    getView().hideLoading();
                                    e.printStackTrace();
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
    public void setShippingCourierViewModelsState(List<ShippingCourierUiModel> shippingCourierUiModelsState,
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
    public void setHasDeletePromoAfterChecKPromoCodeFinal(boolean hasDeletePromoAfterChecKPromoCodeFinal) {
        this.hasDeletePromoAfterChecKPromoCodeFinal = hasDeletePromoAfterChecKPromoCodeFinal;
    }

    @Override
    public boolean getHasDeletePromoAfterChecKPromoCodeFinal() {
        return hasDeletePromoAfterChecKPromoCodeFinal;
    }


    @Override
    public CodModel getCodData() {
        return this.codData;
    }

    @Override
    public void proceedCodCheckout(CheckPromoParam checkPromoParam, boolean hasInsurance,
                                   boolean isOneClickShipment, boolean isTradeIn,
                                   String deviceId, String leasingId) {
        CheckoutRequest checkoutRequest = generateCheckoutRequest(null, hasInsurance, checkPromoParam,
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
                processReloadCheckoutPageBecauseOfError(isOneClickShipment, isTradeIn, deviceId);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                getView().hideLoading();
                CodResponse response = graphqlResponse.getData(CodResponse.class);
                if (getView() == null || !response.getValidateCheckoutCod().getHeader().getErrorCode().equals("200")) {
                    mTrackerCod.eventClickBayarDiTempatShipmentNotSuccessIncomplete();
                    processReloadCheckoutPageBecauseOfError(isOneClickShipment, isTradeIn, deviceId);
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
    public void setLastApplyData(LastApplyUiModel lastApplyData) {
        this.lastApplyData = lastApplyData;
    }

    @Override
    public LastApplyUiModel getLastApplyData() {
        return lastApplyData;
    }

    @Override
    public ValidateUsePromoRevampUiModel getValidateUsePromoRevampUiModel() {
        return validateUsePromoRevampUiModel;
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
                        e.printStackTrace();
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
}
