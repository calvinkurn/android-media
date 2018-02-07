package com.tokopedia.digital.tokocash.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.ActionHistory;
import com.tokopedia.digital.tokocash.model.ItemHistory;
import com.tokopedia.digital.tokocash.model.WalletToDepositPassData;

import butterknife.BindView;

/**
 * Created by nabillasabbaha on 8/24/17.
 */

public class DetailTransactionActivity extends BasePresenterActivity {

    private static final String ITEM_HISTORY_KEY = "item_history";
    private static final int REQUEST_MOVE_TO_SALDO = 110;

    @BindView(R2.id.icon_item_history)
    ImageView iconItem;
    @BindView(R2.id.price_item_history)
    TextView priceItem;
    @BindView(R2.id.title_item_history)
    TextView titleItem;
    @BindView(R2.id.desc_item_history)
    TextView descItem;
    @BindView(R2.id.transaction_info_detail)
    TextView transactionInfoDetail;
    @BindView(R2.id.bantuan_btn)
    Button bantuanBtn;
    @BindView(R2.id.button_opsi)
    LinearLayout buttonOpsiContainer;
    @BindView(R2.id.notes_item_history)
    TextView notesItem;

    private ItemHistory itemHistory;


    public static Intent newInstance(Context context, ItemHistory itemHistory) {
        Intent intent = new Intent(context, DetailTransactionActivity.class);
        intent.putExtra(ITEM_HISTORY_KEY, itemHistory);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        itemHistory = extras.getParcelable(ITEM_HISTORY_KEY);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_transaction;
    }

    @Override
    protected void initView() {
        if (itemHistory.getActionHistoryList() != null) {
            for (ActionHistory actionHistory : itemHistory.getActionHistoryList()) {
                if (actionHistory.getType().equals("button")) {
                    View view = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.item_button_opsi, buttonOpsiContainer, false);
                    Button buttonOpsi = (Button) view.findViewById(R.id.opsi_btn);
                    buttonOpsi.setText(actionHistory.getTitle());
                    buttonOpsi.setOnClickListener(getOpsiListener(actionHistory));
                    buttonOpsiContainer.addView(view);
                }
            }
        }
    }

    @Override
    protected void setViewListener() {
        bantuanBtn.setOnClickListener(getHelpListener());
    }

    @Override
    protected void initVar() {
        toolbar.setTitle(getString(R.string.title_detail_transaction));
    }

    @Override
    protected void setActionVar() {
        titleItem.setText(itemHistory.getTitle());
        descItem.setText(itemHistory.getDescription());
        transactionInfoDetail.setText(itemHistory.getTransactionInfoId() + " " +
                itemHistory.getTransactionInfoDate());
        priceItem.setText(itemHistory.getAmountChanges());
        priceItem.setTextColor(ContextCompat.getColor(this,
                itemHistory.getAmountChangesSymbol().equals("+") ? R.color.green_500 : R.color.red_500));

        if (itemHistory.getUrlImage() != null) {
            Glide.with(this)
                    .load(itemHistory.getUrlImage())
                    .placeholder(ContextCompat.getDrawable(this, R.drawable.ic_loading_toped))
                    .into(iconItem);
        }
        if (!TextUtils.isEmpty(itemHistory.getNotes())) {
            notesItem.setText(itemHistory.getNotes());
            notesItem.setVisibility(View.VISIBLE);
        } else {
            notesItem.setVisibility(View.GONE);
        }
        if (itemHistory.getActionHistoryList() != null && itemHistory.getActionHistoryList().size() > 0) {
            buttonOpsiContainer.setVisibility(View.VISIBLE);
            bantuanBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.digital_white_border_grey));
            bantuanBtn.setTextColor(ContextCompat.getColor(this, R.color.black_38));
        } else {
            buttonOpsiContainer.setVisibility(View.GONE);
            bantuanBtn.setBackground(ContextCompat.getDrawable(this, R.color.medium_green));
            bantuanBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    @NonNull
    private View.OnClickListener getOpsiListener(final ActionHistory actionHistory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionHistory.getType().equals("button") && actionHistory.getName().equals("movetosaldo")) {
                    WalletToDepositPassData walletToDepositPassData =
                            new WalletToDepositPassData.Builder()
                                    .amountFormatted(itemHistory.getAmountPending())
                                    .method(actionHistory.getMethod())
                                    .params(actionHistory.getParams())
                                    .name(actionHistory.getName())
                                    .url(actionHistory.getUrl())
                                    .title(actionHistory.getTitle())
                                    .build();
                    startActivityForResult(WalletToDepositActivity.newInstance(getApplicationContext(), walletToDepositPassData), REQUEST_MOVE_TO_SALDO);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MOVE_TO_SALDO) {
            if (resultCode == WalletToDepositActivity.RESULT_WALLET_TO_DEPOSIT_SUCCESS ||
                    resultCode == WalletToDepositActivity.RESULT_WALLET_TO_DEPOSIT_FAILED) {
                finish();
            }
        }
    }

    @NonNull
    private View.OnClickListener getHelpListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelpHistoryDetailActivity.class);
                intent.putExtra(HelpHistoryDetailActivity.TRANSACTION_ID, String.valueOf(itemHistory.getTransactionId()));
                startActivityForResult(intent, 201);
            }
        };
    }

    @Override
    protected void onDestroy() {
        if (buttonOpsiContainer.getChildCount() > 0) {
            buttonOpsiContainer.removeAllViews();
        }
        super.onDestroy();
    }
}