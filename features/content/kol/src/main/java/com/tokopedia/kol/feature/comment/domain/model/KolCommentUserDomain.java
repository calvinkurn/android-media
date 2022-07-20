package com.tokopedia.kol.feature.comment.domain.model;

/**
 * @author by nisie on 11/8/17.
 */

public class KolCommentUserDomain {

    private final String id;
    private final boolean isKol;
    private final String photo;
    private final String name;

    public KolCommentUserDomain(String id, boolean iskol, String name, String photo) {
        this.id = id;
        this.isKol = iskol;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public boolean isKol() {
        return isKol;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }
}
