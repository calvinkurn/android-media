package com.tokopedia.topads.dashboard.view.fragment;


import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.williamchart.base.BaseWilliamChartConfig;
import com.db.williamchart.base.BaseWilliamChartModel;
import com.db.williamchart.config.GrossGraphDataSetConfig;
import com.db.williamchart.model.TooltipModel;
import com.db.williamchart.renderer.XRenderer;
import com.db.williamchart.tooltip.TooltipWithDynamicPointer;
import com.db.williamchart.util.TopAdsBaseWilliamChartConfig;
import com.db.williamchart.util.TopAdsTooltipConfiguration;
import com.db.williamchart.view.LineChartView;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.Summary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi.putra on 26/04/18.
 */

public abstract class TopAdsDashboardStatisticFragment extends TkpdBaseV4Fragment {
    protected TextView contentTitleGraph;
    LineChartView contentGraph;
    private TopAdsBaseWilliamChartConfig topAdsBaseWilliamChartConfig;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private Summary summary;
    private List<Cell> cells;
    private String[] mLabels;
    private ArrayList<TooltipModel> mLabelDisplay = new ArrayList<>();
    private float[] mValues;

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.partial_statistics_graph_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        contentTitleGraph = (TextView) view.findViewById(R.id.content_title_graph);
        contentGraph = (LineChartView) view.findViewById(R.id.content_graph);
        contentTitleGraph.setText(getTitleGraph());
    }

    private void generateLineChart() {
        try {
            contentGraph.dismissAllTooltips();
            final List<Integer> indexToDisplay = calculateIndexToDisplay();


            if (topAdsBaseWilliamChartConfig == null) {
                topAdsBaseWilliamChartConfig = new TopAdsBaseWilliamChartConfig();
            }

            if (baseWilliamChartConfig == null) {
                baseWilliamChartConfig = new BaseWilliamChartConfig();
            }

            BaseWilliamChartModel baseWilliamChartModel =
                    new BaseWilliamChartModel(mLabels, mValues);


            contentGraph.addDataDisplayDots(mLabelDisplay);
            TooltipWithDynamicPointer tooltip = new TooltipWithDynamicPointer(getActivity(),
                    R.layout.item_tooltip_topads, R.id.tooltip_value, R.id.tooltip_title, R.id.tooltip_pointer);
            baseWilliamChartConfig
                    .reset()
                    .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                    .setBasicGraphConfiguration(topAdsBaseWilliamChartConfig)
                    .setTooltip(tooltip, new TopAdsTooltipConfiguration())
                    .setxRendererListener(new XRenderer.XRendererListener() {
                        @Override
                        public boolean filterX(@IntRange(from = 0) int i) {
                            if (mValues != null) {
                                if (i == 0 || mValues.length - 1 == i)
                                    return true;

                                if (mValues.length <= 10) {
                                    return true;
                                }

                                return indexToDisplay.contains(i);
                            } else {
                                return true;
                            }
                        }
                    })
                    .setDotDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.oval_2_copy_6))
                    .buildChart(contentGraph);
        } catch (Exception e) {
        }
    }

    @NonNull
    private List<Integer> calculateIndexToDisplay() {
        //filter display dot at graph to avoid tight display graph
        final List<Integer> indexToDisplay = new ArrayList<>();
        int divider;
        if(mValues.length > 50){
            divider = 10;
        }else{
            divider = 5;
        }
        int divided = mValues.length / divider;
        for (int j = 1; j <= divided - 1; j++) {
            indexToDisplay.add((j * divider) - 1);
        }
        return indexToDisplay;
    }

    public void updateDataStatistic(DataStatistic dataStatistic) {
        if (dataStatistic == null){
            return;
        }
        this.summary = dataStatistic.getSummary();
        this.cells = dataStatistic.getCells();
        mLabels = generateLabels();
        mValues = generateValues();
        mLabelDisplay = generateLabelDisplay();
        generateLineChart();
    }

    protected String[] generateLabels() {
        if (cells != null && cells.size() > 0) {
            String[] labels = new String[cells.size()];
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                String label = getDate(cell);
                labels[i] = label;
            }
            return labels;
        } else {
            return null;
        }
    }

    private String getDate(Cell cell) {
        SimpleDateFormat formatterLabel = new SimpleDateFormat("dd MMM");
        return formatterLabel.format(cell.getDate());
    }

    protected float[] generateValues() {
        if (cells != null && cells.size() > 0) {
            float[] values = new float[cells.size()];
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                float value = getValueData(cell);
                values[i] = value;
            }
            return values;
        } else {
            return null;
        }
    }


    private ArrayList<TooltipModel> generateLabelDisplay() {
        if (cells != null && cells.size() > 0) {
            ArrayList<TooltipModel> valuesDisplay = new ArrayList<>();
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                String value = getValueDisplay(cell);
                String title = getDate(cell);
                valuesDisplay.add(new TooltipModel(title, value));
            }
            return valuesDisplay;
        } else {
            return null;
        }
    }

    protected abstract String getTitleGraph();

    protected abstract String getValueDisplay(Cell cell);

    protected abstract float getValueData(Cell cell);
}
