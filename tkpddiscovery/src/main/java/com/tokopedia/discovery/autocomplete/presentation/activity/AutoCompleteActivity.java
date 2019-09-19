package com.tokopedia.discovery.autocomplete.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.presentation.AutoCompleteContract;
import com.tokopedia.discovery.autocomplete.presentation.presenter.AutoCompletePresenter;
import com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTracking;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.graphql.data.GraphqlClient;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL;
import static com.tokopedia.discovery.common.constants.SearchConstant.FROM_APP_SHORTCUTS;

public class AutoCompleteActivity extends DiscoveryActivity
        implements AutoCompleteContract.View {

    public static Intent newInstance(Context context) {
        return new Intent(context, AutoCompleteActivity.class);
    }

    @DeepLink(ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
    public static Intent getCallingApplinkAutoCompleteSearchIntent(Context context, Bundle bundle) {
        return createIntentToAutoCompleteActivityFromBundle(context, bundle);
    }

    private static Intent createIntentToAutoCompleteActivityFromBundle(Context context, Bundle bundle) {
        SearchParameter searchParameter = createSearchParameterFromBundle(bundle);

        Intent intent = new Intent(context, AutoCompleteActivity.class);
        intent.putExtra(EXTRA_SEARCH_PARAMETER_MODEL, searchParameter);

        return intent;
    }

    private static SearchParameter createSearchParameterFromBundle(Bundle bundle) {
        String deepLinkURI = bundle.getString(DeepLink.URI);
        return new SearchParameter(deepLinkURI == null ? "" : deepLinkURI);
    }

    @Inject
    AutoCompletePresenter autoCompletePresenter;
    @Inject
    DiscoveryTracking discoveryTracking;

    private SearchComponent searchComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityOnCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void initActivityOnCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        initInjector();
    }

    private void initInjector() {
        searchComponent =
                DaggerSearchComponent.builder()
                        .appComponent(getApplicationComponent())
                        .build();
        searchComponent.inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        initPresenter();

        SearchParameter searchParameter = getSearchParameterFromIntent(intent);

        handleIntentAutoComplete(searchParameter);

        if (intent.getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            discoveryTracking.eventSearchShortcut();
        }
    }

    private void initPresenter() {
        setPresenter(autoCompletePresenter);
        autoCompletePresenter.attachView(this);
        autoCompletePresenter.setDiscoveryView(this);
    }

    private SearchParameter getSearchParameterFromIntent(Intent intent) {
        SearchParameter searchParameter = intent.getParcelableExtra(EXTRA_SEARCH_PARAMETER_MODEL);

        if (searchParameter == null) {
            searchParameter = new SearchParameter();
        }

        return searchParameter;
    }

    private void handleIntentAutoComplete(SearchParameter searchParameter) {
        searchView.showSearch(true, false, searchParameter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_auto_complete;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected void initToolbar() {
        //Overridden and left empty to avoid toolbar click listener get called
        //Autocomplete activity don't need toolbar anymore
        //cause it only need to launch discoverySearchView
    }
}
