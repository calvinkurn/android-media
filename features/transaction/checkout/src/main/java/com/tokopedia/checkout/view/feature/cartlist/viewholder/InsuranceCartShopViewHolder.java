package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.tokopedia.date.util.SaldoDatePickerUtil;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceApplicationValidation;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceProductApplicationDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsuranceCartShopViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_VIEW_INSURANCE_CART_SHOP = R.layout.insurance_cart_item_shop;
    private final TextView tvInsuranceApplicationDetails;

    private CheckBox cbSelectInsurance;
    private ImageView ivInsuranceIcon;
    private TextView tvInsuranceTitle;
    private TextView tvProductTitle;
    private TextView tvInsurancePrice;
    private TextView tvInsuranceInfo;
    private ImageView ivDeleteInsurance;
    private TextView tvChangeInsuranceApplicationDetails;
    private InsuranceCartShops insuranceCartShops;

    private InsuranceItemActionListener insuranceItemActionlistener;
    private Button btnValidate;
    private CloseableBottomSheetDialog closeableBottomSheetDialog;
    private String errorMessage;
    private SimpleDateFormat sdfo1;
    private SimpleDateFormat sdfo2;
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
        ivDeleteInsurance = itemView.findViewById(R.id.insurance_delete_icon);
        tvChangeInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_application_details_change);
        tvInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_appliation_details);
    }

    public void bindData(InsuranceCartShops insuranceCartShops, int position) {

        this.insuranceCartShops = insuranceCartShops;
        InsuranceCartDigitalProduct insuranceCartDigitalProduct = insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0);

        datePicker = new SaldoDatePickerUtil((Activity) ivInsuranceIcon.getContext());
        boolean isInsuranceSelected = insuranceCartDigitalProduct.getOptIn();

        /*
         * By default need to keep this checked for cart page
         * */

        cbSelectInsurance.setChecked(true);

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getTitle())) {
            tvProductTitle.setText(insuranceCartDigitalProduct.getProductInfo().getTitle());
        } else {
            tvProductTitle.setText("Produk Asuransi");
        }

        if (!TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getSectionTitle())) {
            tvInsuranceTitle.setText(insuranceCartDigitalProduct.getProductInfo().getSectionTitle());
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

                        /*ArrayList<InsuranceProductApplicationDetails> insuranceProductApplicationDetailsArrayList =
                                insuranceCartDigitalProduct.getApplicationDetails();*/


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
                                        updateEditTextBackground(subtitle, errorMessageView.getCurrentTextColor());
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

                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                                try {
                                    Date date = formatter.parse(dateText);
                                    String newDate = new SimpleDateFormat(DATE_FORMAT_VIEW).format(date);
                                    subTitleTextView.setText(newDate);
                                } catch (ParseException exception) {
                                    exception.printStackTrace();
                                }

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
                                            insuranceProductApplicationDetails.setValue(getDateStringInServerFormat(s.toString()));
                                            insuranceProductApplicationDetails.setError(false);
                                        } else {
                                            errorMessageView.setVisibility(View.VISIBLE);
                                            errorMessageView.setText(errorMessage);
                                            insuranceProductApplicationDetails.setError(true);
                                        }
                                        updateEditTextBackground(subTitleTextView, errorMessageView.getCurrentTextColor());
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
                insuranceItemActionlistener.deleteInsurance(insuranceCartShops, true);
            }
        });
    }


    private String getDateStringInServerFormat(String value) {

        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_VIEW);
        try {
            date = formatter.parse(value);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return newDate;
    }


    private void onDateViewClicked(TextView view) {
        String date = dateFormatter(view.getText().toString());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            view.setText(getDate(year, month, day));
        });
    }

    private String getDate(int year, int month, int day) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        return dateFormat.format(cal.getTime());
    }

    private int getStartYear(String date) {
        String year = date.substring(6, 10);
        return Integer.parseInt(year);
    }

    private int getStartMonth(String date) {
        String month = date.substring(3, 5);
        return Integer.parseInt(month);
    }

    private int getDay(String date) {
        String day = date.substring(0, 2);
        return Integer.parseInt(day);
    }

    private String dateFormatter(String date) {

        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        DateFormat sdf_ws = new SimpleDateFormat("dd/MM/yyyy");
        Date formattedStart = null;
        try {
            formattedStart = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdf_ws.format(formattedStart);

    }

    @SuppressLint("RestrictedApi")
    private void updateEditTextBackground(TextView subtitle, int currentTextColor) {
        if (subtitle == null) {
            return;
        }

        Drawable editTextBackground = subtitle.getBackground();

        if (editTextBackground == null) return;

        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(editTextBackground)) {
            editTextBackground = editTextBackground.mutate();
        }

        boolean isErrorShowing = !TextUtils.isEmpty(errorMessage);
        if (isErrorShowing) {
            // Set a color filter of the error color
            editTextBackground.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(
                    currentTextColor, PorterDuff.Mode.SRC_IN));
        } else {
            // Else reset the color filter and refresh the drawable state so that the
            // normal tint is used
            DrawableCompat.clearColorFilter(editTextBackground);
            subtitle.refreshDrawableState();
        }
    }

    private boolean validateView(TextView valueView, InsuranceProductApplicationDetails insuranceProductApplicationDetails) {
        errorMessage = "";

        for (InsuranceApplicationValidation insuranceApplicationValidation : insuranceProductApplicationDetails.getValidationsList()) {
            if (insuranceApplicationValidation.getType().equalsIgnoreCase("minLength")) {
                if (!validateMinLength(valueView.getText(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();
                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase("maxLength")) {
                if (!validateMaxLength(valueView.getText(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();

                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase("pattern")) {
                if (!validatePattern(valueView.getText(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();

                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase("minDate")) {
                if (!validateMinDate(valueView.getText().toString(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();
                    return false;
                }
            } else if (insuranceApplicationValidation.getType().equalsIgnoreCase("maxDate")) {

                if (!validateMaxDate(valueView.getText().toString(), insuranceApplicationValidation.getValidationValue())) {
                    errorMessage = insuranceApplicationValidation.getValidationErrorMessage();
                    return false;
                }
            }
        }
        return true;
    }

    private String DATE_FORMAT_VIEW = "dd MMM yyyy";


    private boolean validateMinDate(String text, String validationValue) {

        sdfo1 = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date incomingValue = null;
        try {
            incomingValue = sdfo1.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdfo2 = new SimpleDateFormat("yyyy-MM-dd");
        Date minDate = null;
        try {
            minDate = sdfo2.parse(validationValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (incomingValue != null && incomingValue.compareTo(minDate) >= 0) {
            System.out.println("incomingValue is after minDate");
            return true;
        } else {
            System.out.println("incomingValue is before minDate");
            return false;
        }
    }

    private boolean validateMaxDate(String text, String validationValue) {
        sdfo1 = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date incomingValue = null;
        try {
            incomingValue = sdfo1.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdfo2 = new SimpleDateFormat("yyyy-MM-dd");
        Date minDate = null;
        try {
            minDate = sdfo2.parse(validationValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (incomingValue.compareTo(minDate) > 0) {
            System.out.println("incomingValue is after minDate");
            return false;
        } else {
            System.out.println("incomingValue is before minDate");
            return true;
        }
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
