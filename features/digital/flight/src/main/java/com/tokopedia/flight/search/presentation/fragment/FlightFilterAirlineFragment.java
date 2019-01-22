package com.tokopedia.flight.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.flight.search.presentation.adapter.FlightFilterAirlineAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.presentation.model.resultstatistics.AirlineStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


public class FlightFilterAirlineFragment extends BaseFlightFilterFragment<AirlineStat, FlightFilterAirlineAdapterTypeFactory> {
    public static final String TAG = FlightFilterAirlineFragment.class.getSimpleName();

    public static FlightFilterAirlineFragment newInstance() {
        Bundle args = new Bundle();
        FlightFilterAirlineFragment fragment = new FlightFilterAirlineFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onItemClicked(AirlineStat airlineStat) {
        // no op
    }

    @Override
    public void renderList(@NonNull List<AirlineStat> list) {
        super.renderList(list);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel != null) {
            List<String> airlineList = flightFilterModel.getAirlineList();
            if (airlineList != null) {
                for (int i = 0, sizei = airlineList.size(); i < sizei; i++) {
                    String selectedAirline = airlineList.get(i);
                    for (int j = 0, sizej = list.size(); j < sizej; j++) {
                        AirlineStat airlineStat = list.get(j);
                        if (airlineStat.getAirlineDB().getId().equals(selectedAirline)) {
                            checkedPositionList.add(j);
                            break;
                        }
                    }
                }
            }
        }
        adapter.setCheckedPositionList(checkedPositionList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChecked(AirlineStat airlineStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<AirlineStat> airlineStatList = adapter.getCheckedDataList();
        List<String> airlineList = Observable.from(airlineStatList).map(new Func1<AirlineStat, String>() {
            @Override
            public String call(AirlineStat airlineStat) {
                return airlineStat.getAirlineDB().getId();
            }
        }).toList().toBlocking().first();
        flightFilterModel.setAirlineList(airlineList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void resetFilter() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setAirlineList(new ArrayList<String>());
        adapter.resetCheckedItemSet();
        adapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void loadData(int page) {
        if (listener != null && listener.getFlightSearchStatisticModel() != null) {
            List<AirlineStat> airlineStatList = listener.getFlightSearchStatisticModel().getAirlineStatList();
            renderList(airlineStatList);
        }
    }

    @Override
    protected FlightFilterAirlineAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightFilterAirlineAdapterTypeFactory(this);
    }

    @Override
    public boolean isChecked(int position) {
        return adapter.isChecked(position);
    }

    @Override
    public void updateListByCheck(boolean isChecked, int position) {
        adapter.updateListByCheck(isChecked, position);
    }
}
