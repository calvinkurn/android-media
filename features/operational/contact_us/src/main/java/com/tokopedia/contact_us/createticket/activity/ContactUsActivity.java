package com.tokopedia.contact_us.createticket.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.contact_us.R;
import com.tokopedia.contact_us.createticket.ContactUsConstant;
import com.tokopedia.contact_us.createticket.fragment.ContactUsFaqFragment;
import com.tokopedia.contact_us.createticket.fragment.ContactUsFaqFragment.ContactUsFaqListener;
import com.tokopedia.contact_us.createticket.fragment.CreateTicketFormFragment;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsActivity extends BasePresenterActivity implements
        ContactUsFaqListener,
        CreateTicketFormFragment.FinishContactUsListener,
        ContactUsConstant {

    public static final String PARAM_SOLUTION_ID = "PARAM_SOLUTION_ID";
    public static final String PARAM_ORDER_ID = "PARAM_ORDER_ID";
    public static final String PARAM_INVOICE_ID = "PARAM_INVOICE_ID";
    public static final String PARAM_TAG = "PARAM_TAG";
    public static final String PARAM_TOOLBAR_TITLE = "PARAM_TOOLBAR_TITLE";
    private static final String CURRENT_FRAGMENT_BACKSTACK = "CURRENT_FRAGMENT_BACKSTACK";
    private static final String PARAM_BUNDLE = "PARAM_BUNDLE";
    String url;
    Bundle bundleCreateTicket;
    private BackButtonListener listener;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getString(CURRENT_FRAGMENT_BACKSTACK, "").equals(CreateTicketFormFragment.class.getSimpleName())) {

            Bundle bundle = new Bundle();
            if (savedInstanceState.getBundle(PARAM_BUNDLE) != null) {
                bundleCreateTicket = savedInstanceState.getBundle(PARAM_BUNDLE);
                bundle.putAll(savedInstanceState.getBundle(PARAM_BUNDLE));
            }

            url = savedInstanceState.getString(PARAM_URL, "");
            if (!url.equals(""))
                bundle.putString(PARAM_SOLUTION_ID, Uri.parse(url).getQueryParameter("solution_id"));

            CreateTicketFormFragment fragment = CreateTicketFormFragment.createInstance(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            while (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStackImmediate();
            }
            transaction.add(R.id.main_view, fragment, CreateTicketFormFragment.class.getSimpleName());
            transaction.addToBackStack(CreateTicketFormFragment.class.getSimpleName());
            transaction.commit();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONTACT_US;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        ContactUsFaqFragment fragment;
        if (getFragmentManager().findFragmentByTag(ContactUsFaqFragment.class.getSimpleName()) == null) {
            fragment = ContactUsFaqFragment.createInstance(bundle);
        } else {
            fragment = (ContactUsFaqFragment) getFragmentManager().findFragmentByTag(ContactUsFaqFragment.class.getSimpleName());
        }

        listener = fragment.getBackButtonListener();
        fragmentTransaction.replace(R.id.main_view, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

        setTitle();
    }

    private void setTitle() {
        if (getIntent().getExtras() != null && getIntent().getStringExtra(PARAM_TOOLBAR_TITLE) != null) {
            toolbar.setTitle(getIntent().getStringExtra(PARAM_TOOLBAR_TITLE));
        } else if (getIntent().getExtras() == null || (
                getIntent().getExtras() != null
                        && getIntent().getExtras()
                        .getString(InboxRouter.PARAM_URL, "").equals(""))) {
            toolbar.setTitle(R.string.title_help);
        } else {
            toolbar.setTitle(R.string.title_activity_contact_us);
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onGoToCreateTicket(Bundle bundle) {
        bundleCreateTicket = bundle;
        if (getFragmentManager().findFragmentByTag(CreateTicketFormFragment.class.getSimpleName()) == null) {
            CreateTicketFormFragment fragment = CreateTicketFormFragment.createInstance(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
            transaction.add(R.id.main_view, fragment, CreateTicketFormFragment.class.getSimpleName());
            if (!getIntent().getBooleanExtra(ContactUsConstant.IS_CHAT_BOT, false)) {
                transaction.addToBackStack(CreateTicketFormFragment.class.getSimpleName());
            }
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else if (listener != null && listener.canGoBack()) {
            listener.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public void onFinishCreateTicket() {
        CommonUtils.UniversalToast(this, getString(R.string.title_contact_finish));
        if (GlobalConfig.isSellerApp() && SessionHandler.isV4Login(this)) {
            Intent intent = SellerAppRouter.getSellerHomeActivity(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                    .getHomeIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_FRAGMENT_BACKSTACK, getFragmentManager().findFragmentById(R.id.main_view).getTag());
        outState.putString(PARAM_URL, url);
        outState.putBundle(PARAM_BUNDLE, bundleCreateTicket);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    public interface BackButtonListener {
        void onBackPressed();

        boolean canGoBack();
    }
}