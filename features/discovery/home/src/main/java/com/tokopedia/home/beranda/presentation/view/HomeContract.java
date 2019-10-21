package com.tokopedia.home.beranda.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.beranda.data.model.KeywordSearchData;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData;
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo;

import java.util.List;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public interface HomeContract {

    interface View extends CustomerView {

        boolean isLoading();

        void showLoading();

        void hideLoading();

        void setItems(List<Visitable> items, int repositoryFlag);

        void setHint(SearchPlaceholder searchPlaceholder);

        void updateHeaderItem(HeaderViewModel headerViewModel);

        void showNetworkError(String message);

        void showNetworkError();

        void removeNetworkError();

        String getString(@StringRes int res);

        Context getContext();

        void openWebViewURL(String url, Context context);

        void openWebViewURL(String url);

        Activity getActivity();

        void updateListOnResume(List<Visitable> visitables);

        void addImpressionToTrackingQueue(List<HomeVisitable> visitables);

        void configureRecommendationButton(boolean isShow);

        Observable<TokopointHomeDrawerData> getTokopoint();

        void startShopInfo(String shopId);

        void startDeeplinkShopInfo(String url);

        void showPopupIntroOvo(String applinkActivation);

        void onTabFeedLoadError(Throwable e);

        void onTabFeedLoadSuccess(List<FeedTabModel> feedTabModelList);

        void onHomeDataLoadSuccess();

        void detectAndSendLocation();

        boolean hasGeolocationPermission();

        void setStickyContent(StickyLoginTickerPojo.TickerDetail tickerDetail);

        void hideStickyLogin();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getHomeData();

        void updateHomeData();

        void getHeaderData(boolean initialStart);

        void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletActionData);

        void showPopUpIntroWalletOvo(String applinkActivation);

        void updateHeaderTokoCashPendingData(CashBackData cashBackData);

        void getShopInfo(String url, String shopDomain);

        void openProductPageIfValid(String url, String shopDomain);

        void onHeaderTokocashError();

        void onHeaderTokopointError();

        void onRefreshTokoPoint();

        void onRefreshTokoCash();

        void onResume();

        void onFirstLaunch();

        void onDestroy();

        void hitBannerImpression(BannerSlidesModel slidesModel);

        void onBannerClicked(BannerSlidesModel slidesModel);

        void updateHeaderTokoPointData(TokopointsDrawerHomeData tokopointsDrawerHomeData);

        void updateKeywordSearch(KeywordSearchData keywordSearchData);

        void getFeedTabData();

        void getStickyContent();
    }
}
