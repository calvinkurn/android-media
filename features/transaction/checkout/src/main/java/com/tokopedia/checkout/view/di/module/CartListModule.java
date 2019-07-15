package com.tokopedia.checkout.view.di.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.common.PromoActionListener;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.feature.cartlist.ActionListener;
import com.tokopedia.checkout.view.feature.cartlist.CartFragment;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.feature.cartlist.CartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListView;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsGqlUseCase;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class, PromoCheckoutModule.class})
public class CartListModule {

    private final ICartListView cartListView;
    private final ActionListener cartActionListener;
    private final PromoActionListener promoActionListener;
    private final CartItemAdapter.ActionListener cartItemActionListener;

    public CartListModule(CartFragment cartFragment) {
        this.cartListView = cartFragment;
        this.cartActionListener = cartFragment;
        this.cartItemActionListener = cartFragment;
        this.promoActionListener = cartFragment;
    }

    @Provides
    @CartListScope
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @CartListScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartListScope
    AddWishListUseCase providesAddWishListUseCase() {
        return new AddWishListUseCase(cartListView.getActivity());
    }

    @Provides
    @CartListScope
    RemoveWishListUseCase providesTkpdRemoveWishListUseCase() {
        return new RemoveWishListUseCase(cartListView.getActivity());
    }

    @Provides
    @CartListScope
    UserSessionInterface provideUserSessionInterface() {
        return new UserSession(cartListView.getActivity());
    }

    @Provides
    @CartListScope
    TopAdsGqlUseCase topAdsUseCase(Context context) {
        return new TopAdsGqlUseCase(context);
    }

    @Provides
    @CartListScope
    CheckPromoStackingCodeUseCase provideCheckPromoStackingCodeUseCase(@ApplicationContext Context context) {
        return new CheckPromoStackingCodeUseCase(context.getResources());
    }

    @Provides
    @CartListScope
    GetRecentViewUseCase provideGetRecentViewUseCase() {
        return new GetRecentViewUseCase(cartListView.getActivity());
    }

    @Provides
    @CartListScope
    GetWishlistUseCase provideGetWishlistUseCase() {
        return new GetWishlistUseCase(cartListView.getActivity());
    }

    @Provides
    @CartListScope
    GraphqlUseCase providesGraphqlUseCase()  {
        return new GraphqlUseCase();
    }

    @Provides
    @Named("recommendationQuery")
    String provideRecommendationRawQuery(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_recommendation_widget);
    }

    @Provides
    @CartListScope
    GetRecommendationUseCase provideGetRecommendationUseCase(@Named("recommendationQuery") String recomQuery,
                                                             GraphqlUseCase graphqlUseCase,
                                                             UserSessionInterface userSessionInterface) {
        return new GetRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface);
    }

    @Provides
    @Named("atcMutation")
    String provideAddToCartMutation(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_add_to_cart);
    }

    @Provides
    @CartListScope
    AddToCartUseCase provideAddToCartUseCase(@Named("atcMutation") String mutation) {
        return new AddToCartUseCase(mutation);
    }

    @Provides
    @CartListScope
    ICartListPresenter provideICartListPresenter(GetCartListUseCase getCartListUseCase,
                                                 DeleteCartListUseCase deleteCartListUseCase,
                                                 UpdateCartUseCase updateCartUseCase,
                                                 ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                                                 CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase,
                                                 CheckPromoStackingCodeMapper checkPromoStackingCodeMapper,
                                                 CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                 CompositeSubscription compositeSubscription,
                                                 CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                                 AddWishListUseCase addWishListUseCase,
                                                 RemoveWishListUseCase removeWishListUseCase,
                                                 UpdateAndReloadCartUseCase updateAndReloadCartUseCase,
                                                 UserSessionInterface userSessionInterface,
                                                 TopAdsGqlUseCase topAdsGqlUseCase,
                                                 ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                                                 GetRecentViewUseCase getRecentViewUseCase,
                                                 GetWishlistUseCase getWishlistUseCase,
                                                 GetRecommendationUseCase getRecommendationUseCase,
                                                 AddToCartUseCase addToCartUseCase) {
        return new CartListPresenter(getCartListUseCase, deleteCartListUseCase,
                updateCartUseCase, resetCartGetCartListUseCase, checkPromoStackingCodeUseCase,
                checkPromoStackingCodeMapper, checkPromoCodeCartListUseCase, compositeSubscription,
                cartApiRequestParamGenerator, addWishListUseCase, removeWishListUseCase,
                updateAndReloadCartUseCase, userSessionInterface, topAdsGqlUseCase,
                clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                getRecommendationUseCase, addToCartUseCase);
    }

    @Provides
    @CartListScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration();
    }

    @Provides
    @CartListScope
    CartAdapter provideCartListAdapter() {
        return new CartAdapter(cartActionListener, promoActionListener, cartItemActionListener);
    }

    @Provides
    @CartListScope
    @ApplicationContext
    Context provideContextAbstraction(Context context) {
        return context;
    }

    @Provides
    @CartListScope
    TrackingPromoCheckoutUtil provideTrackingPromo(@ApplicationContext Context context) {
        return new TrackingPromoCheckoutUtil();
    }
}
