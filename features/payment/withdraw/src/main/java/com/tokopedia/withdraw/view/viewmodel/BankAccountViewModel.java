
package com.tokopedia.withdraw.view.viewmodel;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public class BankAccountViewModel {

    private int bankId;
    private int bankBranch;
    private String bankAccountName;
    private String bankAccountNumber;
    private int isVerifiedAccount;
    private String bankAccountId;
    private String bankName;
    private int isDefaultBank;
    private boolean checked;

    public BankAccountViewModel() {
        this.checked = false;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(int bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankAccountName() {
        return MethodChecker.fromHtml(bankAccountName).toString();
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Boolean isVerifiedAccount() {
        return isVerifiedAccount == 1;
    }

    public void setIsVerifiedAccount(int isVerifiedAccount) {
        this.isVerifiedAccount = isVerifiedAccount;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankName() {
        return MethodChecker.fromHtml(bankName).toString();
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getIsDefaultBank() {
        return isDefaultBank;
    }

    public void setIsDefaultBank(int isDefaultBank) {
        this.isDefaultBank = isDefaultBank;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
