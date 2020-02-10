package com.tokopedia.events.domain.model;

/**
 * Created by pranaymohapatra on 01/05/18.
 */

public class ProductRatingDomain {
    private int id;
    private int totalLikes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }
}
