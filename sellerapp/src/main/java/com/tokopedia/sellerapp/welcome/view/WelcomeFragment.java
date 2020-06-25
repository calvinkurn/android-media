package com.tokopedia.sellerapp.welcome.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.welcome.presenter.WelcomeFragmentPresenter;
import com.tokopedia.sellerapp.welcome.presenter.WelcomeFragmentPresenterImpl;
import com.tokopedia.sellerapp.welcome.widget.LoginTextView;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;
import static com.tokopedia.sellerapp.welcome.presenter.WelcomeFragmentPresenterImpl.DISCOVER_LOGIN;

/**
 * Created by stevenfredian on 10/5/16.
 */

public class WelcomeFragment extends BaseDaggerFragment implements
        WelcomeFragmentView {

    private static final int REQUEST_LOGIN = 101;
    private static final int REQUEST_REGISTER = 102;

    ImageView background;
    LoginTextView login;
    TextView register;
    LinearLayout linearLayout;
    ProgressBar progressBar;
    View titleView;
    LinearLayout containerProvider;
    View splash;

    private View decorView;
    Snackbar snackbar;
    WelcomeFragmentPresenter presenter;

    private UserSessionInterface userSession;
    LocalCacheHandler isNotFirstRun;
    Spannable spannable;

    List<LoginProviderModel.ProvidersBean> listProvider;
    private String backgroundUrl;
    String sourceString = "Belum punya akun? " + "Daftar";
    private ClickableSpan clickableSpan;

    public static WelcomeFragment createInstance(Bundle bundle) {
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

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
                , 0);

        register.setText(spannable, TextView.BufferType.SPANNABLE);
        register.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container,false);

        background = view.findViewById(R.id.background);
        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);
        linearLayout = view.findViewById(R.id.parent);
        progressBar = view.findViewById(R.id.progress_login);
        titleView = view.findViewById(R.id.title_view);
        containerProvider = view.findViewById(R.id.container_provider);
        splash = view.findViewById(R.id.splash);

        presenter = new WelcomeFragmentPresenterImpl(this);
        prepareView();
        return view;
    }

    private void prepareView() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
                    Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
                    startActivityForResult(intent, REQUEST_LOGIN);
                }
            }
        });
        isNotFirstRun = new LocalCacheHandler(getActivity(), "FirstRun");

        if (isNotFirstRun.getBoolean("firstRun").equals(false)) {
            isNotFirstRun.putBoolean("firstRun", true);
            isNotFirstRun.applyEditor();
            showPopUp();
        }

        spannable = new SpannableString(sourceString);

        clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.CREATE_SHOP);
                startActivityForResult(intent, REQUEST_REGISTER);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.tkpd_main_green));
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserAuthenticationAnalytics.setActiveLogin(getActivity());
        showProgress(false);
        presenter.initialize(getActivity());
        presenter.initData();
    }


    private void showPopUp() {
        InfoWelcomeDialogFragment fragment = InfoWelcomeDialogFragment.newInstance();
        fragment.show(getActivity().getFragmentManager(), "INFO_WELCOME");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();
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
                if (color == null) {
                    colorInt = Color.parseColor("#FFFFFF");
                } else {
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
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onGoogleClick();
                        }
                    });
                } else {
                    tv.setOnClickListener(loginProvideOnClick(i));
                }
                tv.setBorderColor(Color.BLACK);

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
                if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
                    Intent intent = LoginActivity.DeepLinkIntents.getAutoLoginWebview(getActivity(), listProvider.get(position)
                                    .getName(), listProvider.get(position).getUrl());
                    startActivityForResult(intent, REQUEST_LOGIN);
                }
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                UserAuthenticationAnalytics.setActiveAuthenticationMedium(getActivity(), listProvider.get(position).getName());
            }
        };
    }

    public void onGoogleClick() {
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intent = LoginActivity.DeepLinkIntents.getAutoLoginGoogle(getActivity());
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    private void onFacebookClick() {
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intent = LoginActivity.DeepLinkIntents.getAutoLoginFacebook(getActivity());
            startActivityForResult(intent, REQUEST_LOGIN);
        }
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
            case DISCOVER_LOGIN:
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
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.GONE);
        } else {
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
        if (backgroundURL != null) {
            ImageHandler.loadImageWithoutPlaceholder(background, backgroundURL, R.drawable.background);
            background.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getUserSession().isLoggedIn()) {
            onSuccessLogin();
        }
    }

    private void onSuccessLogin() {
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intent;
            if (getUserSession().hasShop() && getActivity() != null) {
                intent = ((TkpdCoreRouter) getActivity().getApplication()).getHomeIntent(getActivity());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                        .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent = RouteManager.getIntent(getContext(), OPEN_SHOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
        }
    }

    private UserSessionInterface getUserSession() {
        if (userSession == null) {
            userSession = new UserSession(getActivity());
        }
        return userSession;
    }
}
