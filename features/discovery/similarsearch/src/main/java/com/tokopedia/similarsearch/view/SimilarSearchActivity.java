package com.tokopedia.similarsearch.view;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.similarsearch.R;

import java.util.List;

public class SimilarSearchActivity extends BaseSimpleActivity implements SimilarSearchFragment.OnAnimationCompletelistner{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.base_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateExit(getFragment());
            }
        });

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_similar_search;
    }

    @Override
    protected Fragment getNewFragment() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> paths = UriUtil.destructureUri(ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, uri);
            if (!paths.isEmpty()) {
                String productId = paths.get(0);
                return SimilarSearchFragment.newInstance(productId);
            }
        }
        return null;
    }

    protected void inflateFragment() {
        Fragment newFragment = getNewFragment();
        if (newFragment == null) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(com.tokopedia.abstraction.R.id.parent_view, newFragment,getTagFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        animateExit(getFragment());
    }

    private void animateExit(Fragment exitFragment) {
        if (exitFragment != null) {
            final View view = exitFragment.getView();
            if (view != null) {
                int anim = R.anim.slide_in_down;
                Animation animation =
                        AnimationUtils.loadAnimation(this, anim);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.remove(getFragment()). commitAllowingStateLoss();
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        Fragment fragment = getFragment();
        if(fragment != null) {
            View view = fragment.getView();
            if (view != null) {
                view.clearAnimation();
                if (canCancelAnimation()) {
                    view.animate().cancel();
                }
            }
        }
        overridePendingTransition(0,0);
    }
    public static boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    @Override
    public void onAnimaitonComplete() {
            final View view = findViewById(R.id.background_black);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
                int anim = R.anim.fade_in;
                Animation animation =
                        AnimationUtils.loadAnimation(this, anim);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {


                    }
                });
                view.startAnimation(animation);
            }
        //start fade in animation
    }
}
