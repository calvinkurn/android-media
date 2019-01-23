package com.tokopedia.home.account.presentation.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewholder.AddProductViewHolder;
import com.tokopedia.home.account.presentation.viewholder.BuyerCardViewHolder;
import com.tokopedia.home.account.presentation.viewholder.InfoCardViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuGridViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuListViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuTitleViewHolder;
import com.tokopedia.home.account.presentation.viewholder.SellerEmptyViewHolder;
import com.tokopedia.home.account.presentation.viewholder.SellerSaldoViewHolder;
import com.tokopedia.home.account.presentation.viewholder.ShopCardViewHolder;
import com.tokopedia.home.account.presentation.viewholder.TickerViewHolder;
import com.tokopedia.home.account.presentation.viewholder.TokopediaPayViewHolder;
import com.tokopedia.home.account.presentation.viewmodel.AddProductViewModel;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.SellerEmptyViewModel;
import com.tokopedia.home.account.presentation.viewmodel.SellerSaldoViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TickerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

/**
 * @author okasurya on 7/17/18.
 */
public class AccountTypeFactory extends BaseAdapterTypeFactory {
    private AccountItemListener listener;

    public ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

    public AccountTypeFactory(@NonNull AccountItemListener listener) {
        this.listener = listener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TickerViewHolder.LAYOUT) {
            return new TickerViewHolder(parent, listener);
        } else if (type == BuyerCardViewHolder.LAYOUT) {
            return new BuyerCardViewHolder(parent, listener);
        } else if (type == TokopediaPayViewHolder.LAYOUT) {
            return new TokopediaPayViewHolder(parent, listener);
        } else if (type == MenuTitleViewHolder.LAYOUT) {
            return new MenuTitleViewHolder(parent);
        } else if (type == MenuGridViewHolder.LAYOUT) {
            return new MenuGridViewHolder(parent, listener);
        } else if (type == MenuListViewHolder.LAYOUT) {
            return new MenuListViewHolder(parent, listener);
        } else if (type == InfoCardViewHolder.LAYOUT) {
            return new InfoCardViewHolder(parent, listener);
        } else if (type == ShopCardViewHolder.LAYOUT) {
            return new ShopCardViewHolder(parent, listener);
        } else if (type == SellerSaldoViewHolder.LAYOUT) {
            return new SellerSaldoViewHolder(parent, listener, showCaseObjectList);
        } else if (type == AddProductViewHolder.LAYOUT) {
            return new AddProductViewHolder(parent, listener);
        } else if (type == SellerEmptyViewHolder.LAYOUT) {
            return new SellerEmptyViewHolder(parent, listener);
        }
        return super.createViewHolder(parent, type);
    }

    public int type(TickerViewModel vm) {
        return TickerViewHolder.LAYOUT;
    }

    public int type(BuyerCardViewModel vm) {
        return BuyerCardViewHolder.LAYOUT;
    }

    public int type(TokopediaPayViewModel vm) {
        return TokopediaPayViewHolder.LAYOUT;
    }

    public int type(MenuTitleViewModel vm) {
        return MenuTitleViewHolder.LAYOUT;
    }

    public int type(MenuGridViewModel vm) {
        return MenuGridViewHolder.LAYOUT;
    }

    public int type(MenuListViewModel vm) {
        return MenuListViewHolder.LAYOUT;
    }

    public int type(InfoCardViewModel vm) {
        return InfoCardViewHolder.LAYOUT;
    }

    public int type(ShopCardViewModel vm) {
        return ShopCardViewHolder.LAYOUT;
    }

    public int type(SellerSaldoViewModel vm) {
        return SellerSaldoViewHolder.LAYOUT;
    }

    public int type(AddProductViewModel viewModel) {
        return AddProductViewHolder.LAYOUT;
    }

    public int type(SellerEmptyViewModel viewModel) {
        return SellerEmptyViewHolder.LAYOUT;
    }

    public ArrayList<ShowCaseObject> getShowCaseObjectList() {
        return showCaseObjectList;
    }
}