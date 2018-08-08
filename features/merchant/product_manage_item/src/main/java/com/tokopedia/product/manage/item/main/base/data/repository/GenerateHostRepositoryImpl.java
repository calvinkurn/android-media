package com.tokopedia.product.manage.item.data.repository;

import com.tokopedia.product.manage.item.data.mapper.GenerateHostMapper;
import com.tokopedia.product.manage.item.data.source.GenerateHostDataSource;
import com.tokopedia.product.manage.item.common.domain.repository.GenerateHostRepository;
import com.tokopedia.product.manage.item.common.domain.model.GenerateHostDomainModel;

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
