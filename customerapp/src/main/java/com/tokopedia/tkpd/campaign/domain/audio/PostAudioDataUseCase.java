package com.tokopedia.tkpd.campaign.domain.audio;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.inboxchat.util.ImageUploadHandlerChat;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;
import com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by sandeepgoyal on 25/01/18.
 */

public class PostAudioDataUseCase extends ShakeUseCase {


    public static final String AUDIO_PATH = "tkp_audio_path";


    private final CampaignDataRepository campaignDataRepository;


    public PostAudioDataUseCase(CampaignDataRepository campaignDataRepository) {
        super(campaignDataRepository);
        this.campaignDataRepository = campaignDataRepository;
    }
    public Observable<CampaignResponseEntity> createObservable(RequestParams requestParams) {

        return campaignDataRepository.getCampaignFromShakeAudio(generateRequestBody(requestParams),generateRequestAudio(requestParams));
    }

    protected HashMap<String, RequestBody> generateRequestBody(RequestParams requestParams) {

        RequestBody isAudio = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(IS_AUDIO,
                        "false"));
        RequestBody screenName = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(SCREEN_NAME,
                        "false"));
        HashMap<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(IS_AUDIO, isAudio);
        requestBodyMap.put(SCREEN_NAME,screenName);
        return requestBodyMap;
    }

     private MultipartBody.Part generateRequestAudio(RequestParams requestParams) {

         File file = new File(requestParams.getString(AUDIO_PATH, ""));
         RequestBody requestBody = RequestBody.create(MediaType.parse("audio/wav"), file);

         return MultipartBody.Part.createFormData("tkp_file", file.getName(), requestBody);

     }

}
