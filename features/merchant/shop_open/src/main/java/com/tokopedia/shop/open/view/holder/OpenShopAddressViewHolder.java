package com.tokopedia.shop.open.view.holder;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.shop.open.view.watcher.AfterTextWatcher;

import java.util.ArrayList;


/**
 * Created by Yehezkiel on 22/05/18.
 */

public class OpenShopAddressViewHolder {

    private EditText editTextInputShopAddress;
    private EditText editTextInputShopPostal;
    private TkpdHintTextInputLayout textInputAddress;
    private TkpdHintTextInputLayout textInputPostal;
    private Integer districtId;
    private ArrayList<String> postalCode;

    public OpenShopAddressViewHolder(View view, Context context, OpenShopAddressListener openShopAddressListener) {
        textInputAddress = view.findViewById(com.tokopedia.seller.R.id.text_input_address);
        textInputPostal = view.findViewById(com.tokopedia.seller.R.id.text_input_postal);
        editTextInputShopAddress = view.findViewById(com.tokopedia.seller.R.id.edit_text_input_address);
        editTextInputShopPostal = view.findViewById(com.tokopedia.seller.R.id.edit_text_input_postal);

        editTextInputShopAddress.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                textInputAddress.disableSuccessError();
                openShopAddressListener.buttonEnabledFalse();
                openShopAddressListener.hideSnackBarRetry();
                if (TextUtils.isEmpty(s)) {
                    textInputAddress.setError(context.getString(com.tokopedia.seller.R.string.shop_open_error_address));
                } else {
                    openShopAddressListener.checkEnableSubmit();
                }
            }
        });

        editTextInputShopAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShopAddressListener.navigateToDistrictChooser();
            }
        });

        editTextInputShopPostal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShopAddressListener.navigateToPostalChooser();
            }
        });

        editTextInputShopPostal.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                textInputPostal.disableSuccessError();
                openShopAddressListener.buttonEnabledFalse();
                openShopAddressListener.hideSnackBarRetry();
                if (TextUtils.isEmpty(s)) {
                    textInputPostal.setError(context.getString(com.tokopedia.seller.R.string.shop_open_error_postal));
                } else {
                    openShopAddressListener.checkEnableSubmit();
                }
            }
        });
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public ArrayList<String> getPostalCode() {
        return postalCode;
    }

    // Get Postal Code List from DistrictRecommendationFragment and store it,
    // then use it when users click the EditText Postal Code
    public void initPostalCode(ArrayList<String> postalCode) {
        this.postalCode = postalCode;
    }

    public void updateLocationView(String provinceName, String cityName, String districtName) {
        editTextInputShopAddress.setText(provinceName + ", " + cityName + ", " + districtName);
    }

    public void updatePostalCodeView(String postalCode){
        editTextInputShopPostal.setText(postalCode);
    }

    public void clearFocus(){
        editTextInputShopAddress.clearFocus();
        editTextInputShopPostal.clearFocus();
    }

    public interface OpenShopAddressListener {
        void checkEnableSubmit();
        void buttonEnabledFalse();
        void navigateToDistrictChooser();
        void hideSnackBarRetry();
        void navigateToPostalChooser();
    }
}
