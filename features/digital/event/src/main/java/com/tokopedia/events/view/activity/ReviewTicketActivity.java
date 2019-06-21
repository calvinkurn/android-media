package com.tokopedia.events.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.presenter.EventReviewTicketPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;
import com.tokopedia.oms.scrooge.ScroogePGUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewTicketActivity extends EventBaseActivity implements
        EventReviewTicketsContractor.EventReviewTicketsView, View.OnClickListener, View.OnFocusChangeListener {

    ImageView eventImageSmall;
    TextView eventNameTv;
    View eventTimeTv;
    View eventAddressTv;
    TextView eventTotalTickets;
    TextView tvTicketSummary;
    EditText tvEmailID;
    EditText tvTelephone;
    TextView tvBaseFare;
    TextView tvConvFees;
    TextView tvTotalPrice;
    TextView buttonTextview;
    TextView baseFareBreak;
    View btnGoToPayment;
    View edPromoLayout;
    View progressBarLayout;
    ProgressBar progBar;
    View updateEmail;
    View updateNumber;
    ScrollView scrollView;
    @BindView(R2.id.form_layout)
    View formLayout;
    EditText edForm1;
    EditText edForm2;
    EditText edForm3;
    EditText edForm4;
    FrameLayout mainContent;
    TextView tvPromoSuccessMsg;
    TextView tvPromoCashbackMsg;
    TextView batal;
    View tooltipLayout;
    ImageView infoEmail;
    ImageView infoMoreinfo;
    TextView tooltipTitle;
    TextView tooltipSubtitle;
    TextView dismissTooltip;
    TextView tvTicketCntType;
    View selectedSeatLayout;
    TextView seatNumbers;
    View gotoPromo;
    View sectionDiscount;
    TextView tvDiscount;

    private int baseFare;
    private int convFees;

    EventReviewTicketsContractor.EventReviewTicketPresenter eventReviewTicketPresenter;

    public static final int PAYMENT_REQUEST_CODE = 65000;
    public static final int PAYMENT_SUCCESS = 5;
    private ImageTextViewHolder timeHolder;
    private ImageTextViewHolder addressHolder;
    private EventsAnalytics eventsAnalytics;

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getReviewTicketPresenter();
        eventReviewTicketPresenter = (EventReviewTicketPresenter) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.review_booking_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventsAnalytics = new EventsAnalytics();
        tvEmailID.setEnabled(false);
        tvEmailID.setTextIsSelectable(false);
        tvEmailID.setFocusable(false);

        tvTelephone.setEnabled(false);
        tvTelephone.setTextIsSelectable(false);
        tvTelephone.setFocusable(false);

        eventReviewTicketPresenter.getProfile();
    }

    @Override
    void setupVariables() {
        eventImageSmall = findViewById(R.id.event_image_small);
        eventNameTv = findViewById(R.id.event_name_tv);
        eventTimeTv = findViewById(R.id.event_time_tv);
        eventAddressTv = findViewById(R.id.event_address_tv);
        eventTotalTickets = findViewById(R.id.event_total_tickets);
        tvTicketSummary = findViewById(R.id.tv_ticket_summary);
        tvEmailID = findViewById(R.id.tv_visitor_names);
        tvTelephone = findViewById(R.id.tv_telephone);
        tvBaseFare = findViewById(R.id.tv_base_fare);
        tvConvFees = findViewById(R.id.tv_conv_fees);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        buttonTextview = findViewById(R.id.button_textview);
        baseFareBreak = findViewById(R.id.base_fare_break);
        btnGoToPayment = findViewById(R.id.btn_go_to_payment);
        edPromoLayout = findViewById(R.id.ed_promo_layout);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        progBar = findViewById(R.id.prog_bar);
        updateEmail = findViewById(R.id.update_email);
        updateNumber = findViewById(R.id.update_number);
        scrollView = findViewById(R.id.scroll_view);
        formLayout = findViewById(R.id.form_layout);
        edForm1 = findViewById(R.id.ed_form_1);
        edForm2 = findViewById(R.id.ed_form_2);
        edForm3 = findViewById(R.id.ed_form_3);
        edForm4 = findViewById(R.id.ed_form_4);
        mainContent = findViewById(R.id.main_content);
        tvPromoSuccessMsg = findViewById(R.id.tv_promo_success_msg);
        tvPromoCashbackMsg = findViewById(R.id.tv_promo_cashback_msg);
        batal = findViewById(R.id.batal);
        tooltipLayout = findViewById(R.id.tooltip_layout);
        infoEmail = findViewById(R.id.info_email);
        infoMoreinfo = findViewById(R.id.info_moreinfo);
        tooltipTitle = findViewById(R.id.tooltipinfo_title);
        tooltipSubtitle = findViewById(R.id.tooltipinfo_subtitle);
        dismissTooltip = findViewById(R.id.button_dismisstooltip);
        tvTicketCntType = findViewById(R.id.tv_ticket_cnt_type);
        selectedSeatLayout = findViewById(R.id.selected_seats_layout);
        seatNumbers = findViewById(R.id.seat_numbers);
        gotoPromo = findViewById(R.id.goto_promo);
        sectionDiscount = findViewById(R.id.rl_section_discount);
        tvDiscount = findViewById(R.id.tv_discount);

        btnGoToPayment.setOnClickListener(this);
        infoEmail.setOnClickListener(this);
        infoMoreinfo.setOnClickListener(this);
        updateEmail.setOnClickListener(this);
        updateNumber.setOnClickListener(this);
        batal.setOnClickListener(this);
        gotoPromo.setOnClickListener(this);
        dismissTooltip.setOnClickListener(this);

        edForm1.setOnFocusChangeListener(this);
        edForm2.setOnFocusChangeListener(this);
        edForm3.setOnFocusChangeListener(this);
        edForm4.setOnFocusChangeListener(this);
    }

    @Override
    public void renderFromPackageVM(PackageViewModel packageViewModel, SelectedSeatViewModel selectedSeats) {
        toolbar.setTitle(packageViewModel.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        String timerange = packageViewModel.getTimeRange();
        ImageHandler.loadImageCover2(eventImageSmall, packageViewModel.getThumbnailApp());
        timeHolder = new ImageTextViewHolder(this);
        addressHolder = new ImageTextViewHolder(this);
        ButterKnife.bind(timeHolder, eventTimeTv);
        ButterKnife.bind(addressHolder, eventAddressTv);
        eventNameTv.setText(packageViewModel.getDisplayName());
        if (timerange == null || timerange.length() == 0) {
            eventTimeTv.setVisibility(View.GONE);
        } else {
            setHolder(R.drawable.ic_time, timerange, timeHolder);
        }
        setHolder(R.drawable.ic_skyline, packageViewModel.getAddress(), addressHolder);
        eventTotalTickets.setText(String.format(getString(R.string.jumlah_tiket),
                packageViewModel.getSelectedQuantity()));

        baseFare = packageViewModel.getSelectedQuantity() * packageViewModel.getSalesPrice();
        convFees = packageViewModel.getConvenienceFee();
        updateTotalPrice(baseFare, convFees, 0);
        buttonTextview.setText(getString(R.string.pay_button));
        tvTicketCntType.setText(String.format(getString(R.string.x_type),
                packageViewModel.getSelectedQuantity(), packageViewModel.getDisplayName()));
        String baseBreak = String.format(getString(R.string.x_type),
                packageViewModel.getSelectedQuantity(), CurrencyUtil.convertToCurrencyString(packageViewModel.getSalesPrice()));
        baseFareBreak.setText("(" + baseBreak + ")");
        if (selectedSeats != null && selectedSeats.getSeatNos() != null && selectedSeats.getPhysicalRowIds() != null) {
            List<String> seatID = selectedSeats.getSeatNos();
            List<String> rowID = selectedSeats.getPhysicalRowIds();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < seatID.size(); i++) {
                builder.append(rowID.get(i)).append(seatID.get(i));
                if (i != seatID.size() - 1)
                    builder.append(", ");
                else
                    builder.append("");
            }
            seatNumbers.setText(builder.toString());
            selectedSeatLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEmailID(String emailID) {
        tvEmailID.setText(emailID);
    }

    @Override
    public void setPhoneNumber(String number) {
        tvTelephone.setText(number);
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
    public void initForms(String[] hintText, String[] regex) {
        formLayout.setVisibility(View.VISIBLE);
        try {
            edForm1.setHint(hintText[0]);
            edForm1.setVisibility(View.VISIBLE);
            edForm1.setTag(regex[0]);

            edForm2.setHint(hintText[1]);
            edForm2.setVisibility(View.VISIBLE);
            edForm2.setTag(regex[1]);

            edForm3.setHint(hintText[2]);
            edForm3.setVisibility(View.VISIBLE);
            edForm3.setTag(regex[2]);

            edForm4.setHint(hintText[3]);
            edForm4.setVisibility(View.VISIBLE);
            edForm4.setTag(regex[3]);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showPromoSuccessMessage(String text, int color) {
        tvPromoSuccessMsg.setText(text);
        tvPromoSuccessMsg.setTextColor(color);
        tvPromoSuccessMsg.setVisibility(View.VISIBLE);
        batal.setVisibility(View.VISIBLE);
        edPromoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSuccessMessage() {
        edPromoLayout.setVisibility(View.GONE);
        tvPromoSuccessMsg.setVisibility(View.GONE);
        tvPromoCashbackMsg.setVisibility(View.GONE);
        batal.setVisibility(View.GONE);
    }

    @Override
    public void showEmailTooltip() {
        tooltipTitle.setText(getResources().getString(R.string.tujuan_pengiriman_tiket));
        tooltipSubtitle.setText(getResources().getString(R.string.emailinfo_text));
        tooltipLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreinfoTooltip() {
        tooltipTitle.setText(getResources().getString(R.string.data_pelanggan_tambahan));
        tooltipSubtitle.setText(getResources().getString(R.string.additionallinfo_text));
        tooltipLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTooltip() {
        tooltipLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean validateAllFields() {
        boolean result = true;
        if (edForm1.getVisibility() == View.VISIBLE)
            result = result && eventReviewTicketPresenter.validateEditText(edForm1);
        if (edForm2.getVisibility() == View.VISIBLE)
            result = result && eventReviewTicketPresenter.validateEditText(edForm2);
        if (edForm3.getVisibility() == View.VISIBLE)
            result = result && eventReviewTicketPresenter.validateEditText(edForm3);
        if (edForm4.getVisibility() == View.VISIBLE)
            result = result && eventReviewTicketPresenter.validateEditText(edForm4);
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST_CODE) {
            switch (resultCode) {
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_SUCCESS:
                    getActivity().setResult(PAYMENT_SUCCESS);
                    eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PURCHASE_ATTEMPT, EventsGAConst.PAYMENT_SUCCESS);
                    finish();
                    break;
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_FAILED:
                    showToastMessage(
                            getString(R.string.alert_payment_canceled_or_failed_digital_module)
                    );
                    eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PURCHASE_ATTEMPT, EventsGAConst.PAYMENT_FAILURE);
                    break;
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_CANCELLED:
                    showToastMessage(getString(R.string.alert_payment_canceled_digital_module));
                    eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PURCHASE_ATTEMPT, EventsGAConst.PAYMENT_CANCELLED);
                    break;
                default:
                    break;
            }
        } else if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            hideProgressBar();
            long discount = 0;
            switch (resultCode) {
                case IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE:
                    eventReviewTicketPresenter.updatePromoCode(data.getExtras().getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE
                    ));
                    showPromoSuccessMessage(data.getExtras().getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE),
                            getResources().getColor(R.color.green_nob));
                    discount = data.getExtras().getLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT);
                    break;
                case IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE:
                    eventReviewTicketPresenter.updatePromoCode(data.getExtras().getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE
                    ));
                    showPromoSuccessMessage(data.getExtras().getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE
                    ), getResources().getColor(R.color.green_nob));
                    discount = data.getExtras().getLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);

                    break;
                default:
                    break;
            }
            if (discount > 0) {
                showDiscountSection(discount, true);
            } else
                showDiscountSection(discount, false);
            updateTotalPrice(baseFare, convFees, discount);
        } else if (requestCode == ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE) {
            if (data != null) {
                String url = data.getStringExtra(ScroogePGUtil.SUCCESS_MSG_URL) + "?from_payment=true";
                RouteManager.route(this, url);
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
                manager.sendBroadcast(new Intent(EventModuleRouter.ACTION_CLOSE_ACTIVITY));
                eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PURCHASE_ATTEMPT, EventsGAConst.PAYMENT_SUCCESS);
                this.finish();
            }
        }
    }

    public void showToastMessage(String message) {
        View view = null;
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setHolder(int resID, String label, ImageTextViewHolder holder) {
        holder.setImage(resID);
        holder.setTextView(label);
    }

    private void showDiscountSection(long discount, boolean show) {
        if (show) {
            tvDiscount.setText(String.format(getString(R.string.discount), discount));
            sectionDiscount.setVisibility(View.VISIBLE);
        } else {
            tvDiscount.setText("");
            sectionDiscount.setVisibility(View.GONE);
        }
    }

    private void updateTotalPrice(int baseFare, int convFee, long discount) {
        tvBaseFare.setText(String.format(getString(R.string.price_holder), CurrencyUtil.convertToCurrencyString(baseFare)));
        tvConvFees.setText(String.format(getString(R.string.price_holder), CurrencyUtil.convertToCurrencyString(convFee)));
        int total = (int) (baseFare + convFee - discount);
        tvTotalPrice.setText(String.format(getString(R.string.price_holder), CurrencyUtil.convertToCurrencyString(total)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return eventReviewTicketPresenter.getSCREEN_NAME();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go_to_payment) {
            eventReviewTicketPresenter.updateEmail(tvEmailID.getText().toString());
            eventReviewTicketPresenter.updateNumber(tvTelephone.getText().toString());
            eventReviewTicketPresenter.proceedToPayment();
        } else if (v.getId() == R.id.update_email) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!tvEmailID.isEnabled()) {
                tvTelephone.setEnabled(false);
                tvTelephone.setTextIsSelectable(false);
                tvTelephone.setFocusable(false);
                tvTelephone.setInputType(InputType.TYPE_NULL);
                tvEmailID.setEnabled(true);
                tvEmailID.setTextIsSelectable(true);
                tvEmailID.setFocusable(true);
                tvEmailID.setFocusableInTouchMode(true);
                tvEmailID.setSelection(tvEmailID.getText().length());
                tvEmailID.setInputType(InputType.TYPE_CLASS_TEXT);
                tvEmailID.requestFocus();
                imm.showSoftInput(tvEmailID, InputMethodManager.SHOW_IMPLICIT);
            } else {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(tvEmailID.getWindowToken(), 0);
                }
                tvEmailID.setEnabled(false);
                tvEmailID.setTextIsSelectable(false);
                tvEmailID.setFocusable(false);
                tvEmailID.setInputType(InputType.TYPE_NULL);
                tvEmailID.clearFocus();
                mainContent.requestFocus();
            }
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CHANGE_EMAIL, "");
            eventReviewTicketPresenter.updateEmail(tvEmailID.getText().toString());
        } else if (v.getId() == R.id.update_number) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!tvTelephone.isEnabled()) {
                tvEmailID.setEnabled(false);
                tvEmailID.setTextIsSelectable(false);
                tvEmailID.setFocusable(false);
                tvEmailID.setInputType(InputType.TYPE_NULL);
                tvTelephone.setEnabled(true);
                tvTelephone.setTextIsSelectable(true);
                tvTelephone.setSelection(tvTelephone.getText().length());
                tvTelephone.setFocusable(true);
                tvTelephone.setFocusableInTouchMode(true);
                tvTelephone.setInputType(InputType.TYPE_CLASS_TEXT);
                tvTelephone.requestFocus();
                imm.showSoftInput(tvTelephone, InputMethodManager.SHOW_IMPLICIT);
            } else {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(tvTelephone.getWindowToken(), 0);
                }
                tvTelephone.setEnabled(false);
                tvTelephone.setTextIsSelectable(false);
                tvTelephone.setFocusable(false);
                tvTelephone.setInputType(InputType.TYPE_NULL);
                tvTelephone.clearFocus();
                mainContent.requestFocus();
            }
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CHANGE_NUMBER, "");
            eventReviewTicketPresenter.updateNumber(tvTelephone.getText().toString());
        } else if (v.getId() == R.id.batal) {
            eventReviewTicketPresenter.updatePromoCode("");
            showDiscountSection(0, false);
            updateTotalPrice(baseFare, convFees, 0);
        } else if (v.getId() == R.id.info_email || v.getId() == R.id.info_moreinfo || v.getId() == R.id.button_dismisstooltip || v.getId() == R.id.goto_promo) {
            if (v.getId() == R.id.info_email) {
                eventReviewTicketPresenter.clickEmailIcon();
            } else if (v.getId() == R.id.info_moreinfo) {
                eventReviewTicketPresenter.clickMoreinfoIcon();
            } else if (v.getId() == R.id.button_dismisstooltip) {
                eventReviewTicketPresenter.clickDismissTooltip();
            } else if (v.getId() == R.id.goto_promo) {
                eventReviewTicketPresenter.clickGoToPromo();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        eventReviewTicketPresenter.validateEditText((EditText) v);
    }
}
