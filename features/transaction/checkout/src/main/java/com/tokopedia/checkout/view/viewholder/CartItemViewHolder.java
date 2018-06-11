package com.tokopedia.checkout.view.viewholder;

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
    }

    public void bindViewHolder(CartItemModel cartItem) {
        ImageHandler.LoadImage(mIvProductImage, cartItem.getImageUrl());
        mTvProductName.setText(cartItem.getName());
        mTvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (int) cartItem.getPrice(), true));
        mTvProductCountAndWeight.setText(cartItem.getWeightFmt());
        mTvProductCountAndWeight.setText(String.format(mTvProductCountAndWeight.getContext()
                        .getString(R.string.iotem_count_and_weight_format),
                String.valueOf(cartItem.getQuantity()), cartItem.getWeightFmt()));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItem.getNoteToSeller());
        mLlOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        mTvOptionalNoteToSeller.setText(cartItem.getNoteToSeller());

        mllProductPoliciesLayout.setVisibility(View.GONE);
        mIvFreeReturnIcon.setVisibility(cartItem.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvFreeReturnLabel.setVisibility(cartItem.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvPreOrder.setVisibility(cartItem.isPreOrder() ? View.VISIBLE : View.GONE);
        mTvCashback.setVisibility(cartItem.isCashback() ? View.VISIBLE : View.GONE);
        String cashback = mTvCashback.getContext().getString(R.string.label_cashback) + " " + cartItem.getCashback();
        mTvCashback.setText(cashback);
    }

}
