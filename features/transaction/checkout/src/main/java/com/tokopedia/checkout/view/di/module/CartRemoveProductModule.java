package com.tokopedia.checkout.view.di.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.view.common.adapter.CartRemoveProductAdapter;
import com.tokopedia.checkout.view.di.scope.CartRemoveProductScope;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.feature.removecartitem.RemoveCartItemAdapter;
import com.tokopedia.checkout.view.feature.removecartitem.RemoveCartItemPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class})
public class CartRemoveProductModule {

    private final CartRemoveProductAdapter.CartRemoveProductActionListener actionListener;
    private final Context context;

    public CartRemoveProductModule() {
        actionListener = null;
        context = null;
    }

    @Provides
    @CartRemoveProductScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartRemoveProductScope
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @CartRemoveProductScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration();
    }

    @Provides
    @CartRemoveProductScope
    CartRemoveProductAdapter provideCartRemoveProductAdapter() {
        return new CartRemoveProductAdapter(actionListener);
    }

    @Provides
    @CartRemoveProductScope
    RemoveCartItemPresenter provideRemoveCartItemPresenter(CompositeSubscription compositeSubscription,
                                                           DeleteCartUseCase deleteCartUseCase) {
        return new RemoveCartItemPresenter(compositeSubscription, deleteCartUseCase);
    }

    @Provides
    @CartRemoveProductScope
    RemoveCartItemAdapter provideRemoveCartItemAdapter() {
        return new RemoveCartItemAdapter();
    }
}
