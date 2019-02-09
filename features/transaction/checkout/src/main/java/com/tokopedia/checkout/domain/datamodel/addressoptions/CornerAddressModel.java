package com.tokopedia.checkout.domain.datamodel.addressoptions;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerAddressModel {

    private int cornerId;
    private String cornerName;
    private String cornerBranchName;
    private String cornerBranchDesc;
    private boolean isSelected;

    public CornerAddressModel() {
    }

    public int getCornerId() {
        return cornerId;
    }

    public void setCornerId(int cornerId) {
        this.cornerId = cornerId;
    }

    public String getCornerName() {
        return cornerName;
    }

    public void setCornerName(String cornerName) {
        this.cornerName = cornerName;
    }

    public String getCornerBranchName() {
        return cornerBranchName;
    }

    public void setCornerBranchName(String cornerBranchName) {
        this.cornerBranchName = cornerBranchName;
    }

    public String getCornerBranchDesc() {
        return cornerBranchDesc;
    }

    public void setCornerBranchDesc(String cornerBranchDesc) {
        this.cornerBranchDesc = cornerBranchDesc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
