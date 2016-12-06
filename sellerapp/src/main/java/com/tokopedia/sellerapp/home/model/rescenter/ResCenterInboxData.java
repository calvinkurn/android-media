package com.tokopedia.sellerapp.home.model.rescenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 4/6/16.
 */
public class ResCenterInboxData implements Parcelable {

    @SerializedName("count")
    @Expose
    private Count count;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("pending_amount")
    @Expose
    private String resCenterPendingAmount;
    @SerializedName("counter_14_days")
    @Expose
    private ResCenterCounterPending resCenterCounterPending;
    @SerializedName("list")
    @Expose
    private ArrayList<ResolutionList> list = new ArrayList<>();

    /**
     *
     * @return
     *     The count
     */
    public Count getCount() {
        return count;
    }

    /**
     *
     * @param count
     *     The count
     */
    public void setCount(Count count) {
        this.count = count;
    }

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
     *     The type
     */
    public Integer getType() {
        return type;
    }

    /**
     *
     * @param type
     *     The type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public String getResCenterPendingAmount() {
        return resCenterPendingAmount;
    }

    public void setResCenterPendingAmount(String resCenterPendingAmount) {
        this.resCenterPendingAmount = resCenterPendingAmount;
    }

    public ResCenterCounterPending getResCenterCounterPending() {
        return resCenterCounterPending;
    }

    public void setResCenterCounterPending(ResCenterCounterPending resCenterCounterPending) {
        this.resCenterCounterPending = resCenterCounterPending;
    }

    /**
     *
     * @return
     *     The list
     */
    public ArrayList<ResolutionList> getList() {
        return list;
    }

    /**
     *
     * @param list
     *     The list
     */
    public void setList(ArrayList<ResolutionList> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.count, flags);
        dest.writeParcelable(this.paging, flags);
        dest.writeValue(this.type);
        dest.writeString(this.resCenterPendingAmount);
        dest.writeParcelable(this.resCenterCounterPending, flags);
        dest.writeTypedList(this.list);
    }

    public ResCenterInboxData() {
    }

    protected ResCenterInboxData(Parcel in) {
        this.count = in.readParcelable(Count.class.getClassLoader());
        this.paging = in.readParcelable(Paging.class.getClassLoader());
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resCenterPendingAmount = in.readString();
        this.resCenterCounterPending = in.readParcelable(ResCenterCounterPending.class.getClassLoader());
        this.list = in.createTypedArrayList(ResolutionList.CREATOR);
    }

    public static final Creator<ResCenterInboxData> CREATOR = new Creator<ResCenterInboxData>() {
        @Override
        public ResCenterInboxData createFromParcel(Parcel source) {
            return new ResCenterInboxData(source);
        }

        @Override
        public ResCenterInboxData[] newArray(int size) {
            return new ResCenterInboxData[size];
        }
    };
}
