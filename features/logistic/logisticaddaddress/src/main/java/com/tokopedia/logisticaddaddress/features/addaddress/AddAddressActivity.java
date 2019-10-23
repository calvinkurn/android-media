package com.tokopedia.logisticaddaddress.features.addaddress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.List;

import static com.tokopedia.logisticaddaddress.AddressConstants.EDIT_PARAM;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_INSTANCE_TYPE;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_PLATFORM_PAGE;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_MULTIPLE_CHECKOUT;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS;
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

    private final String PARAM_ADDRESS_MODEL = "PARAM_ADDRESS_MODEL";

    /**
     * Always pass a bundle because the activity always get intent extra
     */
    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            Bundle oldBundle = getIntent().getExtras();
            AddressModel model = oldBundle.getParcelable(PARAM_ADDRESS_MODEL);
            if (model != null) {
                bundle.putParcelable(EDIT_PARAM, model.convertToDestination());
            }
            bundle.putParcelable(KERO_TOKEN, oldBundle.getParcelable(KERO_TOKEN));
        }

        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> params = UriUtil.destructureUri(ApplinkConstInternalLogistic.ADD_ADDRESS_V1, uri);
            int refId = 0;
            try {
                refId = Integer.valueOf(params.get(0));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            bundle.putInt(EXTRA_INSTANCE_TYPE, refId);
        }
        return AddAddressFragment.newInstance(bundle);
    }

    public static Intent createInstanceAddAddressFromCheckoutSingleAddressFormWhenDefaultAddressIsEmpty(@NonNull Activity activity,
                                                                                                        @Nullable Token token) {
        return createInstance(activity, null, token,
                INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS);
    }

    public static Intent createInstanceAddAddressFromCheckoutSingleAddressForm(@NonNull Activity activity,
                                                                               @Nullable Token token) {
        return createInstance(activity, null, token,
                INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT);
    }

    public static Intent createInstanceAddAddressFromCheckoutMultipleAddressForm(@NonNull Activity activity,
                                                                                 @Nullable Token token) {
        return createInstance(
                activity, null, token,
                INSTANCE_TYPE_ADD_ADDRESS_FROM_MULTIPLE_CHECKOUT);
    }

    public static Intent createInstanceEditAddressFromCheckoutMultipleAddressForm(@NonNull Activity activity,
                                                                                  @Nullable AddressModel addressModel,
                                                                                  @Nullable Token token) {
        return createInstance(
                activity, addressModel, token,
                INSTANCE_TYPE_EDIT_ADDRESS_FROM_MULTIPLE_CHECKOUT);
    }

    public static Intent createInstanceEditAddressFromCheckoutSingleAddressForm(@NonNull Activity activity,
                                                                                @Nullable AddressModel addressModel,
                                                                                @Nullable Token token) {
        return createInstance(activity, addressModel, token,
                INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT);
    }


    public static Intent createInstanceEditAddressFromManageAddress(@NonNull Activity activity,
                                                                    @Nullable AddressModel addressModel,
                                                                    @Nullable Token token) {
        return createInstance(
                activity, addressModel, token,
                INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS);
    }

    public static Intent createInstanceAddAddressFromManageAddress(@NonNull Activity activity,
                                                                   @Nullable Token token) {
        return createInstance(
                activity, null, token,
                INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS);
    }

    public static Intent createInstanceAddAddressFromManageAddressWhenDefaultAddressIsEmpty(@NonNull Activity activity,
                                                                                            @Nullable Token token) {
        return createInstance(
                activity, null, token,
                INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS);
    }

    private static Intent createInstance(
            @NonNull Activity activity,
            @Nullable AddressModel data,
            @Nullable Token token,
            int typeInstance
    ) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        if (data != null)
            bundle.putParcelable(EDIT_PARAM, data.convertToDestination());
        bundle.putParcelable(KERO_TOKEN, token);
        bundle.putInt(EXTRA_INSTANCE_TYPE, typeInstance);
        intent.putExtras(bundle);
        return intent;
    }


}
