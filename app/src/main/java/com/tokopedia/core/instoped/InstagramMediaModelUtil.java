package com.tokopedia.core.instoped;

import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.instoped.model.InstagramMediaModelParc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 6/22/16.
 */
public class InstagramMediaModelUtil {
    public static InstagramMediaModel convertTo(InstagramMediaModelParc o){
        InstagramMediaModel i = new InstagramMediaModel();
        i.filter = o.filter;
        i.link = o.link;
        i.thumbnail = o. thumbnail;
        i.standardResolution = o.standardResolution;
        i.captionText = o.captionText;

        return i;
    }

    public static InstagramMediaModelParc convertTo(InstagramMediaModel i){
        InstagramMediaModelParc instagramMediaModelParc = new InstagramMediaModelParc();
        instagramMediaModelParc.captionText  = i.captionText;
        instagramMediaModelParc.standardResolution = i.standardResolution;
        instagramMediaModelParc.thumbnail = i.thumbnail;
        instagramMediaModelParc.link = i.link;
        instagramMediaModelParc.filter = i.filter;

        return instagramMediaModelParc;
    }
    public static List<InstagramMediaModelParc> convertTo(List<InstagramMediaModel> models){
        List<InstagramMediaModelParc> parcs = new ArrayList<InstagramMediaModelParc>();
        for (InstagramMediaModel instagramMediaModel :
                models) {
            parcs.add(convertTo(instagramMediaModel));
        }
        return parcs;
    }
}
