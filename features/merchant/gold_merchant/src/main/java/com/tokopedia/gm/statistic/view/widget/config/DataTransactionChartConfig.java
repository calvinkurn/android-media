package com.tokopedia.gm.statistic.view.widget.config;

import android.content.Context;
import android.util.TypedValue;

import com.db.williamchart.config.GrossGraphChartConfig;
import com.db.williamchart.renderer.AxisRenderer;
import com.db.williamchart.renderer.StringFormatRenderer;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class DataTransactionChartConfig extends GrossGraphChartConfig {
    private Context context;

    public DataTransactionChartConfig(Context context) {
        this.context = context;
    }

    @Override
    public AxisRenderer.LabelPosition xLabelPosition() {
        return AxisRenderer.LabelPosition.NONE;
    }

    @Override
    public AxisRenderer.LabelPosition yLabelPosition() {
        return AxisRenderer.LabelPosition.NONE;
    }

    @Override
    public boolean xAxis() {
        return false;
    }

    @Override
    public boolean yAxis() {
        return false;
    }

    @Override
    public boolean xDataGrid() {
        return false;
    }

    @Override
    public StringFormatRenderer yStringFormatRenderer() {
        return new YAxisRenderer();
    }

    @Override
    public int topMargin() {
        return 0;
    }

    @Override
    public int rightMargin() {
        return 0;
    }

    @Override
    public int bottomMargin() {
        int bottomMargin = 5;
        return (int) DptoPx(context, bottomMargin);
    }

    public float DptoPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
