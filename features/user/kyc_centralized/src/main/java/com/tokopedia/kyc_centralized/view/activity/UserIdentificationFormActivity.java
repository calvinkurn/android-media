package com.tokopedia.kyc_centralized.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.BulletSpan;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.model.StepperModel;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFaceFragment;
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFinalFragment;
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormKtpFragment;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics;
import com.tokopedia.user_identification_common.view.fragment.NotFoundFragment;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormActivity extends BaseStepperActivity {

    private List<Fragment> fragmentList;
    private SnackbarRetry snackbar;
    private int projectId = -1;
    protected UserIdentificationCommonAnalytics analytics;
    public static boolean isSupportedLiveness = true;

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
        try {
            projectId = Integer.parseInt(getIntent().getData().getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID));
            getIntent().putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId);
        } catch (NumberFormatException | NullPointerException e) {
            projectId = KYCConstant.STATUS_DEFAULT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        analytics = UserIdentificationCommonAnalytics.createInstance(projectId);

        if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(STEPPER_MODEL_EXTRA);
        } else {
            stepperModel = createNewStepperModel();
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
        if(projectId == KYCConstant.STATUS_DEFAULT){
            ArrayList<Fragment> notFoundList = new ArrayList<>();
            notFoundList.add(NotFoundFragment.Companion.createInstance());
            return notFoundList;
        }else {
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
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        if (getListFragment().size() >= currentPosition) {
            Fragment fragment = getListFragment().get(currentPosition - 1);
            Bundle fragmentArguments = fragment.getArguments();
            Bundle bundle;
            if(fragmentArguments == null){
                bundle = new Bundle();
            }else {
                bundle = fragmentArguments;
            }
            bundle.putParcelable(STEPPER_MODEL_EXTRA, stepperModel);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(getParentView(), fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    public void showError(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        snackbar = NetworkErrorHelper.createSnackbarWithAction(this, error, retryClickedListener);
        snackbar.showRetrySnackbar();
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
        if(getListFragment().size()  == currentPosition) {
            int fragmentId = getListFragment().get(currentPosition-1).getId();
            UserIdentificationFormFinalFragment fragment = (UserIdentificationFormFinalFragment) getSupportFragmentManager().findFragmentById(fragmentId);
            if(fragment != null) {
                fragment.clickBackAction();
            }
            showDocumentAlertDialog();
        }else{
            backToPreviousFragment();
        }
    }

    public void showDocumentAlertDialog(){
        DialogUnify dialog = new DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE);
        dialog.setTitle(getString(R.string.kyc_dialog_title));
        dialog.setDescription(getString(R.string.kyc_dialog_description));
        dialog.setPrimaryCTAText(getString(R.string.kyc_dialog_primary_button));
        dialog.setSecondaryCTAText(getString(R.string.kyc_dialog_secondary_button));

        dialog.setPrimaryCTAClickListener(() -> {
            analytics.eventClickDialogStay();
            dialog.dismiss();
            return Unit.INSTANCE;
        });

        dialog.setSecondaryCTAClickListener(() -> {
            analytics.eventClickDialogExit();
            dialog.dismiss();
            setResult(KYCConstant.USER_EXIT);
            finish();
            return Unit.INSTANCE;
        });
        dialog.show();
    }

    public void backToPreviousFragment(){
        if (getListFragment().size() > 0 &&
                getListFragment().get(currentPosition - 1) != null &&
                getListFragment().get(currentPosition - 1) instanceof Listener) {
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

    public void setTextViewWithBullet(String text, Context context, LinearLayout layout){
        Typography tv = new Typography(context);
        SpannableString span = new SpannableString(text);

        int radius = dpToPx(4);
        int gapWidth = dpToPx(12);
        int color = ResourcesCompat.getColor(getResources(), R.color.kyc_centralized_dbdee2, null);
        BulletSpan bulletSpan;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            bulletSpan = new BulletSpan(gapWidth, color, radius);
        } else{
            bulletSpan = new BulletSpan(gapWidth, color);
        }

        span.setSpan(bulletSpan, 0, text.length(), 0);
        tv.setType(Typography.BODY_2);
        tv.setText(span);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        int margin = dpToPx(8);
        setMargins(tv, 0, 0, 0, margin);
        layout.addView(tv);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
