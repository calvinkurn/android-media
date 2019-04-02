package com.tokopedia.home.account.presentation.adapter.seller;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

import java.util.List;

/**
 * @author okasurya on 7/25/18.
 */
public class SellerAccountAdapter extends BaseAdapter<AccountTypeFactory> {
    public SellerAccountAdapter(AccountTypeFactory accountTypeFactory, List<Visitable> visitables) {
        super(accountTypeFactory, visitables);
    }

    public Visitable getItemAt(int i) {
        return visitables.get(i);
    }

    public void removeElementAt(int i) {
        if (!visitables.isEmpty() && i < visitables.size()) {
            visitables.remove(i);
            notifyItemRemoved(i);
        }
    }
}
