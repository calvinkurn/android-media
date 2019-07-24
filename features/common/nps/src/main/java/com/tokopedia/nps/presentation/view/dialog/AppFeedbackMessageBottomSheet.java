package com.tokopedia.nps.presentation.view.dialog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

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

    private EditTextCompat messageDesc;
    private AppCompatButton sendButton;

    @Inject
    FeedbackPresenter presenter;

    private void initInjector() {
        FeedbackComponent component = DaggerFeedbackComponent.builder()
                .feedbackModule(new FeedbackModule(getContext()))
                .build();
        component.inject(this);
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

        messageDesc = view.findViewById(R.id.message_description);
        sendButton = view.findViewById(R.id.send_button);

        final String descriptionText = messageDesc.getText().toString();

        if (sendButton != null) {
            sendButton.setOnClickListener(v -> {
                FragmentManager manager = getActivity().getSupportFragmentManager();

                if (manager != null) {
                    new AppFeedbackThankYouBottomSheet().show(manager, "AppFeedbackThankYouBottomSheet");
                    this.dismiss();
                }
            });
        }
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
            new AppFeedbackThankYouBottomSheet().show(manager, "AppFeedbackThankYouBottomSheet");
            this.dismiss();
        }
    }
}
