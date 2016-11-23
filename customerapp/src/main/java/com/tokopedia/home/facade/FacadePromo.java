package com.tokopedia.home.facade;

import android.content.Context;

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

    @Parcel
    public static class PromoItem {
        public String imgUrl;
        public String promoUrl;

        public PromoItem() {

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
