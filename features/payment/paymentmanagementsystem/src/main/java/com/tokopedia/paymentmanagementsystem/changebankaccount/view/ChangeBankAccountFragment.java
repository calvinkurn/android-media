package com.tokopedia.paymentmanagementsystem.changebankaccount.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.activity.BankDestinationActivity;
import com.tokopedia.paymentmanagementsystem.changebankaccount.di.ChangeBankAccountModule;
import com.tokopedia.paymentmanagementsystem.common.Constant;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;
import com.tokopedia.paymentmanagementsystem.changebankaccount.di.DaggerChangeBankAccountComponent;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeBankAccountFragment extends BaseDaggerFragment implements ChangeBankAccountContract.View {

    public static final int REQUEST_CODE_GET_LIST_BANK = 738;
    @Inject
    ChangeBankAccountPresenter changeBankAccountPresenter;

    private PaymentListModel paymentListModel;
    private Spinner spinnerBankDest;
    private EditText inputAccountNo;
    private EditText inputAccountName;
    private EditText notes;
    private Button buttonUse;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerChangeBankAccountComponent.builder()
                .changeBankAccountModule(new ChangeBankAccountModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        changeBankAccountPresenter.attachView(this);
    }

    public static Fragment createInstance(PaymentListModel paymentListModel) {
        ChangeBankAccountFragment changeBankAccountFragment = new ChangeBankAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.PAYMENT_LIST_MODEL_EXTRA, paymentListModel);
        changeBankAccountFragment.setArguments(bundle);
        return changeBankAccountFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        paymentListModel = getArguments().getParcelable(Constant.PAYMENT_LIST_MODEL_EXTRA);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_bank_account, container, false);
        spinnerBankDest = view.findViewById(R.id.input_dest_bank_account);
        inputAccountNo = view.findViewById(R.id.input_account_number);
        inputAccountName = view.findViewById(R.id.input_account_name);
        notes = view.findViewById(R.id.input_note_optional);
        buttonUse = view.findViewById(R.id.button_use);

        inputAccountNo.setText(paymentListModel.getUserAccountNo());
        inputAccountName.setText(paymentListModel.getUserAccountName());

        buttonUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBankAccountPresenter.saveDetailAccount(getResources(), paymentListModel.getTransactionId(), paymentListModel.getMerchantCode(),
                        "destbank", inputAccountNo.getText().toString(), inputAccountName.getText().toString(), notes.getText().toString());
            }
        });
        spinnerBankDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(BankDestinationActivity.createIntent(getActivity()), REQUEST_CODE_GET_LIST_BANK);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        changeBankAccountPresenter.detachView();
    }

    @Override
    public void onErrorEditDetailAccount(Throwable e) {

    }

    @Override
    public void onResultEditDetailAccount(boolean success) {

    }
}
