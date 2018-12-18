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

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.closeable ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.interuptViewModel, flags);
    }

    public OverlayViewModel() {
        this.closeable = true;
        this.interuptViewModel = new InteruptViewModel();
    }

    public OverlayViewModel(boolean closeable, InteruptViewModel interuptViewModel) {
        this.closeable = closeable;
        this.interuptViewModel = interuptViewModel;
    }

    protected OverlayViewModel(Parcel in) {
        this.closeable = in.readByte() != 0;
        this.interuptViewModel = in.readParcelable(InteruptViewModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<OverlayViewModel> CREATOR = new Parcelable.Creator<OverlayViewModel>() {
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
