package com.tokopedia.withdraw.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.view.listener.WithdrawSuccessPageContract;
import com.tokopedia.withdraw.view.presenter.WithdrawSuccessPresenter;

public class SuccessFragmentWithdrawal extends BaseDaggerFragment implements View.OnClickListener, WithdrawSuccessPageContract.View {

    private TextView titleTxtv;
    private TextView bankName;
    private TextView accountNum;
    private TextView totalAmt;
    private TextView backToSaldoDetail;
    private TextView backToAppShopping;
    private WithdrawSuccessPresenter withdrawSuccessPresenter;

    @Override
    protected void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        withdrawComponent.inject(this);
        withdrawSuccessPresenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
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
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.backto_saldo_dtl){
            if(getActivity() != null){
                getActivity().finish();
                //launch the detail page
            }
        }
        else if(id == R.id.backto_shop){
            if(getActivity() != null){
                getActivity().finish();
                //launch home page
            }
        }
    }

    @Override
    public void setSuccessTtlTxt(String ttlTxt) {
        this.titleTxtv.setText(ttlTxt);
    }

    @Override
    public void setBankName(String bankName) {
        this.bankName.setText(bankName);
    }

    @Override
    public void setAccountNumber(String accountNumber) {
        this.accountNum.setText(accountNumber);
    }

    @Override
    public void setWithdrawAmount(String withdrawAmount) {
        this.totalAmt.setText(withdrawAmount);
    }

    @Override
    public void onDestroy() {
        withdrawSuccessPresenter.detachView();
        super.onDestroy();
    }
}
