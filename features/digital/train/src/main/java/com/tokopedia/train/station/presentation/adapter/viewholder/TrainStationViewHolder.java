package com.tokopedia.train.station.presentation.adapter.viewholder;

import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.station.presentation.adapter.viewholder.listener.TrainStationActionListener;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationViewHolder extends AbstractViewHolder<TrainStationViewModel> {
    public static final int LAYOUT = R.layout.view_train_station_item;
    private AppCompatTextView stationNameTextView;
    private AppCompatTextView stationCityTextView;
    private final TrainStationActionListener trainStationActionListener;
    private TrainStationViewModel element;

    public TrainStationViewHolder(View itemView, TrainStationActionListener trainStationActionListener) {
        super(itemView);
        stationNameTextView = itemView.findViewById(R.id.tv_station_name);
        stationCityTextView = itemView.findViewById(R.id.tv_station_city);
        this.trainStationActionListener = trainStationActionListener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainStationViewHolder.this.trainStationActionListener.onStationClicked(element);
            }
        });
    }

    @Override
    public void bind(TrainStationViewModel element) {
        this.element = element;
        stationNameTextView.setText(String.format("%s (%s)", getCapsSentences(element.getStationName()), element.getStationCode()));
        stationCityTextView.setText(element.getCityName());
    }

    private String getCapsSentences(String tagName) {
        String[] splits = tagName.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }
}
