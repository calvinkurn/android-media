package com.tokopedia.product.edit.data.repository;

import com.tokopedia.product.edit.data.mapper.GenerateHostMapper;
import com.tokopedia.product.edit.data.source.GenerateHostDataSource;
import com.tokopedia.product.common.domain.repository.GenerateHostRepository;
import com.tokopedia.product.common.domain.model.GenerateHostDomainModel;

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
