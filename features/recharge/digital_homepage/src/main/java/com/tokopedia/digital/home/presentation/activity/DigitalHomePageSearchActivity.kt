package com.tokopedia.digital.home.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponentInstance
import com.tokopedia.digital.home.presentation.fragment.DigitalHomePageSearchFragment
import com.tokopedia.digital.home.presentation.fragment.DigitalHomepageSearchByDynamicIconsFragment
import com.tokopedia.graphql.data.GraphqlClient

class DigitalHomePageSearchActivity : BaseSimpleActivity(), HasComponent<RechargeHomepageComponent> {

    private lateinit var rechargeHomepageComponent: RechargeHomepageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        GraphqlClient.init(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val platformId = bundle?.getInt(PARAM_PLATFORM_ID)
        val enablePersonalize = bundle?.getBoolean(PARAM_ENABLE_PERSONALIZE) ?: true
        val sectionId = bundle?.getStringArrayList(PARAM_SECTION_ID) ?: arrayListOf()
        val searchBarPlaceHolder = bundle?.getString(PARAM_SEARCH_BAR_PLACE_HOLDER, "") ?: ""

        return if (platformId != null && sectionId.isNotEmpty()) {
            DigitalHomepageSearchByDynamicIconsFragment.newInstance(platformId, enablePersonalize, sectionId, searchBarPlaceHolder)
        } else {
            DigitalHomePageSearchFragment.getInstance()
        }
    }

    override fun getComponent(): RechargeHomepageComponent {
        if (!::rechargeHomepageComponent.isInitialized) {
            rechargeHomepageComponent = RechargeHomepageComponentInstance.getRechargeHomepageComponent(application)
        }
        return rechargeHomepageComponent
    }

    companion object {
        private const val PARAM_PLATFORM_ID = "platform_id"
        private const val PARAM_ENABLE_PERSONALIZE = "personalize"
        private const val PARAM_SECTION_ID = "section_id"
        private const val PARAM_SEARCH_BAR_PLACE_HOLDER = "search_bar_place_holder"

        fun getCallingIntent(context: Context): Intent = Intent(context, DigitalHomePageSearchActivity::class.java)

        fun getCallingIntent(context: Context, platformID: Int,
                             enablePersonalize: Boolean = true, sectionId: ArrayList<String>,
                             searchBarPlaceHolder: String): Intent {
            val intent = Intent(context, DigitalHomePageSearchActivity::class.java)
            intent.putExtra(PARAM_PLATFORM_ID, platformID)
            intent.putExtra(PARAM_ENABLE_PERSONALIZE, enablePersonalize)
            intent.putStringArrayListExtra(PARAM_SECTION_ID, sectionId)
            intent.putExtra(PARAM_SEARCH_BAR_PLACE_HOLDER, searchBarPlaceHolder)
            return intent
        }
    }
}