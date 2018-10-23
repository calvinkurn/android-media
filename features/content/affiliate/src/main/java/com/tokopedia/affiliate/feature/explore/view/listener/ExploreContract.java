package com.tokopedia.affiliate.feature.explore.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;

import java.util.List;

/**
 * @author by yfsx on 24/09/18.
 */
public interface ExploreContract {
    interface View extends CustomerView {

        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetFirstData(List<Visitable> itemList, String cursor);

        void onErrorGetFirstData(String error);

        void onSuccessGetMoreData(List<Visitable> itemList, String cursor);

        void onErrorGetMoreData(String error);

        void onBymeClicked(ExploreViewModel model);

        void onProductClicked(ExploreViewModel model);

        void dropKeyboard();

        void onButtonEmptySearchClicked();

        void onEmptySearchResult();

        void onSuccessCheckAffiliate(boolean isAffiliate, String productId, String adId);

        void onErrorCheckAffiliate(String error, String productId, String adId);

        void onSuccessCheckQuota(String productId, String adId);

        void onSuccessCheckQuotaButEmpty();

        void onErrorCheckQuota(String error, String productId, String adId);

    }

    interface Presenter extends CustomerPresenter<View> {

        void getFirstData(ExploreParams exploreParams, boolean isPullToRefresh);

        void loadMoreData(ExploreParams exploreParams);

        void checkIsAffiliate(String productId, String adId);

        void checkAffiliateQuota(String productId, String adId);
    }
}
