package com.tokopedia.instantdebitbca.data.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.instantdebitbca.data.view.utils.ApplinkConstant;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class InstantDebitBcaActivity extends BaseSimpleActivity {


    @SuppressWarnings("unused")
    @DeepLink({ApplinkConstant.INSTANT_DEBIT_BCA_APPLINK})
    public static Intent intentForTaskStackBuilderMethods(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, InstantDebitBcaActivity.class);
        return intent.setData(uri.build()).putExtras(extras);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, InstantDebitBcaActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return InstantDebitBcaFragment.newInstance(getApplicationContext());
    }
}
