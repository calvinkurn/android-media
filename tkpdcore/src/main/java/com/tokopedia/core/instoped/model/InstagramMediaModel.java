package com.tokopedia.core.instoped.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by Tkpd_Eka on 4/6/2016.
 * modified by m.normansyah on 4/18/2016
 * TODO : MOVE IT TO THE SELLER MODULE AFTER MYPRODUCT MOVED TO SELLER MODULE
 */
public class InstagramMediaModel extends RecyclerViewItem implements Parcelable{
    public InstagramMediaModel(){
        setType(TkpdState.RecyclerView.VIEW_INSTOPED);
    }
    public String filter;
    public String link;
    public String thumbnail;
    public String standardResolution;
    public String captionText;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.filter);
        dest.writeString(this.link);
        dest.writeString(this.thumbnail);
        dest.writeString(this.standardResolution);
        dest.writeString(this.captionText);
    }

    protected InstagramMediaModel(Parcel in) {
        super(in);
        this.filter = in.readString();
        this.link = in.readString();
        this.thumbnail = in.readString();
        this.standardResolution = in.readString();
        this.captionText = in.readString();
    }

    public static final Creator<InstagramMediaModel> CREATOR = new Creator<InstagramMediaModel>() {
        @Override
        public InstagramMediaModel createFromParcel(Parcel source) {
            return new InstagramMediaModel(source);
        }

        @Override
        public InstagramMediaModel[] newArray(int size) {
            return new InstagramMediaModel[size];
        }
    };
}
