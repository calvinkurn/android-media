package com.tokopedia.tkpd.home.facade;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.facade.BaseFacade;
import com.tokopedia.core.network.entity.home.Slide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 9/10/15.
 */
@Deprecated
public class FacadePromo extends BaseFacade {

    public static class PromoItem implements Parcelable {
        public String imgUrl="";
        public String promoUrl="";
        public String title="";
        public String id="";
        public String appLink="";

        public PromoItem() {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.imgUrl);
            dest.writeString(this.promoUrl);
            dest.writeString(this.title);
            dest.writeString(this.id);
            dest.writeString(this.appLink);
        }

        protected PromoItem(Parcel in) {
            this.imgUrl = in.readString();
            this.promoUrl = in.readString();
            this.title = in.readString();
            this.id = in.readString();
            this.appLink = in.readString();
        }

        public static final Creator<PromoItem> CREATOR = new Creator<PromoItem>() {
            @Override
            public PromoItem createFromParcel(Parcel source) {
                return new PromoItem(source);
            }

            @Override
            public PromoItem[] newArray(int size) {
                return new PromoItem[size];
            }
        };
    }

    public FacadePromo(Context context) {
        super(context);
    }


    public interface GetPromoListener {
        void OnSuccessBanner(List<PromoItem> promoList);

        void OnError();
    }

    public static List<PromoItem> parseTickerList(Slide slide){
        List<PromoItem> promoList = new ArrayList<>();
        for(Slide.Slides ss : slide.getData().getSlides()){
            PromoItem p = parseTicker(ss);
            promoList.add(p);
        }
        return promoList;
    }

    public static PromoItem parseTicker(Slide.Slides s){
        PromoItem p = new PromoItem();
        p.id = s.getId();
        p.title = s.getTitle();
        p.imgUrl = s.getImageUrl();
        p.promoUrl = s.getRedirectUrl();
        p.appLink = s.getApplink();
        return p;
    }
}
