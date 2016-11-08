
package com.tokopedia.core.deposit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;

public class SummaryWithdraw {

    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("list")
    @Expose
    private java.util.List<Deposit> list = new ArrayList<Deposit>();
    @SerializedName("error_date")
    @Expose
    private int errorDate = 0;

    /**
     *
     * @return
     *     The summary
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     *     The summary
     */
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    /**
     * @return
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     *     The end_date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return
     *     The paging
     */
    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    /**
     *
     * @param paging
     *     The paging
     */
    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    /**
     *
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     *     The start_date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return
     *     The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     *     The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     *     The list
     */
    public java.util.List<Deposit> getList() {
        return list;
    }

    /**
     *
     * @param list
     *     The list
     */
    public void setList(java.util.List<Deposit> list) {
        this.list = list;
    }

    /**
     *
     * @return
     *     The errorDate
     */
    public Boolean isErrorDate() {
        return errorDate == 1;
    }

    /**
     *
     * @param errorDate
     *     The error_date
     */
    public void setErrorDate(int errorDate) {
        this.errorDate = errorDate;
    }

}
