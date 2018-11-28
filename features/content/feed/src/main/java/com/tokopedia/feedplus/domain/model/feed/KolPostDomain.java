package com.tokopedia.feedplus.domain.model.feed;

import javax.annotation.Nullable;

/**
 * @author by nisie on 11/2/17.
 */

public class KolPostDomain {
    private final int id;

    private final
    @Nullable
    String imageUrl;

    private final
    @Nullable
    String videoUrl;


    private final
    @Nullable
    String youtubeUrl;


    private final
    @Nullable
    String type;


    private final
    @Nullable
    String description;

    private final int commentCount;

    private final int likeCount;

    private final boolean isLiked;

    private final boolean isFollowed;

    private final
    @Nullable
    String createTime;

    private final
    @Nullable
    String productPrice;

    private final
    @Nullable
    String contentLink;

    private final
    @Nullable
    String contentUrl;

    private final
    @Nullable
    String userName;

    private final
    @Nullable
    String userPhoto;

    private final
    @Nullable
    String tagsType;

    private final
    @Nullable
    String caption;

    private final int itemId;

    private final
    @Nullable
    String label;

    private final
    @Nullable
    String headerTitle;

    @Nullable
    private final String userUrl;

    private final int userId;

    private final boolean isShowComment;

    private final boolean isShowLike;

    private final boolean reportable;

    @Nullable
    private final String cardType;


    public KolPostDomain(int id, String imageUrl, String videoUrl, String youtubeUrl, String type,
                         String description, int commentCount, int likeCount, boolean isLiked,
                         boolean isFollowed, String createTime, String productPrice,
                         String contentLink, String contentUrl, String userName, String userPhoto,
                         String tagsType, String caption, int itemId, String label,
                         String headerTitle, String userUrl, int userId, boolean isShowComment,
                         boolean isShowLike, boolean reportable, String cardType) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.youtubeUrl = youtubeUrl;
        this.type = type;
        this.description = description;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.createTime = createTime;
        this.productPrice = productPrice;
        this.contentLink = contentLink;
        this.contentUrl = contentUrl;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.tagsType = tagsType;
        this.caption = caption;
        this.itemId = itemId;
        this.label = label;
        this.headerTitle = headerTitle;
        this.userUrl = userUrl;
        this.userId = userId;
        this.isShowComment = isShowComment;
        this.isShowLike = isShowLike;
        this.reportable = reportable;
        this.cardType = cardType;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    public String getVideoUrl() {
        return videoUrl;
    }

    @Nullable
    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    @Nullable
    public String getCreateTime() {
        return createTime;
    }

    @Nullable
    public String getProductPrice() {
        return productPrice;
    }

    @Nullable
    public String getContentLink() {
        return contentLink;
    }

    @Nullable
    public String getContentUrl() {
        return contentUrl;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    @Nullable
    public String getUserPhoto() {
        return userPhoto;
    }

    @Nullable
    public String getTagsType() {
        return tagsType;
    }

    @Nullable
    public String getCaption() {
        return caption;
    }

    public int getItemId() {
        return itemId;
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    @Nullable
    public String getHeaderTitle() {
        return headerTitle;
    }

    @Nullable
    public String getUserUrl() {
        return userUrl;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isShowComment() {
        return isShowComment;
    }

    public boolean isShowLike() {
        return isShowLike;
    }

    public boolean isReportable() {
        return reportable;
    }

    @Nullable
    public String getCardType() {
        return cardType;
    }
}
