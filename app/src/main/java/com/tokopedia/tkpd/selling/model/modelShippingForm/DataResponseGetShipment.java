
package com.tokopedia.tkpd.selling.model.modelShippingForm;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponseGetShipment {

    @SerializedName("shipment")
    @Expose
    private List<Shipment> shipment = new ArrayList<Shipment>();

    /**
     * 
     * @return
     *     The shipment
     */
    public List<Shipment> getShipment() {
        return shipment;
    }

    /**
     * 
     * @param shipment
     *     The shipment
     */
    public void setShipment(List<Shipment> shipment) {
        this.shipment = shipment;
    }

}
