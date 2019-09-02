package com.tokopedia.train.station.presentation.adapter.viewholder;

import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.station.presentation.adapter.viewholder.listener.TrainStationActionListener;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationCityViewModel;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainStationInCityViewHolder extends AbstractViewHolder<TrainStationCityViewModel> {
    public static final int LAYOUT = R.layout.view_train_station_city_item;
    private AppCompatTextView cityNameAppCompatTextView;
    private TrainStationActionListener trainStationActionListener;
    private TrainStationCityViewModel element;

    public TrainStationInCityViewHolder(View itemView, TrainStationActionListener trainStationActionListener) {
        super(itemView);
        this.cityNameAppCompatTextView = itemView.findViewById(R.id.tv_city_name);
        this.trainStationActionListener = trainStationActionListener;
        itemView.setOnClickListener(view -> TrainStationInCityViewHolder.this.trainStationActionListener.onCityClicked(element));
    }

    @Override
    public void bind(TrainStationCityViewModel element) {
        this.element = element;
        cityNameAppCompatTextView.setText(element.getCityName());
    }
}
