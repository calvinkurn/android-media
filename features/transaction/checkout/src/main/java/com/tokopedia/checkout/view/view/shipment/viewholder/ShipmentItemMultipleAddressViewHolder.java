package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItem;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

/**
 * Created by kris on 3/14/18. Tokopedia
 * Modified by Irfan
 */

public class ShipmentItemMultipleAddressViewHolder extends ShipmentItemViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_MULTIPLE_ADDRESS = R.layout.item_shipment_multiple;

    public ShipmentItemMultipleAddressViewHolder(View itemView, Context context,
                                                 ShipmentAdapterActionListener actionListener,
                                                 ShipmentAdapter shipmentAdapter) {
        super(itemView, context, actionListener, shipmentAdapter);
    }

    @Override
    public void bindViewHolder(ShipmentCartItem shipmentCartItem,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        super.bindViewHolder(shipmentCartItem, recipientAddressModel, showCaseObjectList);
        rlExpandOtherProduct.setVisibility(View.GONE);

        ShipmentMultipleAddressCartItem shipmentMultipleAddressItem = (ShipmentMultipleAddressCartItem) shipmentCartItem;
        MultipleAddressItemData multipleAddressItemData = shipmentMultipleAddressItem.getMultipleAddressItemData();

        renderItem(shipmentMultipleAddressItem, multipleAddressItemData);
        renderAddress(multipleAddressItemData);
    }

    private void renderItem(ShipmentMultipleAddressCartItem shipmentMultipleAddressItem, MultipleAddressItemData multipleAddressItemData) {
        ImageHandler.LoadImage(ivProductImage, shipmentMultipleAddressItem.getProductImageUrl());
        tvProductName.setText(shipmentMultipleAddressItem.getProductName());
        tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                shipmentMultipleAddressItem.getProductPriceNumber(), true));
        tvItemCountAndWeight.setText(String.format(tvItemCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(multipleAddressItemData.getProductQty()),
                getFormattedWeight(multipleAddressItemData.getProductRawWeight() *
                                Integer.parseInt(multipleAddressItemData.getProductQty()))));

        boolean isEmptyNotes = TextUtils.isEmpty(multipleAddressItemData.getProductNotes());
        llOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        tvOptionalNoteToSeller.setText(multipleAddressItemData.getProductNotes());

        llProductPoliciesLayout.setVisibility(isPoliciesVisible(shipmentMultipleAddressItem) ?
                View.VISIBLE : View.GONE);
        ivFreeReturnIcon.setVisibility(shipmentMultipleAddressItem.isProductIsFreeReturns() ? View.VISIBLE : View.GONE);
        tvFreeReturnLabel.setVisibility(shipmentMultipleAddressItem.isProductIsFreeReturns() ? View.VISIBLE : View.GONE);
        tvPreOrder.setVisibility(shipmentMultipleAddressItem.isProductIsPreorder() ? View.VISIBLE : View.GONE);
        tvCashback.setVisibility(shipmentMultipleAddressItem.isProdustHasCasback() ? View.VISIBLE : View.GONE);
        String cashback = tvCashback.getContext().getString(R.string.label_cashback) + " " + shipmentMultipleAddressItem.getCashback();
        tvCashback.setText(cashback);
    }

    private void renderAddress(MultipleAddressItemData multipleAddressItemData) {
        String fullAddress = multipleAddressItemData.getAddressStreet()
                + ", " + multipleAddressItemData.getAddressCityName()
                + ", " + multipleAddressItemData.getAddressProvinceName()
                + ", " + multipleAddressItemData.getRecipientPhoneNumber();
        tvAddressName.setText(multipleAddressItemData.getAddressTitle());
        tvRecipientName.setText(multipleAddressItemData.getAddressReceiverName());
        tvRecipientAddress.setText(fullAddress);
        tvRecipientPhone.setText(multipleAddressItemData.getRecipientPhoneNumber());
        tvChangeAddress.setVisibility(View.GONE);
    }

    private boolean isPoliciesVisible(ShipmentMultipleAddressCartItem shipmentMultipleAddressItem) {
        return shipmentMultipleAddressItem.isProdustHasCasback()
                || shipmentMultipleAddressItem.isProductIsFreeReturns()
                || shipmentMultipleAddressItem.isProductIsPreorder();
    }

}
