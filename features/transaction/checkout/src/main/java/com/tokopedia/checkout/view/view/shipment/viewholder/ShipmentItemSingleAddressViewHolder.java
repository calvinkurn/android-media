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
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.adapter.InnerProductListAdapter;
import com.tokopedia.checkout.view.adapter.SingleAddressShipmentAdapter.ActionListener;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressItem;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentItemSingleAddressViewHolder extends ShipmentItemViewHolder {

    private static final int FIRST_ELEMENT = 0;

    private Context mContext;
    private ShipmentAdapterActionListener mActionListener;

    private boolean mIsAllCartItemShown;
    private boolean mIsCostDetailShown;

    public ShipmentItemSingleAddressViewHolder(View itemView, Context context,
                                               ShipmentAdapterActionListener actionListener) {
        super(itemView);
        mContext = context;
        mActionListener = actionListener;
    }

    public void bindViewHolder(ShipmentSingleAddressItem shipmentSingleAddressItem){
        // TODO : BIND HERE
    }

    public void bindViewHolder(CartSellerItemModel cartSellerItemModel,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList) {

        mIsAllCartItemShown = false;
        mIsCostDetailShown = false;

        List<CartItemModel> cartItemModelList = new ArrayList<>(cartSellerItemModel.getCartItemModels());

        bindFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT));
        bindOtherCartItems(cartItemModelList);
        bindChooseCourier(cartSellerItemModel, cartSellerItemModel.getSelectedShipmentDetailData(),
                recipientAddressModel);
        bindCostDetail(cartSellerItemModel);
        bindError(cartSellerItemModel);
        bindWarnings(cartSellerItemModel);

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
        tvCashback.setText(cartItemModel.getCashback());
    }

    private void bindOtherCartItems(List<CartItemModel> cartItemModelList) {
        rlExpandOtherProduct.setVisibility(cartItemModelList.isEmpty() ?
                View.GONE : View.VISIBLE);
        tvExpandOtherProduct.setText(getOtherCartItemsLabel(cartItemModelList,
                mIsAllCartItemShown));

        ivDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsAllCartItemShown));
        rlExpandOtherProduct.setOnClickListener(showAllProductListener(cartItemModelList));
        tvExpandOtherProduct.setOnClickListener(showAllProductListener(cartItemModelList));

        initInnerRecyclerView(cartItemModelList);
    }

    private void bindChooseCourier(CartSellerItemModel cartSellerItemModel,
                                   ShipmentDetailData shipmentDetailData,
                                   RecipientAddressModel recipientAddressModel) {
        chooseCourierButton.setOnClickListener(selectShippingOptionListener(getAdapterPosition(),
                cartSellerItemModel, recipientAddressModel));
        llSelectedCourier.setOnClickListener(selectShippingOptionListener(getAdapterPosition(),
                cartSellerItemModel, recipientAddressModel));

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        chooseCourierButton.setVisibility(isCourierSelected ? View.GONE : View.VISIBLE);
        tvSelectedShipment.setText(isCourierSelected ?
                shipmentDetailData.getSelectedCourier().getName() : "");
        llSelectedCourier.setVisibility(isCourierSelected ? View.VISIBLE : View.GONE);
    }

    private void bindCostDetail(CartSellerItemModel cartSellerItem) {
        rlCartSubTotal.setVisibility(View.VISIBLE);
        rlShipmentCost.setVisibility(mIsCostDetailShown ? View.VISIBLE : View.GONE);

        tvShopName.setText(cartSellerItem.getShopName());

        tvTotalItemPrice.setText(getPriceFormat((int) cartSellerItem.getTotalItemPrice()));
        tvTotalItem.setText(getTotalItemLabel(cartSellerItem.getTotalQuantity()));
        tvShippingFee.setText(getTotalWeightLabel(cartSellerItem.getTotalWeight(),
                cartSellerItem.getWeightUnit()));

        tvShippingFee.setText(getPriceFormat((int) cartSellerItem.getShippingFee()));
        tvInsuranceFee.setText(getPriceFormat((int) cartSellerItem.getInsuranceFee()));

        tvSubTotalPrice.setText(getPriceFormat((int) cartSellerItem.getTotalPrice()));
        rlCartSubTotal.setOnClickListener(costDetailOptionListener());
    }

    private void bindError(CartSellerItemModel data) {
        if (data.isError()) {
            tvError.setText(data.getErrorMessage());
            layoutError.setVisibility(View.VISIBLE);
        } else {
            layoutError.setVisibility(View.GONE);
        }
    }

    private void bindWarnings(CartSellerItemModel data) {
        if (data.isWarning()) {
            tvWarning.setText(data.getWarningMessage());
            layoutWarning.setVisibility(View.VISIBLE);
        } else {
            layoutWarning.setVisibility(View.GONE);
        }
    }

    private void initInnerRecyclerView(List<CartItemModel> cartItemList) {
        rvCartItem.setVisibility(View.GONE);

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

    private String getTotalItemLabel(int totalItem) {
        return String.format("Jumlah Barang (%s Item)", totalItem);
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

    private View.OnClickListener showAllProductListener(final List<CartItemModel> cartItemList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleShowAllProduct(cartItemList);
            }
        };
    }

    private void toggleShowAllProduct(List<CartItemModel> cartItemList) {
        mIsAllCartItemShown = !mIsAllCartItemShown;
        rvCartItem.setVisibility(mIsAllCartItemShown ? View.VISIBLE : View.GONE);

        tvExpandOtherProduct.setText(getOtherCartItemsLabel(cartItemList,
                mIsAllCartItemShown));
    }

    private View.OnClickListener selectShippingOptionListener(final int position,
                                                              final CartSellerItemModel cartSellerItemModel,
                                                              final RecipientAddressModel recipientAddressModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onChooseShipment(position, cartSellerItemModel, recipientAddressModel);
            }
        };
    }

    private View.OnClickListener costDetailOptionListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleShowCostDetail();
            }
        };
    }

    private void toggleShowCostDetail() {
        mIsCostDetailShown = !mIsCostDetailShown;
        ivDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsCostDetailShown));

        rlShipmentCost.setVisibility(mIsCostDetailShown ? View.VISIBLE : View.GONE);
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