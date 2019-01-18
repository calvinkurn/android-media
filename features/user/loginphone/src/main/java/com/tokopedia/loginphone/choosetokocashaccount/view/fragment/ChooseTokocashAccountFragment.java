package com.tokopedia.loginphone.choosetokocashaccount.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.text.TextDrawable;
import com.tokopedia.loginphone.R;
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel;
import com.tokopedia.loginphone.choosetokocashaccount.di.DaggerChooseAccountComponent;
import com.tokopedia.loginphone.choosetokocashaccount.view.adapter.TokocashAccountAdapter;
import com.tokopedia.loginphone.choosetokocashaccount.view.listener.ChooseTokocashAccountContract;
import com.tokopedia.loginphone.choosetokocashaccount.view.presenter.ChooseTokocashAccountPresenter;
import com.tokopedia.loginphone.common.LoginPhoneNumberRouter;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.sessioncommon.data.loginphone.UserDetail;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.SecurityPojo;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountFragment extends BaseDaggerFragment implements
        ChooseTokocashAccountContract.View, ChooseTokocashAccountContract.ViewAdapter {
    private static final int MENU_ID_LOGOUT = 111;
    private int REQUEST_SECURITY_QUESTION = 101;

    TextView message;
    RecyclerView listAccount;
    View mainView;
    ProgressBar progressBar;
    TokocashAccountAdapter adapter;

    ChooseTokoCashAccountViewModel viewModel;

    @Inject
    ChooseTokocashAccountPresenter presenter;

    @Inject
    LoginPhoneNumberAnalytics analytics;

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    UserSessionInterface userSessionInterface;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChooseTokocashAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {

            BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication())
                    .getBaseAppComponent();

            LoginRegisterPhoneComponent loginRegisterPhoneComponent =
                    DaggerLoginRegisterPhoneComponent.builder()
                            .baseAppComponent(appComponent).build();

            DaggerChooseAccountComponent daggerChooseAccountComponent = (DaggerChooseAccountComponent)
                    DaggerChooseAccountComponent.builder()
                            .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
                            .build();

            daggerChooseAccountComponent.inject(this);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(ChooseTokoCashAccountViewModel.ARGS_DATA);
        } else if (getArguments() != null) {
            viewModel = getArguments().getParcelable(ChooseTokoCashAccountViewModel.ARGS_DATA);
        } else if (getActivity() != null) {
            getActivity().finish();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_login_phone_account, parent, false);
        setHasOptionsMenu(true);
        message = view.findViewById(R.id.message);
        listAccount = view.findViewById(R.id.list_account);
        mainView = view.findViewById(R.id.main_view);
        progressBar = view.findViewById(R.id.progress_bar);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null) {
            menu.add(Menu.NONE, MENU_ID_LOGOUT, 0, "");
            MenuItem menuItem = menu.findItem(MENU_ID_LOGOUT);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setIcon(getDraw(getActivity()));
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private Drawable getDraw(Context context) {
        TextDrawable drawable = new TextDrawable(context);
        drawable.setText(getResources().getString(R.string.action_logout));
        drawable.setTextColor(R.color.black_70);
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == MENU_ID_LOGOUT) {
            goToLoginPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLoginPage() {
        if (getActivity() != null) {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    private void prepareView() {
        adapter = TokocashAccountAdapter.createInstance(this, viewModel.getListAccount());
        listAccount.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        listAccount.setAdapter(adapter);
    }


    @Override
    public void onSelectedTokocashAccount(UserDetail accountTokocash) {
        presenter.loginWithTokocash(viewModel.getKey(),
                accountTokocash);
    }

    @Override
    public void onSuccessLogin(String userId) {
        if (getActivity() != null) {
            analytics.eventSuccessLoginPhoneNumber();
            ((LoginPhoneNumberRouter) getActivity().getApplicationContext()).setTrackingUserId(userId,
                    getActivity().getApplicationContext());
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void showLoadingProgress() {
        mainView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingProgress() {
        mainView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public LoginSuccessRouter getLoginRouter() {
        return new LoginSuccessRouter() {
            @Override
            public void onGoToActivationPage(String email) {
                // Not implemented in login phone number
            }

            @Override
            public void onForbidden() {
                ForbiddenActivity.startActivity(getActivity());
            }

            @Override
            public void onErrorLogin(String errorMessage) {
                dismissLoadingProgress();
                NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
            }

            @Override
            public void onGoToCreatePasswordPage(GetUserInfoData info) {
                // Not implemented in login phone number
            }

            @Override
            public void onGoToPhoneVerification() {
                // Not implemented in login phone number
            }

            @Override
            public void onGoToSecurityQuestion(SecurityPojo securityPojo, String fullName, String email, String phone) {
                if (getActivity() != null) {
                    Intent intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                            getActivity(),
                            RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                            email,
                            phone);
                    intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivityForResult(intent, REQUEST_SECURITY_QUESTION);
                    getActivity().finish();
                }
            }

            @Override
            public void logUnknownError(Throwable message) {
                try {
                    Crashlytics.logException(message);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        message.setText(getPromptText());
    }

    private Spanned getPromptText() {
        return MethodChecker.fromHtml(getString(R.string.prompt_choose_tokocash_account,
                viewModel.getListAccount().size(),
                viewModel.getPhoneNumber()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            onSuccessLogin(userSessionInterface.getTemporaryUserId());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ChooseTokoCashAccountViewModel.ARGS_DATA, viewModel);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
