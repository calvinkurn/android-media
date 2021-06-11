package com.tokopedia.checkout.view;

import androidx.fragment.app.FragmentManager;

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.checkout.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;

import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public interface ShipmentAdapterActionListener {

    void onCancelVoucherLogisticClicked(String pslCode, int position);

    void onDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onDataDisableToCheckout(String message);

    void onCheckoutValidationResult(boolean result, Object shipmentData, int position);

    void onChangeAddress();

    void onTotalPaymentChange(String totalPayment);

    void onFinishChoosingShipment(int lastSelectedCourierOrder, String lastSelectedCourierOrdercartString);

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

    void onEgoldChecked(boolean checked);

    void onChangeShippingDuration(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  int position);

    void onChangeShippingCourier(RecipientAddressModel recipientAddressModel,
                                 ShipmentCartItemModel shipmentCartItemModel,
                                 int position);

    void hideSoftKeyboard();

    void onLoadShippingState(int shipperId, int spId, int itemPosition,
                             ShipmentDetailData shipmentDetailData,
                             ShipmentCartItemModel shipmentCartItemModel,
                             List<ShopShipment> shopShipmentList,
                             boolean isTradeInDropOff);

    void onCourierPromoCanceled(String shipperName, String promoCode);

    void onPurchaseProtectionLogicError();

    void onPurchaseProtectionChangeListener(int position);

    void navigateToProtectionMore(String url);

    void onProcessToPayment();

    void onChangeTradeInDropOffClicked();

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

    void onViewTickerProductError(String shopId, String errorMessage);

    void onViewTickerOrderError(String shopId, String errorMessage);

    void onViewTickerPaymentError(String errorMessage);

    void onClickLihatOnTickerOrderError(String shopId, String errorMessage);

    void onClickRefreshErrorLoadCourier();

    void onViewErrorInCourierSection(String errorMessage);
}