package com.tokopedia.gm.statistic.view.widget.config;

import com.db.williamchart.config.GrossGraphDataSetConfig;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class DataTransactionDataSetConfig extends GrossGraphDataSetConfig {
    @Override
    public float pointsSize() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
