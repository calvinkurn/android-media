package com.tokopedia.affiliate.feature.createpost.di;

import com.tokopedia.affiliate.feature.createpost.view.fragment.CreatePostFragment;
import com.tokopedia.affiliate.feature.createpost.view.service.SubmitPostService;

import dagger.Component;

/**
 * @author by milhamj on 9/26/18.
 */
@CreatePostScope
@Component(modules = CreatePostModule.class)
public interface CreatePostComponent {
    void inject(CreatePostFragment fragment);

    void inject(SubmitPostService service);
}
