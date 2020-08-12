package com.tokopedia.review.feature.reputationhistory.view.helper;

import androidx.annotation.NonNull;

import android.view.View;

import com.tokopedia.review.R;

import java.util.Calendar;

/**
 * Created by normansyahputa on 3/17/17.
 */

public class ReputationHeaderViewHelper extends GMStatHeaderViewHelper {

    public ReputationHeaderViewHelper(View itemView) {
        super(itemView, true);
        stopLoading();
    }

    public void setData(String startDate, String endDate) {
        calendarRange.setText(startDate + " - " + endDate);
        setImageIcon();
        stopLoading();
    }

    @Override
    protected void setImageIcon() {
        calendarIcon.setImageResource(R.mipmap.ic_icon_calendar_02);
    }

    @NonNull
    @Override
    protected Calendar getMaxDateCalendar() {
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);
        return maxCalendar;
    }
}
