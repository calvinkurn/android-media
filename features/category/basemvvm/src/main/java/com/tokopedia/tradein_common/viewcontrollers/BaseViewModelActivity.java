package com.tokopedia.tradein_common.viewcontrollers;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.tradein_common.R;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tradein_common.viewmodel.BaseLifeCycleObserver;
import com.tokopedia.tradein_common.viewmodel.BaseViewModel;
import com.tokopedia.unifycomponents.Toaster;

public abstract class BaseViewModelActivity<T extends BaseViewModel> extends BaseSimpleActivity {

    protected boolean isTncShowing = false;
    protected T bVM;

    protected void initView() {

    }

    abstract protected Class<T> getViewModelType();

    abstract protected void setViewModel(BaseViewModel viewModel);

    abstract protected int getMenuRes();

    abstract protected Fragment getTncFragmentInstance(int TncResId);

    abstract protected int getBottomSheetLayoutRes();

    abstract protected boolean doNeedReattach();

    abstract protected ViewModelProvider.AndroidViewModelFactory getVMFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkClient.init(this);
        GraphqlClient.init(this);
        setViewModel();
        initView();
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back_black);
        bVM.getProgBarVisibility().observe(this, (visibility) -> {
            if (visibility != null) {
                if (visibility)
                    showProgressBar();
                else
                    hideProgressBar();
            }
        });

        bVM.getWarningMessage().observe(this, (message) -> {
            hideProgressBar();
            if (!TextUtils.isEmpty(message)) {
                try {
                    Toaster.INSTANCE.showError(this.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bVM.getErrorMessage().observe(this, (message) -> {
            hideProgressBar();
            if (!TextUtils.isEmpty(message)) {
                try {
                    Toaster.INSTANCE.showErrorWithAction(this.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG, getButtonStringOnError(), (v) -> retryOnError());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setViewModel() {
        bVM = ViewModelProviders.of(this, getVMFactory()).get(getViewModelType());
        setViewModel(bVM);
        getLifecycle().addObserver(getLifeCycleObserver(bVM));
    }

    protected void retryOnError() {

    }

    protected String getButtonStringOnError() {
        return getString(R.string.close);
    }

    protected BaseLifeCycleObserver getLifeCycleObserver(BaseViewModel viewModel) {
        return new BaseLifeCycleObserver(viewModel);
    }

    public void showMessageWithAction(String message, String actionText, View.OnClickListener listener) {
        Toaster.INSTANCE.showErrorWithAction(this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_INDEFINITE, actionText, listener);

    }

    public void showMessage(String message) {
        Toaster.INSTANCE.showError(this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG);
    }

    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    protected abstract void showProgressBar();

    protected abstract void hideProgressBar();

    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.white));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() != -1) {
            getMenuInflater().inflate(getMenuRes(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public View getRootView() {
        return findViewById(R.id.root_view);
    }

    public int getRequestCode() {
        return 0;
    }

    public void showBottomFragment() {

    }

    public void hideBottomFragment() {

    }

    public void updateDataSet() {

    }

    protected void setSnackBarErrorMessage(String message, boolean clickable) {

    }

    public void clearSearch() {

    }

    public boolean isSearchMode() {
        return false;
    }

    public void toggleSearch(int visibility) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isTncShowing) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back_black);
                getSupportActionBar().setTitle(getTitle());
            }
            isTncShowing = false;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (isTncShowing) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back_black);
                getSupportActionBar().setTitle(getTitle());
            }
            isTncShowing = false;
        }
        super.onBackPressed();
    }

    protected void showTnC(int tncResId) {
        isTncShowing = true;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
            getSupportActionBar().setTitle("Syarat dan Ketentuan");
        }

        Fragment fragment = getTncFragmentInstance(tncResId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack("TNC");
        transaction.replace(getRootViewId(), fragment);
        transaction.commit();
    }

    public int getRootViewId() {
        return R.id.root_view;
    }

    protected void showDialogFragment(String titleText, String bodyText, String positiveButton, String negativeButton) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AccessRequestFragment accessDialog = AccessRequestFragment.newInstance();
        accessDialog.setBodyText(bodyText);
        accessDialog.setTitle(titleText);
        accessDialog.setPositiveButton(positiveButton);
        accessDialog.setNegativeButton(negativeButton);
        accessDialog.show(fragmentManager, AccessRequestFragment.TAG);
    }

    protected void showAgeVerificationDialogFragment(String titleText, String bodyText, String positiveButton, String negativeButton) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AccessRequestFragment accessDialog = AccessRequestFragment.newInstance();
        accessDialog.setLayoutResId(R.layout.age_restriction_verifcation_dialog);
        accessDialog.setBodyText(bodyText);
        accessDialog.setTitle(titleText);
        accessDialog.setPositiveButton(positiveButton);
        accessDialog.setNegativeButton(negativeButton);
        accessDialog.show(fragmentManager, AccessRequestFragment.TAG);
    }

    protected void sendGeneralEvent(String event, String category, String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(event,
                category,
                action,
                label);
    }

    protected String getMeGQlString(int rawResId) {
        return GraphqlHelper.loadRawString(getResources(), rawResId);
    }
}
