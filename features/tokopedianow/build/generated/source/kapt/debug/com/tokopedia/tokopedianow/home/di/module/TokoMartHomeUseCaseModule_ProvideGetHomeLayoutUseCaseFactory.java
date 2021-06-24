package com.tokopedia.tokopedianow.home.di.module;

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase;
import dagger.internal.Factory;
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
public final class TokoMartHomeUseCaseModule_ProvideGetHomeLayoutUseCaseFactory implements Factory<GetHomeLayoutListUseCase> {
  private final HomeUseCaseModule module;

  private final Provider<GraphqlRepository> graphqlRepositoryProvider;

  public TokoMartHomeUseCaseModule_ProvideGetHomeLayoutUseCaseFactory(
          HomeUseCaseModule module, Provider<GraphqlRepository> graphqlRepositoryProvider) {
    this.module = module;
    this.graphqlRepositoryProvider = graphqlRepositoryProvider;
  }

  @Override
  public GetHomeLayoutListUseCase get() {
    return provideGetHomeLayoutUseCase(module, graphqlRepositoryProvider.get());
  }

  public static TokoMartHomeUseCaseModule_ProvideGetHomeLayoutUseCaseFactory create(
          HomeUseCaseModule module, Provider<GraphqlRepository> graphqlRepositoryProvider) {
    return new TokoMartHomeUseCaseModule_ProvideGetHomeLayoutUseCaseFactory(module, graphqlRepositoryProvider);
  }

  public static GetHomeLayoutListUseCase provideGetHomeLayoutUseCase(
          HomeUseCaseModule instance, GraphqlRepository graphqlRepository) {
    return Preconditions.checkNotNullFromProvides(instance.provideGetHomeLayoutUseCase(graphqlRepository));
  }
}
