package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewModel extends BaseKolViewModel implements Visitable<KolPostTypeFactory> {
    public final static int DEFAULT_ID = -1;

    private String kolImage;
    private int tagsId;
    private String contentName;
    private String tagsType;
    private String tagsCaption;
    private String tagsLink;

    public KolPostViewModel(int userId, String cardType, String title, String name,
                            String avatar, String label, String kolProfileUrl, boolean followed,
                            String review, boolean liked, int totalLike, int totalComment, int page,
                            int kolId, String time, boolean isShowComment, boolean isShowLike,
                            String kolImage, int tagsId, String contentName, String tagsType,
                            String tagsCaption, String tagsLink) {
        super(userId, cardType, title, name, avatar, label, kolProfileUrl, followed, review,
                liked, totalLike, totalComment, page, kolId, time, isShowComment, isShowLike);
        this.kolImage = kolImage;
        this.tagsId = tagsId;
        this.contentName = contentName;
        this.tagsType = tagsType;
        this.tagsCaption = tagsCaption;
        this.tagsLink = tagsLink;
    }

    public String getKolImage() {
        return kolImage;
    }

    public void setKolImage(String kolImage) {
        this.kolImage = kolImage;
    }

    public int getTagsId() {
        return tagsId;
    }

    public void setTagsId(int tagsId) {
        this.tagsId = tagsId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getTagsType() {
        return tagsType;
    }

    public void setTagsType(String tagsType) {
        this.tagsType = tagsType;
    }

    public String getTagsCaption() {
        return tagsCaption;
    }

    public void setTagsCaption(String tagsCaption) {
        this.tagsCaption = tagsCaption;
    }

    public String getTagsLink() {
        return tagsLink;
    }

    public void setTagsLink(String tagsLink) {
        this.tagsLink = tagsLink;
    }

    @Override
    public int type(KolPostTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}

