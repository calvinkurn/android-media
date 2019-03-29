package com.tokopedia.topads.common.domain.interactor;


import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSource;

import java.util.Date;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class TopAdsDatePickerInteractorImpl implements TopAdsDatePickerInteractor {

    private TopAdsCacheDataSource topAdsCacheDataSource;
    //private Context context;

    public TopAdsDatePickerInteractorImpl(TopAdsCacheDataSource topAdsCacheDataSource) {
        //this.context = context;
        this.topAdsCacheDataSource = topAdsCacheDataSource;
    }

    @Override
    public void resetDate() {
        topAdsCacheDataSource.resetDate();
    }

    @Override
    public void saveDate(Date startDate, Date endDate) {
        topAdsCacheDataSource.saveDate(startDate, endDate);
    }

    @Override
    public Date getStartDate(Date defaultDate) {
        return topAdsCacheDataSource.getStartDate(defaultDate);
    }

    @Override
    public Date getEndDate(Date defaultDate) {
        return topAdsCacheDataSource.getEndDate(defaultDate);
    }

    @Override
    public void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex) {
        topAdsCacheDataSource.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
    }

    @Override
    public int getLastSelectionDatePickerType() {
        return topAdsCacheDataSource.getLastSelectionDatePickerType();
    }

    @Override
    public int getLastSelectionDatePickerIndex() {
        return topAdsCacheDataSource.getLastSelectionDatePickerIndex();
    }
}
