package com.tokopedia.tkpd.home.favorite.data;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author madi on 3/24/17.
 */
public class FavoriteDataRepositoryTest {

    @Mock
    private FavoriteFactory favoriteFactory;
    @Mock
    private RequestParams requestParams;

    private FavoriteDataRepository favoriteDataRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        favoriteDataRepository = new FavoriteDataRepository(favoriteFactory);
        TKPDMapParam<String, Object> stringObjectHashMap = new TKPDMapParam<>();
        given(requestParams.getParameters()).willReturn(stringObjectHashMap);
    }

    @Test
    public void testGetWishlist() throws Exception {

        //given
        Observable<DomainWishlist> mockDomainWishlistObservable
                = Observable.just(new DomainWishlist());
        given(favoriteDataRepository.getWishlist(requestParams.getParameters()))
                .willReturn(mockDomainWishlistObservable);

        //when
        TestSubscriber<DomainWishlist> subscriber = new TestSubscriber<>();
        favoriteDataRepository.getWishlist(requestParams.getParameters()).subscribe(subscriber);

        //then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<DomainWishlist> listOfDomainWishlist = subscriber.getOnNextEvents();
        DomainWishlist domainWishlist = listOfDomainWishlist.get(0);

        assertNotNull(domainWishlist);
        verify(favoriteFactory).getWishlist(requestParams.getParameters());

    }

    @Test
    public void test_getFreshWishlist_notNull() throws Exception {

        Observable<DomainWishlist> mockDomainWishlistObservable
                = Observable.just(new DomainWishlist());

        given(favoriteFactory.getFreshWishlist(requestParams.getParameters()))
                .willReturn(mockDomainWishlistObservable);

        TestSubscriber<DomainWishlist> subscriber = new TestSubscriber<>();
        favoriteDataRepository
                .getFreshWishlist(requestParams.getParameters())
                .subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<DomainWishlist> listOfDomainWishlist = subscriber.getOnNextEvents();
        DomainWishlist domainWishlist = listOfDomainWishlist.get(0);

        assertNotNull(domainWishlist);
        verify(favoriteFactory).getFreshWishlist(requestParams.getParameters());

    }

    @Test
    public void testGetFavoriteShop() throws Exception {
        TKPDMapParam<String,String> fakeParams= new TKPDMapParam<>();
        Observable<FavoriteShop> mockFavoriteShopObservable = Observable.just(new FavoriteShop());

        given(favoriteFactory.getFavoriteShop(fakeParams))
                .willReturn(mockFavoriteShopObservable);

        TestSubscriber<FavoriteShop> subscriber = new TestSubscriber<>();
        favoriteDataRepository.getFavoriteShop(fakeParams).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(favoriteFactory).getFavoriteShop(fakeParams);

        List<FavoriteShop> onNextEvents = subscriber.getOnNextEvents();
        FavoriteShop favoriteShop = onNextEvents.get(0);

        assertNotNull(favoriteShop);

    }

    @Test
    public void testGetFavoriteFirstPage() throws Exception {

        TKPDMapParam<String,String> fakeParams= new TKPDMapParam<>();
        Observable<FavoriteShop> mockFavoriteShopObservable = Observable.just(new FavoriteShop());

        given(favoriteFactory.getFavoriteShopFirstPage(fakeParams))
                .willReturn(mockFavoriteShopObservable);

        TestSubscriber<FavoriteShop> subscriber = new TestSubscriber<>();
        favoriteDataRepository.getFirstPageFavoriteShop(fakeParams).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(favoriteFactory).getFavoriteShopFirstPage(fakeParams);

        List<FavoriteShop> onNextEvents = subscriber.getOnNextEvents();
        FavoriteShop favoriteShop = onNextEvents.get(0);

        assertNotNull(favoriteShop);

    }

    @Test
    public void testGetTopAdsShop() throws Exception {
        TopAdsShop localValue = new TopAdsShop();
        TopAdsShop cloudValue = new TopAdsShop();
        Observable<TopAdsShop> localMockObservable = Observable.just(localValue);
        Observable<TopAdsShop> cloudMockObservable = Observable.just(cloudValue);

        TestSubscriber<TopAdsShop> subscriber = new TestSubscriber<>();
        Observable<TopAdsShop> mockResult
                = Observable.concat(localMockObservable,cloudMockObservable)
                 .first();
        mockResult.subscribe(subscriber);

        given(favoriteFactory.getTopAdsShop(requestParams.getParameters()))
                .willReturn(mockResult);

        favoriteDataRepository.getTopAdsShop(requestParams.getParameters());

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(favoriteFactory).getTopAdsShop(requestParams.getParameters());
        subscriber.assertValue(localValue);

    }

    @Test
    public void testGetFreshTopAdsShop() throws Exception {

        TopAdsShop fakeTopAds = new TopAdsShop();
        Observable<TopAdsShop> mockResultObservable = Observable.just(fakeTopAds);
        given(favoriteFactory.getFreshTopAdsShop(requestParams.getParameters()))
                .willReturn(mockResultObservable);

        TestSubscriber<TopAdsShop> subscriber = new TestSubscriber<>();
        favoriteDataRepository.getFreshTopAdsShop(requestParams.getParameters())
                .subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(favoriteFactory).getFreshTopAdsShop(requestParams.getParameters());
        List<TopAdsShop> onNextEvents = subscriber.getOnNextEvents();
        TopAdsShop topAdsShop = onNextEvents.get(0);

        assertNotNull(topAdsShop);
        assertSame(fakeTopAds, topAdsShop);

    }

    @Test
    public void testAddFavoriteShop() throws Exception {

        TKPDMapParam<String, String> mockParams = new TKPDMapParam<>();
        FavShop fakeFavShop = new FavShop();
        Observable<FavShop> mockResultObservable =Observable.just(fakeFavShop);

        given(favoriteFactory.postFavShop(mockParams)).willReturn(mockResultObservable);

        TestSubscriber<FavShop> subscriber = new TestSubscriber<>();
        favoriteDataRepository.addFavoriteShop(mockParams).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(favoriteFactory).postFavShop(mockParams);

        List<FavShop> onNextEvents = subscriber.getOnNextEvents();
        FavShop favShop = onNextEvents.get(0);

        assertNotNull(favShop);
    }

}