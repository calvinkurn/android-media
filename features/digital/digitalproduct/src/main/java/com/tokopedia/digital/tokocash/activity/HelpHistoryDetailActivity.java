package com.tokopedia.digital.tokocash.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.tokocash.WalletService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.adapter.HelpHistoryAdapter;
import com.tokopedia.digital.tokocash.domain.HistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.TokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.listener.HelpHistoryListener;
import com.tokopedia.digital.tokocash.model.HelpHistoryTokoCash;
import com.tokopedia.digital.tokocash.presenter.HelpHistoryPresenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 10/16/17.
 */

public class HelpHistoryDetailActivity extends BasePresenterActivity<HelpHistoryPresenter> implements HelpHistoryListener {

    public static final String TRANSACTION_ID = "transaction_id";
    private static final int MIN_CHAR_HELP_DETAIL = 30;

    @BindView(R2.id.spinner_history_help)
    Spinner spinnerHistoryHelp;
    @BindView(R2.id.history_help_detail)
    EditText historyHelpDetail;
    @BindView(R2.id.cancel_send_help)
    Button cancelHelp;
    @BindView(R2.id.send_help)
    Button sendHelp;

    private String selectedCategory;
    private String transactionId;
    private int positionCategorySelected;
    private HelpHistoryAdapter adapter;
    private TkpdProgressDialog progressDialogNormal;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        transactionId = getIntent().getStringExtra(TRANSACTION_ID);
    }

    @Override
    protected void initialPresenter() {
        SessionHandler sessionHandler = new SessionHandler(getApplicationContext());
        presenter = new HelpHistoryPresenter(getApplicationContext(),
                new TokoCashHistoryInteractor(
                        new HistoryTokoCashRepository(new WalletService(sessionHandler.getAccessTokenTokoCash())),
                        new CompositeSubscription(),
                        new JobExecutor(),
                        new UIThread()), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_history_detail;
    }

    @Override
    protected void initView() {
        progressDialogNormal = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        selectedCategory = "";
        toolbar.setTitle(getString(R.string.title_help_history));
        presenter.getHelpCategoryHistory();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void loadHelpHistoryData(final List<HelpHistoryTokoCash> helpHistoryTokoCashes) {
        adapter = new HelpHistoryAdapter(this, android.R.layout.simple_spinner_item, helpHistoryTokoCashes);
        spinnerHistoryHelp.setAdapter(adapter);
        spinnerHistoryHelp.setSelection(adapter.getCount());
        spinnerHistoryHelp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerHistoryHelp.setSelection(position);
                positionCategorySelected = position;
                selectedCategory = helpHistoryTokoCashes.get(position).getTranslation();
                setValidationButtonSendHelp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeOptionSelected();
            }
        });

        sendHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.submitHelpHistory(getString(R.string.tokocash), historyHelpDetail.getText().toString(), selectedCategory, transactionId);
            }
        });

        historyHelpDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setValidationButtonSendHelp();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isValidForm() {
        return positionCategorySelected != adapter.getCount() && historyHelpDetail.length() > MIN_CHAR_HELP_DETAIL;
    }

    private void setValidationButtonSendHelp() {
        if (isValidForm()) {
            sendHelp.setEnabled(true);
            sendHelp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        } else {
            sendHelp.setEnabled(false);
            sendHelp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.digital_voucher_notes));
        }
    }

    @Override
    public void successSubmitHelpHistory() {
        Toast.makeText(getApplicationContext(), getString(R.string.success_message_send_help), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void showErrorHelpHistory(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void showProgressLoading() {
        if (!progressDialogNormal.isProgress()) progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        if (progressDialogNormal.isProgress()) progressDialogNormal.dismiss();
    }
}
