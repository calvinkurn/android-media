package com.tokopedia.topads.dashboard.view.model;

/**
 * @author normansyahputa on 2/13/17.
 */

public class TopAdsAddProductModel extends StateTypeBasedModel
        implements BaseTopAdsProductModel {
    public static final int TYPE = 1292394;

    public String imageUrl;
    public String description;
    public String snippet;
    public TopAdsProductViewModel productDomain;

    private TopAdsAddProductModel() {

    }

    public TopAdsAddProductModel(String imageUrl, String description, String snippet) {
        this();
        this.imageUrl = imageUrl;
        this.description = description;
        this.snippet = snippet;
    }

    public TopAdsAddProductModel(String imageUrl, String description, String snippet,
                                 TopAdsProductViewModel productDomain) {
        this(imageUrl, description, snippet);
        this.productDomain = productDomain;
    }

    @Override
    public TopAdsProductViewModel getTopAdsProductViewModel() {
        return productDomain;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
