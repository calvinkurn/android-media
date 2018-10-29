package com.tokopedia.gm.statistic.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.gm.statistic.view.adapter.viewholder.GMStatisticTransactionTableViewHolder;

/**
 * @author normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableAdapter extends BaseListAdapter<GMStatisticTransactionTableModel> {

    private @GMTransactionTableSortBy int sortBy = GMTransactionTableSortBy.TRANS_SUM;

    public void setSortBy(@GMTransactionTableSortBy int sortBy){
        this.sortBy = sortBy;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)) {
            case GMStatisticTransactionTableModel.TYPE:
                ((GMStatisticTransactionTableViewHolder) holder).bindData(data.get(position), sortBy);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GMStatisticTransactionTableModel.TYPE:
                return new GMStatisticTransactionTableViewHolder(getLayoutView(parent, R.layout.item_gm_statistic_transaction_table));
        }
        return super.onCreateViewHolder(parent, viewType);
    }
}
