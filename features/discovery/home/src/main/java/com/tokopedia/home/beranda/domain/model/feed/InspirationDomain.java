package com.tokopedia.home.beranda.domain.model.feed;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author by nisie on 10/26/17.
 */

public class InspirationDomain {

    @Nullable
    private final String experimentVersion;


    @Nullable
    private final String source;

    @Nullable
    private final String title;

    @Nullable
    private final String foreignTitle;

    @Nullable
    private final String widgetUrl;

    @Nullable
    private final List<InspirationItemDomain> listInspirationItem;

    public InspirationDomain(String experimentVersion, String source, String title,
                             String foreignTitle, String widgetUrl,
                             List<InspirationItemDomain> listInspirationItem) {
        this.experimentVersion = experimentVersion;
        this.source = source;
        this.title = title;
        this.foreignTitle = foreignTitle;
        this.widgetUrl = widgetUrl;
        this.listInspirationItem = listInspirationItem;
    }

    @Nullable
    public String getExperimentVersion() {
        return experimentVersion;
    }

    @Nullable
    public String getSource() {
        return source;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getForeignTitle() {
        return foreignTitle;
    }

    @Nullable
    public String getWidgetUrl() {
        return widgetUrl;
    }

    @Nullable
    public List<InspirationItemDomain> getListInspirationItem() {
        return listInspirationItem;
    }
}
