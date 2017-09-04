package com.tokopedia.digital.wallets.accountsetting;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingFragment extends BasePresenterFragment<IWalletAccountSettingPresenter>
        implements IWalletAccountSettingView, RefreshHandler.OnRefreshHandlerListener, ConnectedWalletAccountListAdapter.ActionListener {

    public static final String ARG_EXTRA_WALLET_ACCOUNT_SETTING_DATA =
            "ARG_EXTRA_WALLET_ACCOUNT_SETTING_DATA";
    public static final String EXTRA_STATE_WALLET_ACCOUNT_SETTING_DATA =
            "EXTRA_STATE_WALLET_ACCOUNT_SETTING_DATA";
    public static final String EXTRA_STATE_WALLET_ACCOUNT_SETTING_PASS_DATA =
            "EXTRA_STATE_WALLET_ACCOUNT_SETTING_PASS_DATA";

    @BindView(R2.id.main_container)
    NestedScrollView mainContainer;
    @BindView(R2.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R2.id.til_et_name)
    TkpdTextInputLayout tilEtName;
    @BindView(R2.id.til_et_email)
    TkpdTextInputLayout tilEtEmail;
    @BindView(R2.id.til_et_phone)
    TkpdTextInputLayout tilEtPhone;
    @BindView(R2.id.et_name)
    TextInputEditText etName;
    @BindView(R2.id.et_email)
    TextInputEditText etEmail;
    @BindView(R2.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R2.id.rv_account_list)
    RecyclerView rvConnectedUser;

    public WalletAccountSettingPassData stateWalletAccountSettingPassData; //pass data dari intrnt
    public WalletAccountSettingData stateWalletAccountSettingData; //data UI dari API

    private TkpdProgressDialog progressDialogNormal;
    private RefreshHandler refreshHandler;
    private ConnectedWalletAccountListAdapter accountListAdapter;

    public static Fragment newInstance(WalletAccountSettingPassData passData) {
        WalletAccountSettingFragment fragment = new WalletAccountSettingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_WALLET_ACCOUNT_SETTING_DATA, passData);
        fragment.setArguments(bundle);
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
        state.putParcelable(
                EXTRA_STATE_WALLET_ACCOUNT_SETTING_PASS_DATA, stateWalletAccountSettingPassData
        );
        state.putParcelable(
                EXTRA_STATE_WALLET_ACCOUNT_SETTING_DATA, stateWalletAccountSettingData
        );
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        stateWalletAccountSettingPassData = savedState.getParcelable(
                EXTRA_STATE_WALLET_ACCOUNT_SETTING_PASS_DATA
        );
        stateWalletAccountSettingData = savedState.getParcelable(
                EXTRA_STATE_WALLET_ACCOUNT_SETTING_DATA
        );
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new WalletAccountSettingPresenter(this, new Object());
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        stateWalletAccountSettingPassData = arguments.getParcelable(ARG_EXTRA_WALLET_ACCOUNT_SETTING_DATA);
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
        accountListAdapter = new ConnectedWalletAccountListAdapter(this, this);
        rvConnectedUser.setLayoutManager(linearLayoutManager);
        rvConnectedUser.setAdapter(accountListAdapter);
    }

    @Override
    protected void setActionVar() {
        if (stateWalletAccountSettingData == null) refreshHandler.startRefresh();
        else renderWalletAccountSettingData(stateWalletAccountSettingData);
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
        accountListAdapter.clearAllDataList();
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
        if (refreshHandler.isRefreshing()) presenter.processGetWalletAccountData();
    }

    @Override
    public void renderWalletAccountSettingData(WalletAccountSettingData walletAccountSettingData) {
        this.stateWalletAccountSettingData = walletAccountSettingData;
        refreshHandler.finishRefresh();

        mainContainer.setVisibility(View.VISIBLE);
        tvSubTitle.setText(walletAccountSettingData.getSubTitle());
        etName.setText(walletAccountSettingData.getName());
        etEmail.setText(walletAccountSettingData.getEmail());
        etPhone.setText(walletAccountSettingData.getPhoneNumber());
        accountListAdapter.addAllDataList(
                walletAccountSettingData.getTitleListConnectedUser(),
                walletAccountSettingData.getConnectedUserDataList()
        );

    }

    @Override
    public void renderErrorGetWalletAccountSettingData(String message) {
        renderEmptyPage(message);
    }


    @Override
    public void renderErrorHttpGetWalletAccountSettingData(String message) {
        renderEmptyPage(message);
    }

    @Override
    public void renderErrorNoConnectionGetWalletAccountSettingData(String message) {
        renderEmptyPage(message);
    }

    @Override
    public void renderErrorTimeoutConnectionGetWalletAccountSettingData(String message) {
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
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                }
        );
    }

    @Override
    public void onConnectedWalletAccountDeleteAccessClicked(WalletAccountSettingConnectedUserData data) {

    }
}
