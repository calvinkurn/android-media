package com.tokopedia.profile.following_list.domain.model;

import java.util.List;

/**
 * Created by yfsx on 29/12/17.
 */

public class FollowingResultDomain {
    private boolean isCanLoadMore;
    private String lastCursor;
    private List<FollowingDomain> followingDomainList;
    private String buttonText;
    private String buttonApplink;

    public FollowingResultDomain(
            boolean isCanLoadMore,
            String lastCursor,
            List<FollowingDomain> followingDomainList,
            String buttonText,
            String buttonApplink) {
        this.isCanLoadMore = isCanLoadMore;
        this.lastCursor = lastCursor;
        this.followingDomainList = followingDomainList;
        this.buttonText = buttonText;
        this.buttonApplink = buttonApplink;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonApplink() {
        return buttonApplink;
    }

    public void setButtonApplink(String buttonApplink) {
        this.buttonApplink = buttonApplink;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    public List<FollowingDomain> getFollowingDomainList() {
        return followingDomainList;
    }

    public void setFollowingDomainList(List<FollowingDomain> followingDomainList) {
        this.followingDomainList = followingDomainList;
    }
}
