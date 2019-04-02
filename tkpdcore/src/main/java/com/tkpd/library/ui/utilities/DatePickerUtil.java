package com.tkpd.library.ui.utilities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.Window;
import android.widget.DatePicker;

import com.tokopedia.core2.R;

import java.util.Calendar;

public class DatePickerUtil {
    private int Year;
    private int Month;
    private int Day;
    private int MaxYear = 0;
    private int MinYear = 0;
    private long maxDate = 0;
    private long minDate = 0;
    private boolean ShowToday = true;
    private Dialog dialog;
    private onDateSelectedListener listener;
    private Activity context;
    Calendar c;
    public static boolean IS_PICKING_DATE = false;

    public DatePickerUtil(Activity Context) {
        context = Context;
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calendar);
        c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH) + 1;
        Day = c.get(Calendar.DAY_OF_MONTH);
    }

    public DatePickerUtil(Activity Context, int day, int month, int year) {
        context = Context;
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calendar);
        Year = year;
        Month = month;
        Day = day;
    }

    public interface onDateSelectedListener {
        void onDateSelected(int year, int month, int dayOfMonth);
    }

    public void SetDate(int day, int month, int year) {
        Year = year;
        Month = month;
        Day = day;
    }

    public void SetShowToday(boolean ShowToday) {
        this.ShowToday = ShowToday;
    }

    public void SetMaxYear(int maxyear) {
        c = Calendar.getInstance();
        MaxYear = c.get(Calendar.YEAR) - maxyear;
    }

    public void SetMinYear(int minyear) {
        c = Calendar.getInstance();
        MinYear = c.get(Calendar.YEAR) - minyear;
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
        DatePickerDialog dpd = new DatePickerDialog(context, new OnDateSetListener() {

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
        dpd.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                IS_PICKING_DATE = false;
            }
        });

        dpd.setOnCancelListener(new OnCancelListener() {

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

    public void DatePickerCalendarShopClose(onDateSelectedListener Listener) {
        if (!IS_PICKING_DATE) {
            DatePickerSpinnerShopClose(Listener);
        }
    }

    public void DatePickerSpinnerShopClose(onDateSelectedListener Listener) {
        IS_PICKING_DATE = true;
        listener = Listener;
        DatePickerDialog dpd = new DatePickerDialog(context, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                listener.onDateSelected(year, monthOfYear, dayOfMonth);
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
            dpd.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }

        if (minDate != 0) {
            dpd.getDatePicker().setMinDate(minDate);
        }
        if (maxDate != 0) {
            dpd.getDatePicker().setMaxDate(maxDate);
        }
        dpd.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                IS_PICKING_DATE = false;
            }
        });

        dpd.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                IS_PICKING_DATE = false;
            }
        });

        dpd.setTitle(null);
        dpd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dpd.show();
    }
}
