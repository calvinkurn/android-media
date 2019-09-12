package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
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
    private RelativeLayout mRlTotalPromo;
    private TextView mTvTotalPromoStackAmount;
    private TextView mTvTotalPromoStackLabel;
    private TextView mTvOrderPrioritasLabel;
    private TextView mTvOrderPrioritasFee;

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
        mRlTotalPromo = itemView.findViewById(R.id.rl_total_promo);
        mTvTotalPromoStackAmount = itemView.findViewById(R.id.tv_total_promo_amount);
        mTvTotalPromoStackLabel = itemView.findViewById(R.id.tv_total_promo_label);
        mTvOrderPrioritasLabel = itemView.findViewById(R.id.tv_order_prioritas_label);
        mTvOrderPrioritasFee = itemView.findViewById(R.id.tv_order_prioritas_price);

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
        mTvOrderPrioritasFee.setText(getPriceFormat(mTvOrderPrioritasLabel,mTvOrderPrioritasFee,shipmentCost.getPriorityFee()));
        mTvPurchaseProtectionLabel.setText(getTotalPurchaseProtectionItemLabel(mTvPurchaseProtectionLabel.getContext(), shipmentCost.getTotalPurchaseProtectionItem()));
        mTvPurchaseProtectionFee.setText(getPriceFormat(mTvPurchaseProtectionLabel, mTvPurchaseProtectionFee, shipmentCost.getPurchaseProtectionFee()));
        mTvPromoDiscount.setText(String.format(mTvPromoDiscount.getContext().getString(R.string.promo_format),
                getPriceFormat(mTvPromoOrCouponLabel, mTvPromoDiscount, shipmentCost.getPromoPrice())));
        mTvSellerCostAdditionFee.setText(getPriceFormat(mTvSellerCostAdditionLabel, mTvSellerCostAdditionFee, shipmentCost.getAdditionalFee()));
        mTvDonationPrice.setText(getPriceFormat(mTvDonationLabel, mTvDonationPrice, shipmentCost.getDonation()));
        mTvEmasPrice.setText(getPriceFormat(mTvEmasLabel, mTvEmasPrice, shipmentCost.getEmasPrice()));
        mTvTradeInPrice.setText(String.format(mTvTradeInPrice.getContext().getString(R.string.promo_format),
                getPriceFormat(mTvTradeInLabel, mTvTradeInPrice, shipmentCost.getTradeInPrice())));

        if (shipmentCost.getTotalPromoStackAmount() > 0) {
            mRlTotalPromo.setVisibility(View.VISIBLE);
            mTvTotalPromoStackAmount.setText(shipmentCost.getTotalPromoStackAmountStr());
            mRlTotalPromo.setOnClickListener(v -> shipmentAdapterActionListener.showBottomSheetTotalBenefit());
        } else {
            mRlTotalPromo.setVisibility(View.GONE);
        }
    }

    public void hideTotalNilaiPromo() {
        mRlTotalPromo.setVisibility(View.GONE);
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