package com.tokopedia.graphql.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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

    @Expose
    @SerializedName(GraphqlConstant.GqlApiKeys.TYPE)
    /*transient by nature hence it will not be part of request body*/
    private transient Type typeOfT; /*Mandatory parameter*/

    public GraphqlRequest(String query, Type typeOfT) {
        this.query = query;
        this.typeOfT = typeOfT;
    }

    public GraphqlRequest(String query, Type typeOfT, Map<String, Object> variables) {
        this(query, typeOfT);
        this.variables = variables;
    }

    public GraphqlRequest(String query, Type typeOfT, Map<String, Object> variables, String operationName) {
        this(query, typeOfT, variables);
        this.operationName = operationName;
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


    //Do not rewrite on remove it
    @Override
    public String toString() {
        return "GraphqlRequest{" +
                "query='" + query + '\'' +
                ", variables=" + variables +
                ", operationName='" + operationName + '\'' +
                ", typeOfT=" + typeOfT +
                '}';
    }
}
