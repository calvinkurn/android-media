package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.view.adapter.InnerProductListAdapter;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressCartItem;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Original @author Aghny A. Putra on 02/03/18
 * Modified by Irfan
 */

public class ShipmentItemSingleAddressViewHolder extends ShipmentItemViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_SINGLE_ADDRESS = R.layout.item_shipment_single;

    private static final int FIRST_ELEMENT = 0;

    public ShipmentItemSingleAddressViewHolder(View itemView, Context context,
                                               ShipmentAdapterActionListener actionListener,
                                               ShipmentAdapter shipmentAdapter) {
        super(itemView, context, actionListener, shipmentAdapter);
    }

    @Override
    public void bindViewHolder(ShipmentCartItem shipmentCartItem,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        super.bindViewHolder(shipmentCartItem, recipientAddressModel, showCaseObjectList);

        addressLayout.setVisibility(View.GONE);

        ShipmentSingleAddressCartItem shipmentSingleAddressItem = (ShipmentSingleAddressCartItem) shipmentCartItem;
        List<CartItemModel> cartItemModelList = new ArrayList<>(shipmentSingleAddressItem.getCartItemModels());
        renderFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT));
        if (shipmentSingleAddressItem.getCartItemModels() != null && shipmentSingleAddressItem.getCartItemModels().size() > 1) {
            rlExpandOtherProduct.setVisibility(View.VISIBLE);
            renderOtherCartItems(shipmentSingleAddressItem, cartItemModelList);
        } else {
            rlExpandOtherProduct.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
        }

        setShowCase(llShipmentOptionViewLayout, showCaseObjectList);
    }

    private void renderFirstCartItem(CartItemModel cartItemModel) {
        ImageHandler.LoadImage(ivProductImage, cartItemModel.getImageUrl());
        tvProductName.setText(cartItemModel.getName());
        tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (int) cartItemModel.getPrice(), true));
        tvItemCountAndWeight.setText(String.format(tvItemCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItemModel.getQuantity()),
                getFormattedWeight(cartItemModel.getWeight() * cartItemModel.getQuantity())));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItemModel.getNoteToSeller());
        llOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        tvOptionalNoteToSeller.setText(cartItemModel.getNoteToSeller());

        llProductPoliciesLayout.setVisibility(isPoliciesVisible(cartItemModel) ?
                View.VISIBLE : View.GONE);
        ivFreeReturnIcon.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        tvFreeReturnLabel.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        tvPreOrder.setVisibility(cartItemModel.isPreOrder() ? View.VISIBLE : View.GONE);
        tvCashback.setVisibility(cartItemModel.isCashback() ? View.VISIBLE : View.GONE);
        String cashback = tvCashback.getContext().getString(R.string.label_cashback) + " " + cartItemModel.getCashback();
        tvCashback.setText(cashback);
    }

    private void renderOtherCartItems(ShipmentSingleAddressCartItem shipmentItem, List<CartItemModel> cartItemModels) {
        rlExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem));
        tvExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem));
        initInnerRecyclerView(cartItemModels);
        if (shipmentItem.isStateAllItemViewExpanded()) {
            rvCartItem.setVisibility(View.VISIBLE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(R.string.label_hide_other_item);
        } else {
            rvCartItem.setVisibility(View.GONE);
            vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            tvExpandOtherProduct.setText(String.format(context.getString(R.string.label_other_item_count_format),
                    String.valueOf(cartItemModels.size())));
        }
    }

    private void initInnerRecyclerView(List<CartItemModel> cartItemList) {
        rvCartItem.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCartItem.setLayoutManager(layoutManager);

        InnerProductListAdapter innerProductListAdapter =
                new InnerProductListAdapter(cartItemList);
        rvCartItem.setAdapter(innerProductListAdapter);
    }

    private boolean isPoliciesVisible(CartItemModel cartItemModel) {
        return cartItemModel.isCashback()
                || cartItemModel.isFreeReturn()
                || cartItemModel.isPreOrder();
    }

    private View.OnClickListener showAllProductListener(final ShipmentCartItem shipmentCartItem) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shipmentCartItem.setStateAllItemViewExpanded(!shipmentCartItem.isStateAllItemViewExpanded());
                mActionListener.onViewVisibilityStateChanged(getAdapterPosition());
            }
        };
    }

    private void setShowCase(ViewGroup viewGroup, ArrayList<ShowCaseObject> showCaseObjectList) {
        showCaseObjectList.add(new ShowCaseObject(viewGroup,
                "Pilih Kurir Pengiriman",
                "Gunakan layanan jasa pengiriman yang didukung oleh\ntoko ini.",
                ShowCaseContentPosition.UNDEFINED)
        );
    }

    private void showShipmentWarning(String message) {
        imgShippingWarning.setImageResource(R.drawable.ic_warning_red);
        tvShippingWarning.setText(message);
        llShippingWarningContainer.setVisibility(View.VISIBLE);
        disableItemView();
    }

    private void hideShipmentWarning() {
        llShippingWarningContainer.setVisibility(View.GONE);
        enableItemView();
    }

    private void disableItemView() {
        tvProductName.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvProductPrice.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvFreeReturnLabel.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvPreOrder.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvNoteToSellerLabel.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvCashback.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        setImageFilterGrayScale();
    }

    private void setImageFilterGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        ivProductImage.setColorFilter(cf);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
    }

    private void enableItemView() {
        tvProductName.setTextColor(ContextCompat.getColor(context, R.color.black_70));
        tvProductPrice.setTextColor(ContextCompat.getColor(context, R.color.orange_red));
        tvFreeReturnLabel.setTextColor(ContextCompat.getColor(context, R.color.font_black_secondary_54));
        tvPreOrder.setTextColor(ContextCompat.getColor(context, R.color.font_black_secondary_54));
        tvNoteToSellerLabel.setTextColor(ContextCompat.getColor(context, R.color.black_38));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(context, R.color.black_70));
        tvCashback.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_cashback));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        ivProductImage.setColorFilter(null);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

}