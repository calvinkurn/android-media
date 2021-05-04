package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.DaggerAddNewAddressRevampComponent
import kotlinx.android.synthetic.main.activity_search_address.*

class SearchPageActivity : BaseActivity(), HasComponent<AddNewAddressRevampComponent> {

    override fun getComponent(): AddNewAddressRevampComponent {
        return DaggerAddNewAddressRevampComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_address)
//        initViews()
        component.inject(this)
    }

  /*  private fun initViews() {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, SearchPageFragment.newInstance(bundle)).commit()
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }*/

}