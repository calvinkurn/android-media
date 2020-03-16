package com.tokopedia.autocomplete;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.autocomplete.analytics.AutocompleteEventTracking;
import com.tokopedia.autocomplete.analytics.AutocompleteTracking;
import com.tokopedia.autocomplete.initialstate.InitialStateFragment;
import com.tokopedia.autocomplete.initialstate.InitialStateViewUpdateListener;
import com.tokopedia.autocomplete.searchbar.SearchBarView;
import com.tokopedia.autocomplete.suggestion.SuggestionFragment;
import com.tokopedia.autocomplete.suggestion.SuggestionViewUpdateListener;
import com.tokopedia.autocomplete.util.UrlParamHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.tokopedia.discovery.common.constants.SearchConstant.FROM_APP_SHORTCUTS;

public class AutoCompleteActivity extends BaseActivity
        implements SearchBarView.OnQueryTextListener,
        SuggestionViewUpdateListener,
        InitialStateViewUpdateListener {

    public static final int PAGER_POSITION_PRODUCT = 0;
    public static final int PAGER_POSITION_SHOP = 1;

    AutocompleteTracking autocompleteTracking;

    protected SearchParameter searchParameter;

    protected SearchBarView searchBarView;

    protected ConstraintLayout mSuggestionView;
    protected ConstraintLayout mInitialStateView;

    protected SuggestionFragment suggestionFragment;
    protected InitialStateFragment initialStateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete);

        setStatusBarColor();
        proceed();
        initActivityOnCreate();
        handleIntent(getIntent());
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(getResources().getColor(R.color.white, null));
        }
    }

    private void proceed() {
        initView();
        prepareView();
    }

    protected void initView() {
        searchBarView = findViewById(R.id.autocompleteSearchBar);
        mSuggestionView = findViewById(R.id.search_suggestion_container);
        mInitialStateView = findViewById(R.id.search_initial_state_container);
        suggestionFragment = (SuggestionFragment) getSupportFragmentManager().findFragmentById(R.id.search_suggestion);
        initialStateFragment = (InitialStateFragment) getSupportFragmentManager().findFragmentById(R.id.search_initial_state);
    }

    protected void prepareView() {
        initSearchBarView();
    }

    private void initActivityOnCreate() {
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
        SearchParameter param = searchBarView.showSearch(searchParameter);
        if (suggestionFragment != null) {
            suggestionFragment.setSearchParameter(param);
            suggestionFragment.setSuggestionViewUpdateListener(this);
        }
        if (initialStateFragment != null) {
            initialStateFragment.setSearchParameter(param);
            initialStateFragment.setInitialStateViewUpdateListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean isAllowShake() {
        return false;
    }

    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(this, searchBarView);
    }

    private void initSearchBarView() {
        searchBarView.setActivity(this);
        searchBarView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(@NotNull SearchParameter searchParameter) {
        this.searchParameter = new SearchParameter(searchParameter);

        String query = searchParameter.getSearchQuery();
        AutocompleteTracking.eventClickSubmit(query);

        clearFocusSearchView();
        handleQueryTextSubmitBasedOnCurrentTab();
        return true;
    }

    private void clearFocusSearchView() {
        if (searchBarView != null) {
            searchBarView.clearFocus();
        }
    }

    private void handleQueryTextSubmitBasedOnCurrentTab() throws RuntimeException {
        switch (suggestionFragment.getCurrentTab()) {
            case PAGER_POSITION_PRODUCT:
                onProductQuerySubmit();
                break;
            case PAGER_POSITION_SHOP:
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
        RouteManager.route(this, createSearchResultApplink());
        finish();
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
    public void onQueryTextChange(@NotNull SearchParameter searchParameter) {
        if (searchParameter.getSearchQuery().isEmpty()) {
            if (initialStateFragment != null) {
                initialStateFragment.getInitialStateData(searchParameter);
            }
        } else {
            if (suggestionFragment != null) {
                suggestionFragment.search(searchParameter);
            }
        }
    }

    @Override
    public void showInitialStateView() {
        mSuggestionView.setVisibility(View.GONE);
        mInitialStateView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuggestionView() {
        mInitialStateView.setVisibility(View.GONE);
        mSuggestionView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SearchBarView.REQUEST_VOICE) {
                onVoiceSearchClicked(data);
            }
        }
    }

    public void onVoiceSearchClicked(Intent data) {
        List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (results != null && results.size() > 0) {
            searchBarView.setQuery(results.get(0), false);
            sendVoiceSearchGTM(results.get(0));
        }
    }

    public void setSearchQuery(String keyword) {
        searchBarView.setQuery(keyword, false, true);
    }
}