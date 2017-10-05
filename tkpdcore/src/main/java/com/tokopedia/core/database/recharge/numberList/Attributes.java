package com.tokopedia.core.database.recharge.numberList;

/**
 * @author rizkyfadillah on 9/28/2017.
 */

public class Attributes {
    private String client_number;
    private String label;
    private String last_updated;
    private String last_product;

    public String getClient_number() {
        return client_number;
    }

    public void setClient_number(String client_number) {
        this.client_number = client_number;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getLast_product() {
        return last_product;
    }

    public void setLast_product(String last_product) {
        this.last_product = last_product;
    }
}
