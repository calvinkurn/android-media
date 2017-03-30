package com.tokopedia.tkpd.home.favorite.data.source.local;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * @author madi on 3/27/17.
 */
public class LocalFavoriteShopDataSourceTest {
    @Mock
    private Context context;
    @Mock
    private GlobalCacheManager cacheManager;

    private LocalFavoriteShopDataSource localFavoriteShopDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Gson gson = new Gson();
        localFavoriteShopDataSource = new LocalFavoriteShopDataSource(context, gson, cacheManager);
    }

    @Test
    public void testGetFavorite() throws Exception {

        String invalidResponse = "mockString";

        given(cacheManager.getValueString(TkpdCache.Key.FAVORITE_SHOP)).willReturn(invalidResponse);

        TestSubscriber<FavoriteShop> subscriber = new TestSubscriber<>();
        localFavoriteShopDataSource.getFavorite().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.awaitTerminalEvent();

        List<FavoriteShop> onNextEvents = subscriber.getOnNextEvents();
        FavoriteShop favoriteShop = onNextEvents.get(0);

        assertNull(favoriteShop);

    }

}