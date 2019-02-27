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
public class OverlayViewModel extends BaseGroupChatPojo implements Visitable<GroupChatTypeFactory>,Parcelable {


    public static final String TYPE = "overlay_message";

    @SerializedName("closeable")
    @Expose
    private boolean closeable;
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("assets")
    @Expose
    private InteruptViewModel interuptViewModel;

    public boolean isCloseable() {
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    public InteruptViewModel getInteruptViewModel() {
        return interuptViewModel;
    }

    public void setInteruptViewModel(InteruptViewModel interuptViewModel) {
        this.interuptViewModel = interuptViewModel;
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

    public OverlayViewModel() {
        this.closeable = true;
        this.interuptViewModel = new InteruptViewModel();
        this.status = 0;
    }

    public OverlayViewModel(boolean closeable, int status, InteruptViewModel interuptViewModel) {
        this.closeable = closeable;
        this.status = status;
        this.interuptViewModel = interuptViewModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.closeable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.status);
        dest.writeParcelable(this.interuptViewModel, flags);
    }

    protected OverlayViewModel(Parcel in) {
        this.closeable = in.readByte() != 0;
        this.status = in.readInt();
        this.interuptViewModel = in.readParcelable(InteruptViewModel.class.getClassLoader());
    }

    public static final Creator<OverlayViewModel> CREATOR = new Creator<OverlayViewModel>() {
        @Override
        public OverlayViewModel createFromParcel(Parcel source) {
            return new OverlayViewModel(source);
        }

        @Override
        public OverlayViewModel[] newArray(int size) {
            return new OverlayViewModel[size];
        }
    };
}
