package com.tokopedia.digital.tokocash.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 10/17/17.
 */

public class ResponseHelpHistoryEntity {

    @SerializedName("Topic")
    private String topic;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("subject")
    private String subject;
    @SerializedName("message")
    private String message;
    @SerializedName("invoice")
    private String invoice;
    @SerializedName("product")
    private String product;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("categories")
    private String categories;
    @SerializedName("categories_delimiter")
    private String categoriesDelimiter;
    @SerializedName("user_agent")
    private String userAgent;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getCategoriesDelimiter() {
        return categoriesDelimiter;
    }

    public void setCategoriesDelimiter(String categoriesDelimiter) {
        this.categoriesDelimiter = categoriesDelimiter;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
