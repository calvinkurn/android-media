package com.tokopedia.autocompletecomponent.suggestion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.autocompletecomponent.createFakeBaseAppComponent
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment.Companion.SUGGESTION_FRAGMENT_TAG
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment.SuggestionViewUpdateListener
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionViewListenerModule
import com.tokopedia.autocompletecomponent.test.R
import com.tokopedia.discovery.common.constants.SearchApiConst

class SuggestionActivityTest:
    AppCompatActivity(),
    SuggestionViewUpdateListener {

    private lateinit var suggestionFragment: SuggestionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion)

        val component = DaggerSuggestionTestComponent
            .builder()
            .baseAppComponent(getBaseAppComponent())
            .suggestionViewListenerModule(SuggestionViewListenerModule(this))
            .build()

        suggestionFragment = SuggestionFragment.create(component)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.suggestionFragmentContainer,
                suggestionFragment,
                SUGGESTION_FRAGMENT_TAG
            ).commit()
    }

    override fun onStart() {
        super.onStart()

        suggestionFragment.getSuggestion(
            mapOf(SearchApiConst.Q to "samsung"),
            SearchBarKeyword(keyword = "samsung")
        )
    }

    override fun showSuggestionView() {

    }

    override fun setSearchQuery(keyword: String) {

    }

    private fun getBaseAppComponent() = createFakeBaseAppComponent(this)

    override fun finish() { }
}
