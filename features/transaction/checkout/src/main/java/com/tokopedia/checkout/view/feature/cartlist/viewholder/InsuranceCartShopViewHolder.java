package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.InsuranceItemActionListener;
import com.tokopedia.date.util.SaldoDatePickerUtil;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceApplicationValidation;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceProductApplicationDetails;

import java.util.ArrayList;

import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.VALIDATION_TYPE_MAX_DATE;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.VALIDATION_TYPE_MAX_LENGTH;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.VALIDATION_TYPE_MIN_DATE;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.VALIDATION_TYPE_MIN_LENGTH;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.VALIDATION_TYPE_PATTERN;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.dateFormatter;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.getDate;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.getDateInServerFormat;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.getDateStringInUIFormat;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.getDay;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.getStartMonth;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.getStartYear;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.updateEditTextBackground;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.validateMaxDate;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.validateMaxLength;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.validateMinDate;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.validateMinLength;
import static com.tokopedia.transactiondata.utils.TransactionalInsuranceUtilsKt.validatePattern;

public class InsuranceCartShopViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_VIEW_INSURANCE_CART_SHOP = R.layout.insurance_cart_item_shop;
    private final TextView tvInsuranceApplicationDetails;

    private CheckBox cbSelectInsurance;
    private ImageView ivInsuranceIcon;
    private TextView tvInsuranceTitle;
    private TextView tvProductTitle;
    private TextView tvInsurancePrice;
    private TextView tvInsuranceInfo;
    private TextView tvInsuranceSubtitle;
    private TextView tvInsuranceTickerText;
    private ImageView ivDeleteInsurance;
    private TextView tvChangeInsuranceApplicationDetails;
    private InsuranceCartShops insuranceCartShops;
    private ArrayList<TextView> typeValues = new ArrayList<>();

    private InsuranceItemActionListener insuranceItemActionlistener;
    private Button btnValidate;
    private CloseableBottomSheetDialog closeableBottomSheetDialog;
    private String errorMessage;
    private SaldoDatePickerUtil datePicker;

    public InsuranceCartShopViewHolder(View itemView, InsuranceItemActionListener insuranceItemActionlistener) {
        super(itemView);
        this.insuranceItemActionlistener = insuranceItemActionlistener;

        cbSelectInsurance = itemView.findViewById(R.id.insurance_checkbox);
        ivInsuranceIcon = itemView.findViewById(R.id.insurance_image_icon);
        tvInsuranceTitle = itemView.findViewById(R.id.insurance_tv_title);
        tvProductTitle = itemView.findViewById(R.id.tv_product_title);
        tvInsurancePrice = itemView.findViewById(R.id.insurance_tv_price);
        tvInsuranceInfo = itemView.findViewById(R.id.insurance_tv_info);
        tvInsuranceSubtitle = itemView.findViewById(R.id.insurance_tv_subtitle);
        ivDeleteInsurance = itemView.findViewById(R.id.insurance_delete_icon);
        tvInsuranceTickerText = itemView.findViewById(R.id.tv_ticker_text);
        tvChangeInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_application_details_change);
        tvInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_appliation_details);
    }

    public void bindData(InsuranceCartShops insuranceCartShops, int position) {

        this.insuranceCartShops = insuranceCartShops;
        InsuranceCartDigitalProduct insuranceCartDigitalProduct = insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0);

        datePicker = new SaldoDatePickerUtil((Activity) ivInsuranceIcon.getContext());

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getSectionTitle())) {
            tvProductTitle.setText(insuranceCartDigitalProduct.getProductInfo().getSectionTitle());
        } else {
            tvProductTitle.setText("Produk Asuransi");
        }

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getTitle())) {
            tvInsuranceTitle.setText(insuranceCartDigitalProduct.getProductInfo().getTitle());
        } else {
            tvInsuranceTitle.setText("Produk Asuransi");
        }

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getSubTitle())) {
            tvInsuranceSubtitle.setText(insuranceCartDigitalProduct.getProductInfo().getSubTitle());
            tvInsuranceSubtitle.setVisibility(View.VISIBLE);
        } else {
            tvInsuranceSubtitle.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getTickerText())) {
            tvInsuranceTickerText.setText(insuranceCartDigitalProduct.getProductInfo().getTickerText());
            tvInsuranceTickerText.setVisibility(View.VISIBLE);
        } else {
            tvInsuranceTickerText.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getIconUrl())) {
            ImageHandler.loadImage(ivInsuranceIcon.getContext(), ivInsuranceIcon, insuranceCartDigitalProduct.getProductInfo().getIconUrl(), R.drawable.ic_modal_toko);
        }

        if (TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getLinkName())) {
            tvInsuranceInfo.setVisibility(View.GONE);
        } else {
            tvInsuranceInfo.setVisibility(View.VISIBLE);
            tvInsuranceInfo.setText(insuranceCartDigitalProduct.getProductInfo().getLinkName());
            tvInsuranceInfo.setOnClickListener(v -> {
                // TODO: 19/6/19 open bottom sheet with url
                CloseableBottomSheetDialog dealsCategoryBottomSheet =
                        CloseableBottomSheetDialog.createInstanceRounded(tvInsuranceInfo.getContext());

            });
        }

        if (!insuranceCartDigitalProduct.getApplicationDetails().isEmpty()) {

            StringBuilder applicationDetails = new StringBuilder();
            for (int i = 0; i < insuranceCartDigitalProduct.getApplicationDetails().size(); i++) {
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

                        typeValues.clear();
                        for (InsuranceProductApplicationDetails insuranceProductApplicationDetails :
                                insuranceCartDigitalProduct.getApplicationDetails()) {

                            if (insuranceProductApplicationDetails.getType().equalsIgnoreCase("text") ||
                                    insuranceProductApplicationDetails.getType().equalsIgnoreCase("number")) {

                                View view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.getContext()).inflate(R.layout.application_detail_text, null, false);

                                TextView subtitle = view.findViewById(R.id.sub_title);
                                TextView errorMessageView = view.findViewById(R.id.error_message);

                                ((TextView) view.findViewById(R.id.tv_title)).setText(insuranceProductApplicationDetails.getLabel());
                                subtitle.setText(insuranceProductApplicationDetails.getValue());

                                subtitle.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (validateView(subtitle, insuranceProductApplicationDetails)) {
                                            errorMessageView.setVisibility(View.GONE);
                                        } else {
                                            errorMessageView.setVisibility(View.VISIBLE);
                                            errorMessageView.setText(errorMessage);
                                        }
                                        updateEditTextBackground(subtitle, errorMessageView.getCurrentTextColor(), !TextUtils.isEmpty(errorMessage));
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                applicationDetailsView.addView(view);
                                addToValuesList(view, insuranceProductApplicationDetails);

                            } else if (insuranceProductApplicationDetails.getType().equalsIgnoreCase("date")) {

                                View view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.getContext()).inflate(R.layout.application_detail_date, null, false);

                                ((TextView) view.findViewById(R.id.title)).setText(insuranceProductApplicationDetails.getLabel());
                                TextView subTitleTextView = view.findViewById(R.id.sub_title);
                                TextView errorMessageView = view.findViewById(R.id.error_message);

                                String dateText = insuranceProductApplicationDetails.getValue();
                                subTitleTextView.setText(getDateStringInUIFormat(dateText));
                                subTitleTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onDateViewClicked(subTitleTextView);
                                    }
                                });

                                subTitleTextView.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        if (validateView(subTitleTextView, insuranceProductApplicationDetails)) {
                                            errorMessageView.setVisibility(View.GONE);
                                            insuranceProductApplicationDetails.setValue(getDateInServerFormat(s.toString()));
                                            insuranceProductApplicationDetails.setError(false);
                                        } else {
                                            errorMessageView.setVisibility(View.VISIBLE);
                                            errorMessageView.setText(errorMessage);
                                            insuranceProductApplicationDetails.setError(true);
                                        }
                                        updateEditTextBackground(subTitleTextView, errorMessageView.getCurrentTextColor(), !TextUtils.isEmpty(errorMessage));
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                applicationDetailsView.addView(view);
                                addToValuesList(view, insuranceProductApplicationDetails);

                            } else if (insuranceProductApplicationDetails.getType().equalsIgnoreCase("dropdown")) {

                                View view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.getContext()).inflate(R.layout.application_detail_date, null, false);

                                ((TextView) view.findViewById(R.id.title)).setText(insuranceProductApplicationDetails.getLabel());
                                TextView subTitleTextView = view.findViewById(R.id.sub_title);
                                if (!TextUtils.isEmpty(insuranceProductApplicationDetails.getValue())) {
                                    subTitleTextView.setText(insuranceProductApplicationDetails.getValue());
                                } else {
                                    subTitleTextView.setText(insuranceProductApplicationDetails.getPlaceHolder());
                                }

                                TextView errorMessageView = view.findViewById(R.id.error_message);

                                applicationDetailsView.addView(view);
                                addToValuesList(view, insuranceProductApplicationDetails);
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

        cbSelectInsurance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0).setOptIn(isChecked);
            insuranceItemActionlistener.onInsuranceSelectStateChanges();
        });

        /*
         * By default need to keep this checked for cart page
         * */

        cbSelectInsurance.setChecked(true);

        ivDeleteInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<InsuranceCartDigitalProduct> insuranceCartDigitalProductArrayList = new ArrayList<>();
                insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct);
                insuranceItemActionlistener.deleteMacroInsurance(insuranceCartDigitalProductArrayList, true);
            }
        });
    }

    private void onDateViewClicked(TextView view) {
        String date = dateFormatter(view.getText().toString());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            view.setText(getDate(year, month, day));
        });
    }

    private boolean validateView(TextView valueView, InsuranceProductApplicationDetails insuranceProductApplicationDetails) {
        errorMessage = "";

        for (InsuranceApplicationValidation insuranceApplicationValidation : insuranceProductApplicationDetails.getValidationsList()) {
            if (insuranceApplicationValidation.getType().equalsIgnoreCase(VALIDATION_TYPE_MIN_LENGTH)) {
                if (!validateMinLength(valueView.getText(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();
                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase(VALIDATION_TYPE_MAX_LENGTH)) {
                if (!validateMaxLength(valueView.getText(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();

                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase(VALIDATION_TYPE_PATTERN)) {
                if (!validatePattern(valueView.getText(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();

                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase(VALIDATION_TYPE_MIN_DATE)) {
                if (!validateMinDate(valueView.getText().toString(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();
                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase(VALIDATION_TYPE_MAX_DATE)) {

                if (!validateMaxDate(valueView.getText().toString(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();
                    return false;
                }
            }
        }
        return true;
    }

    private void setValidateListener() {
        btnValidate.setOnClickListener(v -> {
            if (validateViews()) {
                insuranceItemActionlistener.updateInsuranceProductData(insuranceCartShops, updateInsuranceProductApplicationDetailsArrayList);
                closeableBottomSheetDialog.dismiss();
            }
        });
    }

    private ArrayList<UpdateInsuranceProductApplicationDetails> updateInsuranceProductApplicationDetailsArrayList = new ArrayList<>();

    private boolean validateViews() {
        String updatedValue = "";
        updateInsuranceProductApplicationDetailsArrayList.clear();
        for (TextView valueView : typeValues) {
            InsuranceProductApplicationDetails data = (InsuranceProductApplicationDetails) valueView.getTag();
            for (InsuranceApplicationValidation validation : data.getValidationsList()) {
                if (validation.getType().equalsIgnoreCase(VALIDATION_TYPE_MIN_LENGTH)) {
                    if (!validateMinLength(valueView.getText(), validation.getValidationValue())) {
                        //show Some Error
                        errorMessage = validation.getValidationErrorMessage();
                        Toast.makeText(ivInsuranceIcon.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        updatedValue = valueView.getText().toString();
                    }
                } else if (validation.getType().equalsIgnoreCase(VALIDATION_TYPE_MAX_LENGTH)) {
                    if (!validateMaxLength(valueView.getText(), validation.getValidationValue())) {
                        //show Some Error
                        errorMessage = validation.getValidationErrorMessage();
                        Toast.makeText(ivInsuranceIcon.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        updatedValue = valueView.getText().toString();
                    }
                } else if (validation.getType().equalsIgnoreCase(VALIDATION_TYPE_PATTERN)) {
                    if (!validatePattern(valueView.getText(), validation.getValidationValue())) {
                        //show Some Error
                        errorMessage = validation.getValidationErrorMessage();
                        Toast.makeText(ivInsuranceIcon.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        updatedValue = valueView.getText().toString();
                    }
                } else if (validation.getType().equalsIgnoreCase(VALIDATION_TYPE_MIN_DATE)) {
                    if (!validateMinDate(valueView.getText().toString(), validation.getValidationValue())) {
                        errorMessage = validation.getValidationErrorMessage();
                        Toast.makeText(ivInsuranceIcon.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        updatedValue = getDateInServerFormat(valueView.getText().toString());
                    }
                } else if (validation.getType().equalsIgnoreCase(VALIDATION_TYPE_MAX_DATE)) {

                    if (!validateMaxDate(valueView.getText().toString(), validation.getValidationValue())) {
                        errorMessage = validation.getValidationErrorMessage();
                        Toast.makeText(ivInsuranceIcon.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        updatedValue = getDateInServerFormat(valueView.getText().toString());
                    }
                }
            }

            UpdateInsuranceProductApplicationDetails updateInsuranceProductApplicationDetails =
                    new UpdateInsuranceProductApplicationDetails(data.getId(), updatedValue);
            updateInsuranceProductApplicationDetailsArrayList.add(updateInsuranceProductApplicationDetails);
        }
        return true;
    }

    private void addToValuesList(View view, InsuranceProductApplicationDetails data) {
        TextView view1 = view.findViewById(R.id.sub_title);
        view1.setTag(data);
        typeValues.add(view1);

    }
}
