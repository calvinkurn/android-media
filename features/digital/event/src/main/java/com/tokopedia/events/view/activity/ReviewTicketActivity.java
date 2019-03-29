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
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class ReviewTicketActivity extends EventBaseActivity implements
        EventReviewTicketsContractor.EventReviewTicketsView {

    @BindView(R2.id.event_image_small)
    ImageView eventImageSmall;
    @BindView(R2.id.event_name_tv)
    TextView eventNameTv;
    @BindView(R2.id.event_time_tv)
    View eventTimeTv;
    @BindView(R2.id.event_address_tv)
    View eventAddressTv;
    @BindView(R2.id.event_total_tickets)
    TextView eventTotalTickets;
    @BindView(R2.id.tv_ticket_summary)
    TextView tvTicketSummary;
    @BindView(R2.id.tv_visitor_names)
    EditText tvEmailID;
    @BindView(R2.id.tv_telephone)
    EditText tvTelephone;
    @BindView(R2.id.tv_base_fare)
    TextView tvBaseFare;
    @BindView(R2.id.tv_conv_fees)
    TextView tvConvFees;
    @BindView(R2.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R2.id.button_textview)
    TextView buttonTextview;
    @BindView(R2.id.base_fare_break)
    TextView baseFareBreak;
    @BindView(R2.id.btn_go_to_payment)
    View btnGoToPayment;
    @BindView(R2.id.ed_promo_layout)
    View edPromoLayout;
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.update_email)
    View updateEmail;
    @BindView(R2.id.update_number)
    View updateNumber;
    @BindView(R2.id.scroll_view)
    ScrollView scrollView;
    @BindView(R2.id.form_layout)
    View formLayout;
    @BindView(R2.id.ed_form_1)
    EditText edForm1;
    @BindView(R2.id.ed_form_2)
    EditText edForm2;
    @BindView(R2.id.ed_form_3)
    EditText edForm3;
    @BindView(R2.id.ed_form_4)
    EditText edForm4;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;
    @BindView(R2.id.tv_promo_success_msg)
    TextView tvPromoSuccessMsg;
    @BindView(R2.id.tv_promo_cashback_msg)
    TextView tvPromoCashbackMsg;
    @BindView(R2.id.batal)
    TextView batal;
    @BindView(R2.id.tooltip_layout)
    View tooltipLayout;
    @BindView(R2.id.info_email)
    ImageView infoEmail;
    @BindView(R2.id.info_moreinfo)
    ImageView infoMoreinfo;
    @BindView(R2.id.tooltipinfo_title)
    TextView tooltipTitle;
    @BindView(R2.id.tooltipinfo_subtitle)
    TextView tooltipSubtitle;
    @BindView(R2.id.button_dismisstooltip)
    TextView dismissTooltip;
    @BindView(R2.id.tv_ticket_cnt_type)
    TextView tvTicketCntType;
    @BindView(R2.id.selected_seats_layout)
    View selectedSeatLayout;
    @BindView(R2.id.seat_numbers)
    TextView seatNumbers;
    @BindView(R2.id.goto_promo)
    View gotoPromo;
    @BindView(R2.id.rl_section_discount)
    View sectionDiscount;
    @BindView(R2.id.tv_discount)
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

        eventsAnalytics = new EventsAnalytics(getApplicationContext());
        tvEmailID.setEnabled(false);
        tvEmailID.setTextIsSelectable(false);
        tvEmailID.setFocusable(false);

        tvTelephone.setEnabled(false);
        tvTelephone.setTextIsSelectable(false);
        tvTelephone.setFocusable(false);

        eventReviewTicketPresenter.getProfile();
    }

    @Override
    public void renderFromPackageVM(PackageViewModel packageViewModel, SelectedSeatViewModel selectedSeats) {
        toolbar.setTitle(packageViewModel.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        String timerange = packageViewModel.getTimeRange();
        ImageHandler.loadImageCover2(eventImageSmall, packageViewModel.getThumbnailApp());
        timeHolder = new ImageTextViewHolder();
        addressHolder = new ImageTextViewHolder();
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

    @OnClick(R2.id.btn_go_to_payment)
    void clickPay() {
        eventReviewTicketPresenter.updateEmail(tvEmailID.getText().toString());
        eventReviewTicketPresenter.updateNumber(tvTelephone.getText().toString());
        eventReviewTicketPresenter.proceedToPayment();
    }

    @OnClick(R2.id.update_email)
    void updateEmail() {
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
    }

    @OnClick(R2.id.update_number)
    void updateNumber() {
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
    }

    @OnClick(R2.id.batal)
    void dismissPromoCode() {
        eventReviewTicketPresenter.updatePromoCode("");
        showDiscountSection(0, false);
        updateTotalPrice(baseFare, convFees, 0);
    }

    @OnClick({R2.id.info_email,
            R2.id.info_moreinfo,
            R2.id.button_dismisstooltip,
            R2.id.goto_promo})
    void onClickInfoIcon(View view) {
        if (view.getId() == R.id.info_email) {
            eventReviewTicketPresenter.clickEmailIcon();
        } else if (view.getId() == R.id.info_moreinfo) {
            eventReviewTicketPresenter.clickMoreinfoIcon();
        } else if (view.getId() == R.id.button_dismisstooltip) {
            eventReviewTicketPresenter.clickDismissTooltip();
        } else if (view.getId() == R.id.goto_promo) {
            eventReviewTicketPresenter.clickGoToPromo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST_CODE) {
            switch (resultCode) {
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_SUCCESS:
                    getActivity().setResult(PAYMENT_SUCCESS);
                    eventsAnalytics.eventDigitalEventTracking( EventsGAConst.EVENT_PURCHASE_ATTEMPT, EventsGAConst.PAYMENT_SUCCESS);
                    finish();
                    break;
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_FAILED:
                    showToastMessage(
                            getString(R.string.alert_payment_canceled_or_failed_digital_module)
                    );
                    eventsAnalytics.eventDigitalEventTracking( EventsGAConst.EVENT_PURCHASE_ATTEMPT, EventsGAConst.PAYMENT_FAILURE);
                    break;
                case com.tokopedia.payment.activity.TopPayActivity.PAYMENT_CANCELLED:
                    showToastMessage(getString(R.string.alert_payment_canceled_digital_module));
                    eventsAnalytics.eventDigitalEventTracking( EventsGAConst.EVENT_PURCHASE_ATTEMPT, EventsGAConst.PAYMENT_CANCELLED);
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

    @OnFocusChange({
            R2.id.ed_form_3,
            R2.id.ed_form_1,
            R2.id.ed_form_2,
            R2.id.ed_form_4})
    void validateEditText(EditText view) {
        eventReviewTicketPresenter.validateEditText(view);
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
}
