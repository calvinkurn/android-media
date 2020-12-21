package com.tokopedia.review.feature.inbox.buyerreview.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent;
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationReportActivity;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationReport;
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationReportPresenter;

import javax.inject.Inject;

import static com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationReportActivity.ARGS_REVIEW_ID;

/**
 * @author by nisie on 9/13/17.
 */

public class InboxReputationReportFragment extends BaseDaggerFragment
        implements InboxReputationReport.View {

    private Button sendButton;
    private RadioGroup reportRadioGroup;
    private RadioButton otherRadioButton;
    private RadioButton spamRadioButton;
    private RadioButton saraRadioButton;
    private EditText otherReason;
    private ProgressDialog progressDialog;

    private String feedbackId;

    @Inject
    InboxReputationReportPresenter presenter;

    @Inject
    ReputationTracking tracking;

    public static Fragment createInstance(String reviewId, int shopId) {
        Fragment fragment = new InboxReputationReportFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(InboxReputationReportActivity.ARGS_SHOP_ID, shopId);
        bundle.putString(ARGS_REVIEW_ID, reviewId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION_REPORT;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent = ((BaseMainApplication) requireContext().getApplicationContext()).getBaseAppComponent();
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
                        .builder()
                        .baseAppComponent(baseAppComponent)
                        .build();
        reputationComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_report, container,
                false);
        sendButton = (Button) parentView.findViewById(R.id.send_button);
        reportRadioGroup = (RadioGroup) parentView.findViewById(R.id.radio_group);
        otherReason = (EditText) parentView.findViewById(R.id.reason);
        otherRadioButton = (RadioButton) parentView.findViewById(R.id.report_other);
        spamRadioButton = parentView.findViewById(R.id.report_spam);
        saraRadioButton = parentView.findViewById(R.id.report_sara);
        prepareView();
        presenter.attachView(this);
        if (getArguments() != null) {
            feedbackId = getArguments().getString(ARGS_REVIEW_ID);
        }
        return parentView;
    }

    private void prepareView() {
        reportRadioGroup.setOnCheckedChangeListener((group, checkedId) -> setSendButton());
        otherReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otherRadioButton.setChecked(true);
                setSendButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otherReason.setOnClickListener(view -> otherRadioButton.setChecked(true));

        sendButton.setOnClickListener(v -> {
            tracking.onSubmitReportAbuse(feedbackId);
            presenter.reportReview(getArguments().getString(ARGS_REVIEW_ID, ""),
                    String.valueOf(getArguments().getInt(InboxReputationReportActivity.ARGS_SHOP_ID)),
                    reportRadioGroup.getCheckedRadioButtonId(),
                    otherReason.getText().toString());
        });

        initProgressDialog();
    }

    private void setSendButton() {

        if(reportRadioGroup.getCheckedRadioButtonId() == R.id.report_spam) {
            tracking.onClickRadioButtonReportAbuse(spamRadioButton.getText().toString());
        } else if(reportRadioGroup.getCheckedRadioButtonId() == R.id.report_sara) {
            tracking.onClickRadioButtonReportAbuse(saraRadioButton.getText().toString());
        } else if(reportRadioGroup.getCheckedRadioButtonId() == R.id.report_other) {
            tracking.onClickRadioButtonReportAbuse(otherRadioButton.getText().toString());
        }

        if (reportRadioGroup.getCheckedRadioButtonId() == R.id.report_spam
                || reportRadioGroup.getCheckedRadioButtonId() == R.id.report_sara
                || (reportRadioGroup.getCheckedRadioButtonId() == R.id.report_other
                && !TextUtils.isEmpty(otherReason.getText().toString().trim()))
        ) {
            sendButton.setEnabled(true);
            sendButton.setTextColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
            MethodChecker.setBackground(sendButton,
                    getResources().getDrawable(com.tokopedia.design.R.drawable.green_button_rounded));
        } else {
            sendButton.setEnabled(false);
            sendButton.setTextColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N500));
            MethodChecker.setBackground(sendButton,
                    getResources().getDrawable(com.tokopedia.design.R.drawable.bg_button_disabled));
        }
    }

    private void initProgressDialog() {
        if (getContext() != null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("");
            progressDialog.setMessage(getContext().getString(R.string.progress_dialog_loading));
            progressDialog.setCancelable(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
    }

    @Override
    public void showLoadingProgress() {
        if (!progressDialog.isShowing() && getContext() != null && progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onErrorReportReview(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessReportReview() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void removeLoadingProgress() {
        if (progressDialog.isShowing() && getContext() != null && progressDialog != null)
            progressDialog.dismiss();
    }
}
