package com.tokopedia.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.profile.view.subscriber.FollowKolSubscriber;

import okhttp3.Interceptor;

/**
 * @author by alvinatin on 24/04/18.
 */

public interface ProfileModuleRouter {

    void doFollowKolPost(int id, FollowKolSubscriber followKolPostSubscriber);

    void doUnfollowKolPost(int id, FollowKolSubscriber followKolPostSubscriber);

    BaseDaggerFragment getKolPostFragment(String userId,
                                          int postId,
                                          Intent resultIntent,
                                          Bundle bundle);

    Interceptor getChuckInterceptor();

    Intent getShopPageIntent(Context context, String shopId);

    Intent getLoginIntent(Context context);

    Intent getProfileCompletionIntent(Context context);
}
