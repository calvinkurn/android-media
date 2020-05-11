package com.tokopedia.product.manage.item.video.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.product.manage.item.video.view.model.AddUrlVideoModel;

import java.util.List;

/**
 * @author normansyahputa on 4/17/17.
 */
public interface YoutubeAddVideoView extends CustomerView {

    String KEY_VIDEOS_LINK = "KEY_VIDEOS_LINK";

    void addAddUrlVideModel(AddUrlVideoModel addUrlVideoModel);

    void addYoutubeUrl(String youtubeUrl);

    void showMessageError(String message);

    void showMessageErrorRaw(String message);

    void addAddUrlVideModels(List<AddUrlVideoModel> convert);

    void showLoading();

    void hideLoading();

    void hideRetryFull();
}
