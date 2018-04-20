package com.tokopedia.checkout.view.view.multipleaddressform;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;

/**
 * Created by kris on 1/25/18. Tokopedia
 */

public class AddShipmentAddressActivity extends BaseCheckoutActivity {

    private static final String PRODUCT_DATA_EXTRAS = "PRODUCT_DATA_EXTRAS";
    private static final String ADDRESS_DATA_EXTRAS = "ADDRESS_DATA_EXTRAS";
    private static final String MODE_EXTRA = "MODE_EXTRAS";
    public static final String ADDRESS_DATA_RESULT = "ADDRESxS_DATA_RESULT";
    public static final int ADD_MODE = 1;
    public static final int EDIT_MODE = 2;

    private int formMode;
    MultipleAddressAdapterData multipleAddressAdapterData;
    MultipleAddressItemData multipleAddressItemData;

    public static Intent createIntent(Context context,
                                      MultipleAddressAdapterData data,
                                      MultipleAddressItemData addressData,
                                      int mode) {
        Intent intent = new Intent(context, AddShipmentAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_DATA_EXTRAS, data);
        bundle.putParcelable(ADDRESS_DATA_EXTRAS, addressData);
        bundle.putInt(MODE_EXTRA, mode);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.formMode = extras.getInt(MODE_EXTRA);
        this.multipleAddressAdapterData = extras.getParcelable(PRODUCT_DATA_EXTRAS);
        this.multipleAddressItemData = extras.getParcelable(ADDRESS_DATA_EXTRAS);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    protected void initInjector() {
    }


    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        return AddShipmentAddressFragment.newInstance(
                multipleAddressAdapterData, multipleAddressItemData, formMode
        );
    }
}
