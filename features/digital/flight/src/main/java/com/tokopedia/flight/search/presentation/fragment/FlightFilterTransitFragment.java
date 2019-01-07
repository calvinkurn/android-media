package com.tokopedia.flight.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.flight.search.presentation.adapter.FlightFilterTransitAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum;
import com.tokopedia.flight.search.presentation.model.resultstatistics.TransitStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FlightFilterTransitFragment extends BaseFlightFilterFragment<TransitStat, FlightFilterTransitAdapterTypeFactory> {
    public static final String TAG = FlightFilterTransitFragment.class.getSimpleName();

    public static FlightFilterTransitFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterTransitFragment fragment = new FlightFilterTransitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClicked(TransitStat transitStat) {
        // no op
    }

    @Override
    public void onItemChecked(TransitStat transitStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<TransitStat> transitStatList = adapter.getCheckedDataList();

        List<TransitEnum> transitEnumList = Observable.from(transitStatList)
                .map(new Func1<TransitStat, TransitEnum>() {
                    @Override
                    public TransitEnum call(TransitStat transitStat) {
                        return transitStat.getTransitType();
                    }
                }).toList().toBlocking().first();
        flightFilterModel.setTransitTypeList(transitEnumList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void renderList(@NonNull List<TransitStat> list) {
        super.renderList(list);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel != null) {
            List<TransitEnum> transitEnumList = flightFilterModel.getTransitTypeList();
            if (transitEnumList != null) {
                for (int i = 0, sizei = transitEnumList.size(); i < sizei; i++) {
                    TransitEnum transitEnum = transitEnumList.get(i);
                    for (int j = 0, sizej = list.size(); j < sizej; j++) {
                        TransitStat transitStat = list.get(j);
                        if (transitStat.getTransitType().getId() == transitEnum.getId()) {
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
    public void resetFilter() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setTransitTypeList(new ArrayList<TransitEnum>());
        adapter.resetCheckedItemSet();
        adapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void loadData(int page) {
        if (listener != null && listener.getFlightSearchStatisticModel() != null) {
            List<TransitStat> airlineStatList = listener.getFlightSearchStatisticModel().getTransitTypeStatList();
            renderList(airlineStatList);
        }
    }

    @Override
    protected FlightFilterTransitAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightFilterTransitAdapterTypeFactory(this);
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