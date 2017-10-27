package com.tokopedia.tkpd.home.wishlist;

import com.tokopedia.core.network.entity.wishlist.Shop;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.network.entity.wishlist.WishlistData;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.tkpd.home.wishlist.domain.model.BadgeWishlistDomain;
import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;
import com.tokopedia.tkpd.home.wishlist.domain.model.LabelWishlistDomain;
import com.tokopedia.tkpd.home.wishlist.domain.model.ShopWishlistDomain;
import com.tokopedia.tkpd.home.wishlist.domain.model.WishlistDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 2/21/17.
 */
public class WishlistViewModelMapper {
    private WishlistData wishlistData;

    public WishlistViewModelMapper(DataWishlist dataWishlist) {
        wishlistData = mappingToViewModel(dataWishlist);
    }

    private WishlistData mappingToViewModel(DataWishlist dataWishlist) {
        WishlistData wishlistData = new WishlistData();
        wishlistData.setWishlist(mappingWishlistViewModel(dataWishlist.getWishlists()));
        wishlistData.setPaging(dataWishlist.getPaging());
        return wishlistData;
    }

    private List<Wishlist> mappingWishlistViewModel(List<WishlistDomain> dataWishlist) {

        ArrayList<Wishlist> wishlists = new ArrayList<>();
        for (WishlistDomain wishlistDomain : dataWishlist) {
            Wishlist wishlist = new Wishlist();
            wishlist.setCondition(wishlistDomain.getCondition());
            wishlist.setId(wishlistDomain.getId());
            wishlist.setImageUrl(wishlistDomain.getImageUrl());
            wishlist.setIsAvailable(wishlistDomain.isAvailable());
            wishlist.setIsPreOrder(wishlistDomain.isPreOrder());
            wishlist.setMinimumOrder(wishlistDomain.getMinimumOrder());
            wishlist.setName(wishlistDomain.getName());
            wishlist.setPrice(wishlistDomain.getPrice());
            wishlist.setPriceFmt(wishlistDomain.getPriceFormated());
            wishlist.setStatus(wishlistDomain.getStatus());
            wishlist.setUrl(wishlistDomain.getUrl());
            if (wishlistDomain.getShop() != null) {
                wishlist.setShop(mappingShopToViewModel(wishlistDomain.getShop()));
            }

            if (wishlistDomain.getLabels() != null && wishlistDomain.getLabels().size() > 0) {
                wishlist.setLabels(mappingLabelToviewModel(wishlistDomain.getLabels()));
            }

            if (wishlistDomain.getBadges() != null && wishlistDomain.getBadges().size() > 0) {
                wishlist.setBadges(mappingBadgeToViewModel(wishlistDomain.getBadges()));
            }
            wishlists.add(wishlist);
        }
        return wishlists;
    }

    private List<Badge> mappingBadgeToViewModel(List<BadgeWishlistDomain> badgesDomain) {
        ArrayList<Badge> badges = new ArrayList<>();
        for (BadgeWishlistDomain badgeWishlistDomain : badgesDomain) {
            Badge badge = new Badge();
            badge.setImgUrl(badgeWishlistDomain.getImgUrl());
            badge.setImageUrl(badgeWishlistDomain.getImageUrl());
            badge.setTitle(badgeWishlistDomain.getTitle());
            badges.add(badge);
        }
        return badges;
    }

    private List<Label> mappingLabelToviewModel(List<LabelWishlistDomain> labelWishlistDomains) {
        ArrayList<Label> labels = new ArrayList<>();
        for (LabelWishlistDomain labelWishlistDomain : labelWishlistDomains) {
            Label label = new Label();
            label.setColor(labelWishlistDomain.getColor());
            label.setTitle(labelWishlistDomain.getTitle());
            labels.add(label);
        }
        return labels;
    }

    private Shop mappingShopToViewModel(ShopWishlistDomain shopDomain) {
        Shop shop = new Shop();
        shop.setName(shopDomain.getName());
        shop.setUrl(shopDomain.getUrl());
        shop.setStatus(shopDomain.getStatus());
        shop.setId(shopDomain.getId());
        shop.setIsGoldMerchant(shopDomain.isGoldMerchant());
        shop.setOfficial(shopDomain.isOfficial());
        shop.setLocation(shopDomain.getLocation());
        shop.setLuckyMerchant(shopDomain.getLuckiMerchant());
        return shop;
    }

    public WishlistData getWishlistData() {
        return wishlistData;
    }
}
