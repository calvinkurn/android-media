package com.tokopedia.core.manage.people.bank.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tokopedia.core.R;
import com.tokopedia.core.manage.people.bank.ManagePeopleBankConstant;
import com.tokopedia.core.manage.people.bank.interactor.ManageBankActRetrofitInteractor;
import com.tokopedia.core.manage.people.bank.interactor.ManageBankActRetrofitInteractorImpl;
import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;

/**
 * Created by Nisie on 6/13/16.
 */
public class ManagePeopleBankIntentService extends IntentService
        implements ManagePeopleBankConstant {

    public ManagePeopleBankIntentService() {
        super(INTENT_NAME);
        networkInteractor = new ManageBankActRetrofitInteractorImpl();
    }

    private ManageBankActRetrofitInteractor networkInteractor;

    public static void startAction(Context context, Bundle bundle,
                                   ManageBankResultReceiver receiver, int type) {
        Intent intent = new Intent(context, ManagePeopleBankIntentService.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(EXTRA_TYPE, 0);
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            switch (action) {
                case ACTION_ADD_BANK_ACCOUNT:
                    handleAddBankAccount(bundle, receiver);
                    break;
                case ACTION_EDIT_BANK_ACCOUNT:
                    handleEditBankAccount(bundle, receiver);
                    break;
                case ACTION_DELETE_BANK_ACCOUNT:
                    handleDeleteBankAccount(bundle, receiver);
                    break;
                case ACTION_EDIT_DEFAULT_BANK_ACCOUNT:
                    handleEditDefaultBankAccount(bundle, receiver);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }

        }
    }

    private void handleAddBankAccount(Bundle bundle, final ResultReceiver receiver) {
        ActSettingBankPass param = bundle.getParcelable(PARAM_ADD_BANK_ACCOUNT);

        if (param != null) {
            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_ADD_BANK_ACCOUNT);

            networkInteractor.addBank(getBaseContext(),
                    param.getParamAddBankAccount(),
                    new ManageBankActRetrofitInteractor.ActBankAccountListener() {
                        @Override
                        public void onSuccess(String status) {
                            resultData.putString(EXTRA_SUCCESS, status);
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout(String message) {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoNetworkConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleEditBankAccount(Bundle bundle, final ResultReceiver receiver) {
        ActSettingBankPass param = bundle.getParcelable(PARAM_EDIT_BANK_ACCOUNT);

        if (param != null) {
            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_EDIT_BANK_ACCOUNT);

            networkInteractor.editBank(getBaseContext(),
                    param.getParamEditBankAccount(),
                    new ManageBankActRetrofitInteractor.ActBankAccountListener() {
                        @Override
                        public void onSuccess(String status) {
                            resultData.putString(EXTRA_SUCCESS, status);
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout(String message) {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoNetworkConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleDeleteBankAccount(Bundle bundle, final ResultReceiver receiver) {
        ActSettingBankPass param = bundle.getParcelable(PARAM_DELETE_BANK_ACCOUNT);

        if (param != null) {
            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_DELETE_BANK_ACCOUNT);
            resultData.putParcelable(PARAM_DELETE_BANK_ACCOUNT, param);

            networkInteractor.deleteBank(getBaseContext(),
                    param.getParamDeleteBankAccount(),
                    new ManageBankActRetrofitInteractor.ActBankAccountListener() {
                        @Override
                        public void onSuccess(String status) {
                            resultData.putString(EXTRA_SUCCESS, status);
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout(String message) {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoNetworkConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleEditDefaultBankAccount(Bundle bundle, final ResultReceiver receiver) {
        ActSettingBankPass param = bundle.getParcelable(PARAM_DEFAULT_BANK_ACCOUNT);

        if (param != null) {
            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_EDIT_DEFAULT_BANK_ACCOUNT);
            resultData.putParcelable(PARAM_DEFAULT_BANK_ACCOUNT, param);

            networkInteractor.defaultBank(getBaseContext(),
                    param.getParamDefaultBankAccount(),
                    new ManageBankActRetrofitInteractor.ActBankAccountListener() {
                        @Override
                        public void onSuccess(String status) {
                            resultData.putString(EXTRA_SUCCESS, status);
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout(String message) {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoNetworkConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

}
