//package com.tokopedia.saldodetails.response.model;
//
//import android.os.Parcel;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.tokopedia.abstraction.common.utils.view.MethodChecker;
//import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
//import com.tokopedia.saldodetails.viewmodel.ParcelableViewModel;
//
//public class Deposit implements ParcelableViewModel<SaldoDetailTransactionFactory> {
//
//    @SerializedName("deposit_id")
//    @Expose
//    private String depositId;
//    @SerializedName("deposit_saldo_idr")
//    @Expose
//    private String depositSaldoIdr;
//    @SerializedName("deposit_date_full")
//    @Expose
//    private String depositDateFull;
//    @SerializedName("deposit_amount")
//    @Expose
//    private String depositAmount;
//    @SerializedName("deposit_amount_idr")
//    @Expose
//    private String depositAmountIdr;
//    @SerializedName("deposit_type")
//    @Expose
//    private int depositType;
//    @SerializedName("deposit_date")
//    @Expose
//    private String depositDate;
//    @SerializedName("deposit_withdraw_date")
//    @Expose
//    private String depositWithdrawDate;
//    @SerializedName("deposit_withdraw_status")
//    @Expose
//    private String depositWithdrawStatus;
//    @SerializedName("deposit_notes")
//    @Expose
//    private String depositNotes;
//
//    protected Deposit(Parcel in) {
//        depositId = in.readString();
//        depositSaldoIdr = in.readString();
//        depositDateFull = in.readString();
//        depositAmount = in.readString();
//        depositAmountIdr = in.readString();
//        depositType = in.readInt();
//        depositDate = in.readString();
//        depositWithdrawDate = in.readString();
//        depositWithdrawStatus = in.readString();
//        depositNotes = in.readString();
//    }
//
//    public static final Creator<Deposit> CREATOR = new Creator<Deposit>() {
//        @Override
//        public Deposit createFromParcel(Parcel in) {
//            return new Deposit(in);
//        }
//
//        @Override
//        public Deposit[] newArray(int size) {
//            return new Deposit[size];
//        }
//    };
//
//    /**
//     * @return The depositId
//     */
//    public String getDepositId() {
//        return depositId;
//    }
//
//    /**
//     * @param depositId The deposit_id
//     */
//    public void setDepositId(String depositId) {
//        this.depositId = depositId;
//    }
//
//    /**
//     * @return The depositSaldoIdr
//     */
//    public String getDepositSaldoIdr() {
//        return depositSaldoIdr;
//    }
//
//    /**
//     * @param depositSaldoIdr The deposit_saldo_idr
//     */
//    public void setDepositSaldoIdr(String depositSaldoIdr) {
//        this.depositSaldoIdr = depositSaldoIdr;
//    }
//
//    /**
//     * @return The depositDateFull
//     */
//    public String getDepositDateFull() {
//        return depositDateFull;
//    }
//
//    /**
//     * @param depositDateFull The deposit_date_full
//     */
//    public void setDepositDateFull(String depositDateFull) {
//        this.depositDateFull = depositDateFull;
//    }
//
//    /**
//     * @return The depositAmount
//     */
//    public String getDepositAmount() {
//        return depositAmount;
//    }
//
//    /**
//     * @param depositAmount The deposit_amount
//     */
//    public void setDepositAmount(String depositAmount) {
//        this.depositAmount = depositAmount;
//    }
//
//    /**
//     * @return The depositAmountIdr
//     */
//    public String getDepositAmountIdr() {
//        return depositAmountIdr;
//    }
//
//    /**
//     * @param depositAmountIdr The deposit_amount_idr
//     */
//    public void setDepositAmountIdr(String depositAmountIdr) {
//        this.depositAmountIdr = depositAmountIdr;
//    }
//
//    /**
//     * @return The depositType
//     */
//    public int getDepositType() {
//        return depositType;
//    }
//
//    /**
//     * @param depositType The deposit_type
//     */
//    public void setDepositType(int depositType) {
//        this.depositType = depositType;
//    }
//
//    /**
//     * @return The depositDate
//     */
//    public String getDepositDate() {
//        return depositDate;
//    }
//
//    /**
//     * @param depositDate The deposit_date
//     */
//    public void setDepositDate(String depositDate) {
//        this.depositDate = depositDate;
//    }
//
//    /**
//     * @return The depositWithdrawDate
//     */
//    public String getDepositWithdrawDate() {
//        return depositWithdrawDate;
//    }
//
//    /**
//     * @param depositWithdrawDate The deposit_withdraw_date
//     */
//    public void setDepositWithdrawDate(String depositWithdrawDate) {
//        this.depositWithdrawDate = depositWithdrawDate;
//    }
//
//    /**
//     * @return The depositWithdrawStatus
//     */
//    public String getDepositWithdrawStatus() {
//        return depositWithdrawStatus;
//    }
//
//    /**
//     * @param depositWithdrawStatus The deposit_withdraw_status
//     */
//    public void setDepositWithdrawStatus(String depositWithdrawStatus) {
//        this.depositWithdrawStatus = depositWithdrawStatus;
//    }
//
//    /**
//     * @return The depositNotes
//     */
//    public String getDepositNotes() {
//        return MethodChecker.fromHtml(depositNotes).toString();
//    }
//
//    /**
//     * @param depositNotes The deposit_notes
//     */
//    public void setDepositNotes(String depositNotes) {
//        this.depositNotes = depositNotes;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(depositId);
//        parcel.writeString(depositSaldoIdr);
//        parcel.writeString(depositDateFull);
//        parcel.writeString(depositAmount);
//        parcel.writeString(depositAmountIdr);
//        parcel.writeInt(depositType);
//        parcel.writeString(depositDate);
//        parcel.writeString(depositWithdrawDate);
//        parcel.writeString(depositWithdrawStatus);
//        parcel.writeString(depositNotes);
//    }
//
//    @Override
//    public int type(SaldoDetailTransactionFactory typeFactory) {
//        return 0;//typeFactory.type(this);
//    }
//}
//
