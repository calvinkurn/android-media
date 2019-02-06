package com.tokopedia.topads.common.data.source.local;

import java.util.Date;

/**
 * Created by Nathaniel on 11/28/2016.
 */

public interface TopAdsCacheDataSource {

    void resetDate();

    void saveDate(Date startDate, Date endDate);

    Date getStartDate(Date defaultDate);

    Date getEndDate(Date defaultDate);

    void updateLastInsertStatistic();

    boolean isStatisticDataExpired();

    void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex);

    int getLastSelectionDatePickerType();

    int getLastSelectionDatePickerIndex();
}