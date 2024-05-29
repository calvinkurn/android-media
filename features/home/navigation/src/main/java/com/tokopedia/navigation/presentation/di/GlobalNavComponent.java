package com.tokopedia.navigation.presentation.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation.presentation.activity.NewMainParentActivity;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;
import com.tokopedia.navigation.presentation.fragment.NotificationFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by meta on 25/07/18.
 */
@Component(modules = { GlobalNavModule.class, GlobalNavViewModelModule.class }, dependencies = BaseAppComponent.class)
@GlobalNavScope
public interface GlobalNavComponent {

    void inject(MainParentActivity activity);

    void inject(NewMainParentActivity activity);

    void inject(InboxFragment fragment);

    void inject(NotificationFragment fragment);

    @Component.Factory
    interface Factory {
        GlobalNavComponent create(
                BaseAppComponent component,
                @BindsInstance Context context
        );
    }
}

