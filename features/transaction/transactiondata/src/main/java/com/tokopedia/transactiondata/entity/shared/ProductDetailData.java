package com.tokopedia.transactiondata.entity.shared;

import com.tokopedia.transactiondata.entity.response.cartlist.WholesalePrice;

import java.util.List;

/**
 * Created by Irfan Khoirul on 07/02/19.
 */

public class ProductDetailData {

    // Common data
    private int parentId;
    private int productImageUrl;
    private int productName;
    private int minOrderQuantity;
    private int maxOrderQuantity;
    private int productPrice;
    // Wholesale
    private List<WholesalePrice> wholesalePrice;
    // Variant data
    private int defaultChild;


}
