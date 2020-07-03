package com.tokopedia.manageaddress.ui

import android.os.Bundle
import com.example.manageaddress.R
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.manageaddress.di.DaggerManageAddressComponent
import com.tokopedia.manageaddress.di.ManageAddressComponent
import kotlinx.android.synthetic.main.activity_manage_address.*

class ManageAddressActivity : BaseActivity(), HasComponent<ManageAddressComponent>{

    override fun getComponent(): ManageAddressComponent {
        return DaggerManageAddressComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_address)
        initViews()
        component.inject(this)
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction().replace(R.id.container, ManageAddressFragment.newInstance()).commit()

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    fun setAddButtonOnClickListener(onClick: () -> Unit) {
        btn_add.setOnClickListener {
            onClick()
        }
    }
}