package com.tokopedia.core.session;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.google.GoogleActivity;
import com.tokopedia.core.session.model.LoginModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.presenter.LoginImpl;
import com.tokopedia.core.session.presenter.LoginView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author m.normansyah
 * @since 4-11-2015
 */

/**
 * edited by steven
 */
@RuntimePermissions
public class LoginFragment extends Fragment implements LoginView {
    // demo only
    int anTestInt = 0;
    com.tokopedia.core.session.presenter.Login login;

    Context mContext;

    View rootView;
    @Bind(R2.id.email_auto)
    AutoCompleteTextView mEmailView;
    //    @Bind(R2.id.gplus_login)
//    TextView mGplusLogin;
    @Bind(R2.id.password)
    PasswordView mPasswordView;
    @Bind(R2.id.login_form)
    ScrollView mLoginFormView;
    @Bind(R2.id.login_status)
    RelativeLayout mLoginStatusView;
    @Bind(R2.id.login_status_message)
    TextView mLoginStatusMessageView;
    @Bind(R2.id.sign_in_button)
    TextView signInButton;
    @Bind(R2.id.register_button)
    TextView registerButton;
    @Bind(R2.id.forgot_pass)
    TextView forgotPass;
//    @Bind(R2.id.facebook_login)
//    TextView facebookLogin;

    @Bind(R2.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R2.id.accounts_sign_in)
    TextView accountSignIn;

    ArrayAdapter<String> autoCompleteAdapter;
    List<LoginProviderModel.ProvidersBean> listProvider;
    Snackbar snackbar;

    LocalCacheHandler cacheGTM;


    public static LoginFragment newInstance(String mEmail, boolean goToIndex) {
        Bundle extras = new Bundle();
        extras.putString("mEmail", mEmail);
        extras.putBoolean("goToIndex", goToIndex);
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(extras);
        return loginFragment;
    }

    public LoginFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cacheGTM = new LocalCacheHandler(getActivity(), AppEventTracking.GTM_CACHE);
        cacheGTM.putString(AppEventTracking.GTMCacheKey.SESSION_STATE,
                AppEventTracking.GTMCacheValue.LOGIN);
        cacheGTM.applyEditor();

        login = new LoginImpl(this);
        login.initLoginInstance(mContext);
        login.fetchDataAfterRotate(savedInstanceState);
        login.fetchIntenValues(getArguments());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // this is for testing only
        if (savedInstanceState != null)
            Log.d(TAG, LoginFragment.class.getSimpleName() + " : get testing data : " + (anTestInt = savedInstanceState.getInt(TEST_INT_KEY)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_reborn, container, false);
        ButterKnife.bind(this, rootView);
        setListener();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        login.initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        login.unSubscribe();
        ButterKnife.unbind(this);
        dismissSnackbar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // this is for testing only
        outState.putInt(TEST_INT_KEY, ++anTestInt);
        if (mEmailView != null && mPasswordView != null && listProvider != null)
            login.saveDatabeforeRotate(outState, mEmailView.getText().toString(), mPasswordView.getText().toString());
        else
            login.saveDatabeforeRotate(outState, "", "");
    }

    @Override
    public void setAutoCompleteAdapter(List<String> LoginIdList) {
        autoCompleteAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, LoginIdList);
        mEmailView.setAdapter(autoCompleteAdapter);
    }


    @Override
    public void setEmailText(String text) {
        mEmailView.setText(text);
    }

    @Override
    public void startLoginWithGoogle(String LoginType, Object model) {
        login.startLoginWithGoogle(LoginType, model);
        storeCacheGTM(AppEventTracking.GTMCacheKey.LOGIN_TYPE,
                AppEventTracking.GTMCacheValue.GMAIL);
    }

//    @OnClick(R2.id.gplus_login)
//    public void onGoogleLogin() {
//        ((GoogleActivity) getActivity()).onSignInClicked();
//    }


    @Override
    public void setListener() {
        String sourceString = "Belum punya akun? <b>Daftar</b>";
        registerButton.setText(Html.fromHtml(sourceString));
        mPasswordView.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            FocusPair focusPair = validateSignIn();
                            if (focusPair.isFocus()) {
                                focusPair.getView().requestFocus();
                            } else {
                                // Show a progress spinner, and kick off a background task to
                                // perform the user login attempt.
                                mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                                KeyboardHandler.DropKeyboard(mContext, mEmailView);
                                LoginViewModel model = new LoginViewModel();
                                if (mPasswordView != null && mEmailView != null) {
                                    model.setUsername(mEmailView.getText().toString());
                                    model.setPassword(mPasswordView.getText().toString());
                                    model.setIsEmailClick(true);
                                    login.sendDataFromInternet(LoginModel.EmailType, model);
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                });
        mEmailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mPasswordView.requestFocus();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save last input email
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
                if (login.addAutoCompleteData(mEmailView.getText().toString())) {
                    setAutoCompleteAdapter(login.getLastLoginIdList());
                    notifyAutoCompleteAdapter();
                }

                FocusPair focusPair = validateSignIn();
                if (focusPair.isFocus()) {
                    focusPair.getView().requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                    KeyboardHandler.DropKeyboard(mContext, mEmailView);
                    LoginViewModel model = new LoginViewModel();
                    if (mPasswordView != null && mEmailView != null) {
                        model.setUsername(mEmailView.getText().toString());
                        model.setPassword(mPasswordView.getText().toString());
                        model.setIsEmailClick(true);
                        login.sendDataFromInternet(LoginModel.EmailType, model);
                    }
                }
            }
        });

        accountSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save last input email
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
                if (login.addAutoCompleteData(mEmailView.getText().toString())) {
                    setAutoCompleteAdapter(login.getLastLoginIdList());
                    notifyAutoCompleteAdapter();
                }

                FocusPair focusPair = validateSignIn();
                if (focusPair.isFocus()) {
                    focusPair.getView().requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                    KeyboardHandler.DropKeyboard(mContext, mEmailView);
                    LoginViewModel model = new LoginViewModel();
                    if (mPasswordView != null && mEmailView != null) {
                        model.setUsername(mEmailView.getText().toString());
                        model.setPassword(mPasswordView.getText().toString());
                        model.setIsEmailClick(true);
                        login.getToken(LoginModel.EmailType, model);
                    }
                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SessionView) getActivity()).moveToRegister();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof SessionView) {
                    ((SessionView) getActivity()).moveToForgotPassword();
                }
            }
        });
//
//        facebookLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                login.loginFacebook();
//            }
//        });
    }

    //
//    @OnClick(R2.id.facebook_login)
    public void onFacebookClick() {
        login.loginFacebook();
        storeCacheGTM(AppEventTracking.GTMCacheKey.LOGIN_TYPE,
                AppEventTracking.GTMCacheValue.FACEBOOK);
    }

    @Override
    public void notifyAutoCompleteAdapter() {
        autoCompleteAdapter.notifyDataSetChanged();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void showProgress(final boolean isShow) {
        //[START] save progress for rotation
        login.updateViewModel(LoginViewModel.ISPROGRESSSHOW, isShow);
        if(isShow && snackbar!=null) snackbar.dismiss();
        //[END] save progress for rotation

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(isShow ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //[END] save progress for rotation
                            if (mLoginStatusView != null)
                                mLoginStatusView.setVisibility(isShow ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(isShow ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //[END] save progress for rotation
                            if (mLoginFormView != null)
                                mLoginFormView.setVisibility(isShow ? View.GONE
                                        : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(isShow ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(isShow ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public FocusPair validateSignIn() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setPasswordError(null);

        // Store values at the time of the login attempt.
        Log.d(TAG, messageTAG + " login : " + login);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        FocusPair focusPair = new FocusPair();

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setPasswordError(getString(R.string.error_field_required));
            focusPair.setView(mPasswordView);
            focusPair.setIsFocus(true);
        } else if (password.length() < 4) {
            mPasswordView.setPasswordError(getString(R.string.error_incorrect_password));
            focusPair.setView(mPasswordView);
            focusPair.setIsFocus(true);
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusPair.setView(mEmailView);
            focusPair.setIsFocus(true);
        } else if (!CommonUtils.EmailValidation(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusPair.setView(mEmailView);
            focusPair.setIsFocus(true);
        }

        return focusPair;
    }

    @Override
    public void moveToFragmentSecurityQuestion(int security1, int security2, int userId) {
        if (mContext != null) {// && !((AppCompatActivity)mContext).isFinishing()
            ((SessionView) mContext).moveToFragmentSecurityQuestion(security1, security2, userId);
        }
    }

    public class FocusPair {
        View view;
        boolean isFocus;

        public FocusPair() {
        }

        public FocusPair(View view, boolean isFocus) {
            this.view = view;
            this.isFocus = isFocus;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public boolean isFocus() {
            return isFocus;
        }

        public void setIsFocus(boolean isFocus) {
            this.isFocus = isFocus;
        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.LOGIN;
    }

    @Override
    public void showDialog(String dialogText) {
        snackbar = SnackbarManager.make(getActivity(), dialogText, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void destroyActivity() {
        Log.d(getClass().getSimpleName(), "destroyActivity");
        if (getActivity() != null && getActivity() instanceof SessionView) {
            ((SessionView) getActivity()).destroy();
        }
    }

    @Override
    public void showProvider(List<LoginProviderModel.ProvidersBean> data) {
        listProvider = data;
        if (listProvider != null && checkHasNoProvider()) {
            login.saveProvider(listProvider);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0, 20, 0, 0);

            for (int i = 0; i < listProvider.size(); i++) {
                LoginTextView tv = new LoginTextView(getActivity());
                String color = listProvider.get(i).getColor();
                if (listProvider.get(i).getId().equalsIgnoreCase("facebook")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onFacebookClick();
                        }
                    });
                } else if (listProvider.get(i).getId().equalsIgnoreCase("gplus")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginFragmentPermissionsDispatcher.onGooglePlusClickedWithCheck(LoginFragment.this);
                        }
                    });
                } else {
                    tv.setOnClickListener(loginProvideOnClick(i));
                }
                if(color==null) {
                    generateLayout(tv, Color.parseColor("#FFFFFF"), i);
                }else{
                    generateLayout(tv, Color.parseColor(color), i);
                }
                if (linearLayout != null) {
                    linearLayout.addView(tv, linearLayout.getChildCount(), layoutParams);
                }
            }
        }
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void onGooglePlusClicked() {
        showProgress(true);
        ((GoogleActivity) getActivity()).onSignInClicked();
        storeCacheGTM(AppEventTracking.GTMCacheKey.LOGIN_TYPE,
                AppEventTracking.GTMCacheValue.GMAIL);
    }

    @Override
    public void addProgressbar() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = linearLayout.getChildCount() - 1;
        if (linearLayout != null && !(linearLayout.getChildAt(lastPos) instanceof ProgressBar))
            linearLayout.addView(pb, linearLayout.getChildCount());
    }

    @Override
    public void removeProgressBar() {
        int lastPos = linearLayout.getChildCount() - 1;
        if (linearLayout != null && linearLayout.getChildAt(lastPos) instanceof ProgressBar)
            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
    }

    private void generateLayout(LoginTextView tv, int color, int position) {
        //generate background
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{3, 3, 3, 3, 3, 3, 3, 3});
        shape.setColor(color);
        if (color == Color.WHITE) shape.setStroke(1, Color.BLACK);

        try {
            tv.setBackground(shape);
        }catch (NoSuchMethodError error){
            tv.setBackgroundDrawable(shape);
        }

        //generate other view
        TextView textView = (TextView) tv.findViewById(R.id.provider_name);
        textView.setTextColor(getInverseColor(color));
        textView.setText(String.format("Masuk Dengan %s", listProvider.get(position).getName()));
        ImageView imageView = (ImageView) tv.findViewById(R.id.provider_image);
        ImageHandler.loadImage2(imageView, listProvider.get(position).getImage()
                , R.drawable.ic_icon_toped_announce);

    }

    private int getInverseColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    public boolean checkHasNoProvider() {
        for (int i = linearLayout.getChildCount() - 1; i >= 0; i--) {
            if (linearLayout.getChildAt(i) instanceof LoginTextView) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void showError(String string) {
        SnackbarManager.make(getActivity(), string, Snackbar.LENGTH_LONG).show();
    }

    private View.OnClickListener loginProvideOnClick(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewLoginFragment newFragment = WebViewLoginFragment
                        .createInstance(listProvider.get(position).getUrl());
                newFragment.setTargetFragment(LoginFragment.this, 100);
                newFragment.show(getFragmentManager().beginTransaction(), "dialog");
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                storeCacheGTM(AppEventTracking.GTMCacheKey.LOGIN_TYPE,
                        listProvider.get(position).getName());
            }
        };
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        Log.d(TAG, messageTAG + " arise !!!");
        showDialog(getString(R.string.message_verification_timeout));
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        showProgress(false);
        //[START] move to activation resent
        if (text.contains("belum diaktivasi")) {
            if (mContext != null && mContext instanceof SessionView) {
                ((SessionView) mContext).moveToActivationResend(mEmailView.getText().toString());
            }
        }
        switch (type) {
            case DownloadService.DISCOVER_LOGIN:
                removeProgressBar();
                snackbar = SnackbarManager.make(getActivity(), "Gagal mendownload provider", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Coba lagi", retryDiscover());
                snackbar.show();
                break;
            default:
                snackbar = SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG);
                snackbar.show();
                login.getProvider();
                break;
        }
        //[END] move to activation resent
        mPasswordView.setText("");
    }

    private View.OnClickListener retryDiscover() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.getProvider();
            }
        };
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String) data[0];
        showDialog(text);
        showProgress(false);
    }

    @Override
    public void setData(int type, Bundle data) {
        if (login != null)
            login.setData(type, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                Bundle bundle = data.getBundleExtra("bundle");
                if (bundle.getString("path").contains("error")) {
                    snackbar = SnackbarManager.make(getActivity(), bundle.getString("message"), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (bundle.getString("path").contains("code")) {
                    login.sendDataFromInternet(LoginModel.WebViewType, bundle);
                } else if (bundle.getString("path").contains("activation-social")) {
                    ((SessionView) mContext).moveToActivationResend(mEmailView.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    private void dismissSnackbar() {
        if(snackbar!=null) snackbar.dismiss();
    }

    private void storeCacheGTM(String key, String value) {
        cacheGTM.putString(key, value);
        cacheGTM.applyEditor();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginFragmentPermissionsDispatcher.onRequestPermissionsResult(LoginFragment.this,requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.GET_ACCOUNTS)
    void showRationaleForGetAccounts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.GET_ACCOUNTS);
    }

    @OnPermissionDenied(Manifest.permission.GET_ACCOUNTS)
    void showDeniefForGetAccounts() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.GET_ACCOUNTS);
    }

    @OnNeverAskAgain(Manifest.permission.GET_ACCOUNTS)
    void showNeverAskForGetAccounts() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.GET_ACCOUNTS);
    }
}
