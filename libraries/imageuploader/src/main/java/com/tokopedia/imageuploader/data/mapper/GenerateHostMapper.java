package com.tokopedia.imageuploader.data.mapper;


import com.tokopedia.imageuploader.data.entity.GenerateHost;
import com.tokopedia.imageuploader.domain.model.GenerateHostDomainModel;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class GenerateHostMapper implements Func1<GenerateHost, GenerateHostDomainModel> {

    public GenerateHostMapper() {
    }

    @Override
    public GenerateHostDomainModel call(GenerateHost generateHostModelResponse) {
        GenerateHostDomainModel domainModel = new GenerateHostDomainModel();
        domainModel.setServerId(Integer.parseInt(generateHostModelResponse.getServerId()));
        domainModel.setUrl(generateHostModelResponse.getUploadHost());
        return domainModel;
    }
}
