package com.tokopedia.navigation.presentation.activity;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;

/**
 * Created by meta on 10/01/19.
 */
public class InboxMainActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return InboxFragment.newInstance();
    }
}