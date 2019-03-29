package com.tokopedia.logisticdata.data.entity.address;

/**
 * Created on 5/19/16.
 */
public class GetAddressDataPass {

    private int sortID;
    private int page;
    private String query;
    private String profileUserID;

    public int getSortID() {
        return sortID;
    }

    public void setSortID(int sortID) {
        this.sortID = sortID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getProfileUserID() {
        return profileUserID;
    }

    public void setProfileUserID(String profileUserID) {
        this.profileUserID = profileUserID;
    }
}
