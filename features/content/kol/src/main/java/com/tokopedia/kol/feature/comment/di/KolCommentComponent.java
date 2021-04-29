package com.tokopedia.kol.feature.comment.di;

import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentNewFragment;
import com.tokopedia.kol.feature.report.di.ContentReportModule;

import dagger.Component;

/**
 * @author by milhamj on 18/04/18.
 */

@KolCommentScope
@Component(modules = KolCommentModule.class, dependencies = KolComponent.class)
public interface KolCommentComponent {
    void inject(KolCommentFragment kolCommentFragment);
    void inject(KolCommentNewFragment kolCommentNewFragment);
}
