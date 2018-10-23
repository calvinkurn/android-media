package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

import java.util.List;

/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewModel extends BaseKolViewModel implements Visitable<KolPostTypeFactory> {
    public final static int DEFAULT_ID = -1;

    private List<String> imageList;
    private int tagsId;
    private String contentName;
    private String tagsType;
    private String tagsCaption;
    private String tagsLink;
    private String info;
    private boolean showTopShadow;

    public KolPostViewModel(int userId, String cardType, String title, String name,
                            String avatar, String label, String kolProfileUrl, boolean followed,
                            String review, boolean liked, int totalLike, int totalComment, int page,
                            int kolId, String time, boolean isShowComment, boolean isShowLike,
                            List<String> imageList, int tagsId, String contentName, String tagsType,
                            String tagsCaption, String tagsLink) {
        super(userId, cardType, title, name, avatar, label, kolProfileUrl, followed, review,
                liked, totalLike, totalComment, page, kolId, time, isShowComment, isShowLike);
        this.imageList = imageList;
        this.tagsId = tagsId;
        this.contentName = contentName;
        this.tagsType = tagsType;
        this.tagsCaption = tagsCaption;
        this.tagsLink = tagsLink;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isShowTopShadow() {
        return showTopShadow;
    }

    public void setShowTopShadow(boolean showTopShadow) {
        this.showTopShadow = showTopShadow;
    }

    @Override
    public int type(KolPostTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}

