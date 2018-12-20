package com.tokopedia.digital.product.view.compoundview;

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

/**
 * Created by ashwanityagi on on 5/6/17.
 */
public class CheckPulsaBalanceView extends LinearLayout {

    private TextView btnCheckBalance;
    private TextView tvOperatorName;
    private TextView tvPhoneNumber;
    private RelativeLayout checkBalanceLayout;
    private LinearLayout checkBalanceWaitLayout;
    private ProgressBar checkBalanceProgressbar;
    private TextView errorOperator;

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

        btnCheckBalance = findViewById(R.id.btn_check_client_balance);
        tvOperatorName = findViewById(R.id.tv_check_balance);
        tvPhoneNumber = findViewById(R.id.tv_label_check_credit);
        checkBalanceLayout = findViewById(R.id.rl_holder_check_balance);
        checkBalanceWaitLayout = findViewById(R.id.ll_holder_check_balance_wait);
        checkBalanceProgressbar = findViewById(R.id.check_balance_progressbar);
        errorOperator = findViewById(R.id.tv_error_operator);

        LayoutInflater.from(context).inflate(R.layout.view_holder_client_check_balance, this, true);
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
        return v -> actionListener.onButtonCheckBalanceClicked(simPosition, ussdCode , CheckPulsaBalanceView.this);
    }

    public interface ActionListener {

        void onButtonCheckBalanceClicked(int simPosition, String ussdCode, CheckPulsaBalanceView checkPulsaBalanceView);

    }

}