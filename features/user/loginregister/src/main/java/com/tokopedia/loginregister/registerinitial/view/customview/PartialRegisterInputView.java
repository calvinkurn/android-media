package com.tokopedia.loginregister.registerinitial.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.loginregister.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author by alvinatin on 11/06/18.
 */

public class PartialRegisterInputView extends BaseCustomView {

    TkpdHintTextInputLayout wrapperEmailPhone;
    EditText etInputEmailPhone;
    TextView tvMessage;
    TextView tvError;
    TextView btnAction;

    TextInputEditText etPassword;
    TkpdHintTextInputLayout wrapperPassword;
    TextView btnForgotPassword;
    TextView btnChange;

    private PartialRegisterInputViewListener listener;

    public void setListener(PartialRegisterInputViewListener listener) {
        this.listener = listener;
    }

    public interface PartialRegisterInputViewListener {
        void onActionPartialClick(String id);
    }

    public PartialRegisterInputView(@NonNull Context context) {
        super(context);
        init();
    }

    public PartialRegisterInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartialRegisterInputView(@NonNull Context context, @Nullable AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.layout_partial_register_input, this);
        etInputEmailPhone = (EditText) view.findViewById(R.id.input_email_phone);
        etPassword = view.findViewById(R.id.password);
        tvMessage = view.findViewById(R.id.message);
        tvError = view.findViewById(R.id.error_message);
        btnAction = view.findViewById(R.id.register_btn);
        wrapperEmailPhone = view.findViewById(R.id.input_layout);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        btnForgotPassword = view.findViewById(R.id.forgot_pass);
        btnChange = view.findViewById(R.id.change_button);
        renderData();
    }

    public void renderData() {
        etInputEmailPhone.addTextChangedListener(watcher(wrapperEmailPhone));

        btnAction.setOnClickListener(new ClickRegister());
        btnChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefaultView();
            }
        });
    }

    public void onErrorValidate(String message) {
        setWrapperError(wrapperEmailPhone, message);
    }

    public void onErrorPassword(String message) {
        setWrapperError(wrapperPassword, message);
    }

    private void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(null);
            wrapper.setErrorEnabled(false);
            hideError();
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
            showError();
        }
    }

    private TextWatcher watcher(final TkpdHintTextInputLayout wrapper) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setWrapperError(wrapper, null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    public String getTextValue() {
        return etInputEmailPhone.getText().toString();
    }

    private class ClickRegister implements OnClickListener {

        @Override
        public void onClick(View v) {
            String id = etInputEmailPhone.getText().toString();
            listener.onActionPartialClick(id);
        }
    }

    private void hideError() {
        tvError.setVisibility(GONE);
    }

    private void showError() {
        tvError.setVisibility(VISIBLE);
    }

    public void showLoginEmailView(@NotNull String email) {
        wrapperPassword.setVisibility(View.VISIBLE);
        btnForgotPassword.setVisibility(View.VISIBLE);
        btnChange.setVisibility(View.VISIBLE);

        wrapperEmailPhone.setLabel(wrapperEmailPhone.getContext().getString(R.string.title_email));
        btnAction.setText(btnAction.getContext().getString(R.string.login));

        etInputEmailPhone.setText(email);
        etInputEmailPhone.setEnabled(false);

    }

    public void showDefaultView() {
        wrapperPassword.setVisibility(View.GONE);
        btnForgotPassword.setVisibility(View.GONE);
        btnChange.setVisibility(View.GONE);

        wrapperEmailPhone.setLabel(wrapperEmailPhone.getContext().getString(R.string.phone_or_email_input));
        etInputEmailPhone.setText("");
        etInputEmailPhone.setEnabled(true);

    }

    public void resetErrorWrapper() {
        setWrapperError(wrapperEmailPhone, null);
        setWrapperError(wrapperPassword, null);
    }

}
