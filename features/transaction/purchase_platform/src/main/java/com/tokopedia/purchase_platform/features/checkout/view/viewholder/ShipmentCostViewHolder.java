package com.tokopedia.purchase_platform.features.checkout.view.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.design.utils.CurrencyFormatUtil;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentCostViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details;

    private RelativeLayout mRlShipmentCostLayout;
    private TextView mTvTotalItemLabel;
    private TextView mTvTotalItemPrice;
    private TextView mTvShippingFeeLabel;
    private TextView mTvShippingFee;
    private TextView mTvInsuranceFee;
    private TextView mTvPurchaseProtectionLabel;
    private TextView mTvPurchaseProtectionFee;
    private TextView mTvPromoDiscount;
    private TextView mTvSellerCostAdditionLabel;
    private TextView mTvSellerCostAdditionFee;
    private TextView mTvInsuranceFeeLabel;
    private TextView mTvPromoOrCouponLabel;
    private TextView mTvDonationLabel;
    private TextView mTvDonationPrice;
    private TextView mTvEmasLabel;
    private TextView mTvEmasPrice;
    private TextView mTvTradeInLabel;
    private TextView mTvTradeInPrice;
    private TextView mTvOrderPrioritasLabel;
    private TextView mTvOrderPrioritasFee;
    private TextView mTvBookingFeeLabel;
    private TextView mTvBookingFee;
    private TextView mTvMacroInsuranceLabel;
    private TextView mTvMacroInsurancePrice;
    private TextView mTvDiscountLabel;
    private TextView mTvDiscountPrice;
    private TextView mTvShippingDiscountLabel;
    private TextView mTvShippingDiscountPrice;
    private TextView mTvProductDiscountLabel;
    private TextView mTvProductDiscountPrice;
    private RelativeLayout mRlTotalCashback;
    private TextView mTvTotalCashbackLabel;
    private TextView mTvTotalCashbackAmount;

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
        mTvPurchaseProtectionLabel = itemView.findViewById(R.id.tv_purchase_protection_label);
        mTvPurchaseProtectionFee = itemView.findViewById(R.id.tv_purchase_protection_fee);
        mTvPromoDiscount = itemView.findViewById(R.id.tv_promo);
        mTvSellerCostAdditionLabel = itemView.findViewById(R.id.tv_seller_cost_addition);
        mTvSellerCostAdditionFee = itemView.findViewById(R.id.tv_seller_cost_addition_fee);
        mTvInsuranceFeeLabel = itemView.findViewById(R.id.tv_insurance_fee_label);
        mTvPromoOrCouponLabel = itemView.findViewById(R.id.tv_promo_or_coupon_label);
        mTvDonationLabel = itemView.findViewById(R.id.tv_donation_label);
        mTvDonationPrice = itemView.findViewById(R.id.tv_donation_price);
        mTvEmasLabel = itemView.findViewById(R.id.tv_emas_label);
        mTvEmasPrice = itemView.findViewById(R.id.tv_emas_price);
        mTvTradeInLabel = itemView.findViewById(R.id.tv_trade_in_label);
        mTvTradeInPrice = itemView.findViewById(R.id.tv_trade_in);
        mTvOrderPrioritasLabel = itemView.findViewById(R.id.tv_order_prioritas_label);
        mTvOrderPrioritasFee = itemView.findViewById(R.id.tv_order_prioritas_price);
        mTvBookingFeeLabel = itemView.findViewById(R.id.tv_booking_fee_label);
        mTvBookingFee = itemView.findViewById(R.id.tv_booking_fee_price);
        mTvMacroInsuranceLabel = itemView.findViewById(R.id.tv_macro_insurance_label);
        mTvMacroInsurancePrice = itemView.findViewById(R.id.tv_macro_insurance_price);
        mTvDiscountLabel = itemView.findViewById(R.id.tv_discount_label);
        mTvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
        mTvShippingDiscountLabel = itemView.findViewById(R.id.tv_shipping_discount_label);
        mTvShippingDiscountPrice = itemView.findViewById(R.id.tv_shipping_discount_price);
        mTvProductDiscountLabel = itemView.findViewById(R.id.tv_product_discount_label);
        mTvProductDiscountPrice = itemView.findViewById(R.id.tv_product_discount_price);
        mRlTotalCashback = itemView.findViewById(R.id.rl_total_cashback);
        mTvTotalCashbackLabel = itemView.findViewById(R.id.tv_total_cashback_label);
        mTvTotalCashbackAmount = itemView.findViewById(R.id.tv_total_cashback_amount);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;
    }

    public void bindViewHolder(ShipmentCostModel shipmentCost) {
        mRlShipmentCostLayout.setVisibility(View.VISIBLE);

        mTvTotalItemLabel.setText(getTotalItemLabel(mTvTotalItemLabel.getContext(), shipmentCost.getTotalItem()));
        mTvTotalItemPrice.setText(shipmentCost.getTotalItemPrice() == 0 ? "-" :
                CurrencyFormatUtil.convertPriceValueToIdrFormat((long) shipmentCost.getTotalItemPrice(), false));
        mTvShippingFeeLabel.setText(mTvShippingFeeLabel.getContext().getString(R.string.label_shipment_fee));
        mTvShippingFee.setText(getPriceFormat(mTvShippingFeeLabel, mTvShippingFee, shipmentCost.getShippingFee()));
        mTvInsuranceFee.setText(getPriceFormat(mTvInsuranceFeeLabel, mTvInsuranceFee, shipmentCost.getInsuranceFee()));
        mTvOrderPrioritasFee.setText(getPriceFormat(mTvOrderPrioritasLabel, mTvOrderPrioritasFee, shipmentCost.getPriorityFee()));
        mTvPurchaseProtectionLabel.setText(getTotalPurchaseProtectionItemLabel(mTvPurchaseProtectionLabel.getContext(), shipmentCost.getTotalPurchaseProtectionItem()));
        mTvPurchaseProtectionFee.setText(getPriceFormat(mTvPurchaseProtectionLabel, mTvPurchaseProtectionFee, shipmentCost.getPurchaseProtectionFee()));
        mTvPromoDiscount.setText(String.format(mTvPromoDiscount.getContext().getString(R.string.promo_format),
                getPriceFormat(mTvPromoOrCouponLabel, mTvPromoDiscount, shipmentCost.getPromoPrice())));
        mTvSellerCostAdditionFee.setText(getPriceFormat(mTvSellerCostAdditionLabel, mTvSellerCostAdditionFee, shipmentCost.getAdditionalFee()));
        mTvDonationPrice.setText(getPriceFormat(mTvDonationLabel, mTvDonationPrice, shipmentCost.getDonation()));
        mTvEmasPrice.setText(getPriceFormat(mTvEmasLabel, mTvEmasPrice, shipmentCost.getEmasPrice()));
        mTvMacroInsurancePrice.setText(getPriceFormat(mTvMacroInsuranceLabel, mTvMacroInsurancePrice, shipmentCost.getMacroInsurancePrice()));
        if (mTvMacroInsuranceLabel.getVisibility() == View.VISIBLE) {
            mTvMacroInsuranceLabel.setText(shipmentCost.getMacroInsurancePriceLabel());
        }
        mTvTradeInPrice.setText(String.format(mTvTradeInPrice.getContext().getString(R.string.promo_format),
                getPriceFormat(mTvTradeInLabel, mTvTradeInPrice, shipmentCost.getTradeInPrice())));

        if (shipmentCost.getBookingFee() > 0) {
            mTvBookingFeeLabel.setVisibility(View.VISIBLE);
            mTvBookingFee.setVisibility(View.VISIBLE);
            mTvBookingFee.setText(shipmentCost.getBookingFee() + "");
        } else {
            mTvBookingFeeLabel.setVisibility(View.GONE);
            mTvBookingFee.setVisibility(View.GONE);
        }
        mTvBookingFee.setText(getPriceFormat(mTvBookingFeeLabel, mTvBookingFee, shipmentCost.getBookingFee()));

        renderDiscount(shipmentCost);
        renderCashback(shipmentCost);
    }

    private void renderDiscount(ShipmentCostModel shipmentCost) {
        if (shipmentCost.isHasDiscountDetails()) {
            renderShippingDiscount(shipmentCost);
            renderProductDiscount(shipmentCost);
        } else {
            renderGeneralDiscount(shipmentCost);
            mTvShippingDiscountLabel.setVisibility(View.GONE);
            mTvShippingDiscountPrice.setVisibility(View.GONE);
            mTvProductDiscountLabel.setVisibility(View.GONE);
            mTvProductDiscountPrice.setVisibility(View.GONE);
        }
    }

    private void renderProductDiscount(ShipmentCostModel shipmentCost) {
        mTvProductDiscountLabel.setText(mTvProductDiscountLabel.getContext().getString(R.string.label_product_discount));
        mTvProductDiscountPrice.setText(getPriceFormat(mTvProductDiscountLabel, mTvProductDiscountPrice, shipmentCost.getProductDiscountAmount() * -1));
    }

    private void renderShippingDiscount(ShipmentCostModel shipmentCost) {
        mTvShippingDiscountLabel.setText(mTvShippingDiscountLabel.getContext().getString(R.string.label_shipping_discount));
        if (shipmentCost.getShippingDiscountAmount() > 0) {
            if (shipmentCost.getShippingDiscountAmount() >= shipmentCost.getShippingFee()) {
                mTvShippingFee.setText(mTvShippingFee.getContext().getString(R.string.label_free_shipping));
                mTvShippingDiscountPrice.setVisibility(View.GONE);
                mTvShippingDiscountLabel.setVisibility(View.GONE);
            } else {
                mTvShippingDiscountPrice.setText(getPriceFormat(mTvShippingDiscountLabel, mTvShippingDiscountPrice, shipmentCost.getShippingDiscountAmount() * -1));
            }
        }
    }

    private void renderGeneralDiscount(ShipmentCostModel shipmentCost) {
        mTvDiscountLabel.setText(mTvDiscountLabel.getContext().getString(R.string.label_total_discount));
        mTvDiscountPrice.setText(getPriceFormat(mTvDiscountLabel, mTvDiscountPrice, shipmentCost.getDiscountAmount() * -1));
    }

    private void renderCashback(ShipmentCostModel shipmentCost) {
        mTvTotalCashbackLabel.setText(mTvTotalCashbackLabel.getContext().getString(R.string.label_total_cashback));
        mTvTotalCashbackAmount.setText(getPriceFormat(mTvTotalCashbackLabel, mTvTotalCashbackAmount, shipmentCost.getCashbackAmount()));
        if (shipmentCost.getCashbackAmount() > 0) {
            mRlTotalCashback.setVisibility(View.VISIBLE);
            mRlTotalCashback.setOnClickListener(v -> shipmentAdapterActionListener.showBottomSheetTotalBenefit());
        } else {
            mRlTotalCashback.setVisibility(View.GONE);
        }
    }

    private String getTotalItemLabel(Context context, int totalItem) {
        return String.format(context.getString(R.string.label_item_count_summary_with_format), totalItem);
    }

    private String getTotalPurchaseProtectionItemLabel(Context context, int totalItem) {
        return String.format(context.getString(R.string.label_item_count_purchase_protection), totalItem);
    }

    private String getPriceFormat(TextView textViewLabel, TextView textViewPrice, double price) {
        if (price == 0) {
            textViewLabel.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.GONE);
            return "-";
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textViewPrice.setVisibility(View.VISIBLE);
            return CurrencyFormatUtil.convertPriceValueToIdrFormat((long) price, false);
        }
    }

}