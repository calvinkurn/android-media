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
import com.tokopedia.digital.product.model.ClientNumber;

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


    private ActionListener actionListener;
    private Context context;

    private ClientNumber clientNumber;

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

    //    public void setText(String text) {
//        tvBalance.setText(text);
//
//    }
    public void hideProgressbar() {
        //checkBalanceProgressbar.setVisibility(View.GONE);
        checkBalanceLayout.setVisibility(View.VISIBLE);
        checkBalanceWaitLayout.setVisibility(View.INVISIBLE);
    }

    public void showCheckBalanceProgressbar() {
        //checkBalanceProgressbar.setVisibility(View.VISIBLE);
        checkBalanceLayout.setVisibility(View.INVISIBLE);
        checkBalanceWaitLayout.setVisibility(View.VISIBLE);

    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderData(int simPosition, String ussdCode, String phoneNumber,String operatorName) {
        this.btnCheckBalance.setOnClickListener(getButtonCheckBalanceClicked(simPosition, ussdCode));
        tvOperatorName.setText(operatorName);
        tvPhoneNumber.setText(phoneNumber);
    }

    @NonNull
    private OnClickListener getButtonCheckBalanceClicked(final int simPosition, final String ussdCode) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onButtonCheckBalanceClicked(simPosition, ussdCode);
            }
        };
    }


    public interface ActionListener {
        void onButtonCheckBalanceClicked(int simPosition, String ussdCode);

    }

}