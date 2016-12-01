package com.tokopedia.core.shipping.customview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.shipping.fragment.EditShippingViewListener;
import com.tokopedia.core.shipping.model.editshipping.ShopShipping;
import com.tokopedia.core.shipping.presenter.EditShippingPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingAddressLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener>{

    @BindView(R2.id.address_text_field)
    EditText addressArea;

    @BindView(R2.id.value_location) EditText chooseLocation;

    @BindView(R2.id.shop_phone_number)
    TextView phoneNumber;

    @BindView(R2.id.shop_phone_number_title) TextView phoneNumberTitle;

    @BindView(R2.id.change_phone_number_button) TextView phoneNumberButton;

    @OnClick(R2.id.value_location)
    void openGeoLocationMap(){
        mainView.openGeoLocation();
    }

    @OnTextChanged(R2.id.address_text_field)
    void saveAddressArea(){
        presenter.saveAddressArea(addressArea.getText().toString());
    }

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
    protected int getLayoutView() {
        return R.layout.edit_shipping_address_layout;
    }

    @Override
    public void renderData(@NonNull ShopShipping data) {
        addressArea.setText(data.addrStreet);
        setGoogleMapAddress(data.getShopLongitude(), data.getShopLatitude());
    }

    @Override
    public void setListener(EditShippingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

    private void setGoogleMapAddress(String longitude, String latitude) {
        try {
            chooseLocation.setText(GeoLocationUtils.reverseGeoCode(getContext(), Double.parseDouble(latitude), Double.parseDouble(longitude)));
        } catch (NumberFormatException e){
            chooseLocation.setText("");
            e.printStackTrace();
        } catch (Exception e) {
            chooseLocation.setText(getContext().getString(R.string.shop_coordinate)
                    .replace("LAT", latitude)
                    .replace("LONG", longitude));
            e.printStackTrace();
        }
    }

    public void setGoogleMapData(Intent data){
        LocationPass locationPass = data.getParcelableExtra(GeolocationActivity.EXTRA_EXISTING_LOCATION);
        presenter.getShopInformation().setShopLatitude(locationPass.getLatitude());
        presenter.getShopInformation().setShopLongitude(locationPass.getLongitude());
        chooseLocation.setText(getReverseGeocode(locationPass));
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
