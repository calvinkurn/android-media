package com.tokopedia.contactus.home.view;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.contactus.home.view.fragment.ContactUsHomeFragment;

/**
 * Created by sandeepgoyal on 02/04/18.
 */

public class ContactUsHomeActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.CONTACT_US_NATIVE)
    public static Intent getContactUsIntent(Context context, Bundle bundle){
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ContactUsHomeActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }
    @Override
    protected Fragment getNewFragment() {
        return ContactUsHomeFragment.newInstance();
    }
}
