package com.db.williamchart.base;

import java.util.Arrays;

/**
 * @author normansyahputa on 7/6/17.
 *         <p>
 *         this class tend to add more readability
 */

public class BaseWilliamChartModel {
    private String[] labels;
    private float[] values;
    private String[] customValues = new String[0];

    public BaseWilliamChartModel(String[] labels, float[] values) {
        if (labels == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");
        if (values == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");

        this.labels = labels;
        this.values = values;
    }

    public BaseWilliamChartModel(String[] labels, float[] values, String[] customValues) {
        if (labels == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");
        if (values == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");
        if (customValues == null)
            throw new RuntimeException("unable to process null WilliamChartUtils customValues");

        this.labels = labels;
        this.values = values;
        this.customValues = customValues;
    }

    public BaseWilliamChartModel(BaseWilliamChartModel baseWilliamChartModel) {
        setValues(Arrays.copyOf(baseWilliamChartModel.getValues(), baseWilliamChartModel.getValues().length));
        setLabels(Arrays.copyOf(baseWilliamChartModel.getLabels(), baseWilliamChartModel.getLabels().length));
    }

    private BaseWilliamChartModel() {
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public float[] getValues() {
        return values;
    }

    public String[] getCustomValues() {
        return customValues;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public int size() {
        return values.length;
    }

    public boolean hasCustomValues() {
        return customValues.length > 0 && customValues.length == values.length;
    }

    public void increment(int increment) {
        for (int i = 0; i < values.length; i++) {
            values[i] += increment;
        }
    }
}
