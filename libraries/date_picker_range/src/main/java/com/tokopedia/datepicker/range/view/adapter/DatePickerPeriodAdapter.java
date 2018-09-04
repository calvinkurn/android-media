package com.tokopedia.seller.common.datepicker.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.common.datepicker.view.widget.DatePeriodView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class DatePickerPeriodAdapter extends BaseLinearRecyclerViewAdapter {

    public interface Callback {

        void onItemClicked(PeriodRangeModel periodRangeModel);
    }

    private static final int VIEW_DATA = 100;

    private List<PeriodRangeModel> data;
    private int selectedPosition;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setData(List<PeriodRangeModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public DatePickerPeriodAdapter() {
        data = new ArrayList<>();
        selectedPosition = 0;
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_DATA:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_date_picker_periode, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_DATA:
                bindProduct((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return VIEW_DATA;
        }
    }

    private void bindProduct(final ViewHolder holder, int position) {
        PeriodRangeModel periodRangeModel = data.get(position);
        holder.datePeriodView.setChecked(position == selectedPosition);
        holder.datePeriodView.setTitle(periodRangeModel.getLabel());
        holder.datePeriodView.setDate(periodRangeModel.getStartDate(), periodRangeModel.getEndDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
        holder.datePeriodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
    }

    private void onSelectItem(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
        PeriodRangeModel periodRangeModel = getSelectedDate();
        if (callback != null && periodRangeModel != null) {
            callback.onItemClicked(periodRangeModel);
        }
    }

    public PeriodRangeModel getSelectedDate() {
        PeriodRangeModel periodRangeModel = null;
        if (selectedPosition >= 0 && selectedPosition < data.size()) {
            periodRangeModel = data.get(selectedPosition);
        }
        return periodRangeModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public DatePeriodView datePeriodView;

        public ViewHolder(View view) {
            super(view);
            datePeriodView = (DatePeriodView) view.findViewById(R.id.date_period_view);
        }
    }
}