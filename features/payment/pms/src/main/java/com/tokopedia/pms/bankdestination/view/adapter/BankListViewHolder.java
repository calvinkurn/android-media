package com.tokopedia.pms.bankdestination.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.pms.R;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankListViewHolder extends AbstractViewHolder<BankListModel> {
    public static int Layout = R.layout.item_bank_list;

    private TextView bankName;

    public BankListViewHolder(View itemView) {
        super(itemView);
        bankName = itemView.findViewById(R.id.bank_name);
    }

    @Override
    public void bind(BankListModel element) {
        bankName.setText(element.getBankName());
    }
}
