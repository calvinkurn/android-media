package com.tokopedia.tkpd.tkpdcontactus.orderquery.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.tkpdcontactus.common.di.ContactUsModule;
import com.tokopedia.tkpd.tkpdcontactus.common.di.scope.ContactUsModuleScope;
import com.tokopedia.tkpd.tkpdcontactus.home.di.ContactUsHomeModule;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.fragment.OrderQueryTicketFragment;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.fragment.QueryTicketDetailFragment;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.fragment.SubmitTicketFragment;

import dagger.Component;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

@ContactUsModuleScope
@Component(modules = {ContactUsModule.class,OrderQueryModule.class}, dependencies = BaseAppComponent.class)
public interface OrderQueryComponent {
    void inject(OrderQueryTicketFragment orderQueryTicketFragment);
    void inject(QueryTicketDetailFragment queryTicketDetailFragment);
    void inject(SubmitTicketFragment fragment);
}
