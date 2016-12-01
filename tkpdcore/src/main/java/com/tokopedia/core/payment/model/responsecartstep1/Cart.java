package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Cart
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class Cart implements Parcelable {
    @SerializedName("cart_total_logistic_fee")
    @Expose
    private Integer cartTotalLogisticFee;
    @SerializedName("cart_string")
    @Expose
    private String cartString;
    @SerializedName("cart_is_exception_error_delete")
    @Expose
    private Integer cartIsExceptionErrorDelete;
    @SerializedName("cart_shipments")
    @Expose
    private CartShipments cartShipments;
    @SerializedName("cart_destination")
    @Expose
    private CartDestination cartDestination;
    @SerializedName("cart_total_cart_count")
    @Expose
    private String cartTotalCartCount;
    @SerializedName("cart_total_logistic_fee_idr")
    @Expose
    private String cartTotalLogisticFeeIdr;
    @SerializedName("cart_customer_id")
    @Expose
    private String cartCustomerId;
    @SerializedName("cart_total_error")
    @Expose
    private String cartTotalError;
    @SerializedName("cart_insurance_prod")
    @Expose
    private Integer cartInsuranceProd;
    @SerializedName("cart_product_type")
    @Expose
    private Integer cartProductType;
    @SerializedName("cart_force_insurance")
    @Expose
    private Integer cartForceInsurance;
    @SerializedName("cart_error_message_2")
    @Expose
    private String cartErrorMessage2;
    @SerializedName("cart_cannot_insurance")
    @Expose
    private Integer cartCannotInsurance;
    @SerializedName("cart_insurance_price_idr")
    @Expose
    private String cartInsurancePriceIdr;
    @SerializedName("cart_shipping_rate")
    @Expose
    private String cartShippingRate;
    @SerializedName("cart_products")
    @Expose
    private List<CartProduct> cartProducts = new ArrayList<CartProduct>();
    @SerializedName("errors")
    @Expose
    private List<Object> errors = new ArrayList<Object>();
    @SerializedName("cart_error_message_1")
    @Expose
    private String cartErrorMessage1;
    @SerializedName("cart_can_process")
    @Expose
    private Integer cartCanProcess;
    @SerializedName("cart_total_product_price")
    @Expose
    private String cartTotalProductPrice;
    @SerializedName("cart_insurance_price")
    @Expose
    private String cartInsurancePrice;
    @SerializedName("cart_total_weight")
    @Expose
    private String cartTotalWeight;
    @SerializedName("cart_partial")
    @Expose
    private Integer cartPartial;
    @SerializedName("cart_total_product_price_idr")
    @Expose
    private String cartTotalProductPriceIdr;
    @SerializedName("cart_shipping_rate_idr")
    @Expose
    private String cartShippingRateIdr;
    @SerializedName("cart_total_amount_idr")
    @Expose
    private String cartTotalAmountIdr;
    @SerializedName("cart_is_allow_checkout")
    @Expose
    private Integer cartIsAllowCheckout;
    @SerializedName("cart_is_price_changed")
    @Expose
    private String cartIsPriceChanged;
    @SerializedName("cart_total_product")
    @Expose
    private String cartTotalProduct;
    @SerializedName("cart_shop")
    @Expose
    private CartShop cartShop;
    @SerializedName("cart_total_amount")
    @Expose
    private String cartTotalAmount;
    @SerializedName("cart_logistic_fee")
    @Expose
    private String cartLogisticFee;
    @SerializedName("cart_total_product_active")
    @Expose
    private String cartTotalProductActive;

    @SerializedName("cart_dropship_name")
    @Expose
    private String cartDropshipName;
    @SerializedName("cart_dropship_telp")
    @Expose
    private String cartDropshipTelp;

    public Integer getCartTotalLogisticFee() {
        return cartTotalLogisticFee;
    }

    public void setCartTotalLogisticFee(Integer cartTotalLogisticFee) {
        this.cartTotalLogisticFee = cartTotalLogisticFee;
    }

    public String getCartString() {
        return cartString;
    }

    public void setCartString(String cartString) {
        this.cartString = cartString;
    }

    public Integer getCartIsExceptionErrorDelete() {
        return cartIsExceptionErrorDelete;
    }

    public void setCartIsExceptionErrorDelete(Integer cartIsExceptionErrorDelete) {
        this.cartIsExceptionErrorDelete = cartIsExceptionErrorDelete;
    }

    public CartShipments getCartShipments() {
        return cartShipments;
    }

    public void setCartShipments(CartShipments cartShipments) {
        this.cartShipments = cartShipments;
    }

    public CartDestination getCartDestination() {
        return cartDestination;
    }

    public void setCartDestination(CartDestination cartDestination) {
        this.cartDestination = cartDestination;
    }

    public String getCartTotalCartCount() {
        return cartTotalCartCount;
    }

    public void setCartTotalCartCount(String cartTotalCartCount) {
        this.cartTotalCartCount = cartTotalCartCount;
    }

    public String getCartTotalLogisticFeeIdr() {
        return cartTotalLogisticFeeIdr;
    }

    public void setCartTotalLogisticFeeIdr(String cartTotalLogisticFeeIdr) {
        this.cartTotalLogisticFeeIdr = cartTotalLogisticFeeIdr;
    }

    public String getCartCustomerId() {
        return cartCustomerId;
    }

    public void setCartCustomerId(String cartCustomerId) {
        this.cartCustomerId = cartCustomerId;
    }

    public String getCartTotalError() {
        return cartTotalError;
    }

    public void setCartTotalError(String cartTotalError) {
        this.cartTotalError = cartTotalError;
    }

    public Integer getCartInsuranceProd() {
        return cartInsuranceProd;
    }

    public void setCartInsuranceProd(Integer cartInsuranceProd) {
        this.cartInsuranceProd = cartInsuranceProd;
    }

    public Integer getCartProductType() {
        return cartProductType;
    }

    public void setCartProductType(Integer cartProductType) {
        this.cartProductType = cartProductType;
    }

    public Integer getCartForceInsurance() {
        return cartForceInsurance;
    }

    public void setCartForceInsurance(Integer cartForceInsurance) {
        this.cartForceInsurance = cartForceInsurance;
    }

    public String getCartErrorMessage2() {
        return cartErrorMessage2;
    }

    public void setCartErrorMessage2(String cartErrorMessage2) {
        this.cartErrorMessage2 = cartErrorMessage2;
    }

    public Integer getCartCannotInsurance() {
        return cartCannotInsurance;
    }

    public void setCartCannotInsurance(Integer cartCannotInsurance) {
        this.cartCannotInsurance = cartCannotInsurance;
    }

    public String getCartInsurancePriceIdr() {
        return cartInsurancePriceIdr;
    }

    public void setCartInsurancePriceIdr(String cartInsurancePriceIdr) {
        this.cartInsurancePriceIdr = cartInsurancePriceIdr;
    }

    public String getCartShippingRate() {
        return cartShippingRate;
    }

    public void setCartShippingRate(String cartShippingRate) {
        this.cartShippingRate = cartShippingRate;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public String getCartErrorMessage1() {
        return cartErrorMessage1;
    }

    public void setCartErrorMessage1(String cartErrorMessage1) {
        this.cartErrorMessage1 = cartErrorMessage1;
    }

    public Integer getCartCanProcess() {
        return cartCanProcess;
    }

    public void setCartCanProcess(Integer cartCanProcess) {
        this.cartCanProcess = cartCanProcess;
    }

    public String getCartTotalProductPrice() {
        return cartTotalProductPrice;
    }

    public void setCartTotalProductPrice(String cartTotalProductPrice) {
        this.cartTotalProductPrice = cartTotalProductPrice;
    }

    public String getCartInsurancePrice() {
        return cartInsurancePrice;
    }

    public void setCartInsurancePrice(String cartInsurancePrice) {
        this.cartInsurancePrice = cartInsurancePrice;
    }

    public String getCartTotalWeight() {
        return cartTotalWeight;
    }

    public void setCartTotalWeight(String cartTotalWeight) {
        this.cartTotalWeight = cartTotalWeight;
    }

    public Integer getCartPartial() {
        return cartPartial;
    }

    public void setCartPartial(Integer cartPartial) {
        this.cartPartial = cartPartial;
    }

    public String getCartTotalProductPriceIdr() {
        return cartTotalProductPriceIdr;
    }

    public void setCartTotalProductPriceIdr(String cartTotalProductPriceIdr) {
        this.cartTotalProductPriceIdr = cartTotalProductPriceIdr;
    }

    public String getCartShippingRateIdr() {
        return cartShippingRateIdr;
    }

    public void setCartShippingRateIdr(String cartShippingRateIdr) {
        this.cartShippingRateIdr = cartShippingRateIdr;
    }

    public String getCartTotalAmountIdr() {
        return cartTotalAmountIdr;
    }

    public void setCartTotalAmountIdr(String cartTotalAmountIdr) {
        this.cartTotalAmountIdr = cartTotalAmountIdr;
    }

    public Integer getCartIsAllowCheckout() {
        return cartIsAllowCheckout;
    }

    public void setCartIsAllowCheckout(Integer cartIsAllowCheckout) {
        this.cartIsAllowCheckout = cartIsAllowCheckout;
    }

    public String getCartIsPriceChanged() {
        return cartIsPriceChanged;
    }

    public void setCartIsPriceChanged(String cartIsPriceChanged) {
        this.cartIsPriceChanged = cartIsPriceChanged;
    }

    public String getCartTotalProduct() {
        return cartTotalProduct;
    }

    public void setCartTotalProduct(String cartTotalProduct) {
        this.cartTotalProduct = cartTotalProduct;
    }

    public CartShop getCartShop() {
        return cartShop;
    }

    public void setCartShop(CartShop cartShop) {
        this.cartShop = cartShop;
    }

    public String getCartTotalAmount() {
        return cartTotalAmount;
    }

    public void setCartTotalAmount(String cartTotalAmount) {
        this.cartTotalAmount = cartTotalAmount;
    }

    public String getCartLogisticFee() {
        return cartLogisticFee;
    }

    public void setCartLogisticFee(String cartLogisticFee) {
        this.cartLogisticFee = cartLogisticFee;
    }

    public String getCartTotalProductActive() {
        return cartTotalProductActive;
    }

    public void setCartTotalProductActive(String cartTotalProductActive) {
        this.cartTotalProductActive = cartTotalProductActive;
    }

    public String getCartDropshipName() {
        return cartDropshipName;
    }

    public void setCartDropshipName(String cartDropshipName) {
        this.cartDropshipName = cartDropshipName;
    }

    public String getCartDropshipTelp() {
        return cartDropshipTelp;
    }

    public void setCartDropshipTelp(String cartDropshipTelp) {
        this.cartDropshipTelp = cartDropshipTelp;
    }

    protected Cart(Parcel in) {
        cartTotalLogisticFee = in.readByte() == 0x00 ? null : in.readInt();
        cartString = in.readString();
        cartIsExceptionErrorDelete = in.readByte() == 0x00 ? null : in.readInt();
        cartShipments = (CartShipments) in.readValue(CartShipments.class.getClassLoader());
        cartDestination = (CartDestination) in.readValue(CartDestination.class.getClassLoader());
        cartTotalCartCount = in.readString();
        cartTotalLogisticFeeIdr = in.readString();
        cartCustomerId = in.readString();
        cartTotalError = in.readString();
        cartInsuranceProd = in.readByte() == 0x00 ? null : in.readInt();
        cartProductType = in.readByte() == 0x00 ? null : in.readInt();
        cartForceInsurance = in.readByte() == 0x00 ? null : in.readInt();
        cartErrorMessage2 = in.readString();
        cartCannotInsurance = in.readByte() == 0x00 ? null : in.readInt();
        cartInsurancePriceIdr = in.readString();
        cartShippingRate = in.readString();
        if (in.readByte() == 0x01) {
            cartProducts = new ArrayList<CartProduct>();
            in.readList(cartProducts, CartProduct.class.getClassLoader());
        } else {
            cartProducts = null;
        }
        if (in.readByte() == 0x01) {
            errors = new ArrayList<Object>();
            in.readList(errors, Object.class.getClassLoader());
        } else {
            errors = null;
        }
        cartErrorMessage1 = in.readString();
        cartCanProcess = in.readByte() == 0x00 ? null : in.readInt();
        cartTotalProductPrice = in.readString();
        cartInsurancePrice = in.readString();
        cartTotalWeight = in.readString();
        cartPartial = in.readByte() == 0x00 ? null : in.readInt();
        cartTotalProductPriceIdr = in.readString();
        cartShippingRateIdr = in.readString();
        cartTotalAmountIdr = in.readString();
        cartIsAllowCheckout = in.readByte() == 0x00 ? null : in.readInt();
        cartIsPriceChanged = in.readString();
        cartTotalProduct = in.readString();
        cartShop = (CartShop) in.readValue(CartShop.class.getClassLoader());
        cartTotalAmount = in.readString();
        cartLogisticFee = in.readString();
        cartTotalProductActive = in.readString();
        cartDropshipName = in.readString();
        cartDropshipTelp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (cartTotalLogisticFee == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartTotalLogisticFee);
        }
        dest.writeString(cartString);
        if (cartIsExceptionErrorDelete == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartIsExceptionErrorDelete);
        }
        dest.writeValue(cartShipments);
        dest.writeValue(cartDestination);
        dest.writeString(cartTotalCartCount);
        dest.writeString(cartTotalLogisticFeeIdr);
        dest.writeString(cartCustomerId);
        dest.writeString(cartTotalError);
        if (cartInsuranceProd == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartInsuranceProd);
        }
        if (cartProductType == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartProductType);
        }
        if (cartForceInsurance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartForceInsurance);
        }
        dest.writeString(cartErrorMessage2);
        if (cartCannotInsurance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartCannotInsurance);
        }
        dest.writeString(cartInsurancePriceIdr);
        dest.writeString(cartShippingRate);
        if (cartProducts == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cartProducts);
        }
        if (errors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(errors);
        }
        dest.writeString(cartErrorMessage1);
        if (cartCanProcess == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartCanProcess);
        }
        dest.writeString(cartTotalProductPrice);
        dest.writeString(cartInsurancePrice);
        dest.writeString(cartTotalWeight);
        if (cartPartial == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartPartial);
        }
        dest.writeString(cartTotalProductPriceIdr);
        dest.writeString(cartShippingRateIdr);
        dest.writeString(cartTotalAmountIdr);
        if (cartIsAllowCheckout == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cartIsAllowCheckout);
        }
        dest.writeString(cartIsPriceChanged);
        dest.writeString(cartTotalProduct);
        dest.writeValue(cartShop);
        dest.writeString(cartTotalAmount);
        dest.writeString(cartLogisticFee);
        dest.writeString(cartTotalProductActive);
        dest.writeString(cartDropshipName);
        dest.writeString(cartDropshipTelp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cart> CREATOR = new Parcelable.Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };
}
