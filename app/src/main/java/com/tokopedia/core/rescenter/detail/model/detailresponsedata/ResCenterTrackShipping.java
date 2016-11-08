package com.tokopedia.core.rescenter.detail.model.detailresponsedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/3/16.
 */
public class ResCenterTrackShipping {

    @SerializedName("track_shipping")
    @Expose
    private TrackShipping trackShipping;

    public TrackShipping getTrackShipping() {
        return trackShipping;
    }

    public void setTrackShipping(TrackShipping trackShipping) {
        this.trackShipping = trackShipping;
    }

    public class TrackShipping {
        @SerializedName("delivered")
        @Expose
        private Integer delivered;
        @SerializedName("shipping_ref_num")
        @Expose
        private String shippingRefNum;
        @SerializedName("receiver_name")
        @Expose
        private String receiverName;
        @SerializedName("track_history")
        @Expose
        private List<TrackHistory> trackHistory;

        public Integer getDelivered() {
            return delivered;
        }

        public void setDelivered(Integer delivered) {
            this.delivered = delivered;
        }

        public String getShippingRefNum() {
            return shippingRefNum;
        }

        public void setShippingRefNum(String shippingRefNum) {
            this.shippingRefNum = shippingRefNum;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public List<TrackHistory> getTrackHistory() {
            return trackHistory;
        }

        public void setTrackHistory(List<TrackHistory> trackHistory) {
            this.trackHistory = trackHistory;
        }
    }

    public class TrackHistory {
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date")
        @Expose
        private String date;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
