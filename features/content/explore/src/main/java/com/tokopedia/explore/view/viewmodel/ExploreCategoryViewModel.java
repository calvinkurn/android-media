package com.tokopedia.explore.view.viewmodel;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreCategoryViewModel {
    private int id;
    private String name;
    private boolean active;

    public ExploreCategoryViewModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ExploreCategoryViewModel(int id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
