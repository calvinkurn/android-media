package com.tokopedia.review.feature.reputationhistory.data.model.request;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tokopedia.review.feature.reputationhistory.constant.ReputationNetworkConstant.PARAM_FILTER;
import static com.tokopedia.review.feature.reputationhistory.constant.ReputationNetworkConstant.PARAM_PAGE;

/**
 * @author normansyahputa on 3/15/17.
 */
public class SellerReputationRequest {

    public static final String START_DATE = "start_date";
    public static final String DUE_DATE = "due_date";
    public static final String SIZE = "size";
    public static final String NUMBER = "number";
    public static final int PER_PAGE = 10;
    private static final String TAG = "SellerReputationRequest";
    String endDate;
    int page;
    int perPage = PER_PAGE;
    String startDate;
    TKPDMapParam<String, String> param;
    JSONObject pageValueJsonObject;
    JSONObject filterValueJsonObject;
    long sDate;
    long eDate;

    public SellerReputationRequest() {
        param = new TKPDMapParam<>();
        pageValueJsonObject = new JSONObject();
        filterValueJsonObject = new JSONObject();
    }

    public long getsDate() {
        return sDate;
    }

    public void setsDate(long sDate) {
        this.sDate = sDate;
    }

    public long geteDate() {
        return eDate;
    }

    public void seteDate(long eDate) {
        this.eDate = eDate;
    }

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

    public void incrementPage() {
        setPage(getPage() + 1);
    }

    public void resetPage() {
        setPage(0);
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

    public TKPDMapParam<String, String> getParamSummaryReputation() {

        try {
            pageValueJsonObject.put(NUMBER, page);
            pageValueJsonObject.put(SIZE, perPage);
        } catch (JSONException e) {
            // remove old parameter
            pageValueJsonObject.remove(NUMBER);
            pageValueJsonObject.remove(SIZE);
        }

        try {
            filterValueJsonObject.put(START_DATE, startDate);
            filterValueJsonObject.put(DUE_DATE, endDate);
        } catch (Exception ex) {
            // remove old parameter
            filterValueJsonObject.remove(START_DATE);
            filterValueJsonObject.remove(DUE_DATE);
        }

        String pageValue = pageValueJsonObject.toString();
        if(pageValue!= null) {
            param.put(PARAM_PAGE, pageValue);
        }
        String filterValue = filterValueJsonObject.toString();
        if(filterValue!= null) {
            param.put(PARAM_FILTER, filterValue);
        }
        return param;
    }
}
