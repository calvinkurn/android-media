package com.tokopedia.tkpd.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.shipping.fragment.EditShippingViewListener;
import com.tokopedia.tkpd.shipping.fragment.ShippingLocationDialog;
import com.tokopedia.tkpd.shipping.model.editshipping.ShopShipping;
import com.tokopedia.tkpd.shipping.presenter.EditShippingPresenter;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingHeaderLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener>{

    EditShippingPresenter presenter;

    EditShippingViewListener mainView;

    @Bind(R2.id.postal_code) EditText zipCode;

    @Bind(R2.id.title_edit_shipping_change_location)
    TextView editShippingProvinceCitiesDistrict;

    @Bind(R2.id.text_edit_shipping_province)
    EditText shopProvince;

    @Bind(R2.id.text_edit_shipping_city) EditText shopCity;

    @Bind(R2.id.text_edit_shipping_district) EditText shopDistrict;

    @Bind(R2.id.text_input_layout_postal_code)
    TextInputLayout postalTextInputLayout;

    @OnTextChanged(R2.id.postal_code)
    void savePostalCodeToModel(){
        presenter.savePostalCode(zipCode.getText().toString());
    }

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
    protected int getLayoutView() {
        return R.layout.edit_shipping_location_layout;
    }

    @Override
    public void renderData(@NonNull ShopShipping shopData) {
        zipCode.setText(shopData.postalCode);
        setCityAndDistrictVisibility(shopData.districtId == ShippingLocationDialog.JAKARTA_ORIGIN_ID);
    }

    @Override
    public void setListener(EditShippingPresenter presenter) {
        this.presenter = presenter;
        zipCode.addTextChangedListener(postTextWatcher());
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

    public void updateLocationData(String provinceName, String cityName, String districtName){
        shopProvince.setText(provinceName);
        shopCity.setText(cityName);
        shopDistrict.setText(districtName);
        setCityAndDistrictVisibility(cityName.isEmpty() && districtName.isEmpty());
    }

    private void setCityAndDistrictVisibility(boolean shouldNotBeVisible){
        if(shouldNotBeVisible){
            shopCity.setVisibility(View.GONE);
            shopDistrict.setVisibility(View.GONE);
        }
        else{
            shopCity.setVisibility(View.VISIBLE);
            shopDistrict.setVisibility(View.VISIBLE);
        }
    }

    public void setEditShippingLocationButtonTitle(String title) {
        editShippingProvinceCitiesDistrict
                .setText(title);
    }

    public void setZipCodeError(String error){
        postalTextInputLayout.setError(error);
        zipCode.requestFocus();
    }

    public String getZipCodeData(){
        return zipCode.getText().toString();
    }

    @OnClick({R2.id.title_edit_shipping_change_location,
            R2.id.fragment_shipping_header,
            R2.id.text_edit_shipping_province,
            R2.id.text_edit_shipping_city,
            R2.id.text_edit_shipping_district})
    void editAddressSpinner(){
        mainView.editAddressSpinner();
    }

    private TextWatcher postTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(zipCode.getText().length() < 1) postalTextInputLayout.setError(getContext().getString(R.string.error_field_required));
                else postalTextInputLayout.setError(null);
            }
        };
    }
}
