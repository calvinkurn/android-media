package com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet;

import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics;
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel;
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
public final class TokoMartCategoryListBottomSheet_MembersInjector implements MembersInjector<CategoryListBottomSheet> {
  private final Provider<CategoryListViewModel> viewModelProvider;

  private final Provider<CategoryListAnalytics> analyticsProvider;

  public TokoMartCategoryListBottomSheet_MembersInjector(
      Provider<CategoryListViewModel> viewModelProvider,
      Provider<CategoryListAnalytics> analyticsProvider) {
    this.viewModelProvider = viewModelProvider;
    this.analyticsProvider = analyticsProvider;
  }

  public static MembersInjector<CategoryListBottomSheet> create(
      Provider<CategoryListViewModel> viewModelProvider,
      Provider<CategoryListAnalytics> analyticsProvider) {
    return new TokoMartCategoryListBottomSheet_MembersInjector(viewModelProvider, analyticsProvider);
  }

  @Override
  public void injectMembers(CategoryListBottomSheet instance) {
    injectViewModel(instance, viewModelProvider.get());
    injectAnalytics(instance, analyticsProvider.get());
  }

  @InjectedFieldSignature("com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet.TokoMartCategoryListBottomSheet.viewModel")
  public static void injectViewModel(CategoryListBottomSheet instance,
                                     CategoryListViewModel viewModel) {
    instance.viewModel = viewModel;
  }

  @InjectedFieldSignature("com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet.TokoMartCategoryListBottomSheet.analytics")
  public static void injectAnalytics(CategoryListBottomSheet instance,
                                     CategoryListAnalytics analytics) {
    instance.analytics = analytics;
  }
}
