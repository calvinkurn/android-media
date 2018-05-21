package com.tokopedia.digital.tokocash.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.tokocash.TokoCashService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.digital.tokocash.domain.TokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.ActivateTokoCashInteractor;
import com.tokopedia.digital.tokocash.listener.ActivateTokoCashView;
import com.tokopedia.digital.tokocash.presenter.ActivateTokoCashPresenter;
import com.tokopedia.digital.tokocash.presenter.IActivateTokoCashPresenter;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashFragment extends BasePresenterFragment<IActivateTokoCashPresenter>
        implements ActivateTokoCashView {

    @BindView(R2.id.wallet_phone_number)
    TextView walletPhoneNumber;
    @BindView(R2.id.activation_btn)
    Button activationButton;
    @BindView(R2.id.syarat_ketentuan_tokocash)
    TextView syaratKetentuanTokocash;

    private ActionListener listener;
    private TkpdProgressDialog progressDialog;
    private CompositeSubscription compositeSubscription;

    public static ActivateTokoCashFragment newInstance() {
        ActivateTokoCashFragment fragment = new ActivateTokoCashFragment();
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
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        String acessToken = sessionHandler.getAccessToken(MainApplication.getAppContext());
        TokoCashService tokoCashService = new TokoCashService(acessToken);
        compositeSubscription = new CompositeSubscription();
        ActivateTokoCashInteractor interactor = new ActivateTokoCashInteractor
                (compositeSubscription, new TokoCashRepository(tokoCashService),
                        new JobExecutor(),
                        new UIThread());
        presenter = new ActivateTokoCashPresenter(interactor, this);
    }

    @Override
    protected void initialListener(Activity activity) {
        listener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_activate_tokocash;
    }

    @Override
    protected void initView(View view) {
        listener.setTitlePage(getResources().getString(R.string.tokocash_toolbar_activate));
        walletPhoneNumber.setText(SessionHandler.getPhoneNumber());
        setSyaratKetentuanTokocash();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        activationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionHandler.isMsisdnVerified()) {
                    presenter.linkWalletToTokoCash("");
                } else {
                    listener.directPageToOTPWallet();
                }
            }
        });
    }

    private void setSyaratKetentuanTokocash() {
        SpannableString ss = new SpannableString(getActivity().getString(R.string.syarat_ketentuan_tokocash));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(DigitalWebActivity.newInstance(getActivity(),
                        getActivity().getString(R.string.url_help_center_tokocash)));

            }
        };
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),
                R.color.digital_voucher_copied_color)), 48, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan, 48, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        syaratKetentuanTokocash.setMovementMethod(LinkMovementMethod.getInstance());
        syaratKetentuanTokocash.setText(ss);
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onSuccessLinkWalletToTokoCash() {
        finishProgressDialog();
        listener.directToSuccessActivateTokoCashPage();
    }

    @Override
    public void onErrorLinkWalletToTokoCash(String message) {
        finishProgressDialog();
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.showDialog();
        } else {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        }
    }

    private void finishProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void directPageToOTPWallet();

        void directToSuccessActivateTokoCashPage();
    }
}