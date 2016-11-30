package com.tokopedia.core.manage.people.bank.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.bank.ManagePeopleBankConstant;
import com.tokopedia.core.manage.people.bank.adapter.BankAdapter;
import com.tokopedia.core.manage.people.bank.listener.ManagePeopleBankFragmentView;
import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;
import com.tokopedia.core.manage.people.bank.presenter.ManagePeopleBankFragmentPresenter;
import com.tokopedia.core.manage.people.bank.presenter.ManagePeopleBankFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.SessionHandler;

import butterknife.BindView;

/**
 * Created by Nisie on 6/10/16.
 */
public class ManagePeopleBankFragment extends BasePresenterFragment<ManagePeopleBankFragmentPresenter>
        implements ManagePeopleBankFragmentView, ManagePeopleBankConstant {

    public interface DoActionListener {
        void deleteBank(Bundle param);

        void defaultBank(Bundle param);

    }

    public interface BankFormListener {
        void onAddBank();

        void onEditBank(ActSettingBankPass pass);
    }

    @BindView(R2.id.bank_list)
    RecyclerView bankList;

    BankAdapter adapter;
    SnackbarRetry snackbarRetry;
    BankFormListener listener;
    TkpdProgressDialog progressDialog;

    public static ManagePeopleBankFragment createInstance(Bundle extras) {
        ManagePeopleBankFragment fragment = new ManagePeopleBankFragment();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manage_people_bank, menu);
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initData();
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
        presenter = new ManagePeopleBankFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manage_people_bank;
    }

    @Override
    protected void initView(View view) {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        bankList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = BankAdapter.createInstance(getActivity(), presenter);
        bankList.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        bankList.setEnabled(isEnabled);
        setHasOptionsMenu(isEnabled);
    }

    @Override
    public void showLoading() {
        if (adapter.getList().size() == 0)
            adapter.showLoadingFull(true);
        else
            adapter.showLoading(true);
    }

    @Override
    public BankAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void showEmptyState(String message, NetworkErrorHelper.RetryClickedListener listener) {
        if (!message.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, listener);
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);

    }

    @Override
    public void showSnackbar(String message, NetworkErrorHelper.RetryClickedListener listener) {
        if (!message.equals(""))
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, listener);
        else
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), listener);
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void finishLoading() {
        adapter.showLoadingFull(false);
        progressDialog.dismiss();
    }

    @Override
    public void setOnActionBankListener(BankFormListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSuccessEditDefaultBankAccount(Bundle resultData) {
        finishLoading();
        setActionsEnabled(true);
        final ActSettingBankPass pass = resultData.getParcelable(PARAM_DEFAULT_BANK_ACCOUNT);

        if (pass != null && !resultData.getString(EXTRA_SUCCESS, "").equals("")) {
            SnackbarManager.make(getActivity(), resultData.getString(EXTRA_SUCCESS, ""), Snackbar.LENGTH_LONG).show();
            adapter.setDefaultBank(pass.getPosition());
        }
    }

    @Override
    public void onSuccessDeleteBankAccount(Bundle resultData) {
        finishLoading();
        setActionsEnabled(true);
        final ActSettingBankPass pass = resultData.getParcelable(PARAM_DELETE_BANK_ACCOUNT);

        if (pass != null && !resultData.getString(EXTRA_SUCCESS, "").equals("")) {
            SnackbarManager.make(getActivity(), resultData.getString(EXTRA_SUCCESS, ""), Snackbar.LENGTH_LONG).show();
            adapter.getList().remove(pass.getPosition());
            if (adapter.getList().size() == 0)
                adapter.showEmpty(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailedEditDefaultBankAccount(final Bundle resultData) {
        final ActSettingBankPass pass = resultData.getParcelable(PARAM_DEFAULT_BANK_ACCOUNT);
        finishLoading();
        if (pass != null && !resultData.getString(EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(EXTRA_ERROR);
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.onDefaultBank(pass);
                }
            }).showRetrySnackbar();
        } else {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.onDefaultBank(pass);
                }
            }).showRetrySnackbar();
        }
    }

    @Override
    public void onFailedDeleteBankAccount(Bundle resultData) {
        final ActSettingBankPass pass = resultData.getParcelable(PARAM_DELETE_BANK_ACCOUNT);

        finishLoading();
        if (pass != null && !resultData.getString(EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(EXTRA_ERROR);
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.onDeleteBank(pass);
                }
            }).showRetrySnackbar();
        } else {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.onDeleteBank(pass);
                }
            }).showRetrySnackbar();
        }
    }

    @Override
    public void refresh() {
        adapter.getList().clear();
        showLoading();
        presenter.initData();
    }

    @Override
    public void showDialogLoading() {
        if (progressDialog.isProgress())
            progressDialog.dismiss();
        progressDialog.showDialog();
    }

    @Override
    public BankFormListener getBankFormListener() {
        return listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_bank) {
            if (adapter.getList().size() < 10) {
                getActivity().getIntent().putExtra(PARAM_IS_PHONE_VERIFIED, SessionHandler.isMsisdnVerified());
                listener.onAddBank();
            } else
                SnackbarManager.make(getActivity(), getString(R.string.error_max_account), Snackbar.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else {
            return false;
        }
    }


}
