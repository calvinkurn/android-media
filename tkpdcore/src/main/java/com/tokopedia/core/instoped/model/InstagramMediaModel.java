package com.tokopedia.core.instoped.model;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcel;

/**
 * Created by Tkpd_Eka on 4/6/2016.
 * modified by m.normansyah on 4/18/2016
 */
public class InstagramMediaModel extends RecyclerViewItem {
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
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.filter);
        dest.writeString(this.link);
        dest.writeString(this.thumbnail);
        dest.writeString(this.standardResolution);
        dest.writeString(this.captionText);
    }

    protected InstagramMediaModel(android.os.Parcel in) {
        super(in);
        this.filter = in.readString();
        this.link = in.readString();
        this.thumbnail = in.readString();
        this.standardResolution = in.readString();
        this.captionText = in.readString();
    }

    public static final Creator<InstagramMediaModel> CREATOR = new Creator<InstagramMediaModel>() {
        @Override
        public InstagramMediaModel createFromParcel(android.os.Parcel source) {
            return new InstagramMediaModel(source);
        }

        @Override
        public InstagramMediaModel[] newArray(int size) {
            return new InstagramMediaModel[size];
        }
    };
}
