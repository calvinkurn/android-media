package com.tokopedia.core.talk.talkproduct.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TalkProductModel implements Parcelable {

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("list")
    @Expose
    private List<Talk> talkList = new ArrayList<Talk>();

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
     *     The talk
     */
    public List<Talk> getTalk() {
        return talkList;
    }

    /**
     *
     * @param talk
     *     The talk
     */
    public void setTalk(List<Talk> talk) {
        this.talkList = talk;
    }


    protected TalkProductModel(Parcel in) {
        paging = (Paging) in.readValue(Paging.class.getClassLoader());
        if (in.readByte() == 0x01) {
            talkList = new ArrayList<Talk>();
            in.readList(talkList, Talk.class.getClassLoader());
        } else {
            talkList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(paging);
        if (talkList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(talkList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TalkProductModel> CREATOR = new Parcelable.Creator<TalkProductModel>() {
        @Override
        public TalkProductModel createFromParcel(Parcel in) {
            return new TalkProductModel(in);
        }

        @Override
        public TalkProductModel[] newArray(int size) {
            return new TalkProductModel[size];
        }
    };
}