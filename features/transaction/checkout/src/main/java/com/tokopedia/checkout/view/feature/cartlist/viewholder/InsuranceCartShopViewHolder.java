package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.InsuranceItemActionListener;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceApplicationValidation;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceProductApplicationDetails;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private InsuranceCartShops insuranceCartShops;

    private InsuranceItemActionListener insuranceItemActionlistener;
    private Button btnValidate;
    private CloseableBottomSheetDialog closeableBottomSheetDialog;

    public InsuranceCartShopViewHolder(View itemView, InsuranceItemActionListener insuranceItemActionlistener) {
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

        this.insuranceCartShops = insuranceCartShops;
        InsuranceCartDigitalProduct insuranceCartDigitalProduct = insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0);

        boolean isInsuranceSelected = insuranceCartDigitalProduct.getOptIn();
        cbSelectInsurance.setChecked(isInsuranceSelected);

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getTitle())) {
            tvInsuranceTitle.setText(insuranceCartDigitalProduct.getProductInfo().getTitle());
        } else {
            tvInsuranceTitle.setText("Produk Asuransi");
        }

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getIconUrl())) {
            ImageHandler.loadImage(ivInsuranceIcon.getContext(), ivInsuranceIcon, insuranceCartDigitalProduct.getProductInfo().getIconUrl(), R.drawable.ic_modal_toko);
        }

        /*if (TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getLinkDetailInfoTitle())) {
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
        }*/

        if (!insuranceCartDigitalProduct.getApplicationDetails().isEmpty()) {

            StringBuilder applicationDetails = new StringBuilder();
            for (int i = 0; i < insuranceCartDigitalProduct.getApplicationDetails().size(); i++) {
                //InsuranceProductApplicationDetails insuranceProductApplicationDetails : insuranceCartDigitalProduct.getApplicationDetails()) {

                if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getApplicationDetails().get(i).getValue())) {
                    applicationDetails.append(insuranceCartDigitalProduct.getApplicationDetails().get(i).getValue());
                }

                if (i < insuranceCartDigitalProduct.getApplicationDetails().size() - 1) {
                    applicationDetails.append(" | ");
                }
            }

            if (!TextUtils.isEmpty(applicationDetails)) {
                tvInsuranceApplicationDetails.setText(applicationDetails);
                tvInsuranceApplicationDetails.setVisibility(View.VISIBLE);
                tvChangeInsuranceApplicationDetails.setVisibility(View.VISIBLE);

                tvChangeInsuranceApplicationDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        closeableBottomSheetDialog =
                                CloseableBottomSheetDialog.createInstanceRounded(tvChangeInsuranceApplicationDetails.getContext());


                        View rootView = LayoutInflater.from(tvChangeInsuranceApplicationDetails.getContext()).inflate(R.layout.layout_insurance_bottom_sheet, null, false);

                        LinearLayout applicationDetailsView = rootView.findViewById(R.id.ll_application_details);
                        btnValidate = rootView.findViewById(R.id.btn_validate);


                        ArrayList<InsuranceProductApplicationDetails> insuranceProductApplicationDetailsArrayList =
                                insuranceCartDigitalProduct.getApplicationDetails();


                        for (InsuranceProductApplicationDetails insuranceProductApplicationDetails :
                                insuranceProductApplicationDetailsArrayList) {


                            if (insuranceProductApplicationDetails.getType().equals("text")) {

                                View view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.getContext()).inflate(R.layout.application_detail_text, null, false);

                                ((TextView) view.findViewById(R.id.title)).setText(insuranceProductApplicationDetails.getLabel());
                                ((TextView) view.findViewById(R.id.sub_title)).setText(insuranceProductApplicationDetails.getValue());

                                applicationDetailsView.addView(view);
                                addToValuesList(view, insuranceProductApplicationDetails);

                            } else if (insuranceProductApplicationDetails.getType().equals("date")) {

                                View view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.getContext()).inflate(R.layout.application_detail_date, null, false);
                                applicationDetailsView.addView(view);
                                addToValuesList(view, insuranceProductApplicationDetails);

                                // TODO: 12/7/19 open new bottom sheet with  calender

                            }
                        }

                        setValidateListener();

                        closeableBottomSheetDialog.setContentView(rootView);
                        closeableBottomSheetDialog.show();
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
                insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0).setOptIn(isChecked);
                insuranceItemActionlistener.onInsuranceSelectStateChanges();
            }
        });

        ivDeleteInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insuranceItemActionlistener.deleteInsurance(insuranceCartShops);
            }
        });
    }

    private void setValidateListener() {
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateViews()) {
                    insuranceItemActionlistener.updateInsuranceProductData(insuranceCartShops, updateInsuranceProductApplicationDetailsArrayList);
                    closeableBottomSheetDialog.dismiss();
                }
            }
        });
    }

    ArrayList<UpdateInsuranceProductApplicationDetails> updateInsuranceProductApplicationDetailsArrayList = new ArrayList<>();

    private boolean validateViews() {
        for (TextView valueView : typeValues) {
            updateInsuranceProductApplicationDetailsArrayList.clear();
            InsuranceProductApplicationDetails data = (InsuranceProductApplicationDetails) valueView.getTag();
            for (InsuranceApplicationValidation validation : data.getValidationsList()) {
                if (validation.getType().equalsIgnoreCase("minLength")) {
                    if (!validateMinLength(valueView.getText(), validation.getValidationValue())) {
                        //show Some Error
                        Toast.makeText(ivInsuranceIcon.getContext(), "Min length Validation fail", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else if (validation.getType().equalsIgnoreCase("maxLength")) {
                    if (!validateMaxLength(valueView.getText(), validation.getValidationValue())) {
                        //show Some Error
                        Toast.makeText(ivInsuranceIcon.getContext(), "Max length Validation fail", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else if (validation.getType().equalsIgnoreCase("pattern")) {
                    if (!validatePattern(valueView.getText(), validation.getValidationValue())) {
                        //show Some Error
                        Toast.makeText(ivInsuranceIcon.getContext(), "Pattern Validation fail", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
            UpdateInsuranceProductApplicationDetails updateInsuranceProductApplicationDetails = new UpdateInsuranceProductApplicationDetails(data.getId(), valueView.getText().toString());
            updateInsuranceProductApplicationDetailsArrayList.add(updateInsuranceProductApplicationDetails);
            return true;
        }
        return true;
    }

    public boolean validatePattern(CharSequence value, String regExPattern) {
        if (value == null) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(regExPattern);
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
    }

    private boolean validateMaxLength(CharSequence text, String maxLength) {
        return TextUtils.isEmpty(text) || text.length() <= Integer.valueOf(maxLength);
    }

    private boolean validateMinLength(CharSequence text, String minLength) {
        return !TextUtils.isEmpty(text) && text.length() >= Integer.valueOf(minLength);
    }

    private ArrayList<TextView> typeValues = new ArrayList<>();

    private void addToValuesList(View view, InsuranceProductApplicationDetails data) {
        TextView view1 = view.findViewById(R.id.sub_title);
        view1.setTag(data);
        typeValues.add(view1);

    }
}
