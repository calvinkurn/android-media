package com.tokopedia.tokopedianow.home.di.module;

import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics;
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
public final class TokoMartHomeModule_ProvideHomeAnalyticFactory implements Factory<HomeAnalytics> {
  private final HomeModule module;

  public TokoMartHomeModule_ProvideHomeAnalyticFactory(HomeModule module) {
    this.module = module;
  }

  @Override
  public HomeAnalytics get() {
    return provideHomeAnalytic(module);
  }

  public static TokoMartHomeModule_ProvideHomeAnalyticFactory create(HomeModule module) {
    return new TokoMartHomeModule_ProvideHomeAnalyticFactory(module);
  }

  public static HomeAnalytics provideHomeAnalytic(HomeModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideHomeAnalytic());
  }
}
