package com.tokopedia.affiliate.feature.createpost.di;

import com.tokopedia.affiliate.feature.createpost.view.fragment.BaseCreatePostFragment;
import com.tokopedia.affiliate.feature.createpost.view.service.SubmitPostService;

import dagger.Component;

/**
 * @author by milhamj on 9/26/18.
 */
@CreatePostScope
@Component(modules = CreatePostModule.class)
public interface CreatePostComponent {
    void inject(BaseCreatePostFragment fragment);

    void inject(SubmitPostService service);
}
