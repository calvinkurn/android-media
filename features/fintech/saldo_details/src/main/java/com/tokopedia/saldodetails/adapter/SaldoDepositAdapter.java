package com.tokopedia.saldodetails.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.saldodetails.response.model.DepositHistoryList;

public class SaldoDepositAdapter extends BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory> {
    public SaldoDepositAdapter(SaldoDetailTransactionFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}
