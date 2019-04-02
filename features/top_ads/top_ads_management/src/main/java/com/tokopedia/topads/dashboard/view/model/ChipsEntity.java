package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ChipsEntity<E extends GenericClass> implements Parcelable {

    public static final Parcelable.Creator<ChipsEntity> CREATOR = new Parcelable.Creator<ChipsEntity>() {
        @Override
        public ChipsEntity createFromParcel(Parcel source) {
            return new ChipsEntity(source);
        }

        @Override
        public ChipsEntity[] newArray(int size) {
            return new ChipsEntity[size];
        }
    };
    @DrawableRes
    private Integer drawableResId;
    @Nullable
    private String description;
    @NonNull
    private String name;
    @Nullable
    private E rawData;
    private String className;
    private int position;

    private ChipsEntity(Builder<E> builder) {
        drawableResId = builder.drawableResId;
        description = builder.description;
        name = builder.name;
        rawData = builder.rawData;
        className = builder.className;
        position = builder.position;
    }

    protected ChipsEntity(Parcel in) {
        this.drawableResId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.name = in.readString();
        this.className = in.readString();
        ClassLoader classLoader = null;
        try {
            classLoader = Class.forName(this.className).getClassLoader();
        } catch (ClassNotFoundException cfe) {
            cfe.printStackTrace();
        }
        this.rawData = in.readParcelable(classLoader);
        this.position = in.readInt();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Integer getDrawableResId() {
        return drawableResId;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public E getRawData() {
        return rawData;
    }

    public void setRawData(@Nullable E rawData) {
        this.rawData = rawData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.drawableResId);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeString(this.className);
        dest.writeParcelable(this.rawData, flags);
        dest.writeInt(this.position);
    }

    public static final class Builder<T extends GenericClass> {
        private int drawableResId;
        private String description;
        private String name;
        private T rawData;
        private String className;
        private int position;

        private Builder() {
        }

        @NonNull
        public Builder drawableResId(int drawableResId) {
            this.drawableResId = drawableResId;
            return this;
        }

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        @NonNull
        public Builder description(@Nullable String description) {
            this.description = description;
            return this;
        }

        public Builder rawData(@Nullable T rawData) {
            this.rawData = rawData;
            this.className = rawData.getClassName();
            return this;
        }

        public Builder position(int position){
            this.position = position;
            return this;
        }

        @NonNull
        public ChipsEntity build() {
            return new ChipsEntity(this);
        }
    }
}
