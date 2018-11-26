package com.tokopedia.gallery.networkmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageReviewGqlResponse {

    @SerializedName("ProductReviewImageListQuery")
    @Expose
    private ProductReviewImageListQuery productReviewImageListQuery;

    public ProductReviewImageListQuery getProductReviewImageListQuery() {
        return productReviewImageListQuery;
    }

    public static class ProductReviewImageListQuery {
        @SerializedName("list")
        @Expose
        private List<Item> list;
        @SerializedName("detail")
        @Expose
        private Detail detail;
        @SerializedName("hasNext")
        @Expose
        private boolean hasNext;
        @SerializedName("hasPrev")
        @Expose
        private boolean hasPrev;

        public List<Item> getList() {
            return list;
        }

        public void setList(List<Item> list) {
            this.list = list;
        }

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public boolean isHasPrev() {
            return hasPrev;
        }

        public void setHasPrev(boolean hasPrev) {
            this.hasPrev = hasPrev;
        }
    }

    public static class Item {

        @SerializedName("imageID")
        @Expose
        private int imageID;
        @SerializedName("reviewID")
        @Expose
        private int reviewID;
        @SerializedName("imageSibling")
        @Expose
        private List<Integer> imageSibling = null;

        public int getImageID() {
            return imageID;
        }

        public void setImageID(int imageID) {
            this.imageID = imageID;
        }

        public int getReviewID() {
            return reviewID;
        }

        public void setReviewID(int reviewID) {
            this.reviewID = reviewID;
        }

        public List<Integer> getImageSibling() {
            return imageSibling;
        }

        public void setImageSibling(List<Integer> imageSibling) {
            this.imageSibling = imageSibling;
        }
    }

    public static class Detail {

        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;
        @SerializedName("images")
        @Expose
        private List<Image> images = null;

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }
    }

    public static class Image {

        @SerializedName("imageAttachmentID")
        @Expose
        private int imageAttachmentID;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("uriThumbnail")
        @Expose
        private String uriThumbnail;
        @SerializedName("uriLarge")
        @Expose
        private String uriLarge;
        @SerializedName("reviewID")
        @Expose
        private int reviewID;

        public int getImageAttachmentID() {
            return imageAttachmentID;
        }

        public void setImageAttachmentID(int imageAttachmentID) {
            this.imageAttachmentID = imageAttachmentID;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUriThumbnail() {
            return uriThumbnail;
        }

        public void setUriThumbnail(String uriThumbnail) {
            this.uriThumbnail = uriThumbnail;
        }

        public String getUriLarge() {
            return uriLarge;
        }

        public void setUriLarge(String uriLarge) {
            this.uriLarge = uriLarge;
        }

        public int getReviewID() {
            return reviewID;
        }

        public void setReviewID(int reviewID) {
            this.reviewID = reviewID;
        }
    }

    public static class Review {

        @SerializedName("reviewId")
        @Expose
        private int reviewId;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("ratingDescription")
        @Expose
        private String ratingDescription;
        @SerializedName("rating")
        @Expose
        private int rating;
        @SerializedName("time_format")
        @Expose
        private TimeFormat timeFormat;
        @SerializedName("updateTime")
        @Expose
        private int updateTime;
        @SerializedName("isAnonymous")
        @Expose
        private boolean isAnonymous;
        @SerializedName("isReportable")
        @Expose
        private boolean isReportable;
        @SerializedName("isUpdated")
        @Expose
        private boolean isUpdated;
        @SerializedName("reviewer")
        @Expose
        private Reviewer reviewer;

        public int getReviewId() {
            return reviewId;
        }

        public void setReviewId(int reviewId) {
            this.reviewId = reviewId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRatingDescription() {
            return ratingDescription;
        }

        public void setRatingDescription(String ratingDescription) {
            this.ratingDescription = ratingDescription;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public TimeFormat getTimeFormat() {
            return timeFormat;
        }

        public void setTimeFormat(TimeFormat timeFormat) {
            this.timeFormat = timeFormat;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public boolean isIsAnonymous() {
            return isAnonymous;
        }

        public void setIsAnonymous(boolean isAnonymous) {
            this.isAnonymous = isAnonymous;
        }

        public boolean isIsReportable() {
            return isReportable;
        }

        public void setIsReportable(boolean isReportable) {
            this.isReportable = isReportable;
        }

        public boolean isIsUpdated() {
            return isUpdated;
        }

        public void setIsUpdated(boolean isUpdated) {
            this.isUpdated = isUpdated;
        }

        public Reviewer getReviewer() {
            return reviewer;
        }

        public void setReviewer(Reviewer reviewer) {
            this.reviewer = reviewer;
        }

    }

    public static class Reviewer {

        @SerializedName("userID")
        @Expose
        private int userID;
        @SerializedName("fullName")
        @Expose
        private String fullName;
        @SerializedName("profilePicture")
        @Expose
        private String profilePicture;
        @SerializedName("url")
        @Expose
        private String url;

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public static class TimeFormat {

        @SerializedName("date_time_fmt1")
        @Expose
        private String dateTimeFmt1;

        public String getDateTimeFmt1() {
            return dateTimeFmt1;
        }

        public void setDateTimeFmt1(String dateTimeFmt1) {
            this.dateTimeFmt1 = dateTimeFmt1;
        }

    }
}
