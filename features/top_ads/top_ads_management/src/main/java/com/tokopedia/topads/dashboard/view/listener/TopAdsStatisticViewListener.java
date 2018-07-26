package com.tokopedia.topads.dashboard.view.listener;

import android.view.View;

import com.tokopedia.topads.dashboard.data.model.data.Cell;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public interface TopAdsStatisticViewListener {
    void updateDataCell(List<Cell> cells);

    View getDateLabelView();
}
