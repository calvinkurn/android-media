package com.tokopedia.core.talkview.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.talk.model.model.InboxTalk;

public class TalkDetailModel implements Parcelable {

    @SerializedName("talk")
    @Expose
    private InboxTalk talk;

    @SerializedName("list")
    @Expose
    private List<TalkDetail> talkDetail = new ArrayList<TalkDetail>();

    protected TalkDetailModel(Parcel in) {
        talk = in.readParcelable(InboxTalk.class.getClassLoader());
        talkDetail = in.createTypedArrayList(TalkDetail.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(talk, flags);
        dest.writeTypedList(talkDetail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TalkDetailModel> CREATOR = new Creator<TalkDetailModel>() {
        @Override
        public TalkDetailModel createFromParcel(Parcel in) {
            return new TalkDetailModel(in);
        }

        @Override
        public TalkDetailModel[] newArray(int size) {
            return new TalkDetailModel[size];
        }
    };

    /**
     *
     * @return
     *     The talkDetail
     */
    public List<TalkDetail> getTalkDetail() {
        return talkDetail;
    }

    /**
     *
     * @param talkDetail
     *     The talk_detail
     */
    public void setTalkDetail(List<TalkDetail> talkDetail) {
        this.talkDetail = talkDetail;
    }

    public InboxTalk getTalk() {
        return talk;
    }

    public void setTalk(InboxTalk talk) {
        this.talk = talk;
    }


//    protected TalkDetailModel(Parcel in) {
//        if (in.readByte() == 0x01) {
//            talkDetail = new ArrayList<TalkDetail>();
//            in.readList(talkDetail, TalkDetail.class.getClassLoader());
//        } else {
//            talkDetail = null;
//        }
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        if (talkDetail == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeList(talkDetail);
//        }
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<TalkDetailModel> CREATOR = new Parcelable.Creator<TalkDetailModel>() {
//        @Override
//        public TalkDetailModel createFromParcel(Parcel in) {
//            return new TalkDetailModel(in);
//        }
//
//        @Override
//        public TalkDetailModel[] newArray(int size) {
//            return new TalkDetailModel[size];
//        }
//    };
}