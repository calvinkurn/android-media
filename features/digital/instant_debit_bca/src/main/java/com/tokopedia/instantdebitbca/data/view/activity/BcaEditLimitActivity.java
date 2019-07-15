package com.tokopedia.instantdebitbca.data.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.instantdebitbca.data.view.fragment.EditLimitFragment;
import com.tokopedia.instantdebitbca.data.view.utils.ApplinkConstant;

public class BcaEditLimitActivity extends InstantDebitBcaActivity {

    public static final String XCOID = "xcoid";

    @DeepLink({ApplinkConstant.INSTANT_DEBIT_BCA_EDITLIMIT_APPLINK})
    public static Intent intentForTaskStackBuilderMethods(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        String xcoid = extras.containsKey(XCOID) ? extras.getString(XCOID) : "";
        String callbackUrl = extras.containsKey(CALLBACK_URL) ? extras.getString(CALLBACK_URL) : "";
        return BcaEditLimitActivity.newInstance(context, callbackUrl, xcoid);
    }

    public static Intent newInstance(Context context, String callbackUrl, String xcoid) {
        Intent intent = new Intent(context, BcaEditLimitActivity.class);
        intent.putExtra(XCOID, xcoid);
        intent.putExtra(CALLBACK_URL, callbackUrl);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return EditLimitFragment.newInstance(getApplicationContext(), getIntent().getStringExtra(CALLBACK_URL), getIntent().getStringExtra(XCOID));
    }

}
