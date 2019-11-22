package com.tokopedia.withdraw.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.constant.WithdrawConstant;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.domain.model.BankAccount;

import java.util.Objects;

import javax.inject.Inject;

public class SuccessFragmentWithdrawal extends BaseDaggerFragment implements View.OnClickListener{

    private TextView titleTxtv;
    private TextView bankName;
    private TextView accountNum;
    private TextView totalAmt;
    private TextView backToSaldoDetail;
    private TextView backToAppShopping;
    private TextView adminFees;

    @Inject
    WithdrawAnalytics analytics;

    @Override
    protected void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        withdrawComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return WithdrawAnalytics.SCREEN_WITHDRAW_SUCCESS_PAGE;
    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.success_page, container, false);
        titleTxtv = view.findViewById(R.id.sucs_ttl);
        bankName = view.findViewById(R.id.bank_name);
        accountNum = view.findViewById(R.id.actn_no);
        totalAmt = view.findViewById(R.id.amt);
        backToSaldoDetail = view.findViewById(R.id.backto_saldo_dtl);
        backToSaldoDetail.setOnClickListener(this);
        backToAppShopping = view.findViewById(R.id.backto_shop);
        backToAppShopping.setOnClickListener(this);
        adminFees = view.findViewById(R.id.admin_fees);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            BankAccount bankAccount = getArguments().getParcelable(WithdrawConstant.Keys.BANK_ACCOUNT);
            String message = getArguments().getString(WithdrawConstant.Keys.MESSAGE);
            titleTxtv.setText(message);
            bankName.setText(bankAccount.getBankName());
            if(bankAccount.getAdminFee() > 0){
                adminFees.setText(String.format(getActivity().getResources().getString(R.string.admin_fee_msg),  Long.toString(bankAccount.getAdminFee())));
                adminFees.setVisibility(View.VISIBLE);
            }
            accountNum.setText(bankAccount.getAccountNo() + "-" + bankAccount.getAccountName());
            double amount = getArguments().getDouble(WithdrawConstant.Keys.AMOUNT);
            totalAmt.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(amount, false));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.backto_saldo_dtl){
            if(getActivity() != null){
                Objects.requireNonNull(getActivity()).setResult(WithdrawConstant.ResultCode.GOTO_SALDO_DETAIL_PAGE);
                getActivity().finish();
                analytics.eventClickBackToSaldoPage();
            }
        }
        else if(id == R.id.backto_shop){
            if(getActivity() != null){
                Objects.requireNonNull(getActivity()).setResult(WithdrawConstant.ResultCode.GOTO_TOKOPEDIA_HOME_PAGE);
                getActivity().finish();
                analytics.eventClicGoToHomePage();
            }
        }
    }

    public static Fragment getInstance(Bundle bundle){
        Fragment successFragment = new SuccessFragmentWithdrawal();
        successFragment.setArguments(bundle);
        return successFragment;
    }
}
