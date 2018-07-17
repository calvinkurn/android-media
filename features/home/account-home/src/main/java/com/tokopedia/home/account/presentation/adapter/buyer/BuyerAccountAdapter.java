package com.tokopedia.home.account.presentation.adapter.buyer;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountAdapter extends BaseAdapter<BuyerAccountTypeFactory> {

    public BuyerAccountAdapter(BuyerAccountTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }
}
