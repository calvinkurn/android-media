package com.tokopedia.discovery.dynamicfilter.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.discovery.model.DynamicFilterModel;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by noiz354 on 7/11/16.
 */
public class DynamicFilterListImpl extends DynamicFilterList {

    List<String> titleList;
    private List<DynamicFilterModel.Filter> dataList;

    public DynamicFilterListImpl(DynamicFilterListView view) {
        super(view);
    }

    @Override
    public String getMessageTAG() {
        return "DynamicFilterListImpl";
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return "DynamicFilterListImpl";
    }

    @Override
    public void initData(@NonNull Context context) {
        if (!isAfterRotate) {
            view.setupRecyclerView();
        }
    }

    @Override
    public void fetchArguments(Bundle argument) {
        if (argument != null && !isAfterRotate) {
            titleList = Parcels.unwrap(argument.getParcelable(TITLE_LIST));

            dataList = Parcels.unwrap(argument.getParcelable(DATA_LIST));
        }
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotate) {
            view.setupAdapter(dataList);
        }

    }
}
