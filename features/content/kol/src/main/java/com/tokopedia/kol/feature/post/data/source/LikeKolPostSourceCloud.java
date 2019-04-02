package com.tokopedia.kol.feature.post.data.source;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.feature.post.data.mapper.LikeKolPostMapper;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 17/05/18.
 */

public class LikeKolPostSourceCloud {
    private final Context context;
    private final KolApi kolApi;
    private final LikeKolPostMapper likeKolPostMapper;

    @Inject
    public LikeKolPostSourceCloud(@ApplicationContext Context context,
                                  KolApi kolApi,
                                  LikeKolPostMapper likeKolPostMapper) {
        this.context = context;
        this.kolApi = kolApi;
        this.likeKolPostMapper = likeKolPostMapper;
    }

    public Observable<Boolean> likeKolPost(RequestParams requestParams) {
        return kolApi.likeKolPost(getRequestPayload(requestParams, R.raw.mutation_like_kol_post))
                .map(likeKolPostMapper);
    }

    private GraphqlRequest getRequestPayload(RequestParams requestParams, int resId) {
        return new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), resId),
                requestParams.getParameters()
        );
    }
}
