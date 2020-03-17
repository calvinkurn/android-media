package com.tokopedia.feedplus.view.viewmodel.feeddetail;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;

import java.util.List;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedDetailViewModel implements Visitable<FeedPlusDetailTypeFactory> {

    private String name;
    private String price;
    private String priceOriginal;
    private String imageSource;
    private String url;
    private String cashback;
    private boolean isWholesale;
    private boolean isPreorder;
    private boolean isFreeReturn;
    private boolean isWishlist;
    private List<TagsItem> tags;
    private Double rating;
    private String countReview;
    private Integer productId;


    public FeedDetailViewModel(Integer productId,
                               String name,
                               String price,
                               String priceOriginal,
                               String imageSource,
                               String url,
                               String cashback,
                               boolean isWholesale,
                               boolean isPreorder,
                               boolean isFreeReturn,
                               boolean isWishlist,
                               List<TagsItem> tags,
                               Double rating,
                               String countReview) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.priceOriginal = priceOriginal;
        this.imageSource = imageSource;
        this.url = url;
        this.cashback = cashback;
        this.isWholesale = isWholesale;
        this.isPreorder = isPreorder;
        this.isFreeReturn = isFreeReturn;
        this.isWishlist = isWishlist;
        this.tags = tags;
        this.rating = rating;
        this.countReview = countReview;
    }

    @Override
    public int type(FeedPlusDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
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

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public void setWholesale(boolean wholesale) {
        isWholesale = wholesale;
    }

    public boolean isPreorder() {
        return isPreorder;
    }

    public void setPreorder(boolean preorder) {
        isPreorder = preorder;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        isFreeReturn = freeReturn;
    }

    public boolean isWishlist() {
        return isWishlist;
    }

    public void setWishlist(boolean wishlist) {
        isWishlist = wishlist;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(String priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public String getCountReview() {
        return countReview;
    }

    public void setCountReview(String countReview) {
        this.countReview = countReview;
    }

    public List<TagsItem> getTags() {
        return tags;
    }

    public void setTags(List<TagsItem> tags) {
        this.tags = tags;
    }
}
