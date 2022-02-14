package com.tokopedia.navigation.presentation.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;

/**
 * Created by meta on 10/01/19.
 */
public class InboxMainActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return InboxFragment.newInstance();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        toolbar.setBackgroundColor(
                MethodChecker.getColor(this,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background)
        );
    }
}