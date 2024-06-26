package com.tokopedia.imageuploader.data;

import com.tokopedia.imageuploader.data.mapper.GenerateHostMapper;
import com.tokopedia.imageuploader.data.source.GenerateHostDataSource;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.model.GenerateHostDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class GenerateHostRepositoryImpl implements GenerateHostRepository {
    private final GenerateHostDataSource generateHostDataSource;

    public GenerateHostRepositoryImpl(GenerateHostDataSource generateHostDataSource) {
        this.generateHostDataSource = generateHostDataSource;
    }

    @Override
    public Observable<GenerateHostDomainModel> generateHost() {
        return generateHostDataSource.generateHost()
                .map(new GenerateHostMapper());
    }
}
