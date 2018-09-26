package com.tokopedia.affiliate.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;

/**
 * @author by yfsx on 26/09/18.
 */
public class CreatePostActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.AFFILIATE_CREATE_POST)
    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
