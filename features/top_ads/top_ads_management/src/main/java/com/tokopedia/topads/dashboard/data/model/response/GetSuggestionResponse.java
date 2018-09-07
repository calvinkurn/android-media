package com.tokopedia.topads.dashboard.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 10/25/17.
 */

public class GetSuggestionResponse  {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("data_type")
    @Expose
    private String dataType;
    @SerializedName("suggestion_type")
    @Expose
    private String suggestionType;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSuggestionType() {
        return suggestionType;
    }

    public void setSuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }

    public static class Datum implements Parcelable {

        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("average")
        @Expose
        private long average;
        @SerializedName("average_fmt")
        @Expose
        private String averageFmt;
        @SerializedName("median")
        @Expose
        private long median;
        @SerializedName("median_fmt")
        @Expose
        private String medianFmt;
        @SerializedName("minimum")
        @Expose
        private long minimum;
        @SerializedName("minimum_fmt")
        @Expose
        private String minimumFmt;
        @SerializedName("maximum")
        @Expose
        private long maximum;
        @SerializedName("maximum_fmt")
        @Expose
        private String maximumFmt;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getAverage() {
            return average;
        }

        public void setAverage(long average) {
            this.average = average;
        }

        public String getAverageFmt() {
            return averageFmt;
        }

        public void setAverageFmt(String averageFmt) {
            this.averageFmt = averageFmt;
        }

        public long getMedian() {
            return median;
        }

        public void setMedian(long median) {
            this.median = median;
        }

        public String getMedianFmt() {
            return medianFmt;
        }

        public void setMedianFmt(String medianFmt) {
            this.medianFmt = medianFmt;
        }

        public long getMinimum() {
            return minimum;
        }

        public void setMinimum(long minimum) {
            this.minimum = minimum;
        }

        public String getMinimumFmt() {
            return minimumFmt;
        }

        public void setMinimumFmt(String minimumFmt) {
            this.minimumFmt = minimumFmt;
        }

        public long getMaximum() {
            return maximum;
        }

        public void setMaximum(long maximum) {
            this.maximum = maximum;
        }

        public String getMaximumFmt() {
            return maximumFmt;
        }

        public void setMaximumFmt(String maximumFmt) {
            this.maximumFmt = maximumFmt;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeString(this.name);
            dest.writeLong(this.average);
            dest.writeString(this.averageFmt);
            dest.writeLong(this.median);
            dest.writeString(this.medianFmt);
            dest.writeLong(this.minimum);
            dest.writeString(this.minimumFmt);
            dest.writeLong(this.maximum);
            dest.writeString(this.maximumFmt);
        }

        public Datum() {
        }

        protected Datum(Parcel in) {
            this.id = in.readLong();
            this.name = in.readString();
            this.average = in.readLong();
            this.averageFmt = in.readString();
            this.median = in.readLong();
            this.medianFmt = in.readString();
            this.minimum = in.readLong();
            this.minimumFmt = in.readString();
            this.maximum = in.readLong();
            this.maximumFmt = in.readString();
        }

        public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
            @Override
            public Datum createFromParcel(Parcel source) {
                return new Datum(source);
            }

            @Override
            public Datum[] newArray(int size) {
                return new Datum[size];
            }
        };
    }
}
