package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceProductApplicationDetails;

public class InsuranceCartShopViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_VIEW_INSURANCE_CART_SHOP = R.layout.insurance_cart_item_shop;
    private final TextView tvInsuranceApplicationDetails;

    private CheckBox cbSelectInsurance;
    private ImageView ivInsuranceIcon;
    private TextView tvInsuranceTitle;
    private TextView tvInsurancePrice;
    private TextView tvInsuranceInfo;
    private ImageView ivDeleteInsurance;
    private TextView tvChangeInsuranceApplicationDetails;

    private CartAdapter.InsuranceItemActionlistener insuranceItemActionlistener;

    public InsuranceCartShopViewHolder(View itemView, CartAdapter.InsuranceItemActionlistener insuranceItemActionlistener) {
        super(itemView);
        this.insuranceItemActionlistener = insuranceItemActionlistener;

        cbSelectInsurance = itemView.findViewById(R.id.insurance_checkbox);
        ivInsuranceIcon = itemView.findViewById(R.id.insurance_image_icon);
        tvInsuranceTitle = itemView.findViewById(R.id.insurance_tv_title);
        tvInsurancePrice = itemView.findViewById(R.id.insurance_tv_price);
        tvInsuranceInfo = itemView.findViewById(R.id.insurance_tv_info);
        ivDeleteInsurance = itemView.findViewById(R.id.insurance_delete_icon);
        tvChangeInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_application_details_change);
        tvInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_appliation_details);
    }

    public void bindData(InsuranceCartShops insuranceCartShops, int position) {

        InsuranceCartDigitalProduct insuranceCartDigitalProduct = insuranceCartShops.getShopIemsList().get(0).getDigitalProductList().get(0);

        boolean isInsuranceSelected = insuranceCartDigitalProduct.getOptIn();
        cbSelectInsurance.setChecked(isInsuranceSelected);

        insuranceCartDigitalProduct.getProductInfo();

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getTitle())) {
            tvInsuranceTitle.setText(insuranceCartDigitalProduct.getProductInfo().getTitle());
        } else {
            tvInsuranceTitle.setText("Produk Asuransi");
        }

        if (TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getLinkDetailInfoTitle())) {
            tvInsuranceInfo.setVisibility(View.GONE);
        } else {
            tvInsuranceInfo.setVisibility(View.VISIBLE);
            tvInsuranceInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 19/6/19 open bottom sheet with url
                    CloseableBottomSheetDialog dealsCategoryBottomSheet =
                            CloseableBottomSheetDialog.createInstanceRounded(tvInsuranceInfo.getContext());

                }
            });
        }

        if (!insuranceCartDigitalProduct.getApplicationDetails().isEmpty()) {

            StringBuilder applicationDetails = new StringBuilder();
            for (InsuranceProductApplicationDetails insuranceProductApplicationDetails : insuranceCartDigitalProduct.getApplicationDetails()) {
                if (!TextUtils.isEmpty(insuranceProductApplicationDetails.getValue())) {
                    applicationDetails.append(insuranceProductApplicationDetails.getValue()).append(" | ");
                }
            }

            if (!TextUtils.isEmpty(applicationDetails)) {
                tvInsuranceApplicationDetails.setText(applicationDetails);
                tvInsuranceApplicationDetails.setVisibility(View.VISIBLE);
                tvChangeInsuranceApplicationDetails.setVisibility(View.VISIBLE);

                tvChangeInsuranceApplicationDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 19/6/19 open bottom sheet to change application details
                    }
                });
            } else {
                tvInsuranceApplicationDetails.setVisibility(View.GONE);
                tvChangeInsuranceApplicationDetails.setVisibility(View.GONE);
            }

        } else {
            tvInsuranceApplicationDetails.setVisibility(View.GONE);
            tvChangeInsuranceApplicationDetails.setVisibility(View.GONE);

        }

        tvInsurancePrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceCartDigitalProduct.getPricePerProduct(), false));

        cbSelectInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        ivDeleteInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insuranceItemActionlistener.deleteInsurance(insuranceCartShops);

            }
        });
    }
}
