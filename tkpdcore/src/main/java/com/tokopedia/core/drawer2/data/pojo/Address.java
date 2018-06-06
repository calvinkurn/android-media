
package com.tokopedia.core.drawer2.data.pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("addresses")
    @Expose
    private List<Address_> addresses = null;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public List<Address_> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address_> addresses) {
        this.addresses = addresses;
    }

}
