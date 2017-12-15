package com.tokopedia.core.deposit.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManager;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.deposit.fragment.WithdrawFragment;
import com.tokopedia.core.deposit.interactor.WithdrawCacheInteractor;
import com.tokopedia.core.deposit.interactor.WithdrawCacheInteractorImpl;
import com.tokopedia.core.deposit.interactor.WithdrawRetrofitInteractor;
import com.tokopedia.core.deposit.interactor.WithdrawRetrofitInteractorImpl;
import com.tokopedia.core.deposit.listener.WithdrawFragmentView;
import com.tokopedia.core.deposit.model.DoWithdrawParam;
import com.tokopedia.core.deposit.model.WithdrawForm;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.analytics.UnifyTracking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nisie on 4/13/16.
 */
public class WithdrawFragmentPresenterImpl implements WithdrawFragmentPresenter {

    private static final double MIN_WITHDRAW = 5000;
    private static final String SEND_OTP_WITHDRAWAL_CACHE_KEY = "SEND_OTP_WITHDRAWAL";
    private static final int EXPIRE_TIME = 30;
    private static final java.lang.String EXPIRED_TIME_KEY = "expired_time";
    private static final java.lang.String TIMESTAMP_KEY = "timestamp";

    WithdrawFragmentView viewListener;
    WithdrawRetrofitInteractor networkInteractor;
    WithdrawCacheInteractor cacheInteractor;
    LocalCacheHandler handler;
    LocalCacheHandler bankCache;
    DataManager dataManager;

    public WithdrawFragmentPresenterImpl(WithdrawFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new WithdrawRetrofitInteractorImpl();
        this.cacheInteractor = new WithdrawCacheInteractorImpl();
        this.handler = new LocalCacheHandler(viewListener.getActivity(),
                SEND_OTP_WITHDRAWAL_CACHE_KEY);
        this.dataManager = DataManagerImpl.getDataManager();
        this.bankCache = new LocalCacheHandler(viewListener.getActivity(),
                HomeRouter.TAG_FETCH_BANK);
    }

    @Override
    public void getBankList() {
        viewListener.showLoading();
        viewListener.disableView();

        if (bankCache.isExpired() || getListBankFromDB("").size() == 0) {
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
                    LocalCacheHandler cache = new LocalCacheHandler(
                            MainApplication.getAppContext(), HomeRouter.TAG_FETCH_BANK
                    );

                    cache.setExpire(86400);
                    cache.applyEditor();
                    getWithdrawForm();
                }

                @Override
                public void setShippingCity(List<District> districts) {

                }

                @Override
                public void onNetworkError(String message) {
                    if (viewListener != null && viewListener.getActivity() != null) {
                        viewListener.finishLoading();
                        viewListener.showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getBankList();
                            }
                        });
                    }

                }

                @Override
                public void onMessageError(String message) {
                    if (viewListener != null && viewListener.getActivity() != null) {
                        viewListener.finishLoading();
                        viewListener.showEmptyState(message, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getBankList();
                            }
                        });
                    }
                }

                @Override
                public void onUnknownError(String message) {
                    if (viewListener != null && viewListener.getActivity() != null) {
                        viewListener.finishLoading();
                        viewListener.showEmptyState(message, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getBankList();
                            }
                        });
                    }
                }

                @Override
                public void onTimeout() {
                    if (viewListener != null && viewListener.getActivity() != null) {

                        viewListener.finishLoading();
                        viewListener.showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getBankList();
                            }
                        });
                    }
                }

                @Override
                public void onFailAuth() {
                }
            });
        } else {
            getWithdrawForm();
        }
    }

    private void getWithdrawForm() {
        viewListener.showLoading();
        viewListener.disableView();
        networkInteractor.getWithdrawForm(viewListener.getActivity(), new HashMap<String, String>(), new WithdrawRetrofitInteractor.WithdrawFormListener() {
            @Override
            public void onSuccess(@NonNull WithdrawForm data) {
                if (viewListener != null && viewListener.getActivity() != null) {

                    viewListener.finishLoading();
                    viewListener.enableView();
                    viewListener.setForm(data);
                    viewListener.getAdapter().setList(data.getBankAccount());
                    cacheInteractor.setWithdrawFormCache(data);
                }
            }

            @Override
            public void onTimeout(String message) {
                if (viewListener != null && viewListener.getActivity() != null) {

                    viewListener.finishLoading();
                    viewListener.showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getWithdrawForm();
                        }
                    });
                }

            }

            @Override
            public void onError(String error) {
                if (viewListener != null && viewListener.getActivity() != null) {

                    viewListener.finishLoading();
                    viewListener.showEmptyState(error, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getWithdrawForm();
                        }
                    });
                }
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoNetworkConnection() {
                if (viewListener != null && viewListener.getActivity() != null) {

                    viewListener.finishLoading();
                    viewListener.showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getWithdrawForm();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void doWithdraw() {
        viewListener.disableView();
        viewListener.showProgressDialog();
        networkInteractor.doWithdraw(viewListener.getActivity(), getDoWithdrawParam(), new WithdrawRetrofitInteractor.DoWithdrawListener() {
            @Override
            public void onSuccess() {
                if (viewListener != null && viewListener.getActivity() != null) {

                    UnifyTracking.eventDepositWithdraw();
                    viewListener.finishLoading();
                    viewListener.enableView();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("withdraw", viewListener.getTotalWithdrawal().getText().toString());
                    viewListener.getActivity().setResult(Activity.RESULT_OK, intent);
                    viewListener.getActivity().finish();
                }
            }

            @Override
            public void onTimeout(String message) {
                if (viewListener != null && viewListener.getActivity() != null) {

                    viewListener.finishLoading();
                    viewListener.enableView();
                    viewListener.setError("");
                }
            }

            @Override
            public void onError(String error) {
                if (viewListener != null && viewListener.getActivity() != null) {

                    viewListener.finishLoading();
                    viewListener.enableView();
                    viewListener.setError(error);
                }
            }

            @Override
            public void onNullData() {
                if (viewListener != null && viewListener.getActivity() != null) {
                    viewListener.finishLoading();
                }
            }

            @Override
            public void onNoNetworkConnection() {
                if (viewListener != null && viewListener.getActivity() != null) {

                    viewListener.finishLoading();
                    viewListener.enableView();
                    viewListener.setError("");
                }
            }
        });
    }

    private Map<String, String> getDoWithdrawParam() {
        DoWithdrawParam param = new DoWithdrawParam();
        param.setWithdrawAmount(viewListener.getTotalWithdrawal().getText().toString().replace(",", ""));
        param.setUserPassword(viewListener.getPassword().getText().toString());
        if (viewListener.getOTP().getVisibility() == View.VISIBLE) {
            param.setOtpCode(viewListener.getOTP().getText().toString());
        }
        if (viewListener.getBankForm().getVisibility() == View.VISIBLE) {
            param.setBankAccountName(viewListener.getAccountName().getText().toString());
            param.setBankAccountNumber(viewListener.getAccountNumber().getText().toString());
            param.setBankName(viewListener.getBankName().getText().toString());
            param.setBankId(viewListener.getBankId());
            param.setBankBranch(viewListener.getBranchName().getText().toString());
        } else {
            param.setBankAccountId(viewListener.getAdapter().getList().get(
                    viewListener.getBankList().getSelectedItemPosition() - 1).getBankAccountId()
            );

        }

        return param.getParamDoWithdraw();
    }

    @Override
    public void onConfirmClicked() {
        if (isValid()) {
            doWithdraw();
        }
    }

    @Override
    public void onBankListSelected(final int position) {
        cacheInteractor.getWithdrawFormCache(new WithdrawCacheInteractor.GetWithdrawFormCacheListener() {
            @Override
            public void onSuccess(WithdrawForm withdrawForm) {
                if (isAddNewBank(position)) {
                    viewListener.getBankForm().setVisibility(View.VISIBLE);
                    viewListener.getOTPArea().setVisibility(View.VISIBLE);
                } else {
                    viewListener.getBankForm().setVisibility(View.GONE);
                    if (position > 0) {
                        if (withdrawForm.isMsisdnVerified() == 1 && withdrawForm.getBankAccount().get(position - 1).isVerifiedAccount()) {
                            viewListener.getOTPArea().setVisibility(View.GONE);
                        } else {
                            viewListener.getOTPArea().setVisibility(View.VISIBLE);
                        }
                    } else {
                        viewListener.getOTPArea().setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

    @Override
    public void sendOTP() {
        if (handler.isExpired()) {
            AppUtils.sendOTP(viewListener.getActivity());
            viewListener.getOTP().setEnabled(true);
            handler.setExpire(EXPIRE_TIME);
        } else {
            Long diff = System.currentTimeMillis() / 1000 - handler.getLong(TIMESTAMP_KEY);
            int interval = handler.getInt(EXPIRED_TIME_KEY);
            int remainingTime = interval - diff.intValue();
            CommonUtils.UniversalToast(viewListener.getActivity(), "Silahkan coba " + remainingTime + " detik lagi");
        }
    }

    @Override
    public List<Bank> getListBankFromDB(String query) {
        return DbManagerImpl.getInstance().getListBankFromDB(query);
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    private boolean isAddNewBank(int position) {
        return position == viewListener.getAdapter().getList().size() + 1;
    }

    private boolean isValid() {
        boolean Valid = true;
        viewListener.removeError();

        if (viewListener.getPassword().length() == 0) {
            viewListener.notifyError(viewListener.getPasswordWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
            Valid = false;
        }

        if (viewListener.getOTPArea().getVisibility() == View.VISIBLE) {
            if (viewListener.getOTP().length() == 0) {
                viewListener.notifyError(viewListener.getOTPWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
                Valid = false;
            } else if (viewListener.getOTP().length() > 6) {
                viewListener.notifyError(viewListener.getOTPWrapper(), viewListener.getActivity().getString(R.string.error_max_otp));
                Valid = false;
            } else if (viewListener.getOTP().length() < 6) {
                viewListener.notifyError(viewListener.getOTPWrapper(), viewListener.getActivity().getString(R.string.error_min_otp));
                Valid = false;
            }
        }

        if (viewListener.getBankForm().getVisibility() == View.VISIBLE) {
            if (viewListener.getAccountName().length() == 0) {
                viewListener.notifyError(viewListener.getAccountNameWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
                Valid = false;
            }
            if (viewListener.getAccountNumber().length() == 0) {
                viewListener.notifyError(viewListener.getAccountNumberWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
                viewListener.getAccountNumber().requestFocus();
                Valid = false;
            } else if (viewListener.getAccountNumber().length() > 30) {
                viewListener.notifyError(viewListener.getAccountNumberWrapper(), viewListener.getActivity().getString(R.string.error_max_account_number));
                Valid = false;
            }

            if (viewListener.getBranchName().length() == 0) {
                viewListener.notifyError(viewListener.getBranchNameWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
                Valid = false;
            }
        }

        if (viewListener.getBankList().getSelectedItemPosition() == 0) {
            viewListener.setError(viewListener.getActivity().getString(R.string.error_bank_not_selected));
            Valid = false;
        }

        if (viewListener.getTotalWithdrawal().length() == 0) {
            viewListener.notifyError(viewListener.getTotalWithdrawalWrapper(), viewListener.getActivity().getString(R.string.error_field_required));
            Valid = false;
        } else {
            if (Double.parseDouble(viewListener.getTotalWithdrawal().getText().toString().replace(",", "")) < MIN_WITHDRAW) {
                viewListener.notifyError(viewListener.getTotalWithdrawalWrapper(), viewListener.getActivity().getString(R.string.error_min_withdraw));
                Valid = false;
            }
            if (Double.parseDouble(viewListener.getTotalWithdrawal().getText().toString().replace(",", "")) >
                    Integer.parseInt(viewListener.getArguments().getString(DepositFragmentPresenterImpl.BUNDLE_TOTAL_BALANCE_INT))) {
                viewListener.notifyError(viewListener.getTotalWithdrawalWrapper(), viewListener.getActivity().getString(R.string.error_not_enough));
                Valid = false;
            }
        }

        return Valid;
    }


}
