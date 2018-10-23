package com.tokopedia.affiliate.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.createpost.view.fragment.CreatePostFragment;
import com.tokopedia.applink.ApplinkConst;

public class CreatePostActivity extends BaseSimpleActivity {

    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_AD_ID = "ad_id";
    public static final String PARAM_POST_ID = "post_id";
    public static final String PARAM_IS_EDIT = "is_edit";

    @DeepLink(ApplinkConst.AFFILIATE_CREATE_POST)
    public static Intent getInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink(ApplinkConst.AFFILIATE_EDIT_POST)
    public static Intent getInstanceEdit(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(PARAM_IS_EDIT, true);
        return intent;
    }

    public static Intent getInstance(Context context, String productId, String adId) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        intent.putExtra(PARAM_PRODUCT_ID, productId);
        intent.putExtra(PARAM_AD_ID, adId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return CreatePostFragment.createInstance(bundle);
    }
}
