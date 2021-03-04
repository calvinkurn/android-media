package com.tokopedia.graphql.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.GraphqlConstant;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Object of this class will be dispatch over the network
 */
public class GraphqlRequest {
    @Expose
    @SerializedName(GraphqlConstant.GqlApiKeys.QUERY)
    private String query; /*Mandatory parameter*/

    @Expose
    @SerializedName(GraphqlConstant.GqlApiKeys.VARIABLES)
    private Map<String, Object> variables;

    @Expose
    @SerializedName(GraphqlConstant.GqlApiKeys.OPERATION_NAME)
    private String operationName;

    /*transient by nature hence it will not be part of request body*/
    private transient Type typeOfT; /*Mandatory parameter*/

    private transient String queryCopy;

    private transient int queryHashRetryCount = 1;

    private transient boolean doQueryHash = false;

    /*transient by nature hence it will not be part of request body*/
    private transient boolean shouldThrow = true; /*Optional parameter*/

    @Expose(serialize = false, deserialize = false)
    private transient boolean noCache;

    @Expose(serialize = false, deserialize = false)
    private String md5;

    public String getQueryCopy() {
        return queryCopy;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setQueryCopy(String queryCopy) {
        this.queryCopy = queryCopy;
    }

    public boolean isDoQueryHash() {
        return doQueryHash;
    }

    public void setDoQueryHash(boolean doQueryHash) {
        this.doQueryHash = doQueryHash;
    }

    public int getQueryHashRetryCount() {
        return queryHashRetryCount;
    }

    public void setQueryHashRetryCount(int queryHashRetryCount) {
        this.queryHashRetryCount = queryHashRetryCount;
    }

    public GraphqlRequest(String query, Type typeOfT) {
        this.query = query;
        this.queryCopy = query;
        this.typeOfT = typeOfT;
        this.md5 = FingerprintManager.md5(query);
    }

    public GraphqlRequest(String query, boolean doQueryHash, Type typeOfT) {
        this.query = query;
        this.queryCopy = query;
        this.typeOfT = typeOfT;
        this.doQueryHash = doQueryHash;
        this.md5 = FingerprintManager.md5(query);
    }

    /**
     * Use constructor without param shouldThrow for null checker
     *
     * @param query
     * @param typeOfT
     * @param shouldThrow
     */
    public GraphqlRequest(String query, Type typeOfT, boolean shouldThrow) {
        this(query, typeOfT);
        this.shouldThrow = shouldThrow;
    }

    public GraphqlRequest(String query, Type typeOfT, Map<String, Object> variables) {
        this(query, typeOfT);
        this.variables = variables;
    }

    public GraphqlRequest(boolean doQueryHash, String query, Type typeOfT, Map<String, Object> variables) {
        this(query, typeOfT);
        this.variables = variables;
        this.doQueryHash = doQueryHash;
    }

    /**
     * Use constructor without param shouldThrow for null checker
     *
     * @param query
     * @param typeOfT
     * @param variables
     * @param shouldThrow
     */
    @Deprecated
    public GraphqlRequest(String query, Type typeOfT, Map<String, Object> variables,
                          boolean shouldThrow) {
        this(query, typeOfT, variables);
        this.shouldThrow = shouldThrow;
    }

    public GraphqlRequest(String query, Type typeOfT, Map<String, Object> variables,
                          String operationName) {
        this(query, typeOfT, variables);
        this.operationName = operationName;
    }

    /**
     * Use constructor without param shouldThrow for null checker
     *
     * @param query
     * @param typeOfT
     * @param variables
     * @param operationName
     * @param shouldThrow
     */
    @Deprecated
    public GraphqlRequest(String query, Type typeOfT, Map<String, Object> variables,
                          String operationName, boolean shouldThrow) {
        this(query, typeOfT, variables, operationName);
        this.shouldThrow = shouldThrow;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getOperationName() {
        return operationName;
    }

    public Type getTypeOfT() {
        return typeOfT;
    }

    public boolean isShouldThrow() {
        return shouldThrow;
    }

    /**
     * Should use nullCheker
     *
     * @param shouldThrow
     */
    @Deprecated
    public void setShouldThrow(boolean shouldThrow) {
        this.shouldThrow = shouldThrow;
    }

    public boolean isNoCache() {
        return noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public String getMd5() {
        return md5;
    }

    //Do not rewrite on remove it
    @Override
    public String toString() {
        return "GraphqlRequest{" +
                "query='" + query + '\'' +
                ", variables=" + variables +
                ", operationName='" + operationName + '\'' +
                ", typeOfT=" + typeOfT +
                ", shouldThrow=" + shouldThrow +
                '}';
    }

    public String cacheKey() {
        return FingerprintManager.md5(query + variables);
    }
}
