package com.tokopedia.core.session;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.Cart;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.fragment.FragmentSecurityQuestion;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.msisdn.activity.MsisdnActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.google.GoogleActivity;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionImpl;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by m.normansyah on 04/11/2015.
 * <p/>
 * inside Login activity :
 * 1. Login {@link LoginFragment}
 * 2. Security Question {@link FragmentSecurityQuestion}
 * 3. RegisterNewViewFragment {@link RegisterNewViewFragment}
 * 4. Facebook Login Fragment {@link LoginFragment}
 * 5. Google Login Fragment {@link LoginFragment}
 * 6. RegisterThirdFragment {@link RegisterThirdFragment}
 * <p/>
 * inside session package :
 * 1. Logout Fragment currently dialog is discard when rotate.
 */
public class Login extends GoogleActivity implements SessionView, GoogleActivity.GoogleListener, DownloadResultReceiver.Receiver {

    //    int whichFragmentKey;
    LocalCacheHandler cacheGTM;
    Session session;
    FragmentManager supportFragmentManager;
    Toolbar toolbar;
    SimpleFacebook simplefacebook;
    DownloadResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Tokopedia3);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }
        setContentView(R.layout.activity_login2);

        session = new SessionImpl(this);
        session.fetchExtras(getIntent());
        session.fetchDataAfterRotate(savedInstanceState);
        session.initDataInstance();

        supportFragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        supportFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.d(TAG, "back stack changed [count : " + backCount);
                if (backCount == 0) {
                    // block where back has been pressed. since backstack is zero.
                    SessionHandler.clearUserData(Login.this);// because user is back that reset all data
                    SessionHandler.deleteRegisterNext(Login.this);
                    Login.this.simplefacebook.logout(new OnLogoutListener() {
                        @Override
                        public void onLogout() {
                            Log.i(TAG, "logout facebook");
                        }
                    });
//                    finish();
                    destroy();
                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.login_fragment);
                    session.setWhichFragment(((BaseView) fragment).getFragmentId());
                    setToolbarTitle();
                    invalidateOptionsMenu();
                }
            }
        });

        setToolbarTitle();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initListener(this);
        simplefacebook = SimpleFacebook.getInstance(this);

         /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        cacheGTM = new LocalCacheHandler(this, AppEventTracking.GTM_CACHE);
    }

    @Override
    public void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG, int type) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_fragment, fragment, TAG);
        if (isAddtoBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void moveToRegisterThird(RegisterViewModel model, String type) {
        Log.d(TAG, messageTAG + " moveToRegisterThird : " + model);
        if (isFragmentCreated(REGISTER_THIRD)) {
            Fragment fragment = RegisterThirdFragment.newInstance(model, type);
            moveToFragment(fragment, true, REGISTER_THIRD, TkpdState.DrawerPosition.REGISTER_THIRD);
        }
    }

    public void moveToRegisterPassPhone(CreatePasswordModel model) {
        Log.d(TAG, messageTAG + " moveToRegisterThird : " + model);
        if (isFragmentCreated(REGISTER_THIRD)) {
            Fragment fragment = RegisterPassPhoneFragment.newInstance(model, null);
            moveToFragment(fragment, false, REGISTER_THIRD, TkpdState.DrawerPosition.REGISTER_THIRD);
        }
    }

    @Override
    public void moveToRegisterPassPhone(CreatePasswordModel model, List<String> createPasswordList) {
        Log.d(TAG, messageTAG + " moveToRegisterThird : " + model);
        if (isFragmentCreated(REGISTER_THIRD)) {
            Fragment fragment = RegisterPassPhoneFragment.newInstance(model, createPasswordList);
            moveToFragment(fragment, false, REGISTER_THIRD, TkpdState.DrawerPosition.REGISTER_THIRD);
        }
    }


    @Override
    public void moveToNewRegisterNext(String email, String password, boolean isAutoVerify) {
        Log.d(TAG, messageTAG + " moveToRegisterNext : " + email + " password : " + password);
        if (isFragmentCreated(REGISTER_NEXT_TAG)) {
            Fragment fragment = RegisterNewNextFragment.newInstance(email, password, isAutoVerify);
            moveToFragment(fragment, true, REGISTER_NEXT_TAG, TkpdState.DrawerPosition.REGISTER_NEXT);
        }
    }

    @Override
    public void moveToRegisterNext(RegisterViewModel model) {
        Log.d(TAG, messageTAG + " moveToRegisterNext : " + model);
        if (isFragmentCreated(REGISTER_NEXT_TAG)) {
            Fragment fragment = RegisterNextFragment.newInstance(model.getmName(), model.getmPhone(), model.getmGender(), model.getDateText(), model.getmEmail());
            moveToFragment(fragment, true, REGISTER_NEXT_TAG, TkpdState.DrawerPosition.REGISTER_NEXT);
        }
    }

    @Override
    public void moveTo(int type) {
        //[BUGFIX] AN-1640 Home: 'Register' page should navigate into
        // 'Home' page when the user click on device back arrow.
        Log.d(getClass().getSimpleName(), "moveTo " + type);
        switch (type) {
            case MOVE_TO_CART_TYPE:
                if (isSellerApp() && !SessionHandler.isMsisdnVerified()) {
                    Intent intent = new Intent(this, MsisdnActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (SessionHandler.isV4Login(this)) {
                        startActivity(new Intent(this, Cart.class));
                    } else {
                        Intent intent = new Intent(this, HomeRouter.getHomeActivityClass());
                        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_HOME);
                        startActivity(intent);
                    }
                }
                break;
            case HOME:
                if (isSellerApp() && !SessionHandler.isMsisdnVerified()) {
                    Intent intent = new Intent(this, MsisdnActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else{
                if (SessionHandler.isV4Login(this)) {
                    Intent intent = new Intent(this, HomeRouter.getHomeActivityClass());
                    intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                            HomeRouter.INIT_STATE_FRAGMENT_FEED);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, HomeRouter.getHomeActivityClass());
                    intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                            HomeRouter.INIT_STATE_FRAGMENT_HOME);
                    startActivity(intent);
                }}
                break;
        }
    }

    private boolean isSellerApp() {
        return getApplication().getClass().getSimpleName().equals("SellerMainApplication");
    }

    @Override
    public void setToolbarTitle() {
        switch (session.getWhichFragment()) {
            case TkpdState.DrawerPosition.LOGIN:
                toolbar.setTitle(getString(R.string.title_activity_login));
                break;
            case TkpdState.DrawerPosition.REGISTER_THIRD:
            case TkpdState.DrawerPosition.REGISTER:
                toolbar.setTitle(getString(R.string.title_activity_register));
                break;
            case TkpdState.DrawerPosition.SECURITY_QUESTION:
                toolbar.setTitle(getString(R.string.title_security_question));
                break;
            case TkpdState.DrawerPosition.REGISTER_NEXT:
                toolbar.setTitle(getString(R.string.title_activity_register));
                break;
            case TkpdState.DrawerPosition.ACTIVATION_RESENT:
                toolbar.setTitle(getString(R.string.title_activity_activation));
                break;
            case TkpdState.DrawerPosition.FORGOT_PASSWORD:
                toolbar.setTitle(getString(R.string.title_activity_forgot_password));
                break;
        }
    }

    @Override
    public void moveToActivationResend(String email) {
        // START CLEAR ALL FRAGMENT STACK
        if (supportFragmentManager.getBackStackEntryCount() > 1) {
            FragmentManager.BackStackEntry first = supportFragmentManager.getBackStackEntryAt(1);
            supportFragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        // END OF CLEAR ALL FRAGMENT STACK
        showError("Akun anda belum diaktivasi. Cek email anda untuk mengaktivasi akun.");
        Fragment fragment = ActivationResentFragment.newInstance(email);
        moveToFragment(fragment, false, ACTIVATION_RESEND_TAG, TkpdState.DrawerPosition.ACTIVATION_RESENT);

        // Change the header

        session.setWhichFragment(TkpdState.DrawerPosition.ACTIVATION_RESENT);
        setToolbarTitle();
        invalidateOptionsMenu();
    }

    @Override
    public void moveToRegister() {
        Fragment fragment = RegisterNewViewFragment.newInstance();
        moveToFragment(fragment, true, REGISTER_FRAGMENT_TAG, TkpdState.DrawerPosition.REGISTER);

        // Change the header
        session.setWhichFragment(TkpdState.DrawerPosition.REGISTER);
        setToolbarTitle();
        invalidateOptionsMenu();
    }

    @Override
    public void moveToForgotPassword() {
        // START CLEAR ALL FRAGMENT STACK
        Log.d(TAG, messageTAG + supportFragmentManager.getBackStackEntryCount());
        if (supportFragmentManager.getBackStackEntryCount() > 1) {
            FragmentManager.BackStackEntry first = supportFragmentManager.getBackStackEntryAt(1);
            supportFragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        // END OF CLEAR ALL FRAGMENT STACK

        Fragment fragment = new ForgotPasswordFragment();
        moveToFragment(fragment, true, FORGOT_PASSWORD_TAG, TkpdState.DrawerPosition.FORGOT_PASSWORD);

        // Change the header
        session.setWhichFragment(TkpdState.DrawerPosition.FORGOT_PASSWORD);
        setToolbarTitle();
        invalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            simplefacebook.onActivityResult(requestCode, resultCode, data);
        } catch (NullPointerException e) {
            e.printStackTrace();
            CommonUtils.UniversalToast(MainApplication.getAppContext(), MainApplication.getAppContext().getString(R.string.try_again));
        }
    }

    @Override
    public void onCancelChooseAccount() {
        Fragment fragment = supportFragmentManager.findFragmentById(R.id.login_fragment);
        if (fragment instanceof LoginFragment && fragment.isVisible()) {
            ((LoginFragment) fragment).showProgress(false);
        }
        if (fragment instanceof RegisterNewViewFragment && fragment.isVisible()) {
            ((RegisterNewViewFragment) fragment).showProgress(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (session.getWhichFragment()) {
            case TkpdState.DrawerPosition.LOGIN:
                if (isFragmentCreated(LOGIN_FRAGMENT_TAG)) {
                    Log.d(TAG, messageTAG + LoginFragment.class.getSimpleName() + " is created !!!");
                    Intent intent = getIntent();
                    String mEmail = "";
                    boolean GoToIndex = false;
                    if (intent != null) {
                        mEmail = intent.getStringExtra(com.tokopedia.core.session.presenter.Login.EXTRA_EMAIL);
                        GoToIndex = intent.getBooleanExtra(com.tokopedia.core.session.presenter.Login.GO_TO_INDEX_KEY, false);
                    }
                    Fragment loginFragment = LoginFragment.newInstance(mEmail, GoToIndex);
                    moveToFragment(loginFragment, true, LOGIN_FRAGMENT_TAG, TkpdState.DrawerPosition.LOGIN);
                } else {
                    Log.d(TAG, messageTAG + LoginFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case TkpdState.DrawerPosition.REGISTER:
                if (isFragmentCreated(REGISTER_FRAGMENT_TAG)) {
//                    Fragment fragment = RegisterFragment.newInstance("test 1", "test 2");
                    Fragment fragment = RegisterNewViewFragment.newInstance();
                    moveToFragment(fragment, true, REGISTER_FRAGMENT_TAG, TkpdState.DrawerPosition.REGISTER);

                    // Change the header
                    session.setWhichFragment(TkpdState.DrawerPosition.REGISTER);
                    setToolbarTitle();
                    invalidateOptionsMenu();
                } else {
                    Log.d(TAG, messageTAG + RegisterFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case TkpdState.DrawerPosition.SECURITY_QUESTION:
                if (isFragmentCreated(SECURITY_QUESTION_TAG)) {
                    Log.d(TAG, messageTAG + FragmentSecurityQuestion.class.getSimpleName() + " is created !!!");
                    Fragment fragment = FragmentSecurityQuestion
                            .newInstance(0, 0, "", null);
                    moveToFragment(fragment, false, SECURITY_QUESTION_TAG, TkpdState.DrawerPosition.SECURITY_QUESTION);
                } else {
                    Log.d(TAG, messageTAG + FragmentSecurityQuestion.class.getSimpleName() + " is not created !!!");
                }
                break;
            case TkpdState.DrawerPosition.REGISTER_NEXT:
                if (isFragmentCreated(REGISTER_NEXT_TAG)) {
                    Log.d(TAG, messageTAG + " : currently RegisterNext cannot be called outside registerFragment");
                } else {
                    Log.d(TAG, messageTAG + RegisterNextFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case TkpdState.DrawerPosition.ACTIVATION_RESENT:
                if (isFragmentCreated(ACTIVATION_RESEND_TAG)) {
                    Log.d(TAG, messageTAG + " : currently Activation Reset cannot be called outside session");
                } else {
                    Log.d(TAG, messageTAG + ActivationResentFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case TkpdState.DrawerPosition.FORGOT_PASSWORD:
                if (isFragmentCreated(FORGOT_PASSWORD_TAG)) {
                    Fragment fragment = new ForgotPasswordFragment();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.login_fragment, fragment, FORGOT_PASSWORD_TAG);
                    fragmentTransaction.commit();
                } else {
                    Log.d(TAG, messageTAG + ForgotPasswordFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
        }
        sendNotifLocalyticsCallback();
    }

    @Override
    public void moveToFragmentSecurityQuestion(int security1, int security2, int userId) {
        Fragment fragment = FragmentSecurityQuestion
                .newInstance(security1, security2, userId + "", null);
        moveToFragment(fragment, false, SECURITY_QUESTION_TAG, TkpdState.DrawerPosition.SECURITY_QUESTION);

        // Change the header
        session.setWhichFragment(TkpdState.DrawerPosition.SECURITY_QUESTION);
        setToolbarTitle();
        invalidateOptionsMenu();
    }

    @Override
    public void destroy() {
        Log.d(getClass().getSimpleName(), "destroy");
        this.setResult(RESULT_OK);
        this.finish();
        session.finishTo();
    }

    /**
     * @param tag
     * @return true means fragment is null
     */
    @Override
    public boolean isFragmentCreated(String tag) {
        return supportFragmentManager.findFragmentByTag(tag) == null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
//        Log.d(TAG, messageTAG + "onSaveInstance whichFragment : " + whichFragmentKey);
//        outState.putInt(WHICH_FRAGMENT_KEY, whichFragmentKey);
        session.saveDataBeforeRotate(outState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.d(TAG, messageTAG + "onSaveInstance  API21+ whichFragment : " + whichFragmentKey);
//        outState.putInt(WHICH_FRAGMENT_KEY, whichFragmentKey);
        session.saveDataBeforeRotate(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R2.id.home:
                Log.d(TAG, messageTAG + " R.id.home !!!");
                return true;
            case android.R.id.home:
                Log.d(TAG, messageTAG + " android.R.id.home !!!");
                getSupportFragmentManager().popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSignedInUI() {
        updateUI(true);
    }

    @Override
    public void showSignedOutUI() {
        updateUI(false);
    }

    @Override
    public void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            if (currentPerson != null) {
                // Show signed-in user's name
                String name = currentPerson.getDisplayName();

                LoginGoogleModel model = new LoginGoogleModel();
                model.setFullName(currentPerson.getDisplayName());
                model.setGoogleId(currentPerson.getId());
                model.setEmail(email);
                model.setGender(getGenderFromGoogle(currentPerson));
                model.setBirthday(getBirthdayFromGoogle(currentPerson));
                model.setImageUrl(currentPerson.getImage().getUrl());

                Fragment fragment = supportFragmentManager.findFragmentById(R.id.login_fragment);
                if (fragment instanceof LoginFragment && fragment.isVisible()) {
                    ((LoginFragment) fragment).startLoginWithGoogle(LoginModel.GoogleType, model);
                }

                // [START] pass some data to register fragment
                fragment = supportFragmentManager.findFragmentById(R.id.login_fragment);
                if (fragment instanceof RegisterNewViewFragment && fragment.isVisible()) {
                    ((RegisterNewViewFragment) fragment).startLoginWithGoogle(LoginModel.GoogleType, model);
                }
                // [END] pass some data to register fragment

                // Show users' email address (which requires GET_ACCOUNTS permission)
//                if (checkAccountsPermission()) {
//                String currentAccount = Plus.AccountApi.getAccountName(mGoogleApiClient);
//                Toast.makeText(this, name+" currentAccount : "+currentAccount, Toast.LENGTH_LONG).show();
//                }
            } else {
                // If getCurrentPerson returns null there is generally some error with the
                // configuration of the application (invalid Client ID, Plus API not enabled, etc).
                Log.w(TAG, "null person");
                SnackbarManager.make(this, "Profil tidak ditemukan", Snackbar.LENGTH_LONG).show();
            }

            onDisconnectClicked();
            // Set button visibility
        } else {
            // Show signed-out message and clear email field

            // Set button visibility
        }
    }

    @Override
    public String getGenderFromGoogle(Person user) {
        String response;
        switch (user.getGender()) {
            case 0:
                response = "male";
                break;
            case 1:
                response = "female";
                break;
            default:
                response = "male";
                break;
        }
        return response;
    }

    @Override
    public String getBirthdayFromGoogle(Person user) {
        String response = null;
        if (user.getBirthday() == null) {
            response = "01/01/1991";
        } else {
            String[] temp = user.getBirthday().split("-");
            response = temp[1] + "/" + temp[2] + "/" + temp[0];
        }
        return response;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(DownloadService.TYPE, DownloadService.INVALID_TYPE);
        Fragment fragment = null;
        switch (type) {
            case DownloadService.LOGIN_EMAIL:
                fragment = supportFragmentManager.findFragmentByTag(LOGIN_FRAGMENT_TAG);
                break;
            case DownloadService.SECURITY_QUESTION_GET:
            case DownloadService.REQUEST_OTP:
            case DownloadService.ANSWER_SECURITY_QUESTION:
                fragment = supportFragmentManager.findFragmentByTag(SECURITY_QUESTION_TAG);
                break;
            case DownloadService.REGISTER:
            case DownloadService.REGISTER_LOGIN:
                fragment = supportFragmentManager.findFragmentByTag(REGISTER_NEXT_TAG);
                break;

            case DownloadService.REGISTER_THIRD:
            case DownloadService.REGISTER_THIRD_LOGIN:
            case DownloadService.REGISTER_PASS_PHONE:
                fragment = supportFragmentManager.findFragmentByTag(REGISTER_THIRD);
                break;
            case DownloadService.LOGIN_GOOGLE:
            case DownloadService.LOGIN_FACEBOOK:
            case DownloadService.LOGIN_ACCOUNTS_TOKEN:
            case DownloadService.LOGIN_ACCOUNTS_INFO:
            case DownloadService.LOGIN_ACCOUNTS_PROFILE:
            case DownloadService.LOGIN_WEBVIEW:
            case DownloadService.MAKE_LOGIN:
            case DownloadService.DISCOVER_LOGIN:
            case DownloadService.REGISTER_FACEBOOK:
//                fragment = supportFragmentManager.findFragmentByTag(LOGIN_FRAGMENT_TAG);
//                if(fragment==null || !fragment.isVisible()){
//                    fragment = supportFragmentManager.findFragmentByTag(REGISTER_FRAGMENT_TAG);
//                }
                fragment = supportFragmentManager.findFragmentById(R.id.login_fragment);
                break;
            case DownloadService.RESET_PASSWORD:
                fragment = supportFragmentManager.findFragmentByTag(FORGOT_PASSWORD_TAG);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }

        //check if Fragment implement necessary interface
        if (fragment != null && fragment instanceof BaseView && type != DownloadService.INVALID_TYPE) {
            switch (resultCode) {
                case DownloadService.STATUS_RUNNING:
                    switch (type) {
                        case DownloadService.LOGIN_EMAIL:
                        case DownloadService.LOGIN_GOOGLE:
                        case DownloadService.LOGIN_FACEBOOK:
                        case DownloadService.LOGIN_ACCOUNTS_TOKEN:
                        case DownloadService.LOGIN_ACCOUNTS_INFO:
                        case DownloadService.LOGIN_ACCOUNTS_PROFILE:
                        case DownloadService.MAKE_LOGIN:
                        case DownloadService.LOGIN_WEBVIEW:
                        case DownloadService.DISCOVER_LOGIN:
                        case DownloadService.REGISTER_FACEBOOK:
                            //[START] show progress bar
                            if (fragment instanceof LoginFragment) {
                                boolean showDialog = resultData.getBoolean(DownloadService.LOGIN_SHOW_DIALOG, false);
                                ((LoginFragment) fragment).showProgress(showDialog);
                            }
                            if (fragment instanceof RegisterNewViewFragment) {
                                boolean showDialog = resultData.getBoolean(DownloadService.LOGIN_SHOW_DIALOG, false);
                                ((RegisterNewViewFragment) fragment).showProgress(showDialog);
                            }
                            if (fragment instanceof RegisterPassPhoneFragment) {
                                boolean showDialog = resultData.getBoolean(DownloadService.LOGIN_SHOW_DIALOG, false);
                                ((RegisterPassPhoneFragment) fragment).showProgress(showDialog);
                            }
                            break;
                        case DownloadService.REQUEST_OTP:
                        case DownloadService.ANSWER_SECURITY_QUESTION:
                            if (fragment instanceof FragmentSecurityQuestion) {
                                boolean showDialog = resultData.getBoolean(DownloadService.SECURITY_QUESTION_LOADING, false);
                                ((FragmentSecurityQuestion) fragment).displayProgress(showDialog);
                            }
                            break;
                        case DownloadService.REGISTER:
                            //[START] This is old register
//                            if(fragment instanceof  RegisterNextFragment){
//                                boolean showDialog = resultData.getBoolean(DownloadService.REGISTER_QUESTION_LOADING, false);
//                                ((RegisterNextFragment)fragment).showProgress(showDialog);
//                            }
                            //[END] This is old register

                            if (fragment instanceof RegisterNewNextFragment) {
                                boolean showDialog = resultData.getBoolean(DownloadService.REGISTER_QUESTION_LOADING, false);
                                ((RegisterNewNextFragment) fragment).showProgress(showDialog);
                            }
                            break;
                        case DownloadService.REGISTER_THIRD:
                            if (fragment instanceof RegisterThirdFragment) {
                                boolean showDialog = resultData.getBoolean(DownloadService.REGISTER_QUESTION_LOADING, false);
                                ((RegisterThirdFragment) fragment).showProgress(showDialog);
                            }
                            break;
                    }
                    break;
                case DownloadService.STATUS_FINISHED:
                    switch (type) {
                        case DownloadService.LOGIN_EMAIL:
//                        case DownloadService.LOGIN_GOOGLE:
//                        case DownloadService.LOGIN_FACEBOOK:
                        case DownloadService.SECURITY_QUESTION_GET:
                        case DownloadService.REQUEST_OTP:
                        case DownloadService.ANSWER_SECURITY_QUESTION:
                        case DownloadService.REGISTER:
                        case DownloadService.REGISTER_LOGIN:
                        case DownloadService.REGISTER_THIRD:
                        case DownloadService.REGISTER_THIRD_LOGIN:
                        case DownloadService.REGISTER_PASS_PHONE:
                        case DownloadService.LOGIN_ACCOUNTS_INFO:
                        case DownloadService.MAKE_LOGIN:
                            sendBroadcast(new Intent(ShopInfoActivity.LOGIN_ACTION));
                            if (resultData.getBoolean(DownloadService.RETRY_FLAG, false)) {
                                boolean retry = resultData.getBoolean(DownloadService.RETRY_FLAG, false);
                                ((BaseView) fragment).ariseRetry(type, retry);
                            } else {
                                if (resultData.getBoolean(DownloadService.LOGIN_MOVE_REGISTER_THIRD, false)) {// register new for third party
                                    CreatePasswordModel model = Parcels.unwrap(resultData.getParcelable(DownloadService.LOGIN_GOOGLE_MODEL_KEY));
                                    moveToRegisterPassPhone(model);
                                } else {
                                    ((BaseView) fragment).setData(type, resultData);
                                    sendGTMEvent();
                                    sendLocalytics(resultData);
                                }
                            }
                            break;
                        case DownloadService.DISCOVER_LOGIN:
                            if (fragment instanceof LoginFragment || fragment instanceof RegisterNewViewFragment)
                                ((BaseView) fragment).setData(type, resultData);
                            break;

                        case DownloadService.LOGIN_GOOGLE:
                        case DownloadService.LOGIN_ACCOUNTS_TOKEN:
                        case DownloadService.LOGIN_FACEBOOK:
                        case DownloadService.LOGIN_WEBVIEW:
                        case DownloadService.REGISTER_FACEBOOK:
                            Log.d("steven", "berhasil minta token");
                            sendDataFromInternet(DownloadService.LOGIN_ACCOUNTS_INFO, resultData);
                            break;
                        case DownloadService.RESET_PASSWORD:
                            ((BaseView) fragment).setData(type, resultData);
                            break;
//                        case DownloadService.LOGIN_ACCOUNTS_INFO:
//                            Log.d("steven", "berhasil minta info");
//                            sendDataFromInternet(DownloadService.LOGIN_ACCOUNTS_PROFILE,resultData);
//                            break;
                    }
                    break;
                case DownloadService.STATUS_ERROR:
                    switch (resultData.getInt(DownloadService.NETWORK_ERROR_FLAG, DownloadService.INVALID_NETWORK_ERROR_FLAG)) {
                        case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " BAD_REQUEST_NETWORK_ERROR !!!");
                            break;
                        case NetworkConfig.INTERNAL_SERVER_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " INTERNAL_SERVER_ERROR !!!");
                            break;
                        case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " FORBIDDEN_NETWORK_ERROR !!!");
                            break;
                        case DownloadService.INVALID_NETWORK_ERROR_FLAG:
                        default:
                            String messageError = resultData.getString(DownloadService.MESSAGE_ERROR_FLAG, DownloadService.INVALID_MESSAGE_ERROR);
                            if (!messageError.equals(DownloadService.INVALID_MESSAGE_ERROR)) {
                                ((BaseView) fragment).onMessageError(type, messageError);
                            }
                    }
                    break;
            }// end of status download service
        }
    }

    @Override
    public void sendDataFromInternet(int type, Bundle data) {
        switch (type) {
            case DownloadService.REQUEST_OTP:
            case DownloadService.ANSWER_SECURITY_QUESTION:
            case DownloadService.SECURITY_QUESTION_GET:
            case DownloadService.LOGIN_EMAIL:
            case DownloadService.LOGIN_GOOGLE:
            case DownloadService.LOGIN_FACEBOOK:
            case DownloadService.REGISTER:
            case DownloadService.REGISTER_LOGIN:
            case DownloadService.REGISTER_THIRD:
            case DownloadService.REGISTER_THIRD_LOGIN:
            case DownloadService.REGISTER_PASS_PHONE:
            case DownloadService.LOGIN_ACCOUNTS_TOKEN:
            case DownloadService.LOGIN_ACCOUNTS_INFO:
            case DownloadService.LOGIN_ACCOUNTS_PROFILE:
            case DownloadService.MAKE_LOGIN:
            case DownloadService.LOGIN_WEBVIEW:
            case DownloadService.DISCOVER_LOGIN:
            case DownloadService.REGISTER_FACEBOOK:
            case DownloadService.RESET_PASSWORD:
                DownloadService.startDownload(this, mReceiver, data, type);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void prevFragment() {
        supportFragmentManager.popBackStack();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sendNotifLocalyticsCallback();
    }

    private void sendNotifLocalyticsCallback() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)) {
                TrackingUtils.eventLocaNotificationCallback(getIntent());
            }
        }
    }

    @Override
    public void showError(String text) {
        if (text != null) {
            SnackbarManager.make(this, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void sendGTMEvent() {
        switch (cacheGTM.getString(AppEventTracking.GTMCacheKey.SESSION_STATE)) {
            case AppEventTracking.GTMCacheValue.LOGIN:
                UnifyTracking.eventLoginSuccess(cacheGTM.getString(
                        AppEventTracking.GTMCacheKey.LOGIN_TYPE,
                        AppEventTracking.DEFAULT_CHANNEL
                ));
                break;
            case AppEventTracking.GTMCacheValue.REGISTER:
                UnifyTracking.eventRegisterSuccess(cacheGTM.getString(
                        AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                        AppEventTracking.DEFAULT_CHANNEL
                ));
                break;
        }
    }

    private void sendLocalytics(Bundle bundle) {
        switch (cacheGTM.getString(AppEventTracking.GTMCacheKey.SESSION_STATE)) {
            case AppEventTracking.GTMCacheValue.LOGIN:
                Map<String, String> attributesLogin = new HashMap<String, String>();
                CustomerWrapper customerLogin = new CustomerWrapper();
                customerLogin.setCustomerId(bundle.getString(AppEventTracking.USER_ID_KEY,
                        AppEventTracking.NOT_AVAILABLE));
                customerLogin.setFullName(bundle.getString(AppEventTracking.FULLNAME_KEY,
                        AppEventTracking.NOT_AVAILABLE));
                customerLogin.setEmailAddress(bundle.getString(AppEventTracking.EMAIL_KEY,
                        AppEventTracking.NOT_AVAILABLE));
                customerLogin.setExtraAttr(attributesLogin);
                customerLogin.setMethod(cacheGTM.getString(
                        AppEventTracking.GTMCacheKey.LOGIN_TYPE,
                        AppEventTracking.DEFAULT_CHANNEL));

                UnifyTracking.eventLoginLoca(customerLogin);

                break;
            case AppEventTracking.GTMCacheValue.REGISTER:
                Map<String, String> attributesRegister = new HashMap<String, String>();
                CustomerWrapper customerRegister = new CustomerWrapper();
                customerRegister.setCustomerId(bundle.getString(AppEventTracking.USER_ID_KEY,
                        AppEventTracking.NOT_AVAILABLE));
                customerRegister.setFullName(bundle.getString(AppEventTracking.FULLNAME_KEY,
                        AppEventTracking.NOT_AVAILABLE));
                customerRegister.setEmailAddress(bundle.getString(AppEventTracking.EMAIL_KEY,
                        AppEventTracking.NOT_AVAILABLE));
                customerRegister.setExtraAttr(attributesRegister);
                customerRegister.setMethod(cacheGTM.getString(
                        AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                        AppEventTracking.DEFAULT_CHANNEL
                ));
                UnifyTracking.eventRegisterLoca(customerRegister);
                break;
        }
    }
}
