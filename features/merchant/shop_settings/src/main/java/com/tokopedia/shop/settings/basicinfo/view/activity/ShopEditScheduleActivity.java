package com.tokopedia.shop.settings.basicinfo.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
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
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.constant.ShopScheduleActionDef;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopShedulePresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.common.util.ShopDateUtil;
import com.tokopedia.shop.settings.common.widget.ImageLabelView;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class ShopEditScheduleActivity extends BaseSimpleActivity
        implements UpdateShopShedulePresenter.View {

    public static final String EXTRA_SHOP_MODEL = "shop_model";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_IS_CLOSED_NOW = "is_closed_now";

    public static final String SAVED_SELECTED_START_DATE = "svd_selected_start_date";
    public static final String SAVED_SELECTED_END_DATE = "svd_selected_end_date";

    @Inject
    UpdateShopShedulePresenter updateShopShedulePresenter;

    private ProgressDialog progressDialog;

    private TextView tvSave;
    private TkpdHintTextInputLayout tilShopCloseNote;
    private EditText etShopCloseNote;
    private ImageLabelView labelStartClose;
    private ImageLabelView labelEndClose;

    private long selectedStartCloseUnixTimeMs;
    private long selectedEndCloseUnixTimeMs;

    private ShopBasicDataModel shopBasicDataModel;
    private boolean isClosedNow;

    public static Intent createIntent(Context context, @NonNull ShopBasicDataModel shopBasicDataModel,
                                      String title,
                                      boolean isClosedNow) {
        Intent intent = new Intent(context, ShopEditScheduleActivity.class);
        intent.putExtra(EXTRA_SHOP_MODEL, shopBasicDataModel);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_IS_CLOSED_NOW, isClosedNow);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);

        shopBasicDataModel = getIntent().getParcelableExtra(EXTRA_SHOP_MODEL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        isClosedNow = getIntent().getBooleanExtra(EXTRA_IS_CLOSED_NOW, false);
        setTitle(title);

        if (savedInstanceState != null) {
            selectedStartCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_START_DATE);
            selectedEndCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_END_DATE);
        } else {
            String closeSchedule = shopBasicDataModel.getCloseSchedule();
            if (!StringUtils.isEmptyNumber(closeSchedule)) {
                selectedStartCloseUnixTimeMs = Long.parseLong(closeSchedule) * 1000L;
            }
            String closedUntil = shopBasicDataModel.getCloseUntil();
            if (!StringUtils.isEmptyNumber(closedUntil)) {
                selectedEndCloseUnixTimeMs = Long.parseLong(closedUntil) * 1000L;
            }
        }

        super.onCreate(savedInstanceState);

        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        updateShopShedulePresenter.attachView(this);

        tvSave = findViewById(R.id.tvSave);
        labelStartClose = findViewById(R.id.labelStartClose);
        labelEndClose = findViewById(R.id.labelEndClose);
        tilShopCloseNote = findViewById(R.id.tilShopCloseNote);
        etShopCloseNote = findViewById(R.id.etShopCloseNote);
        etShopCloseNote.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tilShopCloseNote.setError(null);
            }
        });

        setUIShopSchedule(shopBasicDataModel);

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
                    if (isClosedNow) {
                        minDate = ShopDateUtil.getCurrentDate();
                    } else {
                        minDate = ShopDateUtil.getTomorrowDate();
                    }
                } else {
                    minDate = ShopDateUtil.unixToDate(selectedStartCloseUnixTimeMs);
                }
                Date selectedDate;
                if (selectedEndCloseUnixTimeMs == 0) {
                    if (isClosedNow) {
                        selectedDate = ShopDateUtil.getCurrentDate();
                    } else {
                        selectedDate = ShopDateUtil.getTomorrowDate();
                    }
                } else {
                    selectedDate = ShopDateUtil.unixToDate(selectedEndCloseUnixTimeMs);
                }
                showEndDatePickerDialog(selectedDate, minDate);
            }
        });
        tvSave.setVisibility(View.VISIBLE);
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
    }

    private void onSaveButtonClicked() {
        String closeNote = etShopCloseNote.getText().toString();
        if (TextUtils.isEmpty(closeNote)) {
            tilShopCloseNote.setError(getString(R.string.note_must_be_filled));
            return;
        }

        showSubmitLoading(getString(R.string.title_loading));
        @ShopScheduleActionDef int shopAction =
                (isClosedNow || shopBasicDataModel.isClosed()) ?
                        ShopScheduleActionDef.CLOSED :
                        ShopScheduleActionDef.OPEN;
        long closeStart = selectedStartCloseUnixTimeMs;
        long closeEnd = selectedEndCloseUnixTimeMs;
        updateShopShedulePresenter.updateShopSchedule(
                shopAction,
                isClosedNow,
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

    private void setUIShopSchedule(ShopBasicDataModel shopBasicDataModel) {
        String shopCloseSchedule = shopBasicDataModel.getCloseSchedule();
        String shopCloseUntil = shopBasicDataModel.getCloseUntil();

        boolean hasCloseUntil = !StringUtils.isEmptyNumber(shopCloseUntil);
        //set close schedule
        if (isClosedNow || shopBasicDataModel.isClosed()) {
            labelStartClose.setEnabled(false);
            setStartCloseDate(ShopDateUtil.getCurrentDate());
        } else {
            labelStartClose.setEnabled(true);
            boolean hasCloseSchedule = !StringUtils.isEmptyNumber(shopCloseSchedule);
            if (hasCloseSchedule) {
                long shopCloseScheduleUnixMs = Long.parseLong(shopCloseSchedule) * 1000L;
                Date shopCloseDate = ShopDateUtil.unixToDate(shopCloseScheduleUnixMs);
                setStartCloseDate(shopCloseDate);
            }
        }

        //set open schedule.
        if (hasCloseUntil) {
            long shopOpenScheduleUnixMs = Long.parseLong(shopCloseUntil) * 1000L;
            Date shopOpenDate = ShopDateUtil.unixToDate(shopOpenScheduleUnixMs);
            setEndCloseDate(shopOpenDate);
        }
        etShopCloseNote.setText(shopBasicDataModel.getCloseNote());
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
