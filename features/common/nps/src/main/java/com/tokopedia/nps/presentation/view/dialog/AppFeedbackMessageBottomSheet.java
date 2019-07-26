package com.tokopedia.nps.presentation.view.dialog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.design.component.EditTextCompat;
import com.tokopedia.nps.R;
import com.tokopedia.nps.presentation.di.DaggerFeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackModule;
import com.tokopedia.nps.presentation.presenter.FeedbackPresenter;
import com.tokopedia.nps.presentation.view.FeedbackView;

import javax.inject.Inject;

public class AppFeedbackMessageBottomSheet extends BottomSheets implements FeedbackView {

    private FrameLayout buttonView;
    private float appRating;
    private String messageDesc;
    private String messageCategory;

    @Inject
    FeedbackPresenter presenter;

    public void showDialog(FragmentManager manager, float appRating, String tag) {
        super.show(manager, tag);
        this.appRating = appRating;
    }

    private void initInjector() {
        FeedbackComponent component = DaggerFeedbackComponent.builder()
                .feedbackModule(new FeedbackModule(getContext()))
                .build();
        component.inject(this);
    }

    private void setSendButtonClickListener(FrameLayout button) {
        if (button != null) {
            button.setEnabled(false);

            button.setOnClickListener(view -> {
                presenter.post(String.valueOf((int) appRating), messageCategory, messageDesc);
            });
        }
    }

    private void handleOnTextChanged(EditTextCompat textField) {
        if (textField != null) {
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

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.dialog_feedback_base;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_message;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.NORMAL;
    }

    @Override
    protected String title() {
        return "";
    }

    @Override
    public void initView(View view) {
        initInjector();
        this.presenter.setView(this);

        EditTextCompat messageDescView = view.findViewById(R.id.message_description);
        buttonView = view.findViewById(R.id.send_button);

        messageDesc = messageDescView.getText().toString();
        messageCategory = getString(R.string.message_default_category);

        setSendButtonClickListener(buttonView);
        handleOnTextChanged(messageDescView);
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
        FragmentManager manager = getActivity().getSupportFragmentManager();

        if (manager != null) {
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
}
