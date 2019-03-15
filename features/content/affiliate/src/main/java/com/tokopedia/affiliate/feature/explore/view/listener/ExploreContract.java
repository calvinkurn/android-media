package com.tokopedia.affiliate.feature.explore.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.AutoCompleteViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreProductViewModel;
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

        void hideLoading();

        void onSuccessGetFirstData(List<Visitable> itemList,
                                   String cursor,
                                   boolean isSearch,
                                   boolean isPullToRefresh,
                                   List<SortViewModel> sortViewModel);

        void onSuccessGetFilteredSortedFirstData(List<Visitable> itemList,
                                   String cursor,
                                   boolean isSearch,
                                   boolean isPullToRefresh);

        void onErrorGetFirstData(String error);

        void onSuccessGetMoreData(List<Visitable<?>> itemList, String cursor);

        void onErrorGetMoreData(String error);

        void onBymeClicked(ExploreProductViewModel model);

        void onProductClicked(ExploreProductViewModel model);

        void dropKeyboard();

        void onButtonEmptySearchClicked();

        void onEmptySearchResult();

        void onSuccessCheckAffiliate(boolean isAffiliate, String productId, String adId);

        void onErrorCheckAffiliate(String error, String productId, String adId);

        void onSuccessCheckQuota(String productId, String adId);

        void onSuccessCheckQuotaButEmpty();

        void onErrorCheckQuota(String error, String productId, String adId);

        void onAutoCompleteItemClicked(String keyword);

        void onAutoCompleteIconClicked(String keyword);

        void onSuccessGetAutoComplete(List<AutoCompleteViewModel> modelList);

        void stopTrace();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getFirstData(ExploreParams exploreParams, boolean isPullToRefresh);

        void loadMoreData(ExploreParams exploreParams);

        void checkIsAffiliate(String productId, String adId);

        void checkAffiliateQuota(String productId, String adId);

        void getAutoComplete(String keyword);

        void unsubscribeAutoComplete();
    }
}
