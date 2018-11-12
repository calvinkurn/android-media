package com.tokopedia.useridentification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.useridentification.view.fragment.UserIdentificationInfoFragment;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.KYC)
    public static Intent getDeeplinkIntent(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();

        Intent intent = new Intent(context, UserIdentificationInfoActivity.class);
        intent.setData(uri);
        intent.putExtras(extras);

        return intent;
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UserIdentificationInfoActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return UserIdentificationInfoFragment.createInstance();
    }
}
