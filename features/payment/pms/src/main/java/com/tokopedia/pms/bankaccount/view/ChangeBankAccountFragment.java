package com.tokopedia.pms.bankaccount.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.pms.R;
import com.tokopedia.pms.bankdestination.view.activity.BankDestinationActivity;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;
import com.tokopedia.pms.bankaccount.di.ChangeBankAccountModule;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.common.SpinnerTextViewBankList;
import com.tokopedia.pms.payment.view.model.PaymentListModel;
import com.tokopedia.pms.bankaccount.di.DaggerChangeBankAccountComponent;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeBankAccountFragment extends BaseDaggerFragment implements ChangeBankAccountContract.View {

    public static final int REQUEST_CODE_GET_LIST_BANK = 738;
    @Inject
    ChangeBankAccountPresenter changeBankAccountPresenter;

    private PaymentListModel paymentListModel;
    private SpinnerTextViewBankList spinnerBankDest;
    private EditText inputAccountNo;
    private EditText inputAccountName;
    private EditText notes;
    private Button buttonUse;
    private ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading));

        inputAccountNo.setText(paymentListModel.getUserAccountNo());
        inputAccountName.setText(paymentListModel.getUserAccountName());
        spinnerBankDest.setSpinnerValue(paymentListModel.getBankId());

        buttonUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBankAccountPresenter.saveDetailAccount(getResources(), paymentListModel.getTransactionId(), paymentListModel.getMerchantCode(),
                        Integer.valueOf(spinnerBankDest.getSpinnerValue()), inputAccountNo.getText().toString(), inputAccountName.getText().toString(), notes.getText().toString());
            }
        });
        spinnerBankDest.setListenerOnClick(new SpinnerTextViewBankList.ListenerOnClick() {
            @Override
            public void onClickTextAutoComplete(View view) {
                startActivityForResult(BankDestinationActivity.createIntent(getActivity()), REQUEST_CODE_GET_LIST_BANK);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GET_LIST_BANK && resultCode == Activity.RESULT_OK){
            BankListModel bankListModel = data.getParcelableExtra(Constant.EXTRA_BANK_LIST_MODEL);
            spinnerBankDest.setSpinnerValue(bankListModel.getId());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        changeBankAccountPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onErrorEditDetailAccount(Throwable e) {
        NetworkErrorHelper.showSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onResultEditDetailAccount(boolean success, String message) {
        if(success){
            NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), message);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }else{
            NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
        }
    }

    @Override
    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        progressDialog.hide();
    }
}
