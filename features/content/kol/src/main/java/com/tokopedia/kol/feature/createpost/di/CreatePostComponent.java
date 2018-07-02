package com.tokopedia.kol.feature.createpost.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.kol.feature.createpost.view.activity.CreatePostImageEditorActivity;

import dagger.Component;

/**
 * @author by yfsx on 25/06/18.
 */
@CreatePostScope
@Component(modules = {CreatePostModule.class}, dependencies = KolComponent.class)
public interface CreatePostComponent {

    void inject(CreatePostImageEditorActivity createPostImageEditorActivity);

}
