package com.tokopedia.tokocash.activation.presentation.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.activation.presentation.contract.LinkedTokoCashContract;
import com.tokopedia.tokocash.activation.presentation.presenter.LinkedTokoCashPresenter;
import com.tokopedia.tokocash.common.di.TokoCashComponent;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashFragment extends BaseDaggerFragment implements LinkedTokoCashContract.View {

    private TextView walletPhoneNumber;
    private Button activationButton;
    private TextView syaratKetentuanTokocash;

    private ActionListener listener;
    private ProgressBar progressBar;

    @Inject
    LinkedTokoCashPresenter presenter;

    public static ActivateTokoCashFragment newInstance() {
        ActivateTokoCashFragment fragment = new ActivateTokoCashFragment();
        return fragment;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activate_tokocash, container, false);
        walletPhoneNumber = view.findViewById(R.id.wallet_phone_number);
        activationButton = view.findViewById(R.id.activation_btn);
        syaratKetentuanTokocash = view.findViewById(R.id.syarat_ketentuan_tokocash);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ActionListener) activity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener.setTitlePage(getResources().getString(R.string.tokocash_toolbar_activate));
        walletPhoneNumber.setText(presenter.getUserPhoneNumber());
        setSyaratKetentuanTokocash();

        activationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.isMsisdnUserVerified()) {
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
                Application application = getActivity().getApplication();
                if (application != null && application instanceof TokoCashRouter) {
                    Intent intent = ((TokoCashRouter) application).getWebviewActivityWithIntent(getActivity(),
                            getString(R.string.url_help_center_tokocash), getString(R.string.help_general_tokocash));
                    startActivity(intent);
                }
            }
        };
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),
                R.color.tkpd_main_green)), 48, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan, 48, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        syaratKetentuanTokocash.setMovementMethod(LinkMovementMethod.getInstance());
        syaratKetentuanTokocash.setText(ss);
    }

    @Override
    public void onDestroyView() {
        presenter.destroyView();
        super.onDestroyView();
    }

    @Override
    public void onSuccessLinkWalletToTokoCash() {
        finishProgressDialog();
        listener.directToSuccessActivateTokoCashPage();
    }

    @Override
    public void onErrorLinkWalletToTokoCash(Throwable e) {
        finishProgressDialog();
        NetworkErrorHelper.showSnackbar(getActivity(), e.getMessage());
    }

    @Override
    public void onErrorNetwork(Throwable e) {
        finishProgressDialog();
        String message = ErrorHandler.getErrorMessage(getActivity(), e);
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void finishProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        TokoCashComponent tokoCashComponent =
                TokoCashComponentInstance.getComponent(getActivity().getApplication());
        tokoCashComponent.inject(this);
        presenter.attachView(this);
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void directPageToOTPWallet();

        void directToSuccessActivateTokoCashPage();
    }
}