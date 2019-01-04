package com.tokopedia.common_digital.product.presentation.model;

import java.util.List;

public class OperatorBuilder {
    private String operatorId;
    private String operatorType;
    private String name;
    private String image;
    private String lastorderUrl;
    private int defaultProductId;
    private Rule rule;
    private List<String> prefixList;
    private List<ClientNumber> clientNumberList;
    private List<Product> productList;
    private String ussdCode;

    public OperatorBuilder operatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public OperatorBuilder operatorType(String operatorType) {
        this.operatorType = operatorType;
        return this;
    }

    public OperatorBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OperatorBuilder image(String image) {
        this.image = image;
        return this;
    }

    public OperatorBuilder lastOrderUrl(String lastorderUrl) {
        this.lastorderUrl = lastorderUrl;
        return this;
    }

    public OperatorBuilder defaultProductId(int defaultProductId) {
        this.defaultProductId = defaultProductId;
        return this;
    }

    public OperatorBuilder rule(Rule rule) {
        this.rule = rule;
        return this;
    }

    public OperatorBuilder prefixList(List<String> prefixList) {
        this.prefixList = prefixList;
        return this;
    }

    public OperatorBuilder clientNumberList(List<ClientNumber> clientNumberList) {
        this.clientNumberList = clientNumberList;
        return this;
    }

    public OperatorBuilder products(List<Product> productList) {
        this.productList = productList;
        return this;
    }

    public OperatorBuilder ussdCode(String ussdCode) {
        this.ussdCode = ussdCode;
        return this;
    }

    public Operator createOperator() {
        return new Operator(operatorId, operatorType, name, image, lastorderUrl, defaultProductId,
                rule, prefixList, clientNumberList, productList, ussdCode);
    }
}