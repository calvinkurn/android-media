package com.tokopedia.tokocash.autosweepmf.view.model;

public class AutoSweepDetail extends BaseModel {
    private int accountStatus;
    private double balance;
    private int autoSweepStatus;
    private long amountLimit;
    private String title;
    private String content;
    private String tooltipContent;
    private boolean isEnable;
    private String dashboardLink;
    private String dialogContent;
    private String dialogTitle;
    private String dialogLabelPositive;
    private String dialogLabelNegative;
    private String description;
    private String dialogNegativeButtonLink;
    private String mfInfoLink;

    public String getMfInfoLink() {
        return mfInfoLink;
    }

    public void setMfInfoLink(String mfInfoLink) {
        this.mfInfoLink = mfInfoLink;
    }

    public String getDialogNegativeButtonLink() {
        return dialogNegativeButtonLink;
    }

    public void setDialogNegativeButtonLink(String dialogNegativeButtonLink) {
        this.dialogNegativeButtonLink = dialogNegativeButtonLink;
    }

    public String getDialogContent() {
        return dialogContent;
    }

    public void setDialogContent(String dialogContent) {
        this.dialogContent = dialogContent;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogLabelPositive() {
        return dialogLabelPositive;
    }

    public void setDialogLabelPositive(String dialogLabelPositive) {
        this.dialogLabelPositive = dialogLabelPositive;
    }

    public String getDialogLabelNegative() {
        return dialogLabelNegative;
    }

    public void setDialogLabelNegative(String dialogLabelNegative) {
        this.dialogLabelNegative = dialogLabelNegative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDashboardLink() {
        return dashboardLink;
    }

    public void setDashboardLink(String dashboardLink) {
        this.dashboardLink = dashboardLink;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getTooltipContent() {
        return tooltipContent;
    }

    public void setTooltipContent(String tooltipContent) {
        this.tooltipContent = tooltipContent;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAutoSweepStatus() {
        return autoSweepStatus;
    }

    public void setAutoSweepStatus(int autoSweepStatus) {
        this.autoSweepStatus = autoSweepStatus;
    }

    public long getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(long amountLimit) {
        this.amountLimit = amountLimit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AutoSweepDetail{" +
                "accountStatus=" + accountStatus +
                ", balance=" + balance +
                ", autoSweepStatus=" + autoSweepStatus +
                ", amountLimit=" + amountLimit +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tooltipContent='" + tooltipContent + '\'' +
                ", isEnable=" + isEnable +
                ", dashboardLink='" + dashboardLink + '\'' +
                ", dialogContent='" + dialogContent + '\'' +
                ", dialogTitle='" + dialogTitle + '\'' +
                ", dialogLabelPositive='" + dialogLabelPositive + '\'' +
                ", dialogLabelNegative='" + dialogLabelNegative + '\'' +
                ", description='" + description + '\'' +
                ", dialogNegativeButtonLink='" + dialogNegativeButtonLink + '\'' +
                ", mfInfoLink='" + mfInfoLink + '\'' +
                '}';
    }
}
