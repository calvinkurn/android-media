package com.db.williamchart.util;

/**
 * Created by normansyahputa on 7/7/17.
 * <p>
 * This class represent {@link com.db.williamchart.model.ChartSet} configuration
 */
public interface DataSetConfiguration {
    int lineColor();

    int pointColor();

    float lineThickness();

    float pointsSize();

    boolean isVisible();
}
