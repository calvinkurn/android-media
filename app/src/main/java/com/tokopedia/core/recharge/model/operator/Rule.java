package com.tokopedia.core.recharge.model.operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alifa on 11/16/2016.
 */

public class Rule {
    @SerializedName("product_text")
    @Expose
    private String productText;
    @SerializedName("products_view_style")
    @Expose
    private Integer productsViewStyle;
    @SerializedName("show_price")
    @Expose
    private Boolean showPrice;
    @SerializedName("show_product")
    @Expose
    private Boolean showProduct;
    @SerializedName("show_product_list_page")
    @Expose
    private Boolean showProductListPage;

    /**
     *
     * @return
     * The productText
     */
    public String getProductText() {
        return productText;
    }

    /**
     *
     * @param productText
     * The product_text
     */
    public void setProductText(String productText) {
        this.productText = productText;
    }

    /**
     *
     * @return
     * The productsViewStyle
     */
    public Integer getProductsViewStyle() {
        return productsViewStyle;
    }

    /**
     *
     * @param productsViewStyle
     * The products_view_style
     */
    public void setProductsViewStyle(Integer productsViewStyle) {
        this.productsViewStyle = productsViewStyle;
    }

    /**
     *
     * @return
     * The showPrice
     */
    public Boolean getShowPrice() {
        return showPrice;
    }

    /**
     *
     * @param showPrice
     * The show_price
     */
    public void setShowPrice(Boolean showPrice) {
        this.showPrice = showPrice;
    }

    /**
     *
     * @return
     * The showProduct
     */
    public Boolean getShowProduct() {
        return showProduct;
    }

    /**
     *
     * @param showProduct
     * The show_product
     */
    public void setShowProduct(Boolean showProduct) {
        this.showProduct = showProduct;
    }

    /**
     *
     * @return
     * The showProductListPage
     */
    public Boolean getShowProductListPage() {
        return showProductListPage;
    }

    /**
     *
     * @param showProductListPage
     * The show_product_list_page
     */
    public void setShowProductListPage(Boolean showProductListPage) {
        this.showProductListPage = showProductListPage;
    }
}
