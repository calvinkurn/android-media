package com.tokopedia.checkout.view.di.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.feature.cartlist.CartFragment;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.feature.cartlist.CartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListView;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutRouter;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingFirstCodeMapper;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsGqlUseCase;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class, PromoCheckoutModule.class})
public class CartListModule {

    private final ICartListView cartListView;
    private final CartAdapter.ActionListener cartActionListener;
    private final CartItemAdapter.ActionListener cartItemActionListener;

    public CartListModule(CartFragment cartFragment) {
        this.cartListView = cartFragment;
        this.cartActionListener = cartFragment;
        this.cartItemActionListener = cartFragment;
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
    TopAdsGqlUseCase topAdsUseCase(Context context){
        return new TopAdsGqlUseCase(context);
    }

    @Provides
    @CartListScope
    ICartListPresenter provideICartListPresenter(GetCartListUseCase getCartListUseCase,
                                                 DeleteCartUseCase deleteCartUseCase,
                                                 DeleteCartGetCartListUseCase deleteCartGetCartListUseCase,
                                                 UpdateCartUseCase updateCartUseCase,
                                                 ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                                                 CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase,
                                                 CheckPromoStackingFirstCodeMapper checkPromoStackingCodeMapper,
                                                 CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                 CompositeSubscription compositeSubscription,
                                                 CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                                 CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                 AddWishListUseCase addWishListUseCase,
                                                 RemoveWishListUseCase removeWishListUseCase,
                                                 UpdateAndReloadCartUseCase updateAndReloadCartUseCase,
                                                 UserSessionInterface userSessionInterface,
                                                 TopAdsGqlUseCase topAdsGqlUseCase,
                                                 ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase) {
        return new CartListPresenter(
                cartListView, getCartListUseCase, deleteCartUseCase, deleteCartGetCartListUseCase,
                updateCartUseCase, resetCartGetCartListUseCase, checkPromoStackingCodeUseCase,
                checkPromoStackingCodeMapper, checkPromoCodeCartListUseCase, compositeSubscription,
                cartApiRequestParamGenerator, cancelAutoApplyCouponUseCase, addWishListUseCase,
                removeWishListUseCase, updateAndReloadCartUseCase, userSessionInterface, topAdsGqlUseCase,
                clearCacheAutoApplyStackUseCase);
    }

    @Provides
    @CartListScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration();
    }

    @Provides
    @CartListScope
    CartAdapter provideCartListAdapter() {
        return new CartAdapter(cartActionListener, cartItemActionListener);
    }

    @Provides
    @CartListScope
    @ApplicationContext
    Context provideContextAbstraction(Context context){
        return context;
    }

    @Provides
    @CartListScope
    TrackingPromoCheckoutUtil provideTrackingPromo(@ApplicationContext Context context) {
        if(context instanceof TrackingPromoCheckoutRouter){
            return new TrackingPromoCheckoutUtil((TrackingPromoCheckoutRouter)context);
        }else{
            return new TrackingPromoCheckoutUtil(null);
        }
    }
}
