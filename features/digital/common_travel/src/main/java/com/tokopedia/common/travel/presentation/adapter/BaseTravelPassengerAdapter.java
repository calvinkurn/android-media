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
    protected TravelPassenger passengerSelected;
    protected Context context;
    protected boolean showCheckbox;

    public BaseTravelPassengerAdapter(TravelPassenger passengerSelected) {
        this.travelPassengerList = new ArrayList<>();
        this.passengerSelected = passengerSelected;
    }

    protected abstract RecyclerView.ViewHolder getViewHolder(View view);

    protected abstract int getLayoutAdapter();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(getLayoutAdapter(), parent, false);
        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TravelItemViewHolder itemViewHolder = (TravelItemViewHolder) holder;
        TravelPassenger travelPassenger = travelPassengerList.get(position);
        String salutationString = getSalutationString(travelPassenger.getTitle());
        itemViewHolder.passengerName.setText(salutationString + ". " + travelPassenger.getName());
        itemViewHolder.passengerName.setTextColor(getColorPassenger(travelPassenger.isSelected()));

        showCheckbox = false;
        if (travelPassenger.getIdPassenger().equals(passengerSelected.getIdPassenger())) {
            showCheckbox = true;
            itemViewHolder.passengerName.setTextColor(context.getResources().getColor(R.color.black));
        }

        String statusPassenger = context.getString(R.string.travel_account_default);
        int colorStatus = R.color.font_black_disabled_38;
        if (travelPassenger.isSelected()) {
            statusPassenger = context.getString(R.string.travel_account_chosen);
            colorStatus = R.color.black_24;
        }
        itemViewHolder.passengerStatus.setText(statusPassenger);
        itemViewHolder.passengerStatus.setTextColor(context.getResources().getColor(colorStatus));
    }

    protected int getColorPassenger(boolean isSelected) {
        return isSelected ? context.getResources().getColor(R.color.black_24) :
                context.getResources().getColor(R.color.font_black_primary_70);
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
            return context.getString(R.string.travel_salutation_mister_string);
        } else if (title == TravelPassengerTitle.NYONYA) {
            return context.getString(R.string.travel_salutation_mrs_string);
        } else {
            return context.getString(R.string.travel_salutation_miss_string);
        }
    }
}
