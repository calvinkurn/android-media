package com.tokopedia.core.share.presenter;


import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.linker.model.LinkerData;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 */
public interface ProductSharePresenter {
    void shareBBM(LinkerData data);

    void shareFb(LinkerData data);

    void shareTwitter(LinkerData data);

    void shareWhatsApp(LinkerData data);

    void shareLine(LinkerData data);

    void sharePinterest(LinkerData data);

    void shareMore(LinkerData data);

    void shareInstagram(LinkerData data);

    void shareGPlus(LinkerData data);

    void shareCopy(LinkerData data);

    void setFacebookCache();

    void shareCategory(LinkerData data, String media);
}
