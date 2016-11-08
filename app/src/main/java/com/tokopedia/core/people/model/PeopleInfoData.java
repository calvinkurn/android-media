package com.tokopedia.core.people.model;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 5/31/16.
 */
public class PeopleInfoData {

    @SerializedName("user_info")
    private UserInfo userInfo;
    @SerializedName("shop_stats")
    private ShopStats shopStats;
    @SerializedName("shop_info")
    private ShopInfo shopInfo;
    @SerializedName("ratings")
    private Ratings ratings;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ShopStats getShopStats() {
        return shopStats;
    }

    public void setShopStats(ShopStats shopStats) {
        this.shopStats = shopStats;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public Ratings getRatings() {
        return ratings;
    }

    public void setRatings(Ratings ratings) {
        this.ratings = ratings;
    }

    public static class UserInfo {
        @SerializedName("user_name")
        private String userName;
        @SerializedName("user_messenger")
        private String userMessenger;
        @SerializedName("user_birth")
        private String userBirth;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("user_email")
        private String userEmail;
        @SerializedName("user_hobbies")
        private String userHobbies;
        @SerializedName("user_reputation")
        private UserReputation userReputation;
        @SerializedName("user_phone")
        private String userPhone;
        @SerializedName("user_image")
        private String userImage;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserMessenger() {
            return Html.fromHtml(userMessenger).toString();
        }

        public void setUserMessenger(String userMessenger) {
            this.userMessenger = userMessenger;
        }

        public String getUserBirth() {
            return userBirth;
        }

        public void setUserBirth(String userBirth) {
            this.userBirth = userBirth;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserHobbies() {
            return Html.fromHtml(userHobbies).toString();
        }

        public void setUserHobbies(String userHobbies) {
            this.userHobbies = userHobbies;
        }

        public UserReputation getUserReputation() {
            return userReputation;
        }

        public void setUserReputation(UserReputation userReputation) {
            this.userReputation = userReputation;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public static class UserReputation {
            @SerializedName("negative")
            private String negative;
            @SerializedName("neutral")
            private String neutral;
            @SerializedName("positive")
            private String positive;
            @SerializedName("no_reputation")
            private int noReputation;
            @SerializedName("positive_percentage")
            private String positivePercentage;

            public String getNegative() {
                return negative;
            }

            public void setNegative(String negative) {
                this.negative = negative;
            }

            public String getNeutral() {
                return neutral;
            }

            public void setNeutral(String neutral) {
                this.neutral = neutral;
            }

            public String getPositive() {
                return positive;
            }

            public void setPositive(String positive) {
                this.positive = positive;
            }

            public int getNoReputation() {
                return noReputation;
            }

            public void setNoReputation(int noReputation) {
                this.noReputation = noReputation;
            }

            public String getPositivePercentage() {
                return positivePercentage;
            }

            public void setPositivePercentage(String positivePercentage) {
                this.positivePercentage = positivePercentage;
            }
        }
    }

    public static class ShopStats {
        @SerializedName("shop_badge_level")
        private ShopBadgeLevel shopBadgeLevel;
        @SerializedName("shop_accuracy_rate")
        private String shopAccuracyRate;
        @SerializedName("shop_item_sold")
        private String shopItemSold;
        @SerializedName("shop_total_etalase")
        private String shopTotalEtalase;
        @SerializedName("shop_service_rate")
        private String shopServiceRate;
        @SerializedName("shop_speed_description")
        private String shopSpeedDescription;
        @SerializedName("shop_accuracy_description")
        private String shopAccuracyDescription;
        @SerializedName("shop_total_transaction_canceled")
        private String shopTotalTransactionCanceled;
        @SerializedName("shop_total_transaction")
        private String shopTotalTransaction;
        @SerializedName("shop_service_description")
        private String shopServiceDescription;
        @SerializedName("shop_last_twelve_months")
        private ShopLastTwelveMonths shopLastTwelveMonths;
        @SerializedName("shop_last_six_months")
        private ShopLastSixMonths shopLastSixMonths;
        @SerializedName("shop_reputation_score")
        private String shopReputationScore;
        @SerializedName("shop_last_one_month")
        private ShopLastOneMonth shopLastOneMonth;
        @SerializedName("shop_total_product")
        private String shopTotalProduct;
        @SerializedName("shop_speed_rate")
        private String shopSpeedRate;

        public ShopBadgeLevel getShopBadgeLevel() {
            return shopBadgeLevel;
        }

        public void setShopBadgeLevel(ShopBadgeLevel shopBadgeLevel) {
            this.shopBadgeLevel = shopBadgeLevel;
        }

        public String getShopAccuracyRate() {
            return shopAccuracyRate;
        }

        public void setShopAccuracyRate(String shopAccuracyRate) {
            this.shopAccuracyRate = shopAccuracyRate;
        }

        public String getShopItemSold() {
            return shopItemSold;
        }

        public void setShopItemSold(String shopItemSold) {
            this.shopItemSold = shopItemSold;
        }

        public String getShopTotalEtalase() {
            return shopTotalEtalase;
        }

        public void setShopTotalEtalase(String shopTotalEtalase) {
            this.shopTotalEtalase = shopTotalEtalase;
        }

        public String getShopServiceRate() {
            return shopServiceRate;
        }

        public void setShopServiceRate(String shopServiceRate) {
            this.shopServiceRate = shopServiceRate;
        }

        public String getShopSpeedDescription() {
            return shopSpeedDescription;
        }

        public void setShopSpeedDescription(String shopSpeedDescription) {
            this.shopSpeedDescription = shopSpeedDescription;
        }

        public String getShopAccuracyDescription() {
            return shopAccuracyDescription;
        }

        public void setShopAccuracyDescription(String shopAccuracyDescription) {
            this.shopAccuracyDescription = shopAccuracyDescription;
        }

        public String getShopTotalTransactionCanceled() {
            return shopTotalTransactionCanceled;
        }

        public void setShopTotalTransactionCanceled(String shopTotalTransactionCanceled) {
            this.shopTotalTransactionCanceled = shopTotalTransactionCanceled;
        }

        public String getShopTotalTransaction() {
            return shopTotalTransaction;
        }

        public void setShopTotalTransaction(String shopTotalTransaction) {
            this.shopTotalTransaction = shopTotalTransaction;
        }

        public String getShopServiceDescription() {
            return shopServiceDescription;
        }

        public void setShopServiceDescription(String shopServiceDescription) {
            this.shopServiceDescription = shopServiceDescription;
        }

        public ShopLastTwelveMonths getShopLastTwelveMonths() {
            return shopLastTwelveMonths;
        }

        public void setShopLastTwelveMonths(ShopLastTwelveMonths shopLastTwelveMonths) {
            this.shopLastTwelveMonths = shopLastTwelveMonths;
        }

        public ShopLastSixMonths getShopLastSixMonths() {
            return shopLastSixMonths;
        }

        public void setShopLastSixMonths(ShopLastSixMonths shopLastSixMonths) {
            this.shopLastSixMonths = shopLastSixMonths;
        }

        public String getShopReputationScore() {
            return shopReputationScore;
        }

        public void setShopReputationScore(String shopReputationScore) {
            this.shopReputationScore = shopReputationScore;
        }

        public ShopLastOneMonth getShopLastOneMonth() {
            return shopLastOneMonth;
        }

        public void setShopLastOneMonth(ShopLastOneMonth shopLastOneMonth) {
            this.shopLastOneMonth = shopLastOneMonth;
        }

        public String getShopTotalProduct() {
            return shopTotalProduct;
        }

        public void setShopTotalProduct(String shopTotalProduct) {
            this.shopTotalProduct = shopTotalProduct;
        }

        public String getShopSpeedRate() {
            return shopSpeedRate;
        }

        public void setShopSpeedRate(String shopSpeedRate) {
            this.shopSpeedRate = shopSpeedRate;
        }

        public static class ShopBadgeLevel {
            @SerializedName("set")
            private int set;
            @SerializedName("level")
            private int level;

            public int getSet() {
                return set;
            }

            public void setSet(int set) {
                this.set = set;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }
        }

        public static class ShopLastTwelveMonths {
            @SerializedName("count_score_bad")
            private String countScoreBad;
            @SerializedName("count_score_neutral")
            private String countScoreNeutral;
            @SerializedName("count_score_good")
            private String countScoreGood;

            public String getCountScoreBad() {
                return countScoreBad;
            }

            public void setCountScoreBad(String countScoreBad) {
                this.countScoreBad = countScoreBad;
            }

            public String getCountScoreNeutral() {
                return countScoreNeutral;
            }

            public void setCountScoreNeutral(String countScoreNeutral) {
                this.countScoreNeutral = countScoreNeutral;
            }

            public String getCountScoreGood() {
                return countScoreGood;
            }

            public void setCountScoreGood(String countScoreGood) {
                this.countScoreGood = countScoreGood;
            }
        }

        public static class ShopLastSixMonths {
            @SerializedName("count_score_bad")
            private String countScoreBad;
            @SerializedName("count_score_neutral")
            private String countScoreNeutral;
            @SerializedName("count_score_good")
            private String countScoreGood;

            public String getCountScoreBad() {
                return countScoreBad;
            }

            public void setCountScoreBad(String countScoreBad) {
                this.countScoreBad = countScoreBad;
            }

            public String getCountScoreNeutral() {
                return countScoreNeutral;
            }

            public void setCountScoreNeutral(String countScoreNeutral) {
                this.countScoreNeutral = countScoreNeutral;
            }

            public String getCountScoreGood() {
                return countScoreGood;
            }

            public void setCountScoreGood(String countScoreGood) {
                this.countScoreGood = countScoreGood;
            }
        }

        public static class ShopLastOneMonth {
            @SerializedName("count_score_bad")
            private String countScoreBad;
            @SerializedName("count_score_neutral")
            private String countScoreNeutral;
            @SerializedName("count_score_good")
            private String countScoreGood;

            public String getCountScoreBad() {
                return countScoreBad;
            }

            public void setCountScoreBad(String countScoreBad) {
                this.countScoreBad = countScoreBad;
            }

            public String getCountScoreNeutral() {
                return countScoreNeutral;
            }

            public void setCountScoreNeutral(String countScoreNeutral) {
                this.countScoreNeutral = countScoreNeutral;
            }

            public String getCountScoreGood() {
                return countScoreGood;
            }

            public void setCountScoreGood(String countScoreGood) {
                this.countScoreGood = countScoreGood;
            }
        }
    }

    public static class ShopInfo {
        @SerializedName("shop_description")
        private String shopDescription;
        @SerializedName("shop_avatar")
        private String shopAvatar;
        @SerializedName("shop_location")
        private String shopLocation;
        @SerializedName("shop_url")
        private String shopUrl;
        @SerializedName("shop_owner_id")
        private String shopOwnerId;
        @SerializedName("shop_open_since")
        private String shopOpenSince;
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("shop_id")
        private String shopId;
        @SerializedName("shop_cover")
        private String shopCover;
        @SerializedName("shop_domain")
        private String shopDomain;
        @SerializedName("shop_tagline")
        private String shopTagline;

        public String getShopDescription() {
            return shopDescription;
        }

        public void setShopDescription(String shopDescription) {
            this.shopDescription = shopDescription;
        }

        public String getShopAvatar() {
            return shopAvatar;
        }

        public void setShopAvatar(String shopAvatar) {
            this.shopAvatar = shopAvatar;
        }

        public String getShopLocation() {
            return shopLocation;
        }

        public void setShopLocation(String shopLocation) {
            this.shopLocation = shopLocation;
        }

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        public String getShopOwnerId() {
            return shopOwnerId;
        }

        public void setShopOwnerId(String shopOwnerId) {
            this.shopOwnerId = shopOwnerId;
        }

        public String getShopOpenSince() {
            return shopOpenSince;
        }

        public void setShopOpenSince(String shopOpenSince) {
            this.shopOpenSince = shopOpenSince;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopCover() {
            return shopCover;
        }

        public void setShopCover(String shopCover) {
            this.shopCover = shopCover;
        }

        public String getShopDomain() {
            return shopDomain;
        }

        public void setShopDomain(String shopDomain) {
            this.shopDomain = shopDomain;
        }

        public String getShopTagline() {
            return shopTagline;
        }

        public void setShopTagline(String shopTagline) {
            this.shopTagline = shopTagline;
        }
    }

    public static class Ratings {
        @SerializedName("quality_width")
        private QualityWidth qualityWidth;
        @SerializedName("accuracy_width")
        private AccuracyWidth accuracyWidth;
        @SerializedName("accuracy")
        private Accuracy accuracy;
        @SerializedName("quality")
        private Quality quality;

        public QualityWidth getQualityWidth() {
            return qualityWidth;
        }

        public void setQualityWidth(QualityWidth qualityWidth) {
            this.qualityWidth = qualityWidth;
        }

        public AccuracyWidth getAccuracyWidth() {
            return accuracyWidth;
        }

        public void setAccuracyWidth(AccuracyWidth accuracyWidth) {
            this.accuracyWidth = accuracyWidth;
        }

        public Accuracy getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(Accuracy accuracy) {
            this.accuracy = accuracy;
        }

        public Quality getQuality() {
            return quality;
        }

        public void setQuality(Quality quality) {
            this.quality = quality;
        }

        public static class QualityWidth {
            @SerializedName("one_star_rank")
            private String oneStarRank;
            @SerializedName("four_star_rank")
            private String fourStarRank;
            @SerializedName("two_star_rank")
            private String twoStarRank;
            @SerializedName("five_star_rank")
            private String fiveStarRank;
            @SerializedName("three_star_rank")
            private String threeStarRank;

            public String getOneStarRank() {
                return oneStarRank;
            }

            public void setOneStarRank(String oneStarRank) {
                this.oneStarRank = oneStarRank;
            }

            public String getFourStarRank() {
                return fourStarRank;
            }

            public void setFourStarRank(String fourStarRank) {
                this.fourStarRank = fourStarRank;
            }

            public String getTwoStarRank() {
                return twoStarRank;
            }

            public void setTwoStarRank(String twoStarRank) {
                this.twoStarRank = twoStarRank;
            }

            public String getFiveStarRank() {
                return fiveStarRank;
            }

            public void setFiveStarRank(String fiveStarRank) {
                this.fiveStarRank = fiveStarRank;
            }

            public String getThreeStarRank() {
                return threeStarRank;
            }

            public void setThreeStarRank(String threeStarRank) {
                this.threeStarRank = threeStarRank;
            }
        }

        public static class AccuracyWidth {
            @SerializedName("five_star_rank")
            private String fiveStarRank;
            @SerializedName("two_star_rank")
            private String twoStarRank;
            @SerializedName("three_star_rank")
            private String threeStarRank;
            @SerializedName("four_star_rank")
            private String fourStarRank;
            @SerializedName("one_star_rank")
            private String oneStarRank;

            public String getFiveStarRank() {
                return fiveStarRank;
            }

            public void setFiveStarRank(String fiveStarRank) {
                this.fiveStarRank = fiveStarRank;
            }

            public String getTwoStarRank() {
                return twoStarRank;
            }

            public void setTwoStarRank(String twoStarRank) {
                this.twoStarRank = twoStarRank;
            }

            public String getThreeStarRank() {
                return threeStarRank;
            }

            public void setThreeStarRank(String threeStarRank) {
                this.threeStarRank = threeStarRank;
            }

            public String getFourStarRank() {
                return fourStarRank;
            }

            public void setFourStarRank(String fourStarRank) {
                this.fourStarRank = fourStarRank;
            }

            public String getOneStarRank() {
                return oneStarRank;
            }

            public void setOneStarRank(String oneStarRank) {
                this.oneStarRank = oneStarRank;
            }
        }

        public static class Accuracy {
            @SerializedName("two_star_rank")
            private String twoStarRank;
            @SerializedName("average")
            private String average;
            @SerializedName("one_star_rank")
            private String oneStarRank;
            @SerializedName("rating_star")
            private String ratingStar;
            @SerializedName("five_star_rank")
            private String fiveStarRank;
            @SerializedName("three_star_rank")
            private String threeStarRank;
            @SerializedName("four_star_rank")
            private String fourStarRank;
            @SerializedName("count_total")
            private String countTotal;

            public String getTwoStarRank() {
                return twoStarRank;
            }

            public void setTwoStarRank(String twoStarRank) {
                this.twoStarRank = twoStarRank;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }

            public String getOneStarRank() {
                return oneStarRank;
            }

            public void setOneStarRank(String oneStarRank) {
                this.oneStarRank = oneStarRank;
            }

            public String getRatingStar() {
                return ratingStar;
            }

            public void setRatingStar(String ratingStar) {
                this.ratingStar = ratingStar;
            }

            public String getFiveStarRank() {
                return fiveStarRank;
            }

            public void setFiveStarRank(String fiveStarRank) {
                this.fiveStarRank = fiveStarRank;
            }

            public String getThreeStarRank() {
                return threeStarRank;
            }

            public void setThreeStarRank(String threeStarRank) {
                this.threeStarRank = threeStarRank;
            }

            public String getFourStarRank() {
                return fourStarRank;
            }

            public void setFourStarRank(String fourStarRank) {
                this.fourStarRank = fourStarRank;
            }

            public String getCountTotal() {
                return countTotal;
            }

            public void setCountTotal(String countTotal) {
                this.countTotal = countTotal;
            }
        }

        public static class Quality {
            @SerializedName("count_total")
            private String countTotal;
            @SerializedName("three_star_rank")
            private String threeStarRank;
            @SerializedName("rating_star")
            private String ratingStar;
            @SerializedName("five_star_rank")
            private String fiveStarRank;
            @SerializedName("four_star_rank")
            private String fourStarRank;
            @SerializedName("one_star_rank")
            private String oneStarRank;
            @SerializedName("average")
            private String average;
            @SerializedName("two_star_rank")
            private String twoStarRank;

            public String getCountTotal() {
                return countTotal;
            }

            public void setCountTotal(String countTotal) {
                this.countTotal = countTotal;
            }

            public String getThreeStarRank() {
                return threeStarRank;
            }

            public void setThreeStarRank(String threeStarRank) {
                this.threeStarRank = threeStarRank;
            }

            public String getRatingStar() {
                return ratingStar;
            }

            public void setRatingStar(String ratingStar) {
                this.ratingStar = ratingStar;
            }

            public String getFiveStarRank() {
                return fiveStarRank;
            }

            public void setFiveStarRank(String fiveStarRank) {
                this.fiveStarRank = fiveStarRank;
            }

            public String getFourStarRank() {
                return fourStarRank;
            }

            public void setFourStarRank(String fourStarRank) {
                this.fourStarRank = fourStarRank;
            }

            public String getOneStarRank() {
                return oneStarRank;
            }

            public void setOneStarRank(String oneStarRank) {
                this.oneStarRank = oneStarRank;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }

            public String getTwoStarRank() {
                return twoStarRank;
            }

            public void setTwoStarRank(String twoStarRank) {
                this.twoStarRank = twoStarRank;
            }
        }
    }
}
