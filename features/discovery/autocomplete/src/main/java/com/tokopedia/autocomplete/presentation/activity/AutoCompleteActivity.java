package com.tokopedia.autocomplete.presentation.activity;

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
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.analytics.AutocompleteEventTracking;
import com.tokopedia.autocomplete.analytics.AutocompleteTracking;
import com.tokopedia.autocomplete.fragment.SearchMainFragment;
import com.tokopedia.autocomplete.presentation.AutoCompleteContract;
import com.tokopedia.autocomplete.util.UrlParamHelper;
import com.tokopedia.autocomplete.view.SearchBarView;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.tokopedia.discovery.common.constants.SearchConstant.FROM_APP_SHORTCUTS;

public class AutoCompleteActivity extends BaseActivity
        implements SearchBarView.ImageSearchClickListener,
        SearchBarView.OnQueryTextListener,
        SearchBarView.SuggestionViewUpdateListener,
        AutoCompleteContract.View {

    AutocompleteTracking autocompleteTracking;

    protected SearchBarView searchBarView;

    protected SearchParameter searchParameter;

    protected ConstraintLayout mSuggestionView;

    protected SearchMainFragment suggestionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete);

        setStatusBarColor();
        proceed();
        initActivityOnCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }
    }

    private void proceed() {
        initView();
        prepareView();
    }

    protected void initView() {
        searchBarView = findViewById(R.id.search_bar);
        mSuggestionView = findViewById(R.id.search_suggestion_container);
        suggestionFragment = (SearchMainFragment) getSupportFragmentManager().findFragmentById(R.id.search_suggestion);
    }

    protected void prepareView() {
        setSuggestionViewAnimation();
        initSearchBarView();
    }

    private void setSuggestionViewAnimation() {
        TransitionSet transitionSet = new TransitionSet();
        Fade fade = new Fade(Fade.MODE_IN);
        transitionSet.addTransition(fade);
        TransitionManager.beginDelayedTransition(mSuggestionView, fade);
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
        searchBarView.showSearch(searchParameter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();
    }

    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(this, searchBarView);
    }

    private void initSearchBarView() {
        searchBarView.setActivity(this);
        searchBarView.setOnImageSearchClickListener(this);
        searchBarView.setOnQueryTextListener(this);
        searchBarView.setOnSuggestionViewUpdateListener(this);
    }

    protected void setLastQuerySearchView(String lastQuerySearchView) {
        searchBarView.setLastQuery(lastQuerySearchView);
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
    public boolean onQueryTextChange(@NotNull String searchQuery) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SearchBarView.REQUEST_VOICE:
                    List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && results.size() > 0) {
                        searchBarView.setQuery(results.get(0), false);
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
        searchBarView.setQuery(keyword, false, true);
    }

    public void deleteAllRecentSearch() {
        suggestionFragment.deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword) {
        suggestionFragment.deleteRecentSearch(keyword);
    }

    @Override
    public void onTextChanged(@Nullable CharSequence newText, @Nullable CharSequence mOldQueryText, @NotNull SearchParameter searchParameter) {
        if (suggestionFragment != null && !TextUtils.equals(newText, mOldQueryText)) {
            suggestionFragment.search(searchParameter);
        }
    }

    @Override
    public void showSuggestions() {
        if (suggestionFragment != null && mSuggestionView.getVisibility() == View.GONE) {
            mSuggestionView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismissSuggestions() {
        if (mSuggestionView.getVisibility() == View.VISIBLE) {
            mSuggestionView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSearchParameter(@NotNull SearchParameter searchParameter) {
        if (suggestionFragment != null) {
            suggestionFragment.setSearchParameter(searchParameter);
        }
    }
}
