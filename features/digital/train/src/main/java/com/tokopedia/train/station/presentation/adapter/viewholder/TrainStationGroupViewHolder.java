package com.tokopedia.train.station.presentation.adapter.viewholder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.station.presentation.adapter.TrainStationAdapterTypeFactory;
import com.tokopedia.train.station.presentation.adapter.TrainStationTypeFactory;
import com.tokopedia.train.station.presentation.adapter.viewholder.listener.TrainStationActionListener;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationGroupViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationGroupViewHolder extends AbstractViewHolder<TrainStationGroupViewModel> {
    public static final int LAYOUT = R.layout.view_train_station_group_item;
    private RecyclerView stationsRecyclerView;
    private final TrainStationActionListener trainStationActionListener;

    public TrainStationGroupViewHolder(View itemView, TrainStationActionListener trainStationActionListener) {
        super(itemView);
        this.trainStationActionListener = trainStationActionListener;
        stationsRecyclerView = itemView.findViewById(R.id.rv_stations);
    }

    @Override
    public void bind(TrainStationGroupViewModel element) {
        TrainStationTypeFactory typeFactory = new TrainStationAdapterTypeFactory(trainStationActionListener);
        BaseListAdapter<TrainStationViewModel, TrainStationTypeFactory> adapter = new BaseListAdapter<>(typeFactory);
        LinearLayoutManager trainStationLayoutManager
                = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        stationsRecyclerView.setLayoutManager(trainStationLayoutManager);
        stationsRecyclerView.setHasFixedSize(true);
        stationsRecyclerView.setNestedScrollingEnabled(false);
        stationsRecyclerView.setAdapter(adapter);
        adapter.addElement(element.getStations());
        adapter.notifyDataSetChanged();
    }
}
