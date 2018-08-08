package com.tokopedia.core.talkview.product.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentTalkModel implements Parcelable {

    @SerializedName("list")
    @Expose
    private List<CommentTalk> commentTalk = new ArrayList<CommentTalk>();

    /**
     * 
     * @return
     *     The commentTalk
     */
    public List<CommentTalk> getCommentTalk() {
        return commentTalk;
    }

    /**
     * 
     * @param commentTalk
     *     The comment_talk
     */
    public void setCommentTalk(List<CommentTalk> commentTalk) {
        this.commentTalk = commentTalk;
    }


    protected CommentTalkModel(Parcel in) {
        if (in.readByte() == 0x01) {
            commentTalk = new ArrayList<CommentTalk>();
            in.readList(commentTalk, CommentTalk.class.getClassLoader());
        } else {
            commentTalk = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (commentTalk == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(commentTalk);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommentTalkModel> CREATOR = new Parcelable.Creator<CommentTalkModel>() {
        @Override
        public CommentTalkModel createFromParcel(Parcel in) {
            return new CommentTalkModel(in);
        }

        @Override
        public CommentTalkModel[] newArray(int size) {
            return new CommentTalkModel[size];
        }
    };
}