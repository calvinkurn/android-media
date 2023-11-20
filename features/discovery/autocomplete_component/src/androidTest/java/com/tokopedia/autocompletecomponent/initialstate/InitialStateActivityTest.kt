package com.tokopedia.autocompletecomponent.initialstate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.autocompletecomponent.createFakeBaseAppComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateViewListenerModule
import com.tokopedia.autocompletecomponent.test.R

class InitialStateActivityTest:
        AppCompatActivity(),
        InitialStateFragment.InitialStateViewUpdateListener {

    private lateinit var initialStateFragment: InitialStateFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_state)

        val component = DaggerInitialStateTestComponent
            .builder()
            .baseAppComponent(getBaseAppComponent())
            .initialStateViewListenerModule(InitialStateViewListenerModule(this))
            .build()

        initialStateFragment = InitialStateFragment.create(component)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.initialStateContainer,
                initialStateFragment,
                InitialStateFragment.INITIAL_STATE_FRAGMENT_TAG
            ).commit()
    }

    override fun onStart() {
        super.onStart()

        initialStateFragment.show(mapOf())
    }

    override fun showInitialStateView() {

    }

    private fun getBaseAppComponent() = createFakeBaseAppComponent(this)

    override fun finish() { }
}
