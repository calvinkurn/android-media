package com.tokopedia.core.people.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 8/12/16.
 */
public class PeopleFavShop implements Parcelable {

    @SerializedName("data_random_fav_shop")
    private DataRandomFavShop dataRandomFavShop;

    public DataRandomFavShop getDataRandomFavShop() {
        return dataRandomFavShop;
    }

    public void setDataRandomFavShop(DataRandomFavShop dataRandomFavShop) {
        this.dataRandomFavShop = dataRandomFavShop;
    }

    public static class DataRandomFavShop implements Parcelable {
        @SerializedName("fave_uri")
        private String faveUri;
        @SerializedName("total_fave_f")
        private String totalFaveFmt;
        @SerializedName("total_fave")
        private int totalFave;
        @SerializedName("rand_fave")
        private List<RandFave> randFave;

        public String getFaveUri() {
            return faveUri;
        }

        public void setFaveUri(String faveUri) {
            this.faveUri = faveUri;
        }

        public String getTotalFaveFmt() {
            return totalFaveFmt;
        }

        public void setTotalFaveFmt(String totalFaveFmt) {
            this.totalFaveFmt = totalFaveFmt;
        }

        public int getTotalFave() {
            return totalFave;
        }

        public void setTotalFave(int totalFave) {
            this.totalFave = totalFave;
        }

        public List<RandFave> getRandFave() {
            return randFave;
        }

        public void setRandFave(List<RandFave> randFave) {
            this.randFave = randFave;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.faveUri);
            dest.writeString(this.totalFaveFmt);
            dest.writeInt(this.totalFave);
            dest.writeTypedList(this.randFave);
        }

        public DataRandomFavShop() {
        }

        protected DataRandomFavShop(Parcel in) {
            this.faveUri = in.readString();
            this.totalFaveFmt = in.readString();
            this.totalFave = in.readInt();
            this.randFave = in.createTypedArrayList(RandFave.CREATOR);
        }

        public static final Creator<DataRandomFavShop> CREATOR = new Creator<DataRandomFavShop>() {
            @Override
            public DataRandomFavShop createFromParcel(Parcel source) {
                return new DataRandomFavShop(source);
            }

            @Override
            public DataRandomFavShop[] newArray(int size) {
                return new DataRandomFavShop[size];
            }
        };
    }

    public static class RandFave implements Parcelable {
        @SerializedName("fs_reputation")
        private FsReputation fsReputation;
        @SerializedName("fs_name")
        private String fsName;
        @SerializedName("fs_img")
        private String fsImg;
        @SerializedName("fs_id")
        private String fsId;
        @SerializedName("fs_uri")
        private String fsUri;

        public FsReputation getFsReputation() {
            return fsReputation;
        }

        public void setFsReputation(FsReputation fsReputation) {
            this.fsReputation = fsReputation;
        }

        public String getFsName() {
            return fsName;
        }

        public void setFsName(String fsName) {
            this.fsName = fsName;
        }

        public String getFsImg() {
            return fsImg;
        }

        public void setFsImg(String fsImg) {
            this.fsImg = fsImg;
        }

        public String getFsId() {
            return fsId;
        }

        public void setFsId(String fsId) {
            this.fsId = fsId;
        }

        public String getFsUri() {
            return fsUri;
        }

        public void setFsUri(String fsUri) {
            this.fsUri = fsUri;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.fsReputation, flags);
            dest.writeString(this.fsName);
            dest.writeString(this.fsImg);
            dest.writeString(this.fsId);
            dest.writeString(this.fsUri);
        }

        public RandFave() {
        }

        protected RandFave(Parcel in) {
            this.fsReputation = in.readParcelable(FsReputation.class.getClassLoader());
            this.fsName = in.readString();
            this.fsImg = in.readString();
            this.fsId = in.readString();
            this.fsUri = in.readString();
        }

        public static final Creator<RandFave> CREATOR = new Creator<RandFave>() {
            @Override
            public RandFave createFromParcel(Parcel source) {
                return new RandFave(source);
            }

            @Override
            public RandFave[] newArray(int size) {
                return new RandFave[size];
            }
        };
    }

    public static class FsReputation implements Parcelable {
        @SerializedName("tooltip")
        private String tooltip;
        @SerializedName("reputation_badge")
        private ReputationBadge reputationBadge;
        @SerializedName("reputation_score")
        private String reputationScore;
        @SerializedName("min_badge_score")
        private int minBadgeScore;
        @SerializedName("score")
        private int score;

        public String getTooltip() {
            return tooltip;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public ReputationBadge getReputationBadge() {
            return reputationBadge;
        }

        public void setReputationBadge(ReputationBadge reputationBadge) {
            this.reputationBadge = reputationBadge;
        }

        public String getReputationScore() {
            return reputationScore;
        }

        public void setReputationScore(String reputationScore) {
            this.reputationScore = reputationScore;
        }

        public int getMinBadgeScore() {
            return minBadgeScore;
        }

        public void setMinBadgeScore(int minBadgeScore) {
            this.minBadgeScore = minBadgeScore;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tooltip);
            dest.writeParcelable(this.reputationBadge, flags);
            dest.writeString(this.reputationScore);
            dest.writeInt(this.minBadgeScore);
            dest.writeInt(this.score);
        }

        public FsReputation() {
        }

        protected FsReputation(Parcel in) {
            this.tooltip = in.readString();
            this.reputationBadge = in.readParcelable(ReputationBadge.class.getClassLoader());
            this.reputationScore = in.readString();
            this.minBadgeScore = in.readInt();
            this.score = in.readInt();
        }

        public static final Creator<FsReputation> CREATOR = new Creator<FsReputation>() {
            @Override
            public FsReputation createFromParcel(Parcel source) {
                return new FsReputation(source);
            }

            @Override
            public FsReputation[] newArray(int size) {
                return new FsReputation[size];
            }
        };
    }

    public static class ReputationBadge implements Parcelable {
        @SerializedName("level")
        private int level;
        @SerializedName("set")
        private int set;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getSet() {
            return set;
        }

        public void setSet(int set) {
            this.set = set;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.level);
            dest.writeInt(this.set);
        }

        public ReputationBadge() {
        }

        protected ReputationBadge(Parcel in) {
            this.level = in.readInt();
            this.set = in.readInt();
        }

        public static final Creator<ReputationBadge> CREATOR = new Creator<ReputationBadge>() {
            @Override
            public ReputationBadge createFromParcel(Parcel source) {
                return new ReputationBadge(source);
            }

            @Override
            public ReputationBadge[] newArray(int size) {
                return new ReputationBadge[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.dataRandomFavShop, flags);
    }

    public PeopleFavShop() {
    }

    protected PeopleFavShop(Parcel in) {
        this.dataRandomFavShop = in.readParcelable(DataRandomFavShop.class.getClassLoader());
    }

    public static final Parcelable.Creator<PeopleFavShop> CREATOR = new Parcelable.Creator<PeopleFavShop>() {
        @Override
        public PeopleFavShop createFromParcel(Parcel source) {
            return new PeopleFavShop(source);
        }

        @Override
        public PeopleFavShop[] newArray(int size) {
            return new PeopleFavShop[size];
        }
    };
}
