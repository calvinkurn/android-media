package com.tokopedia.editshipping.ui.customview;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.databinding.EditShippingLocationLayoutBinding;
import com.tokopedia.editshipping.domain.model.editshipping.ShopShipping;
import com.tokopedia.editshipping.presenter.EditShippingPresenter;
import com.tokopedia.editshipping.ui.EditShippingViewListener;

import java.util.ArrayList;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingHeaderLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener, EditShippingLocationLayoutBinding> {

    private static final int POSTAL_CODE_LENGTH = 5;
    EditShippingPresenter presenter;
    EditShippingViewListener mainView;
//    AutoCompleteTextView zipCode;
//    EditText shopCity;
//    TextInputLayout postalTextInputLayout;
    ArrayAdapter<String> zipCodeAdapter;

    public ShippingHeaderLayout(Context context) {
        super(context);
    }

    public ShippingHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShippingHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindView(EditShippingLocationLayoutBinding view) {
//        zipCode = view.findViewById(R.id.postal_code);
//        shopCity = view.findViewById(R.id.text_edit_shipping_city);
//        postalTextInputLayout = view.findViewById(R.id.text_input_layout_postal_code);
        view.postalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.savePostalCode(s.toString());
            }
        });
        View.OnClickListener editAddressSpinnerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.editAddress();
            }
        };
        // todo make sure this one
        view.getRoot().setOnClickListener(editAddressSpinnerClickListener);
        view.textEditShippingCity.setOnClickListener(editAddressSpinnerClickListener);

        view.postalCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!view.postalCode.isPopupShowing()) {
                    view.postalCode.showDropDown();
                }
                return false;
            }
        });

        view.postalCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 && !Character.isDigit(getBinding().postalCode.getText().toString().charAt(0))) {
                    getBinding().postalCode.setText("");
                }
            }
        });
    }

    public void initializeZipCodes() {
        getBinding().postalCode.setText("");
        String header = getResources().getString(R.string.hint_type_postal_code);
        ArrayList<String> zipCodes = presenter.getselectedAddress().getZipCodes();
        if (!zipCodes.contains(header)) {
            zipCodes.add(0, header);
        }
        zipCodeAdapter = new ArrayAdapter<>(getContext(), com.tokopedia.design.R.layout.item_autocomplete_text_double_row,
                com.tokopedia.design.R.id.item, presenter.getselectedAddress().getZipCodes());
        getBinding().postalCode.setAdapter(zipCodeAdapter);
    }

    @Override
    protected EditShippingLocationLayoutBinding getLayoutView(Context context) {
        return EditShippingLocationLayoutBinding.inflate(LayoutInflater.from(context), this, true);
    }

    @Override
    public void renderData(@NonNull ShopShipping shopData) {
        getBinding().postalCode.setText(shopData.postalCode);
        if (!TextUtils.isEmpty(shopData.districtName) && !TextUtils.isEmpty(shopData.cityName) &&
                !TextUtils.isEmpty(shopData.provinceName)) {
            updateLocationData(shopData.provinceName, shopData.cityName, shopData.districtName);
        }
    }

    @Override
    public void setListener(EditShippingPresenter presenter) {
        this.presenter = presenter;
        getBinding().postalCode.addTextChangedListener(postTextWatcher());
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

    public void updateLocationData(String provinceName, String cityName, String districtName) {
        getBinding().textEditShippingCity.setText(provinceName + ", " + cityName + ", " + districtName);
    }

    public void updateLocationData(String location) {
        getBinding().textEditShippingCity.setText(location);
    }

    public void setZipCodeError(String error) {
        getBinding().textInputLayoutPostalCode.setError(error);
        getBinding().postalCode.requestFocus();
    }

    public String getZipCodeData() {
        return getBinding().postalCode.getText().toString();
    }

    public String getDistrictAndCity() {
        return getBinding().textEditShippingCity.getText().toString();
    }

    private TextWatcher postTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (getBinding().postalCode.getText().length() < 1)
                    getBinding().textInputLayoutPostalCode.setError(getContext().getString(R.string.error_field_required));
                else getBinding().textInputLayoutPostalCode.setError(null);
            }
        };
    }
}
