package com.tokopedia.shop.common.view;

/**
 * Created by nathan on 5/4/17.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

public class ShopPageSavedState extends View.BaseSavedState {

    private SparseArray childrenStates;

    public SparseArray getChildrenStates() {
        return childrenStates;
    }

    public ShopPageSavedState(Parcelable superState) {
        super(superState);
    }

    private ShopPageSavedState(Parcel in, ClassLoader classLoader) {
        super(in);
        childrenStates = in.readSparseArray(classLoader);
    }

    public void initChildrenStates() {
        childrenStates = new SparseArray();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeSparseArray(childrenStates);
    }

    public static final ClassLoaderCreator<ShopPageSavedState> CREATOR = new ClassLoaderCreator<ShopPageSavedState>() {
        @Override
        public ShopPageSavedState createFromParcel(Parcel source, ClassLoader loader) {
            return new ShopPageSavedState(source, loader);
        }

        @Override
        public ShopPageSavedState createFromParcel(Parcel source) {
            return createFromParcel(null);
        }

        public ShopPageSavedState[] newArray(int size) {
            return new ShopPageSavedState[size];
        }
    };
}