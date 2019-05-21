package com.tokopedia.updateinactivephone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.design.view.UpdateInactivePhoneInfoBottomSheet;
import com.tokopedia.updateinactivephone.di.DaggerUpdateInactivePhoneComponent;
import com.tokopedia.updateinactivephone.fragment.SelectImageNewPhoneFragment;
import com.tokopedia.updateinactivephone.fragment.UpdateNewPhoneEmailFragment;
import com.tokopedia.updateinactivephone.presenter.ChangeInactiveFormRequestPresenter;
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest;

import javax.inject.Inject;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.IS_DUPLICATE_REQUEST;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.USER_EMAIL;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.USER_PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.OLD_PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.USER_ID;

/**
 * For navigating to this class
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal#CHANGE_INACTIVE_PHONE_FORM}
 * Please pass USER_ID and OLD_PHONE
 */
public class ChangeInactiveFormRequestActivity extends BaseSimpleActivity implements
        HasComponent<AppComponent>, ChangeInactiveFormRequest.View,
        SelectImageNewPhoneFragment.SelectImageInterface, UpdateNewPhoneEmailFragment.UpdateNewPhoneEmailInteractor {

    @Inject
    ChangeInactiveFormRequestPresenter presenter;

    private String userId;
    private TkpdProgressDialog tkpdProgressDialog;
    private UpdateNewPhoneEmailFragment updateNewPhoneEmailFragment;
    private String newEmail;
    private String newPhoneNumber;
    private UpdateInactivePhoneInfoBottomSheet updateInactivePhoneInfoBottomSheet;


    public static Intent createIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChangeInactiveFormRequestActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createIntentWithUserId(Context context, String userId, String oldPhoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        bundle.putString(OLD_PHONE, oldPhoneNumber);
        return createIntent(context, bundle);
    }

    @Override
    protected Fragment getNewFragment() {
        return SelectImageNewPhoneFragment.getInstance();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        setupToolbar();
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.change_inactive_form_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        presenter.attachView(this);
        updateInactivePhoneInfoBottomSheet = new UpdateInactivePhoneInfoBottomSheet(this);
    }

    protected void initInjector() {
        DaggerUpdateInactivePhoneComponent daggerUpdateInactivePhoneComponent = (DaggerUpdateInactivePhoneComponent)
                DaggerUpdateInactivePhoneComponent.builder()
                        .appComponent(getComponent())
                        .build();

        daggerUpdateInactivePhoneComponent.inject(this);
    }

    private void initView() {

        if (getIntent() != null && getIntent().getExtras() != null) {
            userId = getIntent().getExtras().getString(USER_ID);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (SelectImageNewPhoneFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = SelectImageNewPhoneFragment.getInstance();
        }
        fragmentTransaction.replace(R.id.parent_view, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(null).commit();

    }


    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_icon_back_black);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ImageView infoIcon = toolbar.findViewById(R.id.info_icon);
        infoIcon.setOnClickListener(view -> {
            updateInactivePhoneInfoBottomSheet.setTitle("Info");
            updateInactivePhoneInfoBottomSheet.show();
        });

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onContinueButtonClick() {

        Bundle bundle = new Bundle();
        if (getIntent() != null &&
                getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        updateNewPhoneEmailFragment = (UpdateNewPhoneEmailFragment) getSupportFragmentManager().findFragmentByTag
                (UpdateNewPhoneEmailFragment.class.getSimpleName());

        if (updateNewPhoneEmailFragment == null) {
            updateNewPhoneEmailFragment = UpdateNewPhoneEmailFragment.getInstance(bundle);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.parent_view, updateNewPhoneEmailFragment).addToBackStack(null).commit();
    }

    @Override
    public boolean isValidPhotoIdPath() {
        return presenter.isValidPhotoIdPath();
    }

    @Override
    public void setAccountPhotoImagePath(String imagePath) {
        presenter.setAccountPhotoImagePath(imagePath);
    }

    @Override
    public void setPhotoIdImagePath(String imagePath) {
        presenter.setPhotoIdImagePath(imagePath);
    }

    @Override
    public void dismissLoading() {
        if (tkpdProgressDialog != null)
            tkpdProgressDialog.dismiss();
    }

    @Override
    public void showLoading() {
        if (tkpdProgressDialog == null)
            tkpdProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog
                    .NORMAL_PROGRESS);

        tkpdProgressDialog.showDialog();
    }

    @Override
    public void showErrorValidateData(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void onUserDataValidated() {
        presenter.uploadPhotoIdImage(newEmail, newPhoneNumber, userId);
    }

    @Override
    public void onPhoneTooShort() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.phone_number_invalid_min_8);
        }
    }

    @Override
    public void onPhoneTooLong() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.phone_number_invalid_max_15);
        }
    }

    @Override
    public void onPhoneBlackListed() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.phone_blacklisted);
        }
    }

    @Override
    public void onWrongUserIDInput() {
        NetworkErrorHelper.showSnackbar(this, getString(R.string.wrong_user_id));
    }

    @Override
    public void onPhoneDuplicateRequest() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_DUPLICATE_REQUEST, true);
        Intent intent = ChangeInactivePhoneRequestSubmittedActivity.createNewIntent(this, bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPhoneServerError() {
        NetworkErrorHelper.showSnackbar(this);
    }

    @Override
    public void onSameMsisdn() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.old_new_phone_same);
        }
    }

    @Override
    public void onAlreadyRegisteredMsisdn() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.already_registered_phone);
        }
    }

    @Override
    public void onEmptyMsisdn() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.phone_empty);
        }
    }

    @Override
    public void onInvalidPhone() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.phone_invalid);
        }
    }

    @Override
    public void onMaxReachedPhone() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(R.string.max_limit_reached_for_phone);
        }
    }

    @Override
    public void showErrorPhoneNumber(int resId) {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorPhone(resId);
        }
    }

    @Override
    public void showErrorEmail(int error_invalid_email) {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorEmail(error_invalid_email);
        }
    }

    @Override
    public void onEmailError() {
        if (updateNewPhoneEmailFragment != null) {
            updateNewPhoneEmailFragment.showErrorEmail(R.string.error_invalid_email);
        }
    }

    @Override
    public void onUserNotRegistered() {
        NetworkErrorHelper.showSnackbar(this, getString(R.string.user_not_registered));
    }

    @Override
    public void onInvalidFileUploaded() {
        NetworkErrorHelper.showSnackbar(this);
    }

    @Override
    public void onUpdateDataRequestFailed() {
        NetworkErrorHelper.showSnackbar(this);
    }

    @Override
    public void onUpdateDataRequestSuccess() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_DUPLICATE_REQUEST, false);
        bundle.putString(USER_EMAIL, newEmail);
        bundle.putString(USER_PHONE, newPhoneNumber);
        Intent intent = ChangeInactivePhoneRequestSubmittedActivity.createNewIntent(this, bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onForbidden() {

    }

    @Override
    public void onSubmissionButtonClicked(String email, String phone, String userId) {
        this.newEmail = email;
        this.newPhoneNumber = phone;
        presenter.validateUserData(email, phone, userId);
    }
}
