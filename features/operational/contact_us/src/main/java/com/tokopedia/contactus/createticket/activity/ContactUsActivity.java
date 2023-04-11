package com.tokopedia.contactus.createticket.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.createticket.fragment.ContactUsFaqFragment;
import com.tokopedia.contactus.createticket.fragment.ContactUsFaqFragment.ContactUsFaqListener;
import com.tokopedia.contactus.createticket.fragment.CreateTicketFormFragment;
import com.tokopedia.contactus.createticket.utilities.LoggingOnNewRelic;
import com.tokopedia.core.analytics.AppScreen;


/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsActivity extends BaseSimpleActivity implements
        ContactUsFaqListener,
        CreateTicketFormFragment.FinishContactUsListener,
        ContactUsConstant {

    public static final String PARAM_SOLUTION_ID = "PARAM_SOLUTION_ID";
    public static final String PARAM_ORDER_ID = "PARAM_ORDER_ID";
    public static final String PARAM_INVOICE_ID = "PARAM_INVOICE_ID";
    public static final String PARAM_TAG = "PARAM_TAG";
    public static final String EXTRAS_PARAM_TOOLBAR_TITLE = "EXTRAS_PARAM_TOOLBAR_TITLE";
    private static final String CURRENT_FRAGMENT_BACKSTACK = "CURRENT_FRAGMENT_BACKSTACK";
    private static final String PARAM_BUNDLE = "PARAM_BUNDLE";
    public static final String PARAM_URL = "PARAM_URL";
    String url;
    Bundle bundleCreateTicket;
    private BackButtonListener listener;
    private final LoggingOnNewRelic newRelicLogging= new LoggingOnNewRelic();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        String dataAppLink = getIntent().getData().toString();
        newRelicLogging.sendToNewRelicLog(dataAppLink);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getString(CURRENT_FRAGMENT_BACKSTACK, "").equals(CreateTicketFormFragment.class.getSimpleName())) {

            Bundle bundle = new Bundle();
            if (savedInstanceState.getBundle(PARAM_BUNDLE) != null) {
                bundleCreateTicket = savedInstanceState.getBundle(PARAM_BUNDLE);
                bundle.putAll(savedInstanceState.getBundle(PARAM_BUNDLE));
            }

            url = savedInstanceState.getString(EXTRAS_PARAM_URL, "");
            if (!url.equals(""))
                bundle.putString(PARAM_SOLUTION_ID, Uri.parse(url).getQueryParameter("solution_id"));

            CreateTicketFormFragment fragment = CreateTicketFormFragment.createInstance(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            while (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                this.getSupportFragmentManager().popBackStackImmediate();
            }
            transaction.add(R.id.main_view, fragment, CreateTicketFormFragment.class.getSimpleName());
            transaction.addToBackStack(CreateTicketFormFragment.class.getSimpleName());
            transaction.commit();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONTACT_US;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contact_us;
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        ContactUsFaqFragment fragment;
        if (getSupportFragmentManager().findFragmentByTag(ContactUsFaqFragment.class.getSimpleName()) == null) {
            fragment = ContactUsFaqFragment.createInstance(bundle);
        } else {
            fragment = (ContactUsFaqFragment) getSupportFragmentManager().findFragmentByTag(ContactUsFaqFragment.class.getSimpleName());
        }

        if (fragment != null) {
            listener = fragment.getBackButtonListener();
            fragmentTransaction.replace(R.id.main_view, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }

        setTitle();
    }

    private void setTitle() {
        if (getIntent().getExtras() != null && getIntent().getStringExtra(EXTRAS_PARAM_TOOLBAR_TITLE) != null) {
            toolbar.setTitle(getIntent().getStringExtra(EXTRAS_PARAM_TOOLBAR_TITLE));
        } else if (getIntent().getExtras() == null || (
                getIntent().getExtras() != null
                        && getIntent().getExtras()
                        .getString(PARAM_URL, "").equals(""))) {
            toolbar.setTitle(R.string.title_help);
        } else {
            toolbar.setTitle(R.string.title_activity_contact_us);
        }
    }

    @Override
    public void onGoToCreateTicket(Bundle bundle) {
        bundleCreateTicket = bundle;
        if (getSupportFragmentManager().findFragmentByTag(CreateTicketFormFragment.class.getSimpleName()) == null) {
            CreateTicketFormFragment fragment = CreateTicketFormFragment.createInstance(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.contactus_slide_in_left, 0, 0, R.animator.contactus_slide_out_right);
            transaction.add(R.id.main_view, fragment, CreateTicketFormFragment.class.getSimpleName());
            if (!getIntent().getBooleanExtra(ContactUsConstant.EXTRAS_IS_CHAT_BOT, false)) {
                transaction.addToBackStack(CreateTicketFormFragment.class.getSimpleName());
            }
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else if (listener != null && listener.canGoBack()) {
            listener.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public void onFinishCreateTicket() {
        Toast.makeText(this, getString(R.string.title_contact_finish), Toast.LENGTH_LONG).show();
        Intent intent = RouteManager.getIntent(this, ApplinkConst.HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_view);
        if (fragment!=null){
            outState.putString(CURRENT_FRAGMENT_BACKSTACK, fragment.getTag());
        }
        outState.putString(EXTRAS_PARAM_URL, url);
        outState.putBundle(PARAM_BUNDLE, bundleCreateTicket);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    public interface BackButtonListener {
        void onBackPressed();

        boolean canGoBack();
    }
}