package com.tokopedia.digital.widget.model.recentnumber;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class Relationship implements Parcelable {

    private Category category;

    protected Relationship(Parcel in) {
        category = in.readParcelable(Category.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(category, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Relationship> CREATOR = new Creator<Relationship>() {
        @Override
        public Relationship createFromParcel(Parcel in) {
            return new Relationship(in);
        }

        @Override
        public Relationship[] newArray(int size) {
            return new Relationship[size];
        }
    };

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
