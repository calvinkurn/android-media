package com.tokopedia.core.manage.people.password.presenter;

import com.tokopedia.core.manage.people.password.activity.ManagePasswordActivity;
import com.tokopedia.core.manage.people.password.activity.ManagePasswordActivityView;
import com.tokopedia.core.manage.people.password.fragment.ManagePasswordFragment;

/**
 * Created by stevenfredian on 9/28/16.
 */
public class ManagePasswordActivityPresenterImpl implements ManagePasswordActivityPresenter{

    ManagePasswordActivityView view;

    public ManagePasswordActivityPresenterImpl(ManagePasswordActivity managePasswordActivity) {
        view = managePasswordActivity;
    }

    @Override
    public void initFragment() {
        view.inflateFragment(ManagePasswordFragment.newInstance(),ManagePasswordFragment.class.getSimpleName());
    }
}
