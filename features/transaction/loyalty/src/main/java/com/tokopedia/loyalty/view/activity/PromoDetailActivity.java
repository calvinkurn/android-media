package com.tokopedia.loyalty.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoDetailComponent;
import com.tokopedia.loyalty.di.component.PromoDetailComponent;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.fragment.PromoDetailFragment;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class PromoDetailActivity extends BaseSimpleActivity implements HasComponent<PromoDetailComponent>,
        PromoDetailFragment.OnFragmentInteractionListener {

    private static final String EXTRA_PROMO_DATA = "promo_data";
    private static final String EXTRA_PROMO_SLUG = "slug";
    private static final String EXTRA_PROMO_POSITION = "position";
    private static final String EXTRA_PROMO_PAGE = "page";

    private PromoDetailComponent component;

    public static Intent getCallingIntent(Context context, PromoData promoData, int position, int page) {
        Intent intent = new Intent(context, PromoDetailActivity.class);
        intent.putExtra(EXTRA_PROMO_DATA, promoData);
        intent.putExtra(EXTRA_PROMO_PAGE, page);
        intent.putExtra(EXTRA_PROMO_POSITION, position);
        return intent;
    }

    public static Intent getCallingIntent(Context context, String slug) {
        Intent intent = new Intent(context, PromoDetailActivity.class);
        intent.putExtra(EXTRA_PROMO_SLUG, slug);
        return intent;
    }

    @DeepLink(ApplinkConst.PROMO_DETAIL)
    public static Intent getAppLinkIntent(Context context, Bundle extras) {
        String slug = extras.getString(EXTRA_PROMO_SLUG);
        return PromoDetailActivity.getCallingIntent(context, slug);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.title_promo_detail));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_promo_detail;
    }

    @Override
    protected Fragment getNewFragment() {
        PromoData promoData = getIntent().getParcelableExtra(EXTRA_PROMO_DATA);

        if (promoData == null) {
            String slug = getIntent().getStringExtra(EXTRA_PROMO_SLUG);
            return PromoDetailFragment.newInstance(slug);
        } else {
            int position = getIntent().getIntExtra(EXTRA_PROMO_POSITION, 0);
            int page = getIntent().getIntExtra(EXTRA_PROMO_PAGE, 0);
            return PromoDetailFragment.newInstance(promoData, page, position);
        }
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
        if(getApplicationContext() instanceof LoyaltyModuleRouter){
            ((LoyaltyModuleRouter)getApplicationContext()).sharePromoLoyalty(this, promoData);
        }
    }
}