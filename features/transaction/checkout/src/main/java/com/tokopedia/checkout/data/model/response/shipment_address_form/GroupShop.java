package com.tokopedia.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupShop {

    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("shop_shipments")
    @Expose
    private List<ShopShipment> shopShipments = new ArrayList<>();
    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<>();
    @SerializedName("shipping_id")
    @Expose
    private int shippingId;
    @SerializedName("sp_id")
    @Expose
    private int spId;
    @SerializedName("dropshipper")
    @Expose
    private Dropshiper dropshiper;
    @SerializedName("is_insurance")
    @Expose
    private boolean isInsurance;
    @SerializedName("is_fulfillment_service")
    @Expose
    private boolean isFulfillment;
    @SerializedName("toko_cabang")
    private TokoCabangInfo tokoCabangInfo;
    @SerializedName("warehouse")
    @Expose
    private Warehouse warehouse;
    @SerializedName("cart_string")
    @Expose
    private String cartString;
    @SerializedName("has_promo_list")
    @Expose
    private boolean hasPromoList;
    @SerializedName("save_state_flag")
    @Expose
    private boolean saveStateFlag;
    @SerializedName("vehicle_leasing")
    @Expose
    private VehicleLeasing vehicleLeasing;
    @SerializedName("promo_codes")
    @Expose
    private List<String> listPromoCodes;
    @SerializedName("shipment_information")
    @Expose
    private ShipmentInformation shipmentInformation;

    public List<String> getErrors() {
        return errors;
    }

    public Shop getShop() {
        return shop;
    }

    public List<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getShippingId() {
        return shippingId;
    }

    public int getSpId() {
        return spId;
    }

    public Dropshiper getDropshiper() {
        return dropshiper;
    }

    public boolean isInsurance() {
        return isInsurance;
    }

    public boolean isFulfillment() {
        return isFulfillment;
    }

    public TokoCabangInfo getTokoCabangInfo() {
        return tokoCabangInfo;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public String getCartString() { return cartString; }

    public boolean isHasPromoList() { return hasPromoList; }

    public boolean isSaveStateFlag() {
        return saveStateFlag;
    }

    public VehicleLeasing getVehicleLeasing() { return vehicleLeasing; }

    public List<String> getListPromoCodes() { return listPromoCodes; }

    public ShipmentInformation getShipmentInformation() {
        return shipmentInformation;
    }
}
