package com.tokopedia.checkout.view.viewholder;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewholder.ShipmentItemViewHolder;
import com.tokopedia.design.utils.CurrencyFormatUtil;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class CartItemViewHolder extends ShipmentItemViewHolder {

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private ImageView mIvProductImage;
    private TextView mTvProductName;
    private TextView mTvProductPrice;
    private TextView mTvProductCountAndWeight;
    private LinearLayout mLlOptionalNoteToSellerLayout;
    private TextView mTvOptionalNoteToSeller;
    private LinearLayout mllProductPoliciesLayout;
    private ImageView mIvFreeReturnIcon;
    private TextView mTvFreeReturnLabel;
    private TextView mTvPreOrder;
    private TextView mTvCashback;
    private TextView mTvNoteToSellerLabel;
    private LinearLayout mLlShippingWarningContainer;
    private TextView mTvShippingWarning;
    private View mSeparatorMultipleProductSameStore;

    public CartItemViewHolder(View itemView) {
        super(itemView);

        mIvProductImage = itemView.findViewById(R.id.iv_product_image);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
        mTvProductCountAndWeight = itemView.findViewById(R.id.tv_item_count_and_weight);
        mLlOptionalNoteToSellerLayout = itemView.findViewById(R.id.ll_optional_note_to_seller_layout);
        mTvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);
        mllProductPoliciesLayout = itemView.findViewById(R.id.layout_policy);
        mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        mTvFreeReturnLabel = itemView.findViewById(R.id.tv_free_return_label);
        mTvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        mTvCashback = itemView.findViewById(R.id.tv_cashback);
        mTvNoteToSellerLabel = itemView.findViewById(R.id.tv_note_to_seller_label);
        mLlShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        mTvShippingWarning = itemView.findViewById(R.id.tv_shipping_warning);
        mSeparatorMultipleProductSameStore = itemView.findViewById(R.id.v_separator_multiple_product_same_store);

    }

    public void bindViewHolder(CartItemModel cartItem) {
        if (cartItem.isError()) {
            showShipmentWarning(cartItem.getErrorMessage());
        } else {
            hideShipmentWarning();
        }
        mSeparatorMultipleProductSameStore.setVisibility(View.VISIBLE);
        ImageHandler.LoadImage(mIvProductImage, cartItem.getImageUrl());
        mTvProductName.setText(cartItem.getName());
        mTvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (long) cartItem.getPrice(), true));
        mTvProductCountAndWeight.setText(cartItem.getWeightFmt());
        mTvProductCountAndWeight.setText(String.format(mTvProductCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItem.getQuantity()), cartItem.getWeightFmt()));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItem.getNoteToSeller());
        mLlOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        mTvOptionalNoteToSeller.setText(cartItem.getNoteToSeller());

        mIvFreeReturnIcon.setVisibility(cartItem.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvFreeReturnLabel.setVisibility(cartItem.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvPreOrder.setVisibility(cartItem.isPreOrder() ? View.VISIBLE : View.GONE);
        mTvCashback.setVisibility(cartItem.isCashback() ? View.VISIBLE : View.GONE);
        String cashback = "    " + mTvCashback.getContext().getString(R.string.label_cashback) +
                " " + cartItem.getCashback() + "    ";
        mTvCashback.setText(cashback);
        mTvNoteToSellerLabel.setVisibility(View.GONE);
        if (cartItem.isFreeReturn() || cartItem.isPreOrder() || cartItem.isCashback()) {
            mllProductPoliciesLayout.setVisibility(View.VISIBLE);
        } else {
            mllProductPoliciesLayout.setVisibility(View.GONE);
        }
    }

    private void showShipmentWarning(String message) {
        if (!TextUtils.isEmpty(message)) {
            mTvShippingWarning.setText(message);
            mLlShippingWarningContainer.setVisibility(View.VISIBLE);
        } else {
            mLlShippingWarningContainer.setVisibility(View.GONE);
        }
        disableItemView();
    }

    private void hideShipmentWarning() {
        mLlShippingWarningContainer.setVisibility(View.GONE);
        enableItemView();
    }

    private void disableItemView() {
        int colorGreyNonActiveText = ContextCompat.getColor(mTvProductName.getContext(), R.color.grey_nonactive_text);
        mTvProductName.setTextColor(colorGreyNonActiveText);
        mTvProductPrice.setTextColor(colorGreyNonActiveText);
        mTvFreeReturnLabel.setTextColor(colorGreyNonActiveText);
        mTvPreOrder.setTextColor(colorGreyNonActiveText);
        mTvNoteToSellerLabel.setTextColor(colorGreyNonActiveText);
        mTvOptionalNoteToSeller.setTextColor(colorGreyNonActiveText);
        mTvCashback.setBackgroundColor(colorGreyNonActiveText);
        setImageFilterGrayScale();
    }

    private void setImageFilterGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        mIvProductImage.setColorFilter(cf);
        mIvProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
    }

    private void enableItemView() {
        mTvProductName.setTextColor(ContextCompat.getColor(mTvProductName.getContext(), R.color.black_70));
        mTvProductPrice.setTextColor(ContextCompat.getColor(mTvProductPrice.getContext(), R.color.orange_red));
        mTvFreeReturnLabel.setTextColor(ContextCompat.getColor(mTvFreeReturnLabel.getContext(), R.color.font_black_secondary_54));
        mTvPreOrder.setTextColor(ContextCompat.getColor(mTvPreOrder.getContext(), R.color.font_black_secondary_54));
        mTvNoteToSellerLabel.setTextColor(ContextCompat.getColor(mTvNoteToSellerLabel.getContext(), R.color.black_38));
        mTvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mTvOptionalNoteToSeller.getContext(), R.color.black_38));
        mTvCashback.setBackground(ContextCompat.getDrawable(mTvCashback.getContext(), R.drawable.bg_cashback));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        mIvProductImage.setColorFilter(null);
        mIvProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

}
