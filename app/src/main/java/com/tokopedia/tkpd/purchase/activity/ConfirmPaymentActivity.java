package com.tokopedia.tkpd.purchase.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.BasePresenterActivity;
import com.tokopedia.tkpd.database.model.Bank;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.purchase.listener.ConfirmPaymentViewListener;
import com.tokopedia.tkpd.purchase.model.ConfirmPaymentData;
import com.tokopedia.tkpd.purchase.model.ConfirmationData;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.BankAccount;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.Datetime;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.Form;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.FormConfPaymentData;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.FormEdit;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.FormEditPaymentData;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.Method;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.SysBankAccount;
import com.tokopedia.tkpd.purchase.presenter.ConfirmPaymentPresenter;
import com.tokopedia.tkpd.purchase.presenter.ConfirmPaymentPresenterImpl;
import com.tokopedia.tkpd.purchase.receiver.TxActionReceiver;
import com.tokopedia.tkpd.util.TokopediaBankAccount;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author by Angga.Prasetiyo on 20/06/2016.
 */
public class ConfirmPaymentActivity extends BasePresenterActivity<ConfirmPaymentPresenter>
        implements ConfirmPaymentViewListener {
    private static final String EXTRA_CONFIRMATION_ID = "EXTRA_CONFIRMATION_ID";
    private static final String EXTRA_INSTANCE_TYPE = "EXTRA_INSTANCE_TYPE";

    public static final String EXTRA_MESSAGE_ERROR_GET_FORM = "EXTRA_MESSAGE_ERROR_GET_FORM";
    public static final int RESULT_FORM_FAILED = 2;

    public static final int INSTANCE_EDIT = 1;
    public static final int INSTANCE_NEW = 0;

    @Bind(R.id.tv_label_payment_method)
    TextView tvLabelPaymentMethod;
    @Bind(R.id.tv_label_bank)
    TextView tvLabelOriginAccountBank;
    @Bind(R.id.tv_label_dest_bank)
    TextView tvLabelDestAccountBank;

    @Bind(R.id.spinner_dest_account)
    AppCompatSpinner spDestAccountBank;
    @Bind(R.id.spinner_from_account)
    AppCompatSpinner spOriginAccountBank;
    @Bind(R.id.spinner_payment_method)
    AppCompatSpinner spPaymentMethod;

    @Bind(R.id.tv_error_payment_method)
    TextView tvErrorPaymentMethod;
    @Bind(R.id.error_spinner_account_from)
    TextView tvErrorOriginAccountBank;
    @Bind(R.id.error_spinner_account_dest)
    TextView tvErrorDestAccountBank;

    @Bind(R.id.dest_account)
    View viewAccountDestination;
    @Bind(R.id.from_account)
    View viewFormAccount;
    @Bind(R.id.main_view)
    View viewMain;
    @Bind(R.id.new_account)
    View viewNewAccount;
    @Bind(R.id.total_payment_view)
    View viewInfoPayment;
    @Bind(R.id.password_view)
    View viewFormPassword;
    @Bind(R.id.nama_penyetor_view)
    View viewFormDepositor;
    @Bind(R.id.et_choose_bank)
    EditText tvChooseAccountBank;
    @Bind(R.id.msg_success)
    TextView tvSuccessMessage;
    @Bind(R.id.submit_but)
    TextView btnSubmit;
    @Bind(R.id.total_payment)
    TextView tvTotalPayment;
    @Bind(R.id.total_payment_success)
    TextView tvSuccessTotalPayment;
    @Bind(R.id.title_conf_payment)
    TextView tvLabelTotalPayment;
    @Bind(R.id.tokopedia_deposit)
    TextView tvSuccessTokopediaDeposit;
    @Bind(R.id.remaining_tokopedia_deposit)
    TextView tvRemainingTokopediaDeposit;
    @Bind(R.id.check_account)
    View btnSysAccountInfo;
    @Bind(R.id.account_owner)
    EditText etAccountOwner;
    @Bind(R.id.account_number)
    EditText etAccountNumber;
    @Bind(R.id.payment_date)
    EditText etPayementDate;
    @Bind(R.id.password)
    EditText etUserPassword;
    @Bind(R.id.remark)
    EditText etNotes;
    @Bind(R.id.input_total_payment)
    EditText etTotalPayment;
    @Bind(R.id.nama_penyetor)
    EditText etDepositorName;
    @Bind(R.id.branch)
    EditText etAccountBankBranch;
    @Bind(R.id.pay_conf)
    View viewFormPayment;
    @Bind(R.id.pay_conf_success)
    View viewPaymentSuccess;

    @Bind(R.id.input_layout_payment_date)
    TextInputLayout tilEtDate;
    @Bind(R.id.input_layout_account_owner)
    TextInputLayout tilEtAccountOwner;
    @Bind(R.id.input_layout_account_number)
    TextInputLayout tilEtAccountNumber;
    @Bind(R.id.input_layout_choose_bank)
    TextInputLayout tilEtChooseBank;
    @Bind(R.id.input_layout_branch)
    TextInputLayout tilEtBankBranch;
    @Bind(R.id.input_layout_depositor)
    TextInputLayout tilEtDepositorName;
    @Bind(R.id.input_layout_total_payment)
    TextInputLayout tilEtPaymentAmount;
    @Bind(R.id.input_layout_remark)
    TextInputLayout tilEtRemark;
    @Bind(R.id.input_layout_password)
    TextInputLayout tilEtPassword;


    private TkpdProgressDialog progressDialog;
    private TkpdProgressDialog progressDialogMain;

    private DatePickerDialog datePicker;

    private int instanceType = INSTANCE_NEW;
    private String confirmationId;

    private ConfirmPaymentData confirmPaymentData;
    private Form formConfirmData;
    private FormEdit formEditData;
    private TxActionReceiver receiver;

    public static Intent instanceConfirm(Context context, String confirmationId) {
        Intent intent = new Intent(context, ConfirmPaymentActivity.class);
        intent.putExtra(EXTRA_CONFIRMATION_ID, confirmationId);
        intent.putExtra(EXTRA_INSTANCE_TYPE, INSTANCE_NEW);
        return intent;
    }

    public static Intent instanceEdit(Context context, String confirmationId) {
        Intent intent = new Intent(context, ConfirmPaymentActivity.class);
        intent.putExtra(EXTRA_CONFIRMATION_ID, confirmationId);
        intent.putExtra(EXTRA_INSTANCE_TYPE, INSTANCE_EDIT);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.instanceType = extras.getInt(EXTRA_INSTANCE_TYPE);
        this.confirmationId = extras.getString(EXTRA_CONFIRMATION_ID);
    }

    @Override
    protected void initialPresenter() {
        presenter = new ConfirmPaymentPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_confirm_payment;
    }

    @Override
    protected void initView() {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialogMain = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS,
                getWindow().getDecorView().getRootView());
        progressDialogMain.setLoadingViewId(R.id.include_loading);
    }

    @Override
    protected void setViewListener() {
        etPayementDate.setVisibility(View.GONE);
        viewPaymentSuccess.setVisibility(View.GONE);
        viewMain.setVisibility(View.GONE);
        etAccountOwner.addTextChangedListener(new InputWatcher(etAccountOwner.getId()));
        etAccountNumber.addTextChangedListener(new InputWatcher(etAccountNumber.getId()));
        etTotalPayment.addTextChangedListener(new InputWatcher(etTotalPayment.getId()));
        etDepositorName.addTextChangedListener(new InputWatcher(etDepositorName.getId()));
        etNotes.addTextChangedListener(new InputWatcher(etNotes.getId()));
        etAccountBankBranch.addTextChangedListener(new InputWatcher(etAccountBankBranch.getId()));
        etUserPassword.addTextChangedListener(new InputWatcher(etUserPassword.getId()));
    }

    @Override
    protected void initVar() {
        IntentFilter filter = new IntentFilter(TxActionReceiver.TX_ACTION_RECEIVER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = TxActionReceiver.instanceConfirmPayment(this);
        confirmPaymentData = new ConfirmPaymentData();
        confirmPaymentData.setPaymentId(confirmationId);
        confirmPaymentData.setConfirmation(instanceType == INSTANCE_NEW);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void setActionVar() {
        progressDialogMain.showDialog();
        switch (instanceType) {
            case INSTANCE_EDIT:
                presenter.processGetEditPaymentForm(this, confirmationId);
                break;
            case INSTANCE_NEW:
                presenter.processGetConfirmPaymentForm(this, confirmationId);
                break;
        }
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        else CommonUtils.UniversalToast(this, message);
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void closeView() {
        finish();
    }

    @OnClick(R.id.payment_date)
    void actionChooseDate() {
        if (datePicker != null) datePicker.show();
    }

    @OnClick(R.id.submit_but)
    void actionSubmitConfirmation() {
        presenter.processSubmitConfirmation(this, confirmPaymentData, formConfirmData, formEditData);
    }

    @OnClick(R.id.et_choose_bank)
    void actionChooseBank() {
        presenter.processChooseBank(this, new OnNewAccountBankSelected() {
            @Override
            public void onBankSelected(Bank bank) {
                confirmPaymentData.setBankId(bank.getBankId());
                confirmPaymentData.setBankName(bank.getBankName());
                tvChooseAccountBank.setText(confirmPaymentData.getBankName());
            }
        });
    }

    @Override
    public void renderFormConfirmation(FormConfPaymentData data) {
        this.formConfirmData = data.getForm();
        this.confirmPaymentData.setToken(data.getForm().getToken());
        renderDatePicker(data.getForm().getDatetime());
        renderMethodPayment(data.getForm().getMethod(data.getForm().getOrder()
                .getOrderDepositable().equals("0")));
        renderFormBank(data.getForm().getBankAccount());
        renderSysBank(data.getForm().getSysbankAccount());
        tvTotalPayment.setText(data.getForm().getOrder().getOrderLeftAmountIdr());
        progressDialogMain.dismiss();
        viewMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderFormEdit(FormEditPaymentData data) {
        this.formEditData = data.getForm();
        renderDatePicker(data.getForm().getDatetime());
        renderMethodPayment(data.getForm().getMethodEdit().getMethodList(data.getForm()
                .getPayment().getOrderDepositable() == 0));
        renderFormBank(data.getForm().getBankAccountEdit().getBankAccountList());
        renderSysBank(data.getForm().getSysBankAccountEdit().getSysBankList());
        renderChosenEditForm(data.getForm());
        tvTotalPayment.setText(data.getForm().getPayment().getOrderLeftAmountIdr());
        progressDialogMain.dismiss();
        viewMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderConfirmationError(String errorMsg) {
        showToastMessage(errorMsg);
    }

    @Override
    public void renderConfirmationTimeout(String errorMsg) {
        NetworkErrorHelper.createSnackbarWithAction(this,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        actionSubmitConfirmation();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void renderEditTimeout(String message) {
        NetworkErrorHelper.createSnackbarWithAction(this,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        actionSubmitConfirmation();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void renderConfirmationNoConnection(String message) {
        NetworkErrorHelper.showDialog(this, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                actionSubmitConfirmation();
            }
        });
    }

    @Override
    public void renderErrorFormConfirmation(String message) {
        progressDialogMain.dismiss();
        setResult(RESULT_FORM_FAILED, new Intent().putExtra(EXTRA_MESSAGE_ERROR_GET_FORM, message));
        finish();
    }

    @Override
    public void renderNoConnectionFormConfirmation(String message) {
        progressDialogMain.dismiss();
        NetworkErrorHelper.showEmptyState(this, parentView, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setActionVar();
            }
        });
    }

    @Override
    public void renderErrorFormEdit(String message) {
        progressDialogMain.dismiss();
        setResult(RESULT_FORM_FAILED, new Intent().putExtra(EXTRA_MESSAGE_ERROR_GET_FORM, message));
        finish();
    }

    @Override
    public void renderNoConnectionFormEdit(String message) {
        progressDialogMain.dismiss();
        NetworkErrorHelper.showEmptyState(this, parentView, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setActionVar();
            }
        });
    }

    private void renderChosenEditForm(FormEdit form) {
        int chosenMethod = 0;
        for (int i = 0; i < spPaymentMethod.getCount(); i++) {
            Method method = (Method) spPaymentMethod.getAdapter().getItem(i);
            if (form.getMethodEdit().getMethodIdChosen().equals(method.getMethodId()))
                chosenMethod = i;
        }
        spPaymentMethod.setSelection(chosenMethod);
        int chosenBank = 0;
        for (int i = 0; i < spOriginAccountBank.getCount(); i++) {
            BankAccount bankAccount = (BankAccount) spOriginAccountBank.getAdapter().getItem(i);
            if (form.getBankAccountEdit().getBankAccountIdChosen().equals(bankAccount.getBankAccountId()))
                chosenBank = i;
        }
        spOriginAccountBank.setSelection(chosenBank);
        int chosenSys = 0;
        for (int i = 0; i < spDestAccountBank.getCount(); i++) {
            SysBankAccount sysBankAccount = (SysBankAccount) spDestAccountBank.getAdapter().getItem(i);
            if (form.getSysBankAccountEdit().getSysBankIdChosen().equals(sysBankAccount.getSysbankId()))
                chosenSys = i;
        }
        spDestAccountBank.setSelection(chosenSys);
    }

    @Override
    public void renderErrorPaymentMethod(String message) {
        tvErrorPaymentMethod.setText(message);
        tvErrorPaymentMethod.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderErrorSysBank(String message) {
        tvErrorDestAccountBank.setText(message);
        tvErrorDestAccountBank.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderErrorAccountBank(String message) {
        tvErrorOriginAccountBank.setText(message);
        tvErrorOriginAccountBank.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderErrorAccountName(String message) {
        tilEtAccountOwner.setError(message);
        requestFocusError(etAccountOwner);
    }

    @Override
    public void renderErrorAccountNumber(String message) {
        tilEtAccountNumber.setError(message);
        requestFocusError(etAccountNumber);
    }

    @Override
    public void renderErrorAccountBranch(String message) {
        tilEtBankBranch.setError(message);
        requestFocusError(etAccountBankBranch);
    }

    @Override
    public void renderErrorChooseBank(String message) {
        tilEtChooseBank.setError(message);
        requestFocusError(tvChooseAccountBank);
    }

    @Override
    public void renderErrotDepositorName(String message) {
        tilEtDepositorName.setError(message);
        requestFocusError(etDepositorName);
    }

    @Override
    public void renderErrorDepositorPassword(String message) {
        tilEtPassword.setErrorEnabled(true);
        tilEtPassword.setError(message);
        requestFocusError(etUserPassword);
    }

    @Override
    public void renderErrorPaymentAmount(String message) {
        tilEtPaymentAmount.setError(message);
        requestFocusError(etTotalPayment);
    }

    @Override
    public void renderErrorDate(String message) {
        tilEtDate.setError(message);
        requestFocusError(etPayementDate);
    }

    @Override
    public void requestFocusError(View view) {
        if (view.requestFocus())
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void renderConfirmationSuccess(ConfirmationData data) {
        tvLabelTotalPayment.setText(getString(R.string.title_confirmed_payment));
        tvSuccessTotalPayment.setText(data.getPaymentDetail().getPaymentAmt());
        tvSuccessMessage.setText(MessageFormat.format("{0} {1}", getString(R.string.msg_payment_success),
                data.getPaymentDetail().getPaymentMethodName()));
        viewPaymentSuccess.setVisibility(View.VISIBLE);
        viewFormPayment.setVisibility(View.GONE);
        presenter.setLocalyticsFlow(this, data);
        setResult(RESULT_OK);
    }

    private void renderSysBank(List<SysBankAccount> datas) {
        datas.add(0, SysBankAccount.instanceInfo(getString(R.string.title_dest_account_2)));
        ArrayAdapter<SysBankAccount> adapter = new ArrayAdapter<>(this,
                R.layout.multiline_spinner, datas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestAccountBank.setAdapter(adapter);
        spDestAccountBank.setOnItemSelectedListener(new OnSysBankSelected());
    }

    private void renderFormBank(List<BankAccount> datas) {
        datas.add(0, BankAccount.instanceInfo(getString(R.string.title_from_account_2)));
        datas.add(BankAccount.instanceAddNew(getString(R.string.title_add_new_bank)));
        ArrayAdapter<BankAccount> adapter = new ArrayAdapter<>(this,
                R.layout.multiline_spinner, datas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOriginAccountBank.setAdapter(adapter);
        spOriginAccountBank.setOnItemSelectedListener(new OnAccountBankSelected());
    }

    private void renderMethodPayment(List<Method> datas) {
        ArrayAdapter<Method> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, datas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentMethod.setAdapter(adapter);
        spPaymentMethod.setOnItemSelectedListener(new OnMethodPaymentSelected());
    }

    private void renderDatePicker(final Datetime datetime) {
        Calendar maxDate = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        maxDate.set(datetime.getYearMaxInt(), maxDate.getMaximum(Calendar.MONTH),
                maxDate.getMaximum(Calendar.DATE), maxDate.getMaximum(Calendar.HOUR_OF_DAY),
                maxDate.getMaximum(Calendar.HOUR));
        minDate.set(datetime.getYearMinInt(), minDate.getMinimum(Calendar.MONTH),
                minDate.getMinimum(Calendar.DATE), maxDate.getMaximum(Calendar.HOUR_OF_DAY),
                maxDate.getMaximum(Calendar.HOUR) - 1);
        long maxtime = maxDate.getTimeInMillis();
        long mintime = minDate.getTimeInMillis();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                confirmPaymentData.setPaymentDay(dayOfMonth + "");
                confirmPaymentData.setPaymentMonth((monthOfYear + 1) + "");
                confirmPaymentData.setPaymentYear(year + "");
                etPayementDate.setText(datetime.formatDate(year, monthOfYear, dayOfMonth));
            }
        }, datetime.getYearMaxInt(), datetime.getMonthInt(), datetime.getDayInt());
        confirmPaymentData.setPaymentDay(datetime.getDateDay());
        confirmPaymentData.setPaymentYear(datetime.getDateYearMax());
        confirmPaymentData.setPaymentMonth(datetime.getDateMonth());
        datePicker.getDatePicker().setMaxDate(maxtime);
        datePicker.getDatePicker().setMinDate(mintime);
        etPayementDate.setText(datetime.getDateFormatted());
    }


    private class OnMethodPaymentSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String methodId = ((Method) parent.getAdapter().getItem(position)).getMethodId();
            confirmPaymentData.setPaymentMethod(methodId);
            switch (methodId) {
                case "5":
                case "0":
                    viewFormPassword.setVisibility(View.VISIBLE);
                    viewInfoPayment.setVisibility(View.GONE);
                    viewFormAccount.setVisibility(View.GONE);
                    viewAccountDestination.setVisibility(View.GONE);
                    viewFormDepositor.setVisibility(View.GONE);
                    viewNewAccount.setVisibility(View.GONE);
                    btnSysAccountInfo.setVisibility(View.GONE);
                    break;
                case "6":
                    viewFormPassword.setVisibility(View.GONE);
                    viewInfoPayment.setVisibility(View.VISIBLE);
                    viewNewAccount.setVisibility(View.GONE);
                    viewFormAccount.setVisibility(View.GONE);
                    viewAccountDestination.setVisibility(View.VISIBLE);
                    viewFormDepositor.setVisibility(View.VISIBLE);
                    btnSysAccountInfo.setVisibility(View.VISIBLE);
                    break;
                default:
                    viewFormDepositor.setVisibility(View.GONE);
                    viewFormPassword.setVisibility(View.GONE);
                    viewInfoPayment.setVisibility(View.VISIBLE);
                    viewFormAccount.setVisibility(View.VISIBLE);
                    viewAccountDestination.setVisibility(View.VISIBLE);
                    viewNewAccount.setVisibility(confirmPaymentData.getBankAccountId() != null
                            && confirmPaymentData.getBankAccountId().equals("ADD_NEW")
                            && confirmPaymentData.getBankId().equals("ADD_NEW")
                            ? View.VISIBLE : View.GONE);
                    btnSysAccountInfo.setVisibility(View.VISIBLE);
                    break;
            }
            tvLabelPaymentMethod.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            tvErrorPaymentMethod.setVisibility(View.GONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OnAccountBankSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            BankAccount bankAccount = ((BankAccount) parent.getAdapter().getItem(position));
            confirmPaymentData.setBankId(bankAccount.getBankId());
            confirmPaymentData.setBankAccountName(bankAccount.getBankAccountName());
            confirmPaymentData.setBankAccountId(bankAccount.getBankAccountId());
            confirmPaymentData.setBankName(bankAccount.getBankName());
            confirmPaymentData.setBankAccountNumber(bankAccount.getBankAccountNumber());
            if (bankAccount.getBankAccountId().equals("ADD_NEW")) {
                confirmPaymentData.setNewAccountBank(true);
                viewNewAccount.setVisibility(View.VISIBLE);
            } else {
                confirmPaymentData.setNewAccountBank(false);
                viewNewAccount.setVisibility(View.GONE);
            }
            tvLabelOriginAccountBank.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            tvErrorOriginAccountBank.setVisibility(View.GONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OnSysBankSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SysBankAccount sysBankAccount = (SysBankAccount) parent.getAdapter().getItem(position);
            confirmPaymentData.setSysBankId(sysBankAccount.getSysbankId());
            tvLabelDestAccountBank.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            tvErrorDestAccountBank.setVisibility(View.GONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public interface OnNewAccountBankSelected {
        void onBankSelected(Bank bank);
    }

    private class InputWatcher implements TextWatcher {

        private final int resId;

        public InputWatcher(int id) {
            resId = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (resId) {
                case R.id.remark:
                    tilEtRemark.setErrorEnabled(false);
                    break;
                case R.id.nama_penyetor:
                    tilEtDepositorName.setErrorEnabled(false);
                    break;
                case R.id.input_total_payment:
                    tilEtPaymentAmount.setErrorEnabled(false);
                    CurrencyFormatHelper.SetToRupiah(etTotalPayment);
                    break;
                case R.id.account_owner:
                    tilEtAccountOwner.setErrorEnabled(false);
                    confirmPaymentData.setBankAccountName(s.toString());
                    break;
                case R.id.account_number:
                    tilEtAccountNumber.setErrorEnabled(false);
                    confirmPaymentData.setBankAccountNumber(s.toString());
                    break;
                case R.id.branch:
                    tilEtBankBranch.setErrorEnabled(false);
                    confirmPaymentData.setBankAccountBranch(s.toString());
                    break;
                case R.id.password:
                    if (s.length() <= 0) {
                        renderErrorDepositorPassword(getString(R.string.error_empty_password));
                    } else {
                        tilEtPassword.setError(null);
                        tilEtPassword.setErrorEnabled(false);
                    }
                    confirmPaymentData.setPasswordDeposit(s.toString());
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (resId) {
                case R.id.remark:
                    confirmPaymentData.setComments(s.toString());
                    break;
                case R.id.nama_penyetor:
                    confirmPaymentData.setDepositor(s.toString());
                    break;
                case R.id.input_total_payment:
                    confirmPaymentData.setPaymentAmount(s.toString());
                    break;
                case R.id.account_owner:
                    confirmPaymentData.setBankAccountName(s.toString());
                    break;
                case R.id.account_number:
                    confirmPaymentData.setBankAccountNumber(s.toString());
                    break;
                case R.id.branch:
                    confirmPaymentData.setBankAccountBranch(s.toString());
                    break;
                case R.id.password:
                    confirmPaymentData.setPasswordDeposit(s.toString());
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @OnClick(R.id.check_account)
    void actionShowSysAccount() {
        TokopediaBankAccount.createShowAccountDialog(this);
    }
}
