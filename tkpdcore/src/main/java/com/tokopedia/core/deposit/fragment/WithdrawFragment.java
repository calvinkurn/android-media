package com.tokopedia.core.deposit.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.deposit.adapter.BankAdapter;
import com.tokopedia.core.deposit.adapter.BankDialogAdapter;
import com.tokopedia.core.deposit.listener.WithdrawFragmentView;
import com.tokopedia.core.deposit.model.WithdrawForm;
import com.tokopedia.core.deposit.presenter.DepositFragmentPresenterImpl;
import com.tokopedia.core.deposit.presenter.WithdrawFragmentPresenter;
import com.tokopedia.core.deposit.presenter.WithdrawFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Nisie on 3/30/16.
 */
public class WithdrawFragment extends BasePresenterFragment<WithdrawFragmentPresenter>
        implements WithdrawFragmentView {

    private static final String DEFAULT_TOTAL_BALANCE = "Rp.0,-";
    private static final String DEFAULT_TOTAL_WITHDRAWAL = "0";

    @BindView(R2.id.wrapper_total_balance)
    TextInputLayout wrapperTotalBalance;

    @BindView(R2.id.wrapper_total_withdrawal)
    TextInputLayout wrapperTotalWithdrawal;

    @BindView(R2.id.wrapper_account_name)
    TextInputLayout wrapperAccountName;

    @BindView(R2.id.wrapper_bank_branch)
    TextInputLayout wrapperBankBranch;

    @BindView(R2.id.wrapper_account_number)
    TextInputLayout wrapperAccountNumber;

    @BindView(R2.id.wrapper_password)
    TextInputLayout wrapperPassword;

    @BindView(R2.id.wrapper_otp)
    TextInputLayout wrapperCodeOTP;

    @BindView(R2.id.send_otp)
    TextView sendOTP;

    @BindView(R2.id.otp)
    EditText codeOTP;

    @BindView(R2.id.otp_view)
    View otpArea;

    @BindView(R2.id.total_balance)
    EditText totalBalance;

    @BindView(R2.id.total_withdrawal)
    EditText totalWithdrawal;

    @BindView(R2.id.bank_list)
    Spinner bankListView;

    @BindView(R2.id.password)
    EditText password;

    @BindView(R2.id.bank_name)
    TextView bankNameView;

    @BindView(R2.id.add_bank_form)
    LinearLayout bankForm;

    @BindView(R2.id.account_name)
    EditText accountName;

    @BindView(R2.id.account_number)
    EditText accountNumber;

    @BindView(R2.id.bank_branch)
    EditText branchName;

    @BindView(R2.id.main_view)
    View mainView;

    @BindView(R2.id.loading_layout)
    View loadingLayout;

    @BindView(R2.id.forgot_pass)
    TextView forgotPassText;

    TkpdProgressDialog progressDialog;
    BankAdapter bankAdapter;
    WithdrawFragmentPresenter presenter;
    List<Bank> listBank;
    Snackbar snackBar;
    BankDialogAdapter bankDialogadapter;

    public static WithdrawFragment createInstance(Bundle extras) {
        WithdrawFragment fragment = new WithdrawFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextWatcher();
    }

    private void setTextWatcher() {
        totalBalance.addTextChangedListener(watcher(wrapperTotalBalance));
        totalWithdrawal.addTextChangedListener(watcher(wrapperTotalWithdrawal));
        accountName.addTextChangedListener(watcher(wrapperAccountName));
        accountNumber.addTextChangedListener(watcher(wrapperAccountNumber));
        branchName.addTextChangedListener(watcher(wrapperBankBranch));
        codeOTP.addTextChangedListener(watcher(wrapperCodeOTP));
        password.addTextChangedListener(watcher(wrapperPassword));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getActivity().invalidateOptionsMenu();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.getBankList();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new WithdrawFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_withdraw;
    }

    @Override
    protected void initView(View view) {
        snackBar = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected void setViewListener() {
        bankListView.setOnItemSelectedListener(onBankListSelected());
        sendOTP.setOnClickListener(onSendOTPClicked());
        bankNameView.setOnClickListener(onBankNameClicked());
        forgotPassText.setOnClickListener(onGoToForgotPass());
    }

    private View.OnClickListener onGoToForgotPass() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
                    Intent intent = ((TkpdCoreRouter) getActivity().getApplication())
                            .getForgotPasswordIntent(getActivity(), "");
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save_btn_black, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_send) {
            KeyboardHandler.hideSoftKeyboard(getActivity());
            presenter.onConfirmClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onBankNameClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseBankName();
            }
        };
    }

    private void showDialogChooseBankName() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.choose_bank_dialog_2, null);
        alertDialogBuilder.setView(promptsView);

        final RecyclerView lvBank = (RecyclerView) promptsView.findViewById(R.id.lv_bank);
        listBank = presenter.getListBankFromDB("");
        bankDialogadapter = BankDialogAdapter.createAdapter(context, listBank);
        lvBank.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        lvBank.setAdapter(bankDialogadapter);

        final SearchView Search = (SearchView) promptsView.findViewById(R.id.search);
        Search.setIconified(false);
        Search.setIconifiedByDefault(false);
        Search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                listBank = presenter.getListBankFromDB(query);


                if (listBank.size() == 0) {
                    bankDialogadapter.showEmpty();
                } else {
                    bankDialogadapter.removeEmpty();
                }
                bankDialogadapter.setList(listBank);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    listBank = presenter.getListBankFromDB("");
                    bankDialogadapter.setList(listBank);
                }
                return false;
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        bankDialogadapter.setListener(new BankDialogAdapter.OnBankClickListener() {
            @Override
            public void onClick(int position) {
                bankNameView.setText(listBank.get(position).getBankName());
                bankDialogadapter.setSelectedBankId(position);
                alertDialog.dismiss();
            }
        });
    }

    private View.OnClickListener onSendOTPClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendOTP();
            }
        };
    }

    private AdapterView.OnItemSelectedListener onBankListSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onBankListSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    @Override
    protected void initialVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        bankAdapter = BankAdapter.createInstance(getActivity());
        bankListView.setAdapter(bankAdapter);

    }

    @Override
    protected void setActionVar() {
        totalBalance.setText(getArguments().getString(DepositFragmentPresenterImpl.BUNDLE_TOTAL_BALANCE, DEFAULT_TOTAL_BALANCE));
        totalWithdrawal.setText(getArguments().getString(DepositFragmentPresenterImpl.BUNDLE_TOTAL_BALANCE_INT, DEFAULT_TOTAL_WITHDRAWAL));

    }

    @Override
    public void showLoading() {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public BankAdapter getAdapter() {
        return bankAdapter;
    }

    @Override
    public void setForm(WithdrawForm data) {
        totalBalance.setText(data.getUseableDepositIdr());
        totalWithdrawal.setText(String.valueOf(data.getUseableDeposit()));
        if (data.isMsisdnVerified() == 0) {
            sendOTP.setText(getActivity().getString(R.string.title_otp_email));
        } else {
            sendOTP.setText(getActivity().getString(R.string.title_otp_phone));
        }
    }

    @Override
    public void removeError() {
        snackBar.dismiss();
        notifyError(wrapperTotalWithdrawal, null);
        notifyError(wrapperAccountName, null);
        notifyError(wrapperAccountNumber, null);
        notifyError(wrapperBankBranch, null);
        notifyError(wrapperPassword, null);
        notifyError(wrapperCodeOTP, null);

    }

    @Override
    public EditText getPassword() {
        return password;
    }

    @Override
    public EditText getOTP() {
        return codeOTP;
    }

    @Override
    public View getBankForm() {
        return bankForm;
    }

    @Override
    public EditText getAccountName() {
        return accountName;
    }

    @Override
    public EditText getAccountNumber() {
        return accountNumber;
    }

    @Override
    public Spinner getBankList() {
        return bankListView;
    }

    @Override
    public void setError(String error) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (error.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            snackBar = SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.title_close), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }
                    );
            snackBar.show();
        }

    }

    @Override
    public EditText getTotalWithdrawal() {
        return totalWithdrawal;
    }

    @Override
    public TextView getBankName() {
        return bankNameView;
    }

    @Override
    public EditText getBranchName() {
        return branchName;
    }

    @Override
    public View getOTPArea() {
        return otpArea;
    }

    @Override
    public void finishLoading() {
        if (!isAdded()) {
            return;
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void enableView() {
        mainView.setVisibility(View.VISIBLE);
        codeOTP.setEnabled(true);
        totalWithdrawal.setEnabled(true);
        bankListView.setEnabled(true);
        password.setEnabled(true);
        bankNameView.setEnabled(true);
        accountNumber.setEnabled(true);
        accountName.setEnabled(true);
        branchName.setEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void disableView() {
        codeOTP.setEnabled(false);
        totalWithdrawal.setEnabled(false);
        bankListView.setEnabled(false);
        password.setEnabled(false);
        bankNameView.setEnabled(false);
        accountNumber.setEnabled(false);
        accountName.setEnabled(false);
        branchName.setEnabled(false);
        setHasOptionsMenu(false);
    }

    @Override
    public TextInputLayout getTotalWithdrawalWrapper() {
        return wrapperTotalWithdrawal;
    }

    @Override
    public TextInputLayout getBranchNameWrapper() {
        return wrapperBankBranch;
    }

    @Override
    public TextInputLayout getAccountNumberWrapper() {
        return wrapperAccountNumber;
    }

    @Override
    public TextInputLayout getAccountNameWrapper() {
        return wrapperAccountName;
    }

    @Override
    public TextInputLayout getOTPWrapper() {
        return wrapperCodeOTP;
    }

    @Override
    public TextInputLayout getPasswordWrapper() {
        return wrapperPassword;
    }

    @Override
    public String getBankId() {
        return bankDialogadapter != null ? bankDialogadapter.getSelectedBankId() : "";
    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    notifyError(wrapper, null);
                }
                if (wrapper == wrapperAccountNumber) {
                    if (s.length() > 30)
                        notifyError(wrapper, getString(R.string.error_max_account_number));
                    else notifyError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void notifyError(TextInputLayout wrapper, String errorMessage) {
        wrapper.setError(errorMessage);
        if (errorMessage == null) wrapper.setErrorEnabled(false);
        wrapper.requestFocus();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void showEmptyState(NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
    }

    @Override
    public void showEmptyState(String message, NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, listener);
    }

    @Override
    public void setRetry(NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), listener).showRetrySnackbar();
    }

    @Override
    public void setRetry(String error, NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, listener).showRetrySnackbar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
