package com.tokopedia.home_explore_category.presentation.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_explore_category.analytic.ECConstants.Companion.EXTRA_TITLE
import com.tokopedia.home_explore_category.analytic.ECConstants.Companion.EXTRA_TYPE
import com.tokopedia.home_explore_category.analytic.ECConstants.Companion.TYPE_LAYANAN
import com.tokopedia.home_explore_category.di.ExploreCategoryComponent
import com.tokopedia.home_explore_category.presentation.viewmodel.ExploreCategoryViewModel
import javax.inject.Inject

class ExploreCategoryActivity : BaseActivity(), HasComponent<ExploreCategoryComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(ExploreCategoryViewModel::class.java)
    }

    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        handleIntentFromDeeplink()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun handleIntentFromDeeplink() {
        val data = intent.data

        if (data?.getQueryParameter(EXTRA_TITLE) == null) {
           if (data?.getQueryParameter(EXTRA_TYPE)?.toInt() == TYPE_LAYANAN) {
                title = TITLE_LAYANAN
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun getComponent(): ExploreCategoryComponent {
        return DaggerExploreCategoryComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    companion object {
        const val TITLE_LAYANAN = "Jelajah Tokopedia"
    }

}
