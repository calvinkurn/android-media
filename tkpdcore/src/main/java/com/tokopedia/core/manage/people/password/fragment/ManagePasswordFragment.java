package com.tokopedia.core.manage.people.password.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.password.intentservice.ManagePasswordIntentService;
import com.tokopedia.core.manage.people.password.model.ChangePasswordParam;
import com.tokopedia.core.manage.people.password.presenter.ManagePasswordFragmentPresenter;
import com.tokopedia.core.manage.people.password.presenter.ManagePasswordFragmentPresenterImpl;
import com.tokopedia.core.customView.PasswordView;

import butterknife.BindView;

public class ManagePasswordFragment extends BasePresenterFragment<ManagePasswordFragmentPresenter>
                                        implements ManagePasswordFragmentView{

    @BindView(R2.id.old_password)
    PasswordView oldPassword;
    @BindView(R2.id.new_password)
    PasswordView newPassword;
    @BindView(R2.id.new_password_confirmation)
    PasswordView confPassword;
    @BindView(R2.id.wrapper_old)
    TextInputLayout wrapperOld;
    @BindView(R2.id.wrapper_new)
    TextInputLayout wrapperNew;
    @BindView(R2.id.wrapper_conf)
    TextInputLayout wrapperConf;
    @BindView(R2.id.save_button)
    TextView saveButton;

    private TkpdProgressDialog progress;


    public static Fragment newInstance() {
        ManagePasswordFragment fragment = new ManagePasswordFragment();
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManagePasswordFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manage_password;
    }

    @Override
    protected void initView(View view) {
        progress = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        oldPassword.addTextChangedListener(watcher(wrapperOld));
        newPassword.addTextChangedListener(watcher(wrapperNew));
        confPassword.addTextChangedListener(watcher(wrapperConf));
    }

    @Override
    protected void setViewListener() {
        saveButton.setOnClickListener(changePasswordListener());
    }

    private View.OnClickListener changePasswordListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    presenter.changePassword(getActivity(),getParam());
                }
            }
        };
    }

    private boolean validateInput() {

        boolean status;
        setWrapperError(wrapperConf,null);
        setWrapperError(wrapperNew,null);
        setWrapperError(wrapperOld,null);

        status = checkContent(confPassword, wrapperConf)
                        & checkContent(newPassword, wrapperNew)
                        & checkContent(oldPassword, wrapperOld);

        return status;
    }

    private boolean checkContent(PasswordView view, TextInputLayout wrapper) {
        if(TextUtils.isEmpty(view.getText().toString())){
            setWrapperError(wrapper, getString(R.string.error_field_required));
            view.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    private void setWrapperError(TextInputLayout wrapper, String s) {
        if(s == null) {
            wrapper.setError(s);
            wrapper.setErrorEnabled(false);
        }else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }


    private ChangePasswordParam getParam() {
        ChangePasswordParam param = new ChangePasswordParam();
        param.setOldPassword(oldPassword.getText().toString());
        param.setNewPassword(newPassword.getText().toString());
        param.setConfPassword(confPassword.getText().toString());
        return param;
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showProgress() {
        progress.showDialog();
    }

    @Override
    public void dismissProgress() {
        if(progress.isProgress()){
            progress.dismiss();
        }
    }

    @Override
    public void dismissKeyboard() {
        KeyboardHandler.DropKeyboard(context,oldPassword);
    }

    private void showSnackbar(String s) {
        SnackbarManager.make(getActivity(),s, Snackbar.LENGTH_LONG).show();
    }

    public void onSuccessAction(Bundle resultData) {
        dismissProgress();
        showSnackbar(resultData.getString(ManagePasswordIntentService.EXTRA_RESULT));
    }

    public void onErrorAction(Bundle resultData) {
        dismissProgress();
        showSnackbar(resultData.getString(ManagePasswordIntentService.EXTRA_RESULT));
    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }
}
