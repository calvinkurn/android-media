package com.tokopedia.editshipping.ui.customview;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.domain.model.editshipping.ShopShipping;
import com.tokopedia.editshipping.presenter.EditShippingPresenter;
import com.tokopedia.editshipping.ui.EditShippingViewListener;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingAddressLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener>{

    EditText addressArea;
    EditText chooseLocation;
    TextView phoneNumber;
    TextView phoneNumberTitle;
    TextView phoneNumberButton;
    private static final String EXTRA_EXISTING_LOCATION = "EXTRA_EXISTING_LOCATION";

    private EditShippingPresenter presenter;
    private EditShippingViewListener mainView;

    public ShippingAddressLayout(Context context) {
        super(context);
    }

    public ShippingAddressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShippingAddressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindView(View view) {
        addressArea = (EditText) view.findViewById(R.id.address_text_field);

        chooseLocation = (EditText) view.findViewById(R.id.value_location);

        phoneNumber = (TextView) view.findViewById(R.id.shop_phone_number);
        phoneNumberTitle = (TextView) view.findViewById(R.id.shop_phone_number_title);

        phoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);

        chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.openGeoLocation();
            }
        });
        addressArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.saveAddressArea(s.toString());
            }
        });

    }

    @Override
    protected int getLayoutView() {
        return R.layout.edit_shipping_address_layout;
    }

    @Override
    public void renderData(@NonNull ShopShipping data) {
        addressArea.setText(data.addrStreet);
    }

    public void renderGeoAddress(String address) {
        chooseLocation.setText(address);
    }

    @Override
    public void setListener(EditShippingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

    public void setGoogleMapData(Intent data){
        LocationPass locationPass = data.getParcelableExtra(EXTRA_EXISTING_LOCATION);
        if(locationPass != null && locationPass.getLatitude() != null) {
            presenter.getShopInformation().setShopLatitude(locationPass.getLatitude());
            presenter.getShopInformation().setShopLongitude(locationPass.getLongitude());
            chooseLocation.setText(getReverseGeocode(locationPass));
        }
    }

    private String getReverseGeocode(LocationPass locationPass) {
        if (locationPass.getGeneratedAddress().equals(getContext().getString(R.string.choose_this_location))) {
            return locationPass.getLatitude() + ", " + locationPass.getLongitude();
        } else {
            return locationPass.getGeneratedAddress();
        }
    }

    public String getGoogleMapAddressString(){
        return chooseLocation.getText().toString();
    }

    public String getAddressData(){
        return addressArea.getText().toString();
    }
}
