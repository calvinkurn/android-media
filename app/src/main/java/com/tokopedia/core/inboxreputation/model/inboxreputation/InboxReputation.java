
package com.tokopedia.core.inboxreputation.model.inboxreputation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

public class InboxReputation implements Parcelable{

    @SerializedName("navs_tab")
    @Expose
    NavsTab navsTab;
    @SerializedName("list")
    @Expose
    java.util.List<InboxReputationItem> list = new ArrayList<InboxReputationItem>();
    @SerializedName("paging")
    @Expose
    PagingHandler.PagingHandlerModel paging;

    protected InboxReputation(Parcel in) {
        list = in.createTypedArrayList(InboxReputationItem.CREATOR);
        paging = in.readParcelable(PagingHandler.PagingHandlerModel.class.getClassLoader());
    }

    public static final Creator<InboxReputation> CREATOR = new Creator<InboxReputation>() {
        @Override
        public InboxReputation createFromParcel(Parcel in) {
            return new InboxReputation(in);
        }

        @Override
        public InboxReputation[] newArray(int size) {
            return new InboxReputation[size];
        }
    };

    public NavsTab getNavsTab() {
        return navsTab;
    }

    public void setNavsTab(NavsTab navsTab) {
        this.navsTab = navsTab;
    }

    public List<InboxReputationItem> getList() {
        return list;
    }

    public void setList(List<InboxReputationItem> list) {
        this.list = list;
    }

    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
        dest.writeParcelable(paging, flags);
    }
}
