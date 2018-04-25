package com.tokopedia.tkpd.campaign.domain;

import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignDataRepository {
    Observable<CampaignResponseEntity> getCompaignData(HashMap<String, Object> params);
    Observable<CampaignResponseEntity> getCampaignFromShake(HashMap<String, Object> params);
    Observable<CampaignResponseEntity> getCampaignFromShakeAudio(HashMap<String, RequestBody> params, MultipartBody.Part audioFile);
}
