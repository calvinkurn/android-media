package com.tokopedia.opportunity.snapshot;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.opportunity.snapshot.listener.SnapShotProductListener;
import com.tokopedia.opportunity.snapshot.presenter.SnapShotProductImpl;
import com.tokopedia.opportunity.snapshot.presenter.SnapShotProductPresenter;

/**
 * Created by hangnadi on 2/28/17.
 */
public class SnapShotProduct extends BasePresenterActivity<SnapShotProductPresenter>
    implements SnapShotProductListener {

    private static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_OPPORTUNITY_ID = "opportunity_id";


    private Uri uriData;
    private Bundle bundleData;

    public static Intent createIntent(Context context, String productId, String opportunityId) {
        Intent intent = new Intent(context, SnapShotProduct.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PRODUCT_ID, productId);
        bundle.putString(PARAM_OPPORTUNITY_ID, opportunityId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new SnapShotProductImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_info_fragmented;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        presenter.initialFragment(this, uriData, bundleData);
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(com.tokopedia.core.R.id.container, fragment, tag);
            fragmentTransaction.commit();
        }
    }
}
