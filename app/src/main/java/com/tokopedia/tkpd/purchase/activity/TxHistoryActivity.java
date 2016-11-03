package com.tokopedia.tkpd.purchase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.purchase.adapter.HistoryListAdapter;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderHistory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TxHistoryActivity
 * Created by Angga.Prasetiyo on 02/05/2016.
 */
public class TxHistoryActivity extends TActivity {
    private static final String EXTRA_ORDER_HISTORIES = "EXTRA_ORDER_HISTORIES";

    public static Intent createInstance(Context context, List<OrderHistory> orderHistoryList) {
        Intent intent = new Intent(context, TxHistoryActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ORDER_HISTORIES,
                new ArrayList<>(orderHistoryList));
        return intent;
    }

    @Bind(R.id.order_status)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_order_history_view);
        ButterKnife.bind(this);
        HistoryListAdapter adapter = new HistoryListAdapter(this);
        listView.setAdapter(adapter);
        List<OrderHistory> orderHistories =
                getIntent().getParcelableArrayListExtra(EXTRA_ORDER_HISTORIES);
        adapter.addAll(orderHistories);
        adapter.notifyDataSetChanged();
    }
}
