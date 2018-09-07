package com.tokopedia.mitra.account.fragment;

import com.tokopedia.mitra.BaseFragmentTest;
import com.tokopedia.mitra.homepage.activity.MitraParentHomepageActivity;
import com.tokopedia.mitra.homepage.fragment.MitraHomepageFragment;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class MitraAccountFragmentTest extends BaseFragmentTest{

    private ActivityController<MitraParentHomepageActivity> activityController;
    private MitraParentHomepageActivity activity;
    private MitraAccountFragment fragment;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(MitraParentHomepageActivity.class);
        activity = activityController.create().start().postCreate(null).resume().visible().get();
        fragment = MitraAccountFragment.newInstance();
        startFragment(activity, fragment);
        fragment.presenter = spy(fragment.presenter);
    }
}