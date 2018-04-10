package com.tokopedia.digital_deals.view.presenter;


import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TkpdCoreRouter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.GetDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class DealsHomePresenter extends BaseDaggerPresenter<DealsContract.View>
        implements DealsContract.Presenter {

    private GetDealsListRequestUseCase getDealsListRequestUseCase;
    private CategoryViewModel carousel;
    private List<CategoryViewModel> categoryViewModels;
    private TouchViewPager mTouchViewPager;
    private int currentPage, totalPages;
    String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
    String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
    String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";

    @Inject
    public DealsHomePresenter(GetDealsListRequestUseCase getDealsListRequestUseCase) {
        this.getDealsListRequestUseCase = getDealsListRequestUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void startBannerSlide(TouchViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }
        Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (currentPage + 1 < totalPages)
                            ++currentPage;
                        else if (currentPage + 1 >= totalPages) {
                            currentPage = 0;
                        }
                        mTouchViewPager.setCurrentItem(currentPage, true);
                    }
                });
    }

    @Override
    public void onBannerSlide(int page) {
        currentPage = page;
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_menu_search) {
//            ArrayList<SearchViewModel> searchViewModelList = Utils.getSingletonInstance()
//                    .convertIntoSearchViewModel(categoryViewModels);
//            Intent searchIntent = EventSearchActivity.getCallingIntent(getView().getActivity());
//            searchIntent.putParcelableArrayListExtra("TOPEVENTS", searchViewModelList);
//            getView().navigateToActivityRequest(searchIntent,
//                    EventsHomeActivity.REQUEST_CODE_EVENTSEARCHACTIVITY);
        } else if (id == R.id.action_promo) {
            startGeneralWebView(PROMOURL);
        } else if (id == R.id.action_booked_history) {
            startGeneralWebView(TRANSATIONSURL);
        } else if (id == R.id.action_faq) {
            startGeneralWebView(FAQURL);
        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    public void getDealsList() {
        getView().showProgressBar();
        getDealsListRequestUseCase.execute(getView().getParams(), new Subscriber<List<DealsCategoryDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getDealsList();
                    }
                });
            }

            @Override
            public void onNext(List<DealsCategoryDomain> categoryEntities) {
                getView().hideProgressBar();
                categoryViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListViewModel(categoryEntities);
                getCarousel(categoryViewModels);
                getView().renderCategoryList(categoryViewModels);
                getView().showSearchButton();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    public ArrayList<String> getCarouselImages(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ArrayList<String> imagesList = new ArrayList<>();
        if (categoryItemsViewModels != null) {
            for (CategoryItemsViewModel categoryItemsViewModel : categoryItemsViewModels
                    ) {
                imagesList.add(categoryItemsViewModel.getImageWeb());
            }
        }
        return imagesList;
    }

    public void onClickBanner() {
        CategoryItemsViewModel categoryItemsViewModel = carousel.getItems().get(currentPage);
        if (categoryItemsViewModel.getUrl().contains("www.tokopedia.com")
                || categoryItemsViewModel.getUrl().contains("docs.google.com")) {
            startGeneralWebView(categoryItemsViewModel.getUrl());
        } else {
//            Intent intent = new Intent(getView().getActivity(), EventDetailsActivity.class);
//            intent.putExtra("homedata", categoryItemsViewModel);
//            getView().getActivity().startActivity(intent);
        }
    }

    private void getCarousel(List<CategoryViewModel> categoryViewModels) {
        for (CategoryViewModel model : categoryViewModels) {
            if (model.getTitle().equalsIgnoreCase("Carousel")) {
                carousel = model;
                break;
            }
        }
    }

    private void startGeneralWebView(String url) {
        if (getView().getActivity().getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getView().getActivity().getApplication())
                    .actionOpenGeneralWebView(getView().getActivity(), url);
        }
    }

}
