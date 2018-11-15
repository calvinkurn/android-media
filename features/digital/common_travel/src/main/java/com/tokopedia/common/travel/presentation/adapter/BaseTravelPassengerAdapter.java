package com.tokopedia.common.travel.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public abstract class BaseTravelPassengerAdapter extends RecyclerView.Adapter {

    protected List<TravelPassenger> travelPassengerList;
    protected Context context;

    public BaseTravelPassengerAdapter() {
        this.travelPassengerList = new ArrayList<>();
    }

    protected abstract RecyclerView.ViewHolder getViewHolder(View view);
    protected abstract int getLayoutAdapter();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(getLayoutAdapter(), null);
        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TravelItemViewHolder itemViewHolder = (TravelItemViewHolder) holder;
        TravelPassenger travelPassenger = travelPassengerList.get(position);
        itemViewHolder.passengerStatus.setVisibility(travelPassenger.isBuyer() == 1 ? View.VISIBLE : View.GONE);
        String salutationString = getSalutationString(travelPassenger.getTitle());
        itemViewHolder.passengerName.setText(salutationString + ". " + travelPassenger.getName());
        itemViewHolder.passengerName.setTextColor(getColorPassenger(travelPassenger.isSelected()));
    }

    protected int getColorPassenger(boolean isSelected) {
        return isSelected ? context.getResources().getColor(R.color.black_24) : context.getResources().getColor(R.color.font_black_primary_70);
    }

    @Override
    public int getItemCount() {
        return travelPassengerList.size();
    }

    public void addAll(List<TravelPassenger> travelPassengers) {
        this.travelPassengerList.clear();
        this.travelPassengerList = travelPassengers;
        notifyDataSetChanged();
    }

    protected String getSalutationString(int title) {
        if (title == TravelPassengerTitle.TUAN) {
            return "Tn";
        } else if (title == TravelPassengerTitle.NYONYA) {
            return "Ny";
        } else {
            return "Nn";
        }
    }
}
