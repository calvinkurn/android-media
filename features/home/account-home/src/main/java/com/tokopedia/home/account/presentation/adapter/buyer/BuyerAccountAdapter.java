package com.tokopedia.home.account.presentation.adapter.buyer;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

import java.util.List;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountAdapter extends BaseAdapter<AccountTypeFactory> {
    public BuyerAccountAdapter(AccountTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }
}
