package com.tokopedia.discovery.catalog.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author by alvarisi on 10/18/16.
 */

public class CatalogListWrapperData {
    private String catalogId;
    private int start;
    private int row;
    private String orderBy;
    private String condition;
    private String location;

    private CatalogDetailListData data;

    public CatalogListWrapperData(String catalogId) {
        this.catalogId = catalogId;
        this.start = 1;
        this.row = 10;
        this.orderBy = "";
        this.condition = "";
        this.location = "";
    }


    public TKPDMapParam<String, String> getParams() {
        TKPDMapParam<String, String> map = new TKPDMapParam<>();
        map.put("ctg_id", catalogId);
        map.put("start", String.valueOf(getPage(start)));
        map.put("row", String.valueOf(row));
        map.put("ob", orderBy);
        map.put("condition", condition);
        map.put("floc", location);
        return map;
    }


    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CatalogDetailListData getData() {
        return data;
    }

    public void setData(CatalogDetailListData data) {
        this.data = data;
    }

    private int getPage(int page) {
        return 10 * (page - 1);
    }

    public void refreshPage() {
        this.start = 1;
    }

    public void refreshData() {
        this.start = 1;
        this.row = 10;
        this.orderBy = "";
        this.condition = "";
        this.location = "";
    }
}
