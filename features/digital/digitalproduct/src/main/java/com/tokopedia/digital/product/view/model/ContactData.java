package com.tokopedia.digital.product.view.model;

/**
 * @author anggaprasetiyo on 5/9/17.
 */
public class ContactData {

    private String givenName;
    private String contactNumber;
    private int contactType;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }
}
