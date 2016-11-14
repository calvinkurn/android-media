package com.tokopedia.core.manage.people.bank.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManager;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.home.ParentIndexHome;
import com.tokopedia.core.manage.people.bank.ManagePeopleBankConstant;
import com.tokopedia.core.manage.people.bank.fragment.ManagePeopleBankFragment;
import com.tokopedia.core.manage.people.bank.interactor.ManageBankRetrofitInteractor;
import com.tokopedia.core.manage.people.bank.interactor.ManageBankRetrofitInteractorImpl;
import com.tokopedia.core.manage.people.bank.listener.ManagePeopleBankFragmentView;
import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;
import com.tokopedia.core.manage.people.bank.model.BankAccountPass;
import com.tokopedia.core.manage.people.bank.model.ManagePeopleBankResult;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.service.CheckVerification;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;

import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nisie on 6/10/16.
 */
public class ManagePeopleBankFragmentPresenterImpl implements ManagePeopleBankFragmentPresenter,
        ManagePeopleBankConstant{

    ManagePeopleBankFragmentView viewListener;
    ManageBankRetrofitInteractor networkInteractor;
    LocalCacheHandler bankCache;
    DataManager dataManager;
    PagingHandler pagingHandler;
    ManagePeopleBankFragment.DoActionListener listener;

    public ManagePeopleBankFragmentPresenterImpl(ManagePeopleBankFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ManageBankRetrofitInteractorImpl();
        this.bankCache = new LocalCacheHandler(viewListener.getActivity(), ParentIndexHome.FETCH_BANK);
        this.dataManager = DataManagerImpl.getDataManager();
        this.pagingHandler = new PagingHandler();
        this.listener = (ManagePeopleBankFragment.DoActionListener) viewListener.getActivity();

    }

    @Override
    public void initData() {

        if (bankCache.isExpired() || isListBankEmpty()) {
            getListBank();
        } else {
            getBankAccount();
        }


    }

    private void getListBank() {
        viewListener.showLoading();
        viewListener.setActionsEnabled(false);
        dataManager.getListBank(viewListener.getActivity(), new DataReceiver() {
            @Override
            public CompositeSubscription getSubscription() {
                return new CompositeSubscription();
            }

            @Override
            public void setDistricts(List<District> districts) {

            }

            @Override
            public void setCities(List<City> cities) {

            }

            @Override
            public void setProvinces(List<Province> provinces) {

            }

            @Override
            public void setBank(List<Bank> banks) {
                LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), ParentIndexHome.FETCH_BANK);
                cache.setExpire(86400);
                cache.applyEditor();
                getBankAccount();
            }

            @Override
            public void setDepartments(List<CategoryDB> departments) {

            }

            @Override
            public void setShippingCity(List<District> districts) {

            }

            @Override
            public void onNetworkError(String message) {
                viewListener.finishLoading();
                showError(message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getListBank();
                    }
                });
            }

            @Override
            public void onMessageError(String message) {
                viewListener.finishLoading();
                showError(message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getListBank();
                    }
                });
            }

            @Override
            public void onUnknownError(String message) {
                viewListener.finishLoading();
                showError(message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getListBank();
                    }
                });
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                showError(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getListBank();
                    }
                });
            }

            @Override
            public void onFailAuth() {
            }
        });
    }

    private void showError(NetworkErrorHelper.RetryClickedListener listener) {
        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.showEmptyState("", listener);
        } else {
            viewListener.showSnackbar("", listener);
        }
    }

    private void showError(String message, NetworkErrorHelper.RetryClickedListener listener) {
        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.showEmptyState(message, listener);
        } else {
            viewListener.showSnackbar(message, listener);
        }
    }

    private boolean isListBankEmpty() {
        return new Select().from(Bank.class).queryList().size() == 0;
    }

    private void getBankAccount() {
        viewListener.showLoading();
        viewListener.setActionsEnabled(false);
        networkInteractor.getBankAccountList(viewListener.getActivity(), getBankAccountParam(), new ManageBankRetrofitInteractor.GetBankAccountListener() {

            @Override
            public void onSuccess(@NonNull ManagePeopleBankResult data) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                setResult(data);
            }

            @Override
            public void onTimeout(String message) {
                viewListener.finishLoading();
                showError(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getBankAccount();
                    }
                });
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                showError(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getBankAccount();
                    }
                });
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.getAdapter().showEmpty(true);
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                showError(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getBankAccount();
                    }
                });
            }
        });
    }

    private void setResult(ManagePeopleBankResult data) {
        viewListener.getAdapter().showEmpty(false);
        viewListener.getAdapter().getList().addAll(data.getList());

        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.getAdapter().showEmpty(true);
        }

    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    @Override
    public void onEditBank(ActSettingBankPass pass) {
        LocalCacheHandler cache = new LocalCacheHandler(viewListener.getActivity(),
                CheckVerification.VERIFICATION_STATUS);
        viewListener.getActivity().getIntent().putExtra(PARAM_IS_PHONE_VERIFIED,
                SessionHandler.isMsisdnVerified());
        viewListener.getBankFormListener().onEditBank(pass);
    }

    @Override
    public void onDeleteBank(ActSettingBankPass pass) {
        viewListener.showDialogLoading();
        viewListener.setActionsEnabled(false);
        Bundle param = new Bundle();
        param.putParcelable(PARAM_DELETE_BANK_ACCOUNT, pass);
        listener.deleteBank(param);
    }

    @Override
    public void onDefaultBank(ActSettingBankPass pass) {
        viewListener.showDialogLoading();
        viewListener.setActionsEnabled(false);
        Bundle param = new Bundle();
        param.putParcelable(PARAM_DELETE_BANK_ACCOUNT, pass);
        listener.defaultBank(param);
    }

    public Map<String, String> getBankAccountParam() {
        BankAccountPass param = new BankAccountPass();
        param.setPage(String.valueOf(pagingHandler.getPage()));
        param.setprofileUserId(SessionHandler.getLoginID(viewListener.getActivity()));
        return param.getBankAccountParam();
    }
}
