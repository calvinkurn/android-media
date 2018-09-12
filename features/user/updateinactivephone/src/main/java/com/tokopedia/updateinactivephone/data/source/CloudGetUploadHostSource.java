package com.tokopedia.updateinactivephone.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper;
import com.tokopedia.updateinactivephone.data.network.service.GetUploadHostService;
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;

import rx.Observable;

public class CloudGetUploadHostSource {

//    private final UploadImageService accountsService;
    private final GetUploadHostService uploadHostService;
    private UploadHostMapper getUploadHostMapper;

    public CloudGetUploadHostSource(/*UploadImageService accountsService,*/
                                    GetUploadHostService getUploadHostService,
                                    UploadHostMapper getUploadHostMapper) {
//        this.accountsService = accountsService;
        this.getUploadHostMapper = getUploadHostMapper;
        this.uploadHostService = getUploadHostService;
    }

    public Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> params) {
        return uploadHostService.getApi().getUploadHost(params)
                .map(getUploadHostMapper);
    }


}
