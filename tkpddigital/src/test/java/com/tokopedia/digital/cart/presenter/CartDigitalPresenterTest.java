package com.tokopedia.digital.cart.presenter;

import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.domain.CartDigitalRepository;
import com.tokopedia.digital.cart.domain.CheckoutRepository;
import com.tokopedia.digital.cart.domain.VoucherDigitalRepository;
import com.tokopedia.digital.cart.interactor.CartDigitalInteractor;
import com.tokopedia.digital.cart.listener.IDigitalCartView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.subscriptions.CompositeSubscription;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * @author anggaprasetiyo on 3/14/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartDigitalPresenterTest {


    @Mock
    ICartMapperData mapperData;
    @Mock
    DigitalEndpointService digitalEndpointService;
    @Mock
    IDigitalCartView view;
    @Mock
    CompositeSubscription compositeSubscription;

    private ICartDigitalPresenter cartDigitalPresenter;


    @Before
    public void setUp() throws Exception {
        cartDigitalPresenter = new CartDigitalPresenter(view,
                new CartDigitalInteractor(
                        compositeSubscription, new CartDigitalRepository(digitalEndpointService, mapperData),
                        new VoucherDigitalRepository(digitalEndpointService, mapperData),
                        new CheckoutRepository(digitalEndpointService, mapperData)
                )
        );
    }

    @Test
    public void shouldThrowWhenCategoryIdNull() throws Exception {
        given(view.getDigitalCategoryId()).willThrow(new RuntimeException());
        when(view.getDigitalCategoryId());

        cartDigitalPresenter.processGetCartData();

    }

}