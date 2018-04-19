package com.tokopedia.kol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber;

import okhttp3.Interceptor;

/**
 * @author by nisie on 2/6/18.
 */

public interface KolRouter {
    Intent getKolCommentActivity(Context context, int postId, int rowNumber);

    String getKolCommentArgsPosition();

    String getKolCommentArgsTotalComment();

    void doLikeKolPost(int id, LikeKolPostSubscriber likeKolPostSubscriber);

    void doUnlikeKolPost(int id, LikeKolPostSubscriber likeKolPostSubscriber);

    void actionApplinkFromActivity(Activity activity, String linkUrl);

    Interceptor getChuckInterceptor();
}
