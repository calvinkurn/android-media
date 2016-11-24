package com.tokopedia.core.manage.people.password.activity;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by stevenfredian on 9/28/16.
 */
public interface ManagePasswordActivityView {
    void inflateFragment(Fragment managePasswordFragment, String simpleName);

    void changePassword(Bundle param);
}
