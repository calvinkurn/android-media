package com.tokopedia.logisticaddaddress.features.addaddress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticCommon.data.entity.address.AddressModel;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.address.Token;

import java.util.List;

import static com.tokopedia.logisticaddaddress.common.AddressConstants.EDIT_PARAM;
import static com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_INSTANCE_TYPE;
import static com.tokopedia.logisticaddaddress.common.AddressConstants.INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS;
import static com.tokopedia.logisticaddaddress.common.AddressConstants.INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS;
import static com.tokopedia.logisticaddaddress.common.AddressConstants.KERO_TOKEN;

/**
 * Created by nisie on 9/6/16.
 * Refactored by fajar.nuha
 */
public class AddAddressActivity extends BaseSimpleActivity {

    private final String PARAM_ADDRESS_MODEL = "PARAM_ADDRESS_MODEL";
    private boolean isEdit;

    /**
     * Always pass a bundle because the activity always get intent extra
     */
    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            Bundle oldBundle = getIntent().getExtras();
            AddressModel model = oldBundle.getParcelable(EDIT_PARAM);
            if (model != null) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.title_update_address);
                }
                bundle.putParcelable(EDIT_PARAM, model.convertToDestination());
            }
            bundle.putInt(EXTRA_INSTANCE_TYPE, oldBundle.getInt(EXTRA_INSTANCE_TYPE));
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


    public static Intent createInstanceEditAddressFromManageAddress(@NonNull Activity activity,
                                                                    @Nullable RecipientAddressModel addressModel,
                                                                    @Nullable Token token) {
        return createInstance(
                activity, addressModel, token,
                INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS);
    }

    public static Intent createInstanceAddAddressFromManageAddressWhenDefaultAddressIsEmpty(@NonNull Activity activity,
                                                                                            @Nullable Token token) {
        return createInstance(
                activity, null, token,
                INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS);
    }

    private static Intent createInstance(
            @NonNull Activity activity,
            @Nullable RecipientAddressModel data,
            @Nullable Token token,
            int typeInstance
    ) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EDIT_PARAM, data);
        bundle.putParcelable(KERO_TOKEN, token);
        bundle.putInt(EXTRA_INSTANCE_TYPE, typeInstance);
        intent.putExtras(bundle);
        return intent;
    }


}
