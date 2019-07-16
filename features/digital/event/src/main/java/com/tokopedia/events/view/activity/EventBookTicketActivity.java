package com.tokopedia.events.view.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.fragment.FragmentAddTickets;
import com.tokopedia.events.view.fragment.LocationDateBottomSheetFragment;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.FinishActivityReceiver;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

public class EventBookTicketActivity
        extends EventBaseActivity implements EventBookTicketContract.EventBookTicketView, View.OnClickListener {


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

    EventBookTicketContract.BookTicketPresenter bookTicketPresenter;
    private String title;
    EventsAnalytics eventsAnalytics;

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
        toolbar = findViewById(R.id.toolbar_book_ticket);
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
        buttonPayTickets.setOnClickListener(this);
        tvUbahJadwal.setOnClickListener(this);
    }

    @Override
    public void renderFromDetails(EventsDetailsViewModel detailsViewModel) {
        toolbar.setTitle(detailsViewModel.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        title = detailsViewModel.getTitle();
        if (detailsViewModel.getSchedulesViewModels() != null) {
            if (detailsViewModel.getSchedulesViewModels().size() > 1) {
                tvUbahJadwal.setVisibility(View.VISIBLE);
            } else {
                tvUbahJadwal.setVisibility(View.GONE);
            }
            tvLocation.setText(detailsViewModel.getSchedulesViewModels().get(0).getCityName());
            if (detailsViewModel.getTimeRange() != null && detailsViewModel.getTimeRange().length() > 1)
                tvDate.setText(Utils.getSingletonInstance().convertEpochToString(detailsViewModel.getSchedulesViewModels().get(0).getStartDate()));
            else
                tvDate.setVisibility(View.GONE);
            setFragmentData(detailsViewModel.getSchedulesViewModels().get(0));
        } else {
            tvUbahJadwal.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPayButton(int ticketQuantity, int price, String type) {
        totalPrice.setText(String.format(getString(R.string.price_holder),
                CurrencyUtil.convertToCurrencyString(ticketQuantity * price)));
        ticketCount.setText(String.format(getString(R.string.x_type), ticketQuantity, type));
        buttonPayTickets.setVisibility(View.VISIBLE);
        buttonPayTickets.setBackgroundColor(getResources().getColor(R.color.white));
        if (buttonCountLayout.getVisibility() != View.VISIBLE)
            buttonCountLayout.setVisibility(View.VISIBLE);
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
        if (locationFragment != null && locationFragment.isVisible())
            super.onBackPressed();
        else
            finish();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
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
            bookTicketPresenter.payTicketsClick(title);
        } else if (v.getId() == R.id.tv_ubah_jadwal) {
            if (locationFragment == null)
                locationFragment = new LocationDateBottomSheetFragment();

            locationFragment.setData(bookTicketPresenter.getLocationDateModels());
            locationFragment.setPresenter(bookTicketPresenter);
            locationFragment.show(getSupportFragmentManager(), "bottomsheetfragment");
        }
    }
}
