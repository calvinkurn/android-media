package com.tokopedia.useridentification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.useridentification.view.fragment.UserIdentificationInfoFragment;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoActivity extends BaseSimpleActivity {

    boolean isSourceSeller;

    public interface Listener {
        void onTrackBackPressed();
    }

    @DeepLink(ApplinkConst.KYC)
    public static Intent getDeeplinkIntent(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();

        String source = extras.getString(KYCConstant.PARAM_KYC_SRC);
        Intent intent = new Intent(context, UserIdentificationInfoActivity.class);
        intent.setData(uri);
        boolean isSourceSeller = TextUtils.equals(source, KYCConstant.VALUE_KYC_SRC_SELLER);
        intent.putExtra(KYCConstant.EXTRA_IS_SOURCE_SELLER, isSourceSeller);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getFragment() != null &&
                getFragment() instanceof Listener) {
            ((Listener) getFragment()).onTrackBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!= null && getIntent().getExtras()!= null) {
            isSourceSeller = getIntent().getExtras().getBoolean(KYCConstant.EXTRA_IS_SOURCE_SELLER);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return UserIdentificationInfoFragment.createInstance(isSourceSeller);
    }
}
