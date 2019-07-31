package com.tokopedia.events.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.domain.model.scanticket.ScanTicketResponse;
import com.tokopedia.events.view.contractor.ScanCodeContract;
import com.tokopedia.events.view.presenter.ScanCodeDataPresenter;

public class ScanQRCodeActivity extends EventBaseActivity implements ScanCodeContract.ScanCodeDataView, View.OnClickListener {

    private View divider1, divider2;
    private TextView userName, gender, dob, identityNum, ticketQty, ticketQtyNum, eventName, name, eventDate, eventLocation;
    private TextView redeemTicket, ticketRedeemed, closeTicket;
    private LinearLayout ownerData, ticketSuccessLayouyt, ticketFailureLayout;
    private TextView userNameTitle, userGenderTitle, userDobTitle, userIdentityTitle;
    private ImageView crossIcon1, crossIcon2, crossIcon3;
    private FrameLayout mainContent;
    private ProgressBar progressBar;
    private TextView ticketRedeemDate;


    private String scanUrl = "";

    ScanCodeDataPresenter scanCodeDataPresenter;


    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getScanCodePresenter();
        scanCodeDataPresenter = (ScanCodeDataPresenter) mPresenter;

    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_scan_ticket_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void setupVariables() {
        divider1 = findViewById(R.id.divider1);
        divider2 = findViewById(R.id.divider2);
        userName = findViewById(R.id.user_name);
        gender = findViewById(R.id.user_gender);
        dob = findViewById(R.id.user_dob);
        identityNum = findViewById(R.id.user_identity_num);
        eventName = findViewById(R.id.event_name);
        name = findViewById(R.id.name);
        eventDate = findViewById(R.id.event_date);
        eventLocation = findViewById(R.id.event_location);
        redeemTicket = findViewById(R.id.redeem_ticket);
        ticketRedeemed = findViewById(R.id.ticket_closed);
        closeTicket = findViewById(R.id.close_ticket);

        userNameTitle = findViewById(R.id.user_name_title);
        userGenderTitle = findViewById(R.id.user_gender_title);
        userDobTitle = findViewById(R.id.user_dob_title);
        userIdentityTitle = findViewById(R.id.user_identity_title);

        ticketQty = findViewById(R.id.ticket_qty);
        ticketQtyNum = findViewById(R.id.ticket_qty_num);

        ownerData = findViewById(R.id.owner_data_ll);
        ticketSuccessLayouyt = findViewById(R.id.redeem_ticket_success);
        ticketFailureLayout = findViewById(R.id.ticket_alr_used);

        crossIcon1 = ownerData.findViewById(R.id.cross_icon);
        crossIcon2 = ticketSuccessLayouyt.findViewById(R.id.cross_icon);
        crossIcon3 = ticketFailureLayout.findViewById(R.id.cross_icon);

        mainContent = findViewById(R.id.main_content);
        progressBar = findViewById(R.id.prog_bar);
        ticketRedeemDate = ticketFailureLayout.findViewById(R.id.ticket_redeem_date);
        scanUrl = getIntent().getStringExtra("scanUrl");


        scanCodeDataPresenter.getScanData(scanUrl);


        closeTicket.setOnClickListener(this);
        crossIcon1.setOnClickListener(this);
        crossIcon2.setOnClickListener(this);
        crossIcon3.setOnClickListener(this);
        ticketRedeemed.setOnClickListener(this);


        divider1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        divider2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public void renderScannedData(ScanTicketResponse scanTicketResponse) {
        if (scanTicketResponse != null) {

            if (scanTicketResponse.getAction() != null && scanTicketResponse.getAction().get(0).getButtonType().equalsIgnoreCase("text")) {
                ownerData.setVisibility(View.GONE);
                ticketFailureLayout.setVisibility(View.VISIBLE);
                ticketSuccessLayouyt.setVisibility(View.GONE);
                redeemTicket.setVisibility(View.GONE);
                ticketRedeemed.setVisibility(View.VISIBLE);
                if (scanTicketResponse.getProduct() != null) {
                    eventName.setText(scanTicketResponse.getProduct().getDisplayName());
                    if (scanTicketResponse.getSchedule() != null) {
                        eventDate.setText(scanTicketResponse.getSchedule().getShowData());
                        if (!TextUtils.isEmpty(scanTicketResponse.getSchedule().getName())) {
                            name.setText(scanTicketResponse.getSchedule().getName());
                        }
                    }
                    ticketRedeemDate.setText(getString(R.string.ticket_updatedAt) + " " + scanTicketResponse.getProduct().getUpdatedAt());
                }
            } else if (scanTicketResponse.getAction() != null && scanTicketResponse.getAction().get(0).getButtonType().equalsIgnoreCase("button")) {
                if (scanTicketResponse.getUser() != null) {
                    if (!TextUtils.isEmpty(scanTicketResponse.getUser().getName())) {
                        userName.setText(scanTicketResponse.getUser().getName());
                        userNameTitle.setVisibility(View.VISIBLE);
                    }
                    if (!TextUtils.isEmpty(scanTicketResponse.getUser().getDob())) {
                        dob.setText(scanTicketResponse.getUser().getDob());
                        userDobTitle.setVisibility(View.VISIBLE);
                    }
                    if (!TextUtils.isEmpty(scanTicketResponse.getUser().getGender())) {
                        gender.setText(scanTicketResponse.getUser().getGender());
                        userGenderTitle.setVisibility(View.VISIBLE);
                    }
                    if (!TextUtils.isEmpty(scanTicketResponse.getUser().getIdentityNum())) {
                        identityNum.setText(scanTicketResponse.getUser().getIdentityNum());
                        userIdentityTitle.setVisibility(View.VISIBLE);
                    }
                    if (!TextUtils.isEmpty(scanTicketResponse.getQuantity())) {
                        ticketQty.setVisibility(View.VISIBLE);
                        ticketQtyNum.setText(scanTicketResponse.getQuantity() + " " + "Tiket");
                    }
                }


                if (scanTicketResponse.getProduct() != null) {
                    eventName.setText(scanTicketResponse.getProduct().getDisplayName());
                    if (scanTicketResponse.getSchedule() != null) {
                        eventDate.setText(scanTicketResponse.getSchedule().getShowData());
                        if (!TextUtils.isEmpty(scanTicketResponse.getSchedule().getName())) {
                            name.setText(scanTicketResponse.getSchedule().getName());
                        }
                    }
                }

                closeTicket.setVisibility(View.VISIBLE);
                redeemTicket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (scanTicketResponse.getAction() != null && scanTicketResponse.getAction().get(0).getUrlParams() != null) {
                            if (!TextUtils.isEmpty(scanTicketResponse.getAction().get(0).getUrlParams().getAppUrl()))
                                scanCodeDataPresenter.redeemTicket(scanTicketResponse.getAction().get(0).getUrlParams().getAppUrl());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void ticketRedeemed() {
        ticketSuccessLayouyt.setVisibility(View.VISIBLE);
        ticketFailureLayout.setVisibility(View.GONE);
        ownerData.setVisibility(View.GONE);
        ticketRedeemed.setVisibility(View.VISIBLE);
        redeemTicket.setVisibility(View.GONE);
        closeTicket.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        mainContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        mainContent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        scanCodeDataPresenter.onClickOptionMenu(item.getItemId());
        return super.onOptionsItemSelected(item);
    }
}
