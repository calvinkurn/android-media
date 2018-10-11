package com.tokopedia.travelcalendar.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class GridCalendarAdapter extends RecyclerView.Adapter<GridCalendarAdapter.ViewHolder> {

    private static final String DAY_DATE_FORMAT = "EEEE";
    private static final String SUNDAY = "Sunday";

    private List<CellDate> monthlyDates;
    private Calendar currentDate;
    private ActionListener actionListener;
    private int dateCalMonth;
    private List<HolidayResult> holidayResultList;
    private Date maxDateCal;
    private Date minDateCal;

    public GridCalendarAdapter(List<CellDate> monthlyDates, Calendar currentDate,
                               Calendar maxDate, Calendar minDate, List<HolidayResult> holidayResultList) {
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.holidayResultList = holidayResultList;
        this.maxDateCal = maxDate.getTime();
        this.minDateCal = minDate.getTime();
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private int getDisplayMonthInt() {
        int displayMonthInt = currentDate.get(Calendar.MONTH) - 1;
        if (displayMonthInt < 0) {
            displayMonthInt = 11;
        }
        return displayMonthInt;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar_picker_day, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(monthlyDates.get(position));
    }

    @Override
    public int getItemCount() {
        return monthlyDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView cellNumber;
        FrameLayout container;

        public ViewHolder(View view) {
            super(view);
            cellNumber = view.findViewById(R.id.date);
            container = view.findViewById(R.id.container);
        }

        public void bind(CellDate cellDate) {
            Date mDate = cellDate.getDate();
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(mDate);
            int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
            dateCalMonth = dateCal.get(Calendar.MONTH);

            if (dateCalMonth == getDisplayMonthInt()) {
                cellNumber.setVisibility(View.VISIBLE);
                cellNumber.setText(String.valueOf(dayValue));
            } else {
                cellNumber.setVisibility(View.GONE);
            }

            if (cellDate.isSelected()) {
                cellNumber.setBackground(itemView.getResources().getDrawable(R.drawable.bg_calendar_picker_today_selected));
                cellNumber.setTextColor(itemView.getResources().getColor(R.color.white));
            } else {
                if (DateCalendarUtil.getZeroTimeDate(dateCal.getTime()).compareTo(DateCalendarUtil.getZeroTimeDate(minDateCal)) < 0 ||
                        DateCalendarUtil.getZeroTimeDate(dateCal.getTime()).compareTo(DateCalendarUtil.getZeroTimeDate(maxDateCal)) > 0) {
                    cellNumber.setTextColor(itemView.getResources().getColor(R.color.grey_300));
                }else {
                    cellNumber.setBackground(itemView.getResources().getDrawable(R.drawable.bg_calendar_picker_default));
                    cellNumber.setTextColor(itemView.getResources().getColor(R.color.font_black_primary_70));

                    //set holiday in sunday as red color
                    String dayAtDate = new SimpleDateFormat(DAY_DATE_FORMAT, Locale.ENGLISH).format(dateCal.getTime());
                    if (dayAtDate.equalsIgnoreCase(SUNDAY)) {
                        cellNumber.setTextColor(itemView.getResources().getColor(R.color.red_a700));
                    }
                    for (int i = 0; i < holidayResultList.size(); i++) {
                        if (dayValue == holidayResultList.get(i).getAttributes().getDateHoliday().getDate()) {
                            cellNumber.setTextColor(itemView.getResources().getColor(R.color.red_a700));
                        }
                    }
                }
            }

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date dateSelected = cellDate.getDate();
                    if (dateSelected.getMonth() == getDisplayMonthInt() &&
                            DateCalendarUtil.getZeroTimeDate(dateCal.getTime()).compareTo(DateCalendarUtil.getZeroTimeDate(minDateCal)) >= 0 &&
                            DateCalendarUtil.getZeroTimeDate(dateSelected).compareTo(DateCalendarUtil.getZeroTimeDate(maxDateCal)) <= 0)
                        actionListener.onClickDate(cellDate);
                }
            });
        }
    }

    public interface ActionListener {
        void onClickDate(CellDate cellDate);
    }
}
