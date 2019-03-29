package com.tokopedia.updateinactivephone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants;
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventTracking;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.IS_DUPLICATE_REQUEST;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.USER_EMAIL;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.USER_PHONE;

public class ChangeInactivePhoneRequestSubmittedActivity extends BaseSimpleActivity {

    private boolean isDuplicateRequest;
    private String email;
    private String phone;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    public static Intent createNewIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChangeInactivePhoneRequestSubmittedActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        setupToolbar();
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.change_inactive_phone_request_submitted_layout;
    }

    private void initView() {

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            isDuplicateRequest = bundle.getBoolean(IS_DUPLICATE_REQUEST, false);
            email = bundle.getString(USER_EMAIL);
            phone = bundle.getString(USER_PHONE);
        }

        LinearLayout newRequestDetailLayout = findViewById(R.id.first_request_submission_details);
        Button returnToHome = findViewById(R.id.button_return_to_home);
        TextView emailTV = findViewById(R.id.value_email);
        TextView phoneTV = findViewById(R.id.value_phone);
        TextView duplicateRequestTV = findViewById(R.id.duplicate_request_view);

        if (isDuplicateRequest) {
            ScreenTracking.screen(this, getWaitingConfirmationScreenName());
            UpdateInactivePhoneEventTracking.eventViewWaitingForConfirmationPage(this);
            newRequestDetailLayout.setVisibility(View.GONE);
            duplicateRequestTV.setVisibility(View.VISIBLE);
        } else {
            ScreenTracking.screen(this, getSuccessConfirmationScreenName());
            UpdateInactivePhoneEventTracking.eventViewSubmitSuccessPage(this);
            newRequestDetailLayout.setVisibility(View.VISIBLE);
            duplicateRequestTV.setVisibility(View.GONE);
            emailTV.setText(email);
            phoneTV.setText(phone);
        }

        returnToHome.setOnClickListener(view -> {
            RouteManager.route(this, ApplinkConst.HOME);
            finish();
        });
    }

    private String getSuccessConfirmationScreenName() {
        return UpdateInactivePhoneEventConstants.Screen.SUBMIT_SUCCESS_REQUEST_PAGE;
    }

    private String getWaitingConfirmationScreenName() {
        return UpdateInactivePhoneEventConstants.Screen.WAITING_CONFIRMATION_PAGE;
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation_cancel);
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
    }
}
