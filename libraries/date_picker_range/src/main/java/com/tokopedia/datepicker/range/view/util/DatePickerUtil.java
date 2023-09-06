package com.tokopedia.datepicker.range.view.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.DatePicker;

import com.tokopedia.datepicker.range.R;

import java.util.Calendar;

/**
 * Should use from library
 */
@Deprecated
public class DatePickerUtil {
    private int Year;
    private int Month;
    private int Day;
    private int MaxYear = 0;
    private int MinYear = 0;
    private long maxDate = 0;
    private long minDate = 0;
    private Dialog dialog;
    private onDateSelectedListener listener;
    private Activity context;
    Calendar c;
    public static boolean IS_PICKING_DATE = false;

    public DatePickerUtil(Activity Context, int day, int month, int year) {
        context = Context;
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(com.tokopedia.design.R.layout.dialog_calendar);
        Year = year;
        Month = month;
        Day = day;
    }

    public interface onDateSelectedListener {
        void onDateSelected(int year, int month, int dayOfMonth);
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    // TODO Temporary fix
    public void DatePickerSpinner(onDateSelectedListener Listener) {
        IS_PICKING_DATE = true;
        listener = Listener;
        DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                listener.onDateSelected(year, monthOfYear + 1, dayOfMonth);
            }
        }, Year, Month - 1, Day);
        dpd.setCanceledOnTouchOutside(false);
        dpd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (MaxYear != 0) {
            Calendar maxDate = Calendar.getInstance();
            maxDate.set(maxDate.get(Calendar.YEAR) - MaxYear, maxDate.getMaximum(Calendar.MONTH), maxDate.getMaximum(Calendar.DATE));
            dpd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }
        if (MinYear != 0) {
            Calendar minDate = Calendar.getInstance();
            minDate.set(minDate.get(Calendar.YEAR) - MinYear, minDate.getMinimum(Calendar.MONTH), minDate.getMinimum(Calendar.DATE));
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