package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.view.common.utils.WeightFormatterUtil;
import com.tokopedia.design.utils.CurrencyFormatUtil;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentCartItemViewHolder extends RecyclerView.ViewHolder {

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private ShipmentItemListener shipmentItemListener;

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
    private RelativeLayout mRlPurchaseProtection;
    private TextView mTvPPPLinkText;
    private TextView mTvPPPPrice;
    private TextView mTvPPPMore;
    private CheckBox mCbPPP;
    private LinearLayout mLlShippingWarningContainer;
    private View mSeparatorMultipleProductSameStore;
    private TextView tvErrorShipmentItemTitle;
    private TextView tvErrorShipmentItemDescription;

    public ShipmentCartItemViewHolder(View itemView) {
        super(itemView);

        mIvProductImage = itemView.findViewById(R.id.iv_product_image);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
        mTvProductCountAndWeight = itemView.findViewById(R.id.tv_item_count_and_weight);
        mLlOptionalNoteToSellerLayout = itemView.findViewById(R.id.ll_optional_note_to_seller_layout);
        mTvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);
        mRlPurchaseProtection = itemView.findViewById(R.id.rlayout_purchase_protection);
        mTvPPPLinkText = itemView.findViewById(R.id.text_link_text);
        mTvPPPPrice = itemView.findViewById(R.id.text_price_per_product);
        mTvPPPMore = itemView.findViewById(R.id.text_ppp_more);
        mCbPPP = itemView.findViewById(R.id.checkbox_ppp);
        mllProductPoliciesLayout = itemView.findViewById(R.id.layout_policy);
        mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        mTvFreeReturnLabel = itemView.findViewById(R.id.tv_free_return_label);
        mTvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        mTvCashback = itemView.findViewById(R.id.tv_cashback);
        mTvNoteToSellerLabel = itemView.findViewById(R.id.tv_note_to_seller_label);
        mLlShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        mSeparatorMultipleProductSameStore = itemView.findViewById(R.id.v_separator_multiple_product_same_store);
        tvErrorShipmentItemTitle = itemView.findViewById(R.id.tv_error_shipment_item_title);
        tvErrorShipmentItemDescription = itemView.findViewById(R.id.tv_error_shipment_item_description);

    }

    public void bindViewHolder(CartItemModel cartItem, ShipmentItemListener listener) {
        shipmentItemListener = listener;
        if (cartItem.isError()) {
            showShipmentWarning(cartItem);
        } else {
            hideShipmentWarning();
        }
        mSeparatorMultipleProductSameStore.setVisibility(View.VISIBLE);
        ImageHandler.LoadImage(mIvProductImage, cartItem.getImageUrl());
        mTvProductName.setText(cartItem.getName());
        mTvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (long) cartItem.getPrice(), false));
        mTvProductCountAndWeight.setText(String.format(mTvProductCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItem.getQuantity()),
                WeightFormatterUtil.getFormattedWeight(cartItem.getWeight(), cartItem.getQuantity())));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItem.getNoteToSeller());
        mLlOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        mTvOptionalNoteToSeller.setText(cartItem.getNoteToSeller());

        mRlPurchaseProtection.setVisibility(cartItem.isProtectionAvailable() ? View.VISIBLE : View.GONE);
        if (cartItem.isProtectionAvailable()) {
            mTvPPPMore.setText(cartItem.getProtectionLinkText());
            mTvPPPMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shipmentItemListener.navigateToWebView(cartItem.getProtectionLinkUrl());
                }
            });
            mTvPPPLinkText.setText(cartItem.getProtectionTitle());
            mTvPPPPrice.setText(cartItem.getProtectionSubTitle());
            mCbPPP.setChecked(cartItem.isProtectionOptIn());
            mCbPPP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    shipmentItemListener.notifyOnPurchaseProtectionChecked(checked, getAdapterPosition() + 1);
                }
            });
        }

        mIvFreeReturnIcon.setVisibility(cartItem.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvFreeReturnLabel.setVisibility(View.GONE);
        if (cartItem.isPreOrder()) {
            mTvPreOrder.setText(cartItem.getPreOrderInfo());
            mTvPreOrder.setVisibility(View.VISIBLE);
        } else {
            mTvPreOrder.setVisibility(View.GONE);
        }
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

    private void showShipmentWarning(CartItemModel cartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.getErrorMessage())) {
            tvErrorShipmentItemTitle.setText(cartItemModel.getErrorMessage());
            tvErrorShipmentItemTitle.setVisibility(View.VISIBLE);
            mLlShippingWarningContainer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(cartItemModel.getErrorMessageDescription())) {
                tvErrorShipmentItemDescription.setText(cartItemModel.getErrorMessageDescription());
                tvErrorShipmentItemDescription.setVisibility(View.VISIBLE);
            } else {
                tvErrorShipmentItemDescription.setVisibility(View.GONE);
            }
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
        mTvProductCountAndWeight.setTextColor(colorGreyNonActiveText);
        mTvCashback.setTextColor(colorGreyNonActiveText);
        mTvCashback.setBackground(ContextCompat.getDrawable(mTvCashback.getContext(), R.drawable.bg_cashback_disabled));
        setImageFilterGrayScale();
    }

    private void setImageFilterGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter disabledColorFilter = new ColorMatrixColorFilter(matrix);
        mIvProductImage.setColorFilter(disabledColorFilter);
        mIvProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
        mIvFreeReturnIcon.setColorFilter(disabledColorFilter);
        mIvFreeReturnIcon.setImageAlpha(IMAGE_ALPHA_DISABLED);
    }

    private void enableItemView() {
        mTvProductName.setTextColor(ContextCompat.getColor(mTvProductName.getContext(), R.color.black_70));
        mTvProductPrice.setTextColor(ContextCompat.getColor(mTvProductPrice.getContext(), R.color.orange_red));
        mTvFreeReturnLabel.setTextColor(ContextCompat.getColor(mTvFreeReturnLabel.getContext(), R.color.font_black_secondary_54));
        mTvPreOrder.setTextColor(ContextCompat.getColor(mTvPreOrder.getContext(), R.color.font_black_secondary_54));
        mTvNoteToSellerLabel.setTextColor(ContextCompat.getColor(mTvNoteToSellerLabel.getContext(), R.color.black_38));
        mTvProductCountAndWeight.setTextColor(ContextCompat.getColor(mTvProductCountAndWeight.getContext(), R.color.black_38));
        mTvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mTvOptionalNoteToSeller.getContext(), R.color.black_38));
        mTvCashback.setTextColor(ContextCompat.getColor(mTvCashback.getContext(), R.color.cashback_text_color));
        mTvCashback.setBackground(ContextCompat.getDrawable(mTvCashback.getContext(), R.drawable.bg_cashback));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        mIvProductImage.setColorFilter(null);
        mIvProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

    public interface ShipmentItemListener {
        void notifyOnPurchaseProtectionChecked(boolean checked, int position);

        void navigateToWebView(String protectionLinkUrl);
    }

}
