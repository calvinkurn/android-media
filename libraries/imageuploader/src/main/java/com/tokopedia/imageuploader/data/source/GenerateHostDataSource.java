package com.tokopedia.imageuploader.data.source;

import com.tokopedia.imageuploader.data.entity.GenerateHost;

import rx.Observable;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class GenerateHostDataSource {

    private final GenerateHostCloud generateHostCloud;

    public GenerateHostDataSource(GenerateHostCloud generateHostCloud) {
        this.generateHostCloud = generateHostCloud;
    }

    public Observable<GenerateHost> generateHost() {
        return generateHostCloud.generateHost();
    }
}
