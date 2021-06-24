package com.tokopedia.tokopedianow.home.presentation.fragment;

import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics;
import com.tokopedia.tokopedianow.home.presentation.viewmodel.HomeViewModel;
import com.tokopedia.user.session.UserSessionInterface;
import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
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
public final class TokoMartHomeFragment_MembersInjector implements MembersInjector<HomeFragment> {
  private final Provider<UserSessionInterface> userSessionProvider;

  private final Provider<HomeViewModel> viewModelProvider;

  private final Provider<HomeAnalytics> analyticsProvider;

  public TokoMartHomeFragment_MembersInjector(Provider<UserSessionInterface> userSessionProvider,
      Provider<HomeViewModel> viewModelProvider,
      Provider<HomeAnalytics> analyticsProvider) {
    this.userSessionProvider = userSessionProvider;
    this.viewModelProvider = viewModelProvider;
    this.analyticsProvider = analyticsProvider;
  }

  public static MembersInjector<HomeFragment> create(
      Provider<UserSessionInterface> userSessionProvider,
      Provider<HomeViewModel> viewModelProvider,
      Provider<HomeAnalytics> analyticsProvider) {
    return new TokoMartHomeFragment_MembersInjector(userSessionProvider, viewModelProvider, analyticsProvider);
  }

  @Override
  public void injectMembers(HomeFragment instance) {
    injectUserSession(instance, userSessionProvider.get());
    injectViewModel(instance, viewModelProvider.get());
    injectAnalytics(instance, analyticsProvider.get());
  }

  @InjectedFieldSignature("com.tokopedia.tokopedianow.home.presentation.fragment.TokoMartHomeFragment.userSession")
  public static void injectUserSession(HomeFragment instance,
                                       UserSessionInterface userSession) {
    instance.userSession = userSession;
  }

  @InjectedFieldSignature("com.tokopedia.tokopedianow.home.presentation.fragment.TokoMartHomeFragment.viewModel")
  public static void injectViewModel(HomeFragment instance,
                                     HomeViewModel viewModel) {
    instance.viewModel = viewModel;
  }

  @InjectedFieldSignature("com.tokopedia.tokopedianow.home.presentation.fragment.TokoMartHomeFragment.analytics")
  public static void injectAnalytics(HomeFragment instance, HomeAnalytics analytics) {
    instance.analytics = analytics;
  }
}
