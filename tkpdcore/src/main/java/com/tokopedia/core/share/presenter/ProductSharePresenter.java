package com.tokopedia.core.share.presenter;

import com.sromku.simple.fb.SimpleFacebook;
import com.tokopedia.core.product.model.share.ShareData;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 */
public interface ProductSharePresenter {
    void shareBBM(ShareData data);

    void shareFb(SimpleFacebook simpleFacebook, ShareData data);

    void shareTwitter(ShareData data);

    void shareWhatsApp(ShareData data);

    void shareLine(ShareData data);

    void sharePinterest(ShareData data);

    void shareMore(ShareData data);

    void shareInstagram(ShareData data);

    void shareGPlus(ShareData data);

    void shareCopy(ShareData data);
}
