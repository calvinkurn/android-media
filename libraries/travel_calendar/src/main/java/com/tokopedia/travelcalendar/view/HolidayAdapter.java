package com.tokopedia.travelcalendar.view;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.util.List;

/**
 * Created by nabillasabbaha on 15/05/18.
 */
public class HolidayAdapter extends RecyclerView.Adapter {

    private List<HolidayResult> holidayResults;

    public HolidayAdapter(List<HolidayResult> holidayResults) {
        this.holidayResults = holidayResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holiday, parent, false);
        return new ItemViewHoliday(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HolidayResult holidayResult = holidayResults.get(position);
        ItemViewHoliday itemViewHoliday = (ItemViewHoliday) holder;

        String dateString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,
                "d MMM", holidayResult.getAttributes().getDate());

        itemViewHoliday.dateHoliday.setText(dateString);
        itemViewHoliday.eventHoliday.setText(holidayResult.getAttributes().getLabel());
    }

    public void addHoliday(List<HolidayResult> holidayResults) {
        this.holidayResults.clear();
        this.holidayResults = holidayResults;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return holidayResults.size();
    }

    static class ItemViewHoliday extends RecyclerView.ViewHolder {

        private AppCompatTextView dateHoliday;
        private AppCompatTextView eventHoliday;

        public ItemViewHoliday(View itemView) {
            super(itemView);
            dateHoliday = itemView.findViewById(R.id.date_holiday);
            eventHoliday = itemView.findViewById(R.id.event_holiday);
        }
    }

}
