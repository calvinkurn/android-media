package com.tokopedia.core.welcome.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.welcome.presenter.WelcomeFragmentPresenter;
import com.tokopedia.core.welcome.presenter.WelcomeFragmentPresenterImpl;

import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by stevenfredian on 10/5/16.
 */

@RuntimePermissions
public class WelcomeFragment extends BasePresenterFragment<WelcomeFragmentPresenter> implements WelcomeFragmentView{

    @BindView(R2.id.background)
    ImageView background;
    @BindView(R2.id.login)
    LoginTextView login;
    @BindView(R2.id.register)
    TextView register;
    @BindView(R2.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R2.id.progress_login)
    ProgressBar progressBar;
    @BindView(R2.id.title_view)
    View titleView;
    @BindView(R2.id.container_provider)
    LinearLayout containerProvider;
    @BindView(R2.id.splash)
    View splash;

    private View decorView;
    Snackbar snackbar;

    LocalCacheHandler isNotFirstRun;
    Spannable spannable;

    List<LoginProviderModel.ProvidersBean> listProvider;
    private String backgroundUrl;
    String sourceString = "Belum punya akun? "+ "Daftar";
    private ClickableSpan clickableSpan;

    public static WelcomeFragment createInstance(Bundle bundle) {
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        showProgress(false);
        presenter.initialize(getActivity());
        presenter.initData();
    }

    @Override
    public void onResume() {
        super.onResume();

        decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        spannable.setSpan(clickableSpan
                , sourceString.indexOf("Daftar")
                , sourceString.length()
                ,0);

        register.setText(spannable, TextView.BufferType.SPANNABLE);
        register.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new WelcomeFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_welcome;
    }

    @Override
    protected void initView(View view) {

        login.setImageNextToText();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OldSessionRouter.getLoginActivityIntent(context);
                intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.SELLER_HOME);
                startActivity(intent);
            }
        });
        isNotFirstRun = new LocalCacheHandler(getActivity(), "FirstRun");

        if(isNotFirstRun.getBoolean("firstRun").equals(false)){
            isNotFirstRun.putBoolean("firstRun", true);
            isNotFirstRun.applyEditor();
            showPopUp();
        }

        spannable = new SpannableString(sourceString);

        clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ((TkpdCoreRouter)getActivity().getApplication()).goToRegister(getActivity());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.tkpd_main_green));
            }
        };
    }

    private void showPopUp() {
        InfoWelcomeDialogFragment fragment = InfoWelcomeDialogFragment.newInstance();
        fragment.show(getActivity().getFragmentManager(), "INFO_WELCOME");
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        UserAuthenticationAnalytics.setActiveLogin();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();
    }

    @Override
    public void addProgressbar() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = containerProvider.getChildCount() - 1;
        if (containerProvider != null && !(containerProvider.getChildAt(lastPos) instanceof ProgressBar))
            containerProvider.addView(pb, containerProvider.getChildCount());
    }

    @Override
    public void removeProgressBar() {
        int lastPos = containerProvider.getChildCount() - 1;
        if (containerProvider != null && containerProvider.getChildAt(lastPos) instanceof ProgressBar)
            containerProvider.removeViewAt(containerProvider.getChildCount() - 1);
    }

    @Override
    public void showProvider(List<LoginProviderModel.ProvidersBean> data) {
        listProvider = data;
        if (listProvider != null && checkHasNoProvider()) {
            presenter.saveProvider(listProvider);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

            for (int i = 0; i < listProvider.size(); i++) {
                String color = listProvider.get(i).getColor();
                int colorInt;
                if(color==null) {
                    colorInt = Color.parseColor("#FFFFFF");
                }else{
                    colorInt = Color.parseColor(color);
                }
                layoutParams.setMargins(9, 0, 9, 0);


                LoginTextView tv = new LoginTextView(getActivity());
                tv.setColor(colorInt);
                tv.setImage(listProvider.get(i).getImage());
                tv.setTextVisibility(View.GONE);
                tv.setRoundCorner(7);
                if (listProvider.get(i).getId().equalsIgnoreCase("facebook")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onFacebookClick();
                        }
                    });
                } else if (listProvider.get(i).getId().equalsIgnoreCase("gplus")) {
                    tv.setBorderColor(Color.BLACK);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WelcomeFragmentPermissionsDispatcher.onGoogleClickWithCheck(WelcomeFragment.this);
                        }
                    });
                } else {
                    tv.setOnClickListener(loginProvideOnClick(i));
                }

                if (containerProvider != null) {
                    containerProvider.addView(tv, i, layoutParams);
                }
            }
        }
    }

    private View.OnClickListener loginProvideOnClick(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginWebview(getActivity(), listProvider.get(position).getUrl(),
                        listProvider.get(position).getName());
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                UserAuthenticationAnalytics.setActiveAuthenticationMedium(listProvider.get(position).getName());
            }
        };
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void onGoogleClick() {
        presenter.loginGoogle(getActivity());
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.GMAIL);
    }

    private void onFacebookClick() {
        presenter.loginFacebook(getActivity());
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.FACEBOOK);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        showProgress(false);
        //[START] move to activation resent
        if (text.contains("belum diaktivasi")) {
//            if (mContext != null && mContext instanceof SessionView) {
//                ((SessionView) mContext).moveToActivationResend(mEmailView.getText().toString());
//            }
        }
        switch (type) {
            case DownloadService.DISCOVER_LOGIN:
                showProgress(false);
                snackbar = SnackbarManager.make(getActivity(), "Gagal mendownload provider", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Coba lagi", retryDiscover());
                snackbar.show();
                break;
            default:
                snackbar = SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG);
                snackbar.show();
                presenter.initData();
                break;
        }
    }

    @Override
    public void showError(String string) {
        SnackbarManager.make(getActivity(), string, Snackbar.LENGTH_LONG).show();
    }

    public boolean checkHasNoProvider() {
        for (int i = 0; i < containerProvider.getChildCount(); i++) {
            if (containerProvider.getChildAt(i) instanceof LoginTextView) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void showProgress(final boolean isShow) {
        if(isShow){
            progressBar.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            titleView.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener retryDiscover() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.initData();
            }
        };
    }

    @Override
    public void setBackground(String backgroundURL) {
        if(backgroundURL != null) {
            ImageHandler.loadImageWithoutPlaceholder(background, backgroundURL, R.drawable.background);
            background.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            ImageHandler.loadImageWithIdWithoutPlaceholder(background, R.color.white);
            background.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void hideSplash() {
        splash.setVisibility(View.GONE);
    }

    @Override
    public void showSplash() {
        splash.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WelcomeFragmentPermissionsDispatcher.onRequestPermissionsResult(WelcomeFragment.this,requestCode, grantResults);
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
