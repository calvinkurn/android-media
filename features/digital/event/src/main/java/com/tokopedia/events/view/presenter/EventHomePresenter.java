package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetProductRatingUseCase;
import com.tokopedia.events.domain.GetUserLikesUseCase;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.domain.scanTicketUsecase.CheckScanOptionUseCase;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventFavouriteActivity;
import com.tokopedia.events.view.activity.EventSearchActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.activity.ScanQRCodeActivity;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.contractor.EventsContract.AdapterCallbacks;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.events.view.utils.Utils.Constants.EXTRA_EVENT_CALENDAR;
import static com.tokopedia.events.view.utils.Utils.Constants.FAQURL;
import static com.tokopedia.events.view.utils.Utils.Constants.PROMOURL;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventHomePresenter extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventsContract.EventHomePresenter {

    private GetEventsListRequestUseCase getEventsListRequestUsecase;
    private PostUpdateEventLikesUseCase postUpdateEventLikesUseCase;
    private GetUserLikesUseCase getUserLikesUseCase;
    private CategoryViewModel carousel;
    private List<CategoryViewModel> categoryViewModels;
    private ArrayList<Integer> likedEventsLocal;
    private TouchViewPager mTouchViewPager;
    private int currentPage, totalPages;
    private List<AdapterCallbacks> adapterCallbacks;
    private boolean showFavAfterLogin = false;
    private boolean showOMS = false;
    private EventsContract.EventHomeView mView;
    private EventsAnalytics eventsAnalytics;


    public EventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase,
                       PostUpdateEventLikesUseCase eventLikesUseCase,
                       GetUserLikesUseCase likesUseCase,
                       GetProductRatingUseCase ratingUseCase, EventsAnalytics eventsAnalytics) {
        this.getEventsListRequestUsecase = getEventsListRequestUsecase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
        this.getUserLikesUseCase = likesUseCase;
        adapterCallbacks = new ArrayList<>();
        this.eventsAnalytics = eventsAnalytics;
    }

    @Override
    public boolean onClickOptionMenu(int id) {
        return false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void startBannerSlide(TouchViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_IMPRESSION, carousel.getItems().get(currentPage).getTitle() +
                " - " + currentPage);
        carousel.getItems().get(currentPage).setTrack(true);
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }
        Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
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
        if (!carousel.getItems().get(currentPage).isTrack()) {
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_IMPRESSION, carousel.getItems().get(currentPage).getTitle() +
                    " - " + currentPage);
            carousel.getItems().get(currentPage).setTrack(true);
        }
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_menu_search) {
            ArrayList<CategoryItemsViewModel> searchViewModelList = (ArrayList<CategoryItemsViewModel>) Utils.getSingletonInstance()
                    .getTopEvents();
            for (CategoryItemsViewModel model : searchViewModelList) {
                if (Utils.getSingletonInstance().containsLikedEvent(model.getId())) {
                    if (!model.isLiked()) {
                        model.setLiked(true);
                        model.setLikes();
                    }
                } else {
                    if (model.isLiked()) {
                        model.setLiked(false);
                        model.setLikes();
                    }
                }
            }
            Intent searchIntent = EventSearchActivity.getCallingIntent(mView.getActivity());
            searchIntent.putParcelableArrayListExtra("TOPEVENTS", searchViewModelList);
            mView.navigateToActivityRequest(searchIntent,
                    EventsHomeActivity.REQUEST_CODE_EVENTSEARCHACTIVITY);
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_SEARCH, "");
            return true;
        } else if (id == R.id.action_promo) {
            startGeneralWebView(PROMOURL);
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_PROMO, "");
            return true;
        } else if (id == R.id.action_booked_history) {
            if (Utils.getUserSession(mView.getActivity()).isLoggedIn())
                RouteManager.route(mView.getActivity(), ApplinkConst.EVENTS_ORDER);
            else {
                showOMS = true;
                Intent intent = ((EventModuleRouter) mView.getActivity().getApplication()).
                        getLoginIntent(mView.getActivity());
                mView.navigateToActivityRequest(intent, 1099);
            }
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_DAFTAR_TRANSAKSI, "");
            return true;
        } else if (id == R.id.action_faq) {
            startGeneralWebView(FAQURL);
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BANTUAN, "");

            return true;
        } else if (id == R.id.action_menu_fav) {
            getFavouriteItemsAndShow();
            return true;
        } else {
            mView.getActivity().onBackPressed();
            return true;
        }
    }

    @Override
    public void showEventDetails(CategoryItemsViewModel model) {
        Intent detailsIntent = new Intent(mView.getActivity(), EventDetailsActivity.class);
        detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
        detailsIntent.putExtra(Utils.Constants.HOMEDATA, model);
        mView.getActivity().startActivity(detailsIntent);
    }

    @Override
    public void setEventLike(final CategoryItemsViewModel model, final int position) {
        Subscriber<LikeUpdateResultDomain> subscriber = new Subscriber<LikeUpdateResultDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("UPDATEEVENTLIKE", e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(LikeUpdateResultDomain likeUpdateResultDomain) {
                Log.d("UPDATEEVENTLIKE", "onNext");
                if (likeUpdateResultDomain.isLiked() && model.isLiked()) {
                    model.setLikes();
                    Utils.getSingletonInstance().addLikedEvent(model.getId());
                } else if (!likeUpdateResultDomain.isLiked() && !model.isLiked()) {
                    model.setLikes();
                    Utils.getSingletonInstance().removeLikedEvent(model.getId());
                }
                for (AdapterCallbacks callbacks : adapterCallbacks)
                    callbacks.notifyDatasetChanged(position);
            }
        };
        if (Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
            Utils.getSingletonInstance().setEventLike(mView.getActivity(), model, postUpdateEventLikesUseCase, subscriber);
        } else {
            mView.showLoginSnackbar("Please Login to like or share events");
        }


    }

    @Override
    public void shareEvent(CategoryItemsViewModel model) {
        Utils.getSingletonInstance().shareEvent(mView.getActivity(), model.getTitle(), model.getSeoUrl(), model.getImageWeb());
    }

    @Override
    public void onActivityResult(int requestCode) {
        if (requestCode == 1099) {
            if (Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
                mView.hideProgressBar();
                if (showFavAfterLogin) {
                    showFavAfterLogin = false;
                    getFavouriteItemsAndShow();
                } else if (showOMS) {
                    RouteManager.route(mView.getActivity(), ApplinkConst.EVENTS_ORDER);
                    showOMS = false;
                }
            } else {
                mView.hideProgressBar();
            }
        }
    }

    @Override
    public void onClickEventCalendar() {
        ArrayList<CategoryItemsViewModel> searchViewModelList = (ArrayList<CategoryItemsViewModel>) Utils.getSingletonInstance()
                .getTopEvents();
        Intent searchIntent = EventSearchActivity.getCallingIntent(mView.getActivity());
        searchIntent.putExtra(EXTRA_EVENT_CALENDAR, true);
        searchIntent.putParcelableArrayListExtra("TOPEVENTS", searchViewModelList);
        mView.navigateToActivityRequest(searchIntent, 1010);
    }

    @Override
    public void setupCallback(AdapterCallbacks callbacks) {
        this.adapterCallbacks.add(callbacks);
    }

    public void getEventsList() {
        mView.showProgressBar();
        getEventsListRequestUsecase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap((Func1<List<EventsCategoryDomain>, Observable<List<Integer>>>) eventsCategoryDomains -> {
                    categoryViewModels = Utils.getSingletonInstance()
                            .convertIntoCategoryListVeiwModel(eventsCategoryDomains);
                    UserSession userSession = new UserSession(mView.getActivity());
                    if (userSession.isLoggedIn())
                        return getUserLikesUseCase.getExecuteObservable(RequestParams.create())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    else {
                        List<Integer> empty = new ArrayList<>();
                        return Observable.just(empty);
                    }
                }).flatMap((Func1<List<Integer>, Observable<List<CategoryViewModel>>>) integers -> {
            if (!integers.isEmpty() || integers.size() > 0)
                for (Integer id : integers) {
                    for (CategoryViewModel category : categoryViewModels) {
                        for (CategoryItemsViewModel itemsViewModel : category.getItems()) {
                            if (itemsViewModel.getId() == id) {
                                itemsViewModel.setLiked(true);
                                itemsViewModel.setWasLiked(true);
                            }
                        }
                    }
                }
            return Observable.just(categoryViewModels);
        }).subscribe(new Subscriber<List<CategoryViewModel>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                mView.hideProgressBar();
                NetworkErrorHelper.showEmptyState(mView.getActivity(), mView.getRootView(), () -> getEventsList());
            }

            @Override
            public void onNext(List<CategoryViewModel> categoryViewModels) {
                mView.hideProgressBar();
                getCarousel(categoryViewModels);
                mView.renderCategoryList(categoryViewModels);
                mView.showSearchButton();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    public ArrayList<String> getCarouselImages(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ArrayList<String> imagesList = new ArrayList<>();
        if (categoryItemsViewModels != null) {
            for (CategoryItemsViewModel categoryItemsViewModel : categoryItemsViewModels
                    ) {
                imagesList.add(categoryItemsViewModel.getImageApp());
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
            Intent intent = new Intent(mView.getActivity(), EventDetailsActivity.class);
            intent.putExtra("homedata", categoryItemsViewModel);
            mView.getActivity().startActivity(intent);
        }
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_CLICK,
                categoryItemsViewModel.getTitle() + "-" + String.valueOf(currentPage));
    }

    private void getCarousel(List<CategoryViewModel> categoryViewModels) {
        for (CategoryViewModel model : categoryViewModels) {
            if (model.getTitle().equals("Carousel")) {
                carousel = model;
                break;
            }
        }
    }

    private void startGeneralWebView(String url) {
        if (mView.getActivity().getApplication() instanceof EventModuleRouter) {
            ((EventModuleRouter) mView.getActivity().getApplication())
                    .actionOpenGeneralWebView(mView.getActivity(), url);
        }
    }


    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_HOMEPAGE;
    }

    private void getFavouriteItemsAndShow() {
        if (Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
            getUserLikesUseCase.getExecuteObservable(RequestParams.create())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap((Func1<List<Integer>, Observable<List<CategoryItemsViewModel>>>) integers -> {
                        List<CategoryItemsViewModel> favouritesItems = new ArrayList<>();
                        for (Integer id : integers) {
                            for (CategoryViewModel category : categoryViewModels) {
                                for (CategoryItemsViewModel itemsViewModel : category.getItems()) {
                                    if (itemsViewModel.getId() == id) {
                                        itemsViewModel.setLiked(true);
                                        favouritesItems.add(itemsViewModel);
                                    }
                                }
                            }
                        }
                        return Observable.just(favouritesItems);
                    }).subscribe(new Subscriber<List<CategoryItemsViewModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    CommonUtils.dumper("enter error");
                    e.printStackTrace();
                    mView.hideProgressBar();
                    NetworkErrorHelper.showEmptyState(mView.getActivity(), mView.getRootView(), () -> getFavouriteItemsAndShow());
                }

                @Override
                public void onNext(List<CategoryItemsViewModel> categoryItemsViewModels) {
                    ArrayList<CategoryItemsViewModel> favItems = (ArrayList<CategoryItemsViewModel>) categoryItemsViewModels;
                    Intent openFavIntent = new Intent(mView.getActivity(), EventFavouriteActivity.class);
                    openFavIntent.putParcelableArrayListExtra(Utils.Constants.FAVOURITEDATA, favItems);
                    mView.navigateToActivityRequest(openFavIntent, 0);
                }
            });
        } else {
            mView.showLoginSnackbar("Please Login to see your liked events");
            showFavAfterLogin = true;
        }
    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventsContract.EventHomeView) view;
        getEventsList();
    }
}
