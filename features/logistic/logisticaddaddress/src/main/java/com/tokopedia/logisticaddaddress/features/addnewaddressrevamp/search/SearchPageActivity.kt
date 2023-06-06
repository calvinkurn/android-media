package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.databinding.ActivitySearchAddressBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class SearchPageActivity : BaseActivity(), HasComponent<AddNewAddressRevampComponent> {

    private var isEdit: Boolean? = false
    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }

    private var binding: ActivitySearchAddressBinding? = null

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initViews()
        component.inject(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isEdit == true) {
            EditAddressRevampAnalytics.onClickBackArrowSearch(userSession.userId)
        } else {
            AddNewAddressRevampAnalytics.onClickBackArrowSearch(userSession.userId)
        }
    }

    private fun initViews() {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            val extra = intent.extras
            isEdit = extra?.getBoolean(AddressConstants.EXTRA_IS_EDIT)
            extra?.getString(EXTRA_REF).let { from ->
                if (isEdit == false) {
                    AddNewAddressRevampAnalytics.sendScreenName(from)
                }
            }
            bundle.putAll(intent.extras)
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, SearchPageFragment.newInstance(bundle)).commit()
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_REF = "EXTRA_REF"
    }
}
