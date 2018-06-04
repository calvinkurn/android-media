package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchNextUseCase;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.events.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.adapter.FiltersAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.fragment.FilterFragment;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchPresenter
        extends BaseDaggerPresenter<EventSearchContract.IEventSearchView>
        implements EventSearchContract.IEventSearchPresenter {

    private GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase;
    private PostUpdateEventLikesUseCase postUpdateEventLikesUseCase;
    private GetSearchNextUseCase getSearchNextUseCase;
    private String FRAGMENT_TAG = "FILTERFRAGMENT";
    private ArrayList<CategoryItemsViewModel> mTopEvents;
    private SearchDomainModel mSearchData;
    private ValuesItemDomain selectedTime;
    private String catgoryFilters;
    private String timeFilter;
    private String highlight;
    private boolean isLoading;
    private boolean isLastPage;
    private boolean isEventCalendar;
    private final int PAGE_SIZE = 20;
    private String searchTag;
    private List<EventsContract.AdapterCallbacks> adapterCallbacks;
    RequestParams searchNextParams = RequestParams.create();

    @Inject
    public EventSearchPresenter(GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase,
                                GetSearchNextUseCase searchNextUseCase, PostUpdateEventLikesUseCase eventLikesUseCase) {
        this.getSearchEventsListRequestUseCase = getSearchEventsListRequestUseCase;
        this.getSearchNextUseCase = searchNextUseCase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
        adapterCallbacks = new ArrayList<>();
    }

    @Override
    public void getEventsListBySearch(String searchText) {
        highlight = searchText;
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getSearchEventsListRequestUseCase.TAG, searchText);
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
            }

            @Override
            public void onNext(SearchDomainModel searchDomainModel) {
                getView().setSuggestions(processSearchResponse(searchDomainModel), highlight);
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
        if (isEventCalendar) {
            searchSubmitted("");
        } else {
            mTopEvents = getView().getActivity().getIntent().getParcelableArrayListExtra(Utils.Constants.TOPEVENTS);
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
        getEventsListBySearch(searchText);
        searchTag = searchText;
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_SEARCH, searchText);
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_filter) {
            FragmentManager fragmentManager = getView().getFragmentManagerInstance();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            FilterFragment fragment = FilterFragment.newInstance(100);
            fragment.setData(mSearchData.getFilters(), this);
            transaction.add(R.id.main_content, fragment, FRAGMENT_TAG);
            transaction.addToBackStack(FRAGMENT_TAG);
            transaction.commit();
        } else {
            getView().getActivity().onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onClickFilterItem(ValuesItemDomain filterItem, FiltersAdapter.FilterViewHolder viewHolder) {
        if (!filterItem.isMulti()) {
            if (!filterItem.getIsSelected()) {
                if (selectedTime != null)
                    selectedTime.setIsSelected(false);
                filterItem.setIsSelected(true);
                selectedTime = filterItem;
                timeFilter = selectedTime.getName();
            } else {
                filterItem.setIsSelected(false);
                selectedTime = null;
                timeFilter = "";
            }

        } else {
            if (!filterItem.getIsSelected()) {
                filterItem.setIsSelected(true);
                if (catgoryFilters != null && catgoryFilters.length() == 0) {
                    catgoryFilters.concat(",").concat(filterItem.getName());
                } else {
                    catgoryFilters = filterItem.getName();
                }
            } else {
                filterItem.setIsSelected(false);
                catgoryFilters.replace("," + filterItem.getName(), "");
            }
        }
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

    List<CategoryItemsViewModel> processSearchResponse(SearchDomainModel searchDomainModel) {
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
//        return Utils.getSingletonInstance()
//                .convertSearchResultsToModel(categoryItemsViewModels);
    }

    public String getSearchTag() {
        return searchTag;
    }

}
