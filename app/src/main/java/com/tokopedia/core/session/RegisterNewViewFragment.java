package com.tokopedia.core.session;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.session.google.GoogleActivity;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.presenter.RegisterNew;
import com.tokopedia.core.session.presenter.RegisterNewImpl;
import com.tokopedia.core.session.presenter.RegisterNewView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.NetworkUtil;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by m.normansyah on 1/22/16.
 * 25-1-2016, start not rotatable register
 * features :
 * 1. list of email account from devices.
 * 2. show password
 */
@RuntimePermissions
public class RegisterNewViewFragment extends BaseFragment<RegisterNew> implements RegisterNewView {
    public static final int PASSWORD_MINIMUM_LENGTH = 6;

    @Bind(R2.id.register_name)
    AutoCompleteTextView registerName;
    @Bind(R2.id.register_password)
    PasswordView registerPassword;
    @Bind(R2.id.register_privacy_policy)
    RelativeLayout registerPrivacyPolicy;
    @Bind(R2.id.checkbox_privacy_policy)
    CheckBox checkboxPrivacyPolicy;
    @Bind(R2.id.register_next_progress)
    ProgressBar registerNextProgress;
    @Bind(R2.id.register_next)
    RelativeLayout registerNext;
    @Bind(R2.id.register_next_button)
    TextView registerNextButton;
    @Bind(R2.id.register_status)
    LinearLayout registerStatus;
    @Bind(R2.id.register_status_message)
    TextView registerStatusMessage;
    @Bind(R2.id.register_form)
    ScrollView registerForm;

    @Bind(R2.id.step_1) LinearLayout linearLayout;
    List<LoginProviderModel.ProvidersBean> listProvider;
    Context mContext;

    Snackbar snackbar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void showErrorValidateEmail() {
//        showProgress(false);
        showErrorValidateEmail(getString(R.string.alert_email_address_is_already_registered));
        sendGTMRegisterError(AppEventTracking.EventLabel.EMAIL);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_REGISTER;
    }

    @Override
    public void showErrorValidateEmail(String err) {
        snackbar = SnackbarManager.make(RegisterNewViewFragment.this.getActivity()
                ,err, Snackbar.LENGTH_LONG);
        snackbar.show();
        enableDisableAllFieldsForEmailValidationForm(true);

        sendGTMRegisterError(AppEventTracking.EventLabel.EMAIL);
    }

    @Override
    public void moveToRegisterNext(String email, String password) {
//        IsSaving = false;
        if(snackbar!=null){
            snackbar.dismiss();
        }
        if(getActivity()!=null&&getActivity() instanceof SessionView){
            ((SessionView)getActivity()).moveToNewRegisterNext(email, password, isEmailAddressFromDevice());
        }
    }

    @Override
    public void alertBox() {
        alertbox(getString(R.string.alert_on_email_edit_for_first_time));
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if(data.containsKey(RegisterNew.EMAIL)){
            String email = (String)data.get(RegisterNew.EMAIL);
            registerName.setText(email);
        }else if(data.containsKey(RegisterNew.PASSWORD)){
            String password = (String)data.get(RegisterNew.PASSWORD);
            registerPassword.setText(password);
        }else if(data.containsKey(RegisterNew.IS_CHECKED)){
            boolean isChecked = (boolean)data.get(RegisterNew.IS_CHECKED);
            checkboxPrivacyPolicy.setChecked(isChecked);
        }
    }

    public static Fragment newInstance(){
        return new RegisterNewViewFragment();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @Override
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        Log.d("steven check", String.valueOf(show));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            registerStatus.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(registerStatus!=null)
                                registerStatus.setVisibility(show ? View.VISIBLE
                                        : View.GONE);
                        }
                    });


            registerForm.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (registerForm != null)
                                registerForm.setVisibility(show ? View.GONE
                                        : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            registerStatus.setVisibility(show ? View.VISIBLE : View.GONE);
            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View parentView = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        showProgress(false);
        return parentView;
    }

    @OnClick(R2.id.register_privacy_policy)
    public void registerPrivacyPolicy(){
        checkboxPrivacyPolicy.setChecked(!checkboxPrivacyPolicy.isChecked());
    }

//    @OnClick(R2.id.facebook_login)
    public void onFacebookLoginClick(){
        showProgress(true);
        presenter.loginFacebook(getActivity());
        CommonUtils.dumper("LocalTag : TYPE : FACEBOOK");
        presenter.storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                AppEventTracking.GTMCacheValue.FACEBOOK
                );
    }

//    @OnClick(R2.id.gplus_login)
    public void onGoogleLoginClick(){
        showProgress(true);
        if(getActivity() instanceof GoogleActivity){
            ((GoogleActivity)getActivity()).onSignInClicked();
        }
        CommonUtils.dumper("LocalTag : TYPE : GOOGLE");
    }

    @Override
    public void startLoginWithGoogle(String type, LoginGoogleModel loginGoogleModel) {
        presenter.startLoginWithGoogle(getActivity(), type, loginGoogleModel);
        presenter.storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                AppEventTracking.GTMCacheValue.GMAIL
        );
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    public void attemptRegisterStep1() {
//        if (IsSaving) {
//            return;
//        }
        // Reset errors.
        registerName.setError(null);
        registerPassword.setPasswordError(null);

        // Store values at the time of the login attempt.
        String mEmail = registerName.getText().toString();
        String mPassword = registerPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password
        if (TextUtils.isEmpty(mPassword)) {
            registerPassword.setPasswordError(getString(R.string.error_field_required));
            focusView = registerPassword;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD);
        } else if (mPassword.length() < PASSWORD_MINIMUM_LENGTH) {
            registerPassword.setPasswordError(getString(R.string.error_invalid_password));
            focusView = registerPassword;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD);
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            registerName.setError(getString(R.string.error_field_required));
            focusView = registerName;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.EMAIL);
        } else if (!CommonUtils.EmailValidation(mEmail)) {
            registerName.setError(getString(R.string.error_invalid_email));
            focusView = registerName;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.EMAIL);
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            validateEmailAddressTask();
        }
    }

    @Override
    public void validateEmailAddressTask() {
        if (!NetworkUtil.isConnected(getActivity())) {
            alertbox(getString(R.string.alert_check_your_internet_connection));
            return;
        }
        enableDisableAllFieldsForEmailValidationForm(false);
//        IsSaving = true;

        //[START] Validate email using server
//        FacadeRegister.ParamEmailValidation param = new FacadeRegister.ParamEmailValidation();
//        param.action = VALIDATE_EMAIL;
//        param.email = mEmail;
//        facadeRegister.validateEmailUsingDefaultTokopedia(param, OnEmailValidateTokopediaDefaultConnection());
        //[START] Validate email using server

        //[START] Validate using server v4
        presenter.validateEmail(getActivity(), registerName.getText().toString(), registerPassword.getText().toString());
        //[END] Validate using server v4
    }

    @OnClick(R2.id.register_next)
    public void registerNext(){
        attemptRegisterStep1();
    }

    @Override
    public void enableDisableAllFieldsForEmailValidationForm(boolean isEnable) {
        if (isEnable) {
            registerNextProgress.setVisibility(View.GONE);
            registerNextButton.setText(getString(R.string.title_next));
            registerName.setEnabled(true);
            registerPassword.setEnabled(true);
            registerNext.setEnabled(true);
            checkboxPrivacyPolicy.setEnabled(true);
//            registerFacebookLogin.setEnabled(true);
//            registerGPlusLogin.setEnabled(true);
        } else {
            registerNextProgress.setVisibility(View.VISIBLE);
            registerNextButton.setText(getString(R.string.processing));
            registerName.setEnabled(false);
            registerPassword.setEnabled(false);
            registerNext.setEnabled(false);
            checkboxPrivacyPolicy.setEnabled(false);
//            registerFacebookLogin.setEnabled(false);
//            registerGPlusLogin.setEnabled(false);
        }
    }

    @Override
    public void initView() {
        final Typeface typeface = registerPassword.getTypeface();
        registerName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!registerName.isPopupShowing()) {
                    registerName.showDropDown();
                }
                return false;
            }
        });
//        if(mEmail != null && mEmail.length() > 0) {
//            registerName.setText(mEmail);
//        } else {
            setupEmailAddressToEmailTextView();
//        }
        checkboxPrivacyPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int cursorPosition = registerPassword.getSelectionStart();
                if (isChecked) {
                    registerPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    registerPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                registerPassword.setTypeface(typeface);
                registerPassword.setSelection(cursorPosition);
            }
        });
        registerName.addTextChangedListener((TextWatcher) presenter);

        registerPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.register_button || id == EditorInfo.IME_NULL) {
                    registerNext();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribeFacade();
        ButterKnife.unbind(this);
        dismissSnackbar();
    }

    @Override
    protected void initPresenter() {
        presenter = new RegisterNewImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_reborn;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDatas();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveDatas();
        super.onSaveInstanceState(outState);
    }

    private void saveDatas() {
        if(registerName!=null){
            presenter.saveData(RegisterNewImpl.convertToMap(RegisterNew.EMAIL, registerName.getText().toString()));
        }
        if(registerPassword!=null){
            presenter.saveData(RegisterNewImpl.convertToMap(RegisterNew.PASSWORD, registerPassword.getText().toString()));
        }
        if(checkboxPrivacyPolicy!=null){
            presenter.saveData(RegisterNewImpl.convertToMap(RegisterNew.IS_CHECKED, checkboxPrivacyPolicy.isChecked()));
        }
    }

    @Override
    public List<String> getEmailListOfAccountsUserHasLoggedInto() {
        Set<String> listOfAddresses = new LinkedHashSet<>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        if (accounts != null) {
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    listOfAddresses.add(account.name);
                }
            }
        }
        return new ArrayList<>(listOfAddresses);
    }

    @Override
    public boolean isEmailAddressFromDevice() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        boolean result = false;
        if (list.size() > 0) {
            A : for(String email:list) {
                if (email.equals(registerName.getText().toString())) {
                    result = true;
                    break A;
                }
            }
        }
        return result;
    }

    @Override
    public void alertbox(String mymessage)
    {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(mymessage)
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void setupEmailAddressToEmailTextView() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        if (list.size() > 0) {
            String mEmail = list.get(0);
            registerName.setText(mEmail);
            registerName.setSelection(mEmail.length());
        }
        registerName.setThreshold(0);
        AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        registerName.setAdapter(adapter);
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.REGISTER;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void setData(int type, Bundle data) {
        if (presenter != null)
            presenter.setData(getActivity(),type, data);
//        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String) data[0];
        showDialog(text);
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        showProgress(false);
        //[START] move to activation resent
        if (text.contains("belum diaktivasi")) {
            if (mContext != null && mContext instanceof SessionView)
                ((SessionView) mContext).moveToActivationResend(registerName.getText().toString());
        }
        switch (type){
            case DownloadService.DISCOVER_LOGIN:
                removeProgressBar();
                snackbar = SnackbarManager.make(getActivity(),"Gagal mendownload provider"
                        ,Snackbar.LENGTH_INDEFINITE).setAction("Coba lagi", retryDiscover());
                snackbar.show();
                break;
            default:
                ((SessionView)mContext).showError(text);
                break;
        }
        //[END] move to activation resent
        registerPassword.setText("");
    }

    private View.OnClickListener retryDiscover() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getProvider(getActivity());
            }
        };
    }

    public void showDialog(String dialogText) {
        snackbar = SnackbarManager.make(getActivity(), dialogText, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
    * This class shows auto complete textview hints and handles selection plus dismiss events of drop down.
    */
    class AutoCompleteTextViewAdapter extends ArrayAdapter<String> {

        public AutoCompleteTextViewAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    isSelectedFromAutoCompleteTextView = true;
                    registerName.setText(textView.getText().toString());
                    registerName.setSelection(textView.getText().toString().length());
                    registerName.dismissDropDown();
                }
            });
            return view;
        }
    }

    @Override
    public void showProvider(List<LoginProviderModel.ProvidersBean> data) {
        listProvider=data;
        if(listProvider!=null && checkHasNoProvider()){
            presenter.saveProvider(listProvider);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0, 20, 0, 0);

            for (int i = 0; i < listProvider.size(); i++) {
                LoginTextView tv = new LoginTextView(getActivity());
                String color = listProvider.get(i).getColor();
                if(listProvider.get(i).getId().equalsIgnoreCase("facebook")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onFacebookLoginClick();
                        }
                    });
                }else if(listProvider.get(i).getId().equalsIgnoreCase("gplus")){
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RegisterNewViewFragmentPermissionsDispatcher.onRegisterWithGoogleWithCheck(RegisterNewViewFragment.this);
                        }
                    });
                }else{
                    tv.setOnClickListener(loginProvideOnClick(i));
                }
                generateLayout(tv, Color.parseColor(color), i);
                if(linearLayout!=null) {
                    linearLayout.addView(tv, linearLayout.getChildCount(), layoutParams);
                }
            }
        }
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void onRegisterWithGoogle() {
        ((GoogleActivity) getActivity()).onSignInClicked();
        presenter.storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                AppEventTracking.GTMCacheValue.GMAIL);
    }

    @Override
    public void addProgressBar() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = linearLayout.getChildCount()-1;
        if (!(linearLayout.getChildAt(lastPos) instanceof ProgressBar))
            linearLayout.addView(pb, linearLayout.getChildCount());
    }

    @Override
    public void removeProgressBar() {
        int lastPos = linearLayout.getChildCount()-1;
        if (linearLayout.getChildAt(lastPos) instanceof ProgressBar)
            linearLayout.removeViewAt(linearLayout.getChildCount()-1);
    }

    private void generateLayout(LoginTextView tv, int color, int position) {
        //generate background
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{3, 3, 3, 3, 3, 3, 3, 3});
        shape.setColor(color);
        if(color == Color.WHITE) shape.setStroke(1, Color.BLACK);

        try {
            tv.setBackground(shape);
        }catch (NoSuchMethodError error){
            tv.setBackgroundDrawable(shape);
        }

        //generate other view
        TextView textView = (TextView) tv.findViewById(R.id.provider_name);
        textView.setTextColor(getInverseColor(color));
        textView.setText(String.format("Daftar Dengan %s", listProvider.get(position).getName()));
        ImageView imageView = (ImageView) tv.findViewById(R.id.provider_image);
        ImageHandler.loadImage2(imageView, listProvider.get(position).getImage()
                , R.drawable.ic_icon_toped_announce);

    }

    private int getInverseColor(int color){
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    public boolean checkHasNoProvider() {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if(linearLayout.getChildAt(i) instanceof LoginTextView){
                return false;
            }
        }
        return true;
    }

    @Override
    public void showError(String string) {
        SnackbarManager.make(getActivity(),string, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void finishActivity() {
        if(new SessionHandler(getActivity()).isV4Login()) {// go back to home
            getActivity().startActivity(new Intent(getActivity(), HomeRouter.getHomeActivityClass()).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            getActivity().finish();
        }
    }

    private View.OnClickListener loginProvideOnClick(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewLoginFragment newFragment = WebViewLoginFragment
                        .createInstance(listProvider.get(position).getUrl());
                newFragment.setTargetFragment(RegisterNewViewFragment.this, 100);
                newFragment.show(getFragmentManager().beginTransaction(), "dialog");
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                presenter.storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                        listProvider.get(position).getName());
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                Bundle bundle = data.getBundleExtra("bundle");
                if(bundle.getString("path").contains("error")){
                    snackbar = SnackbarManager.make(getActivity(), bundle.getString("message"), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (bundle.getString("path").contains("code")){
                    presenter.loginWebView(getActivity(), bundle);
                }else if (bundle.getString("path").contains("activation-social")){
                    ((SessionView) mContext).moveToActivationResend(registerName.getText().toString());
                }
                break;
            default:
                break;
        }
    }


    private void dismissSnackbar() {
        if(snackbar!=null) snackbar.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
        presenter.initCacheGTM(getActivity());
    }

    private void sendGTMRegisterError(String label){
        UnifyTracking.eventRegisterError(label);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RegisterNewViewFragmentPermissionsDispatcher.onRequestPermissionsResult(RegisterNewViewFragment.this, requestCode, grantResults);
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