package com.db.williamchart.util;

import com.db.williamchart.renderer.AxisRenderer;
import com.db.williamchart.renderer.StringFormatRenderer;
import com.db.williamchart.view.ChartView;

/**
 * Created by normansyahputa on 7/7/17.
 * <p>
 * this class represent {@link ChartView} configuration.
 */

public interface BasicGraphConfiguration {

    int labelColor();

    int axisColor();

    float gridThickness();

    int gridColor();

    AxisRenderer.LabelPosition xLabelPosition();

    AxisRenderer.LabelPosition yLabelPosition();

    ChartView.GridType gridType();

    boolean xAxis();

    boolean yAxis();

    boolean xDataGrid();

    StringFormatRenderer yStringFormatRenderer();

    int topMargin();

    int rightMargin();

    int bottomMargin();

    int leftMargin();

    float xDistAxisToLabel();

    float yDistAxisToLabel();
}
