
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MostHelpfulReviewModel implements Parcelable{

    @SerializedName("helpful_reviews_total")
    @Expose
    private int helpfulReviewsTotal;
    @SerializedName("helpful_reviews")
    @Expose
    private List<HelpfulReview> helpfulReviews = new ArrayList<HelpfulReview>();

    public MostHelpfulReviewModel(){

    }

    protected MostHelpfulReviewModel(Parcel in) {
        helpfulReviewsTotal = in.readInt();
        helpfulReviews = new ArrayList<>();
        in.readTypedList(helpfulReviews,HelpfulReview.CREATOR);
    }

    public static final Creator<MostHelpfulReviewModel> CREATOR = new Creator<MostHelpfulReviewModel>() {
        @Override
        public MostHelpfulReviewModel createFromParcel(Parcel in) {
            return new MostHelpfulReviewModel(in);
        }

        @Override
        public MostHelpfulReviewModel[] newArray(int size) {
            return new MostHelpfulReviewModel[size];
        }
    };

    /**
     * 
     * @return
     *     The helpfulReviewsTotal
     */
    public int getHelpfulReviewsTotal() {
        return helpfulReviewsTotal;
    }

    /**
     * 
     * @param helpfulReviewsTotal
     *     The helpful_reviews_total
     */
    public void setHelpfulReviewsTotal(int helpfulReviewsTotal) {
        this.helpfulReviewsTotal = helpfulReviewsTotal;
    }

    /**
     * 
     * @return
     *     The helpfulReviews
     */
    public List<HelpfulReview> getHelpfulReviews() {
        return helpfulReviews;
    }

    /**
     * 
     * @param helpfulReviews
     *     The helpful_reviews
     */
    public void setHelpfulReviews(List<HelpfulReview> helpfulReviews) {
        this.helpfulReviews = helpfulReviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(helpfulReviewsTotal);
        dest.writeTypedList(helpfulReviews);
    }
}
