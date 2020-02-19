package com.tokopedia.events.view.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.coachmark.CoachMark;
import com.tokopedia.coachmark.CoachMarkBuilder;
import com.tokopedia.coachmark.CoachMarkContentPosition;
import com.tokopedia.coachmark.CoachMarkItem;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.customview.SelectEventDateBottomSheet;
import com.tokopedia.events.view.fragment.FragmentAddTickets;
import com.tokopedia.events.view.fragment.LocationDateBottomSheetFragment;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.FinishActivityReceiver;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.LocationDateModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;
import com.tokopedia.unifycomponents.ticker.Ticker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventBookTicketActivity
        extends EventBaseActivity implements EventBookTicketContract.EventBookTicketView, View.OnClickListener, SelectEventDateBottomSheet.SelectedDates {


    private static String EVENT_DATE_COACH_MARK_TAG = "EVEN_DATE_COACH_MARK";
    private static String EVENT_DATE_COACH_MARK = "EventCoachMark";
    View buttonPayTickets;
    TextView buttonTextview;
    View progressBarLayout;
    ProgressBar progBar;
    ImageView imgvSeatingLayout;
    FrameLayout mainContent;
    View seatMap;
    TextView ticketCount;
    TextView totalPrice;
    TextView tvUbahJadwal;
    TextView tvLocation;
    TextView tvDate;
    View buttonCountLayout;
    RelativeLayout eventDateLayout;
    View ticketInfoLayout;
    Ticker infoTicker;


    EventBookTicketContract.BookTicketPresenter bookTicketPresenter;
    private String title;
    EventsAnalytics eventsAnalytics;
    EventsDetailsViewModel eventsDetailsViewModel;
    private LocationDateBottomSheetFragment locationFragment;
    private FinishActivityReceiver finishReceiver = new FinishActivityReceiver(this);


    private static final int EVENT_LOGIN_REQUEST = 1099;
    private static final String BOOK_TICKET_FRAGMENT = "bookticketfragment";


    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getEventBookTicketPresenter();
        bookTicketPresenter = (EventBookTicketContract.BookTicketPresenter) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.book_ticket_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventModuleRouter.ACTION_CLOSE_ACTIVITY);
        eventsAnalytics = new EventsAnalytics();
        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, intentFilter);
    }

    @Override
    void setupVariables() {
        buttonPayTickets = findViewById(R.id.pay_tickets);
        buttonTextview = findViewById(R.id.button_textview);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        progBar = findViewById(R.id.prog_bar);
        imgvSeatingLayout = findViewById(R.id.imgv_seating_layout);
        mainContent = findViewById(R.id.main_content);
        seatMap = findViewById(R.id.seating_layout_card);
        ticketCount = findViewById(R.id.ticketcount);
        totalPrice = findViewById(R.id.totalprice);
        tvUbahJadwal = findViewById(R.id.tv_ubah_jadwal);
        tvLocation = findViewById(R.id.tv_location_bta);
        tvDate = findViewById(R.id.tv_day_time_bta);
        buttonCountLayout = findViewById(R.id.button_count_layout);
        ticketInfoLayout = findViewById(R.id.ticket_info);
        eventDateLayout = findViewById(R.id.event_date_layout);
        infoTicker = findViewById(R.id.policy_ticker);
        buttonPayTickets.setOnClickListener(this);
        tvUbahJadwal.setOnClickListener(this);
    }


    private void showEventDateCoachMark() {
        ArrayList<CoachMarkItem> coachItems = new ArrayList<>();
        coachItems.add(new CoachMarkItem(eventDateLayout, getString(R.string.coachicon_event_date), getString(R.string.coachicon_event_date_description)));
        CoachMark coachMark = new CoachMarkBuilder().allowPreviousButton(false).build();
        coachMark.setShowCaseStepListener((prev, next, coachMarkItem) -> false);
        if (!Utils.hasShown(getActivity(), EVENT_DATE_COACH_MARK_TAG)) {
            coachMark.show(getActivity(), EVENT_DATE_COACH_MARK, coachItems);
            Utils.setShown(getActivity(), EVENT_DATE_COACH_MARK_TAG, true);
        }
    }

    @Override
    public void renderFromDetails(EventsDetailsViewModel detailsViewModel) {
        if (detailsViewModel != null) {
            infoTicker.setVisibility(View.VISIBLE);
            eventsDetailsViewModel = detailsViewModel;
            toolbar.setTitle(detailsViewModel.getTitle());
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
            title = detailsViewModel.getTitle();
            if (detailsViewModel.getSchedulesViewModels() != null) {
                if (detailsViewModel.getSchedulesViewModels().size() > 1) {
                    showEventDateCoachMark();
                    tvUbahJadwal.setVisibility(View.VISIBLE);
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setText(Utils.getSingletonInstance().convertEpochToSelectedDateFormat(detailsViewModel.getSchedulesViewModels().get(0).getStartDate()));
                } else {
                    tvDate.setVisibility(View.GONE);
                    tvUbahJadwal.setVisibility(View.GONE);
                }
                tvLocation.setText(detailsViewModel.getSchedulesViewModels().get(0).getCityName());
                setFragmentData(detailsViewModel.getSchedulesViewModels().get(0));
            } else {
                tvUbahJadwal.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showPayButton(int ticketQuantity, int price, String type) {
        totalPrice.setText(String.format(getString(R.string.price_holder),
                CurrencyUtil.convertToCurrencyString(ticketQuantity * price)));
        ticketCount.setText(String.format(getString(R.string.x_type), ticketQuantity, type));
        buttonPayTickets.setVisibility(View.VISIBLE);
        buttonPayTickets.setBackgroundColor(getResources().getColor(R.color.white));
        if (buttonCountLayout.getVisibility() != View.VISIBLE) {
            buttonCountLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hidePayButton() {
        buttonCountLayout.setVisibility(View.INVISIBLE);
        buttonPayTickets.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        buttonPayTickets.setClickable(false);
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        buttonPayTickets.setClickable(true);
    }

    @Override
    public void renderSeatmap(String url) {
        ImageHandler.loadImageCover2(imgvSeatingLayout, url);
    }

    @Override
    public void hideSeatmap() {
        seatMap.setVisibility(View.GONE);
    }

    @Override
    public int getButtonLayoutHeight() {
        return buttonCountLayout.getHeight();
    }

    @Override
    public int getRequestCode() {
        return EVENT_LOGIN_REQUEST;
    }

    @Override
    public void setLocationDate(String location, String date, SchedulesViewModel datas) {
        tvLocation.setText(location);
        if (date != null && date.length() > 1) {
            tvDate.setText(date);
            tvDate.setVisibility(View.VISIBLE);
        } else {
            tvDate.setVisibility(View.GONE);
        }
        setFragmentData(datas);
        if (locationFragment != null)
            locationFragment.dismiss();
    }

    private void setFragmentData(SchedulesViewModel schedulesViewModel) {
        FragmentAddTickets fragmentAddTickets = (FragmentAddTickets) getSupportFragmentManager().
                findFragmentById(R.id.bookticket_fragment_holder);
        if (fragmentAddTickets == null) {
            fragmentAddTickets = FragmentAddTickets.newInstance(10);
            fragmentAddTickets.setData(schedulesViewModel.getPackages(), bookTicketPresenter);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.bookticket_fragment_holder, fragmentAddTickets);
            transaction.addToBackStack(BOOK_TICKET_FRAGMENT);
            transaction.commit();
        } else {
            fragmentAddTickets.setData(schedulesViewModel.getPackages(), bookTicketPresenter);
            fragmentAddTickets.resetAdapter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressed();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
        if (locationFragment != null && locationFragment.isVisible()) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public String getScreenName() {
        return bookTicketPresenter.getSCREEN_NAME();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressBar();
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay_tickets) {
            bookTicketPresenter.payTicketsClick(title, tvDate.getText().toString(), tvLocation.getText().toString());
        } else if (v.getId() == R.id.tv_ubah_jadwal) {
            if (eventsDetailsViewModel.getCustomText1() >= Utils.getSingletonInstance().SHOW_DATE_PICKER && bookTicketPresenter.getLocationDateModels() != null && bookTicketPresenter.getLocationDateModels().size() > 0) {
                openCalender(bookTicketPresenter.getLocationDateModels());
            } else {
                if (locationFragment == null)
                    locationFragment = new LocationDateBottomSheetFragment();
                locationFragment.setData(bookTicketPresenter.getLocationDateModels());
                locationFragment.setPresenter(bookTicketPresenter);
                locationFragment.show(getSupportFragmentManager(), "bottomsheetfragment");
            }
        }
    }

    public void openCalender(List<LocationDateModel> models) {
        SelectEventDateBottomSheet selectEventDateBottomSheet = SelectEventDateBottomSheet.Companion.getInstance(models);
        selectEventDateBottomSheet.setSelectedDatesListener(this);
        selectEventDateBottomSheet.show(getSupportFragmentManager(), "");
    }

    @Override
    public void selectedScheduleDate(@NotNull Date date) {
        for(SchedulesViewModel model : eventsDetailsViewModel.getSchedulesViewModels()) {
            if(model.getStartDate() == (date.getTime()/1000)){
                setLocationDate(model.getCityName(), Utils.getSingletonInstance().convertEpochToString(model.getStartDate()), model);
            }
        }

    }
}
