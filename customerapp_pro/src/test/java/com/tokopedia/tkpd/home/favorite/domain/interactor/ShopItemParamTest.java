package com.tokopedia.tkpd.home.favorite.domain.interactor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author madi on 3/23/17.
 */
public class ShopItemParamTest {

    private ShopItemParam shopItemParam;

    @Before
    public void setUp() throws Exception {
        shopItemParam = new ShopItemParam();
    }

    @Test
    public void testGetShopId() throws Exception {
        shopItemParam.setShopId("1");
        assertTrue(shopItemParam.getShopId() == "1");
    }

    @Test
    public void testSetShopId() throws Exception {
        shopItemParam.setShopId("1");
        assertTrue(shopItemParam.getShopId().equals("1"));
    }

    @Test
    public void testGetShopName() throws Exception {
        assertTrue(shopItemParam.getShopName() == null);
        shopItemParam.setShopName("name");
        assertTrue(shopItemParam.getShopName().equals("name"));

    }

    @Test
    public void testSetShopName() throws Exception {
        assertTrue(shopItemParam.getShopName() == null);
        shopItemParam.setShopName("name");
        assertTrue(shopItemParam.getShopName().equals("name"));
    }

    @Test
    public void testGetShopCoverUrl() throws Exception {
        assertTrue(shopItemParam.getShopCoverUrl() == null);
        shopItemParam.setShopCoverUrl("http");
        assertTrue(shopItemParam.getShopCoverUrl().equals("http"));
    }

    @Test
    public void testSetShopCoverUrl() throws Exception {
        shopItemParam.setShopCoverUrl("http");
        assertTrue(shopItemParam.getShopCoverUrl().equals("http"));
    }

    @Test
    public void testGetShopImageUrl() throws Exception {
        assertTrue(shopItemParam.getShopImageUrl() == null);
        shopItemParam.setShopImageUrl("imageUrl");
        assertTrue(shopItemParam.getShopImageUrl().equals("imageUrl"));
    }

    @Test
    public void testSetShopImageUrl() throws Exception {
        shopItemParam.setShopImageUrl("imageUrl");
        assertTrue(shopItemParam.getShopImageUrl().equals("imageUrl"));
    }

}