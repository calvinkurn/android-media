package com.tokopedia.home.account.presentation.adapter.buyer;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.presentation.viewholder.BuyerCardViewHolder;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerAccountTypeFactory extends BaseAdapterTypeFactory {
    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == BuyerCardViewHolder.LAYOUT) {
            return new BuyerCardViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }

    public int type(BuyerCardViewModel userViewModel) {
        return BuyerCardViewHolder.LAYOUT;
    }

    public int type(TokopediaPayViewModel tokopediaPayViewModel) {
        return 0;
    }
}
