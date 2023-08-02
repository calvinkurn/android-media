package com.tokopedia.editshipping.ui.customview;

import static com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_SAVE_DATA_UI_MODEL;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.databinding.EditShippingAddressLayoutBinding;
import com.tokopedia.editshipping.domain.model.editshipping.ShopShipping;
import com.tokopedia.editshipping.presenter.EditShippingPresenter;
import com.tokopedia.editshipping.ui.EditShippingViewListener;
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public class ShippingAddressLayout extends EditShippingCustomView<ShopShipping,
        EditShippingPresenter,
        EditShippingViewListener, EditShippingAddressLayoutBinding> {
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
    protected void bindView(EditShippingAddressLayoutBinding view) {
        view.editShippingMapView.valueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.openGeoLocation();
            }
        });
        view.addressTextField.addTextChangedListener(new TextWatcher() {
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
    protected EditShippingAddressLayoutBinding getLayoutView(Context context) {
        return EditShippingAddressLayoutBinding.inflate(LayoutInflater.from(context), this, true);
    }

    @Override
    public void renderData(@NonNull ShopShipping data) {
        getBinding().addressTextField.setText(data.addrStreet);
    }

    public void renderGeoAddress(String address) {
        getBinding().editShippingMapView.valueLocation.setText(address);
    }

    @Override
    public void setListener(EditShippingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

    public void setGoogleMapData(Intent data) {
        LocationPass locationPass = data.getParcelableExtra(EXTRA_EXISTING_LOCATION);
        if (locationPass != null && locationPass.getLatitude() != null) {
            if (presenter.getShopInformation() != null) {
                presenter.getShopInformation().setShopLatitude(locationPass.getLatitude());
                presenter.getShopInformation().setShopLongitude(locationPass.getLongitude());
            }
            renderGeoAddress(getReverseGeocode(locationPass));
        } else {
            SaveAddressDataModel addressData = data.getParcelableExtra(EXTRA_SAVE_DATA_UI_MODEL);
            if (addressData != null) {
                if (presenter.getShopInformation() != null) {
                    presenter.getShopInformation().setShopLatitude(addressData.getLatitude());
                    presenter.getShopInformation().setShopLongitude(addressData.getLongitude());
                }
                renderGeoAddress(addressData.getFormattedAddress());
            }
        }
    }

    private String getReverseGeocode(LocationPass locationPass) {
        if (locationPass.getGeneratedAddress().equals(getContext().getString(R.string.choose_this_location))) {
            return locationPass.getLatitude() + ", " + locationPass.getLongitude();
        } else {
            return locationPass.getGeneratedAddress();
        }
    }

    public String getGoogleMapAddressString() {
        return getBinding().editShippingMapView.valueLocation.getText().toString();
    }

    public String getAddressData() {
        return getBinding().addressTextField.getText().toString();
    }
}
