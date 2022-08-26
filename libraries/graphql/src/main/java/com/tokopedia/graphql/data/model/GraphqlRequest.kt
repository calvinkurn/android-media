package com.tokopedia.graphql.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlConstant
import java.lang.reflect.Type

/**
 * Object of this class will be dispatch over the network
 */
class GraphqlRequest {
    @SerializedName(GraphqlConstant.GqlApiKeys.QUERY)
    var query /*Mandatory parameter*/: String = ""

    @SerializedName(GraphqlConstant.GqlApiKeys.VARIABLES)
    var variables: Map<String, Any?>? = null

    @SerializedName(GraphqlConstant.GqlApiKeys.OPERATION_NAME)
    var operationName: String? = null
        private set

    /*transient by nature hence it will not be part of request body*/
    @Transient
    var typeOfT /*Mandatory parameter*/: Type
        private set

    @Transient
    var queryNameList: List<String>? = emptyList()
        private set

    @Transient
    var queryCopy: String

    @Transient
    var queryHashRetryCount = 1

    @Transient
    var isDoQueryHash = false

    @Transient
    var url: String? = null
        private set

    /*transient by nature hence it will not be part of request body*/
    @Transient
    var isShouldThrow = true /*Optional parameter*/
        private set

    @Expose(serialize = false, deserialize = false)
    @Transient
    var isNoCache = false

    @SerializedName(GraphqlConstant.GqlApiKeys.MD5)
    var md5: String
        private set

    fun setUrlPath(urlPath: String?) {
        url = urlPath
    }

    constructor(gqlQueryInterface: GqlQueryInterface,
                typeOfT: Type,
                variables: Map<String, Any?>? = null,
                shouldThrow: Boolean = true) {
        this.query = gqlQueryInterface.getQuery()
        this.operationName = gqlQueryInterface.getTopOperationName()
        this.queryNameList = gqlQueryInterface.getOperationNameList()
        queryCopy = query
        md5 = FingerprintManager.md5(query)
        this.typeOfT = typeOfT
        this.variables = variables
        isShouldThrow = shouldThrow
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(query: String?, typeOfT: Type?) {
        this.query = query ?: ""
        queryCopy = query ?: ""
        this.typeOfT = typeOfT ?: String::class.java
        md5 = FingerprintManager.md5(query)
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(query: String?, doQueryHash: Boolean, typeOfT: Type?) {
        this.query = query ?: ""
        queryCopy = query ?: ""
        this.typeOfT = typeOfT ?: String::class.java
        isDoQueryHash = doQueryHash
        md5 = FingerprintManager.md5(query)
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(query: String?, typeOfT: Type?, shouldThrow: Boolean) : this(query, typeOfT) {
        isShouldThrow = shouldThrow
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(query: String?, typeOfT: Type?, variables: Map<String, Any?>?) : this(query, typeOfT) {
        this.variables = variables
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(
        doQueryHash: Boolean,
        query: String?,
        typeOfT: Type?,
        variables: Map<String, Any?>?
    ) : this(query, typeOfT) {
        this.variables = variables
        isDoQueryHash = doQueryHash
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(
        query: String?, typeOfT: Type?, variables: Map<String, Any?>?,
        shouldThrow: Boolean
    ) : this(query, typeOfT, variables) {
        isShouldThrow = shouldThrow
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(
        query: String?, typeOfT: Type?, variables: Map<String, Any?>?,
        operationName: String?
    ) : this(query, typeOfT, variables) {
        this.operationName = operationName
    }

    @Deprecated("use constructor(GqlQueryInterface, ..)")
    constructor(
        query: String?, typeOfT: Type?, variables: Map<String, Any?>?,
        operationName: String?, shouldThrow: Boolean
    ) : this(query, typeOfT, variables, operationName) {
        isShouldThrow = shouldThrow
    }

    //Do not rewrite on remove it
    override fun toString(): String {
        return "GraphqlRequest{" +
                "query='" + query + '\'' +
                ", variables=" + variables +
                ", operationName='" + operationName + '\'' +
                ", typeOfT=" + typeOfT +
                ", shouldThrow=" + isShouldThrow +
                '}'
    }

    fun cacheKey(): String {
        return FingerprintManager.md5(query + variables)
    }
}