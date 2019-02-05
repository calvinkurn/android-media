package com.tokopedia.core.analytics.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by herdimac on 6/24/16.
 */
public abstract class BaseAnalyticsModel {

    protected String name;
    protected String id;
    protected Map<String, String> extraAttr = new HashMap<>();

    public abstract String getName();

    public abstract void setName(String name);

    public abstract void setId(String id);

    public abstract String getId();

    public abstract Map<String, String> getAttr();

    public abstract void setExtraAttr(Map<String, String> extraAttr);


}
