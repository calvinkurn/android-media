//package com.tokopedia.saldodetails.viewmodel;
//
//import android.os.Parcel;
//
//import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
//
//public class SaldoTransactionViewModel implements ParcelableViewModel<SaldoDetailTransactionFactory> {
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//
//    }
//
//    protected SaldoTransactionViewModel(Parcel in) {
//        /*this.userId = in.readString();
//        this.name = in.readString();
//        this.imageUrl = in.readString();
//        this.tokopoint = in.readString();
//        this.coupons = in.readInt();
//        this.progress = in.readInt();*/
//    }
//
//    @Override
//    public int type(SaldoDetailTransactionFactory typeFactory) {
//        return typeFactory.type(this);
//    }
//
//    public static final Creator<SaldoTransactionViewModel> CREATOR = new Creator<SaldoTransactionViewModel>() {
//        @Override
//        public SaldoTransactionViewModel createFromParcel(Parcel source) {
//            return new SaldoTransactionViewModel(source);
//        }
//
//        @Override
//        public SaldoTransactionViewModel[] newArray(int size) {
//            return new SaldoTransactionViewModel[size];
//        }
//    };
//}
