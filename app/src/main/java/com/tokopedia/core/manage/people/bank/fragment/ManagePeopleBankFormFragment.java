package com.tokopedia.core.manage.people.bank.fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.deposit.adapter.BankDialogAdapter;
import com.tokopedia.core.manage.people.bank.ManagePeopleBankConstant;
import com.tokopedia.core.manage.people.bank.listener.ManagePeopleBankFormFragmentView;
import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;
import com.tokopedia.core.manage.people.bank.presenter.ManagePeopleBankFormFragmentPresenter;
import com.tokopedia.core.manage.people.bank.presenter.ManagePeopleBankFormFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Nisie on 6/13/16.
 */
public class ManagePeopleBankFormFragment extends BasePresenterFragment<ManagePeopleBankFormFragmentPresenter>
        implements ManagePeopleBankFormFragmentView, ManagePeopleBankConstant {

    public interface DoActionListener {
        void addBankAccount(Bundle param);
        void editBankAccount(Bundle param);
    }

    public interface FinishActionListener {
        void finishAction();
    }

    @Bind(R2.id.account_name_wrapper)
    TextInputLayout wrapperAccountName;

    @Bind(R2.id.bank_branch_wrapper)
    TextInputLayout wrapperBankBranch;

    @Bind(R2.id.account_number_wrapper)
    TextInputLayout wrapperAccountNumber;

    @Bind(R2.id.password_wrapper)
    TextInputLayout wrapperPassword;

    @Bind(R2.id.otp_wrapper)
    TextInputLayout wrapperCodeOTP;

    @Bind(R2.id.send_otp)
    TextView sendOTP;

    @Bind(R2.id.otp)
    EditText codeOTP;

    @Bind(R2.id.password)
    EditText password;

    @Bind(R2.id.bank_name)
    TextView bankNameView;

    @Bind(R2.id.account_name)
    EditText accountName;

    @Bind(R2.id.account_number)
    EditText accountNumber;

    @Bind(R2.id.bank_branch)
    EditText branchName;

    @Bind(R2.id.save_button)
    TextView saveButton;

    TkpdProgressDialog progressDialog;
    List<Bank> listBank;
    Snackbar snackBar;
    BankDialogAdapter bankDialogadapter;
    FinishActionListener listener;

    public static ManagePeopleBankFormFragment createInstance(Bundle extras) {
        ManagePeopleBankFormFragment fragment = new ManagePeopleBankFormFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        if (getArguments().getBoolean(PARAM_IS_PHONE_VERIFIED, false)) {
            sendOTP.setText(getActivity().getString(R.string.title_otp_phone));
        } else {
            sendOTP.setText(getActivity().getString(R.string.title_otp_email));
        }

        ActSettingBankPass editParam = getArguments().getParcelable(PARAM_EDIT_BANK_ACCOUNT);
        if(editParam != null){
            setEditParam(editParam);
        }
    }

    private void setEditParam(ActSettingBankPass editParam) {
        accountName.setText(editParam.getAccountName());
        accountNumber.setText(editParam.getAccountNumber());
        bankNameView.setText(editParam.getBankName());
        branchName.setText(editParam.getBankBranch());
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextWatcher();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManagePeopleBankFormFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_people_bank_form;
    }

    @Override
    protected void initView(View view) {
        snackBar = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_INDEFINITE);

    }

    @Override
    protected void setViewListener() {
        sendOTP.setOnClickListener(onSendOTPClicked());
        bankNameView.setOnClickListener(onBankNameClicked());
        saveButton.setOnClickListener(onSaveClicked());
    }

    private View.OnClickListener onSaveClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.onSaveClicked();
            }
        };
    }


    @Override
    protected void initialVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {

    }

    private void setTextWatcher() {
        accountName.addTextChangedListener(watcher(wrapperAccountName));
        accountNumber.addTextChangedListener(watcher(wrapperAccountNumber));
        branchName.addTextChangedListener(watcher(wrapperBankBranch));
        codeOTP.addTextChangedListener(watcher(wrapperCodeOTP));
        password.addTextChangedListener(watcher(wrapperPassword));
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

    @Override
    public void notifyError(TextInputLayout wrapper, String errorMessage) {
        wrapper.setError(errorMessage);
        if (errorMessage == null) wrapper.setErrorEnabled(false);
        wrapper.requestFocus();
    }

    @Override
    public void showDialogLoading() {
        if (progressDialog.isProgress())
            progressDialog.dismiss();
        progressDialog.showDialog();
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {

    }

    @Override
    public void finishLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessAddBankAccount(Bundle resultData) {
        finishLoading();
        setActionsEnabled(true);
        if (!resultData.getString(EXTRA_SUCCESS, "").equals("")) {
            SnackbarManager.make(getActivity(),resultData.getString(EXTRA_SUCCESS, ""),Snackbar.LENGTH_LONG).show();
        }
        listener.finishAction();
    }

    @Override
    public void onSuccessEditBankAccount(Bundle resultData) {
        finishLoading();
        setActionsEnabled(true);
        if (!resultData.getString(EXTRA_SUCCESS, "").equals("")) {
            SnackbarManager.make(getActivity(),resultData.getString(EXTRA_SUCCESS, ""),Snackbar.LENGTH_LONG).show();
        }
        listener.finishAction();

    }

    @Override
    public void onFailedAddBankAccount(Bundle resultData) {
        finishLoading();
        if (!resultData.getString(EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(EXTRA_ERROR);
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity());
        }

    }

    @Override
    public void onFailedEditBankAccount(Bundle resultData) {
        finishLoading();
        if (!resultData.getString(EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(EXTRA_ERROR);
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity());

        }
    }

    @Override
    public void setOnFinishActionListener(FinishActionListener listener) {
        this.listener = listener;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removeError() {
        snackBar.dismiss();
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
    public EditText getAccountName() {
        return accountName;
    }

    @Override
    public EditText getAccountNumber() {
        return accountNumber;
    }

    @Override
    public void setError(String error) {
        snackBar.setText(error).show();
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() !=null) {
            ScreenTracking.screen(this);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
