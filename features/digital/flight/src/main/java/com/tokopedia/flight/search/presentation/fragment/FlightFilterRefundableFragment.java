package com.tokopedia.flight.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.flight.search.presentation.adapter.FlightFilterRefundableAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum;
import com.tokopedia.flight.search.presentation.model.resultstatistics.RefundableStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FlightFilterRefundableFragment extends BaseFlightFilterFragment<RefundableStat, FlightFilterRefundableAdapterTypeFactory> {
    public static final String TAG = FlightFilterRefundableFragment.class.getSimpleName();

    public static FlightFilterRefundableFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterRefundableFragment fragment = new FlightFilterRefundableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClicked(RefundableStat refundableStat) {
        // no op
    }

    @Override
    public void renderList(@NonNull List<RefundableStat> list) {
        super.renderList(list);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel != null) {
            List<RefundableEnum> refundableEnumList = flightFilterModel.getRefundableTypeList();
            if (refundableEnumList != null) {
                for (int i = 0, sizei = refundableEnumList.size(); i < sizei; i++) {
                    RefundableEnum refundableEnum = refundableEnumList.get(i);
                    for (int j = 0, sizej = list.size(); j < sizej; j++) {
                        RefundableStat refundableAdapterEnum = list.get(j);
                        if (refundableAdapterEnum.getRefundableEnum().getId() == refundableEnum.getId()) {
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
    public void onItemChecked(RefundableStat refundableStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<RefundableStat> refundableStatList = adapter.getCheckedDataList();

        List<RefundableEnum> refundableEnumList = Observable.from(refundableStatList)
                .map(new Func1<RefundableStat, RefundableEnum>() {
                    @Override
                    public RefundableEnum call(RefundableStat refundableStat) {
                        return refundableStat.getRefundableEnum();
                    }
                }).toList().toBlocking().first();
        flightFilterModel.setRefundableTypeList(refundableEnumList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void resetFilter() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setRefundableTypeList(new ArrayList<RefundableEnum>());
        adapter.resetCheckedItemSet();
        adapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void loadData(int page) {
        if (listener != null && listener.getFlightSearchStatisticModel() != null) {
            List<RefundableStat> airlineStatList = listener.getFlightSearchStatisticModel().getRefundableTypeStatList();
            renderList(airlineStatList);
        }
    }

    @Override
    protected FlightFilterRefundableAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightFilterRefundableAdapterTypeFactory(this);
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
