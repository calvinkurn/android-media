package com.tokopedia.tkpd.campaign.source.api;

import com.tokopedia.config.url.TokopediaUrl;
import com.tokopedia.tkpd.ConsumerAppBaseUrl;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignURL {
    String BASE_URL = TokopediaUrl.getInstance().getBOOKING() + "trigger/v1/api/";

    String BARCODE_CAMPAIGN = "campaign/qr/verify";
    String SHAKE_CAMPAIGN = "campaign/av/verify";

}
