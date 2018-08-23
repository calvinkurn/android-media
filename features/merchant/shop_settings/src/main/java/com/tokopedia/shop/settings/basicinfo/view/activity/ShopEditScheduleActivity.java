package com.tokopedia.shop.settings.basicinfo.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.constant.ShopScheduleActionDef;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopShedulePresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.common.util.ShopDateUtil;
import com.tokopedia.shop.settings.common.widget.ImageLabelView;
import com.tokopedia.shop.settings.common.widget.RadioButtonLabelView;
import com.tokopedia.shop.settings.common.widget.SwitchLabelView;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class ShopEditScheduleActivity extends BaseSimpleActivity
        implements UpdateShopShedulePresenter.View {

    public static final String EXTRA_SHOP_MODEL = "shop_model";

    public static final String SAVED_SELECTED_START_DATE = "svd_selected_start_date";
    public static final String SAVED_SELECTED_END_DATE = "svd_selected_end_date";

    @Inject
    UpdateShopShedulePresenter updateShopShedulePresenter;

    private ProgressDialog progressDialog;

    private TextView tvSave;
    private RadioButtonLabelView labelOpen;
    private RadioButtonLabelView labelClosed;
    private View vgScheduleSwitchContent;
    private EditText etShopCloseNote;
    private SwitchLabelView scheduleSwitch;
    private ImageLabelView labelStartClose;
    private ImageLabelView labelEndClose;

    private long selectedStartCloseUnixTimeMs;
    private long selectedEndCloseUnixTimeMs;

    private ShopBasicDataModel shopBasicDataModel;

    public static Intent createIntent(Context context, @Nullable ShopBasicDataModel shopBasicDataModel) {
        Intent intent = new Intent(context, ShopEditScheduleActivity.class);
        intent.putExtra(EXTRA_SHOP_MODEL, shopBasicDataModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        if (getIntent().hasExtra(EXTRA_SHOP_MODEL)) {
            shopBasicDataModel = getIntent().getParcelableExtra(EXTRA_SHOP_MODEL);
        }
        if (savedInstanceState != null) {
            selectedStartCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_START_DATE);
            selectedEndCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_END_DATE);
        } else {
            if (shopBasicDataModel != null) {
                String closeSchedule = shopBasicDataModel.getCloseSchedule();
                if (!TextUtils.isEmpty(closeSchedule)) {
                    selectedStartCloseUnixTimeMs = Long.parseLong(closeSchedule) * 1000L;
                }
                String openSchedule = shopBasicDataModel.getOpenSchedule();
                if (!TextUtils.isEmpty(openSchedule)) {
                    selectedEndCloseUnixTimeMs = Long.parseLong(openSchedule) * 1000L;
                }
            }
        }

        super.onCreate(savedInstanceState);

        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        updateShopShedulePresenter.attachView(this);

        tvSave = findViewById(R.id.tvSave);
        labelOpen = findViewById(R.id.labelOpen);
        labelClosed = findViewById(R.id.labelClose);
        scheduleSwitch = findViewById(R.id.scheduleSwitch);
        vgScheduleSwitchContent = findViewById(R.id.vgScheduleSwitchContent);
        labelStartClose = findViewById(R.id.labelStartClose);
        labelEndClose = findViewById(R.id.labelEndClose);
        etShopCloseNote = findViewById(R.id.etShopCloseNote);

        labelOpen.setOnRadioButtonLabelViewListener(new RadioButtonLabelView.OnRadioButtonLabelViewListener() {
            @Override
            public void onChecked(boolean isChecked) {
                if (isChecked) {
                    labelClosed.setChecked(false);
                    labelStartClose.setEnabled(true);
                    // if user has select the date, close it
                    if (scheduleSwitch.isChecked() &&
                            selectedStartCloseUnixTimeMs > 0 &&
                            selectedStartCloseUnixTimeMs <= System.currentTimeMillis()) {
                        selectedStartCloseUnixTimeMs = 0;
                        selectedEndCloseUnixTimeMs = 0;
                        labelStartClose.setContent(null);
                        labelEndClose.setContent(null);
                        scheduleSwitch.setChecked(false);
                    }
                }
            }
        });
        labelClosed.setOnRadioButtonLabelViewListener(new RadioButtonLabelView.OnRadioButtonLabelViewListener() {
            @Override
            public void onChecked(boolean isChecked) {
                if (isChecked) {
                    labelOpen.setChecked(false);
                    labelStartClose.setEnabled(false);
                    scheduleSwitch.setChecked(true);
                    setStartCloseDate(ShopDateUtil.getCurrentDate());
                }
            }
        });
        scheduleSwitch.setOnSwitchLabelViewListener(new SwitchLabelView.OnSwitchLabelViewListener() {
            @Override
            public void onChecked(boolean isChecked) {
                if (isChecked) {
                    showCloseScheduleContent();
                } else {
                    hideCloseScheduleContent();
                }
            }
        });

        if (shopBasicDataModel == null) {
            loadShopBasicData();
        } else {
            onSuccessGetShopBasicData(shopBasicDataModel);
        }

        labelStartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date minDate = ShopDateUtil.getTomorrowDate();
                Date selectedDate = ShopDateUtil.unixToDate(selectedStartCloseUnixTimeMs);
                showStartDatePickerDialog(selectedDate, minDate);
            }
        });
        labelEndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date minDate;
                if (selectedStartCloseUnixTimeMs == 0) {
                    minDate = ShopDateUtil.unixToDate(System.currentTimeMillis());
                } else {
                    minDate = ShopDateUtil.unixToDate(selectedStartCloseUnixTimeMs);
                }
                Date selectedDate;
                if (selectedEndCloseUnixTimeMs == 0) {
                    selectedDate = ShopDateUtil.unixToDate(System.currentTimeMillis());
                } else {
                    selectedDate = ShopDateUtil.unixToDate(selectedEndCloseUnixTimeMs);
                }
                showEndDatePickerDialog(selectedDate, minDate);
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
    }

    public void showStartDatePickerDialog(Date selectedDate, Date minDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date selectedDate = ShopDateUtil.toDate(year, month, dayOfMonth);
                setStartCloseDate(selectedDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker.show();
    }

    public void showEndDatePickerDialog(Date selectedDate, Date minDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date selectedDate = ShopDateUtil.toDate(year, month, dayOfMonth);
                setEndCloseDate(selectedDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker.show();
    }

    private void setStartCloseDate(Date date) {
        selectedStartCloseUnixTimeMs = date.getTime();
        labelStartClose.setContent(ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DAY_DATE, date));
        // move end date to start date, if the end < start
        if (selectedEndCloseUnixTimeMs > 0 &&
                selectedEndCloseUnixTimeMs < selectedStartCloseUnixTimeMs) {
            setEndCloseDate(new Date(selectedStartCloseUnixTimeMs));
        }
    }

    private void setEndCloseDate(Date date) {
        selectedEndCloseUnixTimeMs = date.getTime();
        labelEndClose.setContent(ShopDateUtil.toReadableString(ShopDateUtil.FORMAT_DAY_DATE, date));
        if (selectedStartCloseUnixTimeMs == 0) {
            if (labelOpen.isChecked()) {
                setStartCloseDate(ShopDateUtil.getTomorrowDate());
            } else {
                setStartCloseDate(ShopDateUtil.getCurrentDate());
            }
        }
    }

    private void hideCloseScheduleContent() {
        vgScheduleSwitchContent.setVisibility(View.GONE);
    }

    private void showCloseScheduleContent() {
        vgScheduleSwitchContent.setVisibility(View.VISIBLE);
    }


    private void onSaveButtonClicked() {
        showSubmitLoading(getString(R.string.title_loading));
        boolean isScheduleSwitch = scheduleSwitch.isChecked();
        @ShopScheduleActionDef int shopAction =
                labelClosed.isChecked() ?
                        ShopScheduleActionDef.CLOSED :
                        ShopScheduleActionDef.OPEN;
        long closeStart = isScheduleSwitch ? selectedStartCloseUnixTimeMs : 0;
        long closeEnd = isScheduleSwitch ? selectedEndCloseUnixTimeMs : 0;
        String closeNote = isScheduleSwitch ? etShopCloseNote.getText().toString() : "";
        updateShopShedulePresenter.updateShopSchedule(
                shopAction,
                labelClosed.isChecked(),
                closeStart == 0 ? null : String.valueOf(closeStart),
                closeEnd == 0 ? null : String.valueOf(closeEnd),
                closeNote);
    }

    public void showSubmitLoading(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void hideSubmitLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void loadShopBasicData() {
        scheduleSwitch.setVisibility(View.GONE);
        updateShopShedulePresenter.getShopBasicData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateShopShedulePresenter != null) {
            updateShopShedulePresenter.detachView();
        }
    }

    @Override
    public void onSuccessUpdateShopSchedule(String successMessage) {
        hideSubmitLoading();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onErrorUpdateShopSchedule(Throwable throwable) {
        hideSubmitLoading();
        showSnackbarErrorSubmitEdit(throwable);
    }

    @Override
    public void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        this.shopBasicDataModel = shopBasicDataModel;
        scheduleSwitch.setVisibility(View.VISIBLE);
        setUIShopSchedule(shopBasicDataModel);
        tvSave.setVisibility(View.VISIBLE);
    }

    private void setUIShopSchedule(ShopBasicDataModel shopBasicDataModel) {
        int shopStatus = shopBasicDataModel.getStatus();
        switch (shopStatus) {
            case ShopStatusDef.OPEN:
                labelOpen.setChecked(true);
                break;
            default: // closed
                labelClosed.setChecked(true);
                break;
        }
        String shopCloseSchedule = shopBasicDataModel.getCloseSchedule();
        String shopOpenSchedule = shopBasicDataModel.getOpenSchedule();
        if (!TextUtils.isEmpty(shopCloseSchedule) ||
                !TextUtils.isEmpty(shopOpenSchedule)) {
            scheduleSwitch.setChecked(true);
            if (!TextUtils.isEmpty(shopCloseSchedule)) {
                long shopCloseScheduleUnixMs = Long.parseLong(shopCloseSchedule) * 1000L;
                Date shopCloseDate = ShopDateUtil.unixToDate(shopCloseScheduleUnixMs);
                setStartCloseDate(shopCloseDate);
            }
            if (!TextUtils.isEmpty(shopOpenSchedule)) {
                long shopOpenScheduleUnixMs = Long.parseLong(shopOpenSchedule) * 1000L;
                Date shopOpenDate = ShopDateUtil.unixToDate(shopOpenScheduleUnixMs);
                setEndCloseDate(shopOpenDate);
            }
        } else {
            scheduleSwitch.setChecked(false);
        }
        etShopCloseNote.setText(shopBasicDataModel.getCloseNote());
    }

    @SuppressLint("Range")
    @Override
    public void onErrorGetShopBasicData(Throwable throwable) {
        showSnackbarErrorShopInfo(throwable);
    }

    private void showSnackbarErrorShopInfo(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(this, throwable);
        ToasterError.make(findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadShopBasicData();
                    }
                }).show();
    }

    private void showSnackbarErrorSubmitEdit(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(this, throwable);
        ToasterError.make(findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSaveButtonClicked();
                    }
                }).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVED_SELECTED_START_DATE, selectedStartCloseUnixTimeMs);
        outState.putLong(SAVED_SELECTED_END_DATE, selectedEndCloseUnixTimeMs);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_edit_schedule;
    }

}
