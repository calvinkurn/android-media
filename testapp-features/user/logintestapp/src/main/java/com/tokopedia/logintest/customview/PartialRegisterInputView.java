package com.tokopedia.logintest.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.logintest.R;
import com.tokopedia.logintest.common.PartialRegisterInputUtils;
import com.tokopedia.logintest.common.utils.KeyboardHandler;
import com.tokopedia.logintest.common.view.EmailExtension;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author by alvinatin on 11/06/18.
 */

public class PartialRegisterInputView extends BaseCustomView {

    TkpdHintTextInputLayout wrapperEmailPhone;
    AutoCompleteTextView etInputEmailPhone;
    TextView tvMessage;
    TextView tvError;
    ButtonCompat btnAction;
    EmailExtension emailExtension;

    TextInputEditText etPassword;
    TkpdHintTextInputLayout wrapperPassword;
    TextView btnForgotPassword;
    TextView btnChange;

    private static Boolean isButtonValidatorActived = false;
    private Boolean isExtensionSelected = false;

    private PartialRegisterInputViewListener listener;
    private List<String> emailExtensionList;

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
        View view = inflate(getContext(), R.layout.layout_testapp_partial_register_input, this);
        etInputEmailPhone = view.findViewById(R.id.input_email_phone);
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
        etInputEmailPhone.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            etInputEmailPhone.setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO);
        }
        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.bg_rounded_corner_autocomplete_partial_input);
        etInputEmailPhone.setDropDownBackgroundDrawable(background);
        etInputEmailPhone.setOnItemClickListener((parent, view, position, id) -> {

        });
        etInputEmailPhone.addTextChangedListener(watcher(wrapperEmailPhone));
        etInputEmailPhone.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(isValidValue(v.getText().toString()) && isButtonValidatorActived){
                    btnAction.performClick();
                }
                handled = true;
            }
            return handled;
        });

        etPassword.addTextChangedListener(watcher(wrapperPassword));

        btnAction.setOnClickListener(new ClickRegister());
        btnChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefaultView();

                hideEmailExtension();
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
                if(s != null) {
                    if (isButtonValidatorActived) {
                        validateValue(s.toString());
                    }

                    if (etInputEmailPhone.isFocused() && etInputEmailPhone.getText().toString().contains("@") && emailExtension != null) {
                        showEmailExtension();
                        isExtensionSelected = false;

                        String[] charEmail = etInputEmailPhone.getText().toString().split("@");
                        if (charEmail.length > 1) {
                            emailExtension.filterExtensions(charEmail[1]);
                        } else {
                            emailExtension.updateExtensions(emailExtensionList);
                        }
                    } else {
                        hideEmailExtension();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    public void setButtonValidator(Boolean status){
        isButtonValidatorActived = status;
        onValidValue();
    }

    private Boolean isValidValue(String value){
        return PartialRegisterInputUtils.isValidPhone(value) || PartialRegisterInputUtils.isValidEmail(value);
    }

    private void validateValue(String value){
        switch (PartialRegisterInputUtils.getType(value)){
            case PartialRegisterInputUtils.PHONE_TYPE: {
                if(PartialRegisterInputUtils.isValidPhone(value))
                    onValidValue();
                else if(!value.isEmpty())
                    onInvalidValue();
                break;
            }
            case PartialRegisterInputUtils.EMAIL_TYPE: {
                if(PartialRegisterInputUtils.isValidEmail(value))
                    onValidValue();
                else if(!value.isEmpty())
                    onInvalidValue();
                break;
            }
        }

    }

    private void onValidValue(){
        hideError();
        btnAction.setButtonCompatType(ButtonCompat.PRIMARY);
    }

    private void onInvalidValue(){
        btnAction.setButtonCompatType(ButtonCompat.PRIMARY_DISABLED);
    }

    public String getTextValue() {
        return etInputEmailPhone.getText().toString();
    }

    public void setAdapterInputEmailPhone(ArrayAdapter<String> adapter,
                                          OnFocusChangeListener onFocusChangeListener
    ) {
        if(adapter.getItem(0) != null && etInputEmailPhone != null){
            etInputEmailPhone.setText(adapter.getItem(0));
            etInputEmailPhone.setSelection(etInputEmailPhone.getText().length());
            etInputEmailPhone.setOnFocusChangeListener(onFocusChangeListener);
            etInputEmailPhone.setAdapter(adapter);
        }
    }

    public void setEmailExtension(EmailExtension emailExtension, List<String> emailExtensionList) {
        this.emailExtensionList = emailExtensionList;
        this.emailExtension = emailExtension;
        this.emailExtension.setExtensions(emailExtensionList, (extension, position) -> {
            String[] charEmail = etInputEmailPhone.getText().toString().split("@");
            if (charEmail.length > 0) {
                etInputEmailPhone.setText(String.format("%s@%s", charEmail[0], extension));
            } else {
                etInputEmailPhone.setText(String.format("%s@%s", etInputEmailPhone.getText().toString().replace("@", ""), extension));
            }
            etInputEmailPhone.setSelection(etInputEmailPhone.getText().toString().trim().length());
            isExtensionSelected = true;
            hideEmailExtension();
        });
    }

    public void initKeyboardListener(View view) {
        new KeyboardHandler(view, new KeyboardHandler.OnKeyBoardVisibilityChangeListener() {
            @Override
            public void onKeyboardShow() {
                if (etInputEmailPhone != null) {
                    if (etInputEmailPhone.getText().toString().contains("@") && !isExtensionSelected && etInputEmailPhone.isFocused()) {
                        showEmailExtension();
                    }
                }
            }

            @Override
            public void onKeyboardHide() {
                hideEmailExtension();
            }
        });
    }

    private class ClickRegister implements OnClickListener {

        @Override
        public void onClick(View v) {
            String id = etInputEmailPhone.getText().toString();
            listener.onActionPartialClick(id);
        }
    }

    private void hideError() {
        tvError.setText("");
        tvMessage.setVisibility(VISIBLE);
    }

    private void showError() {
        tvError.setVisibility(VISIBLE);
        tvMessage.setVisibility(GONE);
    }

    public void showLoginEmailView(@NotNull String email) {
        isButtonValidatorActived = false;

        wrapperPassword.setVisibility(View.VISIBLE);
        btnForgotPassword.setVisibility(View.VISIBLE);
        btnChange.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(GONE);

        tvMessage.setText("");
        wrapperEmailPhone.setLabel(wrapperEmailPhone.getContext().getString(R.string.title_email));
        btnAction.setText(btnAction.getContext().getString(R.string.login));

        etInputEmailPhone.setText(email);
        etInputEmailPhone.setEnabled(false);
    }

    public void showDefaultView() {
        isButtonValidatorActived = true;

        wrapperPassword.setVisibility(View.GONE);
        btnForgotPassword.setVisibility(View.GONE);
        btnChange.setVisibility(View.GONE);
        tvMessage.setVisibility(VISIBLE);

        tvMessage.setText(tvMessage.getContext().getString(R.string.default_placeholder));
        wrapperEmailPhone.setLabel(wrapperEmailPhone.getContext().getString(R.string.phone_or_email_input));
        etInputEmailPhone.setText("");
        etInputEmailPhone.setEnabled(true);
    }

    public void resetErrorWrapper() {
        setWrapperError(wrapperEmailPhone, null);
        setWrapperError(wrapperPassword, null);
    }

    private void showEmailExtension() {
        if (emailExtension != null) {
            emailExtension.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmailExtension() {
        if (emailExtension != null) {
            emailExtension.setVisibility(View.GONE);
        }
    }
}
