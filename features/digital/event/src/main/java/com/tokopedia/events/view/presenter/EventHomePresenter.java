package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetProductRatingUseCase;
import com.tokopedia.events.domain.GetUserLikesUseCase;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventFavouriteActivity;
import com.tokopedia.events.view.activity.EventSearchActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.contractor.EventsContract.AdapterCallbacks;
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

public class EventHomePresenter extends BaseDaggerPresenter<EventsContract.View>
        implements EventsContract.Presenter {

    private GetEventsListRequestUseCase getEventsListRequestUsecase;
    private PostUpdateEventLikesUseCase postUpdateEventLikesUseCase;
    private GetUserLikesUseCase getUserLikesUseCase;
    private GetProductRatingUseCase getProductRatingUseCase;
    private CategoryViewModel carousel;
    private List<CategoryViewModel> categoryViewModels;
    private List<CategoryItemsViewModel> likedEvents;
    private ArrayList<Integer> likedEventsLocal;
    private TouchViewPager mTouchViewPager;
    private int currentPage, totalPages;
    private List<AdapterCallbacks> adapterCallbacks;
    private boolean showFavAfterLogin = false;
    private boolean showOMS = false;


    @Inject

    public EventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase,
                              PostUpdateEventLikesUseCase eventLikesUseCase,
                              GetUserLikesUseCase likesUseCase,
                              GetProductRatingUseCase ratingUseCase) {
        this.getEventsListRequestUsecase = getEventsListRequestUsecase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
        this.getUserLikesUseCase = likesUseCase;
        this.getProductRatingUseCase = ratingUseCase;
        adapterCallbacks = new ArrayList<>();
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
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_IMPRESSION, carousel.getItems().get(currentPage).getTitle() +
                " - " + currentPage);
        carousel.getItems().get(currentPage).setTrack(true);
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
        if (!carousel.getItems().get(currentPage).isTrack()) {
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_IMPRESSION, carousel.getItems().get(currentPage).getTitle() +
                    " - " + currentPage);
            carousel.getItems().get(currentPage).setTrack(true);
        }
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_menu_search) {
            ArrayList<CategoryItemsViewModel> searchViewModelList = (ArrayList<CategoryItemsViewModel>) Utils.getSingletonInstance()
                    .getTopEvents();
            Intent searchIntent = EventSearchActivity.getCallingIntent(getView().getActivity());
            searchIntent.putParcelableArrayListExtra("TOPEVENTS", searchViewModelList);
            getView().navigateToActivityRequest(searchIntent,
                    EventsHomeActivity.REQUEST_CODE_EVENTSEARCHACTIVITY);
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_SEARCH, "");
            return true;
        } else if (id == R.id.action_promo) {
            startGeneralWebView(PROMOURL);
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_PROMO, "");
            return true;
        } else if (id == R.id.action_booked_history) {
            if (SessionHandler.isV4Login(getView().getActivity()))
                RouteManager.route(getView().getActivity(), ApplinkConst.EVENTS_ORDER);
            else {
                showOMS = true;
                Intent intent = ((TkpdCoreRouter) getView().getActivity().getApplication()).
                        getLoginIntent(getView().getActivity());
                getView().navigateToActivityRequest(intent, 1099);
            }
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_DAFTAR_TRANSAKSI, "");
            return true;
        } else if (id == R.id.action_faq) {
            startGeneralWebView(FAQURL);
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BANTUAN, "");

            return true;
        } else if (id == R.id.action_menu_fav) {
            getFavouriteItemsAndShow();
            return true;
        } else {
            getView().getActivity().onBackPressed();
            return true;
        }
    }

    @Override
    public void showEventDetails(CategoryItemsViewModel model) {
        Intent detailsIntent = new Intent(getView().getActivity(), EventDetailsActivity.class);
        detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
        detailsIntent.putExtra(Utils.Constants.HOMEDATA, model);
        getView().getActivity().startActivity(detailsIntent);
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
                } else if (!likeUpdateResultDomain.isLiked() && !model.isLiked()) {
                    model.setLikes();
                }
                for (AdapterCallbacks callbacks : adapterCallbacks)
                    callbacks.notifyDatasetChanged(position);
            }
        };
        if (SessionHandler.isV4Login(getView().getActivity())) {
            Utils.getSingletonInstance().setEventLike(getView().getActivity(), model, postUpdateEventLikesUseCase, subscriber);
        } else {
            getView().showLoginSnackbar("Please Login to like or share events");
        }


    }

    @Override
    public void shareEvent(CategoryItemsViewModel model) {
        Utils.getSingletonInstance().shareEvent(getView().getActivity(), model.getTitle(), model.getSeoUrl());
    }

    @Override
    public void onActivityResult(int requestCode) {
        if (requestCode == 1099) {
            if (SessionHandler.isV4Login(getView().getActivity())) {
                getView().hideProgressBar();
                if (showFavAfterLogin) {
                    showFavAfterLogin = false;
                    getFavouriteItemsAndShow();
                } else if (showOMS) {
                    RouteManager.route(getView().getActivity(), ApplinkConst.EVENTS_ORDER);
                    showOMS = false;
                }
            } else {
                getView().hideProgressBar();
            }
        }
    }

    @Override
    public void onClickEventCalendar() {
        ArrayList<CategoryItemsViewModel> searchViewModelList = (ArrayList<CategoryItemsViewModel>) Utils.getSingletonInstance()
                .getTopEvents();
        Intent searchIntent = EventSearchActivity.getCallingIntent(getView().getActivity());
        searchIntent.putExtra(EXTRA_EVENT_CALENDAR, true);
        searchIntent.putParcelableArrayListExtra("TOPEVENTS", searchViewModelList);
        getView().navigateToActivityRequest(searchIntent, 1010);
    }

    @Override
    public void setupCallback(AdapterCallbacks callbacks) {
        this.adapterCallbacks.add(callbacks);
    }

    public void getEventsList() {
        getView().showProgressBar();
        getEventsListRequestUsecase.getExecuteObservableAsync(getView().getParams())
                .concatMap(new Func1<List<EventsCategoryDomain>, Observable<List<Integer>>>() {
                    @Override
                    public Observable<List<Integer>> call(List<EventsCategoryDomain> eventsCategoryDomains) {
                        categoryViewModels = Utils.getSingletonInstance()
                                .convertIntoCategoryListVeiwModel(eventsCategoryDomains);
                        UserSession userSession = new UserSession(getView().getActivity());
                        if (userSession.isLoggedIn())
                            return getUserLikesUseCase.getExecuteObservable(RequestParams.create())
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread());
                        else {
                            List<Integer> empty = new ArrayList<>();
                            return Observable.just(empty);
                        }
                    }
                }).flatMap(new Func1<List<Integer>, Observable<List<CategoryViewModel>>>() {
            @Override
            public Observable<List<CategoryViewModel>> call(List<Integer> integers) {
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
            }
        }).subscribe(new Subscriber<List<CategoryViewModel>>() {
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
                        getEventsList();
                    }
                });
            }

            @Override
            public void onNext(List<CategoryViewModel> categoryViewModels) {
                getView().hideProgressBar();
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
            Intent intent = new Intent(getView().getActivity(), EventDetailsActivity.class);
            intent.putExtra("homedata", categoryItemsViewModel);
            getView().getActivity().startActivity(intent);
        }
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_CLICK,
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
        if (getView().getActivity().getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getView().getActivity().getApplication())
                    .actionOpenGeneralWebView(getView().getActivity(), url);
        }
    }


    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_HOMEPAGE;
    }

    private void getFavouriteItemsAndShow() {
        if (SessionHandler.isV4Login(getView().getActivity())) {
            getUserLikesUseCase.getExecuteObservable(RequestParams.create())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<List<Integer>, Observable<List<CategoryItemsViewModel>>>() {
                        @Override
                        public Observable<List<CategoryItemsViewModel>> call(List<Integer> integers) {
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
                        }
                    }).subscribe(new Subscriber<List<CategoryItemsViewModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    CommonUtils.dumper("enter error");
                    e.printStackTrace();
                    getView().hideProgressBar();
                    NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getFavouriteItemsAndShow();
                        }
                    });
                }

                @Override
                public void onNext(List<CategoryItemsViewModel> categoryItemsViewModels) {
                    ArrayList<CategoryItemsViewModel> favItems = (ArrayList<CategoryItemsViewModel>) categoryItemsViewModels;
                    Intent openFavIntent = new Intent(getView().getActivity(), EventFavouriteActivity.class);
                    openFavIntent.putParcelableArrayListExtra(Utils.Constants.FAVOURITEDATA, favItems);
                    getView().navigateToActivityRequest(openFavIntent, 0);
                }
            });
        } else {
            getView().showLoginSnackbar("Please Login to see your liked events");
            showFavAfterLogin = true;
        }
    }
}
