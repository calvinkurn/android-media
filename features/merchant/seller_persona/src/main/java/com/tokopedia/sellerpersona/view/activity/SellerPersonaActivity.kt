package com.tokopedia.sellerpersona.view.activity

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.data.local.PersonaSharedPreference
import com.tokopedia.sellerpersona.databinding.ActivitySellerPersonaBinding
import com.tokopedia.sellerpersona.di.DaggerSellerPersonaComponent
import com.tokopedia.sellerpersona.di.SellerPersonaComponent
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.sellerpersona.view.viewmodel.PersonaSharedViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class SellerPersonaActivity : BaseActivity(), HasComponent<SellerPersonaComponent> {

    @Inject
    lateinit var sharedPref: PersonaSharedPreference

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding: ActivitySellerPersonaBinding? = null
    private val viewModel: PersonaSharedViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PersonaSharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView()
        setWhiteStatusBar()
        fetchPersonaData()
    }

    private fun fetchPersonaData() {
        viewModel.fetchPersonaData()
        observe(viewModel.personaData) {
            when (it) {
                is Success -> setNavigationStartDestination(it.data)
                is Fail -> showErrorState(it.throwable)
            }
        }
    }

    override fun getComponent(): SellerPersonaComponent {
        return DaggerSellerPersonaComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setContentView() {
        binding = ActivitySellerPersonaBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private fun setNavigationStartDestination(persona: PersonaDataUiModel) {
        val navHostFragment: NavHostFragment? = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        navHostFragment?.navController?.let { navController ->
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.nav_graph)

            val defaultDestination = if (persona.personaStatus == PersonaStatus.ACTIVE) {
                R.id.resultFragment
            } else {
                R.id.openingFragment
            }
            graph.startDestination = defaultDestination

            navController.graph = graph
            setupToolbar(navController)
        }
    }

    private fun setupToolbar(navController: NavController) {
        binding?.headerSellerPersona?.let {
            it.setupWithNavController(navController)
            setSupportActionBar(it)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                it.isShowBackButton = true
            }
        }
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            setLightStatusBar(true)
        }
    }

    private fun showErrorState(throwable: Throwable) {

    }
}