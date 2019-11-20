package com.tokopedia.kol.feature.following_list.view.listener;

import com.tokopedia.kol.feature.following_list.view.fragment.BaseFollowListFragment;

import org.jetbrains.annotations.NotNull;

/**
 * @author by milhamj on 30/10/18.
 */

public interface KolFollowingListEmptyListener {
    void onFollowingEmpty(@NotNull Class<? extends BaseFollowListFragment> javaClass);

    void onFollowingNotEmpty();
}
