package com.tokopedia.cacheapi.data.source.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.cacheapi.data.source.db.DbFlowDatabase;

/**
 * Created by normansyahputa on 8/9/17.
 */
@Table(database = DbFlowDatabase.class, updateConflict = ConflictAction.REPLACE, insertConflict = ConflictAction.REPLACE)
public class CacheApiData extends BaseModel {

    public static final String COLUMN_WHITE_LIST_ID = "white_list_id";

    @PrimaryKey
    @Column
    private String host;

    @PrimaryKey
    @Column
    private String path;

    @PrimaryKey
    @Column(name = "request_param")
    private String requestParam;

    @PrimaryKey
    @Column
    private String method;

    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "response_time")
    private long responseTime;

    @Column(name = "expired_time")
    public long expiredTime;

    @Column(name = COLUMN_WHITE_LIST_ID)
    public long whiteListId;

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getWhiteListId() {
        return whiteListId;
    }

    public void setWhiteListId(long whiteListId) {
        this.whiteListId = whiteListId;
    }
}
