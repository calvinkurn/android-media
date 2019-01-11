package com.tokopedia.common.travel.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.ArrayList;

/**
 * Created by nabillasabbaha on 23/10/18.
 */
public class TravelPassengerEditAdapter extends BaseTravelPassengerAdapter {

    private ActionListener listener;

    public TravelPassengerEditAdapter(TravelPassenger selectedPassenger) {
        super(selectedPassenger);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    protected int getLayoutAdapter() {
        return R.layout.item_travel_passenger_edit;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TravelPassenger travelPassenger = travelPassengerList.get(position);

        itemViewHolder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!travelPassenger.isSelected() ||
                        travelPassenger.getIdPassenger().equals(passengerSelected.getIdPassenger()))
                    listener.onEditPassenger(travelPassenger);
            }
        });

        itemViewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!travelPassenger.isSelected())
                    listener.onDeletePassenger(travelPassenger.getIdPassenger(), travelPassenger.getId(),
                            travelPassenger.getTravelId());
            }
        });
        itemViewHolder.passengerStatus.setVisibility(travelPassenger.isBuyer() == 1 || travelPassenger.isSelected() ? View.VISIBLE : View.GONE);
        itemViewHolder.deleteImg.setVisibility(travelPassenger.isBuyer() == 1 || travelPassenger.isSelected() ? View.GONE : View.VISIBLE);
        itemViewHolder.editImg.setVisibility(travelPassenger.isSelected() && !showCheckbox ? View.GONE : View.VISIBLE);
        itemViewHolder.passengerName.setTextColor(getColorPassenger(travelPassenger.isSelected()));
    }

    @Override
    protected int getColorPassenger(boolean isSelected) {
        return isSelected && !showCheckbox ?
                context.getResources().getColor(R.color.black_24) :
                context.getResources().getColor(R.color.font_black_primary_70);
    }

    class ItemViewHolder extends TravelItemViewHolder {

        private AppCompatImageView deleteImg;
        private AppCompatImageView editImg;

        public ItemViewHolder(View itemView) {
            super(itemView);

            deleteImg = itemView.findViewById(R.id.img_delete);
            editImg = itemView.findViewById(R.id.img_edit);
        }
    }

    public interface ActionListener {
        void onEditPassenger(TravelPassenger travelPassenger);

        void onDeletePassenger(String idPassenger, String id, int travelId);
    }
}
