package com.tokopedia.common_digital.product.presentation.model

class OperatorBuilder {
    private var operatorId: String? = null
    private var operatorType: String? = null
    private var name: String? = null
    private var image: String? = null
    private var lastorderUrl: String? = null
    private var defaultProductId: Int = 0
    private var rule: Rule? = null
    private var prefixList: List<String>? = null
    private var clientNumberList: List<ClientNumber>? = null
    private var productList: List<Product>? = null
    private var ussdCode: String? = null

    fun operatorId(operatorId: String): OperatorBuilder {
        this.operatorId = operatorId
        return this
    }

    fun operatorType(operatorType: String): OperatorBuilder {
        this.operatorType = operatorType
        return this
    }

    fun name(name: String): OperatorBuilder {
        this.name = name
        return this
    }

    fun image(image: String): OperatorBuilder {
        this.image = image
        return this
    }

    fun lastOrderUrl(lastorderUrl: String): OperatorBuilder {
        this.lastorderUrl = lastorderUrl
        return this
    }

    fun defaultProductId(defaultProductId: Int): OperatorBuilder {
        this.defaultProductId = defaultProductId
        return this
    }

    fun rule(rule: Rule): OperatorBuilder {
        this.rule = rule
        return this
    }

    fun prefixList(prefixList: List<String>): OperatorBuilder {
        this.prefixList = prefixList
        return this
    }

    fun clientNumberList(clientNumberList: List<ClientNumber>): OperatorBuilder {
        this.clientNumberList = clientNumberList
        return this
    }

    fun products(productList: List<Product>): OperatorBuilder {
        this.productList = productList
        return this
    }

    fun ussdCode(ussdCode: String): OperatorBuilder {
        this.ussdCode = ussdCode
        return this
    }

    fun createOperator(): Operator {
        return Operator(operatorId!!, operatorType!!, name!!, image!!, lastorderUrl!!, defaultProductId,
                rule!!, prefixList!!, clientNumberList!!, productList!!, ussdCode!!)
    }
}