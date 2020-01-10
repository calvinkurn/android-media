package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.analytic.AppScreen;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationReportActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationReport;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationReportPresenter;

import javax.inject.Inject;

/**
 * @author by nisie on 9/13/17.
 */

public class InboxReputationReportFragment extends BaseDaggerFragment
        implements InboxReputationReport.View {

    private Button sendButton;
    private RadioGroup reportRadioGroup;
    private RadioButton otherRadioButton;
    private EditText otherReason;
    private TkpdProgressDialog progressDialog;

    @Inject
    InboxReputationReportPresenter presenter;

    public static Fragment createInstance(String reviewId, int shopId) {
        Fragment fragment = new InboxReputationReportFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(InboxReputationReportActivity.ARGS_SHOP_ID, shopId);
        bundle.putString(InboxReputationReportActivity.ARGS_REVIEW_ID, reviewId);
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
        prepareView();
        presenter.attachView(this);
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

        sendButton.setOnClickListener(view -> presenter.reportReview(
                getArguments().getString(InboxReputationReportActivity.ARGS_REVIEW_ID, ""),
                String.valueOf(getArguments().getInt(InboxReputationReportActivity.ARGS_SHOP_ID)),
                reportRadioGroup.getCheckedRadioButtonId(),
                otherReason.getText().toString()));
    }

    private void setSendButton() {
        if (reportRadioGroup.getCheckedRadioButtonId() == R.id.report_spam
                || reportRadioGroup.getCheckedRadioButtonId() == R.id.report_sara
                || (reportRadioGroup.getCheckedRadioButtonId() == R.id.report_other
                && !TextUtils.isEmpty(otherReason.getText().toString().trim()))
                ) {
            sendButton.setEnabled(true);
            sendButton.setTextColor(getResources().getColor(R.color.white));
            MethodChecker.setBackground(sendButton,
                    getResources().getDrawable(R.drawable.green_button_rounded));
        } else {
            sendButton.setEnabled(false);
            sendButton.setTextColor(getResources().getColor(R.color.grey_700));
            MethodChecker.setBackground(sendButton,
                    getResources().getDrawable(R.drawable.bg_button_disabled));
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
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        if (progressDialog != null)
            progressDialog.showDialog();
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
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
