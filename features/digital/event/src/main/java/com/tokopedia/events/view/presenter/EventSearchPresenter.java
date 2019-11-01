package com.tokopedia.events.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchNextUseCase;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.model.NsqMessage;
import com.tokopedia.events.domain.model.NsqServiceModel;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.events.domain.postusecase.PostNsqEventUseCase;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventFilterActivity;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
        extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventSearchContract.EventSearchPresenter {

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
    private EventSearchContract.EventSearchView mView;
    private EventsAnalytics eventsAnalytics;

    public EventSearchPresenter(GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase,
                                GetSearchNextUseCase searchNextUseCase, PostUpdateEventLikesUseCase eventLikesUseCase, EventsAnalytics eventsAnalytics) {
        this.getSearchEventsListRequestUseCase = getSearchEventsListRequestUseCase;
        this.getSearchNextUseCase = searchNextUseCase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
        adapterCallbacks = new ArrayList<>();
        this.eventsAnalytics = eventsAnalytics;
    }


    private void getEventsListBySearch(String searchText, boolean shouldFireEvent) {
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
        mView.showProgressBar();
        getSearchEventsListRequestUseCase.execute(requestParams, new Subscriber<SearchDomainModel>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgressBar();
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                NetworkErrorHelper.showEmptyState(mView.getActivity(),
                        mView.getRootView(), () -> getEventsListBySearch(highlight, shouldFireEvent));
            }

            @Override
            public void onNext(SearchDomainModel searchDomainModel) {
                mView.setSuggestions(processSearchResponse(searchDomainModel), highlight, showCards, shouldFireEvent);
                checkIfToLoad(mView.getLayoutManager());
                mView.hideProgressBar();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void setupCallback(EventsContract.AdapterCallbacks callbacks) {
        adapterCallbacks.add(callbacks);
    }

    @Override
    public boolean onClickOptionMenu(int id) {
        mView.getActivity().onBackPressed();
        return true;
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
        Utils.getSingletonInstance().setEventLike(mView.getActivity(), model, postUpdateEventLikesUseCase, subscriber);
    }

    @Override
    public void searchTextChanged(String searchText) {
        if (searchText != null) {
            if (searchText.length() > 2) {
                showCards = true;
                getEventsListBySearch(searchText, false);
                searchTag = searchText;
            }
            if (searchText.length() == 0) {
                mView.setTopEvents(mTopEvents);
            }
        } else {
            mView.setTopEvents(mTopEvents);
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        showCards = false;
        getEventsListBySearch(searchText, true);
        searchTag = searchText;
        eventsAnalytics.sendGeneralEvent(EventsAnalytics.EVENT_CLICK_SEARCH, EventsAnalytics.DIGITAL_EVENT, EventsAnalytics.CLICK_SEARCH_ICON, searchText);
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_filter) {

        } else {
            mView.getActivity().onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onSearchResultClick(SearchViewModel searchViewModel, int position) {
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
        Intent detailsIntent = new Intent(mView.getActivity(), EventDetailsActivity.class);
        detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
        detailsIntent.putExtra("homedata", detailsViewModel);
        mView.getActivity().startActivity(detailsIntent);
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
        Intent filterIntent = EventFilterActivity.getCallingIntent(mView.getActivity());
        filterIntent.putExtra(TIME_RANGE, timeFilter);
        filterIntent.putExtra(START_DATE, startDate);
        filterIntent.putExtra(CATEGORY, catgoryFilter);
        mView.navigateToActivityRequest(filterIntent, REQ_OPEN_FILTER);
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
                    mView.setFilterActive();
                } else
                    mView.setFilterInactive();
                if (!category.equals(catgoryFilter) || date != startDate || !time.equals(timeFilter)) {
                    catgoryFilter = category;
                    startDate = date;
                    timeFilter = time;
                    if (!previousSearch.isEmpty() || isEventCalendar || showCards)
                        getEventsListBySearch(previousSearch, false);
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
                mView.removeFooter();
                mView.addEvents(processSearchResponse(searchDomainModel));
                checkIfToLoad(mView.getLayoutManager());
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
                mView.addFooter();
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
        SharedPreferences preferences = mView.getActivity().getSharedPreferences(Utils.Constants.EVENTS_PREFS, Context.MODE_PRIVATE);
        String rawLikes = preferences.getString(Utils.Constants.LIKED_EVENTS, "");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(rawLikes, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray(Utils.Constants.LIKED_EVENTS);
        likedEvents = new ArrayList<>();
        for (JsonElement aJsonArray : jsonArray) {
            likedEvents.add(aJsonArray.getAsInt());
        }
    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventSearchContract.EventSearchView) view;
        isEventCalendar = mView.getActivity().getIntent().
                getBooleanExtra(Utils.Constants.EXTRA_EVENT_CALENDAR, false);
        mTopEvents = (ArrayList<CategoryItemsViewModel>) Utils.getSingletonInstance()
                .getTopEvents();
        if (isEventCalendar) {
            searchSubmitted("");
        } else {
            mView.setTopEvents(mTopEvents);
        }
    }
}
