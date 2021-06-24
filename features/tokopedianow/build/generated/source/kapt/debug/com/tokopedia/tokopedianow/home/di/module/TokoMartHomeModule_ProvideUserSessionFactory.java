package com.tokopedia.tokopedianow.home.di.module;

import android.content.Context;
import com.tokopedia.user.session.UserSessionInterface;
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
public final class TokoMartHomeModule_ProvideUserSessionFactory implements Factory<UserSessionInterface> {
  private final HomeModule module;

  private final Provider<Context> contextProvider;

  public TokoMartHomeModule_ProvideUserSessionFactory(HomeModule module,
                                                      Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public UserSessionInterface get() {
    return provideUserSession(module, contextProvider.get());
  }

  public static TokoMartHomeModule_ProvideUserSessionFactory create(HomeModule module,
                                                                    Provider<Context> contextProvider) {
    return new TokoMartHomeModule_ProvideUserSessionFactory(module, contextProvider);
  }

  public static UserSessionInterface provideUserSession(HomeModule instance,
                                                        Context context) {
    return Preconditions.checkNotNullFromProvides(instance.provideUserSession(context));
  }
}
