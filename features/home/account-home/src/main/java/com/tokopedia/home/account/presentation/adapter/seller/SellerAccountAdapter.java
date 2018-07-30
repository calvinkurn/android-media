package com.tokopedia.home.account.presentation.adapter.seller;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/25/18.
 */
public class SellerAccountAdapter extends BaseAdapter<AccountTypeFactory> {
    public SellerAccountAdapter(AccountTypeFactory accountTypeFactory, List<Visitable> visitables) {
        super(accountTypeFactory, visitables);
    }
}
