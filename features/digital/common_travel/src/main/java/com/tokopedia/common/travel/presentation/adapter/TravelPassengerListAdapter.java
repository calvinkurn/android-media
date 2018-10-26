package com.tokopedia.common.travel.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 23/10/18.
 */
public class TravelPassengerListAdapter extends RecyclerView.Adapter {

    private List<TravelPassenger> travelPassengerList;
    private String namePassengerSelected;
    private ActionListener listener;
    private int paxTypeSelected;

    public TravelPassengerListAdapter(String namePassengerSelected, int paxTypeSelected) {
        this.travelPassengerList = new ArrayList<>();
        this.namePassengerSelected = namePassengerSelected;
        this.paxTypeSelected = paxTypeSelected;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_passenger_list, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TravelPassenger travelPassenger = travelPassengerList.get(position);
        if (travelPassenger.getName().equals(namePassengerSelected)) {
            travelPassenger.setSelected(true);
        }

        itemViewHolder.passengerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (travelPassenger.getPaxType() == paxTypeSelected) {
                    for (TravelPassenger travelPassengerItem : travelPassengerList) {
                        if (travelPassengerItem.isSelected()) {
                            travelPassengerItem.setSelected(false);
                        }
                    }
                    travelPassenger.setSelected(true);
                    notifyDataSetChanged();

                    listener.onClickSortLabel(travelPassenger);
                } else {
                    listener.onShowErrorCantPickPassenger();
                }
            }
        });
        itemViewHolder.passengerStatus.setVisibility(travelPassenger.isBuyer() == 1 ? View.VISIBLE : View.GONE);
        String salutationString = getSalutationString(travelPassenger.getTitle());
        itemViewHolder.passengerName.setText(salutationString + ". " + travelPassenger.getName());
        itemViewHolder.checkboxImg.setVisibility(travelPassenger.isSelected() ? View.VISIBLE : View.GONE);
    }

    private String getSalutationString(int title) {
        if (title == TravelPassengerTitle.TUAN) {
            return "Tn";
        } else if (title == TravelPassengerTitle.NYONYA) {
            return "Ny";
        } else {
            return "Nn";
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView passengerName;
        private TextView passengerStatus;
        private AppCompatImageView checkboxImg;
        private RelativeLayout passengerLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            passengerName = itemView.findViewById(R.id.passenger_name);
            checkboxImg = itemView.findViewById(R.id.img_checked);
            passengerStatus = itemView.findViewById(R.id.passenger_status);
            passengerLayout = itemView.findViewById(R.id.layout_passenger);
        }
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

    public interface ActionListener {
        void onClickSortLabel(TravelPassenger travelPassenger);

        void onShowErrorCantPickPassenger();
    }
}
