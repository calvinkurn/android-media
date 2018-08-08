package com.tokopedia.core.manage.people.bank.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.bank.ManagePeopleBankConstant;
import com.tokopedia.core.manage.people.bank.fragment.ManagePeopleBankFormFragment;
import com.tokopedia.core.manage.people.bank.fragment.ManagePeopleBankFragment;
import com.tokopedia.core.manage.people.bank.intentservice.ManageBankResultReceiver;
import com.tokopedia.core.manage.people.bank.intentservice.ManagePeopleBankIntentService;
import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;

/**
 * Created by Nisie on 6/10/16.
 */
public class ManagePeopleBankActivity extends BasePresenterActivity
        implements ManagePeopleBankFormFragment.DoActionListener,
        ManagePeopleBankFragment.DoActionListener,
        ManageBankResultReceiver.Receiver,
        ManagePeopleBankConstant {

    ManageBankResultReceiver mReceiver;

    public static Intent createInstance(Context context) {
        return new Intent(context, ManagePeopleBankActivity.class);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            ManagePeopleBankFragment fragment = ManagePeopleBankFragment.createInstance(bundle);
            fragment.setOnActionBankListener(OnActionBankForm());
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        } else if (getFragmentManager().findFragmentById(R.id.container) != null
                && getFragmentManager().findFragmentById(R.id.container).isAdded()) {
            finish();
            startActivity(getIntent());
        }
    }

    private ManagePeopleBankFragment.BankFormListener OnActionBankForm() {
        return new ManagePeopleBankFragment.BankFormListener() {
            @Override
            public void onAddBank() {
                Bundle bundle = getIntent().getExtras();
                if (bundle == null)
                    bundle = new Bundle();
                ManagePeopleBankFormFragment fragment = ManagePeopleBankFormFragment.createInstance(bundle);
                fragment.setOnFinishActionListener(onFinishAction());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
                transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
                transaction.addToBackStack("secondStack");
                transaction.commit();
            }

            @Override
            public void onEditBank(ActSettingBankPass pass) {
                Bundle bundle = getIntent().getExtras();
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putParcelable(PARAM_EDIT_BANK_ACCOUNT, pass);
                ManagePeopleBankFormFragment fragment = ManagePeopleBankFormFragment.createInstance(bundle);
                fragment.setOnFinishActionListener(onFinishAction());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
                transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
                transaction.addToBackStack("secondStack");
                transaction.commit();
            }
        };
    }

    private ManagePeopleBankFormFragment.FinishActionListener onFinishAction() {
        return new ManagePeopleBankFormFragment.FinishActionListener() {
            @Override
            public void finishAction() {
                getFragmentManager().popBackStack();
                invalidateOptionsMenu();
                ((ManagePeopleBankFragment) getFragmentManager().findFragmentByTag(ManagePeopleBankFragment.class.getSimpleName())).refresh();
            }
        };
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        mReceiver = new ManageBankResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PEOPLE_BANK;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            invalidateOptionsMenu();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void addBankAccount(Bundle param) {
        ManagePeopleBankIntentService.startAction(this,
                param, mReceiver, ACTION_ADD_BANK_ACCOUNT);
    }

    @Override
    public void editBankAccount(Bundle param) {
        ManagePeopleBankIntentService.startAction(this,
                param, mReceiver, ACTION_EDIT_BANK_ACCOUNT);
    }

    @Override
    public void deleteBank(Bundle param) {
        ManagePeopleBankIntentService.startAction(this,
                param, mReceiver, ACTION_DELETE_BANK_ACCOUNT);
    }

    @Override
    public void defaultBank(Bundle param) {
        ManagePeopleBankIntentService.startAction(this,
                param, mReceiver, ACTION_EDIT_DEFAULT_BANK_ACCOUNT);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(ManagePeopleBankIntentService.EXTRA_TYPE, 0);
        Fragment fragment = getFragment(type);

        if (fragment != null) {
            switch (resultCode) {
                case ManagePeopleBankIntentService.STATUS_SUCCESS:
                    onReceiveResultSuccess(fragment, resultData);
                    break;
                case ManagePeopleBankIntentService.STATUS_ERROR:
                    onReceiveResultError(fragment, resultData);
                    break;
            }
        }
    }

    private Fragment getFragment(int type) {
        switch (type) {
            case ACTION_ADD_BANK_ACCOUNT:
            case ACTION_EDIT_BANK_ACCOUNT:
                return getFragmentManager().findFragmentByTag(ManagePeopleBankFormFragment.class.getSimpleName());
            case ACTION_EDIT_DEFAULT_BANK_ACCOUNT:
            case ACTION_DELETE_BANK_ACCOUNT:
                return getFragmentManager().findFragmentByTag(ManagePeopleBankFragment.class.getSimpleName());
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(ManagePeopleBankIntentService.EXTRA_TYPE, 0);
        switch (type) {
            case ACTION_ADD_BANK_ACCOUNT:
                ((ManagePeopleBankFormFragment) fragment).onSuccessAddBankAccount(resultData);
                break;
            case ACTION_EDIT_BANK_ACCOUNT:
                ((ManagePeopleBankFormFragment) fragment).onSuccessEditBankAccount(resultData);
                break;
            case ACTION_EDIT_DEFAULT_BANK_ACCOUNT:
                ((ManagePeopleBankFragment) fragment).onSuccessFinishAction();
                break;
            case ACTION_DELETE_BANK_ACCOUNT:
                ((ManagePeopleBankFragment) fragment).onSuccessFinishAction();
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(ManagePeopleBankIntentService.EXTRA_TYPE, 0);
        switch (type) {
            case ACTION_ADD_BANK_ACCOUNT:
                ((ManagePeopleBankFormFragment) fragment).onFailedAddBankAccount(resultData);
                break;
            case ACTION_EDIT_BANK_ACCOUNT:
                ((ManagePeopleBankFormFragment) fragment).onFailedEditBankAccount(resultData);
                break;
            case ACTION_EDIT_DEFAULT_BANK_ACCOUNT:
                ((ManagePeopleBankFragment) fragment).onFailedEditDefaultBankAccount(resultData);
                break;
            case ACTION_DELETE_BANK_ACCOUNT:
                ((ManagePeopleBankFragment) fragment).onFailedDeleteBankAccount(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
