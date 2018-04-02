package com.tokopedia.tkpd.campaign.domain.shake;

import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public class ShakeUseCase extends UseCase<CampaignResponseEntity> {

    public static final String IS_AUDIO ="is_audio";
    private final CampaignDataRepository campaignDataRepository;

    public ShakeUseCase(CampaignDataRepository campaignDataRepository) {
        this.campaignDataRepository = campaignDataRepository;
    }
    @Override
    public Observable<CampaignResponseEntity> createObservable(RequestParams requestParams) {

        return campaignDataRepository.getCampaignFromShake(generateRequestBody(requestParams));
    }

    protected HashMap<String, RequestBody> generateRequestBody(RequestParams requestParams) {

        RequestBody isAudio = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(IS_AUDIO,
                        "false"));
        HashMap<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(IS_AUDIO, isAudio);
        return requestBodyMap;
    }

    }
