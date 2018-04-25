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
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemMultipleAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentRecipientAddressViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemSingleAddressViewHolder;
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

    // Adapter Data :
    // - Promo
    // - Promo Suggestion
    // - Address (For single address)
    // - Product List
    // - Price summary

    private static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details;
    private static final int ITEM_VIEW_CART = R.layout.item_shipment;
    private static final int ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS = 1;
    private static final int ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS = 2;

    private ArrayList<ShowCaseObject> showCaseObjectList;
    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    private List<ShipmentData> shipmentDataList;

    private CartItemPromoHolderData cartItemPromoHolderData;
    private CartPromoSuggestion cartPromoSuggestion;
    private RecipientAddressModel recipientAddressModel;
    private ShipmentCostModel shipmentCostModel;

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
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view, shipmentAdapterActionListener);
        } else if (viewType == ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            return new ShipmentItemSingleAddressViewHolder(view, parent.getContext(), shipmentAdapterActionListener);
        } else if (viewType == ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS) {
            return new ShipmentItemMultipleAddressViewHolder(view, parent.getContext(), shipmentAdapterActionListener);
        }
        throw new RuntimeException("No view holder type found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ShipmentData data = shipmentDataList.get(position);

        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            ((CartVoucherPromoViewHolder) holder).bindData((CartItemPromoHolderData) data, position);
            setShowCase(holder.itemView.getContext());
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionViewHolder) holder).bindData((CartPromoSuggestion) data, position);
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            ((ShipmentRecipientAddressViewHolder) holder).bindViewHolder((RecipientAddressModel) data,
                    showCaseObjectList);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) holder).bindViewHolder((ShipmentCostModel) data);
        } else if (viewType == ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS) {
            ((ShipmentItemSingleAddressViewHolder) holder).bindViewHolder(
                    (ShipmentSingleAddressItem) data, recipientAddressModel, showCaseObjectList);
        } else if (viewType == ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS) {
            ((ShipmentItemMultipleAddressViewHolder) holder).bindViewHolder((ShipmentMultipleAddressItem) data);
        }
    }

    @Override
    public int getItemCount() {
        return shipmentDataList.size();
    }

    private void setShowCase(Context context) {
        if (!ShowCasePreference.hasShown(context, SingleAddressShipmentFragment.class.getName())) {
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

    private void checkDataForCheckout() {
        boolean availableCheckout = true;
        for (Object object : shipmentDataList) {
            if (object instanceof CartSellerItemModel) {
                if (((CartSellerItemModel) object).getSelectedShipmentDetailData() == null ||
                        ((CartSellerItemModel) object).isError()) {
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
}