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
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressItem;
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

    private static final int FIRST_ELEMENT = 0;

    public ShipmentItemSingleAddressViewHolder(View itemView, Context context,
                                               ShipmentAdapterActionListener actionListener,
                                               ShipmentAdapter shipmentAdapter) {
        super(itemView, context, actionListener, shipmentAdapter);
    }

    @Override
    public void bindViewHolder(ShipmentItem shipmentItem,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        super.bindViewHolder(shipmentItem, recipientAddressModel, showCaseObjectList);

        addressLayout.setVisibility(View.GONE);

        ShipmentSingleAddressItem shipmentSingleAddressItem = (ShipmentSingleAddressItem) shipmentItem;
        List<CartItemModel> cartItemModelList = new ArrayList<>(shipmentSingleAddressItem.getCartItemModels());
        renderFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT));
        renderOtherCartItems(shipmentSingleAddressItem, cartItemModelList);

        setShowCase(llShipmentOptionViewLayout, showCaseObjectList);
    }

    private void renderFirstCartItem(CartItemModel cartItemModel) {
        ImageHandler.LoadImage(ivProductImage, cartItemModel.getImageUrl());
        tvProductName.setText(cartItemModel.getName());
        tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (int) cartItemModel.getPrice(), true));
        tvProductWeight.setText(cartItemModel.getWeightFmt());
        tvProductTotalItem.setText(String.valueOf(cartItemModel.getQuantity()));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItemModel.getNoteToSeller());
        llOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        tvOptionalNoteToSeller.setText(cartItemModel.getNoteToSeller());

        rlProductPoliciesLayout.setVisibility(isPoliciesVisible(cartItemModel) ?
                View.VISIBLE : View.GONE);
        ivFreeReturnIcon.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        tvFreeReturnLabel.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        tvPreOrder.setVisibility(cartItemModel.isPreOrder() ? View.VISIBLE : View.GONE);
        tvCashback.setVisibility(cartItemModel.isCashback() ? View.VISIBLE : View.GONE);
        String cashback = tvCashback.getContext().getString(R.string.label_cashback) + " " + cartItemModel.getCashback();
        tvCashback.setText(cashback);
    }

    private void renderOtherCartItems(ShipmentSingleAddressItem shipmentItem, List<CartItemModel> cartItemModels) {
        if (cartItemModels != null) {
            if (cartItemModels.size() > 0) {
                vSeparatorMultipleProductSameStore.setVisibility(View.VISIBLE);
            } else {
                vSeparatorMultipleProductSameStore.setVisibility(View.GONE);
            }
            rlExpandOtherProduct.setVisibility(cartItemModels.isEmpty() ?
                    View.GONE : View.VISIBLE);
            tvExpandOtherProduct.setText(getOtherCartItemsLabel(cartItemModels,
                    shipmentItem.isAllItemViewStateExpanded()));

            ivDetailOptionChevron.setImageResource(getResourceDrawerChevron(
                    shipmentItem.isAllItemViewStateExpanded()));
            rlExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem, cartItemModels));
            tvExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem, cartItemModels));

            initInnerRecyclerView(cartItemModels);
        }
    }

    private void initInnerRecyclerView(List<CartItemModel> cartItemList) {
//        rvCartItem.setVisibility(View.GONE);

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

    private String getOtherCartItemsLabel(List<CartItemModel> cartItemList,
                                          boolean isExpandAllProduct) {
        return isExpandAllProduct ? "Tutup" :
                String.format("+%s Produk Lainnya", cartItemList.size());
    }

    private View.OnClickListener showAllProductListener(final ShipmentItem shipmentItem,
                                                        final List<CartItemModel> cartItemList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleShowAllProduct(shipmentItem, cartItemList);
            }
        };
    }

    private void toggleShowAllProduct(ShipmentItem shipmentItem, List<CartItemModel> cartItemList) {
        shipmentItem.setAllItemViewStateExpanded(!shipmentItem.isAllItemViewStateExpanded());
        rvCartItem.setVisibility(shipmentItem.isAllItemViewStateExpanded() ? View.VISIBLE : View.GONE);

        tvExpandOtherProduct.setText(getOtherCartItemsLabel(cartItemList,
                shipmentItem.isAllItemViewStateExpanded()));
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
        tvTextProductWeight.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvProductWeight.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvLabelItemCount.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
        tvProductTotalItem.setTextColor(ContextCompat.getColor(context, R.color.grey_nonactive_text));
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
        tvTextProductWeight.setTextColor(ContextCompat.getColor(context, R.color.black_38));
        tvProductWeight.setTextColor(ContextCompat.getColor(context, R.color.font_black_secondary_54));
        tvLabelItemCount.setTextColor(ContextCompat.getColor(context, R.color.black_38));
        tvProductTotalItem.setTextColor(ContextCompat.getColor(context, R.color.font_black_secondary_54));
        tvNoteToSellerLabel.setTextColor(ContextCompat.getColor(context, R.color.black_38));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(context, R.color.black_70));
        tvCashback.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_bg_cashback));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        ivProductImage.setColorFilter(null);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

}