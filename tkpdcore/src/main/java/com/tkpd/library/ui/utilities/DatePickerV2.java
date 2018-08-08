package com.tkpd.library.ui.utilities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Tkpd_Eka on 2/9/2015.
 */
public class DatePickerV2 {

    /**
     * @author EKA
     * note: Ini Class Date custom  yang dipakai untuk Tokopedia
     */
    public static class Date{
        public int Day;
        public int Month;
        public int Year;

        public String getDay(){
            return checkNumber(Day);
        }

        public String getMonth(){
            return checkNumber(Month);
        }

        public String getYear(){
            return Integer.toString(Year);
        }

        public String getDate(){
            return new StringBuilder().append(getDay()).append("/").append(getMonth()).append("/").append(getYear()).toString();
        }

        public String checkNumber(int number) {
            return number<=9?"0"+number:String.valueOf(number);
        }

        public void setDate(String date){
            String[] dates = date.split("/");
            Day = Integer.parseInt(dates[0]);
            Month = Integer.parseInt(dates[1]);
            Year = Integer.parseInt(dates[2]);
        }

        public Date(){}

        public Date(String date){
            String[] dates = date.split("/");
            Day = Integer.parseInt(dates[0]);
            Month = Integer.parseInt(dates[1]);
            Year = Integer.parseInt(dates[2]);
        }

        public Date(int day, int month, int year){
            Day = day;
            Month = month;
            Year = year;
        }

    }

    public interface OnDatePickerV2Listener{
        void onDatePicked(Date date);
        void onCancel();
    }

    public static DatePickerV2 createInstance(Context context){
        DatePickerV2 picker = new DatePickerV2();
        picker.context = context;
        return picker;
    };

    public static Date getDayAfterToday(int day){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        return convertFromCalToDate(c);
    }

    private boolean isPicking;
    private Context context;

    public static Date getToday(){
        Calendar c = Calendar.getInstance();
        return convertFromCalToDate(c);
    }

    public static Date convertFromCalToDate(Calendar c){
        Date date = new Date();
        date.Day = c.get(Calendar.DAY_OF_MONTH) + 1;
        date.Month = c.get(Calendar.MONTH) + 1;
        date.Year = c.get(Calendar.YEAR);
        return date;
    }

    public void getDatePicker(OnDatePickerV2Listener listener, Date startingDate){
        if(!isPicking){
            isPicking = true;
            createDatePicker(listener, startingDate, 946715844000L);
        }
    }

    public void getDatePicker(OnDatePickerV2Listener listener, Date startingDate, long minDate){
        if(!isPicking){
            isPicking = true;
            createDatePicker(listener, startingDate, minDate);
        }
    }

    private void createDatePicker(final OnDatePickerV2Listener listener, Date date, long minDate){
        DatePickerDialog dialog = new DatePickerDialog(context, onDateSelected(listener), date.Year, date.Month - 1, date.Day);
        dialog.getDatePicker().setMinDate(minDate);
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isPicking = false;
            }
        });
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener onDateSelected(final OnDatePickerV2Listener listener){
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                listener.onDatePicked(new Date(dayOfMonth, monthOfYear + 1, year));
            }
        };
    }


}
