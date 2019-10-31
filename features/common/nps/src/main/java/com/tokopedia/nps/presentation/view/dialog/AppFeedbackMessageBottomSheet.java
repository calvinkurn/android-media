package com.tokopedia.nps.presentation.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tokopedia.design.component.EditTextCompat;
import com.tokopedia.nps.R;
import com.tokopedia.nps.presentation.view.FeedbackView;
import com.tokopedia.unifyprinciples.Typography;

public class AppFeedbackMessageBottomSheet extends AppFeedbackDialog implements FeedbackView {

    private FrameLayout buttonView;
    private ContentLoadingProgressBar progressView;
    private Typography buttonLabel;

    private float appRating;
    private String messageDesc;
    private String messageCategory;
    private boolean isSendingEvent = false;

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_message;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        presenter.setView(this);

        messageCategory = getString(R.string.message_default_category);
        LinearLayout container = view.findViewById(R.id.dialog_container);
        EditTextCompat messageDescView = view.findViewById(R.id.message_description);
        buttonView = view.findViewById(R.id.send_button);
        progressView = view.findViewById(R.id.send_progress);
        buttonLabel = view.findViewById(R.id.button_label);

        if (progressView != null && getContext() != null) {
            progressView.getIndeterminateDrawable()
                    .setColorFilter(
                            ContextCompat.getColor(getContext(), R.color.Neutral_N0),
                            PorterDuff.Mode.SRC_IN
                    );
        }

        setContainerHandlers(container);
        setButtonHandlers(buttonView);
        setEditTextHandlers(messageDescView);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context context() {
        return this.getContext();
    }

    @Override
    public void successPostFeedback() {
        if (getActivity() != null) {
            isSendingEvent = false;
            progressView.setVisibility(View.GONE);
            buttonLabel.setVisibility(View.VISIBLE);

            FragmentManager manager = getActivity().getSupportFragmentManager();

            new AppFeedbackThankYouBottomSheet()
                    .showDialog(manager, appRating, "AppFeedbackThankYouBottomSheet");

            this.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void showDialog(FragmentManager manager, float appRating, String tag) {
        super.show(manager, tag);
        this.appRating = appRating;
    }

    private void setContainerHandlers(ViewGroup view) {
        if (view != null) {
            view.setOnClickListener(v -> {
                Activity activity = getActivity();

                if (activity != null) {
                    InputMethodManager inputMethod = (InputMethodManager) activity
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethod.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            });
        }
    }

    private void setButtonHandlers(FrameLayout button) {
        if (button != null) {
            button.setEnabled(false);

            button.setOnClickListener(view -> {
                if (!isSendingEvent) {
                    presenter.post(String.valueOf((int) appRating), messageCategory, messageDesc);
                    progressView.setVisibility(View.VISIBLE);
                    buttonLabel.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setEditTextHandlers(EditTextCompat textField) {
        if (textField != null) {
            messageDesc = textField.getText() != null ? textField.getText().toString() : "";
            textField.setImeOptions(EditorInfo.IME_ACTION_DONE);
            textField.setRawInputType(InputType.TYPE_CLASS_TEXT);
            textField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() > 0) {
                        buttonView.setEnabled(true);
                    } else {
                        buttonView.setEnabled(false);
                    }
                }
            });
        }
    }
}
