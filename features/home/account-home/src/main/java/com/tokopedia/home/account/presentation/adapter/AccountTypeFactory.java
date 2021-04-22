package com.tokopedia.home.account.presentation.adapter;

import androidx.annotation.NonNull;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringListViewHolder;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewholder.AccountLoadingMoreViewHolder;
import com.tokopedia.home.account.presentation.viewholder.AccountRecommendationTitleViewHolder;
import com.tokopedia.home.account.presentation.viewholder.BuyerCardViewHolder;
import com.tokopedia.home.account.presentation.viewholder.InfoCardViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuGridIconNotificationViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuGridViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuListViewHolder;
import com.tokopedia.home.account.presentation.viewholder.MenuTitleViewHolder;
import com.tokopedia.home.account.presentation.viewholder.RecommendationProductViewHolder;
import com.tokopedia.home.account.presentation.viewholder.TickerViewHolder;
import com.tokopedia.home.account.presentation.viewholder.TokopediaPayViewHolder;
import com.tokopedia.home.account.presentation.viewholder.TopAdsHeadlineViewHolder;
import com.tokopedia.home.account.presentation.viewmodel.AccountRecommendationTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridIconNotificationViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TickerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TopadsHeadlineUiModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author okasurya on 7/17/18.
 */
public class AccountTypeFactory extends BaseAdapterTypeFactory {
    private AccountItemListener listener;
    private UserSession userSession;

    public AccountTypeFactory(@NonNull AccountItemListener listener, UserSession userSession) {
        this.listener = listener;
        this.userSession = userSession;
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
        } else if (type == MenuGridIconNotificationViewHolder.Companion.getLAYOUT()) {
            return new MenuGridIconNotificationViewHolder(parent, listener);
        } else if (type == MenuListViewHolder.LAYOUT) {
            return new MenuListViewHolder(parent, listener);
        } else if (type == InfoCardViewHolder.LAYOUT) {
            return new InfoCardViewHolder(parent, listener);
        } else if (type == AccountRecommendationTitleViewHolder.Companion.getLAYOUT()) {
            return new AccountRecommendationTitleViewHolder(parent);
        } else if (type == RecommendationProductViewHolder.Companion.getLAYOUT()) {
            return new RecommendationProductViewHolder(parent, listener);
        } else if (type == LoadingShimmeringListViewHolder.LAYOUT) {
            return new LoadingShimmeringListViewHolder(parent);
        } else if (type == AccountLoadingMoreViewHolder.LAYOUT) {
            return new AccountLoadingMoreViewHolder(parent);
        } else if (type == TopAdsHeadlineViewHolder.Companion.getLAYOUT()) {
            return new TopAdsHeadlineViewHolder(parent, userSession);
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

    public int type(MenuGridIconNotificationViewModel vm) {
        return MenuGridIconNotificationViewHolder.Companion.getLAYOUT();
    }

    public int type(MenuListViewModel vm) {
        return MenuListViewHolder.LAYOUT;
    }

    public int type(InfoCardViewModel vm) {
        return InfoCardViewHolder.LAYOUT;
    }

    public int type(AccountRecommendationTitleViewModel viewModel) {
        return AccountRecommendationTitleViewHolder.Companion.getLAYOUT();
    }

    public int type(RecommendationProductViewModel viewModel) {
        return RecommendationProductViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringListViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingMoreModel viewModel) {
        return AccountLoadingMoreViewHolder.LAYOUT;
    }

    public int type(TopadsHeadlineUiModel viewModel) {
        return TopAdsHeadlineViewHolder.Companion.getLAYOUT();
    }
}