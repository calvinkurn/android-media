package com.tokopedia.tokopedianow.home.di.module;

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class TokoMartHomeModule_ProvideGrqphqlRepositoryFactory implements Factory<GraphqlRepository> {
  private final HomeModule module;

  public TokoMartHomeModule_ProvideGrqphqlRepositoryFactory(HomeModule module) {
    this.module = module;
  }

  @Override
  public GraphqlRepository get() {
    return provideGrqphqlRepository(module);
  }

  public static TokoMartHomeModule_ProvideGrqphqlRepositoryFactory create(
          HomeModule module) {
    return new TokoMartHomeModule_ProvideGrqphqlRepositoryFactory(module);
  }

  public static GraphqlRepository provideGrqphqlRepository(HomeModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideGrqphqlRepository());
  }
}
