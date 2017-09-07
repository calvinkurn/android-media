package com.tokopedia.digital.tokocash.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.base.BaseDigitalPresenterActivity;
import com.tokopedia.digital.tokocash.model.ItemHistory;

import butterknife.BindView;

/**
 * Created by nabillasabbaha on 8/24/17.
 */

public class DetailTransactionActivity extends BaseDigitalPresenterActivity {

    private static final String ITEM_HISTORY_KEY = "item_history";

    @BindView(R2.id.icon_item_history)
    ImageView iconItem;
    @BindView(R2.id.price_item_history)
    TextView priceItem;
    @BindView(R2.id.title_item_history)
    TextView titleItem;
    @BindView(R2.id.desc_item_history)
    TextView descItem;
    @BindView(R2.id.date_item_history)
    TextView dateItem;
    @BindView(R2.id.transaction_id_history)
    TextView transactionId;
    @BindView(R2.id.bantuan_btn)
    Button bantuanBtn;
    @BindView(R2.id.opsi_btn)
    Button opsiBtn;
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
        toolbar.setTitle(getString(R.string.title_detail_transaction));
        titleItem.setText(itemHistory.getTitle());
        descItem.setText(itemHistory.getDescription());
        dateItem.setText(itemHistory.getTransactionInfoDate());
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
            opsiBtn.setVisibility(View.VISIBLE);
            opsiBtn.setText(itemHistory.getActionHistoryList().get(0).getTitle());
            bantuanBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.digital_white_grey_button_more_rounded));
            bantuanBtn.setTextColor(ContextCompat.getColor(this, R.color.black_38));
        } else {
            opsiBtn.setVisibility(View.GONE);
            bantuanBtn.setBackground(ContextCompat.getDrawable(this, R.color.medium_green));
            bantuanBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    @Override
    protected void setViewListener() {
        bantuanBtn.setOnClickListener(getHelpListener());
        opsiBtn.setOnClickListener(getOpsiListener(itemHistory));
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @NonNull
    private View.OnClickListener getOpsiListener(final ItemHistory itemHistory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : untuk opsi listener, kayak misal pindahkan saldo
                Toast.makeText(getApplicationContext(), "Opsi bantuan" + itemHistory.getTitle(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    private View.OnClickListener getHelpListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : untuk bantuan
                Toast.makeText(getApplicationContext(), "bantuan", Toast.LENGTH_SHORT).show();
            }
        };
    }
}