package com.tokopedia.loginregister.activation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.activation.di.DaggerActivationComponent;
import com.tokopedia.loginregister.activation.view.listener.ChangeEmailContract;
import com.tokopedia.loginregister.activation.view.presenter.ChangeEmailPresenter;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.loginregister.activation.domain.usecase.ChangeEmailUseCase.PARAM_NEW_EMAIL;
import static com.tokopedia.loginregister.activation.domain.usecase.ChangeEmailUseCase.PARAM_OLD_EMAIL;
import static com.tokopedia.loginregister.activation.domain.usecase.ChangeEmailUseCase.PARAM_PASSWORD;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailFragment extends BaseDaggerFragment implements ChangeEmailContract.View {

    public static final int ACTION_CHANGE_EMAIL = 111;
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    EditText oldEmailEditText;
    EditText newEmailEditText;
    EditText passwordEditText;
    TextView changeEmailButton;
    View mainView;
    ProgressBar progressBar;

    @Inject
    ChangeEmailPresenter presenter;

    public static ChangeEmailFragment createInstance(Bundle bundle) {
        ChangeEmailFragment fragment = new ChangeEmailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerActivationComponent daggetActivationComponent = (DaggerActivationComponent)
                DaggerActivationComponent
                        .builder().loginRegisterComponent(getComponent(LoginRegisterComponent.class))
                        .build();

        daggetActivationComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_email_activate, container, false);
        oldEmailEditText = view.findViewById(R.id.old_email);
        newEmailEditText = view.findViewById(R.id.new_email);
        passwordEditText = view.findViewById(R.id.password);
        changeEmailButton = view.findViewById(R.id.change_email_button);
        progressBar = view.findViewById(R.id.progress_bar);
        mainView = view.findViewById(R.id.main_view);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null)
            oldEmailEditText.setText(getArguments().getString(ApplinkConstInternalGlobal.PARAM_EMAIL, ""));

        changeEmailButton.setOnClickListener(v -> {
            KeyboardHandler.DropKeyboard(getActivity(), getView());
            presenter.changeEmail(
                    oldEmailEditText.getText().toString(),
                    newEmailEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PARAM_OLD_EMAIL, oldEmailEditText.getText().toString());
        outState.putString(PARAM_NEW_EMAIL, newEmailEditText.getText().toString());
        outState.putString(PARAM_PASSWORD, passwordEditText.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            oldEmailEditText.setText(savedInstanceState.getString(PARAM_OLD_EMAIL, ""));
            newEmailEditText.setText(savedInstanceState.getString(PARAM_NEW_EMAIL, ""));
            passwordEditText.setText(savedInstanceState.getString(PARAM_PASSWORD, ""));
        }
    }

    @Override
    public void onErrorChangeEmail(String message) {
        finishLoadingProgress();
        ToasterError.make(getView(), message).show();
    }

    @Override
    public void onSuccessChangeEmail() {
        if (getActivity() != null) {
            finishLoadingProgress();
            Intent intent = getActivity().getIntent();
            intent.putExtra(EXTRA_EMAIL,
                    newEmailEditText.getText().toString());
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
        }
    }

    private void finishLoadingProgress() {
        progressBar.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }

    @Override
    public void setPasswordError(String errorMessage) {
        passwordEditText.setError(errorMessage);
        passwordEditText.requestFocus();
    }

    @Override
    public void setNewEmailError(String errorMessage) {
        newEmailEditText.setError(errorMessage);
        newEmailEditText.requestFocus();
    }

    @Override
    public void setOldEmailError(String errorMessage) {
        oldEmailEditText.setError(errorMessage);
        oldEmailEditText.requestFocus();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}