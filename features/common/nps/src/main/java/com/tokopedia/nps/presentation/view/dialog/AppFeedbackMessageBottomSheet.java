package com.tokopedia.nps.presentation.view.dialog;

import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.design.component.EditTextCompat;
import com.tokopedia.nps.R;
import com.tokopedia.nps.presentation.di.FeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackModule;
import com.tokopedia.nps.presentation.presenter.FeedbackPresenter;

import javax.inject.Inject;

public class AppFeedbackMessageBottomSheet extends BottomSheets {

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
        messageDesc = view.findViewById(R.id.message_description);
        sendButton = view.findViewById(R.id.send_button);

        final String descriptionText = messageDesc.getText().toString();

        if (sendButton != null) {
            sendButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), descriptionText, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
