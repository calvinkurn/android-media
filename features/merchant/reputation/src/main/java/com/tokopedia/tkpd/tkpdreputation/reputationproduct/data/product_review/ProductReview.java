
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductReview implements Parcelable{

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("advance_review")
    @Expose
    private AdvanceReview advanceReview;
    @SerializedName("list")
    @Expose
    private java.util.List<ReviewProductModel> list = new ArrayList<>();

    protected ProductReview(Parcel in) {
        advanceReview = in.readParcelable(AdvanceReview.class.getClassLoader());
        list = in.createTypedArrayList(ReviewProductModel.CREATOR);
    }

    public static final Creator<ProductReview> CREATOR = new Creator<ProductReview>() {
        @Override
        public ProductReview createFromParcel(Parcel in) {
            return new ProductReview(in);
        }

        @Override
        public ProductReview[] newArray(int size) {
            return new ProductReview[size];
        }
    };

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
     *     The advanceReview
     */
    public AdvanceReview getAdvanceReview() {
        return advanceReview;
    }

    /**
     * 
     * @param advanceReview
     *     The advance_review
     */
    public void setAdvanceReview(AdvanceReview advanceReview) {
        this.advanceReview = advanceReview;
    }

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<ReviewProductModel> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<ReviewProductModel> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(advanceReview, flags);
        dest.writeTypedList(list);
    }
}
