package com.tokopedia.autocomplete;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.tokopedia.discovery.common.constants.SearchConstant.FROM_APP_SHORTCUTS;
import static com.tokopedia.utils.view.DarkModeUtil.isDarkMode;

public class AutoCompleteActivity extends BaseActivity
        implements SearchBarView.OnQueryTextListener,
        SuggestionViewUpdateListener,
        InitialStateViewUpdateListener {

    AutocompleteTracking autocompleteTracking;

    protected SearchParameter searchParameter;

    protected SearchBarView searchBarView;

    protected ConstraintLayout mSuggestionView;
    protected ConstraintLayout mInitialStateView;

    protected SuggestionFragment suggestionFragment;
    protected InitialStateFragment initialStateFragment;

    private String baseSRPApplink = "";

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!isDarkMode(this)) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
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
        baseSRPApplink = getBaseSRPApplink(searchParameter);
        removeBaseSRPApplink(searchParameter);

        handleIntentAutoComplete(searchParameter);

        if (intent.getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            autocompleteTracking.eventSearchShortcut();
        }
    }

    private String getBaseSRPApplink(SearchParameter searchParameter) {
        return searchParameter.get(SearchApiConst.BASE_SRP_APPLINK);
    }

    private void removeBaseSRPApplink(SearchParameter searchParameter) {
        searchParameter.remove(SearchApiConst.BASE_SRP_APPLINK);
    }

    private SearchParameter getSearchParameterFromIntentUri(Intent intent) {
        Uri uri = intent.getData();

        SearchParameter searchParameter = (uri == null) ? new SearchParameter() : new SearchParameter(uri.toString());
        searchParameter.cleanUpNullValuesInMap();

        return searchParameter;
    }

    private void handleIntentAutoComplete(SearchParameter searchParameter) {
        //need to get user_warehouseId from chooseAddress later
        String warehouseId = "19926";

        searchBarView.showSearch(searchParameter);

        if (suggestionFragment != null) {
            suggestionFragment.setSearchParameter(searchParameter.getSearchParameterHashMap());
            suggestionFragment.setWarehouseId(warehouseId);
            suggestionFragment.setSuggestionViewUpdateListener(this);
        }
        if (initialStateFragment != null) {
            initialStateFragment.setSearchParameter(searchParameter.getSearchParameterHashMap());
            initialStateFragment.setWarehouseId(warehouseId);
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

    @Override
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
        moveToSearchPage();
        return true;
    }

    private void clearFocusSearchView() {
        if (searchBarView != null) {
            searchBarView.clearFocus();
        }
    }

    private void moveToSearchPage() throws RuntimeException{
        RouteManager.route(this, createSearchResultApplink());
        finish();
    }

    private String createSearchResultApplink() {
        if (baseSRPApplink.isEmpty()) {
            return ApplinkConstInternalDiscovery.SEARCH_RESULT
                    + "?"
                    + UrlParamHelper.generateUrlParamString(searchParameter.getSearchParameterHashMap());
        }
        else {
            return baseSRPApplink
                    + "?"
                    + UrlParamHelper.generateUrlParamString(searchParameter.getSearchParameterHashMap());
        }
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
                initialStateFragment.getInitialStateData(searchParameter.getSearchParameterHashMap());
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

    @Override
    public void setSearchQuery(@NotNull String keyword) {
        searchBarView.setQuery(keyword, false, true);
    }

    @Override
    public void setIsTyping(boolean isTyping) {
        suggestionFragment.setIsTyping(isTyping);
    }
}