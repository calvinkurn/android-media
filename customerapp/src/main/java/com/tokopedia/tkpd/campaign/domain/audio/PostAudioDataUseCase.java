package com.tokopedia.tkpd.campaign.domain.audio;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;
import com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

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
}
