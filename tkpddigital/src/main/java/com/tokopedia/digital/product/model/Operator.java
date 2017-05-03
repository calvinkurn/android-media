package com.tokopedia.digital.product.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Operator {

    private String operatorId;
    private String operatorType;

    private String name;
    private String image;
    private String lastorderUrl;
    private int defaultProductId;
    private Rule rule;
    private List<String> prefixList = new ArrayList<>();
    private List<Field> fieldList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastorderUrl() {
        return lastorderUrl;
    }

    public void setLastorderUrl(String lastorderUrl) {
        this.lastorderUrl = lastorderUrl;
    }

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public void setDefaultProductId(int defaultProductId) {
        this.defaultProductId = defaultProductId;
    }

    public List<String> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
