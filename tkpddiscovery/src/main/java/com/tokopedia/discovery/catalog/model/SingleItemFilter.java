package com.tokopedia.discovery.catalog.model;

import com.tokopedia.core.customadapter.ColoredFilterModel;

/**
 * @author by alvarisi on 10/19/16.
 */

public class SingleItemFilter implements ColoredFilterModel{
    private String id;
    private String name;

    public SingleItemFilter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getText() {
        return name;
    }
}
