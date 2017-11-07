package com.tokopedia.core.referral.presenter;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.ShareActivity;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralPresenter implements IReferralPresenter {

    private Activity activity;
    public ReferralPresenter(BasePresenterFragment presenterFragment) {
        this.activity = presenterFragment.getActivity();
    }

    @Override
    public void shareApp() {

        Intent shareIntent = new Intent(activity, ShareActivity.class);
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.APP_SHARE_TYPE)
                .setName(activity.getString(R.string.app_share_title))
                .setDescription(activity.getString(R.string.app_share_contents)+" ")
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .build();
        shareIntent.putExtra(ShareData.TAG, shareData);
        activity.startActivity(shareIntent);
    }
}
