package com.tokopedia.autocomplete.presentation.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.analytics.AutocompleteEventTracking;
import com.tokopedia.autocomplete.analytics.AutocompleteTracking;
import com.tokopedia.autocomplete.fragment.SearchMainFragment;
import com.tokopedia.autocomplete.presentation.AutoCompleteContract;
import com.tokopedia.autocomplete.util.UrlParamHelper;
import com.tokopedia.autocomplete.view.DiscoverySearchView;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.FROM_APP_SHORTCUTS;

public class AutoCompleteActivity extends BaseActivity
        implements DiscoverySearchView.SearchViewListener,
        DiscoverySearchView.ImageSearchClickListener,
        DiscoverySearchView.OnQueryTextListener,
        AutoCompleteContract.View{

    AutocompleteTracking autocompleteTracking;

    protected DiscoverySearchView searchView;

    protected SearchParameter searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete);
        proceed();
        initActivityOnCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void proceed() {
        initView();
        prepareView();
    }

    protected void initView() {
        searchView = findViewById(R.id.search);
    }

    protected void prepareView() {
        initSearchView();
    }

    private void initActivityOnCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        autocompleteTracking = new AutocompleteTracking(new UserSession(this));
    }

    private void handleIntent(Intent intent) {

        SearchParameter searchParameter = getSearchParameterFromIntentUri(intent);

        handleIntentAutoComplete(searchParameter);

        if (intent.getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            autocompleteTracking.eventSearchShortcut();
        }
    }

    private SearchParameter getSearchParameterFromIntentUri(Intent intent) {
        Uri uri = intent.getData();

        SearchParameter searchParameter = (uri == null) ? new SearchParameter() : new SearchParameter(uri.toString());
        searchParameter.cleanUpNullValuesInMap();

        return searchParameter;
    }

    private void handleIntentAutoComplete(SearchParameter searchParameter) {
        searchView.showSearch(searchParameter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();
    }

    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(this, searchView);
    }

    private void initSearchView() {
        searchView.setActivity(this);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchViewListener(this);
        searchView.setOnImageSearchClickListener(this);
    }

    protected void setLastQuerySearchView(String lastQuerySearchView) {
        searchView.setLastQuery(lastQuerySearchView);
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {

    }

    @Override
    public boolean onQueryTextSubmit(SearchParameter searchParameter) {
        this.searchParameter = new SearchParameter(searchParameter);

        String query = searchParameter.getSearchQuery();
        AutocompleteTracking.eventClickSubmit(query);

        clearFocusSearchView();
        handleQueryTextSubmitBasedOnCurrentTab();

        return true;
    }

    private void clearFocusSearchView() {
        if(searchView != null) {
            searchView.clearFocus();
        }
    }

    private void handleQueryTextSubmitBasedOnCurrentTab() throws RuntimeException {
        switch (searchView.getSuggestionFragment().getCurrentTab()) {
            case SearchMainFragment.PAGER_POSITION_PRODUCT:
                onProductQuerySubmit();
                break;
            case SearchMainFragment.PAGER_POSITION_SHOP:
                onShopQuerySubmit();
                break;
            default:
                throw new RuntimeException("Please handle this function if you have new tab of suggestion search view.");
        }
    }

    protected void onProductQuerySubmit() {
        setActiveTabForSearchPage(SearchConstant.ActiveTab.PRODUCT);
        moveToSearchPage();
    }

    private void onShopQuerySubmit() {
        setActiveTabForSearchPage(SearchConstant.ActiveTab.SHOP);
        moveToSearchPage();
    }

    private void setActiveTabForSearchPage(String activeTab) {
        searchParameter.getSearchParameterHashMap().put(SearchApiConst.ACTIVE_TAB, activeTab);
    }

    private void moveToSearchPage() {
        Intent searchActivityIntent = createIntentToSearchResult();

        startActivity(searchActivityIntent);
        finish();
    }

    private Intent createIntentToSearchResult() {
        return RouteManager.getIntent(this, createSearchResultApplink());
    }

    private String createSearchResultApplink() {
        return ApplinkConstInternalDiscovery.SEARCH_RESULT
                + "?"
                + UrlParamHelper.generateUrlParamString(searchParameter.getSearchParameterHashMap());
    }

    private void sendVoiceSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            eventDiscoveryVoiceSearch(keyword);
        }
    }

    public void eventDiscoveryVoiceSearch(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AutocompleteEventTracking.Event.SEARCH,
                AutocompleteEventTracking.Category.SEARCH,
                AutocompleteEventTracking.Action.VOICE_SEARCH,
                label);
    }

    @Override
    public boolean onQueryTextChange(String searchQuery) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DiscoverySearchView.REQUEST_VOICE:
                    List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && results.size() > 0) {
                        searchView.setQuery(results.get(0), false);
                        sendVoiceSearchGTM(results.get(0));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onImageSearchClicked() {
        RouteManager.route(this, ApplinkConstInternalDiscovery.IMAGE_SEARCH_RESULT);
    }

    public void setSearchQuery(String keyword) {
        searchView.setQuery(keyword, false, true);
    }

    public void deleteAllRecentSearch() {
        searchView.getSuggestionFragment().deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword) {
        searchView.getSuggestionFragment().deleteRecentSearch(keyword);
    }
}
