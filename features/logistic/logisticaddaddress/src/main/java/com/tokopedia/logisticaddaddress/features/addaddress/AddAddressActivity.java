package com.tokopedia.logisticaddaddress.features.addaddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import static com.tokopedia.logisticaddaddress.AddressConstants.SCREEN_ADD_ADDRESS_FORM;
import static com.tokopedia.logisticaddaddress.AddressConstants.EDIT_PARAM;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_PLATFORM_PAGE;
import static com.tokopedia.logisticaddaddress.AddressConstants.IS_DISTRICT_RECOMMENDATION;
import static com.tokopedia.logisticaddaddress.AddressConstants.IS_EDIT;
import static com.tokopedia.logisticaddaddress.AddressConstants.KERO_TOKEN;
import static com.tokopedia.logisticaddaddress.AddressConstants.PLATFORM_MARKETPLACE_CART;

/**
 * Created by nisie on 9/6/16.
 * Refactored by fajar.nuha
 */
public class AddAddressActivity extends BaseSimpleActivity {

    @Override
    public String getScreenName() {
        return SCREEN_ADD_ADDRESS_FORM;
    }

    /**
     * Always pass a bundle because the activity always get intent extra
     */
    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = null;
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            fragment = AddAddressFragment.createInstance(bundle);
        }
        return fragment;
    }

    /**
     * @return intent for activity creation
     * Used for add new address
     */
    public static Intent createInstance(Activity activity, Token token) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_DISTRICT_RECOMMENDATION, true);
        bundle.putBoolean(IS_EDIT, false);
        bundle.putParcelable(KERO_TOKEN, token);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * @return intent for activity creation
     * Used for edit address
     */
    public static Intent createInstance(Activity activity, AddressModel data, Token token) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_DISTRICT_RECOMMENDATION, true);
        bundle.putParcelable(EDIT_PARAM, data.convertToDestination());
        bundle.putBoolean(IS_EDIT, true);
        bundle.putParcelable(KERO_TOKEN, token);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * Used from cart checkout for add or edit address
     */
    public static Intent createInstanceFromCartCheckout(
            Activity activity, @Nullable AddressModel data, Token token, boolean isEdit, boolean isEmptyAddressFirst
    ) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_DISTRICT_RECOMMENDATION, true);
        bundle.putString(EXTRA_PLATFORM_PAGE, PLATFORM_MARKETPLACE_CART);
        if (data != null) bundle.putParcelable(EDIT_PARAM, data.convertToDestination());
        bundle.putBoolean(EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST, isEmptyAddressFirst);
        bundle.putBoolean(IS_EDIT, isEdit);
        bundle.putParcelable(KERO_TOKEN, token);
        intent.putExtras(bundle);
        return intent;
    }
}
