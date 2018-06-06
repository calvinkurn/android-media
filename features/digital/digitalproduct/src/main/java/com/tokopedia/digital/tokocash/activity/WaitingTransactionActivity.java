package com.tokopedia.digital.tokocash.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.adapter.WaitingTransactionAdapter;
import com.tokopedia.digital.tokocash.model.ItemHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by nabillasabbaha on 11/28/17.
 */

public class WaitingTransactionActivity extends BasePresenterActivity {

    private final static String TOKOCASH_HISTORY_DATA_EXTRA = "tokocash_history_data_extra";

    @BindView(R2.id.waiting_transaction_rv)
    RecyclerView waitingTransactionRV;

    private WaitingTransactionAdapter adapter;
    private TokoCashHistoryData tokoCashHistoryData;

    public static Intent newInstance(TokoCashHistoryData tokoCashHistoryData, Context context) {
        Intent intent = new Intent(context, WaitingTransactionActivity.class);
        intent.putExtra(TOKOCASH_HISTORY_DATA_EXTRA, tokoCashHistoryData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        tokoCashHistoryData = extras.getParcelable(TOKOCASH_HISTORY_DATA_EXTRA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_waiting_transaction;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(getString(R.string.title_waiting_transaction_tokocash));
        waitingTransactionRV.setHasFixedSize(true);
        waitingTransactionRV.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        adapter = new WaitingTransactionAdapter(new ArrayList<ItemHistory>());
        waitingTransactionRV.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        adapter.addItem(tokoCashHistoryData.getItemHistoryList());
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}