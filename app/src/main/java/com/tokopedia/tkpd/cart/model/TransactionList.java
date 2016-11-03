
package com.tokopedia.tkpd.cart.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionList {

    @SerializedName("cart_total_logistic_fee_idr")
    @Expose
    private String cartTotalLogisticFeeIdr;
    @SerializedName("cart_shipping_rate_idr")
    @Expose
    private String cartShippingRateIdr;
    @SerializedName("cart_total_logistic_fee")
    @Expose
    private String cartTotalLogisticFee;
    @SerializedName("cart_cannot_insurance")
    @Expose
    private Integer cartCannotInsurance;
    @SerializedName("cart_insurance_price")
    @Expose
    private String cartInsurancePrice;
    @SerializedName("cart_total_product")
    @Expose
    private String cartTotalProduct;
    @SerializedName("cart_is_price_changed")
    @Expose
    private String cartIsPriceChanged;
    @SerializedName("cart_insurance_price_idr")
    @Expose
    private String cartInsurancePriceIdr;
    @SerializedName("cart_is_exception_error_delete")
    @Expose
    private String cartIsExceptionErrorDelete;
    @SerializedName("cart_partial")
    @Expose
    private String cartPartial;
    @SerializedName("cart_destination")
    @Expose
    private CartDestination cartDestination;
    @SerializedName("cart_product_type")
    @Expose
    private Integer cartProductType;
    @SerializedName("cart_is_allow_checkout")
    @Expose
    private Integer cartIsAllowCheckout;
    @SerializedName("cart_can_process")
    @Expose
    private Integer cartCanProcess;
    @SerializedName("cart_total_product_price_idr")
    @Expose
    private String cartTotalProductPriceIdr;
    @SerializedName("cart_total_product_active")
    @Expose
    private String cartTotalProductActive;
    @SerializedName("cart_error_message_2")
    @Expose
    private String cartErrorMessage2;
    @SerializedName("cart_insurance_prod")
    @Expose
    private Integer cartInsuranceProd;
    @SerializedName("cart_error_message_1")
    @Expose
    private String cartErrorMessage1;
    @SerializedName("cart_shop")
    @Expose
    private CartShop cartShop;
    @SerializedName("cart_total_amount_idr")
    @Expose
    private String cartTotalAmountIdr;
    @SerializedName("cart_logistic_fee")
    @Expose
    private String cartLogisticFee;
    @SerializedName("cart_total_error")
    @Expose
    private String cartTotalError;
    @SerializedName("cart_total_product_price")
    @Expose
    private String cartTotalProductPrice;
    @SerializedName("cart_products")
    @Expose
    private List<CartProduct> cartProducts = new ArrayList<CartProduct>();
    @SerializedName("cart_customer_id")
    @Expose
    private Integer cartCustomerId;
    @SerializedName("cart_total_amount")
    @Expose
    private String cartTotalAmount;
    @SerializedName("cart_force_insurance")
    @Expose
    private Integer cartForceInsurance;
    @SerializedName("cart_total_weight")
    @Expose
    private String cartTotalWeight;
    @SerializedName("cart_shipping_rate")
    @Expose
    private String cartShippingRate;
    @SerializedName("cart_shipments")
    @Expose
    private CartShipments cartShipments;
    @SerializedName("cart_total_cart_count")
    @Expose
    private String cartTotalCartCount;
    @SerializedName("cart_string")
    @Expose
    private String cartString;

    public String getCartTotalLogisticFeeIdr() {
        return cartTotalLogisticFeeIdr;
    }

    public void setCartTotalLogisticFeeIdr(String cartTotalLogisticFeeIdr) {
        this.cartTotalLogisticFeeIdr = cartTotalLogisticFeeIdr;
    }

    public String getCartShippingRateIdr() {
        return cartShippingRateIdr;
    }

    public void setCartShippingRateIdr(String cartShippingRateIdr) {
        this.cartShippingRateIdr = cartShippingRateIdr;
    }

    public String getCartTotalLogisticFee() {
        return cartTotalLogisticFee;
    }

    public void setCartTotalLogisticFee(String cartTotalLogisticFee) {
        this.cartTotalLogisticFee = cartTotalLogisticFee;
    }

    public Integer getCartCannotInsurance() {
        return cartCannotInsurance;
    }

    public void setCartCannotInsurance(Integer cartCannotInsurance) {
        this.cartCannotInsurance = cartCannotInsurance;
    }

    public String getCartInsurancePrice() {
        return cartInsurancePrice;
    }

    public void setCartInsurancePrice(String cartInsurancePrice) {
        this.cartInsurancePrice = cartInsurancePrice;
    }

    public String getCartTotalProduct() {
        return cartTotalProduct;
    }

    public void setCartTotalProduct(String cartTotalProduct) {
        this.cartTotalProduct = cartTotalProduct;
    }

    public String getCartIsPriceChanged() {
        return cartIsPriceChanged;
    }

    public void setCartIsPriceChanged(String cartIsPriceChanged) {
        this.cartIsPriceChanged = cartIsPriceChanged;
    }

    public String getCartInsurancePriceIdr() {
        return cartInsurancePriceIdr;
    }

    public void setCartInsurancePriceIdr(String cartInsurancePriceIdr) {
        this.cartInsurancePriceIdr = cartInsurancePriceIdr;
    }

    public String getCartIsExceptionErrorDelete() {
        return cartIsExceptionErrorDelete;
    }

    public void setCartIsExceptionErrorDelete(String cartIsExceptionErrorDelete) {
        this.cartIsExceptionErrorDelete = cartIsExceptionErrorDelete;
    }

    public String getCartPartial() {
        return cartPartial;
    }

    public void setCartPartial(String cartPartial) {
        this.cartPartial = cartPartial;
    }

    public CartDestination getCartDestination() {
        return cartDestination;
    }

    public void setCartDestination(CartDestination cartDestination) {
        this.cartDestination = cartDestination;
    }

    public Integer getCartProductType() {
        return cartProductType;
    }

    public void setCartProductType(Integer cartProductType) {
        this.cartProductType = cartProductType;
    }

    public Integer getCartIsAllowCheckout() {
        return cartIsAllowCheckout;
    }

    public void setCartIsAllowCheckout(Integer cartIsAllowCheckout) {
        this.cartIsAllowCheckout = cartIsAllowCheckout;
    }

    public Integer getCartCanProcess() {
        return cartCanProcess;
    }

    public void setCartCanProcess(Integer cartCanProcess) {
        this.cartCanProcess = cartCanProcess;
    }

    public String getCartTotalProductPriceIdr() {
        return cartTotalProductPriceIdr;
    }

    public void setCartTotalProductPriceIdr(String cartTotalProductPriceIdr) {
        this.cartTotalProductPriceIdr = cartTotalProductPriceIdr;
    }

    public String getCartTotalProductActive() {
        return cartTotalProductActive;
    }

    public void setCartTotalProductActive(String cartTotalProductActive) {
        this.cartTotalProductActive = cartTotalProductActive;
    }

    public String getCartErrorMessage2() {
        return cartErrorMessage2;
    }

    public void setCartErrorMessage2(String cartErrorMessage2) {
        this.cartErrorMessage2 = cartErrorMessage2;
    }

    public Integer getCartInsuranceProd() {
        return cartInsuranceProd;
    }

    public void setCartInsuranceProd(Integer cartInsuranceProd) {
        this.cartInsuranceProd = cartInsuranceProd;
    }

    public String getCartErrorMessage1() {
        return cartErrorMessage1;
    }

    public void setCartErrorMessage1(String cartErrorMessage1) {
        this.cartErrorMessage1 = cartErrorMessage1;
    }

    public CartShop getCartShop() {
        return cartShop;
    }

    public void setCartShop(CartShop cartShop) {
        this.cartShop = cartShop;
    }

    public String getCartTotalAmountIdr() {
        return cartTotalAmountIdr;
    }

    public void setCartTotalAmountIdr(String cartTotalAmountIdr) {
        this.cartTotalAmountIdr = cartTotalAmountIdr;
    }

    public String getCartLogisticFee() {
        return cartLogisticFee;
    }

    public void setCartLogisticFee(String cartLogisticFee) {
        this.cartLogisticFee = cartLogisticFee;
    }

    public String getCartTotalError() {
        return cartTotalError;
    }

    public void setCartTotalError(String cartTotalError) {
        this.cartTotalError = cartTotalError;
    }

    public String getCartTotalProductPrice() {
        return cartTotalProductPrice;
    }

    public void setCartTotalProductPrice(String cartTotalProductPrice) {
        this.cartTotalProductPrice = cartTotalProductPrice;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public Integer getCartCustomerId() {
        return cartCustomerId;
    }

    public void setCartCustomerId(Integer cartCustomerId) {
        this.cartCustomerId = cartCustomerId;
    }

    public String getCartTotalAmount() {
        return cartTotalAmount;
    }

    public void setCartTotalAmount(String cartTotalAmount) {
        this.cartTotalAmount = cartTotalAmount;
    }

    public Integer getCartForceInsurance() {
        return cartForceInsurance;
    }

    public void setCartForceInsurance(Integer cartForceInsurance) {
        this.cartForceInsurance = cartForceInsurance;
    }

    public String getCartTotalWeight() {
        return cartTotalWeight;
    }

    public void setCartTotalWeight(String cartTotalWeight) {
        this.cartTotalWeight = cartTotalWeight;
    }

    public String getCartShippingRate() {
        return cartShippingRate;
    }

    public void setCartShippingRate(String cartShippingRate) {
        this.cartShippingRate = cartShippingRate;
    }

    public CartShipments getCartShipments() {
        return cartShipments;
    }

    public void setCartShipments(CartShipments cartShipments) {
        this.cartShipments = cartShipments;
    }

    public String getCartTotalCartCount() {
        return cartTotalCartCount;
    }

    public void setCartTotalCartCount(String cartTotalCartCount) {
        this.cartTotalCartCount = cartTotalCartCount;
    }

    public String getCartString() {
        return cartString;
    }

    public void setCartString(String cartString) {
        this.cartString = cartString;
    }
}
