package com.tokopedia.sellerapp.home.model.rescenter;

import org.parceler.Parcel;

/**
 * Created by normansyahputa on 8/31/16.
 */

@Parcel
public class ResCenterInboxDataPass {
    int requestAs;
    String requestAsString;
    int sortType;
    String sortTypeString;
    int filterStatus;
    String filterStatusString;
    int readUnreadStatus;
    String readUnreadStatusString;
    int requestPage;

    public int getRequestAs() {
        return requestAs;
    }

    public void setRequestAs(int requestAs) {
        this.requestAs = requestAs;
    }

    public String getRequestAsString() {
        return requestAsString;
    }

    public void setRequestAsString(String requestAsString) {
        this.requestAsString = requestAsString;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public String getSortTypeString() {
        return sortTypeString;
    }

    public void setSortTypeString(String sortTypeString) {
        this.sortTypeString = sortTypeString;
    }

    public int getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(int filterStatus) {
        this.filterStatus = filterStatus;
    }

    public String getFilterStatusString() {
        return filterStatusString;
    }

    public void setFilterStatusString(String filterStatusString) {
        this.filterStatusString = filterStatusString;
    }

    public int getReadUnreadStatus() {
        return readUnreadStatus;
    }

    public void setReadUnreadStatus(int readUnreadStatus) {
        this.readUnreadStatus = readUnreadStatus;
    }

    public String getReadUnreadStatusString() {
        return readUnreadStatusString;
    }

    public void setReadUnreadStatusString(String readUnreadStatusString) {
        this.readUnreadStatusString = readUnreadStatusString;
    }

    public int getRequestPage() {
        return requestPage;
    }

    public void setRequestPage(int requestPage) {
        this.requestPage = requestPage;
    }
}
