package com.tokopedia.loyalty.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.share.DefaultShare;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoDetailComponent;
import com.tokopedia.loyalty.di.component.PromoDetailComponent;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.fragment.PromoDetailFragment;
import com.tokopedia.track.TrackApp;
import java.util.HashMap;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class PromoDetailActivity extends BaseSimpleActivity implements HasComponent<PromoDetailComponent>,
        PromoDetailFragment.OnFragmentInteractionListener {

    private static final String EXTRA_PROMO_DATA = "promo_data";
    private static final String EXTRA_PROMO_SLUG = "slug";
    private static final String EXTRA_PROMO_POSITION = "position";
    private static final String EXTRA_PROMO_PAGE = "page";
    private static final String TAG_PROMO_DETAIL_SLUG = "PROMO_DETAIL_SLUG";
    private static final String KEY_SLUG = "slug";

    private PromoDetailComponent component;

    public static Intent getCallingIntent(Context context, PromoData promoData, int position, int page) {
        Intent intent = new Intent(context, PromoDetailActivity.class);
        intent.putExtra(EXTRA_PROMO_DATA, promoData);
        intent.putExtra(EXTRA_PROMO_PAGE, page);
        intent.putExtra(EXTRA_PROMO_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.title_promo_detail));

        trackCampaign(getIntent().getData());
    }

    private void trackCampaign(Uri uri) {
        //track campaign in case there is utm/gclid in url
        if (uri != null) {
            TrackApp.getInstance().getGTM().sendCampaign(this, uri.toString(), getScreenName(), false);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_promo_detail;
    }

    @Override
    protected Fragment getNewFragment() {
        PromoData promoData = getIntent().getParcelableExtra(EXTRA_PROMO_DATA);
        Uri uri = getIntent().getData();
        String slug = "";
        if (uri != null) {
            slug = uri.getQueryParameter(EXTRA_PROMO_SLUG);
        }
        hitSlugToNewRelic(slug);
        if (!TextUtils.isEmpty(slug)) {
            return PromoDetailFragment.newInstance(slug);
        } else if (promoData != null) {
            int position = getIntent().getIntExtra(EXTRA_PROMO_POSITION, 0);
            int page = getIntent().getIntExtra(EXTRA_PROMO_PAGE, 0);
            return PromoDetailFragment.newInstance(promoData, page, position);
        }
        finish();
        return null;
    }

    private void hitSlugToNewRelic(String slug) {
        ServerLogger.log(
                Priority.P2,
                TAG_PROMO_DETAIL_SLUG,
                new HashMap() {
                    {
                        put(KEY_SLUG, slug);
                    }
                }
        );
    }

    @Override
    public PromoDetailComponent getComponent() {
        if (this.component == null) {
            BaseMainApplication application = ((BaseMainApplication) getApplication());
            this.component = DaggerPromoDetailComponent.builder()
                    .baseAppComponent(application.getBaseAppComponent())
                    .build();
        }

        return this.component;
    }

    @Override
    public void onSharePromo(PromoData promoData) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(ShareData.PROMO_TYPE)
                .setId(promoData.getSlug())
                .setName(promoData.getTitle())
                .setTextContent(promoData.getTitle()
                        + getString(com.tokopedia.loyalty.R.string.share_promo_additional_text))
                .setUri(promoData.getLink())
                .build();
        new DefaultShare(this, shareData).show();
    }
}