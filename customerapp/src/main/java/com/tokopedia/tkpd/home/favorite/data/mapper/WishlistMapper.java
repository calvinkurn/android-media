package com.tokopedia.tkpd.home.favorite.data.mapper;

import android.content.Context;

import com.tokopedia.tkpd.home.wishlist.domain.model.GqlWishListDataResponse;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.model.DataWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.WishListBadge;
import com.tokopedia.tkpd.home.favorite.domain.model.WishListLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kulomady on 1/18/17.
 */
public class WishlistMapper implements rx.functions.Func1<GraphqlResponse, DomainWishlist> {
    private final String defaultErrorMessage;

    public WishlistMapper(Context context) {
        defaultErrorMessage = context.getString(R.string.msg_network_error);
    }

    @Override
    public DomainWishlist call(GraphqlResponse response) {

        GqlWishListDataResponse gqlWishListDataResponse = response.getData(GqlWishListDataResponse.class);

        if (gqlWishListDataResponse != null) {
            return ValidateResponse(gqlWishListDataResponse);
        }
        return invalidResponse(defaultErrorMessage);
    }

    private DomainWishlist ValidateResponse(GqlWishListDataResponse gqlWishListDataResponse) {

        if (gqlWishListDataResponse.getGqlWishList().getWishlistDataList() != null &&
                gqlWishListDataResponse.getGqlWishList().getWishlistDataList().size() > 0) {

            return mappingValidResponse(
                    gqlWishListDataResponse.getGqlWishList().getWishlistDataList());
        } else {
            return invalidResponse(defaultErrorMessage);
        }

    }

    private DomainWishlist mappingValidResponse(List<Wishlist> wishlistResponse) {
        DomainWishlist domainWishlist = new DomainWishlist();
        List<DataWishlist> result = new ArrayList<>();
        for (Wishlist wishlist : wishlistResponse) {
            DataWishlist dataWishlist = new DataWishlist();
            dataWishlist.setId(wishlist.getId());
            dataWishlist.setName(wishlist.getName());
            dataWishlist.setProductImageUrl(wishlist.getImageUrl());
            dataWishlist.setShop_name(wishlist.getShop().getName());
            dataWishlist.setShop_location(wishlist.getShop().getLocation());
            dataWishlist.setProductImageUrl(wishlist.getImageUrl());
            dataWishlist.setPrice(wishlist.getPriceFmt());
            if (wishlist.getLabels() != null && wishlist.getLabels().size() > 0) {
                dataWishlist.setLabels(mappingLabelResponse(wishlist.getLabels()));
            }
            if (wishlist.getBadges() != null && wishlist.getBadges().size() > 0) {
                dataWishlist.setBadges(mappingBadgeResponse(wishlist.getBadges()));
            }
            result.add(dataWishlist);
        }
        domainWishlist.setDataIsValid(true);
        domainWishlist.setData(result);
        return domainWishlist;
    }

    private List<WishListBadge> mappingBadgeResponse(List<Badge> badges) {
        ArrayList<WishListBadge> wishListBadges = new ArrayList<>();
        for (Badge badge : badges) {
            wishListBadges.add(new WishListBadge(badge.getTitle(), badge.getImageUrl()));
        }
        return wishListBadges;
    }

    private List<WishListLabel> mappingLabelResponse(List<Label> labels) {
        ArrayList<WishListLabel> wishListLabels = new ArrayList<>();
        for (Label label : labels) {
            WishListLabel wishListLabel = new WishListLabel();
            wishListLabel.setTitle(label.getTitle());
            wishListLabel.setColor(label.getColor());
            wishListLabels.add(wishListLabel);
        }
        return wishListLabels;
    }

    private DomainWishlist invalidResponse(String errorMessage) {
        DomainWishlist domainWishlist = new DomainWishlist();
        domainWishlist.setDataIsValid(false);
        domainWishlist.setMessage(errorMessage);
        return domainWishlist;
    }
}
