package com.tokopedia.tkpd.home.facade;

import android.content.Context;
import android.os.Parcelable;

import com.tokopedia.core.facade.BaseFacade;
import com.tokopedia.core.network.entity.home.Slide;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 9/10/15.
 */
@Deprecated
public class FacadePromo extends BaseFacade {

    public static class PromoItem implements Parcelable {
        public String imgUrl;
        public String promoUrl;

        public PromoItem() {

        }

        protected PromoItem(android.os.Parcel in) {
            imgUrl = in.readString();
            promoUrl = in.readString();
        }

        public static final Creator<PromoItem> CREATOR = new Creator<PromoItem>() {
            @Override
            public PromoItem createFromParcel(android.os.Parcel in) {
                return new PromoItem(in);
            }

            @Override
            public PromoItem[] newArray(int size) {
                return new PromoItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel parcel, int i) {
            parcel.writeString(imgUrl);
            parcel.writeString(promoUrl);
        }
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
        p.imgUrl = s.getImageUrl();
        p.promoUrl = s.getRedirectUrl();
        return p;
    }
}
