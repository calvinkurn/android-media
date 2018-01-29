package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ashwanityagi on on 5/6/17.
 */
public class CheckPulsaBalanceView extends LinearLayout {

    @BindView(R2.id.btn_check_client_balance)
    TextView btnCheckBalance;
    @BindView(R2.id.tv_check_balance)
    TextView tvOperatorName;
    @BindView(R2.id.tv_label_check_credit)
    TextView tvPhoneNumber;
    @BindView(R2.id.rl_holder_check_balance)
    RelativeLayout checkBalanceLayout;
    @BindView(R2.id.ll_holder_check_balance_wait)
    LinearLayout checkBalanceWaitLayout;
    @BindView(R2.id.check_balance_progressbar)
    ProgressBar checkBalanceProgressbar;
    @BindView(R2.id.tv_error_operator)
    TextView errorOperator;

    private ActionListener actionListener;
    private Context context;
    private String mobileNumber;

    public CheckPulsaBalanceView(Context context) {
        super(context);
        init(context);
    }

    public CheckPulsaBalanceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckPulsaBalanceView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_holder_client_check_balance, this, true);
        ButterKnife.bind(this);

    }

    public void hideProgressbar() {
        checkBalanceLayout.setVisibility(View.VISIBLE);
        checkBalanceWaitLayout.setVisibility(View.INVISIBLE);
    }

    public void showCheckBalanceProgressbar() {
        checkBalanceLayout.setVisibility(View.INVISIBLE);
        checkBalanceWaitLayout.setVisibility(View.VISIBLE);

    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderData(int simPosition, String ussdCode, String phoneNumber,String operatorErrorMsg, Boolean activeSim,String operatorName) {
        if (phoneNumber != null && !"".equalsIgnoreCase(phoneNumber.trim()))
            tvPhoneNumber.setText(context.getString(R.string.label_sim) + (simPosition + 1) + "- " + phoneNumber);
        else
            tvPhoneNumber.setText(context.getString(R.string.label_sim) + (simPosition + 1));
        this.mobileNumber = phoneNumber;
        if (activeSim) {
            this.btnCheckBalance.setOnClickListener(getButtonCheckBalanceClicked(simPosition, ussdCode));
            errorOperator.setVisibility(GONE);
            if (operatorErrorMsg != null && operatorName != null) {
                tvPhoneNumber.setText(context.getString(R.string.label_sim) + (simPosition + 1) + "- " + operatorName);
            }
        } else {
            btnCheckBalance.setBackgroundColor(context.getResources().getColor(R.color.grey_hint));
            errorOperator.setVisibility(VISIBLE);
            errorOperator.setText(operatorErrorMsg);
            if (operatorName != null) {
                tvPhoneNumber.setText(context.getString(R.string.label_sim) + (simPosition + 1) + "- " + operatorName);
            }
        }
    }

    public String getPhoneNumberText() {
        return this.mobileNumber;
    }

    @NonNull
    private OnClickListener getButtonCheckBalanceClicked(final int simPosition, final String ussdCode) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onButtonCheckBalanceClicked(simPosition, ussdCode , CheckPulsaBalanceView.this);
            }
        };
    }

    public interface ActionListener {
        void onButtonCheckBalanceClicked(int simPosition, String ussdCode, CheckPulsaBalanceView checkPulsaBalanceView);

    }

}