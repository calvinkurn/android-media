package com.tokopedia.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.common.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordDetailFragment;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailActivity extends BaseSimpleActivity
        implements HasComponent<AppComponent>, TopAdsKeywordDetailFragment.OnKeywordDetailListener {
    private ShowCaseDialog showCaseDialog;

    public static Intent createInstance(Context context, KeywordAd keywordAd, String adId){
        Intent intent = new Intent(context, TopAdsKeywordDetailActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, keywordAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        KeywordAd keywordAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        String adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        return TopAdsKeywordDetailFragment.createInstance(keywordAd, adId);
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsKeywordDetailActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)){
            return;
        }
        if (showCaseDialog != null) {
            return;
        }

        final TopAdsKeywordDetailFragment topAdsKeywordDetailFragment = (TopAdsKeywordDetailFragment) getSupportFragmentManager().findFragmentByTag(getTagFragment());

        if (topAdsKeywordDetailFragment == null || topAdsKeywordDetailFragment.getView() == null) {
            return;
        }

        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

        if (toolbar.getHeight() > 0) {
            int height = toolbar.getHeight();
            int width = toolbar.getWidth();
            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_detail_keyword_title_1),
                            getString(R.string.topads_showcase_detail_keyword_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 0.8), 0,width, height}));
            View statusView = topAdsKeywordDetailFragment.getStatusView();
            if (statusView != null) {
                showCaseList.add(
                        new ShowCaseObject(
                                statusView,
                                getString(R.string.topads_showcase_detail_keyword_title_2),
                                getString(R.string.topads_showcase_detail_keyword_desc_2),
                                ShowCaseContentPosition.UNDEFINED,
                                Color.WHITE));
            }
            showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
            showCaseDialog.show(TopAdsKeywordDetailActivity.this, showCaseTag, showCaseList);
        }
        else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(
                    toolbar,
                    new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            startShowCase();
                        }
                    }
            ));
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
