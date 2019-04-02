package com.tokopedia.saldodetails.response.model;

import java.util.HashMap;
import java.util.Map;

public class SummaryDepositParam {

    private static final String PARAM_START_DATE = "dateFrom";
    private static final String PARAM_END_DATE = "dateTo";
    private static final String PARAM_PER_PAGE = "maxRows";
    private static final String PARAM_PAGE = "page";
    private static final String SALDO_TYPE = "saldoType";
    public static final String PARAM_IS_SELLER = "isSeller";

    private String endDate;
    private int page;
    private int perPage = 25;
    private String startDate;
    private int saldoType;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setSaldoType(int type) {
        saldoType = type;
    }

    public Map<String, Object> getParamSummaryDeposit() {
        Map<String, Object> param = new HashMap<>();
        param.put(PARAM_START_DATE, startDate);
        param.put(PARAM_END_DATE, endDate);
        param.put(PARAM_PER_PAGE, perPage);
        param.put(PARAM_PAGE, page);
        param.put(SALDO_TYPE, saldoType);
        return param;
    }

}
