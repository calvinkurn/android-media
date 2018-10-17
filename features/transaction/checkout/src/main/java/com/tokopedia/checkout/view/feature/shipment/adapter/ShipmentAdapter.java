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
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.ShipmentData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentFragment;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.checkout.view.feature.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentDonationViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentInsuranceTncViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentItemViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentRecipientAddressViewHolder;
import com.tokopedia.checkout.view.common.viewholder.ShipmentSellerCashbackViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentInsuranceTncModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentSellerCashbackModel;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
import com.tokopedia.checkout.view.common.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.common.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int DEFAULT_ERROR_POSITION = -1;

    private ArrayList<ShowCaseObject> showCaseObjectList;
    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    private List<ShipmentData> shipmentDataList;

    private CartItemPromoHolderData cartItemPromoHolderData;
    private CartPromoSuggestion cartPromoSuggestion;
    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private ShipmentInsuranceTncModel shipmentInsuranceTncModel;
    private ShipmentSellerCashbackModel shipmentSellerCashbackModel;
    private ShipmentDonationModel shipmentDonationModel;

    private ShipmentDataRequestConverter shipmentDataRequestConverter;
    private RatesDataConverter ratesDataConverter;

    private boolean hasShownShowCase;
    private int lastChooseCourierItemPosition;
    private String cartIds;
    private int lastServiceId;

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

    @Override
    public int getItemViewType(int position) {
        ShipmentData item = shipmentDataList.get(position);

        if (item instanceof CartItemPromoHolderData) {
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
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
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
        }
        throw new RuntimeException("No view holder type found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ShipmentData data = shipmentDataList.get(position);

        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            ((CartVoucherPromoViewHolder) holder).bindData((CartItemPromoHolderData) data, position);
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
            ((ShipmentCostViewHolder) holder).bindViewHolder(
                    (ShipmentCostModel) data,
                    cartItemPromoHolderData
            );
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            ((ShipmentInsuranceTncViewHolder) holder).bindViewHolder((ShipmentInsuranceTncModel) data);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            ((ShipmentSellerCashbackViewHolder) holder).bindViewHolder(shipmentSellerCashbackModel);
        } else if (viewType == ShipmentDonationViewHolder.ITEM_VIEW_DONATION) {
            ((ShipmentDonationViewHolder) holder).bindViewHolder(shipmentDonationModel);
        }
    }

    @Override
    public int getItemCount() {
        return shipmentDataList.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ShipmentItemViewHolder) {
            ((ShipmentItemViewHolder) holder).unsubscribeDebouncer();
        }
    }

    private void setShowCase(Context context) {
        if (!hasShownShowCase && !ShowCasePreference.hasShown(context, ShipmentFragment.class.getName())) {
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
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    public void clearData() {
        shipmentDataList.clear();
        cartItemPromoHolderData = null;
        cartPromoSuggestion = null;
        shipmentCartItemModelList = null;
        recipientAddressModel = null;
        shipmentCostModel = null;
        shipmentInsuranceTncModel = null;
        shipmentSellerCashbackModel = null;
        shipmentDonationModel = null;
        notifyDataSetChanged();
    }

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        this.cartItemPromoHolderData = cartItemPromoHolderData;
        shipmentDataList.add(cartItemPromoHolderData);
    }

    public void addPromoSuggestionData(CartPromoSuggestion cartPromoSuggestion) {
        if (!TextUtils.isEmpty(cartPromoSuggestion.getPromoCode())) {
            this.cartPromoSuggestion = cartPromoSuggestion;
            shipmentDataList.add(cartPromoSuggestion);
        }
    }

    public void addAddressShipmentData(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
        shipmentDataList.add(recipientAddressModel);
    }

    public void addCartItemDataList(List<ShipmentCartItemModel> shipmentCartItemModel) {
        this.shipmentCartItemModelList = shipmentCartItemModel;
        shipmentDataList.addAll(shipmentCartItemModel);
        checkDataForCheckout();
    }

    public void addShipmentCostData(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;
        shipmentDataList.add(shipmentCostModel);
        updateShipmentCostModel();
    }

    public void addShipmentDonationModel(ShipmentDonationModel shipmentDonationModel) {
        this.shipmentDonationModel = shipmentDonationModel;
        shipmentDataList.add(shipmentDonationModel);
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
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel shipmentCartItemModel = (ShipmentCartItemModel) shipmentData;
                if (shipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance() != null &&
                        shipmentCartItemModel.getSelectedShipmentDetailData().getUseInsurance()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateSelectedAddress(RecipientAddressModel newlySelectedAddress) {
        int addressIndex = 0;
        for (ShipmentData item : shipmentDataList) {
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

    private void resetCourier() {
        setLastServiceId(0);
        for (ShipmentData item : shipmentDataList) {
            if (item instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) item).getSelectedShipmentDetailData() != null) {
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setSelectedShipment(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setSelectedCourier(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setUseDropshipper(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setDropshipperPhone(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setDropshipperName(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setUseInsurance(null);
                    ((ShipmentCartItemModel) item).getSelectedShipmentDetailData().setUsePartialOrder(false);
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
        for (ShipmentData shipmentData : shipmentDataList) {
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

    public void checkDropshipperValidation() {
        boolean hasSelectAllCourier = checkHasSelectAllCourier();
        if (hasSelectAllCourier) {
            boolean availableCheckout = true;
            int errorPosition = DEFAULT_ERROR_POSITION;
            ShipmentData selectedShipmentData = null;
            for (int i = 0; i < shipmentDataList.size(); i++) {
                ShipmentData shipmentData = shipmentDataList.get(i);
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
                            selectedShipmentData = shipmentData;
                        }
                    }
                }
            }

            shipmentAdapterActionListener.onDropshipperValidationResult(availableCheckout, selectedShipmentData, errorPosition);
        } else {
            shipmentAdapterActionListener.onDropshipperValidationResult(false, null, 0);
        }
    }

    public ShipmentCartItemModel setSelectedCourier(int position, CourierItemData newCourierItemData) {
        ShipmentCartItemModel shipmentCartItemModel = null;
        ShipmentData currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItemModel) {
            shipmentCartItemModel = (ShipmentCartItemModel) currentShipmentData;
            if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                shipmentCartItemModel.getSelectedShipmentDetailData().setUseInsurance(null);
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
        checkHasSelectAllCourier();

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
                        if (model.getSelectedShipmentDetailData().isCourierPromoApplied()) {
                            courierPromoStillExist = true;
                            break;
                        }
                    }
                }
                // If courier promo not exist anymore, cancel promo
                if (!courierPromoStillExist) {
                    cancelAutoApplyCoupon();
                    updatePromo(null);
                    shipmentAdapterActionListener.onCourierPromoCanceled(oldCourierItemData.getName());
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
                                             int position) {
        ShipmentData currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItemModel) {
            ShipmentCartItemModel cartItemModel = (ShipmentCartItemModel) currentShipmentData;
            if (cartItemModel.getSelectedShipmentDetailData() != null &&
                    cartItemModel.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                cartItemModel.getSelectedShipmentDetailData().setShippingCourierViewModels(shippingCourierViewModels);
            }
        }
    }

    public boolean checkHasSelectAllCourier() {
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
                shipmentAdapterActionListener.onFinishChoosingShipment(requestData.getPromoRequestData(),
                        requestData.getCheckoutRequestData());
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
        int totalItem = 0;
        double shippingFee = 0;
        double insuranceFee = 0;
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItemModel) {
                ShipmentCartItemModel shipmentSingleAddressItem =
                        (ShipmentCartItemModel) shipmentData;
                List<CartItemModel> cartItemModels = shipmentSingleAddressItem.getCartItemModels();
                for (CartItemModel cartItemModel : cartItemModels) {
                    totalWeight += (cartItemModel.getWeight() * cartItemModel.getQuantity());
                    totalItem += cartItemModel.getQuantity();
                    totalItemPrice += (cartItemModel.getPrice() * cartItemModel.getQuantity());
                }

                if (((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData() != null &&
                        ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getSelectedCourier() != null) {
                    Boolean useInsurance = ((ShipmentCartItemModel) shipmentData).getSelectedShipmentDetailData().getUseInsurance();
                    shippingFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperPrice();
                    if (useInsurance != null && useInsurance) {
                        insuranceFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getInsurancePrice();
                    }
                    additionalFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                            .getSelectedCourier().getAdditionalPrice();
                }
            }
        }
        totalPrice = totalItemPrice + shippingFee + insuranceFee + additionalFee - shipmentCostModel.getPromoPrice();
        shipmentCostModel.setTotalWeight(totalWeight);
        shipmentCostModel.setAdditionalFee(additionalFee);
        shipmentCostModel.setTotalItemPrice(totalItemPrice);
        shipmentCostModel.setTotalItem(totalItem);
        shipmentCostModel.setShippingFee(shippingFee);
        shipmentCostModel.setInsuranceFee(insuranceFee);
        if (shipmentDonationModel.isChecked()) {
            shipmentCostModel.setDonation(shipmentDonationModel.getDonation().getNominal());
        } else {
            if (shipmentCostModel.getDonation() > 0) {
                shipmentCostModel.setDonation(0);
            }
        }
        totalPrice += shipmentCostModel.getDonation();
        shipmentCostModel.setTotalPrice(totalPrice);
        updateCheckoutButtonData(null);
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

    public void updatePromo(PromoCodeCartShipmentData.DataVoucher dataVoucher) {
        if (dataVoucher != null) {
            shipmentCostModel.setPromoPrice(dataVoucher.getVoucherAmount());
            shipmentCostModel.setPromoMessage(dataVoucher.getVoucherPromoDesc());
            for (int i = 0; i < shipmentDataList.size(); i++) {
                ShipmentData itemAdapter = shipmentDataList.get(i);
                if (itemAdapter instanceof CartPromoSuggestion) {
                    ((CartPromoSuggestion) itemAdapter).setVisible(false);
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof CartItemPromoHolderData) {
                    ((CartItemPromoHolderData) itemAdapter).setVisible(false);
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof RecipientAddressModel) {
                    ((RecipientAddressModel) itemAdapter).setStateExtraPaddingTop(true);
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof ShipmentCartItemModel) {
                    updateFirstInvoiceItemMargin(i, (ShipmentCartItemModel) itemAdapter, true);
                }
            }
        } else {
            shipmentCostModel.setPromoPrice(0);
            shipmentCostModel.setPromoMessage(null);
            for (int i = 0; i < shipmentDataList.size(); i++) {
                ShipmentData itemAdapter = shipmentDataList.get(i);
                if (itemAdapter instanceof CartItemPromoHolderData) {
                    ((CartItemPromoHolderData) itemAdapter).setPromoNotActive();
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof CartPromoSuggestion) {
                    ((CartPromoSuggestion) itemAdapter).setVisible(true);
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof RecipientAddressModel) {
                    ((RecipientAddressModel) itemAdapter).setStateExtraPaddingTop(false);
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof ShipmentCartItemModel) {
                    updateFirstInvoiceItemMargin(i, (ShipmentCartItemModel) itemAdapter, false);
                }
            }
        }
        updateShipmentCostModel();
        notifyItemChanged(getShipmentCostPosition());
    }

    private void updateFirstInvoiceItemMargin(int iteration, ShipmentCartItemModel shipmentCartItemModel,
                                              boolean hasExtraMarginTop) {
        if (shipmentCartItemModel.getRecipientAddressModel() != null && shipmentCartItemModelList != null &&
                shipmentCartItemModelList.get(0) != null &&
                shipmentCartItemModel.getShopId() == shipmentCartItemModelList.get(0).getShopId()) {
            shipmentCartItemModel.setStateHasExtraMarginTop(hasExtraMarginTop);
            notifyItemChanged(iteration);
        }
    }

    public void updateItemPromoVoucher(CartItemPromoHolderData cartPromo) {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            ShipmentData shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof CartItemPromoHolderData) {
                shipmentDataList.set(i, cartPromo);
                cartItemPromoHolderData = cartPromo;
                checkDataForCheckout();
                checkDataForCheckout();
                notifyItemChanged(i);
            } else if (shipmentData instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) shipmentData).setVisible(false);
                notifyItemChanged(i);
            }
        }
        notifyItemChanged(getShipmentCostPosition());
    }

    public void cancelAutoApplyCoupon() {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            ShipmentData shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof CartItemPromoHolderData) {
                ((CartItemPromoHolderData) shipmentData).setPromoNotActive();
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
        for (ShipmentData itemData : shipmentDataList) {
            if (itemData instanceof ShipmentCartItemModel) {
                if (((ShipmentCartItemModel) itemData).getSelectedShipmentDetailData() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasAppliedPromoCode() {
        for (ShipmentData itemAdapter : shipmentDataList) {
            if (itemAdapter instanceof CartItemPromoHolderData) {
                return ((CartItemPromoHolderData) itemAdapter).getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER ||
                        ((CartItemPromoHolderData) itemAdapter).getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON;
            }
        }
        return false;
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
        if (shipmentDataList.get(index) instanceof ShipmentCartItemModel) {
            return (ShipmentCartItemModel) shipmentDataList.get(index);
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

}