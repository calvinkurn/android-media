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
@Deprecated
public class ShakeUseCase extends UseCase<CampaignResponseEntity> {

    public static final String IS_AUDIO ="is_audio";
    public static final String PARAM_LATITUDE ="latitude";
    public static final String PARAM_LONGITUDE ="longitude";

    public static final String SCREEN_NAME = "source";
    private final CampaignDataRepository campaignDataRepository;

    public ShakeUseCase(CampaignDataRepository campaignDataRepository) {
        this.campaignDataRepository = campaignDataRepository;
    }
    @Override
    public Observable<CampaignResponseEntity> createObservable(RequestParams requestParams) {

        return campaignDataRepository.getCampaignFromShake(requestParams.getParameters());
    }

    protected HashMap<String, RequestBody> generateRequestBody(RequestParams requestParams) {

        RequestBody isAudio = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(IS_AUDIO,
                        "false"));
        RequestBody screenName = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(SCREEN_NAME,
                        "false"));
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_LATITUDE,
                        "0"));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(PARAM_LONGITUDE,
                        "0"));
        HashMap<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(IS_AUDIO, isAudio);
        requestBodyMap.put(SCREEN_NAME, screenName);
        requestBodyMap.put(PARAM_LATITUDE, latitude);
        requestBodyMap.put(PARAM_LONGITUDE, longitude);
        return requestBodyMap;
    }

    }
