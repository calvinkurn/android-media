package com.tokopedia.discovery.catalog.model;

/**
 * @author by alvarisi on 10/19/16.
 */

public class SingleItemFilter {
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
}
