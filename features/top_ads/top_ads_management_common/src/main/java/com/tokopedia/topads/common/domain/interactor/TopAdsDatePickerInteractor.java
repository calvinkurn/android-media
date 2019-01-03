package com.tokopedia.topads.common.domain.interactor;

import java.util.Date;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public interface TopAdsDatePickerInteractor {

    void resetDate();

    void saveDate(Date startDate, Date endDate);

    Date getStartDate(Date defaultDate);

    Date getEndDate(Date defaultDate);

    void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex);

    int getLastSelectionDatePickerType();

    int getLastSelectionDatePickerIndex();
}