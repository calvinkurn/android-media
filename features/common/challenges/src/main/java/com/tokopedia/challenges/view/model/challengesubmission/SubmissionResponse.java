package com.tokopedia.challenges.view.model.challengesubmission;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.challenges.view.model.Channel;
import com.tokopedia.challenges.view.model.Collection;

import java.util.List;

public class SubmissionResponse implements Parcelable {
    @SerializedName("Start")
    private int start;
    @SerializedName("Size")
    private int size;
    @SerializedName("Count")
    private int count;
    @SerializedName("Found")
    private int found;
    @SerializedName("Results")
    @Expose
    private List<SubmissionResult> submissionResults;
    @SerializedName("Channel")
    @Expose
    private Channel channel;
    @SerializedName("Collection")
    @Expose
    private Collection collection;

    protected SubmissionResponse(Parcel in) {
        start = in.readInt();
        size = in.readInt();
        count = in.readInt();
        found = in.readInt();
        submissionResults = in.createTypedArrayList(SubmissionResult.CREATOR);
        channel = in.readParcelable(Channel.class.getClassLoader());
        collection = in.readParcelable(Collection.class.getClassLoader());
    }

    public static final Creator<SubmissionResponse> CREATOR = new Creator<SubmissionResponse>() {
        @Override
        public SubmissionResponse createFromParcel(Parcel in) {
            return new SubmissionResponse(in);
        }

        @Override
        public SubmissionResponse[] newArray(int size) {
            return new SubmissionResponse[size];
        }
    };

    public List<SubmissionResult> getSubmissionResults() {
        return submissionResults;
    }

    public void setSubmissionResults(List<SubmissionResult> submissionResults) {
        this.submissionResults = submissionResults;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(start);
        parcel.writeInt(size);
        parcel.writeInt(count);
        parcel.writeInt(found);
        parcel.writeTypedList(submissionResults);
        parcel.writeParcelable(channel, i);
        parcel.writeParcelable(collection, i);
    }
}
