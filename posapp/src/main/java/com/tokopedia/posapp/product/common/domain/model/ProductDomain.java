package com.tokopedia.posapp.product.common.domain.model;

import com.tokopedia.posapp.product.productlist.data.pojo.ProductPicture;

import java.util.List;

/**
 * Created by okasurya on 8/9/17.
 */
public class ProductDomain {
    private long id;
    private String name;
    private String price;
    private double priceUnformatted;
    private String url;
    private String description;
    private String image;
    private String image300;
    private String imageFull;
    private List<ProductPicture> pictures;
    private long etalaseId;
    private String originalPrice;
    private double originalPriceUnformatted;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getPriceUnformatted() {
        return priceUnformatted;
    }

    public void setPriceUnformatted(double priceUnformatted) {
        this.priceUnformatted = priceUnformatted;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage300() {
        return image300;
    }

    public void setImage300(String image300) {
        this.image300 = image300;
    }

    public String getImageFull() {
        return imageFull;
    }

    public void setImageFull(String imageFull) {
        this.imageFull = imageFull;
    }

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getOriginalPriceUnformatted() {
        return originalPriceUnformatted;
    }

    public void setOriginalPriceUnformatted(double originalPriceUnformatted) {
        this.originalPriceUnformatted = originalPriceUnformatted;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ProductPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<ProductPicture> pictures) {
        this.pictures = pictures;
    }
}
