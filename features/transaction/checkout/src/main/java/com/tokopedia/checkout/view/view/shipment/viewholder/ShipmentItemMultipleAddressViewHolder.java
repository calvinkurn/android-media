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
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressItem;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

/**
 * Created by kris on 3/14/18. Tokopedia
 * Modified by Irfan
 */

public class ShipmentItemMultipleAddressViewHolder extends ShipmentItemViewHolder {

    public ShipmentItemMultipleAddressViewHolder(View itemView, Context context,
                                                 ShipmentAdapterActionListener actionListener,
                                                 ShipmentAdapter shipmentAdapter) {
        super(itemView, context, actionListener, shipmentAdapter);
    }

    @Override
    public void bindViewHolder(ShipmentItem shipmentItem,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        super.bindViewHolder(shipmentItem, recipientAddressModel, showCaseObjectList);
        rlExpandOtherProduct.setVisibility(View.GONE);

        ShipmentMultipleAddressItem shipmentMultipleAddressItem = (ShipmentMultipleAddressItem) shipmentItem;
        MultipleAddressItemData multipleAddressItemData = shipmentMultipleAddressItem.getMultipleAddressItemData();

        renderItem(shipmentMultipleAddressItem, multipleAddressItemData);
        renderAddress(multipleAddressItemData);
    }

    private void renderItem(ShipmentMultipleAddressItem shipmentMultipleAddressItem, MultipleAddressItemData multipleAddressItemData) {
        ImageHandler.LoadImage(ivProductImage, shipmentMultipleAddressItem.getProductImageUrl());
        tvProductName.setText(shipmentMultipleAddressItem.getProductName());
        tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                shipmentMultipleAddressItem.getProductPriceNumber(), true));
        tvProductWeight.setText(multipleAddressItemData.getProductWeight());
        tvProductTotalItem.setText(String.valueOf(multipleAddressItemData.getProductQty()));

        boolean isEmptyNotes = TextUtils.isEmpty(multipleAddressItemData.getProductNotes());
        llOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        tvOptionalNoteToSeller.setText(multipleAddressItemData.getProductNotes());

        rlProductPoliciesLayout.setVisibility(isPoliciesVisible(shipmentMultipleAddressItem) ?
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

    private boolean isPoliciesVisible(ShipmentMultipleAddressItem shipmentMultipleAddressItem) {
        return shipmentMultipleAddressItem.isProdustHasCasback()
                || shipmentMultipleAddressItem.isProductIsFreeReturns()
                || shipmentMultipleAddressItem.isProductIsPreorder();
    }

}
