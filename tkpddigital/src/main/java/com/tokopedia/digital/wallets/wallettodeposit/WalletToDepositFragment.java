package com.tokopedia.digital.wallets.wallettodeposit;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositFragment extends BasePresenterFragment<IWalletToDepositPresenter>
        implements IWalletToDepositView {
    public static final String ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA =
            "ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA";
    public static final String STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA =
            "STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA";
    public static final String STATE_EXTRA_WALLET_TO_DEPOSIT_DATA =
            "STATE_EXTRA_WALLET_TO_DEPOSIT_DATA";

    @BindView(R2.id.main_container)
    RelativeLayout mainContainer;
    @BindView(R2.id.pb_main_loading)
    ProgressBar mainProgressBar;
    @BindView(R2.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R2.id.tv_nominal)
    TextView tvNominal;
    @BindView(R2.id.btn_negative)
    TextView btnCancel;
    @BindView(R2.id.btn_positive)
    TextView btnProcess;

    private ActionListener actionListener;
    private TkpdProgressDialog progressDialogNormal;
    private WalletToDepositData stateWalletToDepositData;
    private WalletToDepositPassData statePassData;


    public static WalletToDepositFragment newInstance(WalletToDepositPassData passData) {
        WalletToDepositFragment fragment = new WalletToDepositFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.getSomethings();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(STATE_EXTRA_WALLET_TO_DEPOSIT_DATA, stateWalletToDepositData);
        state.putParcelable(STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA, statePassData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        WalletToDepositPassData walletToDepositPassData = savedState.getParcelable(
                STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA
        );
        if (walletToDepositPassData != null) this.statePassData = walletToDepositPassData;
        WalletToDepositData walletToDepositData = savedState.getParcelable(
                STATE_EXTRA_WALLET_TO_DEPOSIT_DATA
        );
        if (walletToDepositData != null) this.stateWalletToDepositData = walletToDepositData;

        if (stateWalletToDepositData != null) renderSomethingFromGet(stateWalletToDepositData);
        else if (statePassData != null) presenter.getSomethings();
        else actionListener.onWalletToDepositFailed();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new WalletToDepositPresenter(this, new Object()); //TODO initial interactor
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.statePassData = arguments.getParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_wallet_to_deposit_digital_module;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {

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
        mainContainer.setVisibility(View.GONE);
        mainProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInitialProgressLoading() {
        mainContainer.setVisibility(View.VISIBLE);
        mainProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void clearContentRendered() {
        mainContainer.setVisibility(View.GONE);
        mainProgressBar.setVisibility(View.GONE);
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

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
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
    public void renderSomethingFromGet(WalletToDepositData data) {
        this.stateWalletToDepositData = data;
        tvSubtitle.setText(stateWalletToDepositData.getSubTitle());
        tvNominal.setText(stateWalletToDepositData.getNominalFormatted());
        btnCancel.setText(stateWalletToDepositData.getTitleButtonNegative());
        btnProcess.setText(stateWalletToDepositData.getTitleButtonPositive());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeView();
            }
        });
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processSomethings();
            }
        });
        actionListener.setTitlePage(stateWalletToDepositData.getTitle());
    }

    @Override
    public void renderSomethingFromProcess(WalletToDepositThanksData data) {
        navigateToActivityRequest(
                WalletToDepositThanksActivity.newInstance(getActivity(),
                        new WalletToDepositThanksPassData.Builder()
                                .walletToDepositThanksData(data)
                                .build()
                ), WalletToDepositThanksActivity.REQUEST_CODE
        );
    }

    @Override
    public String getTitlePageFromPassData() {
        return statePassData.getTitle();
    }

    @Override
    public int getWalletAmountFromPassData() {
        return statePassData.getAmount();
    }

    @Override
    public String getWalletAmountFormattedFromPassData() {
        return statePassData.getAmountFormatted();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WalletToDepositThanksActivity.REQUEST_CODE) {
            switch (resultCode) {
                case WalletToDepositThanksActivity.RESULT_BACK_RETRY:
                    presenter.processSomethings();
                    break;
                case WalletToDepositThanksActivity.RESULT_BACK_FAILED:
                    actionListener.onWalletToDepositFailed();
                    break;
                case WalletToDepositThanksActivity.RESULT_BACK_SUCCESS:
                    actionListener.onWalletToDepositSucceeded();
                    break;
            }
        }
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void onWalletToDepositSucceeded();

        void onWalletToDepositFailed();
    }
}
