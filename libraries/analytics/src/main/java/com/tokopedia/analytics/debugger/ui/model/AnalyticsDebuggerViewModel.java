package com.tokopedia.analytics.debugger.ui.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.database.GtmLogDB;
import com.tokopedia.analytics.debugger.ui.adapter.AnalyticsDebuggerTypeFactory;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsDebuggerViewModel implements Visitable<AnalyticsDebuggerTypeFactory> {
    public static final int LAYOUT = R.layout.item_analytics_debugger;

    private long id;
    private String name;
    private String category;
    private String data;
    private String dataExcerpt;
    private String timestamp;

    public AnalyticsDebuggerViewModel() {

    }

    @Override
    public int type(AnalyticsDebuggerTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataExcerpt() {
        return dataExcerpt;
    }

    public void setDataExcerpt(String dataExcerpt) {
        this.dataExcerpt = dataExcerpt;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
