package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
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
                                                 ShipmentAdapterActionListener actionListener) {
        super(itemView, context, actionListener);
    }

    @Override
    public void bindViewHolder(ShipmentItem shipmentItem,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {
        super.bindViewHolder(shipmentItem, recipientAddressModel, showCaseObjectList);
        rlExpandOtherProduct.setVisibility(View.GONE);

        ShipmentMultipleAddressItem shipmentMultipleAddressItem = (ShipmentMultipleAddressItem) shipmentItem;
        MultipleAddressItemData multipleAddressItemData = shipmentMultipleAddressItem.getMultipleAddressItemData();

        bindItem(shipmentMultipleAddressItem, multipleAddressItemData);

        bindAddress(multipleAddressItemData);
    }

    private void bindItem(ShipmentMultipleAddressItem shipmentMultipleAddressItem, MultipleAddressItemData multipleAddressItemData) {
        ImageHandler.LoadImage(ivProductImage, shipmentMultipleAddressItem.getProductImageUrl());
        tvProductName.setText(shipmentMultipleAddressItem.getProductName());
        tvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (int) shipmentMultipleAddressItem.getProductPriceNumber(), true));
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

    private void bindAddress(MultipleAddressItemData multipleAddressItemData) {
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

//    private void bindCostDetail(ShipmentMultipleAddressItem shipmentMultipleAddressItem) {
//        rlCartSubTotal.setVisibility(View.VISIBLE);
//        rlShipmentCost.setVisibility(shipmentMultipleAddressItem.isDetailSubtotalViewStateExpanded() ? View.VISIBLE : View.GONE);
//
//        int totalItem = 0;
//        double totalWeight = 0;
//        int shippingPrice = 0;
//        int insurancePrice = 0;
//        int additionalPrice = 0;
//        int subTotalPrice = 0;
//        int totalItemPrice = 0;
//
//    }

    private boolean isPoliciesVisible(ShipmentMultipleAddressItem shipmentMultipleAddressItem) {
        return shipmentMultipleAddressItem.isProdustHasCasback()
                || shipmentMultipleAddressItem.isProductIsFreeReturns()
                || shipmentMultipleAddressItem.isProductIsPreorder();
    }

}
