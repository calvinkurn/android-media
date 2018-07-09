package com.tokopedia.contactus.orderquery.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.contactus.common.di.ContactUsModule;
import com.tokopedia.contactus.common.di.scope.ContactUsModuleScope;
import com.tokopedia.contactus.orderquery.view.fragment.OrderQueryTicketFragment;
import com.tokopedia.contactus.orderquery.view.fragment.QueryTicketDetailFragment;
import com.tokopedia.contactus.orderquery.view.fragment.SubmitTicketFragment;
import com.tokopedia.contactus.orderquery.view.presenter.SubmitTicketPresenter;

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
    void inject(SubmitTicketPresenter presenter);
}
