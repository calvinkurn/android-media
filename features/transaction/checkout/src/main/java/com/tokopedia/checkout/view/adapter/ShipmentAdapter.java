package com.tokopedia.checkout.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.checkout.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.converter.RatesDataConverter;
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel;
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel;
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.uimodel.ShipmentInsuranceTncModel;
import com.tokopedia.checkout.view.uimodel.ShippingCompletionTickerModel;
import com.tokopedia.checkout.view.viewholder.PromoCheckoutViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentButtonPaymentViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentDonationViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentEmasViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentInsuranceTncViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentItemViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentRecipientAddressViewHolder;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel;
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener;
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel;
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.features.checkout.view.viewholder.ShippingCompletionTickerViewHolder;
import com.tokopedia.utils.currency.CurrencyFormatUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int DEFAULT_ERROR_POSITION = -1;
    public static final int HEADER_POSITION = 0;
    private static final long LAST_THREE_DIGIT_MODULUS = 1000;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;
    private final SellerCashbackListener sellerCashbackListener;
    private List<Object> shipmentDataList;

    private TickerAnnouncementHolderData tickerAnnouncementHolderData;
    private LastApplyUiModel lastApplyUiModel;
    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private ShipmentInsuranceTncModel shipmentInsuranceTncModel;
    private ShipmentSellerCashbackModel shipmentSellerCashbackModel;
    private ShipmentDonationModel shipmentDonationModel;
    private EgoldAttributeModel egoldAttributeModel;
    private ShippingCompletionTickerModel shippingCompletionTickerModel;
    private ShipmentButtonPaymentModel shipmentButtonPaymentModel;

    private ShipmentDataRequestConverter shipmentDataRequestConverter;
    private RatesDataConverter ratesDataConverter;
    private CompositeSubscription compositeSubscription;

    private boolean isShowOnboarding;
    private int lastChooseCourierItemPosition;
    private int lastServiceId;

    @Inject
    public ShipmentAdapter(ShipmentAdapterActionListener shipmentAdapterActionListener,
                           ShipmentDataRequestConverter shipmentDataRequestConverter,
                           RatesDataConverter ratesDataConverter,
                           SellerCashbackListener sellerCashbackListener) {
        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
        this.shipmentDataRequestConverter = shipmentDataRequestConverter;
        this.ratesDataConverter = ratesDataConverter;
        this.sellerCashbackListener = sellerCashbackListener;
        this.shipmentDataList = new ArrayList<>();
    }

    public void setShowOnboarding(boolean showOnboarding) {
        this.isShowOnboarding = showOnboarding;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = shipmentDataList.get(position);

        if (item instanceof RecipientAddressModel) {
            return ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS;
        } else if (item instanceof ShipmentCartItemModel) {
            return ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_ITEM;
        } else if (item instanceof LastApplyUiModel) {
            return PromoCheckoutViewHolder.getITEM_VIEW_PROMO_CHECKOUT();
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
        } else if (item instanceof TickerAnnouncementHolderData) {
            return TickerAnnouncementViewHolder.Companion.getLAYOUT();
        } else if (item instanceof ShippingCompletionTickerModel) {
            return ShippingCompletionTickerViewHolder.Companion.getITEM_VIEW_TICKER_SHIPPING_COMPLETION();
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS) {
            return new ShipmentRecipientAddressViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_ITEM) {
            return new ShipmentItemViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view);
        } else if (viewType == PromoCheckoutViewHolder.getITEM_VIEW_PROMO_CHECKOUT()) {
            return new PromoCheckoutViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            return new ShipmentInsuranceTncViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            return new ShipmentSellerCashbackViewHolder(view, sellerCashbackListener);
        } else if (viewType == ShipmentDonationViewHolder.ITEM_VIEW_DONATION) {
            return new ShipmentDonationViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentEmasViewHolder.ITEM_VIEW_EMAS) {
            return new ShipmentEmasViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentButtonPaymentViewHolder.getITEM_VIEW_PAYMENT_BUTTON()) {
            if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
                compositeSubscription = new CompositeSubscription();
            }
            return new ShipmentButtonPaymentViewHolder(view, shipmentAdapterActionListener, compositeSubscription);
        } else if (viewType == TickerAnnouncementViewHolder.Companion.getLAYOUT()) {
            return new TickerAnnouncementViewHolder(view, null);
        } else if (viewType == ShippingCompletionTickerViewHolder.Companion.getITEM_VIEW_TICKER_SHIPPING_COMPLETION()) {
            return new ShippingCompletionTickerViewHolder(view, shipmentAdapterActionListener);
        }
        throw new RuntimeException("No view holder type found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Object data = shipmentDataList.get(position);

        if (viewType == ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS) {
            ((ShipmentRecipientAddressViewHolder) holder).bindViewHolder((RecipientAddressModel) data, isShowOnboarding);
        } else if (viewType == ShipmentItemViewHolder.ITEM_VIEW_SHIPMENT_ITEM) {
            ((ShipmentItemViewHolder) holder).bindViewHolder((ShipmentCartItemModel) data, shipmentDataList, recipientAddressModel, ratesDataConverter);
        } else if (viewType == PromoCheckoutViewHolder.getITEM_VIEW_PROMO_CHECKOUT()) {
            ((PromoCheckoutViewHolder) holder).bindViewHolder(lastApplyUiModel);
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) holder).bindViewHolder((ShipmentCostModel) data);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            ((ShipmentInsuranceTncViewHolder) holder).bindViewHolder((ShipmentInsuranceTncModel) data, getItemCount());
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            ((ShipmentSellerCashbackViewHolder) holder).bindViewHolder(shipmentSellerCashbackModel);
        } else if (viewType == ShipmentDonationViewHolder.ITEM_VIEW_DONATION) {
            ((ShipmentDonationViewHolder) holder).bindViewHolder(shipmentDonationModel);
        } else if (viewType == ShipmentEmasViewHolder.ITEM_VIEW_EMAS) {
            ((ShipmentEmasViewHolder) holder).bindViewHolder(egoldAttributeModel);
        } else if (viewType == ShipmentButtonPaymentViewHolder.getITEM_VIEW_PAYMENT_BUTTON()) {
            ((ShipmentButtonPaymentViewHolder) holder).bindViewHolder((ShipmentButtonPaymentModel) data);
        } else if (viewType == TickerAnnouncementViewHolder.Companion.getLAYOUT()) {
            ((TickerAnnouncementViewHolder) holder).bind((TickerAnnouncementHolderData) data);
        } else if (viewType == ShippingCompletionTickerViewHolder.Companion.getITEM_VIEW_TICKER_SHIPPING_COMPLETION()) {
            ((ShippingCompletionTickerViewHolder) holder).bindViewHolder((ShippingCompletionTickerModel) data);
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

    public void clearCompositeSubscription() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }

    public void clearData() {
        shipmentDataList.clear();
        shipmentCartItemModelList = null;
        recipientAddressModel = null;
        shipmentCostModel = null;
        shipmentInsuranceTncModel = null;
        shipmentSellerCashbackModel = null;
        shipmentDonationModel = null;
        egoldAttributeModel = null;
        shipmentButtonPaymentModel = null;
        lastApplyUiModel = null;
        shippingCompletionTickerModel = null;
        notifyDataSetChanged();
    }

    public void addTickerAnnouncementdata(TickerAnnouncementHolderData tickerAnnouncementHolderData) {
        if (tickerAnnouncementHolderData != null) {
            shipmentDataList.add(HEADER_POSITION, tickerAnnouncementHolderData);
            this.tickerAnnouncementHolderData = tickerAnnouncementHolderData;
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

    public void addLastApplyUiDataModel(LastApplyUiModel lastApplyUiModel) {
        if (lastApplyUiModel != null) {
            this.lastApplyUiModel = lastApplyUiModel;
            shipmentDataList.add(lastApplyUiModel);
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
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                    if ((shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null && !shipmentAdapterActionListener.isTradeInByDropOff()) ||
                            (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null && shipmentAdapterActionListener.isTradeInByDropOff())) {
                        cartItemCounter++;
                    }
                }
            }
            if (cartItemCounter == shipmentCartItemModelList.size()) {
                double priceTotal = shipmentCostModel.getTotalPrice() <= 0 ? 0 : shipmentCostModel.getTotalPrice();
                String priceTotalFormatted = Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat((long) priceTotal, false));
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
            shipmentSellerCashbackModel.setSellerCashbackFmt(Utils.removeDecimalSuffix(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat((long) cashback, false)));
            shipmentDataList.add(shipmentSellerCashbackModel);
        }
    }

    public void updateShippingCompletionTickerVisibility() {
        int positionDiff = 1;
        if (shipmentInsuranceTncModel != null) {
            positionDiff++;
        }

        if (!hasSetAllCourier()) {
            if (shippingCompletionTickerModel == null) {
                shippingCompletionTickerModel = new ShippingCompletionTickerModel("Pilih pengiriman dulu sebelum lanjut bayar.");
                shipmentDataList.add(getShipmentCostPosition() + positionDiff, shippingCompletionTickerModel);
                notifyItemInserted(getShipmentCostPosition() + positionDiff);
            }
        } else {
            for (int i = 0; i < shipmentDataList.size(); i++) {
                if (shipmentDataList.get(i) instanceof ShippingCompletionTickerModel) {
                    shippingCompletionTickerModel = null;
                    shipmentDataList.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
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

        if (basisAmount == 0) {
            return 0;
        }

        long buyEgoldValue = 0;
        for (int i = minRange; i <= maxRange; i++) {
            if ((valueTOCheck + i) % basisAmount == 0) {
                buyEgoldValue = i;
                break;
            }
        }
        return buyEgoldValue;
    }

    public void updateEgold(boolean checked) {
        if (egoldAttributeModel != null) {
            egoldAttributeModel.setChecked(checked);
            updateShipmentCostModel();
            notifyItemChanged(getShipmentCostPosition());
        }
    }

    public void resetCourier(int cartPosition) {
        if (shipmentDataList.get(cartPosition) instanceof ShipmentCartItemModel) {
            ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(cartPosition);
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                shipmentCartItemModel.setSelectedShipmentDetailData(null);
                shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                updateShipmentCostModel();
                updateInsuranceTncVisibility();
            }
        }
        notifyItemChanged(cartPosition);
    }

    public void resetAllCourier() {
        boolean eligibleNewShippingExperience = false;
        for (int position = 0; position < shipmentDataList.size(); position++) {
            if (shipmentDataList.get(position) instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(position);
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                    shipmentCartItemModel.setSelectedShipmentDetailData(null);
                    shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                    notifyItemChanged(position);
                    eligibleNewShippingExperience = shipmentCartItemModel.isEligibleNewShippingExperience();
                }
            }
        }
        updateShipmentCostModel();
        updateInsuranceTncVisibility();
        if (eligibleNewShippingExperience) {
            updateShippingCompletionTickerVisibility();
        }
    }

    public RecipientAddressModel getAddressShipmentData() {
        return recipientAddressModel;
    }

    public int getRecipientAddressModelPosition() {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof RecipientAddressModel) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    public int getTickerAnnouncementHolderDataIndex() {
        return shipmentDataList.indexOf(tickerAnnouncementHolderData);
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
            shipmentAdapterActionListener.onDataEnableToCheckout();
        } else {
            shipmentAdapterActionListener.onDataDisableToCheckout(null);
        }
    }

    public void checkDropshipperValidation() {
        boolean hasSelectAllCourier = checkHasSelectAllCourier(true, -1, "");
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

            shipmentAdapterActionListener.onCheckoutValidationResult(availableCheckout, errorSelectedShipmentData, errorPosition);
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
            shipmentAdapterActionListener.onCheckoutValidationResult(false, null, errorPosition);
        }
    }

    public void setSelectedCourierTradeInPickup(CourierItemData courierItemData) {
        // Should be only one invoice
        int index = 0;
        ShipmentCartItemModel shipmentCartItemModel = null;
        for (Object object : shipmentDataList) {
            if (object instanceof ShipmentCartItemModel) {
                index = shipmentDataList.indexOf(object);
                shipmentCartItemModel = (ShipmentCartItemModel) object;
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                    ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
                    shipmentDetailData.setSelectedCourierTradeInDropOff(courierItemData);
                } else {
                    ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
                    shipmentDetailData.setSelectedCourierTradeInDropOff(courierItemData);
                    shipmentCartItemModel.setSelectedShipmentDetailData(shipmentDetailData);
                }

                updateShipmentCostModel();
                checkDataForCheckout();
                break;
            }
        }

        if (index > 0) {
            notifyItemChanged(getShipmentCostPosition());
            notifyItemChanged(index);
            checkHasSelectAllCourier(false, index, shipmentCartItemModel.getCartString());
            if (shipmentCartItemModel.isEligibleNewShippingExperience()) {
                updateShippingCompletionTickerVisibility();
            }
        }
    }

    public ShipmentCartItemModel setSelectedCourier(int position, CourierItemData newCourierItemData, boolean isForceReload) {
        ShipmentCartItemModel shipmentCartItemModel = null;
        Object currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItemModel) {
            shipmentCartItemModel = (ShipmentCartItemModel) currentShipmentData;
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(null);
                shipmentCartItemModel.getSelectedShipmentDetailData().setOrderPriority(null);
                shipmentCartItemModel.setShippingBorderRed(false);
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
                shipmentCartItemModel.setShippingBorderRed(false);
                if (!newCourierItemData.isAllowDropshiper()) {
                    shipmentCartItemModel.getSelectedShipmentDetailData().setUseDropshipper(null);
                }
            }
            updateShipmentCostModel();
            checkDataForCheckout();
        }
        notifyItemChanged(getShipmentCostPosition());
        notifyItemChanged(position);
        int tmpPosition = isForceReload ? position : -1;
        if (shipmentCartItemModel != null && shipmentCartItemModel.isEligibleNewShippingExperience()) {
            checkHasSelectAllCourier(false, tmpPosition, shipmentCartItemModel.getCartString());
            updateShippingCompletionTickerVisibility();
        }

        return shipmentCartItemModel;
    }

    private void checkAppliedCourierPromo(int position, CourierItemData oldCourierItemData,
                                          CourierItemData newCourierItemData, ShipmentCartItemModel shipmentCartItemModel) {
        // Do this section if toggle year end promo is on
        if (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
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

    public void setShippingCourierViewModels(List<ShippingCourierUiModel> shippingCourierUiModels,
                                             CourierItemData recommendedCourier, int position) {
        for (ShippingCourierUiModel shippingCourierUiModel : shippingCourierUiModels) {
            shippingCourierUiModel.setSelected(false);
        }
        Object currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItemModel) {
            ShipmentCartItemModel cartItemModel = (ShipmentCartItemModel) currentShipmentData;
            if (cartItemModel.getSelectedShipmentDetailData() != null &&
                    cartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                for (ShippingCourierUiModel shippingCourierUiModel : shippingCourierUiModels) {
                    if (shippingCourierUiModel.getProductData().getShipperProductId() == recommendedCourier.getShipperProductId()) {
                        shippingCourierUiModel.setSelected(true);
                        break;
                    }
                }
                cartItemModel.getSelectedShipmentDetailData().setShippingCourierViewModels(shippingCourierUiModels);
            }
        }
    }

    public boolean checkHasSelectAllCourier(boolean passCheckShipmentFromPaymentClick,
                                            int lastSelectedCourierOrderIndex,
                                            String lastSelectedCourierOrdercartString) {
        int cartItemCounter = 0;
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                    if ((shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null && !shipmentAdapterActionListener.isTradeInByDropOff()) ||
                            (shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null && shipmentAdapterActionListener.isTradeInByDropOff())) {
                        cartItemCounter++;
                    }
                }
            }
            if (cartItemCounter == shipmentCartItemModelList.size()) {
                RequestData requestData = getRequestData(null, null, false);
                if (!passCheckShipmentFromPaymentClick) {
                    shipmentAdapterActionListener.onFinishChoosingShipment(lastSelectedCourierOrderIndex, lastSelectedCourierOrdercartString);
                }
                shipmentAdapterActionListener.updateCheckoutRequest(requestData.getCheckoutRequestData());
                return true;
            }
        }
        return false;
    }

    public void updateShipmentCostModel() {
        if (shipmentCostModel == null) return;
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
        int totalBookingFee = 0;
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


                if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() != null && (!((ShipmentCartItemModel) shipmentData).isError())) {
                    Boolean useInsurance = ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getUseInsurance();
                    Boolean isOrderPriority = ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().isOrderPriority();
                    boolean isTradeInPickup = shipmentAdapterActionListener.isTradeInByDropOff();
                    if (isTradeInPickup) {
                        if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getSelectedCourierTradeInDropOff() != null) {
                            shippingFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                    .getSelectedCourierTradeInDropOff().getShipperPrice();
                            if (useInsurance != null && useInsurance) {
                                insuranceFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                        .getSelectedCourierTradeInDropOff().getInsurancePrice();
                            }
                            if (isOrderPriority != null && isOrderPriority) {
                                orderPriorityFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                        .getSelectedCourierTradeInDropOff().getPriorityPrice();
                            }
                            additionalFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                    .getSelectedCourierTradeInDropOff().getAdditionalPrice();
                        } else {
                            shippingFee = 0;
                            insuranceFee = 0;
                            orderPriorityFee = 0;
                            additionalFee = 0;
                        }
                    } else if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getSelectedCourier() != null) {
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
                if (shipmentSingleAddressItem.getIsLeasingProduct()) {
                    totalBookingFee += shipmentSingleAddressItem.getBookingFee();
                }
            }
        }
        totalPrice = totalItemPrice + shippingFee + insuranceFee + orderPriorityFee + totalPurchaseProtectionPrice + additionalFee + totalBookingFee -
                shipmentCostModel.getShippingDiscountAmount() - shipmentCostModel.getProductDiscountAmount() - tradeInPrice;
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
        if (shipmentDonationModel != null && shipmentDonationModel.isChecked()) {
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

        shipmentCostModel.setBookingFee(totalBookingFee);
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

    public int getFirstShopPosition() {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof ShipmentCartItemModel) {
                return i;
            }
        }
        return 0;
    }

    public int getPromoCheckoutPosition() {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof LastApplyUiModel) {
                return i;
            }
        }
        return 0;
    }

    public void updateItemAndTotalCost(int position) {
        notifyItemChanged(getShipmentCostPosition());
        notifyItemChanged(position);
    }

    public void setPromoBenefit(List<SummariesItemUiModel> benefitSummaries) {
        if (shipmentCostModel != null) {
            for (SummariesItemUiModel summariesUiModel : benefitSummaries) {
                if (summariesUiModel.getType().equals(SummariesUiModel.getTYPE_DISCOUNT())) {
                    if (summariesUiModel.getDetails().size() > 0) {
                        shipmentCostModel.setHasDiscountDetails(true);
                        for (DetailsItemUiModel detailUiModel : summariesUiModel.getDetails()) {
                            if (detailUiModel.getType().equals(SummariesUiModel.getTYPE_SHIPPING_DISCOUNT())) {
                                shipmentCostModel.setShippingDiscountAmount(detailUiModel.getAmount());
                                shipmentCostModel.setShippingDiscountLabel(detailUiModel.getDescription());
                            } else if (detailUiModel.getType().equals(SummariesUiModel.getTYPE_PRODUCT_DISCOUNT())) {
                                shipmentCostModel.setProductDiscountAmount(detailUiModel.getAmount());
                                shipmentCostModel.setProductDiscountLabel(detailUiModel.getDescription());
                            }
                        }
                    } else {
                        shipmentCostModel.setHasDiscountDetails(false);
                        shipmentCostModel.setDiscountAmount(summariesUiModel.getAmount());
                        shipmentCostModel.setDiscountLabel(summariesUiModel.getDescription());
                    }
                } else if (summariesUiModel.getType().equals(SummariesUiModel.getTYPE_CASHBACK())) {
                    shipmentCostModel.setCashbackAmount(summariesUiModel.getAmount());
                    shipmentCostModel.setCashbackLabel(summariesUiModel.getDescription());
                }
            }
        }
    }

    public void resetPromoBenefit() {
        if (shipmentCostModel != null) {
            shipmentCostModel.setHasDiscountDetails(false);
            shipmentCostModel.setDiscountAmount(0);
            shipmentCostModel.setDiscountLabel("");
            shipmentCostModel.setShippingDiscountAmount(0);
            shipmentCostModel.setShippingDiscountLabel("");
            shipmentCostModel.setProductDiscountAmount(0);
            shipmentCostModel.setProductDiscountLabel("");
            shipmentCostModel.setCashbackAmount(0);
            shipmentCostModel.setCashbackLabel("");
        }
    }

    public void resetCourierPromoState() {
        if (shipmentCartItemModelList != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModelList) {
                shipmentCartItemModel.setStateHasLoadCourierState(false);
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

    public void setCourierPromoApplied(int position) {
        if (shipmentDataList.get(position) instanceof ShipmentCartItemModel) {
            ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentDataList.get(position);
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setCourierPromoApplied(true);
            }
        }
    }

    public RequestData getRequestData(RecipientAddressModel recipientAddressModel,
                                      List<ShipmentCartItemModel> shipmentCartItemModelList,
                                      boolean isAnalyticsPurpose) {
        RecipientAddressModel addressModel;
        if (recipientAddressModel != null) {
            addressModel = recipientAddressModel;
        } else {
            addressModel = this.recipientAddressModel;
        }
        if (shipmentCartItemModelList != null) {
            this.shipmentCartItemModelList = shipmentCartItemModelList;
        }
        return shipmentDataRequestConverter.generateRequestData(
                this.shipmentCartItemModelList, addressModel, isAnalyticsPurpose, shipmentAdapterActionListener.isTradeInByDropOff()
        );
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

    public static class RequestData {

        private List<DataCheckoutRequest> checkoutRequestData;

        @Inject
        public RequestData() {
            checkoutRequestData = new ArrayList<>();
        }

        public List<DataCheckoutRequest> getCheckoutRequestData() {
            return checkoutRequestData;
        }

        public void setCheckoutRequestData(List<DataCheckoutRequest> checkoutRequestData) {
            this.checkoutRequestData = checkoutRequestData;
        }

    }

    public List<ShipmentCartItemModel> getShipmentCartItemModelList() {
        return shipmentCartItemModelList;
    }

    public int getShipmentCartItemModelPosition(ShipmentCartItemModel shipmentCartItemModel) {
        return shipmentDataList.indexOf(shipmentCartItemModel);
    }

    public void updatePromoCheckoutData(PromoUiModel promoUiModel) {
        lastApplyUiModel = LastApplyUiMapper.INSTANCE.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel);
    }

    public void resetPromoCheckoutData() {
        lastApplyUiModel = new LastApplyUiModel();
    }
}