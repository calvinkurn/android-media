package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

import java.util.List;

/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewModel extends BaseKolViewModel implements Visitable<KolPostTypeFactory> {
    public final static int DEFAULT_ID = -1;

    public final static String TYPE_YOUTUBE = "youtube";
    public final static String TYPE_VIDEO = "video";
    public final static String TYPE_MULTI = "multi";


    private List<String> imageList;
    private int tagsId;
    private String contentName;
    private String tagsType;
    private String tagsCaption;
    private String tagsLink;
    private String trackingId;
    private String info;
    private boolean showTopShadow;

    public KolPostViewModel(int userId, String activityType, String cardType, String title, String name, String avatar, String label, String kolProfileUrl, boolean followed, String review, boolean liked, int totalLike, int totalComment, int page, int contentId, String time, boolean isShowComment, boolean isShowLike, boolean editable, boolean deletable, boolean multipleContent) {
        super(userId, activityType, cardType, title, name, avatar, label, kolProfileUrl, followed, review, liked, totalLike, totalComment, page, contentId, time, isShowComment, isShowLike, editable, deletable, multipleContent);
    }

    public KolPostViewModel(int userId, String cardType, String title, String name, String avatar,
                            String label, String kolProfileUrl, boolean followed, String review,
                            boolean liked, int totalLike, int totalComment, int page, int kolId,
                            String time, boolean isShowComment, boolean isShowLike,
                            List<String> imageList, int tagsId, String contentName, String tagsType,
                            String tagsCaption, String tagsLink) {
        super(userId, tagsType, cardType, title, name, avatar, label, kolProfileUrl, followed,
                review, liked, totalLike, totalComment, page, kolId, time, isShowComment,
                isShowLike, false, false, imageList.size() > 1);
        this.imageList = imageList;
        this.tagsId = tagsId;
        this.contentName = contentName;
        this.tagsType = tagsType;
        this.tagsCaption = tagsCaption;
        this.tagsLink = tagsLink;
    }

    public KolPostViewModel(int userId, String cardType, String title, String name, String avatar,
                            String label, String kolProfileUrl, boolean followed, String review,
                            boolean liked, int totalLike, int totalComment, int page, int kolId,
                            String time, boolean isShowComment, boolean isShowLike,
                            boolean editable, boolean deletable, List<String> imageList,
                            int tagsId, String contentName, String tagsType,
                            String tagsCaption, String tagsLink, String trackingId, String info) {
        super(userId, tagsType, cardType, title, name, avatar, label, kolProfileUrl, followed,
                review, liked, totalLike, totalComment, page, kolId, time, isShowComment,
                isShowLike, editable, deletable, imageList.size() > 1);
        this.imageList = imageList;
        this.tagsId = tagsId;
        this.contentName = contentName;
        this.tagsType = tagsType;
        this.tagsCaption = tagsCaption;
        this.tagsLink = tagsLink;
        this.trackingId = trackingId;
        this.info = info;
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

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
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

