package com.tokopedia.common.travel.presentation.adapter;

import android.content.Context;
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
    private TravelPassenger passengerSelected;
    private ActionListener listener;
    private Context context;
    private boolean showCheckbox;

    public TravelPassengerListAdapter(TravelPassenger passengerSelected) {
        this.travelPassengerList = new ArrayList<>();
        this.passengerSelected = passengerSelected;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_travel_passenger_list, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TravelPassenger travelPassenger = travelPassengerList.get(position);
        showCheckbox = false;
        if (travelPassenger.getIdPassenger().equals(passengerSelected.getIdPassenger())) {
            showCheckbox = true;
            itemViewHolder.passengerName.setTextColor(context.getResources().getColor(R.color.black));
        }

        itemViewHolder.passengerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (travelPassenger.getPaxType() == passengerSelected.getPaxType()) {
                    showCheckbox = true;

                    listener.onClickChoosePassenger(travelPassenger);
                    listener.onUpdatePassenger(passengerSelected.getIdPassenger(), false);
                    listener.onUpdatePassenger(travelPassenger.getIdPassenger(), true);
                } else {
                    listener.onShowErrorCantPickPassenger();
                }
            }
        });
        itemViewHolder.passengerStatus.setVisibility(travelPassenger.isBuyer() == 1 ? View.VISIBLE : View.GONE);
        String salutationString = getSalutationString(travelPassenger.getTitle());
        itemViewHolder.passengerName.setText(salutationString + ". " + travelPassenger.getName());
        itemViewHolder.checkboxImg.setVisibility(showCheckbox ? View.VISIBLE : View.GONE);
        itemViewHolder.passengerName.setTextColor(travelPassenger.isSelected() && !showCheckbox ? context.getResources().getColor(R.color.red_30): context.getResources().getColor(R.color.black));
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
        void onClickChoosePassenger(TravelPassenger travelPassenger);

        void onUpdatePassenger(String idPassenger, boolean isSelected);

        void onShowErrorCantPickPassenger();
    }
}
