package com.tokopedia.home.account.presentation.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.di.component.AccountLogoutComponent;
import com.tokopedia.home.account.di.component.DaggerAccountLogoutComponent;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.presentation.presenter.DialogLogoutPresenter;
import com.tokopedia.home.account.presentation.view.DialogLogoutView;

import javax.inject.Inject;

public class DialogLogoutFragment extends DialogFragment implements DialogLogoutView{
    public static final String FRAGMENT_TAG = "DialogLogoutFragment";
    private Button okButton;

    @Inject DialogLogoutPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountLogoutComponent component = DaggerAccountLogoutComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication())
                        .getBaseAppComponent()).build();
        component.inject(this);
        presenter.attachView(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        return new AlertDialog.Builder(activity).setIcon(R.drawable.launch_screen)
                .setTitle(getString(R.string.logout)+" dari Tokopedia")
                .setMessage(R.string.logout_confirmation)
                .setPositiveButton(R.string.logout, null)
                .setNegativeButton(R.string.cancel, ((dialogInterface, i) -> dismiss()))
                .create();
    }

    @Override
    public void onResume() {
        AlertDialog alertDialog = (AlertDialog) getDialog();
        okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(view ->  {
            okButton.setClickable(false);
            presenter.doLogout();
        });
        super.onResume();
    }

    @Override
    public void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onErrorLogout(Throwable throwable) {
        okButton.setClickable(true);
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), throwable));
        dismiss();
    }

    @Override
    public void onSuccessLogout() {
        dismiss();
        if (getActivity().getApplication() instanceof AccountHomeRouter){
            ((AccountHomeRouter) getActivity().getApplication()).doLogoutAccount(getActivity());
        }
    }
}
