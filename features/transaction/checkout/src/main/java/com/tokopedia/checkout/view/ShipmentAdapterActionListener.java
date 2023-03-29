package com.tokopedia.checkout.view;

import androidx.fragment.app.FragmentManager;

import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest;
import com.tokopedia.checkout.view.uimodel.CrossSellModel;
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel;
import com.tokopedia.checkout.view.uimodel.ShipmentPlatformFeeModel;
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;

import java.util.List;

import rx.subjects.PublishSubject;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public interface ShipmentAdapterActionListener {

    void onCancelVoucherLogisticClicked(String pslCode, int position, ShipmentCartItemModel shipmentCartItemModel);

    void onDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onDataDisableToCheckout(String message);

    void onCheckoutValidationResult(boolean result, Object shipmentData, int position, boolean epharmacyError);

    void onChangeAddress();

    void onTotalPaymentChange(String totalPayment, boolean enable);

    void onFinishChoosingShipment(int lastSelectedCourierOrder, String lastSelectedCourierOrdercartString, boolean forceHitValidateUse, boolean skipValidateUse);

    void updateCheckoutRequest(List<DataCheckoutRequest> checkoutRequestData);

    void onInsuranceChecked(int position);

    void onPriorityChecked(int position);

    void onNeedUpdateViewItem(int position);

    void onSubTotalItemClicked(int position);

    void onInsuranceTncClicked();

    void onPriorityTncClicker();

    void onOntimeDeliveryClicked(String url);

    void onNeedUpdateRequestData();

    void onDropshipCheckedForTrackingAnalytics();

    void onInsuranceCheckedForTrackingAnalytics();

    void onDonationChecked(boolean checked);

    void onCrossSellItemChecked(boolean checked, CrossSellModel crossSellModel, int index);

    void onEgoldChecked(boolean checked);

    void onChangeShippingDuration(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  int position);

    void onChangeShippingCourier(RecipientAddressModel recipientAddressModel,
                                 ShipmentCartItemModel shipmentCartItemModel,
                                 int position,
                                 List<ShippingCourierUiModel> selectedShippingCourierUiModels);

    void hideSoftKeyboard();

    void onLoadShippingState(int shipperId, int spId, int itemPosition,
                             ShipmentDetailData shipmentDetailData,
                             ShipmentCartItemModel shipmentCartItemModel,
                             List<ShopShipment> shopShipmentList,
                             boolean isTradeInDropOff);

    void onCourierPromoCanceled(String shipperName, String promoCode);

    void onPurchaseProtectionLogicError();

    void onPurchaseProtectionChangeListener(int position);

    void navigateToProtectionMore(CartItemModel cartItemModel);

    void onProcessToPayment();

    void onChangeTradeInDropOffClicked(String latitude, String longitude);

    boolean isTradeInByDropOff();

    boolean hasSelectTradeInLocation();

    void onTradeInAddressTabChanged(int addressPosition);

    void onClickPromoCheckout(LastApplyUiModel lastApplyUiModel);

    void onSendAnalyticsClickPromoCheckout(Boolean isApplied, List<String> listAllPromoCodes);

    void onSendAnalyticsViewPromoCheckoutApplied();

    void onCheckShippingCompletionClicked();

    void onShowTickerShippingCompletion();

    void onClickTradeInInfo();

    void onClickSwapInIndomaret();

    void onSwapInUserAddress();

    FragmentManager getCurrentFragmentManager();

    void scrollToPositionWithOffset(int position, float dy);

    void onClickLihatOnTickerOrderError(String shopId, String errorMessage);

    void onClickRefreshErrorLoadCourier();

    void onViewErrorInCourierSection(String errorMessage);

    void onClickSetPinpoint(int position);

    void openAddOnProductLevelBottomSheet(CartItemModel cartItemModel, AddOnWordingModel addOnWordingModel);

    void openAddOnOrderLevelBottomSheet(ShipmentCartItemModel cartItemModel, AddOnWordingModel addOnWordingModel);

    void addOnProductLevelImpression(String productId);

    void addOnOrderLevelImpression(List<CartItemModel> cartItemModelList);

    void onViewUpsellCard(ShipmentUpsellModel shipmentUpsellModel);

    void onClickUpsellCard(ShipmentUpsellModel shipmentUpsellModel);

    void onViewNewUpsellCard(ShipmentNewUpsellModel shipmentUpsellModel);

    void onClickApplyNewUpsellCard(ShipmentNewUpsellModel shipmentUpsellModel);

    void onClickCancelNewUpsellCard(ShipmentNewUpsellModel shipmentUpsellModel);

    void onViewFreeShippingPlusBadge();

    void onInsuranceInfoTooltipClickedTrackingAnalytics();

    void onChangeScheduleDelivery(ScheduleDeliveryUiModel scheduleDeliveryUiModel, int position, PublishSubject<Boolean> donePublisher);

    void checkPlatformFee();

    void refetchPlatformFee();

    void showPlatformFeeTooltipInfoBottomSheet(ShipmentPlatformFeeModel platformFeeModel);
}
