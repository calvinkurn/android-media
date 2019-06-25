package com.tokopedia.events.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.view.contractor.SeatSelectionContract;
import com.tokopedia.events.view.customview.CustomSeatAreaLayout;
import com.tokopedia.events.view.customview.CustomSeatLayout;
import com.tokopedia.events.view.presenter.SeatSelectionPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.FinishActivityReceiver;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.LayoutDetailViewModel;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;
import com.tokopedia.events.view.viewmodel.SeatViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeatSelectionActivity extends EventBaseActivity implements
        SeatSelectionContract.SeatSelectionView, View.OnClickListener {


    TextView movieName;
    TextView selectedSeatText;
    TextView showTiming;
    LinearLayout seatTextLayout;
    ImageView previewWindow;
    View previewLayout;
    LinearLayout seatLayout;
    LinearLayout seatPlan;
    ScrollView scrollView;
    HorizontalScrollView horizontalScrollView;
    TextView ticketCount;
    TextView ticketPrice;
    View progressBarLayout;
    ProgressBar progBar;
    FrameLayout mainContent;
    LinearLayout verifySeatLayout;
    TextView verifySeatLayoutBtn;

    SeatSelectionPresenter seatSelectionPresenter;

    private SelectedSeatViewModel selectedSeatViewModel;

    private SeatLayoutViewModel seatLayoutViewModel;
    private FinishActivityReceiver finishReceiver = new FinishActivityReceiver(this);
    private EventsAnalytics eventsAnalytics;

    int price;
    int maxTickets;
    List<String> areacodes = new ArrayList<>();
    List<String> selectedSeats = new ArrayList<>();
    List<String> rowIds = new ArrayList<>();
    List<String> physicalRowIds = new ArrayList<>();
    List<String> seatIds = new ArrayList<>();
    List<String> seatNos = new ArrayList<>();
    List<String> actualseat = new ArrayList<>();
    private HashMap<String, Integer> seatNumberMap;
    String areaId;
    private int quantity;

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getSeatSelectionPresenter();
        seatSelectionPresenter = (SeatSelectionPresenter) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.seat_selection_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seatNumberMap = new HashMap<>();
        selectedSeatViewModel = new SelectedSeatViewModel();
        seatLayoutViewModel = new SeatLayoutViewModel();
        eventsAnalytics = new EventsAnalytics();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventModuleRouter.ACTION_CLOSE_ACTIVITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, intentFilter);
        seatSelectionPresenter.getSeatSelectionDetails();
    }

    @Override
    void setupVariables() {
        movieName = findViewById(R.id.tv_movie_name);
        selectedSeatText = findViewById(R.id.selected_seats);
        showTiming = findViewById(R.id.tv_show_timing);
        seatTextLayout = findViewById(R.id.vertical_layout);
        previewWindow = findViewById(R.id.small_preview);
        previewLayout = findViewById(R.id.preview_window);
        seatLayout = findViewById(R.id.seatLayout);
        seatPlan = findViewById(R.id.seat_plan);
        scrollView = findViewById(R.id.abc);
        horizontalScrollView = findViewById(R.id.horizontal_scroll);
        ticketCount = findViewById(R.id.ticket_count);
        ticketPrice = findViewById(R.id.ticket_price);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        progBar = findViewById(R.id.prog_bar);
        mainContent = findViewById(R.id.main_content);
        verifySeatLayout = findViewById(R.id.pay_tickets);
        verifySeatLayoutBtn = verifySeatLayout.findViewById(R.id.button_textview);
        verifySeatLayoutBtn.setOnClickListener(this);

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivity(intent);
    }

    @Override
    public void renderSeatSelection(int salesPrice, int maxTickets, SeatLayoutViewModel viewModel) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        price = salesPrice;
        this.maxTickets = maxTickets;
        this.seatLayoutViewModel = viewModel;
        addSeatingPlan(viewModel);
    }


    private void addSeatingPlan(SeatLayoutViewModel seatLayoutViewModel) {

        if (seatLayoutViewModel.getArea() != null) {
            areaId = seatLayoutViewModel.getArea().get(0).getAreaCode() + "-" + String.valueOf(seatLayoutViewModel.getArea().get(0).getAreaNo());
        }
        int numOfRows = seatLayoutViewModel.getLayoutDetail().size();
        String currentChar = "";
        for (int i = 0; i < numOfRows; ) {
            LayoutDetailViewModel layoutDetailViewModel = seatLayoutViewModel.getLayoutDetail().get(i);
            CustomSeatAreaLayout customSeatAreaLayout = new CustomSeatAreaLayout(this, seatSelectionPresenter);
            int rowId = layoutDetailViewModel.getRowId();
            if (Utils.isNotNullOrEmpty(layoutDetailViewModel.getPhysicalRowId())) {
                currentChar = layoutDetailViewModel.getPhysicalRowId();
                customSeatAreaLayout.setSeatRow(currentChar);
            }
            int numOfColumns = layoutDetailViewModel.getSeat().size();
            for (int j = 0; j < numOfColumns; j++) {
                SeatViewModel seatViewModel = layoutDetailViewModel.getSeat().get(j);
                if (seatViewModel.getNo() != 0 || seatViewModel.getActualSeat() != 0) {
                    String seatPhysicalRow = String.valueOf(seatViewModel.getActualSeat());
                    customSeatAreaLayout.addColumn(seatPhysicalRow,
                            seatViewModel.getStatus(),
                            maxTickets, rowId, currentChar);
                    String seatNumber = currentChar + seatPhysicalRow;
                    seatNumberMap.put(seatNumber, seatViewModel.getNo());
                } else {
                    customSeatAreaLayout.addColumn(".", 0, 0, 0, "");
                }
            }
            seatTextLayout.addView(customSeatAreaLayout);
            i++;
        }

        final Bitmap bp = Utils.getBitmap(this, seatTextLayout);
        Utils.saveImage(SeatSelectionActivity.this, bp);

    }

    @Override
    public void showPayButton(int ticketQuantity, int price) {

    }

    @Override
    public void hidePayButton() {

    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void setTicketPrice(int numOfTickets) {
        this.quantity = numOfTickets;
        ticketCount.setText(String.format(getString(R.string.x_type),
                numOfTickets, seatSelectionPresenter.getTicketCategory()));
        ticketPrice.setText(String.format(CurrencyUtil.RUPIAH_FORMAT,
                CurrencyUtil.convertToCurrencyString(numOfTickets * price)));
    }

    @Override
    public void setSelectedSeatText() {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            selectedSeatText.setText(String.format(getString(R.string.select_seat), quantity));
        } else {
            String text = TextUtils.join(", ", selectedSeats);
            selectedSeatText.setText(text);
        }
    }

    @Override
    public void initializeSeatLayoutModel(List<String> selectedSeatTextList, List<String> rowIds, List<String> actualSeats) {
        selectedSeats = selectedSeatTextList;
        this.rowIds = rowIds;
        actualseat = actualSeats;
        selectedSeatViewModel.setAreaCodes(areacodes);
        selectedSeatViewModel.setPrice(price);
        selectedSeatViewModel.setSeatRowIds(this.rowIds);
        selectedSeatViewModel.setQuantity(quantity);
        selectedSeatViewModel.setSeatIds(seatIds);
        selectedSeatViewModel.setAreaId(areaId);
        selectedSeatViewModel.setSeatNos(seatNos);
        selectedSeatViewModel.setPhysicalRowIds(physicalRowIds);
        selectedSeatViewModel.setActualSeatNos(actualseat);
    }

    @Override
    public void setEventTitle(String text) {
        movieName.setText(text);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomSeatLayout.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        seatSelectionPresenter.getProfile();
    }

    @Override
    public void setSelectedSeatModel() {
        seatIds.clear();
        physicalRowIds.clear();
        areacodes.clear();
        seatNos.clear();
        if (selectedSeats.size() > 0 && selectedSeats.size() == maxTickets) {
            for (int i = 0; i < selectedSeats.size(); i++) {
                int k = 0;
                String selectedSeat = selectedSeats.get(i);
                Character firstChar = selectedSeats.get(i).charAt(k);
                StringBuilder physicalRowID = new StringBuilder();
                while (Character.isLetter(firstChar)) {
                    physicalRowID.append(firstChar);
                    k++;
                    firstChar = selectedSeats.get(i).charAt(k);
                }
                physicalRowIds.add(physicalRowID.toString());
                seatIds.add(String.valueOf(seatNumberMap.get(selectedSeat)));
                seatNos.add(selectedSeats.get(i).substring(k, selectedSeats.get(i).length()));
                areacodes.add(seatLayoutViewModel.getArea().get(0).getAreaCode());
            }
            selectedSeatViewModel.setQuantity(selectedSeats.size());
            seatSelectionPresenter.verifySeatSelection(selectedSeatViewModel);
        } else {

            Toast.makeText(this, String.format(getString(R.string.select_max_ticket), maxTickets),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        seatSelectionPresenter.onActivityResult(requestCode);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        seatSelectionPresenter.onClickOptionMenu(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getScreenName() {
        return seatSelectionPresenter.getSCREEN_NAME();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_textview) {
            setSelectedSeatModel();
        }
    }
}
