package com.tokopedia.product.manage.item.main.draft.data.model;

import com.tokopedia.base.list.seller.common.util.ItemType;

/**
 * Created by User on 6/21/2017.
 */

public class ProductDraftViewModel implements ItemType {
    public static final int TYPE = 1;

    private long draftProductId;
    private String primaryImageUrl;
    private String productName;
    private int completionPercent;
    private boolean isEdit;

    public ProductDraftViewModel(long draftProductId, String primaryImageUrl,
                                 String productName, int completionPercent, boolean isEdit) {
        this.draftProductId = draftProductId;
        this.primaryImageUrl = primaryImageUrl;
        this.productName = productName;
        this.completionPercent = completionPercent;
        this.isEdit = isEdit;
    }

    public long getProductDraftId() {
        return draftProductId;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public int getCompletionPercent() {
        return completionPercent;
    }

    public boolean isEdit() {
        return isEdit;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
