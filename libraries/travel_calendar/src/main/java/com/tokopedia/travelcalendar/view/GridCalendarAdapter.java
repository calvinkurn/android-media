package com.tokopedia.travelcalendar.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.view.model.CellDate;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nabillasabbaha on 10/05/18.
 */
public class GridCalendarAdapter extends ArrayAdapter {

    private static final String DAY_DATE_FORMAT = "EEEE";
    private static final String SUNDAY = "Sunday";

    private LayoutInflater mInflater;
    private List<CellDate> monthlyDates;
    private Calendar currentDate;
    private ActionListener actionListener;
    private int dateCalMonth;
    private List<HolidayResult> holidayResultList;
    private Date currentCalendar;

    public GridCalendarAdapter(Context context, List<CellDate> monthlyDates, Calendar currentDate,
                               List<HolidayResult> holidayResultList) {
        super(context, R.layout.view_calendar_picker_day);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.holidayResultList = holidayResultList;
        mInflater = LayoutInflater.from(context);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position).getDate();
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        dateCalMonth = dateCal.get(Calendar.MONTH);

        //current date
        currentCalendar = Calendar.getInstance().getTime();

        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.view_calendar_picker_day, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cellNumber = view.findViewById(R.id.date);
            viewHolder.container = view.findViewById(R.id.container);

            //hide date in current month
            if (dateCalMonth == getDisplayMonthInt()) {
                viewHolder.cellNumber.setVisibility(View.VISIBLE);
                viewHolder.cellNumber.setText(String.valueOf(dayValue));
            } else {
                viewHolder.cellNumber.setVisibility(View.GONE);
            }

            if (monthlyDates.get(position).isSelected()) {
                viewHolder.cellNumber.setBackground(getContext().getResources().getDrawable(R.drawable.bg_calendar_picker_today_selected));
                viewHolder.cellNumber.setTextColor(getContext().getResources().getColor(R.color.white));
            } else {
                if (DateCalendarUtil.getZeroTimeDate(dateCal.getTime()).compareTo(DateCalendarUtil.getZeroTimeDate(currentCalendar)) < 0) {
                    viewHolder.cellNumber.setTextColor(getContext().getResources().getColor(R.color.grey_300));
                } else {
                    viewHolder.cellNumber.setBackground(getContext().getResources().getDrawable(R.drawable.bg_calendar_picker_default));
                    viewHolder.cellNumber.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));

                    //set holiday in sunday as red color
                    String dayAtDate = new SimpleDateFormat(DAY_DATE_FORMAT, Locale.ENGLISH).format(dateCal.getTime());
                    if (dayAtDate.equalsIgnoreCase(SUNDAY)) {
                        viewHolder.cellNumber.setTextColor(getContext().getResources().getColor(R.color.red_a700));
                    }
                    for (int i = 0; i < holidayResultList.size(); i++) {
                        if (dayValue == holidayResultList.get(i).getAttributes().getDateHoliday().getDate()) {
                            viewHolder.cellNumber.setTextColor(getContext().getResources().getColor(R.color.red_a700));
                        }
                    }
                }
            }

            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date dateSelected = monthlyDates.get(position).getDate();
                    if (dateSelected.getMonth() == getDisplayMonthInt() &&
                            DateCalendarUtil.getZeroTimeDate(dateSelected).compareTo(DateCalendarUtil.getZeroTimeDate(currentCalendar)) > 0)
                        actionListener.onClickDate(monthlyDates.get(position));
                }
            });
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private int getDisplayMonthInt() {
        int displayMonthInt = currentDate.get(Calendar.MONTH) - 1;
        if (displayMonthInt < 0) {
            displayMonthInt = 11;
        }
        return displayMonthInt;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

    static class ViewHolder {
        AppCompatTextView cellNumber;
        FrameLayout container;
    }

    public interface ActionListener {
        void onClickDate(CellDate cellDate);
    }
}
