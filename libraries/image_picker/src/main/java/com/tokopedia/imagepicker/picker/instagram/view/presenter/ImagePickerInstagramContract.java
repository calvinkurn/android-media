package com.tokopedia.imagepicker.picker.instagram.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public interface ImagePickerInstagramContract {
    interface Presenter extends CustomerPresenter<View> {

        void getListMediaInstagram(String code, String nextMediaId);
    }

    interface View extends BaseListViewListener<InstagramMediaModel> {

        void renderList(List<InstagramMediaModel> instagramMediaModels, boolean hasNextPage, String nextMaxIdPage);
    }
}
