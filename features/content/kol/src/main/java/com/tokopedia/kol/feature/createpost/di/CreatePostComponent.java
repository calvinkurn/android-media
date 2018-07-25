package com.tokopedia.kol.feature.createpost.di;

import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.kol.feature.createpost.view.activity.CreatePostImageEditorActivity;
import com.tokopedia.kol.feature.createpost.view.fragment.CreatePostWebviewFragment;

import dagger.Component;

/**
 * @author by yfsx on 25/06/18.
 */
@CreatePostScope
@Component(modules = {CreatePostModule.class}, dependencies = KolComponent.class)
public interface CreatePostComponent {

    void inject(CreatePostImageEditorActivity createPostImageEditorActivity);

    void inject(CreatePostWebviewFragment createPostWebviewFragment);
}
