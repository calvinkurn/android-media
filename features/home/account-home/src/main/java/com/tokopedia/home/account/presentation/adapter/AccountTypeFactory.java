package com.tokopedia.home.account.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.presentation.viewholder.BuyerCardViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuGridViewHolder;
import com.tokopedia.home.account.presentation.viewholder.TokopediaPayViewHolder;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class AccountTypeFactory extends BaseAdapterTypeFactory {
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
        return TokopediaPayViewHolder.LAYOUT;
    }

    public int type(MenuGridViewModel menuGridViewModel) {
        return MenuGridViewHolder.LAYOUT;
    }
}