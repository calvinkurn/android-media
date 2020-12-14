package com.tokopedia.home.beranda.presentation.view;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.beranda.data.model.KeywordSearchData;
import com.tokopedia.home.beranda.data.model.PlayChannel;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData;
import com.tokopedia.home.beranda.domain.model.HomeFlag;
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.List;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public interface HomeContract {

    interface View extends CustomerView {

        void onSuccessDismissReview();

        boolean isLoading();

        void showLoading();

        void hideLoading();

        void setHint(SearchPlaceholder searchPlaceholder);

        void removeNetworkError();

        String getString(@StringRes int res);

        Context getContext();

        void openWebViewURL(String url, Context context);

        Activity getActivity();

        void addImpressionToTrackingQueue(List<Visitable> visitables);

        void configureHomeFlag(HomeFlag homeFlag);

        void showPopupIntroOvo(String applinkActivation);

        void detectAndSendLocation();

        boolean hasGeolocationPermission();

        boolean needToShowGeolocationComponent();

        TrackingQueue getTrackingQueue();
    }

    interface Presenter extends CustomerPresenter<View> {

        void dismissReview();

        void getSuggestedReview();

        void refreshHomeData();

        void getHeaderData();

        void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletActionData);

        void showPopUpIntroWalletOvo(String applinkActivation);

        void updateHeaderTokoCashPendingData(CashBackData cashBackData);

        void onHeaderTokocashError();

        void onHeaderTokopointError();

        void onRefreshTokoPoint();

        void onRefreshTokoCash();

        void onResume();

        void onDestroy();

        void hitBannerImpression(BannerSlidesModel slidesModel);

        void onBannerClicked(BannerSlidesModel slidesModel);

        void updateHeaderTokoPointData(TokopointsDrawerHomeData tokopointsDrawerHomeData);

        void updateKeywordSearch(KeywordSearchData keywordSearchData);

        void getFeedTabData();

        void onCloseGeolocation();

        void onCloseTicker();

        int getRecommendationFeedSectionPosition();

        void onHomeNetworkRetry();
    }
}
