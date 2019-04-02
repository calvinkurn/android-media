package com.tokopedia.kol.feature.post.view.listener;

import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;

/**
 * @author by milhamj on 14/05/18.
 */

public interface BaseKolListener {
    void onAvatarClickListener(BaseKolViewModel element);

    void onNameClickListener(BaseKolViewModel element);

    void onFollowButtonClickListener(BaseKolViewModel element);

    void onDescriptionClickListener(BaseKolViewModel element);

    void onLikeButtonClickListener(BaseKolViewModel element);

    void onCommentClickListener(BaseKolViewModel element);

    void onMenuClickListener(BaseKolViewModel element);
}
