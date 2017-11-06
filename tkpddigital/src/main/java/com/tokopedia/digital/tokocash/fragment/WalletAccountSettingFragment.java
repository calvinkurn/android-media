package com.tokopedia.digital.tokocash.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.adapter.LinkedAccountAdapter;
import com.tokopedia.digital.tokocash.domain.HistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.AccountSettingInteractor;
import com.tokopedia.digital.tokocash.listener.IWalletAccountSettingView;
import com.tokopedia.digital.tokocash.model.AccountTokoCash;
import com.tokopedia.digital.tokocash.model.OAuthInfo;
import com.tokopedia.digital.tokocash.network.apiservice.HistoryTokoCashService;
import com.tokopedia.digital.tokocash.presenter.IWalletAccountSettingPresenter;
import com.tokopedia.digital.tokocash.presenter.WalletAccountSettingPresenter;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingFragment extends BasePresenterFragment<IWalletAccountSettingPresenter>
        implements IWalletAccountSettingView, RefreshHandler.OnRefreshHandlerListener {

    @BindView(R2.id.main_container)
    NestedScrollView mainContainer;
    @BindView(R2.id.name_account)
    TextView nameAccount;
    @BindView(R2.id.email_account)
    TextView emailAccount;
    @BindView(R2.id.phone_account)
    TextView phoneAccount;
    @BindView(R2.id.rv_account_list)
    RecyclerView rvConnectedUser;
    @BindView(R2.id.root_view)
    CoordinatorLayout rootView;

    private TkpdProgressDialog progressDialogNormal;
    private RefreshHandler refreshHandler;
    private LinkedAccountAdapter accountListAdapter;
    private CompositeSubscription compositeSubscription;

    public static Fragment newInstance() {
        WalletAccountSettingFragment fragment = new WalletAccountSettingFragment();
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

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
        compositeSubscription = new CompositeSubscription();
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        AccountSettingInteractor accountSettingInteractor = new AccountSettingInteractor(
                new HistoryTokoCashRepository(new HistoryTokoCashService(sessionHandler.getAccessTokenTokoCash())),
                compositeSubscription,
                new JobExecutor(),
                new UIThread());
        presenter = new WalletAccountSettingPresenter(this, accountSettingInteractor);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_wallet_account_setting_digital_module;
    }

    @Override
    protected void initView(View view) {
        refreshHandler = new RefreshHandler(getActivity(), view, this);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        accountListAdapter = new LinkedAccountAdapter(new ArrayList<AccountTokoCash>());
        accountListAdapter.setActionListener(getActionListener());
        rvConnectedUser.setLayoutManager(linearLayoutManager);
        rvConnectedUser.setNestedScrollingEnabled(false);
        rvConnectedUser.setAdapter(accountListAdapter);
    }

    private LinkedAccountAdapter.ActionListener getActionListener() {
        return new LinkedAccountAdapter.ActionListener() {
            @Override
            public void onDeleteAccessClicked(AccountTokoCash accountTokoCash) {
                //TODO implement delete account from presenter
                Toast.makeText(getActivity(), "DELETE ACCOUNT", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void setActionVar() {
        refreshHandler.startRefresh();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {
        mainContainer.setVisibility(View.GONE);
    }

    @Override
    public void showProgressLoading() {
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialogNormal.dismiss();
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return null;
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void onRefresh(View view) {
        if (refreshHandler.isRefreshing()) {
            presenter.processGetWalletAccountData();
            presenter.processGetListLinkedAccount();
        }
    }

    @Override
    public void renderWalletOAuthInfoData(OAuthInfo oAuthInfo) {
        mainContainer.setVisibility(View.VISIBLE);
        refreshHandler.finishRefresh();
        nameAccount.setText(oAuthInfo.getName());
        emailAccount.setText(oAuthInfo.getEmail());
        phoneAccount.setText(oAuthInfo.getMsisdn());
    }

    @Override
    public void renderAccountTokoCashList(List<AccountTokoCash> accountTokoCashList) {
        accountListAdapter.addAccountList(accountTokoCashList);
    }

    @Override
    public void renderErrorGetWalletAccountSettingData(String message) {
        renderEmptyPage(message);
    }

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }

    private void renderEmptyPage(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), rootView, message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                }
        );
    }
}