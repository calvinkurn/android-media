package com.tokopedia.product.manage.item.view.listener;

import com.tokopedia.product.manage.item.view.fragment.ProductAddVideoFragment;

import java.util.List;

/**
 * Created by normansyahputa on 4/17/17.
 */

public interface YoutubeAddVideoActView {
    ProductAddVideoFragment youtubeAddVideoFragment();

    void openAddYoutubeDialog();

    List<String> videoIds();

    void addVideoIds(String videoId);

    void removeVideoIds(int index);

    void removeVideoId(String videoIds);
}
