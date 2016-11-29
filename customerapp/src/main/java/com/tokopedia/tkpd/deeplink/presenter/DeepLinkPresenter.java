package com.tokopedia.tkpd.deeplink.presenter;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public interface DeepLinkPresenter {

    void processDeepLinkAction(Uri uri);

    void processAFlistener();

    void sendCampaignGTM(String campaignUri, String screenName);
}
