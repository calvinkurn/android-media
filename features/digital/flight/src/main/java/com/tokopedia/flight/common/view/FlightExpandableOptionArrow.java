package com.tokopedia.flight.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.expandable.ExpandableOptionArrow;


/**
 * Created by zulfikarrahman on 12/15/17.
 */

public class FlightExpandableOptionArrow extends ExpandableOptionArrow {
    public FlightExpandableOptionArrow(Context context) {
        super(context);
    }

    public FlightExpandableOptionArrow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlightExpandableOptionArrow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlightExpandableOptionArrow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTitleText(String titleText){
        super.setTitleText(titleText);
    }
}
