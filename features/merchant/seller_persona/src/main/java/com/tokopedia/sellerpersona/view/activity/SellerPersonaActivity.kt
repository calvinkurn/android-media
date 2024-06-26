package com.tokopedia.sellerpersona.view.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.common.SellerPersonaRemoteConfig
import com.tokopedia.sellerpersona.data.local.PersonaSharedPrefInterface
import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
import com.tokopedia.sellerpersona.databinding.ActivitySellerPersonaBinding
import com.tokopedia.sellerpersona.di.DaggerSellerPersonaComponent
import com.tokopedia.sellerpersona.di.SellerPersonaComponent
import com.tokopedia.sellerpersona.view.model.PERSONA_STATUS_NOT_ROLLED_OUT
import com.tokopedia.sellerpersona.view.viewmodel.PersonaSharedViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class SellerPersonaActivity : BaseActivity(), HasComponent<SellerPersonaComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var sharedPref: PersonaSharedPrefInterface

    @Inject
    lateinit var remoteConfig: SellerPersonaRemoteConfig

    @Inject
    lateinit var userSession: UserSessionInterface

    val openingImpressHolder by lazy { ImpressHolder() }

    private var binding: ActivitySellerPersonaBinding? = null
    private val viewModel: PersonaSharedViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PersonaSharedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        checkIsLoggedIn()
        setContentView()
        fetchPersonaData()
        setWhiteStatusBar()
        observePersonaData()
    }

    override fun getComponent(): SellerPersonaComponent {
        return DaggerSellerPersonaComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        if (sharedPref.isFirstVisit()) {
            sharedPref.setIsFirstVisit(false)
        }
        super.onDestroy()
    }

    private fun observePersonaData() {
        observe(viewModel.personaStatus) {
            dismissLoadingState()
            when (it) {
                is Success -> setNavigationStartDestination(it.data)
                is Fail -> handleError()
            }
        }
    }

    private fun fetchPersonaData() {
        showLoadingState()
        viewModel.fetchPersonaStatus()
    }

    private fun showLoadingState() {
        binding?.loaderSellerPersona?.show()
    }

    private fun dismissLoadingState() {
        binding?.loaderSellerPersona?.gone()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setContentView() {
        binding = ActivitySellerPersonaBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private fun setNavigationStartDestination(data: PersonaStatusModel) {
        if (data.status == PERSONA_STATUS_NOT_ROLLED_OUT) {
            RouteManager.route(this, ApplinkConst.HOME)
            finish()
            return
        }

        binding?.errorViewPersona?.gone()
        val navHostFragment: NavHostFragment? = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        navHostFragment?.navController?.let { navController ->
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.nav_graph)

            val defaultDestination = getDefaultDestination(data.persona)
            graph.setStartDestination(defaultDestination)

            navController.graph = graph
            setupToolbar(navController)
        }
    }

    private fun getDefaultDestination(persona: String): Int {
        val isComposeEnabled = remoteConfig.isComposeEnabled()
        val hasPersona = persona.isNotBlank()
        return when {
            isComposeEnabled && hasPersona -> R.id.composeResultFragment
            isComposeEnabled && !hasPersona -> {
                markAsPersonaFirstVisit()
                R.id.composeOpeningFragment
            }

            hasPersona -> R.id.resultFragment
            else -> {
                markAsPersonaFirstVisit()
                R.id.openingFragment
            }
        }
    }

    private fun markAsPersonaFirstVisit() {
        sharedPref.setIsFirstVisit(true)
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
            setStatusBarColor(getResColor(unifyprinciplesR.color.Unify_Background))
            setLightStatusBar(!isDarkMode())
        }
    }

    private fun handleError() {
        binding?.errorViewPersona?.visible()
        binding?.errorViewPersona?.setOnActionClicked {
            viewModel.fetchPersonaStatus()
        }
    }

    private fun checkIsLoggedIn() {
        if (!userSession.isLoggedIn) {
            RouteManager.route(this, ApplinkConstInternalUserPlatform.SEAMLESS_LOGIN)
            finish()
        }
    }
}
