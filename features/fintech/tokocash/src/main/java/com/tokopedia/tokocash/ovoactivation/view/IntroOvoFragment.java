package com.tokopedia.tokocash.ovoactivation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.common.di.TokoCashComponent;
import com.tokopedia.tokocash.tracker.WalletAnalytics;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 20/09/18.
 */
public class IntroOvoFragment extends BaseDaggerFragment implements IntroOvoContract.View {

    private Button activationOvoBtn;
    private Button learnMoreOvoBtn;
    private TextView tncOvo;
    private TextView titleOvo;
    private TextView descFirstOvo;
    private TextView descSecondOvo;
    private ImageView imgIntroOvo;
    private ProgressBar progressBar;
    private OvoFragmentListener listener;
    private boolean tokocashActive;

    @Inject
    IntroOvoPresenter presenter;
    @Inject
    WalletAnalytics walletAnalytics;

    public static IntroOvoFragment newInstance(boolean tokocashActive) {
        IntroOvoFragment fragment = new IntroOvoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IntroOvoActivity.TOKOCASH_ACTIVE, tokocashActive);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        GraphqlClient.init(getActivity());
        TokoCashComponent tokoCashComponent =
                TokoCashComponentInstance.getComponent(getActivity().getApplication());
        tokoCashComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_ovo, container, false);
        activationOvoBtn = view.findViewById(R.id.activation_ovo_btn);
        learnMoreOvoBtn = view.findViewById(R.id.learn_more_ovo_btn);
        tncOvo = view.findViewById(R.id.tnc_ovo);
        titleOvo = view.findViewById(R.id.title_intro);
        descFirstOvo = view.findViewById(R.id.description_1);
        descSecondOvo = view.findViewById(R.id.description_2);
        imgIntroOvo = view.findViewById(R.id.image_ovo);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getBalanceWallet();

        tokocashActive = getArguments().getBoolean(IntroOvoActivity.TOKOCASH_ACTIVE);

        if (tokocashActive) {
            listener.setTitleHeader(getString(R.string.title_header_ovo));
            titleOvo.setText(getString(R.string.announcement_ovo_title));
            descFirstOvo.setText(getString(R.string.announcement_ovo_description));
            descSecondOvo.setText(getString(R.string.announcement_ovo_second_desc));
            ImageHandler.loadImageWithId(imgIntroOvo, R.drawable.wallet_ic_intro_ovo);
        } else {
            listener.setTitleHeader(getString(R.string.title_header_activation_ovo));
            titleOvo.setText(getString(R.string.announcement_activation_ovo_title));
            descFirstOvo.setText(getString(R.string.announcement_activaiton_ovo_description));
            descSecondOvo.setText(getString(R.string.announcement_activation_ovo_second_desc));
            ImageHandler.loadImageWithId(imgIntroOvo, R.drawable.wallet_ic_intro_activation);
        }

        activationOvoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.checkPhoneNumber();
                walletAnalytics.eventClickActivationOvoNow();
            }
        });
    }

    private void setTncOvo(String tncApplink) {
        SpannableString ss = new SpannableString(getActivity().getString(R.string.announcement_ovo_text_tnc));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                walletAnalytics.eventClickTnc();
                directHelpPageWithApplink(tncApplink);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
            }
        };
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),
                R.color.tkpd_main_green)), 31, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan, 31, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tncOvo.setMovementMethod(LinkMovementMethod.getInstance());
        tncOvo.setText(ss);
    }

    @Override
    public void setApplinkButton(String helpAllink, String tncApplink) {
        setTncOvo(tncApplink);
        learnMoreOvoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletAnalytics.eventClickOvoLearnMore();
                directHelpPageWithApplink(helpAllink);
            }
        });
    }

    public void directHelpPageWithApplink(String ApplinkSchema) {
        if (RouteManager.isSupportApplink(getActivity(), ApplinkSchema)) {
            Intent intentRegisteredApplink = RouteManager.getIntent(getActivity(), ApplinkSchema);
            startActivity(intentRegisteredApplink);
        }
    }

    @Override
    public void directPageWithApplink(String ApplinkSchema) {
        if (RouteManager.isSupportApplink(getActivity(), ApplinkSchema)) {
            Intent intentRegisteredApplink = RouteManager.getIntent(getActivity(), ApplinkSchema);
            startActivity(intentRegisteredApplink);
            getActivity().finish();
        }
    }

    @Override
    public void directPageWithExtraApplink(String unRegisteredApplink, String registeredApplink,
                                           String phoneNumber, String changeMsisdnApplink) {
        if (RouteManager.isSupportApplink(getActivity(), unRegisteredApplink)) {
            Intent intentRegisteredApplink = RouteManager.getIntent(getActivity(), unRegisteredApplink);
            intentRegisteredApplink.putExtra(ActivationOvoActivity.REGISTERED_APPLINK, registeredApplink);
            intentRegisteredApplink.putExtra(ActivationOvoActivity.PHONE_NUMBER, phoneNumber);
            intentRegisteredApplink.putExtra(ActivationOvoActivity.CHANGE_MSISDN_APPLINK, changeMsisdnApplink);
            startActivity(intentRegisteredApplink);
            getActivity().finish();
        }
    }

    @Override
    public void showDialogErrorPhoneNumber(PhoneActionModel phoneActionModel) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(phoneActionModel.getTitlePhoneAction());
        dialog.setDesc(phoneActionModel.getDescPhoneAction());
        dialog.setBtnOk(phoneActionModel.getLabelBtnPhoneAction());
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletAnalytics.eventClickPopupPhoneNumber(phoneActionModel.getLabelBtnPhoneAction());
                directPageWithApplink(phoneActionModel.getApplinkPhoneAction());
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel(getString(R.string.button_label_cancel));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showSnackbarErrorMessage(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public String getErrorMessage(Throwable e) {
        return ErrorHandler.getErrorMessage(getActivity(), e);
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        listener = (OvoFragmentListener) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    interface OvoFragmentListener {
        void setTitleHeader(String titleHeader);
    }
}