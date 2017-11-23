package com.tokopedia.digital.tokocash.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.WalletToDepositThanksData;
import com.tokopedia.digital.tokocash.model.WalletToDepositThanksPassData;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositThanksFragment extends BasePresenterFragment {
    public static final String ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA =
            "ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA";
    public static final String STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA =
            "STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA";

    @BindView(R2.id.iv_icon)
    ImageView ivIcon;
    @BindView(R2.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R2.id.tv_description)
    TextView tvDesc;
    @BindView(R2.id.btn_negative)
    TextView btnNegative;
    @BindView(R2.id.btn_positive)
    TextView btnPositive;

    private WalletToDepositThanksPassData stateWalletToDepositThanksPassData;
    private ActionListener actionListener;


    public static Fragment newInstance(WalletToDepositThanksPassData passData) {
        WalletToDepositThanksFragment fragment = new WalletToDepositThanksFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA, passData);
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
                STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA, stateWalletToDepositThanksPassData
        );
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        stateWalletToDepositThanksPassData = savedState.getParcelable(
                STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA
        );
        if (stateWalletToDepositThanksPassData != null) renderContentData();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.stateWalletToDepositThanksPassData =
                arguments.getParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_wallet_to_deposit_thanks_digital_module;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        renderContentData();
    }

    private void renderContentData() {
        actionListener.setTitlePage(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitle()
        );
        if (stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTypeResult()
                == WalletToDepositThanksData.TypeResult.SUCCESS) {
            renderSuccessResult();
        } else {
            renderFailedResult();
        }
    }

    private void renderFailedResult() {
        tvSubTitle.setVisibility(View.VISIBLE);
        tvDesc.setVisibility(View.GONE);
        btnPositive.setVisibility(View.VISIBLE);
        btnNegative.setVisibility(View.VISIBLE);
        ivIcon.setVisibility(View.VISIBLE);

        ivIcon.setImageResource(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getIconResId()
        );

        tvSubTitle.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getSubTitle()
        );
        btnPositive.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitleButtonPositive()
        );
        btnNegative.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitleButtonNegative()
        );

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onRetryClicked();
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onBackToTokoCashClicked(false);
            }
        });
    }

    private void renderSuccessResult() {
        tvSubTitle.setVisibility(View.VISIBLE);
        tvDesc.setVisibility(View.VISIBLE);
        btnPositive.setVisibility(View.VISIBLE);
        btnNegative.setVisibility(View.GONE);
        ivIcon.setVisibility(View.VISIBLE);

        ivIcon.setImageResource(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getIconResId()
        );

        tvSubTitle.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getSubTitle()
        );
        tvDesc.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getDescription()
        );
        btnPositive.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitleButtonPositive()
        );

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onBackToTokoCashClicked(true);
            }
        });

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }


    public interface ActionListener {
        void onBackToTokoCashClicked(boolean isSucceeded);

        void onRetryClicked();

        void setTitlePage(String titlePage);
    }
}
