package com.tokopedia.tkpd.home.favorite.data;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.network.entity.wishlist.WishlistData;
import com.tokopedia.core.var.Label;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.model.DataWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.WishListLabel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * @author Kulomady on 1/18/17.
 */
public class WishlistMapper implements rx.functions.Func1<Response<String>, DomainWishlist> {
    private final String mDefaultErrorMessage;
    private Gson mGson;

    public WishlistMapper(Context context, Gson gson) {
        mGson = gson;
        mDefaultErrorMessage = context.getString(R.string.msg_network_error);
    }

    @Override
    public DomainWishlist call(Response<String> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return ValidateResponse(response.body());
        }
        return invalidResponse(mDefaultErrorMessage);
    }

    private DomainWishlist ValidateResponse(String repsonseBody) {
        WishlistData wishlistResponse = mGson.fromJson(repsonseBody, WishlistData.class);
        if (wishlistResponse != null) {
            if (wishlistResponse.getWishlist() != null && wishlistResponse.getWishlist().size() > 0) {
                return mappingValidResponse(wishlistResponse.getWishlist());
            } else {
                return invalidResponse(mDefaultErrorMessage);
            }

        } else {
            return invalidResponse(mDefaultErrorMessage);
        }

    }

    private DomainWishlist mappingValidResponse(List<Wishlist> wishlistResponse) {
        DomainWishlist domainWishlist = new DomainWishlist();
        List<DataWishlist> result = new ArrayList<>();
        for (Wishlist wishlist : wishlistResponse) {
            DataWishlist dataWishlist = new DataWishlist();
            dataWishlist.setShop_name(wishlist.getShop().getName());
            dataWishlist.setShop_location(wishlist.getShop().getLocation());
            dataWishlist.setPrice(wishlist.getPriceFmt());
            if (wishlist.getLabels() != null && wishlist.getLabels().size() > 0) {
                dataWishlist.setLabels(mappingLabelResponse(wishlist.getLabels()));
            }
            result.add(dataWishlist);
        }
        domainWishlist.setDataIsValid(true);
        domainWishlist.setData(result);
        return domainWishlist;
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
