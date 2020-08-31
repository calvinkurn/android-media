package com.tokopedia.sellerhome.view.widget.linegraph;

import android.graphics.Color;

import com.db.williamchart.Tools;
import com.db.williamchart.renderer.AxisRenderer;
import com.db.williamchart.renderer.StringFormatRenderer;
import com.db.williamchart.util.AnimationGraphConfiguration;
import com.db.williamchart.util.KMNumbers;
import com.db.williamchart.view.ChartView;

/**
 * Created By @ilhamsuaib on 2020-01-23
 */

public class SellerHomeLineGraphConfig implements AnimationGraphConfiguration {

    private int marginTop = 0, marginRight = 0, marginBottom = 0;

    public SellerHomeLineGraphConfig() {
    }

    @Override
    public int labelColor() {
        return Color.parseColor("#ae31353b");
    }

    @Override
    public int axisColor() {
        return Color.argb(13, 0, 0, 0);
    }

    @Override
    public float gridThickness() {
        return 1f;
    }

    @Override
    public int gridColor() {
        return Color.argb(13, 0, 0, 0);
    }

    @Override
    public AxisRenderer.LabelPosition xLabelPosition() {
        return AxisRenderer.LabelPosition.OUTSIDE;
    }

    @Override
    public AxisRenderer.LabelPosition yLabelPosition() {
        return AxisRenderer.LabelPosition.OUTSIDE;
    }

    @Override
    public ChartView.GridType gridType() {
        return ChartView.GridType.NONE;
    }

    @Override
    public boolean xAxis() {
        return true;
    }

    @Override
    public boolean yAxis() {
        return true;
    }

    @Override
    public boolean xDataGrid() {
        return true;
    }

    @Override
    public StringFormatRenderer yStringFormatRenderer() {
        return rawString -> KMNumbers.formatSuffixNumbers(Long.valueOf(rawString));
    }

    @Override
    public int topMargin() {
        return marginTop;
    }

    @Override
    public int rightMargin() {
        return marginRight;
    }

    @Override
    public int bottomMargin() {
        return marginBottom;
    }

    @Override
    public float xDistAxisToLabel() {
        return Tools.fromDpToPx(10f);
    }

    @Override
    public float yDistAxisToLabel() {
        return Tools.fromDpToPx(10f);
    }

    @Override
    public int alpha() {
        return 1;
    }

    @Override
    public int duration() {
        return 500;
    }

    @Override
    public int easingId() {
        return 0;
    }

    @Override
    public float overlapFactor() {
        return 1f;
    }

    @Override
    public float startX() {
        return 0f;
    }

    @Override
    public float startY() {
        return 1f;
    }

    @Override
    public Runnable endAnimation() {
        return () -> {
            // currently empty
        };
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }
}