package com.tokopedia.events.view.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.events.R;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.domain.model.scanticket.ScanTicketResponse;
import com.tokopedia.events.view.contractor.ScanCodeContract;
import com.tokopedia.events.view.presenter.ScanCodeDataPresenter;

import javax.inject.Inject;

public class ScanQRCodeActivity extends TActivity implements HasComponent<EventComponent>, ScanCodeContract.ScanCodeDataView, View.OnClickListener {

    private View divider1, divider2;
    private TextView userName, gender, dob, identityNum, eventName, eventDate, eventLocation;
    private TextView redeemTicket, ticketRedeemed, closeTicket;
    private LinearLayout ownerData, ticketSuccessLayouyt, ticketFailureLayout;
    private TextView userNameTitle, userGenderTitle, userDobTitle, userIdentityTitle;
    private ImageView crossIcon1, crossIcon2, crossIcon3;
    private ConstraintLayout mainContent;
    private ProgressBar progressBar;
    private TextView ticketRedeemDate;


    private String scanUrl = "";

    @Inject
    ScanCodeDataPresenter scanCodeDataPresenter;

    private EventComponent eventComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ticket_result);
        executeInjector();

        divider1 = findViewById(R.id.divider1);
        divider2 = findViewById(R.id.divider2);
        userName = findViewById(R.id.user_name);
        gender = findViewById(R.id.user_gender);
        dob = findViewById(R.id.user_dob);
        identityNum = findViewById(R.id.user_identity_num);
        eventName = findViewById(R.id.event_name);
        eventDate = findViewById(R.id.event_date);
        eventLocation = findViewById(R.id.event_location);
        redeemTicket = findViewById(R.id.redeem_ticket);
        ticketRedeemed = findViewById(R.id.ticket_closed);
        closeTicket = findViewById(R.id.close_ticket);

        userNameTitle = findViewById(R.id.user_name_title);
        userGenderTitle = findViewById(R.id.user_gender_title);
        userDobTitle = findViewById(R.id.user_dob_title);
        userIdentityTitle = findViewById(R.id.user_identity_title);

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


        scanCodeDataPresenter.attachView(this);
        scanCodeDataPresenter.getScanData(scanUrl);


        closeTicket.setOnClickListener(this);
        crossIcon1.setOnClickListener(this);
        crossIcon2.setOnClickListener(this);
        crossIcon3.setOnClickListener(this);
        ticketRedeemed.setOnClickListener(this);


        divider1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        divider2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }


    private void executeInjector() {
        if (eventComponent == null) initInjector();
        eventComponent.inject(this);
    }

    private void initInjector() {
        eventComponent = DaggerEventComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .eventModule(new EventModule(this))
                .build();
    }

    @Override
    public EventComponent getComponent() {
        if (eventComponent == null) initInjector();
        return eventComponent;
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
                    }
                    ticketRedeemDate.setText("Diredeem pada" + " " + scanTicketResponse.getProduct().getUpdatedAt());
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
                }


                if (scanTicketResponse.getProduct() != null) {
                    eventName.setText(scanTicketResponse.getProduct().getDisplayName());
                    if (scanTicketResponse.getSchedule() != null) {
                        eventDate.setText(scanTicketResponse.getSchedule().getShowData());

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
        mainContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mainContent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
