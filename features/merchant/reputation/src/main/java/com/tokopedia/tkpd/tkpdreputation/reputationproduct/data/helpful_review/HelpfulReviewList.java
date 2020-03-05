package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by stevenfredian on 3/1/16.
 */
public class HelpfulReviewList implements Parcelable{

    @SerializedName("list")
    @Expose
    private List<HelpfulReview> helpfulReviews = new ArrayList<>();

    protected HelpfulReviewList(Parcel in) {
        helpfulReviews = new ArrayList<>();
        in.readTypedList(helpfulReviews, HelpfulReview.CREATOR);
    }

    public static final Creator<HelpfulReviewList> CREATOR = new Creator<HelpfulReviewList>() {
        @Override
        public HelpfulReviewList createFromParcel(Parcel in) {
            return new HelpfulReviewList(in);
        }

        @Override
        public HelpfulReviewList[] newArray(int size) {
            return new HelpfulReviewList[size];
        }
    };

    /**
     *
     * @return
     *     The list
     */
    public List<HelpfulReview> getList() {
        return helpfulReviews;
    }

    /**
     *
     * @param list
     *     The list
     */
    public void setList(List<HelpfulReview> list) {
        this.helpfulReviews = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(helpfulReviews);
    }
}
