package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tokopedia.base.list.seller.view.fragment.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.datepicker.range.view.widget.DatePickerLabelView;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.dialog.DatePickerDialog;
import com.tokopedia.topads.dashboard.view.dialog.TimePickerdialog;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public abstract class TopAdsNewScheduleFragment<T extends StepperModel, V extends TopAdsDetailAdViewModel, P extends TopAdsDetailEditPresenter>
        extends BasePresenterFragment implements TopAdsDetailEditView {

    @Inject
    P daggerPresenter;
    protected T stepperModel;
    protected StepperListener stepperListener;

    protected TextView headerText;
    private RadioGroup showTimeRadioGroup;
    private RadioButton showTimeAutomaticRadioButton;
    private RadioButton showTimeConfiguredRadioButton;
    private View showTimeConfiguredView;
    private DatePickerLabelView showTimeStartDateDatePicker;
    private DatePickerLabelView showTimeStartTimeDatePicker;
    private DatePickerLabelView showTimeEndDateDatePicker;
    private DatePickerLabelView showTimeEndTimeDatePicker;
    protected Button submitButton;
    private ProgressDialog progressDialog;

    private Date startDate;
    private Date endDate;
    protected V detailAd;
    protected String adId;

    protected abstract V initiateDetailAd();

    protected void onNextClicked() {
        showLoading();
        populateDataFromFields();
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadAdDetail();
    }

    private void loadAdDetail() {
        if (!TextUtils.isEmpty(adId)) {
            showLoading();
            daggerPresenter.getDetailAd(adId);
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        headerText = (TextView) view.findViewById(R.id.header_text);
        showTimeRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_show_time);
        showTimeAutomaticRadioButton = (RadioButton) view.findViewById(R.id.radio_button_show_time_automatic);
        showTimeConfiguredRadioButton = (RadioButton) view.findViewById(R.id.radio_button_show_time_configured);
        showTimeConfiguredView = view.findViewById(R.id.layout_show_time_configured);
        showTimeStartDateDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_start_date);
        showTimeStartTimeDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_start_time);
        showTimeEndDateDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_end_date);
        showTimeEndTimeDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_end_time);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        showTimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_button_show_time_automatic) {
                    showTimeConfigurationTime(false);
                } else if (checkedId == R.id.radio_button_show_time_configured) {
                    showTimeConfigurationTime(true);
                }
            }
        });
        showTimeStartDateDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeStartDateDatePicker, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                });
                datePickerDialog.setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        showTimeStartTimeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                TimePickerdialog timePickerdialog = new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeStartTimeDatePicker, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                });
                timePickerdialog.show();
            }
        });
        showTimeEndDateDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeEndDateDatePicker, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        endDate = date;
                    }
                });
                datePickerDialog.setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        showTimeEndTimeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                TimePickerdialog timePickerdialog = new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeEndTimeDatePicker, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        endDate = date;
                    }
                });
                timePickerdialog.show();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClicked();
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    private void showTimeConfigurationTime(boolean show) {
        showTimeConfiguredView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (!show && !showTimeAutomaticRadioButton.isChecked()) {
            showTimeAutomaticRadioButton.setChecked(true);
        }
        if (show && !showTimeConfiguredRadioButton.isChecked()) {
            showTimeConfiguredRadioButton.setChecked(true);
        }
    }

    protected void showLoading() {
        progressDialog.show();
    }

    protected void hideLoading() {
        progressDialog.dismiss();
    }


    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd = initiateDetailAd();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_YEAR, 7);
        startDate = startCalendar.getTime();
        endDate = endCalendar.getTime();
        updateDisplayDateView();
    }

    private void updateDisplayDateView() {
        showTimeStartDateDatePicker.setContent(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeStartTimeDatePicker.setContent(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
        showTimeEndDateDatePicker.setContent(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeEndTimeDatePicker.setContent(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
    }

    private String getDateText(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date);
    }

    private Date getDate(String dateText, String dateFormat) throws ParseException {
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateText);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_schedule;
    }

    protected void populateDataFromFields() {
        if (showTimeAutomaticRadioButton.isChecked()) {
            detailAd.setScheduled(false);
        } else {
            detailAd.setScheduled(true);
        }
        detailAd.setStartDate(showTimeStartDateDatePicker.getValue());
        detailAd.setStartTime(showTimeStartTimeDatePicker.getValue());
        detailAd.setEndDate(showTimeEndDateDatePicker.getValue());
        detailAd.setEndTime(showTimeEndTimeDatePicker.getValue());
    }

    protected void loadAd(V detailAd) {
        if(detailAd != null) {
            this.detailAd = detailAd;

            convertDate(detailAd);
            if (!TextUtils.isEmpty(detailAd.getEndDate())) {
                showTimeConfigurationTime(true);
            } else {
                showTimeConfigurationTime(false);
            }
            updateDisplayDateView();
        }
    }

    private void convertDate(TopAdsDetailAdViewModel detailAd) {
        String parseFormat = TopAdsConstant.EDIT_PROMO_DISPLAY_DATE + ", " + TopAdsConstant.EDIT_PROMO_DISPLAY_TIME;
        try {
            String date = detailAd.getStartDate() + ", " + detailAd.getStartTime();
            startDate = getDate(date, parseFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            String date = detailAd.getEndDate() + ", " + detailAd.getEndTime();
            endDate = getDate(date, parseFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
        adId = arguments.getString(TopAdsExtraConstant.EXTRA_AD_ID);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if(context instanceof StepperListener){
            this.stepperListener = (StepperListener)context;
        }
    }


    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();
        loadAd((V) topAdsDetailAdViewModel);
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        hideLoading();
        showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadAdDetail();
            }
        });
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();
        setResultAdSaved(topAdsDetailAdViewModel);
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
        showSnackBarError(errorMessage);
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* just deal with abstraction */
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        //do nothing
    }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    protected void showEmptyState(NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryClickedListener);
    }

    private void setResultAdSaved(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        intent = setMoreResulAdSaved(intent, topAdsDetailAdViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    protected Intent setMoreResulAdSaved(Intent intent, TopAdsDetailAdViewModel topAdsDetailAdViewModel){
        return intent;
    }

    protected void showSnackBarError(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daggerPresenter.detachView();
    }
}
