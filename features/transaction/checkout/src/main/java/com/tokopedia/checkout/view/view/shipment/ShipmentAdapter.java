package com.tokopedia.checkout.view.view.shipment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemMultipleAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemSingleAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentRecipientAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressItem;
import com.tokopedia.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.checkout.view.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details;
    private static final int ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS = R.layout.item_shipment_single;
    private static final int ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS = R.layout.item_shipment_multiple;

    private ArrayList<ShowCaseObject> showCaseObjectList;
    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    private List<ShipmentData> shipmentDataList;

    private CartItemPromoHolderData cartItemPromoHolderData;
    private CartPromoSuggestion cartPromoSuggestion;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;

    private boolean hasShownShowCase;

    public ShipmentAdapter(ShipmentAdapterActionListener shipmentAdapterActionListener) {
        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
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
            return ITEM_VIEW_RECIPIENT_ADDRESS;
        } else if (item instanceof ShipmentSingleAddressItem) {
            return ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS;
        } else if (item instanceof ShipmentMultipleAddressItem) {
            return ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS;
        } else if (item instanceof ShipmentCostModel) {
            return ITEM_VIEW_SHIPMENT_COST;
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
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            return new ShipmentRecipientAddressViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            return new ShipmentItemSingleAddressViewHolder(view, parent.getContext(), shipmentAdapterActionListener, this);
        } else if (viewType == ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS) {
            return new ShipmentItemMultipleAddressViewHolder(view, parent.getContext(), shipmentAdapterActionListener, this);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view, shipmentAdapterActionListener);
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
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            ((ShipmentRecipientAddressViewHolder) holder).bindViewHolder((RecipientAddressModel) data,
                    showCaseObjectList);
        } else if (viewType == ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            ((ShipmentItemSingleAddressViewHolder) holder).bindViewHolder(
                    (ShipmentSingleAddressItem) data, recipientAddressModel, showCaseObjectList);
            setShowCase(holder.itemView.getContext());
        } else if (viewType == ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS) {
            ((ShipmentItemMultipleAddressViewHolder) holder).bindViewHolder(
                    (ShipmentMultipleAddressItem) data, recipientAddressModel, showCaseObjectList);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) holder).bindViewHolder((ShipmentCostModel) data);
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
        shipmentDataList.add(cartItemPromoHolderData);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addPromoSuggestionData(CartPromoSuggestion cartPromoSuggestion) {
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

    public void addCartItemDataList(List<ShipmentItem> shipmentItem) {
        shipmentDataList.addAll(shipmentItem);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addShipmentCostData(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;
        shipmentDataList.add(shipmentCostModel);
        notifyDataSetChanged();
        checkDataForCheckout();
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
            if (item instanceof ShipmentItem) {
                ((ShipmentItem) item).setSelectedShipmentDetailData(null);
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
            if (shipmentData instanceof ShipmentItem) {
                if (((ShipmentItem) shipmentData).getSelectedShipmentDetailData() == null ||
                        ((ShipmentItem) shipmentData).isError()) {
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

    public void setSelecteCourier(int position, CourierItemData courierItemData) {
        ShipmentData currentShipmentData = shipmentDataList.get(position);
        if (currentShipmentData instanceof ShipmentItem) {
            if (((ShipmentItem) currentShipmentData).getSelectedShipmentDetailData() != null) {
                ((ShipmentItem) currentShipmentData).getSelectedShipmentDetailData().setUseInsurance(null);
                ((ShipmentItem) currentShipmentData).getSelectedShipmentDetailData().setSelectedCourier(courierItemData);
            } else {
                ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
                shipmentDetailData.setSelectedCourier(courierItemData);
                shipmentDetailData.setShipmentCartData(((ShipmentItem) currentShipmentData).getShipmentCartData());
                ((ShipmentItem) currentShipmentData).setSelectedShipmentDetailData(shipmentDetailData);
            }
            updateShipmentCostModel(position);
            checkDataForCheckout();
        }
        notifyItemChanged(getItemCount() - 1);
        notifyItemChanged(position);
    }

    public void updateShipmentCostModel(int position) {
        double totalWeight = 0;
        double totalPrice = 0;
        double additionalFee = 0;
        double totalItemPrice = 0;
        int totalItem = 0;
        double shippingFee = 0;
        double insuranceFee = 0;
        for (ShipmentData shipmentData : shipmentDataList) {
            if (shipmentData instanceof ShipmentItem) {
                if (((ShipmentItem) shipmentData).getSelectedShipmentDetailData() != null) {
                    Boolean useInsurance = ((ShipmentItem) shipmentData).getSelectedShipmentDetailData().getUseInsurance();
                    if (shipmentData instanceof ShipmentSingleAddressItem) {
                        ShipmentSingleAddressItem shipmentSingleAddressItem =
                                (ShipmentSingleAddressItem) shipmentData;
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
                        ShipmentMultipleAddressItem shipmentMultipleAddressItem =
                                (ShipmentMultipleAddressItem) shipmentData;
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

}