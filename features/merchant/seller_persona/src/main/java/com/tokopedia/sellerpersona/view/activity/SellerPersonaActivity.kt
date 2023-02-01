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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.data.local.PersonaSharedPreference
import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
import com.tokopedia.sellerpersona.databinding.ActivitySellerPersonaBinding
import com.tokopedia.sellerpersona.di.DaggerSellerPersonaComponent
import com.tokopedia.sellerpersona.di.SellerPersonaComponent
import com.tokopedia.sellerpersona.view.viewmodel.PersonaSharedViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
        fetchPersonaData()
        setWhiteStatusBar()
        observePersonaData()
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

    private fun observePersonaData() {
        observe(viewModel.personaStatus) {
            dismissLoadingState()
            when (it) {
                is Success -> setNavigationStartDestination(it.data)
                is Fail -> handleError(it.throwable)
            }
        }
    }

    private fun fetchPersonaData() {
        showLoadingState()
        viewModel.fetchPersonaData()
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
        binding?.globalErrorSellerPersona?.gone()
        val navHostFragment: NavHostFragment? = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        navHostFragment?.navController?.let { navController ->
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.nav_graph)

            val hasPersona = data.persona.isNotBlank()
            val defaultDestination = if (hasPersona) {
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

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> showGlobalError(
                GlobalError.NO_CONNECTION
            )
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                        GlobalError.NO_CONNECTION
                    )
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        showGlobalError(GlobalError.SERVER_ERROR)
                    }
                }
            }
            else -> {
                showGlobalError(GlobalError.SERVER_ERROR)
            }
        }
    }

    private fun showGlobalError(type: Int) {
        binding?.run {
            globalErrorSellerPersona.setType(type)
            globalErrorSellerPersona.setActionClickListener {
                viewModel.fetchPersonaData()
            }
            globalErrorSellerPersona.visible()
        }
    }
}