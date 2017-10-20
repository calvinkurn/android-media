package com.tokopedia.tkpd.home.wishlist.data;

import com.tokopedia.core.network.entity.wishlist.Shop;
import com.tokopedia.core.network.entity.wishlist.WholesalePrice;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.network.entity.wishlist.WishlistData;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.tkpd.home.wishlist.domain.model.BadgeWishlistDomain;
import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;
import com.tokopedia.tkpd.home.wishlist.domain.model.LabelWishlistDomain;
import com.tokopedia.tkpd.home.wishlist.domain.model.ShopWishlistDomain;
import com.tokopedia.tkpd.home.wishlist.domain.model.WholesalePriceDomain;
import com.tokopedia.tkpd.home.wishlist.domain.model.WishlistDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author Kulomady on 2/20/17.
 */

public class WishlistDataMapper implements Func1<Response<WishlistData>, DataWishlist> {

    public WishlistDataMapper() {
    }

    @Override
    public DataWishlist call(Response<WishlistData> response) {

        if (response != null && response.isSuccessful()
                && response.body() != null && response.body().getWishlist() != null) {
            return mappingValidResponse(response.body());
        } else {
            return mappingInvalidResponse();
        }
    }

    private DataWishlist mappingValidResponse(WishlistData response) {
        DataWishlist wishlist = new DataWishlist();
        wishlist.setDataValid();
        List<WishlistDomain> wishlistDomains = new ArrayList<>();
        for (Wishlist wishlistResponse : response.getWishlist()) {
            wishlistDomains.add(mappingWishlistResponse(wishlistResponse));
        }
        wishlist.setWishlists(wishlistDomains);
        if (response.getPaging() != null) wishlist.setPaging(response.getPaging());
        return wishlist;
    }

    private DataWishlist mappingInvalidResponse() {
        DataWishlist wishlist = new DataWishlist();
        wishlist.setDataInValid();
        return wishlist;
    }

    private WishlistDomain mappingWishlistResponse(Wishlist wishlistResponse) {
        WishlistDomain wishlistDomain = new WishlistDomain();
        wishlistDomain.setAvailable(wishlistResponse.getIsAvailable());
        wishlistDomain.setCondition(wishlistResponse.getCondition());
        wishlistDomain.setId(wishlistResponse.getId());
        wishlistDomain.setImageUrl(wishlistResponse.getImageUrl());
        wishlistDomain.setMinimumOrder(wishlistResponse.getMinimumOrder());
        wishlistDomain.setName(wishlistResponse.getName());
        wishlistDomain.setPrice(wishlistResponse.getPrice());
        wishlistDomain.setPriceFormated(wishlistResponse.getPriceFmt());

        wishlistDomain.setBadges(mappingWishlistBadges(wishlistResponse.getBadges()));
        wishlistDomain.setLabels(mappingWishlistLabels(wishlistResponse.getLabels()));
        wishlistDomain.setWholeSalePrice(mappingWholeSalePrice(wishlistResponse.getWholesale()));
        wishlistDomain.setShop(mappingShop(wishlistResponse.getShop()));

        return wishlistDomain;
    }


    private ShopWishlistDomain mappingShop(Shop shop) {
        ShopWishlistDomain shopWishlistDomain = new ShopWishlistDomain();
        shopWishlistDomain.setName(shop.getName());
        shopWishlistDomain.setGoldMerchant(shop.getIsGoldMerchant());
        shopWishlistDomain.setOfficial(shop.getOfficial());
        shopWishlistDomain.setId(shop.getId());
        shopWishlistDomain.setLocation(shop.getLocation());
        shopWishlistDomain.setLuckiMerchant(shop.getLuckyMerchant());
        shopWishlistDomain.setStatus(shop.getStatus());
        shopWishlistDomain.setUrl(shop.getUrl());
        return shopWishlistDomain;
    }

    private List<WholesalePriceDomain> mappingWholeSalePrice(List<WholesalePrice> wholesale) {
        ArrayList<WholesalePriceDomain> wholesalePriceDomains = new ArrayList<>();
        for (WholesalePrice wholesalePrice : wholesale) {
            WholesalePriceDomain wholesalePriceDomain = new WholesalePriceDomain();
            wholesalePriceDomain.setMaximum(wholesalePrice.getMaximum());
            wholesalePriceDomain.setMinimum(wholesalePrice.getMinimum());
            wholesalePriceDomain.setPrice(wholesalePrice.getPrice());
            wholesalePriceDomains.add(wholesalePriceDomain);
        }
        return wholesalePriceDomains;
    }

    private List<LabelWishlistDomain> mappingWishlistLabels(List<Label> labelsResponse) {
        ArrayList<LabelWishlistDomain> labelWishlistDomains = new ArrayList<>();
        if (labelsResponse != null) {
            for (Label labelResponse : labelsResponse) {
                LabelWishlistDomain labelWishlistDomain = new LabelWishlistDomain();
                labelWishlistDomain.setTitle(labelResponse.getTitle());
                labelWishlistDomain.setColor(labelResponse.getColor());
                labelWishlistDomains.add(labelWishlistDomain);
            }
        }
        return labelWishlistDomains;
    }

    private List<BadgeWishlistDomain> mappingWishlistBadges(List<Badge> badgesResponse) {
        ArrayList<BadgeWishlistDomain> badgeWishlistDomains = new ArrayList<>();
        if (badgesResponse != null) {
            for (Badge badgeResponse : badgesResponse) {
                BadgeWishlistDomain badgeWishlistDomain = new BadgeWishlistDomain();
                badgeWishlistDomain.setImageUrl(badgeResponse.getImageUrl());
                badgeWishlistDomain.setImgUrl(badgeResponse.getImgUrl());
                badgeWishlistDomain.setTitle(badgeResponse.getTitle());
                badgeWishlistDomains.add(badgeWishlistDomain);
            }
        }
        return badgeWishlistDomains;
    }


}
