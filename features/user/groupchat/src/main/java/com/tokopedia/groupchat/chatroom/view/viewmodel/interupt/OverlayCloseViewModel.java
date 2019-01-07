package com.tokopedia.groupchat.chatroom.view.viewmodel.interupt;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by yfsx on 14/12/18.
 */
public class OverlayCloseViewModel extends BaseGroupChatPojo implements Visitable<GroupChatTypeFactory>,Parcelable {


    public static final String TYPE = "overlay_close";

    @SerializedName("closeable")
    @Expose
    private boolean closeable;
    @SerializedName("status")
    @Expose
    private int status;

    public boolean isCloseable() {
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public OverlayCloseViewModel() {
    }

    public OverlayCloseViewModel(boolean closeable, int status) {
        this.closeable = closeable;
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.closeable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.status);
    }

    protected OverlayCloseViewModel(Parcel in) {
        this.closeable = in.readByte() != 0;
        this.status = in.readInt();
    }

    public static final Creator<OverlayCloseViewModel> CREATOR = new Creator<OverlayCloseViewModel>() {
        @Override
        public OverlayCloseViewModel createFromParcel(Parcel source) {
            return new OverlayCloseViewModel(source);
        }

        @Override
        public OverlayCloseViewModel[] newArray(int size) {
            return new OverlayCloseViewModel[size];
        }
    };
}
