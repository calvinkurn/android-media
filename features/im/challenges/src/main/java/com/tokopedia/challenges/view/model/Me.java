
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Me implements Parcelable{

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SubmissionCounts")
    @Expose
    private SubmissionCounts submissionCounts;
    @SerializedName("Liked")
    @Expose
    private boolean liked;
    @SerializedName("AuthProvider")
    @Expose
    private AuthProvider authProvider;

    protected Me(Parcel in) {
        id = in.readString();
        submissionCounts = in.readParcelable(SubmissionCounts.class.getClassLoader());
        liked = in.readByte() != 0;
        authProvider = in.readParcelable(AuthProvider.class.getClassLoader());
    }

    public static final Creator<Me> CREATOR = new Creator<Me>() {
        @Override
        public Me createFromParcel(Parcel in) {
            return new Me(in);
        }

        @Override
        public Me[] newArray(int size) {
            return new Me[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubmissionCounts getSubmissionCounts() {
        return submissionCounts;
    }

    public void setSubmissionCounts(SubmissionCounts submissionCounts) {
        this.submissionCounts = submissionCounts;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(submissionCounts, i);
        parcel.writeByte((byte) (liked ? 1 : 0));
        parcel.writeParcelable(authProvider, i);
    }
}
