package com.tokopedia.core.deposit.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 4/8/16.
 */
public class SummaryDepositParam {


    private static final String PARAM_START_DATE = "start_date";
    private static final String PARAM_END_DATE = "end_date";
    private static final String PARAM_PER_PAGE = "per_page";
    private static final String PARAM_PAGE = "page";

    String endDate;
    String page;
    String perPage = "10";
    String startDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerPage() {
        return perPage;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Map<String, String> getParamSummaryDeposit() {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_START_DATE, startDate);
        param.put(PARAM_END_DATE, endDate);
        param.put(PARAM_PER_PAGE, perPage);
        param.put(PARAM_PAGE, page);
        return param;
    }

}
