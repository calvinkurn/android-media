package com.tokopedia.home.account.presentation.view.buyercardview;

/**
 * @author okasurya on 8/29/18.
 */
public class BuyerCard {
    private String avatar;
    private int progress;
    private String username;
    private String tokopointAmount;
    private int couponAmount;

    public BuyerCard() {

    }

    BuyerCard(String avatar, String username, int progress, String tokopoint, int coupon) {
        this.avatar = avatar;
        this.username = username;
        this.progress = progress;
        this.tokopointAmount = tokopoint;
        this.couponAmount = coupon;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokopointAmount() {
        return tokopointAmount;
    }

    public void setTokopointAmount(String tokopointAmount) {
        this.tokopointAmount = tokopointAmount;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }


    public static class Builder {
        private String avatar;
        private String username;
        private int progress;
        private String tokopoint;
        private int coupon;

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder progress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder tokopoint(String tokopoint) {
            this.tokopoint = tokopoint;
            return this;
        }

        public Builder coupons(int coupon) {
            this.coupon = coupon;
            return this;
        }

        public BuyerCard build() {
            return new BuyerCard(avatar, username, progress, tokopoint, coupon);
        }
    }
}
