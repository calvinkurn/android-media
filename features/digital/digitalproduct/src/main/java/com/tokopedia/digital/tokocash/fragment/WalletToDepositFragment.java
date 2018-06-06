package com.tokopedia.digital.tokocash.fragment;

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
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.domain.HistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.TokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.model.WalletToDepositPassData;
import com.tokopedia.core.network.apiservices.tokocash.WalletService;
import com.tokopedia.digital.tokocash.presenter.IWalletToDepositPresenter;
import com.tokopedia.digital.tokocash.presenter.WalletToDepositPresenter;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;
import com.tokopedia.digital.tokocash.listener.IWalletToDepositView;
import com.tokopedia.digital.tokocash.model.WalletToDepositData;
import com.tokopedia.digital.tokocash.activity.WalletToDepositThanksActivity;
import com.tokopedia.digital.tokocash.model.WalletToDepositThanksData;
import com.tokopedia.digital.tokocash.model.WalletToDepositThanksPassData;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

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
    private CompositeSubscription compositeSubscription;

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
        showInitialProgressLoading();
        renderInitializePage(
                new WalletToDepositData.Builder()
                        .subTitle(getString(R.string.tokocash_title_confirmation_move_saldo))
                        .title(getTitlePageFromPassData())
                        .nominalFormatted(getWalletAmountFormattedFromPassData())
                        .titleButtonNegative(getString(R.string.button_label_cancel))
                        .titleButtonPositive(getString(R.string.tokocash_button_move_saldo))
                        .build());
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

        if (stateWalletToDepositData != null) renderInitializePage(stateWalletToDepositData);
        else actionListener.onWalletToDepositFailed();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        compositeSubscription = new CompositeSubscription();
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        WalletService walletService = new WalletService(sessionHandler.getAccessTokenTokoCash());
        HistoryTokoCashRepository historyTokoCashRepository = new HistoryTokoCashRepository(walletService);
        TokoCashHistoryInteractor tokoCashHistoryInteractor =
                new TokoCashHistoryInteractor(
                        historyTokoCashRepository,
                        compositeSubscription,
                        new JobExecutor(),
                        new UIThread());
        presenter = new WalletToDepositPresenter(this, tokoCashHistoryInteractor);
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
        if (progressDialogNormal.isProgress())
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

    private void renderInitializePage(final WalletToDepositData walletToDepositData) {
        hideInitialProgressLoading();
        this.stateWalletToDepositData = walletToDepositData;
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
                presenter.processMoveToSaldo(statePassData.getUrl(), statePassData.getParams());
            }
        });
        actionListener.setTitlePage(stateWalletToDepositData.getTitle());
    }

    public void renderThankYouPage(WalletToDepositThanksData data) {
        navigateToActivityRequest(
                WalletToDepositThanksActivity.newInstance(getActivity(),
                        new WalletToDepositThanksPassData.Builder()
                                .walletToDepositThanksData(data)
                                .build()
                ), WalletToDepositThanksActivity.REQUEST_CODE
        );
    }

    @Override
    public void wrappingDataSuccess(long amount) {
        String amountFormatted = CurrencyFormatHelper.ConvertToRupiah(String.valueOf(amount));
        WalletToDepositThanksData successWalletDepositThanksPage =
                new WalletToDepositThanksData.Builder()
                        .title(getString(R.string.tokocash_success_title_move_saldo))
                        .subTitle(getString(R.string.tokocash_success_subtitle_move_saldo))
                        .description(String.format(getString(R.string.tokocash_success_amount),
                                amountFormatted.replace(",", ".")))
                        .titleButtonPositive(getString(R.string.tokocash_move_page))
                        .typeResult(WalletToDepositThanksData.TypeResult.SUCCESS)
                        .iconResId(R.drawable.ic_wallet_to_deposit_thanks_success)
                        .build();
        renderThankYouPage(successWalletDepositThanksPage);
    }

    @Override
    public void wrappingDataFailed() {
        WalletToDepositThanksData failedWalletDepositThanksPage =
                new WalletToDepositThanksData.Builder()
                        .title(getString(R.string.tokocash_failed_title_move_saldo))
                        .subTitle(getString(R.string.tokocash_failed_subtitle_move_saldo))
                        .titleButtonNegative(getString(R.string.tokocash_move_page))
                        .titleButtonPositive(getString(R.string.tokocash_try_again))
                        .typeResult(WalletToDepositThanksData.TypeResult.FAILED)
                        .iconResId(R.drawable.ic_wallet_to_deposit_thanks_success)
                        .build();
        renderThankYouPage(failedWalletDepositThanksPage);
    }

    private String getTitlePageFromPassData() {
        return statePassData.getTitle();
    }

    private String getWalletAmountFormattedFromPassData() {
        return statePassData.getAmountFormatted();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WalletToDepositThanksActivity.REQUEST_CODE) {
            switch (resultCode) {
                case WalletToDepositThanksActivity.RESULT_BACK_RETRY:
                    presenter.processMoveToSaldo(statePassData.getUrl(), statePassData.getParams());
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

    @Override
    public void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void onWalletToDepositSucceeded();

        void onWalletToDepositFailed();
    }
}