package com.db.williamchart.config;

import android.graphics.Color;

import com.db.williamchart.util.DataSetConfiguration;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class GrossGraphDataSetConfig implements DataSetConfiguration {

    @Override
    public int lineColor() {
        return Color.rgb(66, 181, 73);
    }

    @Override
    public int pointColor() {
        return Color.rgb(255, 255, 255);
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
