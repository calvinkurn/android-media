package com.tokopedia.saldodetails.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.saldodetails.adapter.listener.DataEndLessScrollListener;
import com.tokopedia.saldodetails.response.model.DepositHistoryList;

public class SaldoDepositAdapter extends BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory>
        implements DataEndLessScrollListener.OnDataEndlessScrollListener {
    public SaldoDepositAdapter(SaldoDetailTransactionFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }

    @Override
    public int getEndlessDataSize() {
        return getDataSize();
    }
}
