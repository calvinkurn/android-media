package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.design.utils.CurrencyFormatUtil;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentCostViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details;
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";

    private static final int GRAM = 0;
    private static final int KILOGRAM = 1;

    private RelativeLayout mRlShipmentCostLayout;
    private TextView mTvTotalItemLabel;
    private TextView mTvTotalItemPrice;
    private TextView mTvShippingFeeLabel;
    private TextView mTvShippingFee;
    private TextView mTvInsuranceFee;
    private TextView mTvPromoDiscount;
    private TextView mTvSellerCostAdditionLabel;
    private TextView mTvSellerCostAdditionFee;
    private TextView mTvInsuranceFeeLabel;
    private TextView mTvPromoLabel;

    private TextView mTvPromoMessage;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentCostViewHolder(View itemView,
                                  ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        mRlShipmentCostLayout = itemView.findViewById(R.id.rl_shipment_cost);
        mTvTotalItemLabel = itemView.findViewById(R.id.tv_total_item_label);
        mTvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price);
        mTvShippingFeeLabel = itemView.findViewById(R.id.tv_shipping_fee_label);
        mTvShippingFee = itemView.findViewById(R.id.tv_shipping_fee);
        mTvInsuranceFee = itemView.findViewById(R.id.tv_insurance_fee);
        mTvPromoDiscount = itemView.findViewById(R.id.tv_promo);
        mTvPromoMessage = itemView.findViewById(R.id.tv_promo_message);
        mTvSellerCostAdditionLabel = itemView.findViewById(R.id.tv_seller_cost_addition);
        mTvSellerCostAdditionFee = itemView.findViewById(R.id.tv_seller_cost_addition_fee);
        mTvInsuranceFeeLabel = itemView.findViewById(R.id.tv_insurance_fee_label);
        mTvPromoLabel = itemView.findViewById(R.id.tv_promo_label);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
    }

    public void bindViewHolder(ShipmentCostModel shipmentCost) {
        mRlShipmentCostLayout.setVisibility(View.VISIBLE);

        mTvTotalItemLabel.setText(getTotalItemLabel(mTvTotalItemLabel.getContext(), shipmentCost.getTotalItem()));
        mTvTotalItemPrice.setText(shipmentCost.getTotalItemPrice() == 0 ? "-" :
                CurrencyFormatUtil.convertPriceValueToIdrFormat((int) shipmentCost.getTotalItemPrice(), true));
        mTvShippingFeeLabel.setText(mTvShippingFeeLabel.getContext().getString(R.string.label_shipment_fee));
        mTvShippingFee.setText(getPriceFormat(mTvShippingFeeLabel, mTvShippingFee, shipmentCost.getShippingFee()));
        mTvInsuranceFee.setText(getPriceFormat(mTvInsuranceFeeLabel, mTvInsuranceFee, shipmentCost.getInsuranceFee()));
        mTvPromoDiscount.setText(getPriceFormat(mTvPromoLabel, mTvPromoDiscount, shipmentCost.getPromoPrice()));
        mTvSellerCostAdditionFee.setText(getPriceFormat(mTvSellerCostAdditionLabel, mTvSellerCostAdditionFee, shipmentCost.getAdditionalFee()));

        if (!TextUtils.isEmpty(shipmentCost.getPromoMessage())) {
            formatPromoMessage(mTvPromoMessage, shipmentCost.getPromoMessage());
            mTvPromoMessage.setVisibility(View.VISIBLE);
        } else {
            mTvPromoMessage.setVisibility(View.GONE);
        }
    }

    private void formatPromoMessage(TextView textView, String promoMessage) {
        String formatText = " Hapus";
        promoMessage += formatText;
        int startSpan = promoMessage.indexOf(formatText);
        int endSpan = promoMessage.indexOf(formatText) + formatText.length();
        Spannable formattedPromoMessage = new SpannableString(promoMessage);
        final int color = ContextCompat.getColor(textView.getContext(), R.color.tkpd_main_green);
        formattedPromoMessage.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        formattedPromoMessage.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
        formattedPromoMessage.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(color);
                textPaint.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                shipmentAdapterActionListener.onRemovePromoCode();
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(formattedPromoMessage);
    }

    private String getTotalItemLabel(Context context, int totalItem) {
        return String.format(context.getString(R.string.label_item_count_summary_with_format), totalItem);
    }

    private String getTotalWeightLabel(Context context, double weight, int weightUnit) {
        String unit = weightUnit == GRAM ? context.getString(R.string.weight_unit_gram) : context.getString(R.string.weight_unit_kilogram);
        return String.format(context.getString(R.string.label_shipping_price_format), String.valueOf((int) weight), unit);
    }

    private String getPriceFormat(TextView textViewLabel, TextView textViewPrice, double price) {
        if (price == 0) {
            textViewLabel.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.GONE);
            return "-";
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textViewPrice.setVisibility(View.VISIBLE);
            return CurrencyFormatUtil.convertPriceValueToIdrFormat((int) price, true);
        }
    }

    private void togglePromoText() {
        mTvPromoMessage.setVisibility(View.GONE);
    }

}