
package com.tokopedia.home.beranda.domain.gql.feed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inspirasi {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("experiment_version")
    @Expose
    private String experimentVersion;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("foreign_title")
    @Expose
    private String foreignTitle;
    @SerializedName("widget_url")
    @Expose
    private String widgetUrl;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("recommendation")
    @Expose
    private List<Recommendation> recommendation = null;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getExperimentVersion() {
        return experimentVersion;
    }

    public void setExperimentVersion(String experimentVersion) {
        this.experimentVersion = experimentVersion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForeignTitle() {
        return foreignTitle;
    }

    public void setForeignTitle(String foreignTitle) {
        this.foreignTitle = foreignTitle;
    }

    public String getWidgetUrl() {
        return widgetUrl;
    }

    public void setWidgetUrl(String widgetUrl) {
        this.widgetUrl = widgetUrl;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Recommendation> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<Recommendation> recommendation) {
        this.recommendation = recommendation;
    }

}
