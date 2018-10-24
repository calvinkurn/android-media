package com.tokopedia.loginregister.registerinitial.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.loginregister.R;

/**
 * @author by alvinatin on 11/06/18.
 */

public class PartialRegisterInputView extends BaseCustomView{

    EditText tvInputRegister;
    TextView tvMessage;
    TextView tvError;
    TextView btnRegister;
    TkpdHintTextInputLayout wrapper;
    private PartialRegisterInputViewListener listener;

    public void setListener(PartialRegisterInputViewListener listener) {
        this.listener = listener;
    }

    public interface PartialRegisterInputViewListener{
        void onRegisterClick(String id);
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

    private void init(){
        View view = inflate(getContext(), R.layout.layout_partial_register_input, this);
        tvInputRegister = (EditText) view.findViewById(R.id.input_register);
        tvMessage = view.findViewById(R.id.message);
        tvError = view.findViewById(R.id.error_message);
        btnRegister = view.findViewById(R.id.register_btn);
        wrapper = view.findViewById(R.id.input_layout);

        renderData();
    }

    public void renderData(){
        tvInputRegister.addTextChangedListener(watcher(wrapper));

        btnRegister.setOnClickListener(new ClickRegister());
    }

    public void onErrorValidate(String message){
        setWrapperError(wrapper, message);
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
        return tvInputRegister.getText().toString();
    }

    private class ClickRegister implements OnClickListener{

        @Override
        public void onClick(View v) {
            String id = tvInputRegister.getText().toString();
            listener.onRegisterClick(id);
        }
    }

    private void hideError() {
        tvError.setVisibility(GONE);
    }

    private void showError() {
        tvError.setVisibility(VISIBLE);
    }
}
