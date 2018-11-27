package com.tokopedia.updateinactivephone.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper;
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;

import rx.Observable;

public class CloudGetUploadHostSource {

    private final UploadImageService uploadHostService;
    private UploadHostMapper getUploadHostMapper;

    public CloudGetUploadHostSource(UploadImageService uploadImageService,
                                    UploadHostMapper getUploadHostMapper) {
        this.getUploadHostMapper = getUploadHostMapper;
        this.uploadHostService = uploadImageService;
    }

    public Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> params) {
        return uploadHostService.getApi().getUploadHost(params)
                .map(getUploadHostMapper);
    }


}
