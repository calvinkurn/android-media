package com.tokopedia.tokopedianow.home.di.component;

import android.content.Context;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper;
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository;
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase;
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper;
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase;
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics;
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase;
import com.tokopedia.tokopedianow.home.di.module.HomeModule;
import com.tokopedia.tokopedianow.home.di.module.TokoMartHomeModule_ProvideGrqphqlRepositoryFactory;
import com.tokopedia.tokopedianow.home.di.module.TokoMartHomeModule_ProvideHomeAnalyticFactory;
import com.tokopedia.tokopedianow.home.di.module.TokoMartHomeModule_ProvideUserSessionFactory;
import com.tokopedia.tokopedianow.home.di.module.HomeUseCaseModule;
import com.tokopedia.tokopedianow.home.di.module.TokoMartHomeUseCaseModule_ProvideGetHomeLayoutUseCaseFactory;
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase;
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase;
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase;
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase;
import com.tokopedia.tokopedianow.home.presentation.fragment.HomeFragment;
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoMartHomeFragment_MembersInjector;
import com.tokopedia.tokopedianow.home.presentation.viewmodel.HomeViewModel;
import com.tokopedia.user.session.UserSessionInterface;
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
public final class DaggerHomeComponent implements HomeComponent {
  private final BaseAppComponent baseAppComponent;

  private Provider<Context> getContextProvider;

  private Provider<UserSessionInterface> provideUserSessionProvider;

  private Provider<GraphqlRepository> provideGrqphqlRepositoryProvider;

  private Provider<GetHomeLayoutListUseCase> provideGetHomeLayoutUseCaseProvider;

  private Provider<HomeAnalytics> provideHomeAnalyticProvider;

  private DaggerHomeComponent(HomeModule homeModuleParam,
                              HomeUseCaseModule homeUseCaseModuleParam,
                              BaseAppComponent baseAppComponentParam) {
    this.baseAppComponent = baseAppComponentParam;
    initialize(homeModuleParam, homeUseCaseModuleParam, baseAppComponentParam);
  }

  public static Builder builder() {
    return new Builder();
  }

  private GetHomeLayoutDataUseCase getHomeLayoutDataUseCase() {
    return new GetHomeLayoutDataUseCase(provideGrqphqlRepositoryProvider.get());
  }

  private GetCategoryListUseCase getCategoryListUseCase() {
    return new GetCategoryListUseCase(provideGrqphqlRepositoryProvider.get());
  }

  private GetKeywordSearchUseCase getKeywordSearchUseCase() {
    return new GetKeywordSearchUseCase(provideGrqphqlRepositoryProvider.get());
  }

  private GetTickerUseCase getTickerUseCase() {
    return new GetTickerUseCase(provideGrqphqlRepositoryProvider.get());
  }

  private ChosenAddressRequestHelper chosenAddressRequestHelper() {
    return new ChosenAddressRequestHelper(Preconditions.checkNotNullFromComponent(baseAppComponent.getContext()));
  }

  private GetMiniCartListSimplifiedUseCase getMiniCartListSimplifiedUseCase() {
    return new GetMiniCartListSimplifiedUseCase(Preconditions.checkNotNullFromComponent(baseAppComponent.graphqlRepository()), new MiniCartSimplifiedMapper(), chosenAddressRequestHelper());
  }

  private ChooseAddressRepository chooseAddressRepository() {
    return new ChooseAddressRepository(Preconditions.checkNotNullFromComponent(baseAppComponent.graphqlRepository()));
  }

  private GetChosenAddressWarehouseLocUseCase getChosenAddressWarehouseLocUseCase() {
    return new GetChosenAddressWarehouseLocUseCase(chooseAddressRepository());
  }

  private HomeViewModel tokoMartHomeViewModel() {
    return new HomeViewModel(provideGetHomeLayoutUseCaseProvider.get(), getHomeLayoutDataUseCase(), getCategoryListUseCase(), getKeywordSearchUseCase(), getTickerUseCase(), getMiniCartListSimplifiedUseCase(), getChosenAddressWarehouseLocUseCase(), Preconditions.checkNotNullFromComponent(baseAppComponent.coroutineDispatchers()));
  }

  @SuppressWarnings("unchecked")
  private void initialize(final HomeModule homeModuleParam,
      final HomeUseCaseModule homeUseCaseModuleParam,
      final BaseAppComponent baseAppComponentParam) {
    this.getContextProvider = new com_tokopedia_abstraction_common_di_component_BaseAppComponent_getContext(baseAppComponentParam);
    this.provideUserSessionProvider = DoubleCheck.provider(TokoMartHomeModule_ProvideUserSessionFactory.create(homeModuleParam, getContextProvider));
    this.provideGrqphqlRepositoryProvider = DoubleCheck.provider(TokoMartHomeModule_ProvideGrqphqlRepositoryFactory.create(homeModuleParam));
    this.provideGetHomeLayoutUseCaseProvider = DoubleCheck.provider(TokoMartHomeUseCaseModule_ProvideGetHomeLayoutUseCaseFactory.create(homeUseCaseModuleParam, provideGrqphqlRepositoryProvider));
    this.provideHomeAnalyticProvider = DoubleCheck.provider(TokoMartHomeModule_ProvideHomeAnalyticFactory.create(homeModuleParam));
  }

  @Override
  public void inject(HomeFragment fragment) {
    injectTokoMartHomeFragment(fragment);
  }

  private HomeFragment injectTokoMartHomeFragment(HomeFragment instance) {
    TokoMartHomeFragment_MembersInjector.injectUserSession(instance, provideUserSessionProvider.get());
    TokoMartHomeFragment_MembersInjector.injectViewModel(instance, tokoMartHomeViewModel());
    TokoMartHomeFragment_MembersInjector.injectAnalytics(instance, provideHomeAnalyticProvider.get());
    return instance;
  }

  public static final class Builder {
    private HomeModule homeModule;

    private HomeUseCaseModule homeUseCaseModule;

    private BaseAppComponent baseAppComponent;

    private Builder() {
    }

    public Builder tokoMartHomeModule(HomeModule homeModule) {
      this.homeModule = Preconditions.checkNotNull(homeModule);
      return this;
    }

    public Builder tokoMartHomeUseCaseModule(HomeUseCaseModule homeUseCaseModule) {
      this.homeUseCaseModule = Preconditions.checkNotNull(homeUseCaseModule);
      return this;
    }

    public Builder baseAppComponent(BaseAppComponent baseAppComponent) {
      this.baseAppComponent = Preconditions.checkNotNull(baseAppComponent);
      return this;
    }

    public HomeComponent build() {
      if (homeModule == null) {
        this.homeModule = new HomeModule();
      }
      if (homeUseCaseModule == null) {
        this.homeUseCaseModule = new HomeUseCaseModule();
      }
      Preconditions.checkBuilderRequirement(baseAppComponent, BaseAppComponent.class);
      return new DaggerHomeComponent(homeModule, homeUseCaseModule, baseAppComponent);
    }
  }

  private static class com_tokopedia_abstraction_common_di_component_BaseAppComponent_getContext implements Provider<Context> {
    private final BaseAppComponent baseAppComponent;

    com_tokopedia_abstraction_common_di_component_BaseAppComponent_getContext(
        BaseAppComponent baseAppComponent) {
      this.baseAppComponent = baseAppComponent;
    }

    @Override
    public Context get() {
      return Preconditions.checkNotNullFromComponent(baseAppComponent.getContext());
    }
  }
}
