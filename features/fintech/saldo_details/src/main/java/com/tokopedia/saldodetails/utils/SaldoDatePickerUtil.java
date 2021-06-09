package com.tokopedia.saldodetails.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.DatePicker;

import java.util.Calendar;

public class SaldoDatePickerUtil {
    private int year;
    private int month;
    private int day;
    private int maxYear = 0;
    private int minYear = 0;
    private long maxDate = 0;
    private long minDate = 0;
    private boolean ShowToday = true;
    private Dialog dialog;
    private onDateSelectedListener listener;
    private Activity activity;
    private Calendar calendar;
    public static boolean IS_PICKING_DATE = false;

    public SaldoDatePickerUtil(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(com.tokopedia.design.R.layout.dialog_calendar);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public interface onDateSelectedListener {
        void onDateSelected(int year, int month, int dayOfMonth);
    }

    public void setDate(int day, int month, int year) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setShowToday(boolean ShowToday) {
        this.ShowToday = ShowToday;
    }

    public void setMaxYear(int maxyear) {
        calendar = Calendar.getInstance();
        maxYear = calendar.get(Calendar.YEAR) - maxyear;
    }

    public void setMinYear(int minyear) {
        calendar = Calendar.getInstance();
        minYear = calendar.get(Calendar.YEAR) - minyear;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public void DatePickerSpinner(onDateSelectedListener Listener) {
        IS_PICKING_DATE = true;
        listener = Listener;
        DatePickerDialog dpd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                listener.onDateSelected(year, monthOfYear + 1, dayOfMonth);
            }
        }, year, month - 1, day);
        dpd.setCanceledOnTouchOutside(false);
        dpd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (maxYear != 0) {
            Calendar maxDate = Calendar.getInstance();
            maxDate.set(maxDate.get(Calendar.YEAR) - maxYear, maxDate.getMaximum(Calendar.MONTH), maxDate.getMaximum(Calendar.DATE));
            dpd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }
        if (minYear != 0) {
            Calendar minDate = Calendar.getInstance();
            minDate.set(minDate.get(Calendar.YEAR) - minYear, minDate.getMinimum(Calendar.MONTH), minDate.getMinimum(Calendar.DATE));
            dpd.getDatePicker().setMinDate(minDate.getTimeInMillis() - 1000);
        }

        if (minDate != 0) {
            dpd.getDatePicker().setMinDate(minDate);
        }
        if (maxDate != 0) {
            dpd.getDatePicker().setMaxDate(maxDate);
        }
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                IS_PICKING_DATE = false;
            }
        });

        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                IS_PICKING_DATE = false;
            }
        });

        dpd.setTitle(null);
        dpd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dpd.show();
    }

    public void DatePickerCalendar(onDateSelectedListener Listener) {
        if (!IS_PICKING_DATE) {
            DatePickerSpinner(Listener);
        }
    }
}
