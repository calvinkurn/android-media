package com.tokopedia.checkout.view.feature.shipment.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.view.common.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.common.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.checkout.view.common.viewholder.ShipmentSellerCashbackViewHolder;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.ShipmentFragment;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.feature.shipment.util.Utils;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentButtonPaymentViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentDonationViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentEmasViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentInsuranceTncViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentItemViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentNotifierViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentRecipientAddressViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.EgoldTieringModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentInsuranceTncModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentNotifierModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentSellerCashbackModel;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int DEFAULT_ERROR_POSITION = -1;
    public static final int HEADER_POSITION = 0;
    private static final long LAST_THREE_DIGIT_MODULUS = 1000;

    private ArrayList<ShowCaseObject> showCaseObjectList;
    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    private List<Object> shipmentDataList;

    private PromoStackingData promoGlobalStackData;
    private List<PromoStackingData.Builder> listPromoStackingMerchantData;
    private CartPromoSuggestion cartPromoSuggestion;
    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private ShipmentInsuranceTncModel shipmentInsuranceTncModel;
    private ShipmentSellerCashbackModel shipmentSellerCashbackModel;
    private ShipmentDonationModel shipmentDonationModel;
    private EgoldAttributeModel egoldAttributeModel;
    private ShipmentButtonPaymentModel shipmentButtonPaymentModel;

    private ShipmentDataRequestConverter shipmentDataRequestConverter;
    private RatesDataConverter ratesDataConverter;

    private boolean isShowOnboarding;
    private boolean hasShownShowCase;
    private int lastChooseCourierItemPosition;
    private String cartIds;
    private int lastServiceId;
    private String blackboxInfo;

    @Inject
    public ShipmentAdapter(ShipmentAdapterActionListener shipmentAdapterActionListener,
                           ShipmentDataRequestConverter shipmentDataRequestConverter,
                           RatesDataConverter ratesDataConverter) {
        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
        this.shipmentDataRequestConverter = shipmentDataRequestConverter;
        this.ratesDataConverter = ratesDataConverter;
        this.shipmentDataList = new ArrayList<>();
        this.showCaseObjectList = new ArrayList<>();
    }

    public void setCartIds(String cartIds) {
        this.cartIds = cartIds;
    }

    public void setShowOnboarding(boolean showOnboarding) {
        this.isShowOnboarding = showOnboarding;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = shipmentDataList.get(position);

        if (item instanceof ShipmentNotifierModel) {
            return ShipmentNotifierViewHolder.TYPE_VIEW_NOTIFIER_COD;
        } else if (item instanceof PromoStackingData) {
            return CartVoucherPromoViewHolder.TYPE_VIEW_PROMO;
        } else if (item instanceof CartPromoSuggestion) {
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
        } else if (item instanceof RecipientAddressModel) {
            return ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS;
        } else if (item instanceof ShipmentCartItemModel) {
            return ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_ITEM;
        } else if (item instanceof ShipmentCostModel) {
            return ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST;
        } else if (item instanceof ShipmentInsuranceTncModel) {
            return ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC;
        } else if (item instanceof ShipmentSellerCashbackModel) {
            return ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK;
        } else if (item instanceof ShipmentDonationModel) {
            return ShipmentDonationViewHolder.ITEM_VIEW_DONATION;
        } else if (item instanceof EgoldAttributeModel) {
            return ShipmentEmasViewHolder.ITEM_VIEW_EMAS;
        } else if (item instanceof ShipmentButtonPaymentModel) {
            return ShipmentButtonPaymentViewHolder.Companion.getITEM_VIEW_PAYMENT_BUTTON();
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == ShipmentNotifierViewHolder.TYPE_VIEW_NOTIFIER_COD) {
            return new ShipmentNotifierViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            return new CartVoucherPromoViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            return new CartPromoSuggestionViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS) {
            return new ShipmentRecipientAddressViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_ITEM) {
            return new ShipmentItemViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            return new ShipmentInsuranceTncViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            return new ShipmentSellerCashbackViewHolder(view);
        } else if (viewType == ShipmentDonationViewHolder.ITEM_VIEW_DONATION) {
            return new ShipmentDonationViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentEmasViewHolder.ITEM_VIEW_EMAS) {
            return new ShipmentEmasViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentButtonPaymentViewHolder.getITEM_VIEW_PAYMENT_BUTTON()) {
            return new ShipmentButtonPaymentViewHolder(view, shipmentAdapterActionListener);
        }
        throw new RuntimeException("No view holder type found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Object data = shipmentDataList.get(position);

        if (viewType == ShipmentNotifierViewHolder.TYPE_VIEW_NOTIFIER_COD) {
            ((ShipmentNotifierViewHolder) holder).bind((ShipmentNotifierModel) data);
        } else if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            ((CartVoucherPromoViewHolder) holder).bindData((PromoStackingData) data, position);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionViewHolder) holder).bindData((CartPromoSuggestion) data, position);
        } else if (viewType == ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS) {
            ((ShipmentRecipientAddressViewHolder) holder).bindViewHolder((RecipientAddressModel) data,
                    showCaseObjectList, cartIds);
        } else if (viewType == ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_ITEM) {
            ((ShipmentItemViewHolder) holder).bindViewHolder(
                    (ShipmentCartItemModel) data, shipmentDataList, recipientAddressModel,
                    ratesDataConverter, showCaseObjectList);
            setShowCase(holder.itemView.getContext());
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) holder).bindViewHolder((ShipmentCostModel) data);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            ((ShipmentInsuranceTncViewHolder) holder).bindViewHolder((ShipmentInsuranceTncModel) data);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            ((ShipmentSellerCashbackViewHolder) holder).bindViewHolder(shipmentSellerCashbackModel);
        } else if (viewType == ShipmentDonationViewHolder.ITEM_VIEW_DONATION) {
            ((ShipmentDonationViewHolder) holder).bindViewHolder(shipmentDonationModel);
        } else if (viewType == ShipmentEmasViewHolder.ITEM_VIEW_EMAS) {
            ((ShipmentEmasViewHolder) holder).bindViewHolder(egoldAttributeModel);
        } else if (viewType == ShipmentButtonPaymentViewHolder.getITEM_VIEW_PAYMENT_BUTTON()) {
            ((ShipmentButtonPaymentViewHolder) holder).bindViewHolder((ShipmentButtonPaymentModel) data);
        }
    }

    @Override
    public int getItemCount() {
        return shipmentDataList.size();
    }

    public List<Object> getShipmentDataList() {
        return shipmentDataList;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ShipmentItemViewHolder) {
            ((ShipmentItemViewHolder) holder).unsubscribeDebouncer();
        }
    }

    private void setShowCase(Context context) {
        if (!hasShownShowCase && isShowOnboarding) {
            hasShownShowCase = true;
            createShowCaseDialog().show((Activity) context,
                    ShipmentFragment.class.getName(),
                    showCaseObjectList
            );
        }
    }

    @SuppressLint("PrivateResource")
    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .prevStringRes(R.string.show_case_prev)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.dp_12)
                .arrowWidth(R.dimen.dp_16)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.sp_12)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    public void clearData() {
        shipmentDataList.clear();
        promoGlobalStackData = null;
        cartPromoSuggestion = null;
        shipmentCartItemModelList = null;
        recipientAddressModel = null;
        shipmentCostModel = null;
        shipmentInsuranceTncModel = null;
        shipmentSellerCashbackModel = null;
        shipmentDonationModel = null;
        egoldAttributeModel = null;
        shipmentButtonPaymentModel = null;
        notifyDataSetChanged();
    }

    public void addNotifierData(ShipmentNotifierModel shipmentNotifierModel) {
        if (shipmentNotifierModel != null) {
            shipmentDataList.add(HEADER_POSITION, shipmentNotifierModel);
        }
    }

    public void removeNotifierData() {
        if (shipmentDataList.get(HEADER_POSITION) instanceof ShipmentNotifierModel) {
            shipmentDataList.remove(HEADER_POSITION);
            notifyItemRemoved(HEADER_POSITION);
        }
    }

    public void addPromoStackingVoucherData(PromoStackingData promoStackingData) {
        if (promoStackingData != null) {
            this.promoGlobalStackData = promoStackingData;
            shipmentDataList.add(promoStackingData);
        }
    }

    public void addPromoSuggestionData(CartPromoSuggestion cartPromoSuggestion) {
        if (cartPromoSuggestion != null && !TextUtils.isEmpty(cartPromoSuggestion.getPromoCode())) {
            this.cartPromoSuggestion = cartPromoSuggestion;
            shipmentDataList.add(cartPromoSuggestion);
        }
    }

    public void addAddressShipmentData(RecipientAddressModel recipientAddressModel) {
        if (recipientAddressModel != null) {
            this.recipientAddressModel = recipientAddressModel;
            shipmentDataList.add(recipientAddressModel);
        }
    }

    public void addCartItemDataList(List<ShipmentCartItemModel> shipmentCartItemModel) {
        if (shipmentCartItemModel != null) {
            this.shipmentCartItemModelList = shipmentCartItemModel;
            shipmentDataList.addAll(shipmentCartItemModel);
            checkDataForCheckout();
        }
    }

    public void addShipmentCostData(ShipmentCostModel shipmentCostModel) {
        if (shipmentCostModel != null) {
            this.shipmentCostModel = shipmentCostModel;
            shipmentDataList.add(shipmentCostModel);
            updateShipmentCostModel();
        }
    }

    public void addEgoldAttributeData(EgoldAttributeModel egoldAttributeModel) {
        if (egoldAttributeModel != null) {
            this.egoldAttributeModel = egoldAttributeModel;
            shipmentDataList.add(egoldAttributeModel);
        }
    }

    public void addShipmentDonationModel(ShipmentDonationModel shipmentDonationModel) {
        if (shipmentDonationModel != null) {
            this.shipmentDonationModel = shipmentDonationModel;
            shipmentDataList.add(shipmentDonationModel);
        }
    }

    public void addShipmentButtonPaymentModel(ShipmentButtonPaymentModel shipmentButtonPaymentModel) {
        if (shipmentButtonPaymentModel != null) {
            this.shipmentButtonPaymentModel = shipmentButtonPaymentModel;
            shipmentDataList.add(shipmentButtonPaymentModel);
        }
    }

    public void updateCheckoutButtonData(String defaultTotal) {
        if (shipmentCostModel != null && shipmentCartItemModelList != null) {
            int cartItemCounter = 0;
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                    cartItemCounter++;
                }
            }
            if (cartItemCounter == shipmentCartItemModelList.size()) {
                double priceTotal = shipmentCostModel.getTotalPrice() <= 0 ? 0 : shipmentCostModel.getTotalPrice();
                String priceTotalFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat((long) priceTotal, false);
                shipmentAdapterActionListener.onTotalPaymentChange(priceTotalFormatted);
            } else {
                shipmentAdapterActionListener.onTotalPaymentChange("-");
            }
        } else if (defaultTotal != null) {
            shipmentAdapterActionListener.onTotalPaymentChange(defaultTotal);
        }
    }

    public void updateShipmentSellerCashbackVisibility() {
        double cashback = 0;
        if (shipmentCartItemModelList != null && shipmentCartItemModelList.size() > 0) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                    if (cartItemModel.isCashback()) {
                        String cashbackPercentageString = cartItemModel.getCashback().replace("%", "");
                        double cashbackPercentage = Double.parseDouble(cashbackPercentageString);
                        cashback += cashbackPercentage / 100.0f * cartItemModel.getPrice() * cartItemModel.getQuantity();
                    }
                }
            }
        }

        if (cashback > 0) {
            if (shipmentSellerCashbackModel == null) {
                shipmentSellerCashbackModel = new ShipmentSellerCashbackModel();
            }
            shipmentSellerCashbackModel.setVisible(true);
            shipmentSellerCashbackModel.setSellerCashback(CurrencyFormatUtil.convertPriceValueToIdrFormat((long) cashback, false));
            shipmentDataList.add(shipmentSellerCashbackModel);
        }
    }

    public void updateInsuranceTncVisibility() {
        if (checkItemUseInsuranceExist()) {
            if (shipmentInsuranceTncModel == null) {
                shipmentInsuranceTncModel = new ShipmentInsuranceTncModel();
                shipmentInsuranceTncModel.setVisible(true);
                shipmentDataList.add(getShipmentCostPosition() + 1, shipmentInsuranceTncModel);
                notifyItemInserted(getShipmentCostPosition() + 1);
            }
        } else {
            for (int i = 0; i < shipmentDataList.size(); i++) {
                if (shipmentDataList.get(i) instanceof ShipmentInsuranceTncModel) {
                    shipmentInsuranceTncModel = null;
                    shipmentDataList.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
        }
    }

    private boolean checkItemUseInsuranceExist() {
        for (Object shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentData;
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance()) {
                    return true;
                }

                for (CartItemModel item : shipmentCartItemModel.getCartItemModels()) {
                    if (item.isProtectionOptIn()) return true;
                }
            }
        }
        return false;
    }

    public void updateSelectedAddress(RecipientAddressModel newlySelectedAddress) {
        int addressIndex = 0;
        for (Object item : shipmentDataList) {
            if (item instanceof RecipientAddressModel) {
                addressIndex = shipmentDataList.indexOf(item);
                break;
            }
        }
        if (addressIndex != 0) {
            shipmentDataList.set(addressIndex, newlySelectedAddress);
            this.recipientAddressModel = newlySelectedAddress;
            resetCourier();
            notifyDataSetChanged();
            shipmentAdapterActionListener.resetTotalPrice();
        }
    }

    public void updateDonation(boolean checked) {
        if (shipmentDonationModel != null) {
            shipmentDonationModel.setChecked(checked);
            updateShipmentCostModel();
            notifyItemChanged(getShipmentCostPosition());
        }
    }

    private void updateEmasCostModel() {
        long totalPrice = (long) shipmentCostModel.getTotalPrice();
        int valueTOCheck = 0;
        long buyEgoldValue = 0;

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
    }

    private long calculateBuyEgoldValue(int valueTOCheck, int minRange, int maxRange, long basisAmount) {

        long buyEgoldValue = 0;

        for (int i = minRange; i <= maxRange; i++) {
            if ((valueTOCheck + i) % basisAmount == 0) {
                buyEgoldValue = i;
                break;
            }
        }

        return buyEgoldValue;
    }

    private int getBuyEmasPosition() {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof EgoldAttributeModel) {
                return i;
            }
        }
        return 0;
    }

    public void updateEgold(boolean checked) {
        if (egoldAttributeModel != null) {
            egoldAttributeModel.setChecked(checked);
            updateShipmentCostModel();
            notifyItemChanged(getShipmentCostPosition());
        }
    }

    private void resetCourier() {
        setLastServiceId(0);
        for (Object item : shipmentDataList) {
            if (item instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) item).getSelectedShipmentDetailData() != null) {
                    ((ShipmentCartItemModel) item).setSelectedShipmentDetailData(null);
                }
            } else if (item instanceof ShipmentCostModel) {
                ((ShipmentCostModel) item).setAdditionalFee(0);
                ((ShipmentCostModel) item).setInsuranceFee(0);
                ((ShipmentCostModel) item).setShippingFee(0);
                ((ShipmentCostModel) item).setTotalPrice(0);
                ((ShipmentCostModel) item).setPromoPrice(0);
            }
        }
        updateInsuranceTncVisibility();
    }

    public void resetCourier(int cartPosition) {
        if (shipmentDataList.get(cartPosition) instanceof ShipmentCartItemModel) {
            ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(cartPosition);
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setSelectedShipment(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setSelectedCourier(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseDropshipper(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperPhone(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setDropshipperName(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setUsePartialOrder(false);
                shipmentCartItemModel.getSelectedShipmentDetailData().setOrderPriority(null);
                shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                updateShipmentCostModel();
                updateInsuranceTncVisibility();
            }
        }
        notifyItemChanged(cartPosition);
    }

    public RecipientAddressModel getAddressShipmentData() {
        return recipientAddressModel;
    }

    private void checkDataForCheckout() {
        boolean availableCheckout = true;
        for (Object shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() == null ||
                        ((ShipmentCartItemModel) shipmentData).isError()) {
                    availableCheckout = false;
                }
            }
        }
        if (availableCheckout) {
            shipmentAdapterActionListener.onCartDataEnableToCheckout();
        } else {
            shipmentAdapterActionListener.onCartDataDisableToCheckout(null);
        }
    }

    public void checkDropshipperValidation(int requestCode) {
        boolean hasSelectAllCourier = checkHasSelectAllCourier(true);
        if (hasSelectAllCourier) {
            boolean availableCheckout = true;
            int errorPosition = DEFAULT_ERROR_POSITION;
            Object errorSelectedShipmentData = null;
            for (int i = 0; i < shipmentDataList.size(); i++) {
                Object shipmentData = shipmentDataList.get(i);
                if (shipmentData instanceof ShipmentCartItemModel) {
                    if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() != null &&
                            ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getUseDropshipper() != null &&
                            ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getUseDropshipper()) {
                        if (TextUtils.isEmpty(((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getDropshipperName()) ||
                                TextUtils.isEmpty(((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getDropshipperPhone()) ||
                                !((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().isDropshipperNameValid() ||
                                !((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().isDropshipperPhoneValid()) {
                            availableCheckout = false;
                            errorPosition = i;
                            errorSelectedShipmentData = shipmentData;
                        }
                    }
                }
            }

            shipmentAdapterActionListener.onCheckoutValidationResult(availableCheckout, errorSelectedShipmentData, errorPosition, requestCode);
        } else {
            int errorPosition = 0;
            if (shipmentCartItemModelList != null && shipmentDataList != null) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                    if (shipmentCartItemModel.getSelectedShipmentDetailData() == null || (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                            shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() == null)) {
                        errorPosition = shipmentDataList.indexOf(shipmentCartItemModel);
                        break;
                    }
                }
            }
            shipmentAdapterActionListener.onCheckoutValidationResult(false, null, errorPosition, requestCode);
        }
    }

    public ShipmentCartItemModel setSelectedCourier(int position, CourierItemData newCourierItemData) {
        ShipmentCartItemModel shipmentCartItemModel = null;
        Object currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItemModel) {
            shipmentCartItemModel = (ShipmentCartItemModel) currentShipmentData;
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setOrderPriority(null);
                CourierItemData oldCourierItemData = shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier();
                checkAppliedCourierPromo(position, oldCourierItemData, newCourierItemData, shipmentCartItemModel);
                shipmentCartItemModel.getSelectedShipmentDetailData().setSelectedCourier(newCourierItemData);
                if (!newCourierItemData.isAllowDropshiper()) {
                    shipmentCartItemModel.getSelectedShipmentDetailData().setUseDropshipper(null);
                }
            } else {
                ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
                shipmentDetailData.setSelectedCourier(newCourierItemData);
                shipmentDetailData.setShipmentCartData(shipmentCartItemModel.getShipmentCartData());
                shipmentCartItemModel.setSelectedShipmentDetailData(shipmentDetailData);
                if (!newCourierItemData.isAllowDropshiper()) {
                    shipmentCartItemModel.getSelectedShipmentDetailData().setUseDropshipper(null);
                }
            }
            updateShipmentCostModel();
            checkDataForCheckout();
        }
        notifyItemChanged(getShipmentCostPosition());
        notifyItemChanged(position);
        checkHasSelectAllCourier(false);

        return shipmentCartItemModel;
    }

    private void checkAppliedCourierPromo(int position, CourierItemData oldCourierItemData,
                                          CourierItemData newCourierItemData, ShipmentCartItemModel shipmentCartItemModel) {
        // Do this section if toggle year end promo is on
        boolean isToogleYearEndPromoOn = shipmentAdapterActionListener.isToogleYearEndPromoOn();
        if (isToogleYearEndPromoOn && shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            // Check if promo applied on current selected courier
            if (shipmentCartItemModel.getSelectedShipmentDetailData().isCourierPromoApplied() &&
                    TextUtils.isEmpty(newCourierItemData.getPromoCode())) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setCourierPromoApplied(false);
                // If applied on current selected courier but not on new selected courier then
                // check all item if promo still exist
                boolean courierPromoStillExist = false;
                for (int i = 0; i < shipmentDataList.size(); i++) {
                    if (i != position && shipmentDataList.get(i) instanceof ShipmentCartItemModel) {
                        ShipmentCartItemModel model = (ShipmentCartItemModel) shipmentDataList.get(i);
                        if (model.getSelectedShipmentDetailData() != null &&
                                model.getSelectedShipmentDetailData().isCourierPromoApplied()) {
                            courierPromoStillExist = true;
                            break;
                        }
                    }
                }
                // If courier promo not exist anymore, cancel promo
                if (!courierPromoStillExist) {
                    shipmentAdapterActionListener.onCourierPromoCanceled(oldCourierItemData.getName(), oldCourierItemData.getPromoCode());
                }
            }
        }
    }

    public boolean isCourierPromoStillExist() {
        boolean courierPromoStillExist = false;
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel model = (ShipmentCartItemModel) shipmentDataList.get(i);
                if (model.getSelectedShipmentDetailData() != null &&
                        model.getSelectedShipmentDetailData().isCourierPromoApplied()) {
                    courierPromoStillExist = true;
                    break;
                }
            }
        }
        return courierPromoStillExist;
    }

    public void cancelAllCourierPromo() {
        for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null && shipmentCartItemModel.getSelectedShipmentDetailData().isCourierPromoApplied()) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setCourierPromoApplied(false);
            }
        }
    }

    public void setShippingCourierViewModels(List<ShippingCourierViewModel> shippingCourierViewModels,
                                             CourierItemData recommendedCourier, int position) {
        for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
            shippingCourierViewModel.setSelected(false);
        }
        Object currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItemModel) {
            ShipmentCartItemModel cartItemModel = (ShipmentCartItemModel) currentShipmentData;
            if (cartItemModel.getSelectedShipmentDetailData() != null &&
                    cartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
                    if (shippingCourierViewModel.getProductData().getShipperProductId() == recommendedCourier.getShipperProductId()) {
                        shippingCourierViewModel.setSelected(true);
                        break;
                    }
                }
                cartItemModel.getSelectedShipmentDetailData().setShippingCourierViewModels(shippingCourierViewModels);
            }
        }
    }

    public boolean checkHasSelectAllCourier(boolean passCheckShipmentFromPaymentClick) {
        int cartItemCounter = 0;
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                    cartItemCounter++;
                }
            }
            if (cartItemCounter == shipmentCartItemModelList.size()) {
                RequestData requestData = getRequestData(null, null);
                if (!passCheckShipmentFromPaymentClick) {
                    shipmentAdapterActionListener.onFinishChoosingShipment(requestData.getPromoRequestData());
                }
                shipmentAdapterActionListener.updateCheckoutRequest(requestData.getCheckoutRequestData());
                return true;
            }
        }
        return false;
    }

    public void updateShipmentCostModel() {
        double totalWeight = 0;
        double totalPrice = 0;
        double additionalFee = 0;
        double totalItemPrice = 0;
        double tradeInPrice = 0;
        int totalItem = 0;
        double totalPurchaseProtectionPrice = 0;
        int totalPurchaseProtectionItem = 0;
        double shippingFee = 0;
        double insuranceFee = 0;
        double orderPriorityFee = 0;
        for (Object shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel shipmentSingleAddressItem =
                        (ShipmentCartItemModel) shipmentData;
                List<CartItemModel> cartItemModels = shipmentSingleAddressItem.getCartItemModels();
                for (CartItemModel cartItemModel : cartItemModels) {
                    if (!cartItemModel.isError()) {
                        totalWeight += (cartItemModel.getWeight() * cartItemModel.getQuantity());
                        totalItem += cartItemModel.getQuantity();

                        if (cartItemModel.isProtectionOptIn()) {
                            totalPurchaseProtectionItem += cartItemModel.getQuantity();
                            totalPurchaseProtectionPrice += cartItemModel.getProtectionPrice();
                        }

                        if (cartItemModel.isValidTradeIn()) {
                            tradeInPrice += cartItemModel.getOldDevicePrice();
                        }

                        totalItemPrice += (cartItemModel.getPrice() * cartItemModel.getQuantity());
                    }
                }


                if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() != null &&
                        ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getSelectedCourier() != null &&
                        (!((ShipmentCartItemModel) shipmentData).isError())) {
                    Boolean useInsurance = ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getUseInsurance();
                    Boolean isOrderPriority = ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().isOrderPriority();
                    shippingFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperPrice();
                    if (useInsurance != null && useInsurance) {
                        insuranceFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getInsurancePrice();
                    }
                    if (isOrderPriority != null && isOrderPriority) {
                        orderPriorityFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getPriorityPrice();
                    }
                    additionalFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                            .getSelectedCourier().getAdditionalPrice();
                }
            }
        }
        totalPrice = totalItemPrice + shippingFee + insuranceFee + orderPriorityFee + totalPurchaseProtectionPrice + additionalFee -
                shipmentCostModel.getPromoPrice() - tradeInPrice - (double) shipmentCostModel.getTotalDiscWithoutCashback();
        shipmentCostModel.setTotalWeight(totalWeight);
        shipmentCostModel.setAdditionalFee(additionalFee);
        shipmentCostModel.setTotalItemPrice(totalItemPrice);
        shipmentCostModel.setTotalItem(totalItem);
        shipmentCostModel.setShippingFee(shippingFee);
        shipmentCostModel.setInsuranceFee(insuranceFee);
        shipmentCostModel.setPriorityFee(orderPriorityFee);
        shipmentCostModel.setTotalPurchaseProtectionItem(totalPurchaseProtectionItem);
        shipmentCostModel.setPurchaseProtectionFee(totalPurchaseProtectionPrice);
        shipmentCostModel.setTradeInPrice(tradeInPrice);
        if (shipmentDonationModel.isChecked()) {
            shipmentCostModel.setDonation(shipmentDonationModel.getDonation().getNominal());
        } else {
            if (shipmentCostModel.getDonation() > 0) {
                shipmentCostModel.setDonation(0);
            }
        }
        totalPrice += shipmentCostModel.getDonation();
        shipmentCostModel.setTotalPrice(totalPrice);

        if (egoldAttributeModel != null && egoldAttributeModel.isEligible()) {
            updateEmasCostModel();
            if (egoldAttributeModel.isChecked()) {
                totalPrice += egoldAttributeModel.getBuyEgoldValue();
                shipmentCostModel.setTotalPrice(totalPrice);
                shipmentCostModel.setEmasPrice(egoldAttributeModel.getBuyEgoldValue());
            } else if (shipmentCostModel.getEmasPrice() > 0) {
                shipmentCostModel.setEmasPrice(0);
            }
            notifyDataSetChanged();
        }

        updateCheckoutButtonData(null);
    }

    public void clearTotalPromoStackAmount() {
        shipmentCostModel.setTotalPromoStackAmount(0);
        shipmentCostModel.setTotalDiscWithoutCashback(0);
    }

    public int getShipmentCostPosition() {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof ShipmentCostModel) {
                return i;
            }
        }
        return 0;
    }

    public void updateItemAndTotalCost(int position) {
        notifyItemChanged(getShipmentCostPosition());
        notifyItemChanged(position);
    }

    public void updateItemPromoStackVoucher(PromoStackingData promoStackingData) {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            Object object = shipmentDataList.get(i);
            if (object instanceof PromoStackingData) {
                shipmentDataList.set(i, promoStackingData);
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(false);
                notifyItemChanged(i);
            }
        }
    }

    public void updatePromoStack(DataUiModel dataUiModel) {
        if (dataUiModel != null) {
            if (shipmentCostModel != null) {
                if (promoGlobalStackData != null) {
                    boolean isApplied = false;
                    if (TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(dataUiModel.getMessage().getState()) == TickerPromoStackingCheckoutView.State.ACTIVE) {
                        isApplied = true;
                        for (int i = 0; i < shipmentDataList.size(); i++) {
                            Object itemAdapter = shipmentDataList.get(i);
                            if (itemAdapter instanceof PromoStackingData) {
                                ((PromoStackingData) itemAdapter).setState(TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(dataUiModel.getMessage().getState()));
                                ((PromoStackingData) itemAdapter).setTitle(dataUiModel.getTitleDescription());
                                ((PromoStackingData) itemAdapter).setDescription(dataUiModel.getMessage().getText());
                                ((PromoStackingData) itemAdapter).setVariant(TickerPromoStackingCheckoutView.Variant.GLOBAL);
                                notifyItemChanged(i);
                            }
                        }
                    }
                    for (VoucherOrdersItemUiModel voucherOrdersItemUiModel : dataUiModel.getVoucherOrders()) {
                        if (TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(voucherOrdersItemUiModel.getMessage().getState()) == TickerPromoStackingCheckoutView.State.ACTIVE) {
                            isApplied = true;
                        }
                    }
                    if (isApplied) {
                        shipmentCostModel.setTotalPromoStackAmount(dataUiModel.getBenefit().getFinalBenefitAmount());
                        shipmentCostModel.setTotalPromoStackAmountStr(dataUiModel.getBenefit().getFinalBenefitAmountStr());

                        int totalDiscWithoutCashback = 0;
                        for (SummariesUiModel summariesUiModel : dataUiModel.getBenefit().getSummaries()) {
                            if (!summariesUiModel.getType().equalsIgnoreCase("cashback")) {
                                totalDiscWithoutCashback += summariesUiModel.getAmount();
                            }
                        }
                        shipmentCostModel.setTotalDiscWithoutCashback(totalDiscWithoutCashback);
                    } else {
                        shipmentCostModel.setTotalPromoStackAmount(0);
                        shipmentCostModel.setTotalPromoStackAmountStr("-");
                        shipmentCostModel.setTotalDiscWithoutCashback(0);
                    }
                    for (int i = 0; i < shipmentDataList.size(); i++) {
                        Object itemAdapter = shipmentDataList.get(i);
                        if (itemAdapter instanceof CartPromoSuggestion) {
                            ((CartPromoSuggestion) itemAdapter).setVisible(false);
                            notifyItemChanged(i);
                        } else if (itemAdapter instanceof RecipientAddressModel) {
                            ((RecipientAddressModel) itemAdapter).setStateExtraPaddingTop(true);
                            notifyItemChanged(i);
                        } else if (itemAdapter instanceof ShipmentCartItemModel) {
                            updatePromoMerchant((ShipmentCartItemModel) itemAdapter, dataUiModel.getVoucherOrders());
                        }
                    }
                } else {
                    shipmentCostModel.setTotalPromoStackAmount(0);
                    shipmentCostModel.setTotalPromoStackAmountStr("-");
                    for (int i = 0; i < shipmentDataList.size(); i++) {
                        Object itemAdapter = shipmentDataList.get(i);
                        if (itemAdapter instanceof PromoStackingData) {
                            ((PromoStackingData) itemAdapter).setState(TickerPromoStackingCheckoutView.State.EMPTY);
                            ((PromoStackingData) itemAdapter).setVariant(TickerPromoStackingCheckoutView.Variant.GLOBAL);
                            notifyItemChanged(i);
                        } else if (itemAdapter instanceof CartPromoSuggestion) {
                            ((CartPromoSuggestion) itemAdapter).setVisible(true);
                            notifyItemChanged(i);
                        } else if (itemAdapter instanceof RecipientAddressModel) {
                            ((RecipientAddressModel) itemAdapter).setStateExtraPaddingTop(false);
                            notifyItemChanged(i);
                        } else if (itemAdapter instanceof ShipmentCartItemModel) {
                            updatePromoMerchant((ShipmentCartItemModel) itemAdapter, dataUiModel.getVoucherOrders());
                        }
                    }
                }
                updateShipmentCostModel();
                notifyItemChanged(getShipmentCostPosition());
            }
        }
    }

    public void updatePromoMerchant(ShipmentCartItemModel shipmentCartItemModel, List<VoucherOrdersItemUiModel> voucherOrdersItemUiModelList) {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof ShipmentCartItemModel) {
                for (VoucherOrdersItemUiModel voucherOrdersItemUiModel : voucherOrdersItemUiModelList) {
                    if (shipmentCartItemModel.getCartString().equalsIgnoreCase(voucherOrdersItemUiModel.getUniqueId())) {
                        if (voucherOrdersItemUiModel.getType().equalsIgnoreCase(TickerCheckoutUtilKt.getMERCHANT())) {
                            shipmentCartItemModel.setVoucherOrdersItemUiModel(voucherOrdersItemUiModel);
                        } else if (voucherOrdersItemUiModel.getType().equalsIgnoreCase(TickerCheckoutUtilKt.getLOGISTIC())) {
                            VoucherLogisticItemUiModel model = new VoucherLogisticItemUiModel();
                            model.setCode(voucherOrdersItemUiModel.getCode());
                            model.setMessage(voucherOrdersItemUiModel.getMessage());
                            model.setCouponDesc(voucherOrdersItemUiModel.getTitleDescription());
                            model.setCouponAmount(Utils.getFormattedCurrency(voucherOrdersItemUiModel.getDiscountAmount()));
                            shipmentCartItemModel.setVoucherLogisticItemUiModel(model);
                        }
                        notifyItemChanged(i);
                    }
                }
            }
        }
    }

    public void resetCourierPromoState() {
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                shipmentCartItemModel.setStateHasLoadCourierState(false);
            }
        }
    }

    private void updateFirstInvoiceItemMargin(int iteration, ShipmentCartItemModel shipmentCartItemModel, boolean hasExtraMarginTop) {
        if (shipmentCartItemModel.getRecipientAddressModel() != null && shipmentCartItemModelList != null &&
                shipmentCartItemModelList.get(0) != null &&
                shipmentCartItemModel.getShopId() == shipmentCartItemModelList.get(0).getShopId()) {
            shipmentCartItemModel.setStateHasExtraMarginTop(hasExtraMarginTop);

            notifyItemChanged(iteration);
        }
    }

    public void updateItemPromoGlobalStack(PromoStackingData cartPromoStacking) {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            Object shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof PromoStackingData) {
                shipmentDataList.set(i, cartPromoStacking);
                promoGlobalStackData = cartPromoStacking;
                promoGlobalStackData.setTitleDefault(((PromoStackingData) shipmentData).getTitleDefault());
                promoGlobalStackData.setCounterLabelDefault(((PromoStackingData) shipmentData).getCounterLabelDefault());
                checkDataForCheckout();
                notifyItemChanged(i);
            } else if (shipmentData instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) shipmentData).setVisible(false);
                notifyItemChanged(i);
            }
        }
        notifyItemChanged(getShipmentCostPosition());
    }

    public void cancelAutoApplyCoupon(String variant) {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            Object shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof PromoStackingData) {
                ((PromoStackingData) shipmentData).setState(TickerPromoStackingCheckoutView.State.EMPTY);
                if (!variant.isEmpty() && variant.equalsIgnoreCase("merchant_voucher")) {
                    ((PromoStackingData) shipmentData).setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
                } else {
                    ((PromoStackingData) shipmentData).setVariant(TickerPromoStackingCheckoutView.Variant.GLOBAL);
                }
                notifyItemChanged(i);
            } else if (shipmentData instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) shipmentData).setVisible(true);
                notifyItemChanged(i);
            }
        }
    }

    public void updateShipmentDestinationPinpoint(String latitude, String longitude) {
        if (latitude != null && longitude != null) {
            if (recipientAddressModel != null) {
                recipientAddressModel.setLatitude(latitude);
                recipientAddressModel.setLongitude(longitude);
            }
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel.getShipmentCartData() != null) {
                    shipmentCartItemModel.getShipmentCartData().setDestinationLatitude(latitude);
                    shipmentCartItemModel.getShipmentCartData().setDestinationLongitude(longitude);
                }
            }
        }
    }

    public boolean hasSetAllCourier() {
        for (Object itemData : shipmentDataList) {
            if (itemData instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) itemData).getSelectedShipmentDetailData() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasAppliedPromoStackCode() {
        boolean hasApplied = false;
        for (Object itemAdapter : shipmentDataList) {
            if (itemAdapter instanceof PromoStackingData) {
                if (((PromoStackingData) itemAdapter).getState() != TickerPromoStackingCheckoutView.State.EMPTY) {
                    hasApplied = true;
                }
            }

            if (itemAdapter instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) itemAdapter).getVoucherOrdersItemUiModel() != null) {
                    if (TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(((ShipmentCartItemModel) itemAdapter).getVoucherOrdersItemUiModel().getMessage().getState()) != TickerPromoStackingCheckoutView.State.EMPTY) {
                        hasApplied = true;
                    }
                }

                if (((ShipmentCartItemModel) itemAdapter).getVoucherLogisticItemUiModel() != null) {
                    if (TickerCheckoutUtilKt.mapToStatePromoStackingCheckout(((ShipmentCartItemModel) itemAdapter).getVoucherLogisticItemUiModel().getMessage().getState()) != TickerPromoStackingCheckoutView.State.EMPTY) {
                        hasApplied = true;
                    }
                }
            }
        }
        return hasApplied;
    }

    public void setCourierPromoApplied(int position) {
        if (shipmentDataList.get(position) instanceof ShipmentCartItemModel) {
            ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(position);
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setCourierPromoApplied(true);
            }
        }
    }

    public RequestData getRequestData(RecipientAddressModel recipientAddressModel, List<ShipmentCartItemModel> shipmentCartItemModelList) {
        RecipientAddressModel addressModel;
        if (recipientAddressModel != null) {
            addressModel = recipientAddressModel;
        } else {
            addressModel = this.recipientAddressModel;
        }
        if (shipmentCartItemModelList != null) {
            this.shipmentCartItemModelList = shipmentCartItemModelList;
        }
        return shipmentDataRequestConverter.generateRequestData(this.shipmentCartItemModelList, addressModel);
    }

    public ShipmentCartItemModel getShipmentCartItemModelByIndex(int index) {
        if (shipmentDataList != null && !shipmentDataList.isEmpty() && index < shipmentDataList.size()) {
            return shipmentDataList.get(index) instanceof ShipmentCartItemModel ?
                    (ShipmentCartItemModel) shipmentDataList.get(index) : null;
        } else {
            return null;
        }
    }

    public int getLastChooseCourierItemPosition() {
        return lastChooseCourierItemPosition;
    }

    public void setLastChooseCourierItemPosition(int lastChooseCourierItemPosition) {
        this.lastChooseCourierItemPosition = lastChooseCourierItemPosition;
    }

    public int getLastServiceId() {
        return lastServiceId;
    }

    public void setLastServiceId(int lastServiceId) {
        this.lastServiceId = lastServiceId;
    }

    public String getBlackboxInfo() {
        return blackboxInfo;
    }

    public void setBlackboxInfo(String blackboxInfo) {
        this.blackboxInfo = blackboxInfo;
    }

    public PromoStackingData getPromoGlobalStackData() {
        return promoGlobalStackData;
    }


    public static class RequestData {

        private List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData;
        private List<DataCheckoutRequest> checkoutRequestData;
        private List<DataChangeAddressRequest> changeAddressRequestData;

        @Inject
        public RequestData() {
            promoRequestData = new ArrayList<>();
            checkoutRequestData = new ArrayList<>();
            changeAddressRequestData = new ArrayList<>();
        }

        public List<CheckPromoCodeCartShipmentRequest.Data> getPromoRequestData() {
            return promoRequestData;
        }

        public void setPromoRequestData(List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData) {
            this.promoRequestData = promoRequestData;
        }

        public List<DataCheckoutRequest> getCheckoutRequestData() {
            return checkoutRequestData;
        }

        public void setCheckoutRequestData(List<DataCheckoutRequest> checkoutRequestData) {
            this.checkoutRequestData = checkoutRequestData;
        }

        public List<DataChangeAddressRequest> getChangeAddressRequestData() {
            return changeAddressRequestData;
        }

        public void setChangeAddressRequestData(List<DataChangeAddressRequest> changeAddressRequestData) {
            this.changeAddressRequestData = changeAddressRequestData;
        }
    }

    public List<ShipmentCartItemModel> getShipmentCartItemModelList() {
        return shipmentCartItemModelList;
    }

    public ShipmentCartItemModel getShipmentCartItemModelDataByIndex(int index) {
        if (shipmentCartItemModelList.get(index) != null) {
            return shipmentCartItemModelList.get(index);
        }
        return null;
    }
}