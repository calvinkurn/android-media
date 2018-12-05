package com.tokopedia.flight.searchV2.presentation.contract;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModelList;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightPriceViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;

import java.util.List;

/**
 * @author by furqan on 01/10/18.
 */

public interface FlightSearchContract {

    interface View extends CustomerView {

        Activity getActivity();

        FlightSearchPassDataViewModel getFlightSearchPassData();

        boolean isReturning();

        boolean isNeedRefreshFromCache();

        boolean isDoneLoadData();

        boolean isNeedRefreshAirline();

        void loadInitialData();

        void fetchFlightSearchData();

        void reloadDataFromCache();

        void renderSearchList(List<FlightJourneyViewModel> list, boolean needRefresh);

        void addToolbarElevation();

        void addBottomPaddingForSortAndFilterActionButton();

        void setUIMarkFilter();

        void setNeedRefreshFromCache(boolean needRefreshFromCache);

        void setFlightSearchPassData(FlightSearchPassDataViewModel passDataViewModel);

        void setSelectedSortItem(int itemId);

        void setNeedRefreshAirline(boolean needRefresh);

        void showDepartureDateMaxTwoYears(int resId);

        void showDepartureDateShouldAtLeastToday(int resId);

        void showReturnDateShouldGreaterOrEqual(int resId);

        void showFilterAndSortView();

        void showEmptyFlightStateView();

        void showNoRouteFlightEmptyState(String message);

        void showGetListError(Throwable e);

        void hideHorizontalProgress();

        void hideFilterAndSortView();

        void removeToolbarElevation();

        void removeBottomPaddingForSortAndFilterActionButton();

        void clearAdapterData();

        void finishFragment();

        void navigateToNextPage(String selectedId, FlightPriceViewModel fareViewModel, boolean isBestPairing);

        void onGetSearchMeta(FlightSearchMetaViewModel flightSearchMetaViewModel);

        void onSuccessGetDetailFlightDeparture(FlightJourneyViewModel flightJourneyViewModel);

        void onErrorDeleteFlightCache(Throwable throwable);

        void onSuccessDeleteFlightCache();

        FlightFilterModel getFilterModel();
    }

    interface Presenter {

        void initialize();

        void onSeeDetailItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition);

        void onSearchItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition);

        void onSearchItemClicked(FlightJourneyViewModel journeyViewModel);

        void onSearchItemClicked(String selectedId);

        void onSuccessDateChanged(int year, int month, int dayOfMonth);

        void setDelayHorizontalProgress();

        void getDetailDepartureFlight(String journeyId);

        void fetchCombineData(FlightSearchPassDataViewModel passDataViewModel);

        void fetchSearchData(FlightSearchPassDataViewModel passDataViewModel, FlightAirportCombineModelList flightAirportCombineModelList);

        void fetchSearchDataFromCloud(FlightSearchPassDataViewModel passDataViewModel, FlightAirportCombineModel flightAirportCombineModelList);

        void fetchSearchDataFromCloudWithDelay(FlightSearchPassDataViewModel passDataViewModel, FlightAirportCombineModel flightAirportCombineModelList, int delayInSecond);

        void fetchSortAndFilterLocalData(@FlightSortOption int flightSortOption, FlightFilterModel flightFilterModel, boolean needRefresh);

        boolean isDoneLoadData();

        void resetCounterCall();
    }

}
