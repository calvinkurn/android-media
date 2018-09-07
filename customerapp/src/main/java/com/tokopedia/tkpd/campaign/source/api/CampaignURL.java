package com.tokopedia.tkpd.campaign.source.api;

import com.tokopedia.tkpd.ConsumerAppBaseUrl;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignURL {
    String BASE_URL = ConsumerAppBaseUrl.TOKO_CAMPAIGN_URL;

    String BARCODE_CAMPAIGN = "campaign/qr/verify";
    String SHAKE_CAMPAIGN = "campaign/av/verify";

}
