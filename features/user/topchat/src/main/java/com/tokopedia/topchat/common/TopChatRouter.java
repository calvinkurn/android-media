package com.tokopedia.topchat.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author by nisie on 5/18/18.
 */
public interface TopChatRouter {

    String EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP = "SHOP_STATUS_FAVOURITE";

    void openImagePreviewFromChat(Context context, ArrayList<String> listImage, ArrayList<String>
            imageDesc, String title, String date);

    Intent getHomeIntent(Context context);

    boolean isIndicatorVisible();

    String getChannelFragmentTag();

    Fragment getChannelFragment(Bundle bundle);

    Intent getTopProfileIntent(Context context, String userId);

    Intent getShopPageIntent(Context context, String shopId);

    Observable<AddToCartResult> addToCartProduct(AddToCartRequest addToCartRequest, boolean isOneClickShipment);

    Intent getCartIntent(Activity activity);

}
