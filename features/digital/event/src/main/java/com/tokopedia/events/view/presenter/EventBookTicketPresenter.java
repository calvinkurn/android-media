package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.EventSeatLayoutResonse;
import com.tokopedia.events.domain.GetEventSeatLayoutUseCase;
import com.tokopedia.events.domain.model.request.verify.ValidateShow;
import com.tokopedia.events.domain.postusecase.PostValidateShowUseCase;
import com.tokopedia.events.view.activity.ReviewTicketActivity;
import com.tokopedia.events.view.activity.SeatSelectionActivity;
import com.tokopedia.events.view.adapter.AddTicketAdapter;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.fragment.FragmentAddTickets;
import com.tokopedia.events.view.mapper.SeatLayoutResponseToSeatLayoutViewModelMapper;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.LocationDateModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 28/11/17.
 */
public class EventBookTicketPresenter extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventBookTicketContract.BookTicketPresenter {

    private PackageViewModel selectedPackageViewModel;
    private GetEventSeatLayoutUseCase getSeatLayoutUseCase;
    private SeatLayoutViewModel seatLayoutViewModel;
    private int mSelectedPackage = -1;
    private int mSelectedSchedule = 0;
    private AddTicketAdapter.TicketViewHolder selectedViewHolder;
    private List<SchedulesViewModel> schedulesList;
    private PostValidateShowUseCase postValidateShowUseCase;
    private String eventTitle;
    private String selectedPackageDate;
    private int hasSeatLayout;
    private FragmentAddTickets mChildFragment;
    private int px;
    private EventsDetailsViewModel dataModel;
    private List<LocationDateModel> locationDateModels;
    private EventBookTicketContract.EventBookTicketView mView;
    private EventsAnalytics eventsAnalytics;

    public EventBookTicketPresenter(GetEventSeatLayoutUseCase seatLayoutUseCase, PostValidateShowUseCase useCase, EventsAnalytics eventsAnalytics) {
        this.getSeatLayoutUseCase = seatLayoutUseCase;
        this.postValidateShowUseCase = useCase;
        this.eventsAnalytics = eventsAnalytics;
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
        if (requestCode == mView.getRequestCode()) {
            if (Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
                getProfile();
            } else {
                mView.hideProgressBar();
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    private void getTicketDetails() {
        dataModel = mView.
                getActivity().
                getIntent().
                getParcelableExtra(EventsDetailsPresenter.EXTRA_EVENT_VIEWMODEL);
        hasSeatLayout = mView.getActivity().getIntent().getIntExtra(EventsDetailsPresenter.EXTRA_SEATING_PARAMETER, 0);
        generateLocationDateModels();
        mView.renderFromDetails(dataModel);
        if (dataModel.getSchedulesViewModels() != null && dataModel.getSchedulesViewModels().size() > 0) {
            selectedPackageDate = Utils.getSingletonInstance().convertEpochToString(dataModel.getSchedulesViewModels().get(0).getStartDate());
            schedulesList = dataModel.getSchedulesViewModels();
        }
        if (dataModel.getSeatMapImage() != null && !dataModel.getSeatMapImage().isEmpty())
            mView.renderSeatmap(dataModel.getSeatMapImage());
        else
            mView.hideSeatmap();
    }

    @Override
    public void validateSelection() {
        postValidateShowUseCase.execute(RequestParams.create(), new Subscriber<ValidateResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("BookTicketPresenter", "onError");
                throwable.printStackTrace();
                mView.hideProgressBar();
                NetworkErrorHelper.showEmptyState(mView.getActivity(),
                        mView.getRootView(), () -> validateSelection());
            }

            @Override
            public void onNext(ValidateResponse objectResponse) {
                if (objectResponse.getStatus() != 400) {
                    if (hasSeatLayout == 1 && seatLayoutViewModel.getArea() != null && seatLayoutViewModel.getLayoutDetail() != null && seatLayoutViewModel.getLayoutDetail().size() > 0) {
                        Intent reviewTicketIntent = new Intent(mView.getActivity(), SeatSelectionActivity.class);
                        reviewTicketIntent.putExtra(Utils.Constants.EXTRA_PACKAGEVIEWMODEL, selectedPackageViewModel);
                        reviewTicketIntent.putExtra(Utils.Constants.EXTRA_SEATLAYOUTVIEWMODEL, seatLayoutViewModel);
                        reviewTicketIntent.putExtra("event_detail", dataModel);
                        reviewTicketIntent.putExtra("EventTitle", eventTitle);
                        mView.navigateToActivityRequest(reviewTicketIntent, Utils.Constants.REVIEW_REQUEST);
                    } else {
                        Intent reviewTicketIntent = new Intent(mView.getActivity(), ReviewTicketActivity.class);
                        reviewTicketIntent.putExtra(Utils.Constants.EXTRA_PACKAGEVIEWMODEL, selectedPackageViewModel);
                        reviewTicketIntent.putExtra("event_detail", dataModel);
                        mView.navigateToActivityRequest(reviewTicketIntent, Utils.Constants.REVIEW_REQUEST);
                    }
                } else {
                    mView.showToast(objectResponse.getMessageError(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void getProfile() {
        mView.showProgressBar();
        if (!Utils.getUserSession(mView.getActivity()).isLoggedIn()) {
            Intent intent = ((EventModuleRouter) mView.getActivity().getApplication()).
                    getLoginIntent(mView.getActivity());
            mView.navigateToActivityRequest(intent, mView.getRequestCode());
        } else {
            if (hasSeatLayout == 1)
                getSeatSelectionDetails();
            else
                validateSelection();
        }

    }

    @Override
    public void payTicketsClick(String title) {
        eventTitle = title;
        selectedPackageViewModel.setTimeRange(selectedPackageDate);
        ValidateShow validateShow = new ValidateShow();
        validateShow.setQuantity(selectedPackageViewModel.getSelectedQuantity());
        validateShow.setGroupId(selectedPackageViewModel.getProductGroupId());
        validateShow.setPackageId(selectedPackageViewModel.getId());
        validateShow.setScheduleId(selectedPackageViewModel.getProductScheduleId());
        validateShow.setProductId(selectedPackageViewModel.getProductId());
        postValidateShowUseCase.setValidateShowModel(validateShow);
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CHECKOUT, selectedPackageViewModel.getTitle().toLowerCase() + " - " +
                selectedPackageViewModel.getDisplayName().toLowerCase() + " - " +
                CurrencyUtil.convertToCurrencyString(selectedPackageViewModel.getSalesPrice() * selectedPackageViewModel.getSelectedQuantity()));
        getProfile();
    }

    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_TICKET_PAGE;
    }

    public void addTickets(int index, PackageViewModel packageVM, AddTicketAdapter.TicketViewHolder ticketViewHolder) {
        if (mSelectedPackage != -1 && mSelectedPackage != index) {
            selectedPackageViewModel.setSelectedQuantity(0);
            selectedViewHolder.setTvTicketCnt(selectedPackageViewModel.getSelectedQuantity());
            selectedViewHolder.setTicketViewColor(mView.getActivity().getResources().getColor(R.color.white));
            selectedViewHolder.toggleMinTicketWarning(View.INVISIBLE, selectedPackageViewModel.getMinQty());
            selectedViewHolder.toggleMaxTicketWarning(View.INVISIBLE, selectedPackageViewModel.getSelectedQuantity());
            mSelectedPackage = index;
            selectedPackageViewModel = packageVM;
            selectedViewHolder = ticketViewHolder;
            scrollToLastIfNeeded();
        } else if (mSelectedPackage == -1) {
            mSelectedPackage = index;
            selectedPackageViewModel = packageVM;
            selectedViewHolder = ticketViewHolder;
            scrollToLastIfNeeded();
        }
        int selectedCount = selectedPackageViewModel.getSelectedQuantity();
        if (selectedCount < selectedPackageViewModel.getAvailable()
                && selectedCount < selectedPackageViewModel.getMaxQty()) {
            selectedPackageViewModel.setSelectedQuantity(++selectedCount);
            selectedViewHolder.setTvTicketCnt(selectedCount);
            selectedViewHolder.setTicketViewColor(mView.getActivity().getResources().getColor(R.color.light_green));
        }
        if (selectedCount >= selectedPackageViewModel.getAvailable() ||
                selectedCount >= selectedPackageViewModel.getMaxQty()) {
            selectedViewHolder.toggleMaxTicketWarning(View.VISIBLE, selectedPackageViewModel.getSelectedQuantity());
        }
        if (selectedCount < selectedPackageViewModel.getMinQty()) {
            selectedViewHolder.toggleMinTicketWarning(View.VISIBLE, selectedPackageViewModel.getMinQty());
            mView.hidePayButton();
        } else {
            mView.showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice(), selectedPackageViewModel.getDisplayName());
        }
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_ADD_TICKET, "add - " + selectedPackageViewModel.getTitle().toLowerCase() + " - " +
                selectedPackageViewModel.getDisplayName() + " - " +
                CurrencyUtil.convertToCurrencyString(selectedPackageViewModel.getSalesPrice() * selectedPackageViewModel.getSelectedQuantity()).toLowerCase());
    }

    public void removeTickets() {
        int selectedCount = selectedPackageViewModel.getSelectedQuantity();
        if (selectedCount != 0) {
            selectedPackageViewModel.setSelectedQuantity(--selectedCount);
            selectedViewHolder.setTvTicketCnt(selectedCount);
            selectedViewHolder.toggleMaxTicketWarning(View.INVISIBLE, selectedPackageViewModel.getMaxQty());
            if (selectedCount < selectedPackageViewModel.getMinQty()) {
                selectedViewHolder.toggleMinTicketWarning(View.VISIBLE, selectedPackageViewModel.getMinQty());
                mView.hidePayButton();
            } else {
                selectedViewHolder.toggleMinTicketWarning(View.INVISIBLE, selectedPackageViewModel.getMinQty());
                mView.showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice(), selectedPackageViewModel.getDisplayName());
            }
        }
        if (selectedCount == 0) {
            selectedViewHolder.setTicketViewColor(mView.getActivity().getResources().getColor(R.color.white));
            selectedViewHolder.toggleMinTicketWarning(View.INVISIBLE, selectedPackageViewModel.getMinQty());
            mSelectedPackage = -1;
            selectedViewHolder = null;
            mChildFragment.setDecorationHeight(0);
            mView.hidePayButton();
        }
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_REMOVE_TICKET, "remove - " + selectedPackageViewModel.getTitle().toLowerCase() + " - " +
                selectedPackageViewModel.getDisplayName() + " - " +
                CurrencyUtil.convertToCurrencyString(selectedPackageViewModel.getSalesPrice() * selectedPackageViewModel.getSelectedQuantity()).toLowerCase());
    }

    private void getSeatSelectionDetails() {
        RequestParams params = RequestParams.create();
        params.putString("seatlayouturl", selectedPackageViewModel.getFetchSectionUrl());
        mView.showProgressBar();
        getSeatLayoutUseCase.execute(params, new Subscriber<List<SeatLayoutItem>>() {
            @Override
            public void onCompleted() {
                Log.d("Naveen", " on Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                mView.hideProgressBar();
                NetworkErrorHelper.showEmptyState(mView.getActivity(),
                        mView.getRootView(), () -> getSeatSelectionDetails());
            }

            @Override
            public void onNext(List<SeatLayoutItem> response) {
                seatLayoutViewModel = convertResponseToViewModel(convertoSeatLayoutResponse(response.get(0)));
                validateSelection();
            }

        });

    }

    private EventSeatLayoutResonse convertoSeatLayoutResponse(SeatLayoutItem responseEntity) {

        String data = responseEntity.getLayout();
        Gson gson = new Gson();
        return gson.fromJson(data, EventSeatLayoutResonse.class);
    }


    private SeatLayoutViewModel convertResponseToViewModel(EventSeatLayoutResonse response) {
        seatLayoutViewModel = new SeatLayoutViewModel();
        seatLayoutViewModel = SeatLayoutResponseToSeatLayoutViewModelMapper.map(response, seatLayoutViewModel);

        return seatLayoutViewModel;
    }

    public void setChildFragment(FragmentAddTickets childFragment) {
        mChildFragment = childFragment;
    }

    private void scrollToLastIfNeeded() {
        if (schedulesList.get(mSelectedSchedule).getPackages().size() > 1) {
            mChildFragment.setDecorationHeight(mView.getButtonLayoutHeight() + px);
            if (mSelectedPackage == schedulesList.get(mSelectedSchedule).getPackages().size() - 1)
                mChildFragment.scrollToLast();
        }
    }

    public void onClickLocationDate(LocationDateModel model, int index) {
        SchedulesViewModel selectedSchedule = dataModel.getSchedulesViewModels().get(index);
        mView.setLocationDate(model.getmLocation(), model.getDate(), selectedSchedule);
        if (dataModel.getTimeRange() != null && dataModel.getTimeRange().length() > 1)
            selectedPackageDate = Utils.getSingletonInstance().convertEpochToString(selectedSchedule.getStartDate());
        mSelectedSchedule = index;
    }

    private void generateLocationDateModels() {
        locationDateModels = new ArrayList<>();

        List<SchedulesViewModel> schedulesViewModelList = new ArrayList<>();
        if (dataModel != null) {
            schedulesViewModelList = dataModel.getSchedulesViewModels();
            if (schedulesViewModelList != null && !schedulesViewModelList.isEmpty()) {
                for (SchedulesViewModel viewModel : schedulesViewModelList) {
                    LocationDateModel model = new LocationDateModel();
                    model.setmLocation(viewModel.getCityName());
                    if (dataModel.getTimeRange() != null && dataModel.getTimeRange().length() > 1)
                        model.setDate(Utils.getSingletonInstance().convertEpochToString(viewModel.getStartDate()));
                    else
                        model.setDate("");
                    locationDateModels.add(model);
                }
            }
        }
    }

    public List<LocationDateModel> getLocationDateModels() {
        return locationDateModels;
    }

    public void resetViewHolders() {
        if (selectedPackageViewModel != null) {
            selectedPackageViewModel.setSelectedQuantity(0);
            selectedPackageViewModel = null;
        }
        if (selectedViewHolder != null) {
            selectedViewHolder.setTicketViewColor(mView.getActivity().getResources().getColor(R.color.white));
            selectedViewHolder = null;
        }
        mSelectedPackage = -1;
        mView.hidePayButton();
    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventBookTicketContract.EventBookTicketView) view;
        getTicketDetails();
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mView.getActivity().getResources().getDisplayMetrics());
    }
}
