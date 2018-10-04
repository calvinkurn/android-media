package com.tokopedia.pms.bankdestination.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankListAdapterTypeFactory extends BaseAdapterTypeFactory {

    public int type(BankListModel bankListModel) {
        return BankListViewHolder.Layout;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == BankListViewHolder.Layout){
            return new BankListViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }
}
