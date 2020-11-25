package com.tokopedia.checkout.view.viewholder;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.utils.WeightFormatterUtil;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifyprinciples.Typography;

import java.util.List;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentCartItemViewHolder extends RecyclerView.ViewHolder {

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private ShipmentItemListener shipmentItemListener;

    private ImageView mIvProductImage;
    private Typography mTvProductName;
    private Typography mTvProductPrice;
    private Typography mTvProductOriginalPrice;
    private Typography mTvProductCountAndWeight;
    private TextView mTvOptionalNoteToSeller;
    private RelativeLayout mRlPurchaseProtection;
    private TextView mTvPPPLinkText;
    private TextView mTvPPPPrice;
    private TextView mTvPPPMore;
    private CheckBox mCbPPP;
    private CheckBox mCbPPPDisabled;
    private LinearLayout mLlShippingWarningContainer;
    private View mSeparatorMultipleProductSameStore;
    private TextView tvErrorShipmentItemTitle;
    private TextView tvErrorShipmentItemDescription;
    private Ticker productTicker;
    private Typography mTextVariant;
    private FlexboxLayout mLayoutProductInfo;

    public ShipmentCartItemViewHolder(View itemView) {
        super(itemView);

        mIvProductImage = itemView.findViewById(R.id.iv_product_image);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
        mTvProductOriginalPrice = itemView.findViewById(R.id.tv_product_original_price);
        mTvProductCountAndWeight = itemView.findViewById(R.id.tv_item_count_and_weight);
        mTvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);
        mRlPurchaseProtection = itemView.findViewById(R.id.rlayout_purchase_protection);
        mTvPPPLinkText = itemView.findViewById(R.id.text_link_text);
        mTvPPPPrice = itemView.findViewById(R.id.text_price_per_product);
        mTvPPPMore = itemView.findViewById(R.id.text_ppp_more);
        mCbPPP = itemView.findViewById(R.id.checkbox_ppp);
        mCbPPPDisabled = itemView.findViewById(R.id.checkbox_ppp_disabled);
        mLlShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        mSeparatorMultipleProductSameStore = itemView.findViewById(R.id.v_separator_multiple_product_same_store);
        tvErrorShipmentItemTitle = itemView.findViewById(R.id.tv_error_shipment_item_title);
        tvErrorShipmentItemDescription = itemView.findViewById(R.id.tv_error_shipment_item_description);
        productTicker = itemView.findViewById(R.id.product_ticker);
        mTextVariant = itemView.findViewById(R.id.text_variant);
        mLayoutProductInfo = itemView.findViewById(R.id.layout_product_info);
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
        mTvProductCountAndWeight.setText(String.format(mTvProductCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItem.getQuantity()),
                WeightFormatterUtil.getFormattedWeight(cartItem.getWeight(), cartItem.getQuantity())));
        if (!TextUtils.isEmpty(cartItem.getVariant())) {
            mTextVariant.setText(cartItem.getVariant());
            mTextVariant.setVisibility(View.VISIBLE);
        } else {
            mTextVariant.setVisibility(View.GONE);
        }
        renderProductPrice(cartItem);
        renderNotesToSeller(cartItem);
        renderPurchaseProtection(cartItem);
        renderProductTicker(cartItem);
        renderProductProperties(cartItem);
    }

    private void renderProductProperties(CartItemModel cartItemModel) {
        List<String> productInformationList = cartItemModel.getProductInformation();
        if (productInformationList != null && !productInformationList.isEmpty()) {
            for (int i = 0; i < productInformationList.size(); i++) {
                Typography productInfo = new Typography(itemView.getContext());
                productInfo.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68));
                productInfo.setType(Typography.SMALL);
                if (mLayoutProductInfo.getChildCount() > 0) {
                    productInfo.setText(", " + productInformationList.get(i));
                } else {
                    productInfo.setText(productInformationList.get(i));
                }
                mLayoutProductInfo.addView(productInfo);
            }
            mLayoutProductInfo.setVisibility(View.VISIBLE);
        } else {
            mLayoutProductInfo.setVisibility(View.GONE);
        }
    }

    private void renderProductPrice(CartItemModel cartItem) {
        mTvProductPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (long) cartItem.getPrice(), false)));
        int dp4 = mTvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_4);
        int dp10 = mTvProductPrice.getResources().getDimensionPixelOffset(R.dimen.dp_10);
        if (cartItem.getOriginalPrice() > 0) {
            mTvProductPrice.setPadding(dp4, dp4, 0, 0);
            mTvProductOriginalPrice.setText(Utils.removeDecimalSuffix(CurrencyFormatUtil.convertPriceValueToIdrFormat((long) cartItem.getOriginalPrice(), false)));
            mTvProductOriginalPrice.setPaintFlags(mTvProductOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mTvProductOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            mTvProductPrice.setPadding(dp10, dp4, 0, 0);
            mTvProductOriginalPrice.setVisibility(View.GONE);
        }
    }

    private void renderNotesToSeller(CartItemModel cartItem) {
        if (!TextUtils.isEmpty(cartItem.getNoteToSeller())) {
            mTvOptionalNoteToSeller.setText(cartItem.getNoteToSeller());
            mTvOptionalNoteToSeller.setVisibility(View.VISIBLE);
        } else {
            mTvOptionalNoteToSeller.setVisibility(View.GONE);
        }
    }

    private void renderPurchaseProtection(CartItemModel cartItem) {
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


            if (cartItem.isProtectionCheckboxDisabled()) {
                mCbPPP.setVisibility(View.GONE);
                mCbPPPDisabled.setVisibility(View.VISIBLE);
                mCbPPPDisabled.setChecked(true);
                mCbPPPDisabled.setClickable(false);
            } else {
                mCbPPPDisabled.setVisibility(View.GONE);
                mCbPPP.setVisibility(View.VISIBLE);
                mCbPPP.setChecked(cartItem.isProtectionOptIn());
                mCbPPP.setClickable(true);
                mCbPPP.setOnCheckedChangeListener((compoundButton, checked) -> shipmentItemListener.notifyOnPurchaseProtectionChecked(checked, getAdapterPosition() + 1));
            }
        }
    }

    private void renderProductTicker(CartItemModel cartItemModel) {
        if (cartItemModel.isShowTicker() && !TextUtils.isEmpty(cartItemModel.getTickerMessage())) {
            productTicker.setVisibility(View.VISIBLE);
            productTicker.setTextDescription(cartItemModel.getTickerMessage());
        } else productTicker.setVisibility(View.GONE);
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
        mTvProductOriginalPrice.setTextColor(colorGreyNonActiveText);
        mTvOptionalNoteToSeller.setTextColor(colorGreyNonActiveText);
        mTvProductCountAndWeight.setTextColor(colorGreyNonActiveText);
        mTextVariant.setTextColor(colorGreyNonActiveText);
        setImageFilterGrayScale();
    }

    private void setImageFilterGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter disabledColorFilter = new ColorMatrixColorFilter(matrix);
        mIvProductImage.setColorFilter(disabledColorFilter);
        mIvProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
    }

    private void enableItemView() {
        mTvProductName.setTextColor(ContextCompat.getColor(mTvProductName.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_96));
        mTextVariant.setTextColor(ContextCompat.getColor(mTextVariant.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68));
        mTvProductPrice.setTextColor(ContextCompat.getColor(mTvProductPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_96));
        mTvProductOriginalPrice.setTextColor(ContextCompat.getColor(mTvProductOriginalPrice.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68));
        mTvProductCountAndWeight.setTextColor(ContextCompat.getColor(mTvProductCountAndWeight.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_68));
        mTvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mTvOptionalNoteToSeller.getContext(), com.tokopedia.unifyprinciples.R.color.Neutral_N700_96));
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
