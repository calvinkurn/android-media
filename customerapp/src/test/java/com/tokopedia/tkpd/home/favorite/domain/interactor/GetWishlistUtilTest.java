package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author madi on 3/23/17.
 */
public class GetWishlistUtilTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private RequestParams mockRequestParams;

    private GetWishlistUtil getWishlistUtil;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getWishlistUtil = new GetWishlistUtil(favoriteRepository);
    }

    @Test
    public void testCreateObservable() throws Exception {
        getWishlistUtil.getWishListData(mockRequestParams);
        verify(favoriteRepository).getWishlist(mockRequestParams.getParameters());

        verifyNoMoreInteractions(favoriteRepository);
    }

    @Test
    public void testGetDefaultParamsNotEmpty() throws Exception {
        RequestParams defaultParams = GetWishlistUtil.getDefaultParams();
        assertTrue(defaultParams != null);

        assertTrue(defaultParams.getParameters() != null
                && defaultParams.getParameters().size() > 0);

        assertTrue(defaultParams.getParameters().containsKey(GetWishlistUtil.KEY_COUNT));
        assertTrue(defaultParams.getParameters().containsKey(GetWishlistUtil.KEY_PAGE));

        assertTrue(
                defaultParams.getParameters().get(GetWishlistUtil.KEY_COUNT)
                        == GetWishlistUtil.DEFAULT_COUNT_VALUE
        );
        assertTrue(
                defaultParams.getParameters().get(GetWishlistUtil.KEY_PAGE)
                        == GetWishlistUtil.DEFAULT_PAGE_VALUE
        );
    }

}