package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class Page {

    @SerializedName("current")
    @Expose
    private int current;
    @SerializedName("per_page")
    @Expose
    private int perPage;
    @SerializedName("min")
    @Expose
    private int min;
    @SerializedName("max")
    @Expose
    private int max;
    @SerializedName("total")
    @Expose
    private int total;

    /**
     *
     * @return
     * The current
     */
    public int getCurrent() {
        return current;
    }

    /**
     *
     * @param current
     * The current
     */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
     *
     * @return
     * The perPage
     */
    public int getPerPage() {
        return perPage;
    }

    /**
     *
     * @param perPage
     * The per_page
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    /**
     *
     * @return
     * The min
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @param min
     * The min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @return
     * The max
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @param max
     * The max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @return
     * The total
     */
    public int getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(int total) {
        this.total = total;
    }

}
