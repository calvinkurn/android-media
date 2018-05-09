package com.tokopedia.checkout.view.view.shipment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.view.shipment.converter.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentInsuranceTncViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemMultipleAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemSingleAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentRecipientAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentInsuranceTncItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressCartItem;
import com.tokopedia.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.checkout.view.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

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
    private List<ShipmentCartItem> shipmentCartItemList;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;
    private ShipmentInsuranceTncItem shipmentInsuranceTncItem;
    private ShipmentDataRequestConverter shipmentDataRequestConverter;

    private boolean hasShownShowCase;

    @Inject
    public ShipmentAdapter(ShipmentAdapterActionListener shipmentAdapterActionListener,
                           ShipmentDataRequestConverter shipmentDataRequestConverter) {
        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
        this.shipmentDataRequestConverter = shipmentDataRequestConverter;
        this.shipmentDataList = new ArrayList<>();
        this.showCaseObjectList = new ArrayList<>();
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
        } else if (item instanceof ShipmentSingleAddressCartItem) {
            return ShipmentItemSingleAddressViewHolder.ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS;
        } else if (item instanceof ShipmentMultipleAddressCartItem) {
            return ShipmentItemMultipleAddressViewHolder.ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS;
        } else if (item instanceof ShipmentCostModel) {
            return ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST;
        } else if (item instanceof ShipmentInsuranceTncItem) {
            return ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC;
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
        } else if (viewType == ShipmentItemSingleAddressViewHolder.ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            return new ShipmentItemSingleAddressViewHolder(view, parent.getContext(), shipmentAdapterActionListener, this);
        } else if (viewType == ShipmentItemMultipleAddressViewHolder.ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS) {
            return new ShipmentItemMultipleAddressViewHolder(view, parent.getContext(), shipmentAdapterActionListener, this);
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            return new ShipmentInsuranceTncViewHolder(view, shipmentAdapterActionListener);
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
                    showCaseObjectList);
        } else if (viewType == ShipmentItemSingleAddressViewHolder.ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            ((ShipmentItemSingleAddressViewHolder) holder).bindViewHolder(
                    (ShipmentSingleAddressCartItem) data, recipientAddressModel, showCaseObjectList);
            setShowCase(holder.itemView.getContext());
        } else if (viewType == ShipmentItemMultipleAddressViewHolder.ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS) {
            ((ShipmentItemMultipleAddressViewHolder) holder).bindViewHolder(
                    (ShipmentMultipleAddressCartItem) data, recipientAddressModel, showCaseObjectList);
        } else if (viewType == ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) holder).bindViewHolder((ShipmentCostModel) data);
        } else if (viewType == ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC) {
            ((ShipmentInsuranceTncViewHolder) holder).bindViewHolder((ShipmentInsuranceTncItem) data);
        }
    }

    @Override
    public int getItemCount() {
        return shipmentDataList.size();
    }

    private void setShowCase(Context context) {
        if (!hasShownShowCase && !ShowCasePreference.hasShown(context, SingleAddressShipmentFragment.class.getName())) {
            hasShownShowCase = true;
            createShowCaseDialog().show((Activity) context,
                    SingleAddressShipmentFragment.class.getName(),
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

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        this.cartItemPromoHolderData = cartItemPromoHolderData;
        shipmentDataList.add(cartItemPromoHolderData);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addPromoSuggestionData(CartPromoSuggestion cartPromoSuggestion) {
        this.cartPromoSuggestion = cartPromoSuggestion;
        shipmentDataList.add(cartPromoSuggestion);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addAddressShipmentData(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
        shipmentDataList.add(recipientAddressModel);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addCartItemDataList(List<ShipmentCartItem> shipmentCartItem) {
        this.shipmentCartItemList = shipmentCartItem;
        shipmentDataList.addAll(shipmentCartItem);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addShipmentCostData(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;
        shipmentDataList.add(shipmentCostModel);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addShipmentInsuranceTncData(ShipmentInsuranceTncItem shipmentInsuranceTncItem) {
        this.shipmentInsuranceTncItem = shipmentInsuranceTncItem;
        shipmentDataList.add(shipmentInsuranceTncItem);
        notifyDataSetChanged();
    }

    public void updateInsuranceTncVisibility() {
        shipmentInsuranceTncItem.setVisible(checkItemUseInsuranceExist());
        for (int i = 0; i < shipmentDataList.size(); i++) {
            if (shipmentDataList.get(i) instanceof ShipmentInsuranceTncItem) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    private boolean checkItemUseInsuranceExist() {
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItem) {
                ShipmentCartItem shipmentCartItem = (ShipmentCartItem) shipmentData;
                if (shipmentCartItem.getSelectedShipmentDetailData() != null &&
                        shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier() != null &&
                        shipmentCartItem.getSelectedShipmentDetailData().getUseInsurance() != null &&
                        shipmentCartItem.getSelectedShipmentDetailData().getUseInsurance()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateSelectedAddress(RecipientAddressModel recipientAddress) {
        for (ShipmentData item : shipmentDataList) {
            if (item instanceof RecipientAddressModel) {
                if (!((RecipientAddressModel) item).getId().equalsIgnoreCase(recipientAddress.getId())) {
                    int index = shipmentDataList.indexOf(item);
                    shipmentDataList.set(index, recipientAddress);
                    this.recipientAddressModel = recipientAddress;
                    resetCourier();
                    notifyDataSetChanged();
                    shipmentAdapterActionListener.resetTotalPrice();
                }
            }
        }
    }

    private void resetCourier() {
        for (ShipmentData item : shipmentDataList) {
            if (item instanceof ShipmentCartItem) {
                ((ShipmentCartItem) item).setSelectedShipmentDetailData(null);
            } else if (item instanceof ShipmentCostModel) {
                ((ShipmentCostModel) item).setAdditionalFee(0);
                ((ShipmentCostModel) item).setInsuranceFee(0);
                ((ShipmentCostModel) item).setShippingFee(0);
                ((ShipmentCostModel) item).setTotalPrice(0);
                ((ShipmentCostModel) item).setTotalItemPrice(0);
            }
        }
    }

    public RecipientAddressModel getAddressShipmentData() {
        return recipientAddressModel;
    }

    private void checkDataForCheckout() {
        boolean availableCheckout = true;
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentCartItem) {
                if (((ShipmentCartItem) shipmentData).getSelectedShipmentDetailData() == null ||
                        ((ShipmentCartItem) shipmentData).isError()) {
                    availableCheckout = false;
                }
            }
        }
        if (availableCheckout) {
            shipmentAdapterActionListener.onCartDataEnableToCheckout();
        } else {
            shipmentAdapterActionListener.onCartDataDisableToCheckout();
        }
    }

    public void checkDropshipperValidation() {
        boolean availableCheckout = true;
        int errorPosition = DEFAULT_ERROR_POSITION;
        for (int i = 0; i < shipmentDataList.size(); i++) {
            ShipmentData shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof ShipmentCartItem) {
                if (((ShipmentCartItem) shipmentData).getSelectedShipmentDetailData() != null &&
                        ((ShipmentCartItem) shipmentData).getSelectedShipmentDetailData().getUseDropshipper()) {
                    if (TextUtils.isEmpty(((ShipmentCartItem) shipmentData).getSelectedShipmentDetailData().getDropshipperName()) ||
                            TextUtils.isEmpty(((ShipmentCartItem) shipmentData).getSelectedShipmentDetailData().getDropshipperPhone())) {
                        availableCheckout = false;
                        errorPosition = i;
                    }
                }
            }
        }
        shipmentAdapterActionListener.onDropshipperValidationResult(availableCheckout, errorPosition);
    }

    public void setSelecteCourier(int position, CourierItemData courierItemData) {
        ShipmentData currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentCartItem) {
            if (((ShipmentCartItem) currentShipmentData).getSelectedShipmentDetailData() != null) {
                ((ShipmentCartItem) currentShipmentData).getSelectedShipmentDetailData().setUseInsurance(null);
                ((ShipmentCartItem) currentShipmentData).getSelectedShipmentDetailData().setSelectedCourier(courierItemData);
            } else {
                ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
                shipmentDetailData.setSelectedCourier(courierItemData);
                shipmentDetailData.setShipmentCartData(((ShipmentCartItem) currentShipmentData).getShipmentCartData());
                ((ShipmentCartItem) currentShipmentData).setSelectedShipmentDetailData(shipmentDetailData);
            }
            updateShipmentCostModel();
            checkDataForCheckout();
        }
        notifyItemChanged(getItemCount() - 1);
        notifyItemChanged(position);
        checkHasSelectAllCourier();
    }

    private void checkHasSelectAllCourier() {
        int cartItemCounter = 0;
        for (ShipmentCartItem shipmentCartItem : shipmentCartItemList) {
            if (shipmentCartItem.getSelectedShipmentDetailData() != null &&
                    shipmentCartItem.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                cartItemCounter++;
            }
        }
        if (cartItemCounter == shipmentCartItemList.size()) {
            RequestData requestData = getRequestPromoData();
            shipmentAdapterActionListener.onFinishChoosingShipment(requestData.getPromoRequestData(),
                    requestData.getCheckoutRequestData());
        }
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
            if (shipmentData instanceof ShipmentCartItem) {
                if (((ShipmentCartItem) shipmentData).getSelectedShipmentDetailData() != null) {
                    Boolean useInsurance = ((ShipmentCartItem) shipmentData).getSelectedShipmentDetailData().getUseInsurance();
                    if (shipmentData instanceof ShipmentSingleAddressCartItem) {
                        ShipmentSingleAddressCartItem shipmentSingleAddressItem =
                                (ShipmentSingleAddressCartItem) shipmentData;
                        List<CartItemModel> cartItemModels = shipmentSingleAddressItem.getCartItemModels();
                        for (CartItemModel cartItemModel : cartItemModels) {
                            totalWeight += (cartItemModel.getWeight() * cartItemModel.getQuantity());
                            totalItem += cartItemModel.getQuantity();
                            totalItemPrice += (cartItemModel.getPrice() * cartItemModel.getQuantity());
                        }
                        shippingFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getDeliveryPrice();
                        if (useInsurance != null && useInsurance) {
                            insuranceFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                    .getSelectedCourier().getInsurancePrice();
                        }
                        additionalFee += shipmentSingleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getAdditionalPrice();
                        // Additional fee is included in shipping fee
                        shippingFee += additionalFee;
                    } else {
                        ShipmentMultipleAddressCartItem shipmentMultipleAddressItem =
                                (ShipmentMultipleAddressCartItem) shipmentData;
                        totalWeight += (shipmentMultipleAddressItem.getMultipleAddressItemData().getProductRawWeight() *
                                Integer.parseInt(shipmentMultipleAddressItem.getMultipleAddressItemData().getProductQty()));
                        totalItem += Integer.parseInt(shipmentMultipleAddressItem.getMultipleAddressItemData().getProductQty());
                        shippingFee += shipmentMultipleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getDeliveryPrice();
                        if (useInsurance != null && useInsurance) {
                            insuranceFee += shipmentMultipleAddressItem.getSelectedShipmentDetailData()
                                    .getSelectedCourier().getInsurancePrice();
                        }
                        totalItemPrice += (shipmentMultipleAddressItem.getProductPriceNumber() *
                                Integer.parseInt(shipmentMultipleAddressItem.getMultipleAddressItemData().getProductQty()));
                        additionalFee += shipmentMultipleAddressItem.getSelectedShipmentDetailData()
                                .getSelectedCourier().getAdditionalPrice();
                        // Additional fee is included in shipping fee
                        shippingFee += additionalFee;
                    }
                }
            }
        }
        totalPrice = totalItemPrice + shippingFee + insuranceFee;
        shipmentCostModel.setTotalWeight(totalWeight);
        shipmentCostModel.setAdditionalFee(additionalFee);
        shipmentCostModel.setTotalItemPrice(totalItemPrice);
        shipmentCostModel.setTotalItem(totalItem);
        shipmentCostModel.setShippingFee(shippingFee);
        shipmentCostModel.setInsuranceFee(insuranceFee);
        shipmentCostModel.setTotalPrice(totalPrice);
        shipmentAdapterActionListener.onTotalPaymentChange(shipmentCostModel);
    }

    public void updateItemAndTotalCost(int position) {
        notifyItemChanged(getItemCount() - 1);
        notifyItemChanged(position);
    }

    public void updatePromo(CheckPromoCodeCartShipmentResult.DataVoucher dataVoucher) {
        if (dataVoucher != null) {
            shipmentCostModel.setPromoPrice(dataVoucher.getVoucherAmount());
            shipmentCostModel.setPromoMessage(dataVoucher.getVoucherPromoDesc());
            for (int i = 0; i < shipmentDataList.size(); i++) {
                ShipmentData itemAdapter = shipmentDataList.get(i);
                if (itemAdapter instanceof CartPromoSuggestion) {
                    ((CartPromoSuggestion) itemAdapter).setVisible(false);
                    notifyItemChanged(i);
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
                }
            }
        }
        notifyItemChanged(getItemCount() - 1);
    }

    public void updateItemPromoVoucher(CartItemPromoHolderData cartPromo) {
        for (int i = 0; i < shipmentDataList.size(); i++) {
            ShipmentData shipmentData = shipmentDataList.get(i);
            if (shipmentData instanceof CartItemPromoHolderData) {
                shipmentDataList.set(i, cartPromo);
                checkDataForCheckout();
                notifyItemChanged(i);
            } else if (shipmentData instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) shipmentData).setVisible(false);
                notifyItemChanged(i);
            }
        }
        notifyItemChanged(getItemCount() - 1);
    }

    public boolean hasSetAllCourier() {
        for (ShipmentData itemData : shipmentDataList) {
            if (itemData instanceof ShipmentCartItem) {
                if (((ShipmentCartItem) itemData).getSelectedShipmentDetailData() == null) {
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

    public RequestData getRequestPromoData() {
        return shipmentDataRequestConverter.generateRequestData(shipmentCartItemList, recipientAddressModel);
    }

    public void clearData() {
        shipmentDataList.clear();
        notifyDataSetChanged();
    }

    public static class RequestData {

        private List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData;
        private List<DataCheckoutRequest> checkoutRequestData;

        @Inject
        public RequestData() {
            promoRequestData = new ArrayList<>();
            checkoutRequestData = new ArrayList<>();
        }

        public List<CheckPromoCodeCartShipmentRequest.Data> getPromoRequestData() {
            return promoRequestData;
        }

        public void setPromoRequestData(List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData) {
            this.promoRequestData = promoRequestData;
        }

        List<DataCheckoutRequest> getCheckoutRequestData() {
            return checkoutRequestData;
        }

        public void setCheckoutRequestData(List<DataCheckoutRequest> checkoutRequestData) {
            this.checkoutRequestData = checkoutRequestData;
        }

    }

}