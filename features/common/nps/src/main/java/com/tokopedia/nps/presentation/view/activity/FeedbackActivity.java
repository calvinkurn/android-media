package com.tokopedia.nps.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.nps.R;
import com.tokopedia.nps.presentation.di.DaggerFeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackModule;
import com.tokopedia.nps.presentation.presenter.FeedbackPresenter;
import com.tokopedia.nps.presentation.view.FeedbackView;

import javax.inject.Inject;

/**
 * Created by meta on 26/03/18.
 */

public class FeedbackActivity extends BaseSimpleActivity implements FeedbackView {

    public static void start(Context context, float rating) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.class.getSimpleName(), rating);
        context.startActivity(intent);
    }

    @Inject FeedbackPresenter presenter;

    private Spinner spCategory;
    private EditText etDesc;
    private View layoutProgress, layoutContainer;

    private float rating;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initInjector();
        this.presenter.setView(this);

        rating = getIntent().getFloatExtra(FeedbackActivity.class.getSimpleName(), 0);

        spCategory = findViewById(R.id.sp_fb_category);
        etDesc = findViewById(R.id.et_fb_desc);
        layoutProgress = findViewById(R.id.layout_progress);
        layoutContainer = findViewById(R.id.container);
        Button btnSend = findViewById(R.id.btn_fb_send);

        btnSend.setOnClickListener(v -> sendFeedback());
    }

    private void initInjector() {
        FeedbackComponent component = DaggerFeedbackComponent.builder()
                .feedbackModule(new FeedbackModule(this))
                .build();
        component.inject(this);
    }

    private void sendFeedback() {
        if (spCategory.getSelectedItemPosition() > 0) {
            String category = spCategory.getSelectedItem().toString();
            String description = etDesc.getText().toString();

            presenter.post(String.valueOf(Math.round(rating)), category, description);
        } else {
            ToasterError.make(findViewById(android.R.id.content),
                    getString(R.string.fb_warning),
                    BaseToaster.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void showLoading() {
        layoutContainer.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        layoutContainer.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() { }

    @Override
    public void hideRetry() { }

    @Override
    public void showError(String message) {
        ToasterError.make(findViewById(android.R.id.content),
                message,
                BaseToaster.LENGTH_SHORT).show();
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void successPostFeedback() {
        FeedbackThankPageActivity.startActivity(this, rating);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
