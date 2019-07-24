package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;

/**
 * @author devarafikry on 24/07/19.
 */
public class RecommendationProductViewModel implements ParcelableViewModel<AccountTypeFactory> {

    private RecommendationItem product;

    public RecommendationItem getProduct() {
        return product;
    }

    public void setProduct(RecommendationItem product) {
        this.product = product;
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.product, flags);
    }

    public RecommendationProductViewModel() {
    }

    protected RecommendationProductViewModel(Parcel in) {
        this.product = in.readParcelable(RecommendationItem.class.getClassLoader());
    }

    public static final Creator<RecommendationProductViewModel> CREATOR = new Creator<RecommendationProductViewModel>() {
        @Override
        public RecommendationProductViewModel createFromParcel(Parcel source) {
            return new RecommendationProductViewModel(source);
        }

        @Override
        public RecommendationProductViewModel[] newArray(int size) {
            return new RecommendationProductViewModel[size];
        }
    };
}
