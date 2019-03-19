package com.tokopedia.gm.statistic.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.common.utils.KMNumbers;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTransactionTableLeft;
    private final TextView itemTransactionTableRight;

    public GMStatisticTransactionTableViewHolder(View itemView) {
        super(itemView);
        itemTransactionTableLeft = (TextView) itemView.findViewById(R.id.item_transaction_table_left);
        itemTransactionTableRight = (TextView) itemView.findViewById(R.id.item_transaction_table_right);
    }

    public void bindData(GMStatisticTransactionTableModel gmStatisticTransactionTableModel, @GMTransactionTableSortBy int sortBy) {
        final int productId = gmStatisticTransactionTableModel.getProductId();
        itemTransactionTableLeft.setText(gmStatisticTransactionTableModel.productName);
        switch (sortBy){
            case GMTransactionTableSortBy.DELIVERED_AMT:
                itemTransactionTableRight.setText(KMNumbers.formatRupiahString(gmStatisticTransactionTableModel.getDeliveredAmount()));
                break;
            case GMTransactionTableSortBy.TRANS_SUM:
                itemTransactionTableRight.setText(String.valueOf(gmStatisticTransactionTableModel.getTransSum()));
                break;
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteManager.route(itemView.getContext(),
                        UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, String.valueOf(productId)));
            }
        });

    }
}
