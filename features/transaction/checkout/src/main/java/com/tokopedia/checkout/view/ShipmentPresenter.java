package com.tokopedia.checkout.view;

import static com.tokopedia.checkout.data.model.request.checkout.CheckoutRequestKt.FEATURE_TYPE_REGULAR_PRODUCT;
import static com.tokopedia.checkout.data.model.request.checkout.CheckoutRequestKt.FEATURE_TYPE_TOKONOW_PRODUCT;
import static com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ADD_ON_DETAILS;
import static com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.IS_DONATION;
import static com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ORDER_LEVEL;
import static com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.PRODUCT_LEVEL;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.checkout.data.model.request.changeaddress.DataChangeAddressRequest;
import com.tokopedia.checkout.data.model.request.checkout.CheckoutRequestMapper;
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellItemRequestModel;
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellRequest;
import com.tokopedia.checkout.data.model.request.checkout.old.CheckoutRequest;
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest;
import com.tokopedia.checkout.data.model.request.checkout.old.EgoldData;
import com.tokopedia.checkout.data.model.request.checkout.old.ProductDataCheckoutRequest;
import com.tokopedia.checkout.data.model.request.checkout.old.PromoRequest;
import com.tokopedia.checkout.data.model.request.checkout.old.ShopProductCheckoutRequest;
import com.tokopedia.checkout.data.model.request.checkout.old.TokopediaCornerData;
import com.tokopedia.checkout.data.model.request.common.RatesFeature;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.SaveShipmentStateRequest;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateDropshipData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductPreorder;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShippingInfoData;
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShopProductData;
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper;
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.model.cartshipmentform.DynamicDataPassingParamRequest;
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData;
import com.tokopedia.checkout.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase;
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase;
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateDynamicDataPassingUseCase;
import com.tokopedia.checkout.utils.CheckoutFingerprintUtil;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.checkout.view.converter.ShipmentDataConverter;
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.helper.ShipmentCartItemModelHelper;
import com.tokopedia.checkout.view.helper.ShipmentGetCourierHolderData;
import com.tokopedia.checkout.view.subscriber.ClearNotEligiblePromoSubscriber;
import com.tokopedia.checkout.view.subscriber.ClearShipmentCacheAutoApplyAfterClashSubscriber;
import com.tokopedia.checkout.view.subscriber.GetBoPromoCourierRecommendationSubscriber;
import com.tokopedia.checkout.view.subscriber.GetCourierRecommendationSubscriber;
import com.tokopedia.checkout.view.subscriber.ReleaseBookingStockSubscriber;
import com.tokopedia.checkout.view.subscriber.SaveShipmentStateSubscriber;
import com.tokopedia.checkout.view.uimodel.CrossSellModel;
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel;
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel;
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel;
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel;
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel;
import com.tokopedia.fingerprint.util.FingerPrintUtil;
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel;
import com.tokopedia.logisticCommon.data.constant.AddressConstant;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.address.UserAddress;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticCommon.domain.param.EditAddressParam;
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase;
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase;
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
import com.tokopedia.network.authentication.AuthHelper;
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
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse;
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel;
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnBottomSheetModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnButtonModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnDataItemModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnMetadataItemModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnNoteItemModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnProductItemModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnTickerModel;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnBottomSheetResult;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnButtonResult;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnMetadata;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnNote;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.ProductResult;
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem;
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest;
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase;
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel;
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
import rx.subjects.PublishSubject;
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
    private final GetShipmentAddressFormV3UseCase getShipmentAddressFormV3UseCase;
    private final EditAddressUseCase editAddressUseCase;
    private final ChangeShippingAddressGqlUseCase changeShippingAddressGqlUseCase;
    private final SaveShipmentStateGqlUseCase saveShipmentStateGqlUseCase;
    private final GetRatesUseCase ratesUseCase;
    private final GetRatesApiUseCase ratesApiUseCase;
    private final ShippingCourierConverter shippingCourierConverter;
    private final OldClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;
    private final UserSessionInterface userSessionInterface;
    private final ShipmentDataConverter shipmentDataConverter;
    private final ReleaseBookingUseCase releaseBookingUseCase;
    private final GetPrescriptionIdsUseCase prescriptionIdsUseCase;
    private final OldValidateUsePromoRevampUseCase validateUsePromoRevampUseCase;
    private final EligibleForAddressUseCase eligibleForAddressUseCase;
    private final UpdateDynamicDataPassingUseCase updateDynamicDataPassingUseCase;
    private final ExecutorSchedulers executorSchedulers;

    private ShipmentUpsellModel shipmentUpsellModel = new ShipmentUpsellModel();
    private ShipmentNewUpsellModel shipmentNewUpsellModel = new ShipmentNewUpsellModel();
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
    private boolean isBoUnstackEnabled = false;
    private String cartData = "";

    private ShipmentContract.AnalyticsActionListener analyticsActionListener;
    private CheckoutAnalyticsPurchaseProtection mTrackerPurchaseProtection;
    private CheckoutAnalyticsCourierSelection mTrackerShipment;
    private String statusOK = "OK";
    private String statusCode200 = "200";
    private RatesResponseStateConverter stateConverter;
    private LastApplyUiModel lastApplyData;
    private UploadPrescriptionUiModel uploadPrescriptionUiModel;

    private PublishSubject<ShipmentGetCourierHolderData> ratesPublisher = null;
    private PublishSubject<ShipmentGetCourierHolderData> ratesPromoPublisher = null;
    private PublishSubject<Boolean> logisticDonePublisher = null;
    private PublishSubject<Boolean> logisticPromoDonePublisher = null;

    public boolean isUsingDynamicDataPassing = false;
    private DynamicDataPassingParamRequest dynamicDataParam;
    private String dynamicData = "";

    @Inject
    public ShipmentPresenter(CompositeSubscription compositeSubscription,
                             CheckoutGqlUseCase checkoutGqlUseCase,
                             GetShipmentAddressFormV3UseCase getShipmentAddressFormV3UseCase,
                             EditAddressUseCase editAddressUseCase,
                             ChangeShippingAddressGqlUseCase changeShippingAddressGqlUseCase,
                             SaveShipmentStateGqlUseCase saveShipmentStateGqlUseCase,
                             GetRatesUseCase ratesUseCase,
                             GetRatesApiUseCase ratesApiUseCase,
                             OldClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                             RatesResponseStateConverter stateConverter,
                             ShippingCourierConverter shippingCourierConverter,
                             ShipmentContract.AnalyticsActionListener shipmentAnalyticsActionListener,
                             UserSessionInterface userSessionInterface,
                             CheckoutAnalyticsPurchaseProtection analyticsPurchaseProtection,
                             CheckoutAnalyticsCourierSelection checkoutAnalytics,
                             ShipmentDataConverter shipmentDataConverter,
                             ReleaseBookingUseCase releaseBookingUseCase,
                             GetPrescriptionIdsUseCase prescriptionIdsUseCase,
                             OldValidateUsePromoRevampUseCase validateUsePromoRevampUseCase,
                             Gson gson,
                             ExecutorSchedulers executorSchedulers,
                             EligibleForAddressUseCase eligibleForAddressUseCase,
                             UpdateDynamicDataPassingUseCase updateDynamicDataPassingUseCase) {
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
        this.mTrackerPurchaseProtection = analyticsPurchaseProtection;
        this.userSessionInterface = userSessionInterface;
        this.mTrackerShipment = checkoutAnalytics;
        this.shipmentDataConverter = shipmentDataConverter;
        this.releaseBookingUseCase = releaseBookingUseCase;
        this.prescriptionIdsUseCase = prescriptionIdsUseCase;
        this.validateUsePromoRevampUseCase = validateUsePromoRevampUseCase;
        this.gson = gson;
        this.executorSchedulers = executorSchedulers;
        this.eligibleForAddressUseCase = eligibleForAddressUseCase;
        this.updateDynamicDataPassingUseCase = updateDynamicDataPassingUseCase;
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
        if (getShipmentAddressFormV3UseCase != null) {
            getShipmentAddressFormV3UseCase.cancelJobs();
        }
        if (eligibleForAddressUseCase != null) {
            eligibleForAddressUseCase.cancelJobs();
        }
        if (updateDynamicDataPassingUseCase != null) {
            updateDynamicDataPassingUseCase.cancelJobs();
        }
        ratesPublisher = null;
        logisticDonePublisher = null;
        ratesPromoPublisher = null;
        logisticPromoDonePublisher = null;
        dynamicDataParam = null;
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

    private boolean getPromoFlag(String step) {
        if (step.equals(EnhancedECommerceActionField.STEP_2)) {
            if (lastApplyData != null) {
                return lastApplyData.getAdditionalInfo().getPomlAutoApplied();
            }
            return false;
        } else if (validateUsePromoRevampUiModel != null) {
            return validateUsePromoRevampUiModel.getPromoUiModel().getAdditionalInfoUiModel().getPomlAutoApplied();
        }
        return false;
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
                listShipmentCrossSellModel, leasingId, uploadPrescriptionUiModel.getPrescriptionIds()
        );
        Map<String, Object> eeDataLayer = generateCheckoutAnalyticsDataLayer(checkoutRequest, step, pageSource);
        if (eeDataLayer != null) {
            String transactionId = "";
            if (checkoutData != null) {
                transactionId = checkoutData.getTransactionId();
            }
            analyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
                    eeDataLayer, tradeInCustomDimension, transactionId, userSessionInterface.getUserId(), getPromoFlag(step), eventCategory, eventAction, eventLabel
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
                                               @Nullable String leasingId,
                                               boolean isPlusSelected) {
        if (isReloadData) {
            getView().setHasRunningApiCall(true);
            getView().showLoading();
        } else {
            getView().showInitialLoading();
        }

        getShipmentAddressFormV3UseCase.setParams(
                isOneClickShipment, isTradeIn, isSkipUpdateOnboardingState, cornerId, deviceId, leasingId,
                isPlusSelected
        );
        getShipmentAddressFormV3UseCase.execute(
                cartShipmentAddressFormData -> {
                    if (getView() != null) {
                        getView().stopEmbraceTrace();
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
                        getView().stopEmbraceTrace();
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
            checkIsUserEligibleForRevampAna(cartShipmentAddressFormData);
        } else if (cartShipmentAddressFormData.getErrorCode() == CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST) {
            getView().renderCheckoutPageNoMatchedAddress(cartShipmentAddressFormData, userAddress != null ? userAddress.getState() : 0);
        } else if (cartShipmentAddressFormData.getErrorCode() == CartShipmentAddressFormData.NO_ERROR) {
            if (userAddress == null) {
                getView().onShipmentAddressFormEmpty();
            } else {
                getView().updateLocalCacheAddressData(userAddress);
                initializePresenterData(cartShipmentAddressFormData);
                setCurrentDynamicDataParamFromSAF(cartShipmentAddressFormData, isOneClickShipment);
                getView().renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHigher, isOneClickShipment);
                if (cartShipmentAddressFormData.getPopUpMessage().length() > 0) {
                    getView().showToastNormal(cartShipmentAddressFormData.getPopUpMessage());
                }
                if (cartShipmentAddressFormData.getPopup() != null) {
                    PopUpData popUpData = cartShipmentAddressFormData.getPopup();
                    if (!popUpData.getTitle().isEmpty() && !popUpData.getDescription().isEmpty()) {
                        getView().showPopUp(popUpData);
                    }
                }
            }

        }
        isUsingDynamicDataPassing = cartShipmentAddressFormData.isUsingDdp();
        dynamicData = cartShipmentAddressFormData.getDynamicData();
    }

    private void checkIsUserEligibleForRevampAna(CartShipmentAddressFormData cartShipmentAddressFormData) {
        eligibleForAddressUseCase.eligibleForAddressFeature(keroAddrIsEligibleForAddressFeature -> {
            if (getView() != null) {
                getView().renderCheckoutPageNoAddress(cartShipmentAddressFormData, keroAddrIsEligibleForAddressFeature.getEligibleForRevampAna().getEligible());
            }
            return Unit.INSTANCE;
        }, throwable -> {
            if (getView() != null) {
                String errorMessage = throwable.getMessage();
                if (errorMessage == null) {
                    errorMessage = getView().getActivityContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown_short);
                }
                getView().showToastError(errorMessage);
            }
            return Unit.INSTANCE;
        }, AddressConstant.ANA_REVAMP_FEATURE_ID);
    }

    public void initializePresenterData(CartShipmentAddressFormData cartShipmentAddressFormData) {
        setLatValidateUseRequest(null);
        setValidateUsePromoRevampUiModel(null);

        shipmentTickerErrorModel = new ShipmentTickerErrorModel(cartShipmentAddressFormData.getErrorTicker());

        if (cartShipmentAddressFormData.getTickerData() != null) {
            setTickerAnnouncementHolderData(
                    new TickerAnnouncementHolderData(cartShipmentAddressFormData.getTickerData().getId(),
                            cartShipmentAddressFormData.getTickerData().getTitle(),
                            cartShipmentAddressFormData.getTickerData().getMessage())
            );
            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerAnnouncementHolderData.getId());
        } else {
            setTickerAnnouncementHolderData(new TickerAnnouncementHolderData());
        }

        RecipientAddressModel newAddress = shipmentDataConverter
                .getRecipientAddressModel(cartShipmentAddressFormData);
        setRecipientAddressModel(newAddress);

        this.shipmentUpsellModel = shipmentDataConverter.getShipmentUpsellModel(cartShipmentAddressFormData.getUpsell());
        this.shipmentNewUpsellModel = shipmentDataConverter.getShipmentNewUpsellModel(cartShipmentAddressFormData.getNewUpsell());

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
        isBoUnstackEnabled = cartShipmentAddressFormData.getLastApplyData().getAdditionalInfo().getBebasOngkirInfo().isBoUnstackEnabled();

        setShipmentCartItemModelList(shipmentDataConverter.getShipmentItems(
                cartShipmentAddressFormData, newAddress != null && newAddress.getLocationDataModel() != null,
                userSessionInterface.getName())
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

        setUploadPrescriptionData(new UploadPrescriptionUiModel(
                cartShipmentAddressFormData.getPrescriptionShowImageUpload(),
                cartShipmentAddressFormData.getPrescriptionUploadText(),
                cartShipmentAddressFormData.getPrescriptionLeftIconUrl(),
                cartShipmentAddressFormData.getPrescriptionCheckoutId(),
                new ArrayList<>(),
                0,
                "",
                false,
                cartShipmentAddressFormData.getPrescriptionFrontEndValidation(),
                false
        ));
        fetchPrescriptionIds(cartShipmentAddressFormData.getPrescriptionShowImageUpload(), cartShipmentAddressFormData.getPrescriptionCheckoutId());

        cartData = cartShipmentAddressFormData.getCartData();
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
                                String leasingId,
                                boolean isPlusSelected) {
        removeErrorShopProduct();
        CheckoutRequest checkoutRequest = generateCheckoutRequest(null,
                shipmentDonationModel != null && shipmentDonationModel.isChecked() ? 1 : 0,
                listShipmentCrossSellModel, leasingId, uploadPrescriptionUiModel.getPrescriptionIds()
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

            Map<String, Object> params = generateCheckoutParams(isOneClickShipment, isTradeIn, isTradeInDropOff, deviceId, checkoutRequest, dynamicData);
            RequestParams requestParams = RequestParams.create();
            requestParams.putAll(params);
            compositeSubscription.add(
                    checkoutGqlUseCase.createObservable(requestParams)
                            .subscribe(getSubscriberCheckoutCart(
                                    checkoutRequest, isOneClickShipment, isTradeIn, deviceId,
                                    cornerId, leasingId, deviceModel, devicePrice, diagnosticId, isPlusSelected)
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
                                                      CheckoutRequest checkoutRequest,
                                                      String dynamicData) {
        Map<String, Object> params = new HashMap<>();
        params.put(CheckoutGqlUseCase.PARAM_CARTS, CheckoutRequestMapper.INSTANCE.map(checkoutRequest));
        params.put(CheckoutGqlUseCase.PARAM_IS_ONE_CLICK_SHIPMENT, String.valueOf(isOneClickShipment));
        params.put(CheckoutGqlUseCase.PARAM_DYNAMIC_DATA, dynamicData);
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
        boolean hasPromo = false;
        ArrayList<String> globalCodes = new ArrayList<>();
        for (String code : validateUsePromoRequest.getCodes()) {
            if (code != null) {
                globalCodes.add(code);
                hasPromo = true;
            }
        }
        validateUsePromoRequest.setCodes(new ArrayList<>());
        ArrayList<OrdersItem> cloneOrders = new ArrayList<>();
        ArrayList<ClearPromoOrder> clearOrders = new ArrayList<>();
        for (OrdersItem order : validateUsePromoRequest.getOrders()) {
            if (order != null) {
                clearOrders.add(new ClearPromoOrder(order.getUniqueId(), order.getBoType(),
                        order.getCodes(), order.getShopId(), order.isPo(),
                        String.valueOf(order.getPoDuration()), order.getWarehouseId()));
                if (!order.getCodes().isEmpty()) {
                    hasPromo = true;
                }
                order.setCodes(new ArrayList<>());
                cloneOrders.add(order);
            }
        }
        validateUsePromoRequest.setOrders(cloneOrders);
        ClearPromoRequest params = new ClearPromoRequest(OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, false, new ClearPromoOrderData(globalCodes, clearOrders));
        if (hasPromo) {
            clearCacheAutoApplyStackUseCase.setParams(params);
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
        requestParams.putObject(OldValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);

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
                                               if (!validateUsePromoRevampUiModel.getStatus().equalsIgnoreCase(statusOK) || !validateUsePromoRevampUiModel.getErrorCode().equals(statusCode200)) {
                                                   String message = "";
                                                   if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                                                       message = validateUsePromoRevampUiModel.getMessage().get(0);
                                                   } else {
                                                       message = DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO;
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

                                                   cancelAutoApplyPromoStackAfterClash(clashingInfoDetailUiModel);
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
                                "",
                                validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getMessage())
                );
            } else {
                tickerAnnouncementHolderData.setId(String.valueOf(validateUsePromoRevampUiModel.getPromoUiModel().getTickerInfoUiModel().getStatusCode()));
                tickerAnnouncementHolderData.setTitle("");
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
                                                               String diagnosticId,
                                                               boolean isPlusSelected) {
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
                processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, cornerId, deviceId, leasingId, isPlusSelected);
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
                    for (ShipmentCrossSellModel shipmentCrossSellModel : listShipmentCrossSellModel) {
                        if (shipmentCrossSellModel.isChecked()) isCrossSellChecked = true;
                    }
                    if (isCrossSellChecked) triggerCrossSellClickPilihPembayaran();
                    getView().renderCheckoutCartSuccess(checkoutData);
                } else if (checkoutData.getPriceValidationData().isUpdated()) {
                    getView().hideLoading();
                    getView().renderCheckoutPriceUpdated(checkoutData.getPriceValidationData());
                } else if (checkoutData.getPrompt().getEligible()) {
                    getView().hideLoading();
                    getView().renderPrompt(checkoutData.getPrompt());
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
                    processInitialLoadCheckoutPage(true, isOneClickShipment, isTradeIn, true, false, cornerId, deviceId, leasingId, isPlusSelected);
                }
            }
        };
    }

    private void triggerCrossSellClickPilihPembayaran() {
        List<ShipmentCrossSellModel> shipmentCrossSellModelList = getListShipmentCrossSellModel();
        String eventLabel = "";
        String digitalProductName = "";

        if (!shipmentCrossSellModelList.isEmpty()) {
            for (int i = 0; i < shipmentCrossSellModelList.size(); i++) {
                CrossSellModel crossSellModel = shipmentCrossSellModelList.get(i).getCrossSellModel();
                String digitalCategoryName = crossSellModel.getOrderSummary().getTitle();
                String digitalProductId = crossSellModel.getId();
                eventLabel = digitalCategoryName + " - " + digitalProductId;
                digitalProductName = crossSellModel.getInfo().getTitle();
            }
        }

        List<Object> productList = new ArrayList<>();
        for (int j = 0; j < shipmentCartItemModelList.size(); j++) {
            for (CartItemModel cartItemModel : shipmentCartItemModelList.get(j).getCartItemModels()) {
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
        analyticsActionListener.sendEnhancedEcommerceAnalyticsCrossSellClickPilihPembayaran(eventLabel, userSessionInterface.getUserId(), productList);
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
                                    enhancedECommerceProductCartMapData.setDimension83(productDataCheckoutRequest.getFreeShippingName());
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
    public void doValidateUseLogisticPromo(int cartPosition, String cartString, ValidateUsePromoRequest validateUsePromoRequest, String promoCode) {
        if (getView() != null) {
            setCouponStateChanged(true);
            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(OldValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);
            getView().setStateLoadingCourierStateAtIndex(cartPosition, true);

            compositeSubscription.add(
                    validateUsePromoRevampUseCase.createObservable(requestParams)
                            .subscribeOn(executorSchedulers.getIo())
                            .observeOn(executorSchedulers.getMain())
                            .subscribe(new Subscriber<ValidateUsePromoRevampUiModel>() {
                                @Override
                                public void onCompleted() {
                                    if (logisticDonePublisher != null) {
                                        logisticDonePublisher.onCompleted();
                                    }
                                    if (logisticPromoDonePublisher != null) {
                                        logisticPromoDonePublisher.onCompleted();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Timber.d(e);
                                    if (getView() != null) {
                                        getView().setStateLoadingCourierStateAtIndex(cartPosition, false);
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
                                    if (getView() != null) {
                                        getView().setStateLoadingCourierStateAtIndex(cartPosition, false);
                                        ShipmentPresenter.this.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel;
                                        updateTickerAnnouncementData(validateUsePromoRevampUiModel);
                                        showErrorValidateUseIfAny(validateUsePromoRevampUiModel);
                                        validateBBOWithSpecificOrder(validateUsePromoRevampUiModel, cartString, promoCode);
                                        boolean isValidatePromoRevampSuccess = validateUsePromoRevampUiModel.getStatus().equalsIgnoreCase(statusOK) && validateUsePromoRevampUiModel.getErrorCode().equals(statusCode200);
                                        if (isValidatePromoRevampSuccess) {
                                            getView().updateButtonPromoCheckout(validateUsePromoRevampUiModel.getPromoUiModel(), true);
                                        } else {
                                            if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                                                String errMessage = validateUsePromoRevampUiModel.getMessage().get(0);
                                                mTrackerShipment.eventClickLanjutkanTerapkanPromoError(errMessage);
                                                PromoRevampAnalytics.INSTANCE.eventCheckoutViewPromoMessage(errMessage);
                                                getView().showToastError(errMessage);
                                                getView().resetCourier(cartPosition);
                                            } else {
                                                getView().showToastError(DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO);
                                                getView().resetCourier(cartPosition);
                                            }
                                        }
                                    }
                                }
                            }));
        }
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
            getView().showToastNormal(messageInfo);
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

    private void validateBBOWithSpecificOrder(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel, String cartString, String promoCode) {
        boolean orderFound = false;
        for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
            if (voucherOrdersItemUiModel.getUniqueId().equals(cartString) && voucherOrdersItemUiModel.getCode().equalsIgnoreCase(promoCode)) {
                orderFound = true;
            }
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
        if (!orderFound && shipmentCartItemModelList != null) {
            // if not voucher order found for attempted apply BO order,
            // then should reset courier and not apply the BO
            // this should be a rare case
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel.getCartString().equals(cartString)) {
                    if (getView() != null) {
                        getView().resetCourier(shipmentCartItemModel);
                        break;
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
        requestParams.putObject(OldValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest);

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
                                    boolean isValidatePromoRevampSuccess = validateUsePromoRevampUiModel.getStatus().equalsIgnoreCase(statusOK) && validateUsePromoRevampUiModel.getErrorCode().equals(statusCode200);
                                    if (isValidatePromoRevampSuccess) {
                                        getView().renderPromoCheckoutFromCourierSuccess(validateUsePromoRevampUiModel, itemPosition, noToast);
                                    } else {
                                        if (validateUsePromoRevampUiModel.getMessage().size() > 0) {
                                            String errMessage = validateUsePromoRevampUiModel.getMessage().get(0);
                                            getView().renderErrorCheckPromoShipmentData(errMessage);
                                        } else {
                                            getView().renderErrorCheckPromoShipmentData(DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO);
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
                                                   String leasingId,
                                                   ArrayList<String> prescriptionsIds) {
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
                    Utils.toIntOrZero(getRecipientAddressModel().getCornerId())
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
                    crossSellItemRequestModel.setPrice(shipmentCrossSellModel.getCrossSellModel().getPrice());
                    crossSellItemRequestModel.setAdditionalVerticalId(Utils.toIntOrZero(shipmentCrossSellModel.getCrossSellModel().getAdditionalVerticalId()));
                    crossSellItemRequestModel.setTransactionType(shipmentCrossSellModel.getCrossSellModel().getTransactionType());
                    listCrossSellItemRequest.add(crossSellItemRequestModel);
                }
            }
        }
        if (shipmentNewUpsellModel.isSelected() && shipmentNewUpsellModel.isShow()) {
            CrossSellItemRequestModel crossSellItemRequestModel = new CrossSellItemRequestModel();
            crossSellItemRequestModel.setId(Utils.toIntOrZero(shipmentNewUpsellModel.getId()));
            crossSellItemRequestModel.setPrice(shipmentNewUpsellModel.getPrice());
            crossSellItemRequestModel.setAdditionalVerticalId(Utils.toIntOrZero(shipmentNewUpsellModel.getAdditionalVerticalId()));
            crossSellItemRequestModel.setTransactionType(shipmentNewUpsellModel.getTransactionType());
            listCrossSellItemRequest.add(crossSellItemRequestModel);
        }
        crossSellRequest.setListItem(listCrossSellItemRequest);
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
            checkoutRequest.setLeasingId(Utils.toIntOrZero(leasingId));
        }

        if (prescriptionsIds != null && !prescriptionsIds.isEmpty()) {
            checkoutRequest.setPrescriptionIds(prescriptionsIds);
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
        shipmentStateRequestData.setAddressId(recipientAddressModel.getId());
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
    public void cancelAutoApplyPromoStackLogistic(int itemPosition, String promoCode, ShipmentCartItemModel shipmentCartItemModel) {
        setCouponStateChanged(true);
        ArrayList<String> promoCodeList = new ArrayList<>();
        promoCodeList.add(promoCode);
        ArrayList<ClearPromoOrder> clearOrders = new ArrayList<>();
        clearOrders.add(new ClearPromoOrder(shipmentCartItemModel.getCartString(),
                shipmentCartItemModel.getShipmentCartData().getBoMetadata().getBoType(),
                promoCodeList, shipmentCartItemModel.getShopId(), shipmentCartItemModel.isProductIsPreorder(),
                String.valueOf(shipmentCartItemModel.getCartItemModels().get(0).getPreOrderDurationDay()),
                shipmentCartItemModel.getFulfillmentId())
        );

        clearCacheAutoApplyStackUseCase.setParams(new ClearPromoRequest(OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, false, new ClearPromoOrderData(new ArrayList<>(), clearOrders)));
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
                                tickerAnnouncementHolderData.setTitle("");
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

    @Nullable
    public ClearPromoOrder getClearPromoOrderByUniqueId(ArrayList<ClearPromoOrder> list, String uniqueId) {
        for (ClearPromoOrder clearPromoOrder : list) {
            if (clearPromoOrder.getUniqueId().equals(uniqueId)) {
                return clearPromoOrder;
            }
        }
        return null;
    }

    // Clear promo red state before checkout
    @Override
    public void cancelNotEligiblePromo(ArrayList<NotEligiblePromoHolderdata> notEligiblePromoHolderdataArrayList) {
        setCouponStateChanged(true);
        boolean hasPromo = false;
        ArrayList<String> globalPromoCodes = new ArrayList<>();
        ArrayList<ClearPromoOrder> clearOrders = new ArrayList<>();
        for (NotEligiblePromoHolderdata notEligiblePromoHolderdata : notEligiblePromoHolderdataArrayList) {
            if (notEligiblePromoHolderdata.getIconType() == NotEligiblePromoHolderdata.getTYPE_ICON_GLOBAL()) {
                globalPromoCodes.add(notEligiblePromoHolderdata.getPromoCode());
                hasPromo = true;
            } else {
                ClearPromoOrder clearOrder = getClearPromoOrderByUniqueId(clearOrders, notEligiblePromoHolderdata.getUniqueId());
                if (clearOrder != null) {
                    clearOrder.getCodes().add(notEligiblePromoHolderdata.getPromoCode());
                    hasPromo = true;
                } else if (shipmentCartItemModelList != null && !shipmentCartItemModelList.isEmpty()) {
                    for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                        if (shipmentCartItemModel.getCartString().equals(notEligiblePromoHolderdata.getUniqueId())) {
                            ArrayList<String> codes = new ArrayList<>();
                            codes.add(notEligiblePromoHolderdata.getPromoCode());
                            clearOrders.add(new ClearPromoOrder(
                                    notEligiblePromoHolderdata.getUniqueId(),
                                    shipmentCartItemModel.getShipmentCartData().getBoMetadata().getBoType(),
                                    codes, shipmentCartItemModel.getShopId(), shipmentCartItemModel.isProductIsPreorder(),
                                    String.valueOf(shipmentCartItemModel.getCartItemModels().get(0).getPreOrderDurationDay()),
                                    shipmentCartItemModel.getFulfillmentId()
                            ));
                            hasPromo = true;
                            break;
                        }
                    }
                }
            }
        }

        if (hasPromo) {
            clearCacheAutoApplyStackUseCase.setParams(new ClearPromoRequest(OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, false, new ClearPromoOrderData(globalPromoCodes, clearOrders)));
            compositeSubscription.add(
                    clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                            .subscribe(new ClearNotEligiblePromoSubscriber(getView(), this, notEligiblePromoHolderdataArrayList))
            );
        }
    }

    // Clear promo after clash (rare, almost zero probability)
    @Override
    public void cancelAutoApplyPromoStackAfterClash(ClashingInfoDetailUiModel clashingInfoDetailUiModel) {
        ArrayList<String> globalPromoCode = new ArrayList<>();
        ArrayList<ClearPromoOrder> clearOrders = new ArrayList<>();
        for (PromoClashOptionUiModel promoClashOptionUiModel : clashingInfoDetailUiModel.getOptions()) {
            if (promoClashOptionUiModel != null && promoClashOptionUiModel.getVoucherOrders() != null) {
                for (PromoClashVoucherOrdersUiModel promoClashVoucherOrdersUiModel : promoClashOptionUiModel.getVoucherOrders()) {
                    if (promoClashVoucherOrdersUiModel.getUniqueId().isEmpty()) {
                        if (!globalPromoCode.contains(promoClashVoucherOrdersUiModel.getCode())) {
                            globalPromoCode.add(promoClashVoucherOrdersUiModel.getCode());
                        }
                    } else {
                        ClearPromoOrder order = getClearPromoOrderByUniqueId(clearOrders, promoClashVoucherOrdersUiModel.getUniqueId());
                        if (order != null) {
                            if (!order.getCodes().contains(promoClashVoucherOrdersUiModel.getCode())) {
                                order.getCodes().add(promoClashVoucherOrdersUiModel.getCode());
                            }
                        } else {
                            ShipmentCartItemModel cartItemModel = null;
                            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                                if (shipmentCartItemModel.getCartString().equals(promoClashVoucherOrdersUiModel.getUniqueId())) {
                                    cartItemModel = shipmentCartItemModel;
                                    break;
                                }
                            }
                            if (cartItemModel != null) {
                                ArrayList<String> codes = new ArrayList<>();
                                codes.add(promoClashVoucherOrdersUiModel.getCode());
                                clearOrders.add(new ClearPromoOrder(
                                        promoClashVoucherOrdersUiModel.getUniqueId(),
                                        cartItemModel.getShipmentCartData().getBoMetadata().getBoType(),
                                        codes, cartItemModel.getShopId(), cartItemModel.isProductIsPreorder(),
                                        String.valueOf(cartItemModel.getCartItemModels().get(0).getPreOrderDurationDay()),
                                        cartItemModel.getFulfillmentId()
                                ));
                            }
                        }
                    }
                }
            }
        }
        setCouponStateChanged(true);
        getView().showLoading();
        getView().setHasRunningApiCall(true);
        clearCacheAutoApplyStackUseCase.setParams(new ClearPromoRequest(OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, false, new ClearPromoOrderData(globalPromoCode, clearOrders)));
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe(
                        new ClearShipmentCacheAutoApplyAfterClashSubscriber(getView(), this)
                )
        );
    }

    @Override
    public void hitClearAllBo() {
        ArrayList<ClearPromoOrder> clearOrders = new ArrayList<>();
        boolean hasBo = false;
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            if (shipmentCartItemModel != null && shipmentCartItemModel.getShipmentCartData() != null && shipmentCartItemModel.getVoucherLogisticItemUiModel() != null && !shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode().isEmpty()) {
                ArrayList<String> boCodes = new ArrayList<>();
                boCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                clearOrders.add(new ClearPromoOrder(
                        shipmentCartItemModel.getCartString(),
                        shipmentCartItemModel.getShipmentCartData().getBoMetadata().getBoType(),
                        boCodes, shipmentCartItemModel.getShopId(), shipmentCartItemModel.isProductIsPreorder(),
                        String.valueOf(shipmentCartItemModel.getCartItemModels().get(0).getPreOrderDurationDay()),
                        shipmentCartItemModel.getFulfillmentId()
                ));
                hasBo = true;
            }
        }
        if (hasBo) {
            clearCacheAutoApplyStackUseCase.setParams(new ClearPromoRequest(OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, false, new ClearPromoOrderData(new ArrayList<>(), clearOrders)));
            compositeSubscription.add(
                    clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe()
            );
        }
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
                                        if (setShippingAddressData.getMessages().isEmpty()) {
                                            getView().showToastNormal(getView().getActivityContext().getString(R.string.label_change_address_success));
                                        } else {
                                            getView().showToastNormal(setShippingAddressData.getMessages().get(0));
                                        }
                                        hitClearAllBo();
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
                .cartData(cartData)
                .mvc("");

        if (!skipMvc) {
            ratesParamBuilder.mvc(mvc);
        }

        RatesParam param = ratesParamBuilder.build();

        Observable<ShippingRecommendationData> observable;
        if (isTradeInDropOff) {
            observable = ratesApiUseCase.execute(param);
            compositeSubscription.add(
                    observable
                            .map(shippingRecommendationData ->
                                    stateConverter.fillState(shippingRecommendationData, shopShipmentList,
                                            spId, 0))
                            .subscribe(
                                    new GetCourierRecommendationSubscriber(
                                            getView(), this, shipperId, spId, itemPosition,
                                            shippingCourierConverter, shipmentCartItemModel,
                                            isInitialLoad, isTradeInDropOff, isForceReload, isBoUnstackEnabled, null
                                    ))
            );
        } else {
            if (ratesPublisher == null) {
                logisticDonePublisher = PublishSubject.create();
                ratesPublisher = PublishSubject.create();
                compositeSubscription.add(
                        ratesPublisher
                                .concatMap(shipmentGetCourierHolderData -> {
                                    ratesUseCase.execute(shipmentGetCourierHolderData.getRatesParam())
                                            .map(shippingRecommendationData ->
                                                    stateConverter.fillState(shippingRecommendationData, shipmentGetCourierHolderData.getShopShipmentList(),
                                                            shipmentGetCourierHolderData.getSpId(), 0)
                                            ).subscribe(
                                                    new GetCourierRecommendationSubscriber(
                                                            getView(), this, shipmentGetCourierHolderData.getShipperId(), shipmentGetCourierHolderData.getSpId(), shipmentGetCourierHolderData.getItemPosition(),
                                                            shippingCourierConverter, shipmentGetCourierHolderData.getShipmentCartItemModel(),
                                                            shipmentGetCourierHolderData.isInitialLoad(), shipmentGetCourierHolderData.isTradeInDropOff(), shipmentGetCourierHolderData.isForceReload(), isBoUnstackEnabled,
                                                            logisticDonePublisher
                                                    ));
                                    return logisticDonePublisher;
                                })
                                .subscribe()
                );
            }
            ratesPublisher.onNext(new ShipmentGetCourierHolderData(
                    shipperId,
                    spId,
                    itemPosition,
                    shipmentCartItemModel,
                    shopShipmentList,
                    isInitialLoad,
                    "",
                    isTradeInDropOff,
                    isForceReload,
                    param,
                    ""
            ));
        }
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

    @Override
    public String getCartDataForRates() {
        return cartData;
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

    @Override
    public void releaseBooking() {
        // As deals product is using OCS, the shipment should only contain 1 product
        long productId = ShipmentCartItemModelHelper.getFirstProductId(shipmentCartItemModelList);
        if (productId != 0) {
            releaseBookingUseCase
                    .execute(productId)
                    .subscribe(new ReleaseBookingStockSubscriber());
        }
    }

    @Override
    public void fetchPrescriptionIds(boolean isUploadPrescriptionNeeded, String checkoutId) {
        if (!checkoutId.isEmpty() && isUploadPrescriptionNeeded) {
            compositeSubscription.add(prescriptionIdsUseCase
                    .execute(checkoutId)
                    .subscribe(new Subscriber<GetPrescriptionIdsResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d(e);
                        }

                        @Override
                        public void onNext(GetPrescriptionIdsResponse getPrescriptionIdsResponse) {
                            if (getPrescriptionIdsResponse.getDetailData() != null &&
                                    getPrescriptionIdsResponse.getDetailData().getPrescriptionData() != null &&
                                    getPrescriptionIdsResponse.getDetailData().getPrescriptionData().getPrescriptions() != null) {
                                getView().updatePrescriptionIds(getPrescriptionIdsResponse.getDetailData().getPrescriptionData().getPrescriptions());
                            }
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
    public void setUploadPrescriptionData(UploadPrescriptionUiModel uploadPrescriptionUiModel) {
        this.uploadPrescriptionUiModel = uploadPrescriptionUiModel;
    }

    @Override
    public UploadPrescriptionUiModel getUploadPrescriptionUiModel() {
        return uploadPrescriptionUiModel;
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

    @Override
    public void updateAddOnProductLevelDataBottomSheet(SaveAddOnStateResult saveAddOnStateResult) {
        for (AddOnResult addOnResult : saveAddOnStateResult.getAddOns()) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                List<CartItemModel> cartItemModelList = shipmentCartItemModel.getCartItemModels();
                for (int i = 0; i < cartItemModelList.size(); i++) {
                    CartItemModel cartItemModel = cartItemModelList.get(i);
                    String keyProductLevel = cartItemModel.getCartString() + "-" + cartItemModel.getCartId();
                    if (keyProductLevel.equalsIgnoreCase(addOnResult.getAddOnKey())) {
                        AddOnsDataModel addOnsDataModel = cartItemModel.getAddOnProductLevelModel();
                        setAddOnsData(addOnsDataModel, addOnResult, 0, cartItemModel.getCartString(), cartItemModel.getCartId());
                    }
                }
            }
        }
    }

    @Override
    public void updateAddOnOrderLevelDataBottomSheet(SaveAddOnStateResult saveAddOnStateResult) {
        for (AddOnResult addOnResult : saveAddOnStateResult.getAddOns()) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if ((shipmentCartItemModel.getCartString() + "-0").equalsIgnoreCase(addOnResult.getAddOnKey()) && shipmentCartItemModel.getAddOnsOrderLevelModel() != null) {
                    AddOnsDataModel addOnsDataModel = shipmentCartItemModel.getAddOnsOrderLevelModel();
                    setAddOnsData(addOnsDataModel, addOnResult, 1, shipmentCartItemModel.getCartString(), 0L);
                }
            }
        }
    }

    // identifier : 0 = product level, 1  = order level
    private void setAddOnsData(AddOnsDataModel addOnsDataModel, AddOnResult addOnResult, int identifier, String cartString, long cartId) {
        addOnsDataModel.setStatus(addOnResult.getStatus());

        AddOnButtonResult addOnButtonResult = addOnResult.getAddOnButton();
        AddOnButtonModel addOnButtonModel = new AddOnButtonModel();
        addOnButtonModel.setAction(addOnButtonResult.getAction());
        addOnButtonModel.setDescription(addOnButtonResult.getDescription());
        addOnButtonModel.setTitle(addOnButtonResult.getTitle());
        addOnButtonModel.setLeftIconUrl(addOnButtonResult.getLeftIconUrl());
        addOnButtonModel.setRightIconUrl(addOnButtonResult.getRightIconUrl());
        addOnsDataModel.setAddOnsButtonModel(addOnButtonModel);

        AddOnBottomSheetResult addOnBottomSheetResult = addOnResult.getAddOnBottomSheet();
        AddOnBottomSheetModel addOnBottomSheetModel = new AddOnBottomSheetModel();
        addOnBottomSheetModel.setHeaderTitle(addOnBottomSheetResult.getHeaderTitle());
        addOnBottomSheetModel.setDescription(addOnBottomSheetResult.getDescription());

        AddOnTickerModel addOnTickerModel = new AddOnTickerModel();
        addOnTickerModel.setText(addOnBottomSheetResult.getTicker().getText());
        addOnBottomSheetModel.setTicker(addOnTickerModel);

        ArrayList<AddOnProductItemModel> listProductAddOn = new ArrayList<>();
        for (ProductResult productResult : addOnBottomSheetResult.getProducts()) {
            AddOnProductItemModel addOnProductItemModel = new AddOnProductItemModel();
            addOnProductItemModel.setProductName(productResult.getProductName());
            addOnProductItemModel.setProductImageUrl(productResult.getProductImageUrl());
            listProductAddOn.add(addOnProductItemModel);
        }
        addOnBottomSheetModel.setProducts(listProductAddOn);
        addOnsDataModel.setAddOnsBottomSheetModel(addOnBottomSheetModel);

        ArrayList<AddOnDataItemModel> listAddOnDataItem = new ArrayList<>();
        for (AddOnData addOnData : addOnResult.getAddOnData()) {
            AddOnDataItemModel addOnDataItemModel = new AddOnDataItemModel();
            addOnDataItemModel.setAddOnId(addOnData.getAddOnId());
            addOnDataItemModel.setAddOnPrice(addOnData.getAddOnPrice());
            addOnDataItemModel.setAddOnQty(addOnData.getAddOnQty());

            AddOnMetadata addOnMetadata = addOnData.getAddOnMetadata();
            AddOnNote addOnNote = addOnMetadata.getAddOnNote();
            AddOnMetadataItemModel addOnMetadataItemModel = new AddOnMetadataItemModel();
            AddOnNoteItemModel addOnNoteItemModel = new AddOnNoteItemModel();
            addOnNoteItemModel.setCustomNote(addOnNote.isCustomNote());
            addOnNoteItemModel.setNotes(addOnNote.getNotes());
            addOnNoteItemModel.setFrom(addOnNote.getFrom());
            addOnNoteItemModel.setTo(addOnNote.getTo());
            addOnMetadataItemModel.setAddOnNoteItemModel(addOnNoteItemModel);
            addOnDataItemModel.setAddOnMetadata(addOnMetadataItemModel);
            listAddOnDataItem.add(addOnDataItemModel);
        }
        addOnsDataModel.setAddOnsDataItemModelList(listAddOnDataItem);
        getView().updateAddOnsData(addOnsDataModel, identifier);
        if (isUsingDynamicDataPassing) {
            getView().updateAddOnsDynamicDataPassing(addOnsDataModel, addOnResult, identifier, cartString, cartId);
        }
    }

    @Override
    public ShipmentUpsellModel getShipmentUpsellModel() {
        return shipmentUpsellModel;
    }

    @Override
    public ShipmentNewUpsellModel getShipmentNewUpsellModel() {
        return shipmentNewUpsellModel;
    }

    @Override
    public Pair<ArrayList<String>, ArrayList<String>> validateBoPromo(ValidateUsePromoRevampUiModel validateUsePromoRevampUiModel) {
        final ArrayList<String> unappliedBoPromoUniqueIds = new ArrayList<>();
        final ArrayList<String> reloadedUniqueIds = new ArrayList<>();
        final ArrayList<String> unprocessedUniqueIds = new ArrayList<>();
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            unprocessedUniqueIds.add(shipmentCartItemModel.getCartString());
        }
        // loop to list voucher orders to be applied this will be used later
        final List<PromoCheckoutVoucherOrdersItemUiModel> toBeAppliedVoucherOrders = new ArrayList<>();
        for (PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel : validateUsePromoRevampUiModel.getPromoUiModel().getVoucherOrderUiModels()) {
            // voucher with shippingId not zero, spId not zero, and voucher type logistic as promo for BO
            if (voucherOrdersItemUiModel.getShippingId() > 0
                    && voucherOrdersItemUiModel.getSpId() > 0
                    && voucherOrdersItemUiModel.getType().equals("logistic")) {
                if (voucherOrdersItemUiModel.getMessageUiModel().getState().equals("green")) {
                    toBeAppliedVoucherOrders.add(voucherOrdersItemUiModel);
                    unprocessedUniqueIds.remove(voucherOrdersItemUiModel.getUniqueId());
                }
            }
        }
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null
                    && unprocessedUniqueIds.contains(shipmentCartItemModel.getCartString())) {
                doUnapplyBo(shipmentCartItemModel.getCartString(), shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                unappliedBoPromoUniqueIds.add(shipmentCartItemModel.getCartString());
                reloadedUniqueIds.add(shipmentCartItemModel.getCartString());
            }
        }
        if (unappliedBoPromoUniqueIds.size() > 0) {
            getView().renderUnapplyBoIncompleteShipment(unappliedBoPromoUniqueIds);
        }
        for (PromoCheckoutVoucherOrdersItemUiModel voucherOrders : toBeAppliedVoucherOrders) {
            doApplyBo(voucherOrders);
            reloadedUniqueIds.add(voucherOrders.getUniqueId());
        }
        return new Pair<>(reloadedUniqueIds, unappliedBoPromoUniqueIds);
    }

    @Override
    public void doUnapplyBo(String uniqueId, String promoCode) {
        final int itemAdapterPosition = getView().getShipmentCartItemModelAdapterPositionByUniqueId(uniqueId);
        final ShipmentCartItemModel shipmentCartItemModel = getView().getShipmentCartItemModel(itemAdapterPosition);
        if (shipmentCartItemModel != null && itemAdapterPosition != -1) {
            getView().resetCourier(itemAdapterPosition);
            clearCacheAutoApply(shipmentCartItemModel, promoCode);
            clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode);
            getView().onNeedUpdateViewItem(itemAdapterPosition);
        }
    }

    private void clearCacheAutoApply(ShipmentCartItemModel shipmentCartItemModel, String promoCode) {
        final List<String> globalCodes = new ArrayList<>();
        final List<ClearPromoOrder> clearOrders = new ArrayList<>();
        final List<String> promoCodes = new ArrayList<>();
        promoCodes.add(promoCode);
        clearOrders.add(new ClearPromoOrder(shipmentCartItemModel.getCartString(),
                shipmentCartItemModel.getShipmentCartData().getBoMetadata().getBoType(), promoCodes,
                shipmentCartItemModel.getShopId(), shipmentCartItemModel.isProductIsPreorder(),
                String.valueOf(shipmentCartItemModel.getCartItemModels().get(0).getPreOrderDurationDay()),
                shipmentCartItemModel.getFulfillmentId()));
        final ClearPromoRequest params = new ClearPromoRequest(OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                false, new ClearPromoOrderData(globalCodes, clearOrders));
        clearCacheAutoApplyStackUseCase.setParams(params);
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe()
        );
    }

    @Override
    public void clearOrderPromoCodeFromLastValidateUseRequest(String uniqueId, String promoCode) {
        if (lastValidateUsePromoRequest != null) {
            for (OrdersItem order : lastValidateUsePromoRequest.getOrders()) {
                if (order.getUniqueId().equals(uniqueId)) {
                    order.getCodes().remove(promoCode);
                }
            }
        }
        if (lastApplyData != null) {
            for (LastApplyVoucherOrdersItemUiModel voucherOrder : lastApplyData.getVoucherOrders()) {
                if (voucherOrder.getUniqueId().equals(uniqueId) && voucherOrder.getCode().equals(promoCode)) {
                    lastApplyData.getVoucherOrders().remove(voucherOrder);
                    break;
                }
            }
        }
    }

    @Override
    public void doApplyBo(PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel) {
        final int itemAdapterPosition = getView().getShipmentCartItemModelAdapterPositionByUniqueId(voucherOrdersItemUiModel.getUniqueId());
        final ShipmentCartItemModel shipmentCartItemModel = getView().getShipmentCartItemModel(itemAdapterPosition);
        if (shipmentCartItemModel != null && itemAdapterPosition != -1) {
            if (shipmentCartItemModel.getVoucherLogisticItemUiModel() == null ||
                    !shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode().equals(voucherOrdersItemUiModel.getCode())) {
                processBoPromoCourierRecommendation(itemAdapterPosition, voucherOrdersItemUiModel, shipmentCartItemModel);
            }
        }
    }

    @Override
    public void processBoPromoCourierRecommendation(int itemPosition, PromoCheckoutVoucherOrdersItemUiModel voucherOrdersItemUiModel, ShipmentCartItemModel shipmentCartItemModel) {
        ShipmentDetailData selectedShipmentDetailData = getView().getShipmentDetailData(shipmentCartItemModel, recipientAddressModel);
        List<Product> products = getProductForRatesRequest(shipmentCartItemModel);
        String cartString = shipmentCartItemModel.getCartString() != null ? shipmentCartItemModel.getCartString() : "";
        boolean isTradeInDropOff = getView().isTradeInByDropOff();
        ShippingParam shippingParam = getShippingParam(selectedShipmentDetailData, products, cartString, isTradeInDropOff, recipientAddressModel);

        int counter = codData == null ? -1 : codData.getCounterCod();
        boolean cornerId = false;
        if (getRecipientAddressModel() != null) {
            cornerId = getRecipientAddressModel().isCornerAddress();
        }
        String pslCode = voucherOrdersItemUiModel.getCode();
        boolean isLeasing = shipmentCartItemModel.isLeasingProduct();

        String mvc = generateRatesMvcParam(cartString);

        List<ShopShipment> shopShipmentList = shipmentCartItemModel.getShopShipmentList();
        RatesParam.Builder ratesParamBuilder = new RatesParam.Builder(shopShipmentList, shippingParam)
                .isCorner(cornerId)
                .codHistory(counter)
                .isLeasing(isLeasing)
                .promoCode(pslCode)
                .cartData(cartData)
                .mvc(mvc);

        RatesParam param = ratesParamBuilder.build();

        Observable<ShippingRecommendationData> observable;
        String promoCode = voucherOrdersItemUiModel.getCode();
        int shippingId = voucherOrdersItemUiModel.getShippingId();
        int spId = voucherOrdersItemUiModel.getSpId();
        getView().setStateLoadingCourierStateAtIndex(itemPosition, true);
        if (isTradeInDropOff) {
            observable = ratesApiUseCase.execute(param);
            compositeSubscription.add(
                    observable
                            .map(shippingRecommendationData ->
                                    stateConverter.fillState(shippingRecommendationData, shopShipmentList,
                                            spId, 0))
                            .subscribe(
                                    new GetBoPromoCourierRecommendationSubscriber(
                                            getView(), this, cartString, promoCode, shippingId, spId,
                                            itemPosition, shippingCourierConverter, shipmentCartItemModel,
                                            isTradeInDropOff, false, null
                                    )));
        } else {
            if (ratesPromoPublisher == null) {
                logisticPromoDonePublisher = PublishSubject.create();
                ratesPromoPublisher = PublishSubject.create();
                compositeSubscription.add(
                        ratesPromoPublisher
                                .concatMap(shipmentGetCourierHolderData -> {
                                    ratesUseCase.execute(shipmentGetCourierHolderData.getRatesParam())
                                            .map(shippingRecommendationData ->
                                                    stateConverter.fillState(shippingRecommendationData, shipmentGetCourierHolderData.getShopShipmentList(),
                                                            shipmentGetCourierHolderData.getSpId(), 0)
                                            ).subscribe(
                                                    new GetBoPromoCourierRecommendationSubscriber(
                                                            getView(), this, shipmentGetCourierHolderData.getCartString(), shipmentGetCourierHolderData.getPromoCode(), shipmentGetCourierHolderData.getShipperId(), shipmentGetCourierHolderData.getSpId(),
                                                            shipmentGetCourierHolderData.getItemPosition(), shippingCourierConverter, shipmentGetCourierHolderData.getShipmentCartItemModel(),
                                                            shipmentGetCourierHolderData.isTradeInDropOff(), false, logisticPromoDonePublisher
                                                    ));
                                    return logisticPromoDonePublisher;
                                })
                                .subscribe()
                );
            }
            ratesPromoPublisher.onNext(new ShipmentGetCourierHolderData(
                    shippingId,
                    spId,
                    itemPosition,
                    shipmentCartItemModel,
                    shopShipmentList,
                    false,
                    cartString,
                    isTradeInDropOff,
                    false,
                    param,
                    promoCode
            ));
        }
    }

    @Override
    public List<Product> getProductForRatesRequest(ShipmentCartItemModel shipmentCartItemModel) {
        ArrayList<Product> products = new ArrayList<>();
        if (shipmentCartItemModel != null) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                if (!cartItemModel.isError()) {
                    Product product = new Product();
                    product.setProductId(cartItemModel.getProductId());
                    product.setFreeShipping(cartItemModel.isFreeShipping());
                    product.setFreeShippingTc(cartItemModel.isFreeShippingExtra());

                    products.add(product);
                }
            }
        }

        return products;
    }

    @Override
    public void validateClearAllBoPromo() {
        if (shipmentCartItemModelList != null && lastValidateUsePromoRequest != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                for (OrdersItem ordersItem : lastValidateUsePromoRequest.getOrders()) {
                    if (ordersItem.getUniqueId().equals(shipmentCartItemModel.getCartString())
                            && ordersItem.getCodes().isEmpty()
                            && shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                        doUnapplyBo(shipmentCartItemModel.getCartString(),
                                shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                    }
                }
            }
        }
    }

    @Override
    public void cancelUpsell(boolean isReloadData, boolean isOneClickShipment,
                             boolean isTradeIn, boolean skipUpdateOnboardingState,
                             boolean isReloadAfterPriceChangeHinger,
                             String cornerId, String deviceId, String leasingId, boolean isPlusSelected) {
        hitClearAllBo();
        processInitialLoadCheckoutPage(isReloadData, isOneClickShipment, isTradeIn,
                skipUpdateOnboardingState, isReloadAfterPriceChangeHinger, cornerId,
                deviceId, leasingId, isPlusSelected);
    }

    @Override
    public void clearAllBoOnTemporaryUpsell() {
        if (shipmentNewUpsellModel.isShow() && shipmentNewUpsellModel.isSelected()) {
            ArrayList<ClearPromoOrder> clearOrders = new ArrayList<>();
            boolean hasBo = false;
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel != null && shipmentCartItemModel.getShipmentCartData() != null && shipmentCartItemModel.getVoucherLogisticItemUiModel() != null && !shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode().isEmpty()) {
                    ArrayList<String> boCodes = new ArrayList<>();
                    boCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                    clearOrders.add(new ClearPromoOrder(
                            shipmentCartItemModel.getCartString(),
                            shipmentCartItemModel.getShipmentCartData().getBoMetadata().getBoType(),
                            boCodes, shipmentCartItemModel.getShopId(), shipmentCartItemModel.isProductIsPreorder(),
                            String.valueOf(shipmentCartItemModel.getCartItemModels().get(0).getPreOrderDurationDay()), shipmentCartItemModel.getFulfillmentId()
                    ));
                    hasBo = true;
                }
            }
            if (hasBo) {
                clearCacheAutoApplyStackUseCase.setParams(new ClearPromoRequest(OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, false, new ClearPromoOrderData(new ArrayList<>(), clearOrders)));
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe();
            }
        }
    }

    public void setCurrentDynamicDataParamFromSAF(CartShipmentAddressFormData cartShipmentAddressFormData, boolean isOneClickShipment) {
        DynamicDataPassingParamRequest ddpParam = new DynamicDataPassingParamRequest();
        ArrayList<DynamicDataPassingParamRequest.DynamicDataParam> listDataParam = new ArrayList<DynamicDataPassingParamRequest.DynamicDataParam>();
        // donation
        if (cartShipmentAddressFormData.getDonation() != null && cartShipmentAddressFormData.getDonation().isChecked()) {
            DynamicDataPassingParamRequest.DynamicDataParam dynamicDataParam = new DynamicDataPassingParamRequest.DynamicDataParam();
            dynamicDataParam.setLevel(DynamicDataPassingMapper.PAYMENT_LEVEL);
            dynamicDataParam.setUniqueId("");
            dynamicDataParam.setAttribute(IS_DONATION);
            dynamicDataParam.setDonation(cartShipmentAddressFormData.getDonation().isChecked());
            listDataParam.add(dynamicDataParam);
        }

        // addons
        for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
            for (GroupShop groupShop : groupAddress.getGroupShop()) {
                // order level
                if (groupShop.getAddOns().getStatus() == 1) {
                    DynamicDataPassingParamRequest.DynamicDataParam dynamicDataParam = new DynamicDataPassingParamRequest.DynamicDataParam();
                    dynamicDataParam.setLevel(ORDER_LEVEL);
                    dynamicDataParam.setUniqueId(groupShop.getCartString());
                    dynamicDataParam.setAttribute(ADD_ON_DETAILS);
                    dynamicDataParam.setAddOn(DynamicDataPassingMapper.INSTANCE.getAddOnFromSAF(groupShop.getAddOns(), isOneClickShipment));
                    listDataParam.add(dynamicDataParam);
                }

                for (com.tokopedia.checkout.domain.model.cartshipmentform.Product product : groupShop.getProducts()) {
                    // product level
                    if (product.getAddOnProduct().getStatus() == 1) {
                        DynamicDataPassingParamRequest.DynamicDataParam dynamicDataParam = new DynamicDataPassingParamRequest.DynamicDataParam();
                        dynamicDataParam.setLevel(PRODUCT_LEVEL);
                        dynamicDataParam.setParentUniqueId(groupShop.getCartString());
                        dynamicDataParam.setUniqueId(String.valueOf(product.getCartId()));
                        dynamicDataParam.setAttribute(ADD_ON_DETAILS);
                        dynamicDataParam.setAddOn(DynamicDataPassingMapper.INSTANCE.getAddOnFromSAF(product.getAddOnProduct(), isOneClickShipment));
                        listDataParam.add(dynamicDataParam);
                    }
                }
            }
        }

        ddpParam.setData(listDataParam);
        this.dynamicDataParam = ddpParam;
    }

    public void updateDynamicData(DynamicDataPassingParamRequest dynamicDataPassingParamRequest, boolean isFireAndForget) {
        updateDynamicDataPassingUseCase.setParams(dynamicDataPassingParamRequest);
        updateDynamicDataPassingUseCase.execute(
                dynamicDataPassingUiModel -> {
                    if (getView() != null) {
                        getView().stopEmbraceTrace();
                        getView().stopTrace();
                        if (!isFireAndForget) {
                            getView().doCheckout();
                        } else {
                            this.dynamicData = dynamicDataPassingUiModel.getDynamicData();
                        }
                    }
                    return Unit.INSTANCE;
                }, throwable -> {
                    Timber.d(throwable);
                    if (getView() != null) {
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

    @Override
    public void setDynamicData(DynamicDataPassingParamRequest.DynamicDataParam dynamicDataParam, boolean isChecked) {
        if (this.dynamicDataParam.getData().contains(dynamicDataParam)) {
            if (!isChecked) {
                this.dynamicDataParam.getData().remove(dynamicDataParam);
            }
        } else {
            this.dynamicDataParam.getData().add(dynamicDataParam);
        }
        updateDynamicData(this.dynamicDataParam, true);
    }

    @Override
    public void validateDynamicData() {
        updateDynamicData(this.dynamicDataParam, false);
    }

    @Override
    public boolean isUsingDynamicDataPassing() {
        return isUsingDynamicDataPassing;
    }

    @Override
    public PublishSubject<Boolean> getLogisticDonePublisher() {
        return logisticDonePublisher;
    }
}
