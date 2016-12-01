package com.tokopedia.core.shopinfo.presenter;

import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalk;

/**
 * Created by nisie on 11/18/16.
 */

public interface ShopTalkPresenter {
    void onDeleteTalk(ShopTalk shopTalk);

    void onReportTalk(ShopTalk shopTalk);

    void onFollowTalk(ShopTalk shopTalk);

    void onUnfollowTalk(ShopTalk shopTalk);

    boolean isRequesting();

    void getShopTalk();

    void loadMore();

}
