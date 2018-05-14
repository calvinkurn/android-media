package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewModel extends BaseKolViewModel implements Visitable<KolPostTypeFactory> {
    public final static int DEFAULT_ID = -1;

    private String kolImage;
    private String productTooltip;
    private String productPrice;

    public KolPostViewModel(String title, String name, String avatar, String label,
                            boolean followed, String kolImage, String productTooltip,
                            String review, boolean liked, int totalLike, int totalComment,
                            int page, String kolProfileUrl, int contentId, int kolId, String time,
                            String contentName, String productPrice, boolean wishlisted,
                            String tagsType, String contentLink, int userId, boolean isShowComment,
                            String cardType) {
        super(tagsType, contentLink, userId, cardType, title, name, avatar, label, kolProfileUrl,
                followed, review, liked, totalLike, totalComment, page, contentId, kolId, time,
                contentName, wishlisted, isShowComment);
        this.kolImage = kolImage;
        this.productTooltip = productTooltip;
        this.productPrice = productPrice;
    }

    public String getKolImage() {
        return kolImage;
    }

    public void setKolImage(String kolImage) {
        this.kolImage = kolImage;
    }

    public String getProductTooltip() {
        return productTooltip;
    }

    public void setProductTooltip(String productTooltip) {
        this.productTooltip = productTooltip;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public int type(KolPostTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}

