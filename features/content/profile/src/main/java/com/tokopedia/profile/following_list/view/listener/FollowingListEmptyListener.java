package com.tokopedia.profile.following_list.view.listener;

import com.tokopedia.profile.following_list.view.fragment.BaseFollowListFragment;
import org.jetbrains.annotations.NotNull;

/**
 * @author by milhamj on 30/10/18.
 */

public interface FollowingListEmptyListener {
    void onFollowingEmpty(@NotNull Class<? extends BaseFollowListFragment> javaClass);

    void onFollowingNotEmpty();
}
