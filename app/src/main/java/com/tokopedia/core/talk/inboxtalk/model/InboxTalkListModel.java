package com.tokopedia.core.talk.inboxtalk.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxTalkListModel implements Parcelable {

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("is_unread")
    @Expose
    private int isUnread;
    @SerializedName("list")
    @Expose
    private List<InboxTalk> list = new ArrayList<>();

    /**
     *
     * @return
     *     The paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     *
     * @param paging
     *     The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    /**
     *
     * @return
     *     The isUnread
     */
    public int getIsUnread() {
        return isUnread;
    }

    /**
     *
     * @param isUnread
     *     The is_unread
     */
    public void setIsUnread(int isUnread) {
        this.isUnread = isUnread;
    }

    /**
     *
     * @return
     *     The list
     */
    public List<InboxTalk> getList() {
        return list;
    }

    /**
     *
     * @param list
     *     The list
     */
    public void setList(List<InboxTalk> list) {
        this.list = list;
    }



    protected InboxTalkListModel(Parcel in) {
        paging = (Paging) in.readValue(Paging.class.getClassLoader());
        isUnread = in.readInt();
        if (in.readByte() == 0x01) {
            list = new ArrayList<InboxTalk>();
            in.readList(list, InboxTalk.class.getClassLoader());
        } else {
            list = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(paging);
        dest.writeInt(isUnread);
        if (list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(list);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<InboxTalkListModel> CREATOR = new Creator<InboxTalkListModel>() {
        @Override
        public InboxTalkListModel createFromParcel(Parcel in) {
            return new InboxTalkListModel(in);
        }

        @Override
        public InboxTalkListModel[] newArray(int size) {
            return new InboxTalkListModel[size];
        }
    };
}