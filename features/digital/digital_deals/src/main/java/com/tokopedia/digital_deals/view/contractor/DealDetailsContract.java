package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealDetailsContract {

    public interface View extends CustomerView{

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderDealDetails(DealsDetailsResponse detailsViewModel);

        void addDealsToCards(List<ProductItem> productItems);

        void showProgressBar();

        void hideProgressBar();

        RequestParams getParams();

        android.view.View getRootView();

        void hideShareButton();

        void showShareButton();

        void hideCollapsingHeader();

        void showCollapsingHeader();

        void setLikes(int likes, boolean isLiked);

        LinearLayoutManager getLayoutManager();

        void addFooter();

        void removeFooter();



        void hideCheckoutView();

        boolean isEnableBuyFromArguments();

        boolean isRecommendationEnableFromArguments();

        void hideRecomendationDealsView();

        boolean isEnableLikeFromArguments();

        void hideLikeButtonView();

        boolean isEnableShareFromArguments();
    }
    public interface Presenter extends CustomerPresenter<DealDetailsContract.View> {

        void initialize();

        void onDestroy();

        boolean onOptionMenuClick(int id);

        void onBannerSlide(int page);

        void startBannerSlide(TouchViewPager viewPager);

        List<Outlet> getAllOutlets();


    }
}
