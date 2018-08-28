package com.tokopedia.product.manage.item.main.base.data.repository;

import com.tokopedia.product.manage.item.common.domain.repository.GenerateHostRepository;
import com.tokopedia.product.manage.item.common.domain.model.GenerateHostDomainModel;
import com.tokopedia.product.manage.item.main.base.data.mapper.GenerateHostMapper;
import com.tokopedia.product.manage.item.main.base.data.source.GenerateHostDataSource;

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
