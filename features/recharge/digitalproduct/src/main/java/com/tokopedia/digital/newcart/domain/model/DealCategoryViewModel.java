package com.tokopedia.digital.newcart.domain.model;

public class DealCategoryViewModel {
    private long id;
    private String name;
    private String url;

    public DealCategoryViewModel() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
