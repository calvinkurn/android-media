package com.tokopedia.tokopedianow.categorylist.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics;
import com.tokopedia.tokopedianow.categorylist.di.module.CategoryListModule;
import com.tokopedia.tokopedianow.categorylist.di.module.TokoMartCategoryListModule_ProvideCategoryAnalyticFactory;
import com.tokopedia.tokopedianow.categorylist.di.module.TokoMartCategoryListModule_ProvideGrqphqlRepositoryFactory;
import com.tokopedia.tokopedianow.categorylist.di.module.CategoryListUseCaseModule;
import com.tokopedia.tokopedianow.categorylist.di.module.TokoMartCategoryListUseCaseModule_ProvideGetCategoryListUseCaseFactory;
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase;
import com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet.CategoryListBottomSheet;
import com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet.TokoMartCategoryListBottomSheet_MembersInjector;
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerCategoryListComponent implements CategoryListComponent {
  private final BaseAppComponent baseAppComponent;

  private Provider<GraphqlRepository> provideGrqphqlRepositoryProvider;

  private Provider<GetCategoryListUseCase> provideGetCategoryListUseCaseProvider;

  private Provider<CategoryListAnalytics> provideCategoryAnalyticProvider;

  private DaggerCategoryListComponent(
          CategoryListModule categoryListModuleParam,
          CategoryListUseCaseModule categoryListUseCaseModuleParam,
          BaseAppComponent baseAppComponentParam) {
    this.baseAppComponent = baseAppComponentParam;
    initialize(categoryListModuleParam, categoryListUseCaseModuleParam, baseAppComponentParam);
  }

  public static Builder builder() {
    return new Builder();
  }

  private CategoryListViewModel tokoMartCategoryListViewModel() {
    return new CategoryListViewModel(provideGetCategoryListUseCaseProvider.get(), Preconditions.checkNotNullFromComponent(baseAppComponent.coroutineDispatchers()));
  }

  @SuppressWarnings("unchecked")
  private void initialize(final CategoryListModule categoryListModuleParam,
      final CategoryListUseCaseModule categoryListUseCaseModuleParam,
      final BaseAppComponent baseAppComponentParam) {
    this.provideGrqphqlRepositoryProvider = DoubleCheck.provider(TokoMartCategoryListModule_ProvideGrqphqlRepositoryFactory.create(categoryListModuleParam));
    this.provideGetCategoryListUseCaseProvider = DoubleCheck.provider(TokoMartCategoryListUseCaseModule_ProvideGetCategoryListUseCaseFactory.create(categoryListUseCaseModuleParam, provideGrqphqlRepositoryProvider));
    this.provideCategoryAnalyticProvider = DoubleCheck.provider(TokoMartCategoryListModule_ProvideCategoryAnalyticFactory.create(categoryListModuleParam));
  }

  @Override
  public void inject(CategoryListBottomSheet fragment) {
    injectTokoMartCategoryListBottomSheet(fragment);
  }

  private CategoryListBottomSheet injectTokoMartCategoryListBottomSheet(
          CategoryListBottomSheet instance) {
    TokoMartCategoryListBottomSheet_MembersInjector.injectViewModel(instance, tokoMartCategoryListViewModel());
    TokoMartCategoryListBottomSheet_MembersInjector.injectAnalytics(instance, provideCategoryAnalyticProvider.get());
    return instance;
  }

  public static final class Builder {
    private CategoryListModule categoryListModule;

    private CategoryListUseCaseModule categoryListUseCaseModule;

    private BaseAppComponent baseAppComponent;

    private Builder() {
    }

    public Builder tokoMartCategoryListModule(
            CategoryListModule categoryListModule) {
      this.categoryListModule = Preconditions.checkNotNull(categoryListModule);
      return this;
    }

    public Builder tokoMartCategoryListUseCaseModule(
            CategoryListUseCaseModule categoryListUseCaseModule) {
      this.categoryListUseCaseModule = Preconditions.checkNotNull(categoryListUseCaseModule);
      return this;
    }

    public Builder baseAppComponent(BaseAppComponent baseAppComponent) {
      this.baseAppComponent = Preconditions.checkNotNull(baseAppComponent);
      return this;
    }

    public CategoryListComponent build() {
      if (categoryListModule == null) {
        this.categoryListModule = new CategoryListModule();
      }
      if (categoryListUseCaseModule == null) {
        this.categoryListUseCaseModule = new CategoryListUseCaseModule();
      }
      Preconditions.checkBuilderRequirement(baseAppComponent, BaseAppComponent.class);
      return new DaggerCategoryListComponent(categoryListModule, categoryListUseCaseModule, baseAppComponent);
    }
  }
}
