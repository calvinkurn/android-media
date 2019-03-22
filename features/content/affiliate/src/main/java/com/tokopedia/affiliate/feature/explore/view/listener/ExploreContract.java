package com.tokopedia.affiliate.feature.explore.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.common.viewmodel.ExploreCardViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.AutoCompleteViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreBannerChildViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileChildViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel;

import java.util.List;

/**
 * @author by yfsx on 24/09/18.
 */
public interface ExploreContract {
    interface View extends CustomerView {

        Context getContext();

        AffiliateAnalytics getAffiliateAnalytics();

        void showLoading();

        void showLoadingScreen();

        void hideLoading();

        void onSuccessGetFirstData(List<Visitable<?>> sections,
                                   List<Visitable<?>> products,
                                   String cursor,
                                   boolean isSearch,
                                   boolean isPullToRefresh,
                                   List<SortViewModel> sortViewModel);

        void onErrorGetFirstData(String error);

        void onSuccessGetData(List<Visitable<?>> products,
                              String cursor,
                              boolean isSearch);

        void onErrorGetData(String error);

        void onSuccessGetMoreData(List<Visitable<?>> itemList, String cursor);

        void onErrorGetMoreData(String error);

        void onBymeClicked(ExploreCardViewModel model);

        void onProductClicked(ExploreCardViewModel model, int adapterPosition);

        void onBannerClicked(ExploreBannerChildViewModel model);

        void onProfileClicked(PopularProfileChildViewModel model);

        void dropKeyboard();

        void onButtonEmptySearchClicked();

        void onEmptySearchResult();

        void onEmptyProduct();

        void onSuccessCheckAffiliate(boolean isAffiliate, String productId, String adId);

        void onErrorCheckAffiliate(String error, String productId, String adId);

        void onSuccessCheckQuota(String productId, String adId);

        void onSuccessCheckQuotaButEmpty();

        void onErrorCheckQuota(String error, String productId, String adId);

        void onAutoCompleteItemClicked(String keyword);

        void onAutoCompleteIconClicked(String keyword);

        void onSuccessGetAutoComplete(List<AutoCompleteViewModel> modelList);

        void stopTrace();

        void unsubscribeAutoComplete();

        boolean shouldBackPressed();

        void refresh();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getFirstData(ExploreParams exploreParams, boolean isPullToRefresh);

        void getData(ExploreParams exploreParams);

        void loadMoreData(ExploreParams exploreParams);

        void checkIsAffiliate(String productId, String adId);

        void checkAffiliateQuota(String productId, String adId);

        void getAutoComplete(String keyword);

        void unsubscribeAutoComplete();
    }
}
