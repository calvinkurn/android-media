package com.tokopedia.discovery.categorynav.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.hades.HadesService;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.discovery.categorynav.data.mapper.CategoryNavigationMapper;
import com.tokopedia.discovery.categorynav.view.CategoryNavigationPresenter;
import com.tokopedia.discovery.categorynav.data.source.CategoryNavigationDataSource;
import com.tokopedia.discovery.categorynav.domain.CategoryNavigationRepository;
import com.tokopedia.discovery.categorynav.data.repository.CategoryNavigationRepositoryImpl;
import com.tokopedia.discovery.categorynav.domain.interactor.GetNavigationCategoryRootUseCase;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationInjector {

    public static CategoryNavigationPresenter getPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        HadesService hadesService = new HadesService();
        HadesApi hadesApi = hadesService.getApi();
        CategoryNavigationMapper mapper = new CategoryNavigationMapper();
        CategoryNavigationDataSource dataSource = new CategoryNavigationDataSource(context,hadesApi, mapper);
        CategoryNavigationRepository repository = new CategoryNavigationRepositoryImpl(dataSource);

        GetNavigationCategoryRootUseCase getNavigationCategoryRootUseCase = new GetNavigationCategoryRootUseCase(
                threadExecutor, postExecutionThread, repository);

        return  new CategoryNavigationPresenter(getNavigationCategoryRootUseCase);

    }
}
