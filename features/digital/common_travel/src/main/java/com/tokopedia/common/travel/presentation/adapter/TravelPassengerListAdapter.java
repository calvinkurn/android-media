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
public class TravelPassengerListAdapter extends BaseTravelPassengerAdapter {

    private ActionListener listener;

    public TravelPassengerListAdapter(TravelPassenger passengerSelected) {
        super(passengerSelected);
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
        return R.layout.item_travel_passenger_list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TravelPassenger travelPassenger = travelPassengerList.get(position);

        itemViewHolder.passengerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!travelPassenger.isSelected()) {
                    if (travelPassenger.getPaxType() == passengerSelected.getPaxType()) {
                        showCheckbox = true;
                    }
                    listener.onClickChoosePassenger(travelPassenger);
                }
            }
        });

        itemViewHolder.passengerName.setTextColor(getColorPassenger(travelPassenger.isSelected()));
        itemViewHolder.checkboxImg.setVisibility(showCheckbox ? View.VISIBLE : View.GONE);
        itemViewHolder.passengerStatus.setVisibility(travelPassenger.isBuyer() == 1 || (travelPassenger.isSelected() && !showCheckbox) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getColorPassenger(boolean isSelected) {
        return isSelected && !showCheckbox ?
                context.getResources().getColor(R.color.black_24) :
                context.getResources().getColor(R.color.font_black_primary_70);
    }

    class ItemViewHolder extends TravelItemViewHolder {

        private AppCompatImageView checkboxImg;

        public ItemViewHolder(View itemView) {
            super(itemView);

            checkboxImg = itemView.findViewById(R.id.img_checked);
        }
    }

    public interface ActionListener {
        void onClickChoosePassenger(TravelPassenger travelPassenger);
    }
}
