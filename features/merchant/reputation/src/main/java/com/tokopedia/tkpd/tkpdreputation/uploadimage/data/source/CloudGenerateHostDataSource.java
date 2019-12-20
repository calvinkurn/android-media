package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class CloudGenerateHostDataSource {

    private final GenerateHostActService generateHostActService;
    private final GenerateHostMapper generateHostMapper;
    private UserSessionInterface userSession;

    public CloudGenerateHostDataSource(GenerateHostActService generateHostActService,
                                       GenerateHostMapper generateHostMapper,
                                       UserSessionInterface userSession) {
        this.generateHostActService = generateHostActService;
        this.generateHostMapper = generateHostMapper;
        this.userSession = userSession;
    }


    public Observable<GenerateHostDomain> generateHost(RequestParams parameters) {
        return generateHostActService.getApi()
                .generateHost4(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        parameters.getParamsAllValueInString()
                ))
                .map(generateHostMapper);
    }
}
