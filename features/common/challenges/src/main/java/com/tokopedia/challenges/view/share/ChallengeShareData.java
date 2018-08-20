//package com.tokopedia.challenges.view.share;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//public class ChallengeShareData implements Parcelable {
//    public String url;
//    public String title;
//    public String ogUrl;
//    public String ogTitle;
//    public String ogImageUrl;
//    public String id;
//    public String deepLink;
//
//    public ChallengeShareData() {
//
//    }
//
//    protected ChallengeShareData(Parcel in) {
//        url = in.readString();
//        title = in.readString();
//        ogUrl = in.readString();
//        ogTitle = in.readString();
//        ogImageUrl = in.readString();
//        id = in.readString();
//        deepLink = in.readString();
//    }
//
//    public final Creator<ChallengeShareData> CREATOR = new Creator<ChallengeShareData>() {
//        @Override
//        public ChallengeShareData createFromParcel(Parcel in) {
//            return new ChallengeShareData(in);
//        }
//
//        @Override
//        public ChallengeShareData[] newArray(int size) {
//            return new ChallengeShareData[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(url);
//        dest.writeString(title);
//        dest.writeString(ogUrl);
//        dest.writeString(ogTitle);
//        dest.writeString(ogImageUrl);
//        dest.writeString(id);
//        dest.writeString(deepLink);
//    }
//
//
//    public static class Builder {
//        private String url;
//        private String title;
//        private String ogUrl;
//        private String ogTitle;
//        private String ogImageUrl;
//        private String id;
//        private String deepLink;
//
//        private Builder() {
//        }
//
//        public static ChallengeShareData.Builder aChallengeShareData() {
//            return new ChallengeShareData.Builder();
//        }
//
//        public ChallengeShareData.Builder setUrl(String url) {
//            this.url = url;
//            return this;
//        }
//
//        public ChallengeShareData.Builder setTitle(String title) {
//            this.title = title;
//            return this;
//        }
//
//        public ChallengeShareData.Builder setId(String id) {
//            this.id = id;
//            return this;
//        }
//
//
//        public ChallengeShareData.Builder setOgUrl(String ogUrl) {
//            this.ogUrl = ogUrl;
//            return this;
//        }
//
//        public ChallengeShareData.Builder setOgTitle(String ogTitle) {
//            this.ogTitle = ogTitle;
//            return this;
//        }
//
//        public ChallengeShareData.Builder setOgImageUrl(String ogImageUrl) {
//            this.ogImageUrl = ogImageUrl;
//            return this;
//        }
//
//        public ChallengeShareData.Builder setdeepLink(String deepLink) {
//            this.deepLink = deepLink;
//            return this;
//        }
//
//
//        public ChallengeShareData build() {
//            ChallengeShareData challengeShareData = new ChallengeShareData();
//            challengeShareData.se
//            ChallengeShareData.setPrice(price);
//            ChallengeShareData.setUri(uri);
//            ChallengeShareData.setDescription(description);
//            ChallengeShareData.setImgUri(imgUri);
//            ChallengeShareData.setType(type);
//            ChallengeShareData.setTextContent(textContent);
//            ChallengeShareData.setSource(source);
//            ChallengeShareData.setId(id);
//            ChallengeShareData.setShareUrl(shareUrl);
//            ChallengeShareData.setPathSticker(pathSticker);
//            ChallengeShareData.setOgUrl(ogUrl);
//            ChallengeShareData.setPathSticker(ogTitle);
//            ChallengeShareData.setPathSticker(ogDescription);
//            ChallengeShareData.setPathSticker(ogImageUrl);
//            ChallengeShareData.setPathSticker(deepLink);
//            return ChallengeShareData;
//        }
//
//    }
//}
//}
