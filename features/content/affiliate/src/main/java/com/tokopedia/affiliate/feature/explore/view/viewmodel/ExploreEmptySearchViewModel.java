package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory;

/**
 * @author by yfsx on 12/10/18.
 */
public class ExploreEmptySearchViewModel implements Visitable<ExploreTypeFactory>, Parcelable {

    public ExploreEmptySearchViewModel() {
    }

    @Override
    public int type(ExploreTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected ExploreEmptySearchViewModel(Parcel in) {
    }

    public static final Parcelable.Creator<ExploreEmptySearchViewModel> CREATOR = new Parcelable.Creator<ExploreEmptySearchViewModel>() {
        @Override
        public ExploreEmptySearchViewModel createFromParcel(Parcel source) {
            return new ExploreEmptySearchViewModel(source);
        }

        @Override
        public ExploreEmptySearchViewModel[] newArray(int size) {
            return new ExploreEmptySearchViewModel[size];
        }
    };
}
