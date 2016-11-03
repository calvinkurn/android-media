package com.tokopedia.tkpd.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo
 * on 30/06/2016.
 */
public class FormEdit {
    private static final String TAG = FormEdit.class.getSimpleName();

    @SerializedName("payment")
    @Expose
    private Payment payment;
    @SerializedName("bank_account")
    @Expose
    private BankAccountEdit bankAccountEdit;
    @SerializedName("order")
    @Expose
    private OrderEdit orderEdit;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("sysbank_account")
    @Expose
    private SysBankAccountEdit sysBankAccountEdit;
    @SerializedName("method")
    @Expose
    private MethodEdit methodEdit;
    @SerializedName("datetime")
    @Expose
    private Datetime datetime;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public BankAccountEdit getBankAccountEdit() {
        return bankAccountEdit;
    }

    public void setBankAccountEdit(BankAccountEdit bankAccountEdit) {
        this.bankAccountEdit = bankAccountEdit;
    }

    public OrderEdit getOrderEdit() {
        return orderEdit;
    }

    public void setOrderEdit(OrderEdit orderEdit) {
        this.orderEdit = orderEdit;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SysBankAccountEdit getSysBankAccountEdit() {
        return sysBankAccountEdit;
    }

    public void setSysBankAccountEdit(SysBankAccountEdit sysBankAccountEdit) {
        this.sysBankAccountEdit = sysBankAccountEdit;
    }

    public MethodEdit getMethodEdit() {
        return methodEdit;
    }

    public void setMethodEdit(MethodEdit methodEdit) {
        this.methodEdit = methodEdit;
    }

    public Datetime getDatetime() {
        return datetime;
    }

    public void setDatetime(Datetime datetime) {
        this.datetime = datetime;
    }
}
