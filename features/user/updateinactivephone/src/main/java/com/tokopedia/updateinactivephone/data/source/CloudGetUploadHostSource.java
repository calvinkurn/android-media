package com.tokopedia.updateinactivephone.data.source;

        import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
        import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper;
        import com.tokopedia.updateinactivephone.data.network.service.UploadImageService;
        import com.tokopedia.updateinactivephone.model.request.UploadHostModel;

        import rx.Observable;

public class CloudGetUploadHostSource {

    private final UploadImageService accountsService;
    private UploadHostMapper getUploadHostMapper;

    public CloudGetUploadHostSource(UploadImageService accountsService,
                                    UploadHostMapper getUploadHostMapper) {
        this.accountsService = accountsService;
        this.getUploadHostMapper = getUploadHostMapper;
    }

    public Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().getUploadHost(params)
                .map(getUploadHostMapper);
    }


}
