package com.tokopedia.useridentification.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.useridentification.view.fragment.UserIdentificationInfoFragment;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoActivity extends BaseSimpleActivity {

    boolean isSourceSeller;
    private int projectId = -1;
    public interface Listener {
        void onTrackBackPressed();
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
        try {
            projectId = Integer.parseInt(getIntent().getData().getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID));
        }
        catch (NullPointerException | NumberFormatException ex) {
            projectId = KYCConstant.STATUS_DEFAULT;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return UserIdentificationInfoFragment.createInstance(isSourceSeller, projectId);
    }
}
