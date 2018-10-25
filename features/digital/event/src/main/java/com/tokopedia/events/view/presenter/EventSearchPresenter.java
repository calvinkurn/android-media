package com.tokopedia.events.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchNextUseCase;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventFilterActivity;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import rx.Subscriber;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY;
import static com.tokopedia.events.view.contractor.EventFilterContract.REQ_OPEN_FILTER;
import static com.tokopedia.events.view.contractor.EventFilterContract.START_DATE;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_RANGE;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchPresenter
        extends BaseDaggerPresenter<EventSearchContract.IEventSearchView>
        implements EventSearchContract.IEventSearchPresenter {

    private GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase;
    private PostUpdateEventLikesUseCase postUpdateEventLikesUseCase;
    private GetSearchNextUseCase getSearchNextUseCase;
    private ArrayList<CategoryItemsViewModel> mTopEvents;
    private ArrayList<Integer> likedEvents;
    private SearchDomainModel mSearchData;
    private String catgoryFilter = "";
    private String timeFilter = "";
    private boolean sendLikeBroadcast = false;
    private long startDate = 0;
    private String highlight;
    private String previousSearch = "";
    private boolean isLoading;
    private boolean isLastPage;
    private boolean showCards;
    private final int PAGE_SIZE = 20;
    private boolean isEventCalendar;
    private String searchTag;
    private List<EventsContract.AdapterCallbacks> adapterCallbacks;
    private RequestParams searchNextParams = RequestParams.create();

    @Inject
    public EventSearchPresenter(GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase,
                                GetSearchNextUseCase searchNextUseCase, PostUpdateEventLikesUseCase eventLikesUseCase) {
        this.getSearchEventsListRequestUseCase = getSearchEventsListRequestUseCase;
        this.getSearchNextUseCase = searchNextUseCase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
        adapterCallbacks = new ArrayList<>();
    }


    private void getEventsListBySearch(String searchText) {
        highlight = searchText;
        previousSearch = searchText;
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getSearchEventsListRequestUseCase.TAG, searchText);
        requestParams.putString(getSearchEventsListRequestUseCase.CATEGORY_ID, catgoryFilter);
        requestParams.putString(getSearchEventsListRequestUseCase.TIME, timeFilter);
        if (startDate > 0) {
            long start = startDate / 1000;
            requestParams.putString(getSearchEventsListRequestUseCase.START_DATE, String.valueOf(start));
        }
        getView().showProgressBar();
        getSearchEventsListRequestUseCase.execute(requestParams, new Subscriber<SearchDomainModel>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressBar();
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), () -> getEventsListBySearch(highlight));
            }

            @Override
            public void onNext(SearchDomainModel searchDomainModel) {
                getView().setSuggestions(processSearchResponse(searchDomainModel), highlight, showCards);
                checkIfToLoad(getView().getLayoutManager());
                getView().hideProgressBar();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void setupCallback(EventsContract.AdapterCallbacks callbacks) {
        adapterCallbacks.add(callbacks);
    }

    @Override
    public void initialize() {
        isEventCalendar = getView().getActivity().getIntent().
                getBooleanExtra(Utils.Constants.EXTRA_EVENT_CALENDAR, false);
        mTopEvents = getView().getActivity().getIntent().getParcelableArrayListExtra(Utils.Constants.TOPEVENTS);
        //getLikedEvents();
        if (isEventCalendar) {
            searchSubmitted("");
        } else {
            getView().setTopEvents(mTopEvents);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setEventLike(final CategoryItemsViewModel model, final int position) {
        Subscriber<LikeUpdateResultDomain> subscriber = new Subscriber<LikeUpdateResultDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(LikeUpdateResultDomain likeUpdateResultDomain) {
                if (likeUpdateResultDomain.isLiked() && model.isLiked()) {
                    model.setLikes();
                    Utils.getSingletonInstance().addLikedEvent(model.getId());
                    Utils.getSingletonInstance().removeUnlikedEvent(model.getId());
                } else if (!likeUpdateResultDomain.isLiked() && !model.isLiked()) {
                    model.setLikes();
                    Utils.getSingletonInstance().removeLikedEvent(model.getId());
                    Utils.getSingletonInstance().addUnlikedEvent(model.getId());
                }
                for (EventsContract.AdapterCallbacks adapterCallback : adapterCallbacks)
                    adapterCallback.notifyDatasetChanged(position);
            }
        };
        Utils.getSingletonInstance().setEventLike(getView().getActivity(), model, postUpdateEventLikesUseCase, subscriber);
    }

    @Override
    public void searchTextChanged(String searchText) {
        if (searchText != null) {
            if (searchText.length() > 2) {
                showCards = false;
                getEventsListBySearch(searchText);
                searchTag = searchText;
                UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_SEARCH, searchText);
            }
            if (searchText.length() == 0) {
                getView().setTopEvents(mTopEvents);
            }
        } else {
            getView().setTopEvents(mTopEvents);
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        showCards = true;
        getEventsListBySearch(searchText);
        searchTag = searchText;
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_SEARCH, searchText);
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_filter) {

        } else {
            getView().getActivity().onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onSearchResultClick(SearchViewModel searchViewModel, int position) {
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_SEARCH_CLICK, searchTag + " - " +
                searchViewModel.getTitle() + " - " + position);
        CategoryItemsViewModel detailsViewModel = new CategoryItemsViewModel();
        detailsViewModel.setTitle(searchViewModel.getTitle());
        detailsViewModel.setDisplayName(searchViewModel.getDisplayName());
        detailsViewModel.setUrl(searchViewModel.getUrl());
        detailsViewModel.setImageApp(searchViewModel.getImageApp());
        detailsViewModel.setMinStartDate(searchViewModel.getMinStartDate());
        detailsViewModel.setMaxEndDate(searchViewModel.getMaxEndDate());
        detailsViewModel.setCityName(searchViewModel.getCityName());
        detailsViewModel.setSalesPrice(searchViewModel.getSalesPrice());
        detailsViewModel.setIsTop(searchViewModel.getIsTop());
        detailsViewModel.setLongRichDesc("Fetching Description");
        detailsViewModel.setTnc("Fetching TnC");
        Intent detailsIntent = new Intent(getView().getActivity(), EventDetailsActivity.class);
        detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
        detailsIntent.putExtra("homedata", detailsViewModel);
        getView().getActivity().startActivity(detailsIntent);
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_SEARCHPAGE;
    }

    @Override
    public void openFilters() {
        Intent filterIntent = EventFilterActivity.getCallingIntent(getView().getActivity());
        filterIntent.putExtra(TIME_RANGE, timeFilter);
        filterIntent.putExtra(START_DATE, startDate);
        filterIntent.putExtra(CATEGORY, catgoryFilter);
        getView().navigateToActivityRequest(filterIntent, REQ_OPEN_FILTER);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (requestcode == REQ_OPEN_FILTER) {
            if (resultcode == RESULT_OK) {
                String time;
                long date;
                String category;
                time = data.getStringExtra(TIME_RANGE);
                date = data.getLongExtra(START_DATE, 0);
                category = data.getStringExtra(CATEGORY);
                if (!category.isEmpty() || date != 0 || !time.isEmpty()) {
                    getView().setFilterActive();
                } else
                    getView().setFilterInactive();
                if (!category.equals(catgoryFilter) || date != startDate || !time.equals(timeFilter)) {
                    catgoryFilter = category;
                    startDate = date;
                    timeFilter = time;
                    if (!previousSearch.isEmpty() || isEventCalendar || showCards)
                        getEventsListBySearch(previousSearch);
                }
            }
        }
    }

    private void loadMoreItems() {
        isLoading = true;

        getSearchNextUseCase.execute(searchNextParams, new Subscriber<SearchDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                isLoading = false;
            }

            @Override
            public void onNext(SearchDomainModel searchDomainModel) {
                isLoading = false;
                getView().removeFooter();
                getView().addEvents(processSearchResponse(searchDomainModel));
                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            } else {
                getView().addFooter();
            }
        }
    }

    private List<CategoryItemsViewModel> processSearchResponse(SearchDomainModel searchDomainModel) {
        mSearchData = searchDomainModel;
        String nexturl = mSearchData.getPage().getUriNext();
        if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
            searchNextParams.putString("nexturl", nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
        return Utils.getSingletonInstance()
                .convertIntoCategoryListItemsVeiwModel(searchDomainModel.getEvents());
    }

    private void getLikedEvents() {
        SharedPreferences preferences = getView().getActivity().getSharedPreferences(Utils.Constants.EVENTS_PREFS, Context.MODE_PRIVATE);
        String rawLikes = preferences.getString(Utils.Constants.LIKED_EVENTS, "");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(rawLikes, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray(Utils.Constants.LIKED_EVENTS);
        likedEvents = new ArrayList<>();
        for (JsonElement aJsonArray : jsonArray) {
            likedEvents.add(aJsonArray.getAsInt());
        }
    }
}
