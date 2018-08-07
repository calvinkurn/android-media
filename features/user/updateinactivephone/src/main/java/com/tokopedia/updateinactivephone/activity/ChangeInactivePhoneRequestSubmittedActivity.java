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
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.router.ChangeInactivePhoneRouter;

public class ChangeInactivePhoneRequestSubmittedActivity extends BaseSimpleActivity {

    private Button returnToHome;
    private LinearLayout newRequestDetailLayout;
    private boolean isDuplicateRequest;
    private String email;
    private String phone;
    private TextView emailTV;
    private TextView phoneTV;
    private TextView duplicateRequestTV;

    public static final String IS_DUPLICATE_REQUEST = "is_duplicate_request";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PHONE = "user_phone";

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

        newRequestDetailLayout = findViewById(R.id.first_request_submission_details);
        returnToHome = findViewById(R.id.button_return_to_home);
        emailTV = findViewById(R.id.value_email);
        phoneTV = findViewById(R.id.value_phone);
        duplicateRequestTV = findViewById(R.id.duplicate_request_view);

        if (isDuplicateRequest) {
            newRequestDetailLayout.setVisibility(View.GONE);
            duplicateRequestTV.setVisibility(View.VISIBLE);
        } else {
            newRequestDetailLayout.setVisibility(View.VISIBLE);
            duplicateRequestTV.setVisibility(View.GONE);
            emailTV.setText(email);
            phoneTV.setText(phone);
        }

        returnToHome.setOnClickListener(view -> {
            ((ChangeInactivePhoneRouter) getApplication()).goToHome(this);
            finish();
        });
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
