package com.tokopedia.tkpd.campaign.domain.audio;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;

import rx.Observable;

/**
 * Created by sandeepgoyal on 25/01/18.
 */

public class PostAudioDataUseCase extends UseCase<CampaignResponseEntity> {


    public static final String AUDIO_PATH = "tkp_audio_path";


    private final CampaignDataRepository campaignDataRepository;


    public PostAudioDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CampaignDataRepository campaignDataRepository) {
        super(threadExecutor,postExecutionThread);
        this.campaignDataRepository = campaignDataRepository;
    }
    @Override
    public Observable<CampaignResponseEntity> createObservable(RequestParams requestParams) {
        return campaignDataRepository.getCampaignFromAudio(requestParams.getParameters());
    }
}
