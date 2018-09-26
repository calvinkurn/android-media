package com.tokopedia.saldodetails.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.saldodetails.response.model.Deposit;

import java.util.List;

public class SaldoDepositAdapter extends BaseListAdapter<Deposit, SaldoDetailTransactionFactory> {
    public SaldoDepositAdapter(SaldoDetailTransactionFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}
