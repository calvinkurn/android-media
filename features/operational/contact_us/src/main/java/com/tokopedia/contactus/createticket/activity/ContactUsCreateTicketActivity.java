package com.tokopedia.contactus.createticket.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.createticket.fragment.CreateTicketFormFragment;

public class ContactUsCreateTicketActivity extends BaseSimpleActivity implements CreateTicketFormFragment.FinishContactUsListener {

    public static final String PARAM_TITLE = "PARAM_TITLE";
    public static final String PARAM_DESCRIPTION = "PARAM_DESCRIPTION";
    public static final String PARAM_DESCRIPTION_TITLE = "PARAM_DESCRIPTION_TITLE";
    public static final String PARAM_ATTACHMENT_TITLE = "PARAM_ATTACHMENT_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.contactus_activity_create_ticket;
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        CreateTicketFormFragment fragment;
        if (getFragmentManager().findFragmentByTag(CreateTicketFormFragment.class.getSimpleName()) == null) {
            fragment = CreateTicketFormFragment.createInstance(bundle);
        } else {
            fragment = (CreateTicketFormFragment) getSupportFragmentManager().findFragmentByTag(CreateTicketFormFragment.class.getSimpleName());
        }

        fragmentTransaction.replace(R.id.main_view, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

        setTitle();
    }

    private void setTitle() {
        if (getIntent().getExtras() != null &&
                getIntent().getExtras().getString(PARAM_TITLE, "").length() > 0) {
            toolbar.setTitle(getIntent().getExtras().getString(PARAM_TITLE, ""));
        } else {
            toolbar.setTitle(R.string.title_activity_contact_us);
        }
    }

    @Override
    public void onFinishCreateTicket() {
        Toast.makeText(this, R.string.title_contact_finish, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
