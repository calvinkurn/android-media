package com.tokopedia.sellerpersona.view.activity

import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.data.local.PersonaSharedPreference
import com.tokopedia.sellerpersona.databinding.ActivitySellerPersonaBinding
import com.tokopedia.sellerpersona.di.DaggerSellerPersonaComponent
import com.tokopedia.sellerpersona.di.SellerPersonaComponent
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class SellerPersonaActivity : BaseActivity(), HasComponent<SellerPersonaComponent> {

    @Inject
    lateinit var sharedPref: PersonaSharedPreference

    private var binding: ActivitySellerPersonaBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView()
        setNavigationStartDestination()
        setWhiteStatusBar()

    }

    override fun getComponent(): SellerPersonaComponent {
        return DaggerSellerPersonaComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setContentView() {
        binding = ActivitySellerPersonaBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private fun setNavigationStartDestination() {
        val navHostFragment: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        val defaultDestination = if (sharedPref.isFirstVisit()) {
            R.id.openingFragment
        } else {
            R.id.resultFragment
        }
        graph.startDestination = defaultDestination

        navController.graph = graph
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            setLightStatusBar(true)
        }
    }
}