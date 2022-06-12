package com.tokopedia.profile.following_list.domain.model;

/**
 * Created by yfsx on 28/12/17.
 */

public class FollowingDomain {
    private final String id;
    private final String name;
    private final String avatarUrl;
    private final String profileApplink;
    private final String profileUrl;
    private final boolean isInfluencer;

    public FollowingDomain(String id, String name, String avatarUrl, String profileApplink,
                           String profileUrl, boolean isInfluencer) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.profileApplink = profileApplink;
        this.profileUrl = profileUrl;
        this.isInfluencer = isInfluencer;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getProfileApplink() {
        return profileApplink;
    }

    public boolean isInfluencer() {
        return isInfluencer;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
