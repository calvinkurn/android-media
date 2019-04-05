package com.tokopedia.logisticaddaddress.features.addaddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import static com.tokopedia.logisticaddaddress.AddressConstants.EDIT_PARAM;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_INSTANCE_TYPE;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_PLATFORM_PAGE;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_MULTIPLE_CHECKOUT;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_EDIT_ADDRESS_FROM_MULTIPLE_CHECKOUT;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT;
import static com.tokopedia.logisticaddaddress.AddressConstants.IS_DISTRICT_RECOMMENDATION;
import static com.tokopedia.logisticaddaddress.AddressConstants.IS_EDIT;
import static com.tokopedia.logisticaddaddress.AddressConstants.KERO_TOKEN;
import static com.tokopedia.logisticaddaddress.AddressConstants.PLATFORM_MARKETPLACE_CART;

/**
 * Created by nisie on 9/6/16.
 * Refactored by fajar.nuha
 */
public class AddAddressActivity extends BaseSimpleActivity {

    /**
     * Always pass a bundle because the activity always get intent extra
     */
    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = null;
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            fragment = AddAddressFragment.newInstance(bundle);
        }
        return fragment;
    }

    public static Intent createInstanceAddAddressFromCheckoutSingleAddressFormWhenDefaultAddressIsEmpty(@NonNull Activity activity,
                                                                                                        @Nullable Token token) {
        return createInstance(activity, null, token, false,
                true, INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS);
    }

    public static Intent createInstanceAddAddressFromCheckoutSingleAddressForm(@NonNull Activity activity,
                                                                               @Nullable Token token) {
        return createInstance(activity, null, token, false,
                false, INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT);
    }

    public static Intent createInstanceAddAddressFromCheckoutMultipleAddressForm(@NonNull Activity activity,
                                                                                 @Nullable Token token) {
        return createInstance(
                activity, null, token, false,
                false, INSTANCE_TYPE_ADD_ADDRESS_FROM_MULTIPLE_CHECKOUT);
    }

    public static Intent createInstanceEditAddressFromCheckoutMultipleAddressForm(@NonNull Activity activity,
                                                                                  @Nullable AddressModel addressModel,
                                                                                  @Nullable Token token) {
        return createInstance(
                activity, addressModel, token, true,
                false, INSTANCE_TYPE_EDIT_ADDRESS_FROM_MULTIPLE_CHECKOUT);
    }

    public static Intent createInstanceEditAddressFromCheckoutSingleAddressForm(@NonNull Activity activity,
                                                                                @Nullable AddressModel addressModel,
                                                                                @Nullable Token token) {
        return createInstance(activity, addressModel, token, true,
                false, INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT);
    }


    public static Intent createInstanceEditAddressFromManageAddress(@NonNull Activity activity,
                                                                    @Nullable AddressModel addressModel,
                                                                    @Nullable Token token) {
        return createInstance(
                activity, addressModel, token, true,
                false, INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS);
    }

    public static Intent createInstanceAddAddressFromManageAddress(@NonNull Activity activity,
                                                                   @Nullable Token token) {
        return createInstance(
                activity, null, token, false,
                false, INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS);
    }

    public static Intent createInstanceAddAddressFromManageAddressWhenDefaultAddressIsEmpty(@NonNull Activity activity,
                                                                                            @Nullable Token token) {
        return createInstance(
                activity, null, token, false,
                false, INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS);
    }

    private static Intent createInstance(
            @NonNull Activity activity,
            @Nullable AddressModel data,
            @Nullable Token token,
            boolean isEdit,
            boolean isEmptyAddressFirst,
            int typeInstance
    ) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_DISTRICT_RECOMMENDATION, true);
        bundle.putString(EXTRA_PLATFORM_PAGE, PLATFORM_MARKETPLACE_CART);
        if (data != null)
            bundle.putParcelable(EDIT_PARAM, data.convertToDestination());
        bundle.putBoolean(EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST, isEmptyAddressFirst);
        bundle.putBoolean(IS_EDIT, isEdit);
        bundle.putParcelable(KERO_TOKEN, token);
        bundle.putInt(EXTRA_INSTANCE_TYPE, typeInstance);
        intent.putExtras(bundle);
        return intent;
    }


}
