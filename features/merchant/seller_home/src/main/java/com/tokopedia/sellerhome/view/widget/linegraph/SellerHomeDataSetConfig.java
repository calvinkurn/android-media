package com.tokopedia.sellerhome.view.widget.linegraph;

import android.graphics.Color;

import com.db.williamchart.util.DataSetConfiguration;

/**
 * Created By @ilhamsuaib on 2020-01-23
 */
public class SellerHomeDataSetConfig implements DataSetConfiguration {

    @Override
    public int lineColor() {
        return Color.parseColor("#03ac0e");
    }

    @Override
    public int pointColor() {
        return Color.parseColor("#4fd15a");
    }

    @Override
    public float lineThickness() {
        return 3;
    }

    @Override
    public float pointsSize() {
        return 4;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
