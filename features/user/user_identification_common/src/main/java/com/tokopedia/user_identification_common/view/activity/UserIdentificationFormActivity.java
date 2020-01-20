package com.tokopedia.user_identification_common.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.model.StepperModel;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.user_identification_common.view.fragment.UserIdentificationFormFaceFragment;
import com.tokopedia.user_identification_common.view.fragment.UserIdentificationFormFinalFragment;
import com.tokopedia.user_identification_common.view.fragment.UserIdentificationFormKtpFragment;
import com.tokopedia.user_identification_common.view.viewmodel.UserIdentificationStepperModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormActivity extends BaseStepperActivity {

    private List<Fragment> fragmentList;
    private SnackbarRetry snackbar;
    private int projectId = -1;

    public interface Listener {
        void trackOnBackPressed();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UserIdentificationFormActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(STEPPER_MODEL_EXTRA);
        } else {
            stepperModel = createNewStepperModel();
        }
        try {
            projectId = Integer.parseInt(getIntent().getData().getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID));
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEPPER_MODEL_EXTRA, stepperModel);
    }

    private StepperModel createNewStepperModel() {
        return new UserIdentificationStepperModel();
    }

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
            fragmentList.add(UserIdentificationFormKtpFragment.createInstance());
            fragmentList.add(UserIdentificationFormFaceFragment.createInstance());
            fragmentList.add(UserIdentificationFormFinalFragment.createInstance(projectId));
            return fragmentList;
        } else {
            return fragmentList;
        }
    }

    public void showError(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        snackbar = NetworkErrorHelper.createSnackbarWithAction(this, error, retryClickedListener);
        snackbar.showRetrySnackbar();
    }

    public void showErrorWithoutAction(String error) {
        NetworkErrorHelper.showCloseSnackbar(this, error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (snackbar != null && snackbar.isShown()) {
            snackbar.hideRetrySnackbar();
        }
    }

    @Override
    protected void onBackEvent() {
        if (getListFragment().size() > 0 &&
                getListFragment().get(currentPosition-1) != null &&
                getListFragment().get(currentPosition-1) instanceof Listener){
            ((Listener) getListFragment().get(currentPosition - 1)).trackOnBackPressed();
        }
        super.onBackEvent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (snackbar != null && snackbar.isShown()) {
                    snackbar.hideRetrySnackbar();
                }
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
