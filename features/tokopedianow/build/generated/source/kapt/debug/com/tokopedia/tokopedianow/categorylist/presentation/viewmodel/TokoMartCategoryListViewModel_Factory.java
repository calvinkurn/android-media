package com.tokopedia.tokopedianow.categorylist.presentation.viewmodel;

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase;
import dagger.internal.Factory;
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
public final class TokoMartCategoryListViewModel_Factory implements Factory<CategoryListViewModel> {
  private final Provider<GetCategoryListUseCase> getCategoryListUseCaseProvider;

  private final Provider<CoroutineDispatchers> dispatchersProvider;

  public TokoMartCategoryListViewModel_Factory(
      Provider<GetCategoryListUseCase> getCategoryListUseCaseProvider,
      Provider<CoroutineDispatchers> dispatchersProvider) {
    this.getCategoryListUseCaseProvider = getCategoryListUseCaseProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public CategoryListViewModel get() {
    return newInstance(getCategoryListUseCaseProvider.get(), dispatchersProvider.get());
  }

  public static TokoMartCategoryListViewModel_Factory create(
      Provider<GetCategoryListUseCase> getCategoryListUseCaseProvider,
      Provider<CoroutineDispatchers> dispatchersProvider) {
    return new TokoMartCategoryListViewModel_Factory(getCategoryListUseCaseProvider, dispatchersProvider);
  }

  public static CategoryListViewModel newInstance(
      GetCategoryListUseCase getCategoryListUseCase, CoroutineDispatchers dispatchers) {
    return new CategoryListViewModel(getCategoryListUseCase, dispatchers);
  }
}
