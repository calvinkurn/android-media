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
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.adapter.InnerProductListAdapter;
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

    private Context mContext;
    private ShipmentAdapterActionListener mActionListener;

    public ShipmentItemSingleAddressViewHolder(View itemView, Context context,
                                               ShipmentAdapterActionListener actionListener) {
        super(itemView);
        mContext = context;
        mActionListener = actionListener;
    }

    @Override
    public void bindViewHolder(ShipmentItem shipmentItem,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {

        addressLayout.setVisibility(View.GONE);

        ShipmentSingleAddressItem shipmentSingleAddressItem = (ShipmentSingleAddressItem) shipmentItem;

        List<CartItemModel> cartItemModelList = new ArrayList<>(shipmentSingleAddressItem.getCartItemModels());

        bindFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT));
        bindOtherCartItems(shipmentSingleAddressItem, cartItemModelList);
        bindChooseCourier(shipmentSingleAddressItem, shipmentSingleAddressItem.getSelectedShipmentDetailData(),
                recipientAddressModel);
        bindCostDetail(shipmentSingleAddressItem);
        bindError(shipmentSingleAddressItem);
        bindWarnings(shipmentSingleAddressItem);

        setShowCase(llShipmentOptionViewLayout, showCaseObjectList);
    }

    private void bindFirstCartItem(CartItemModel cartItemModel) {
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

    private void bindOtherCartItems(ShipmentSingleAddressItem shipmentItem, List<CartItemModel> cartItemModels) {
        if (cartItemModels != null) {
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

    private void bindChooseCourier(ShipmentSingleAddressItem shipmentSingleAddressItem,
                                   ShipmentDetailData shipmentDetailData,
                                   RecipientAddressModel recipientAddressModel) {
        chooseCourierButton.setOnClickListener(selectShippingOptionListener(getAdapterPosition(),
                shipmentSingleAddressItem, recipientAddressModel));
        llSelectedCourier.setOnClickListener(selectShippingOptionListener(getAdapterPosition(),
                shipmentSingleAddressItem, recipientAddressModel));

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        chooseCourierButton.setVisibility(isCourierSelected ? View.GONE : View.VISIBLE);
        tvSelectedShipment.setText(isCourierSelected ?
                shipmentDetailData.getSelectedCourier().getName() : "");
        llSelectedCourier.setVisibility(isCourierSelected ? View.VISIBLE : View.GONE);
    }

    private void bindCostDetail(ShipmentSingleAddressItem shipmentSingleAddressItem) {
        rlCartSubTotal.setVisibility(View.VISIBLE);
        rlShipmentCost.setVisibility(shipmentSingleAddressItem.isDetailSubtotalViewStateExpanded() ? View.VISIBLE : View.GONE);
        tvShopName.setText(shipmentSingleAddressItem.getShopName());

        int totalItem = 0;
        double totalWeight = 0;
        int shippingPrice = 0;
        int insurancePrice = 0;
        int additionalPrice = 0;
        int subTotalPrice = 0;
        int totalItemPrice = 0;

        String tvShippingFeeLabel = tvShippingFee.getContext().getString(R.string.label_delivery_price);
        String tvTotalItemLabel = tvTotalItem.getContext().getString(R.string.label_item_count_without_format);

        if (shipmentSingleAddressItem.getSelectedShipmentDetailData() != null &&
                shipmentSingleAddressItem.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            shippingPrice = shipmentSingleAddressItem.getSelectedShipmentDetailData().getSelectedCourier()
                    .getDeliveryPrice();
            if (shipmentSingleAddressItem.getSelectedShipmentDetailData().getUseInsurance()) {
                insurancePrice = shipmentSingleAddressItem.getSelectedShipmentDetailData()
                        .getSelectedCourier().getInsurancePrice();
            }
            additionalPrice = shipmentSingleAddressItem.getSelectedShipmentDetailData()
                    .getSelectedCourier().getAdditionalPrice();
            for (CartItemModel cartItemModel : shipmentSingleAddressItem.getCartItemModels()) {
                totalItemPrice += (cartItemModel.getQuantity() * cartItemModel.getPrice());
                totalItem += cartItemModel.getQuantity();
                totalWeight += cartItemModel.getWeight();
            }
            tvShippingFeeLabel = getTotalWeightLabel(totalWeight, shipmentSingleAddressItem.getWeightUnit());
            tvTotalItemLabel = String.format(tvTotalItem.getContext().getString(R.string.label_item_count_with_format), totalItem);
            subTotalPrice += (totalItemPrice + shippingPrice + insurancePrice + additionalPrice);
        }
        tvTotalItemPrice.setText(getPriceFormat(totalItemPrice));
        tvTotalItem.setText(tvTotalItemLabel);
        tvShippingFee.setText(tvShippingFeeLabel);
        tvSubTotalPrice.setText(getPriceFormat(subTotalPrice));
        tvShippingFeePrice.setText(getPriceFormat(shippingPrice));
        tvInsuranceFeePrice.setText(getPriceFormat(insurancePrice));
        rlCartSubTotal.setOnClickListener(costDetailOptionListener(shipmentSingleAddressItem));
    }

    private void bindError(ShipmentItem shipmentItem) {
        if (shipmentItem.isError()) {
            tvError.setText(shipmentItem.getErrorMessage());
            layoutError.setVisibility(View.VISIBLE);
        } else {
            layoutError.setVisibility(View.GONE);
        }
    }

    private void bindWarnings(ShipmentItem shipmentItem) {
        if (shipmentItem.isWarning()) {
            tvWarning.setText(shipmentItem.getWarningMessage());
            layoutWarning.setVisibility(View.VISIBLE);
        } else {
            layoutWarning.setVisibility(View.GONE);
        }
    }

    private void initInnerRecyclerView(List<CartItemModel> cartItemList) {
//        rvCartItem.setVisibility(View.GONE);

        rvCartItem.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
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

    private String getTotalWeightLabel(double weight, int weightUnit) {
        String unit = weightUnit == GRAM ? "gr" : "Kg";
        return String.format("Ongkos Kirim (%s %s)", (int) weight, unit);
    }

    private String getPriceFormat(int price) {
        return price == 0 ? "-" : CurrencyFormatUtil.convertPriceValueToIdrFormat(price, true);
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

    private View.OnClickListener selectShippingOptionListener(final int position,
                                                              final ShipmentItem shipmentItem,
                                                              final RecipientAddressModel recipientAddressModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onChooseShipment(position, shipmentItem, recipientAddressModel);
            }
        };
    }

    private View.OnClickListener costDetailOptionListener(final ShipmentItem shipmentItem) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleShowCostDetail(shipmentItem);
            }
        };
    }

    private void toggleShowCostDetail(ShipmentItem shipmentItem) {
        shipmentItem.setDetailSubtotalViewStateExpanded(!shipmentItem.isDetailSubtotalViewStateExpanded());
        ivDetailOptionChevron.setImageResource(getResourceDrawerChevron(shipmentItem.isDetailSubtotalViewStateExpanded()));

        rlShipmentCost.setVisibility(shipmentItem.isDetailSubtotalViewStateExpanded() ? View.VISIBLE : View.GONE);
    }

    private int getResourceDrawerChevron(boolean isExpanded) {
        return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
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
        tvProductName.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvFreeReturnLabel.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvPreOrder.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvTextProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvLabelItemCount.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvProductTotalItem.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvNoteToSellerLabel.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        tvCashback.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
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
        tvProductName.setTextColor(ContextCompat.getColor(mContext, R.color.black_70));
        tvProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.orange_red));
        tvFreeReturnLabel.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        tvPreOrder.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        tvTextProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
        tvProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        tvLabelItemCount.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
        tvProductTotalItem.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        tvNoteToSellerLabel.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
        tvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mContext, R.color.black_70));
        tvCashback.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_bg_cashback));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        ivProductImage.setColorFilter(null);
        ivProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

}